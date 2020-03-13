package ink.techat.client.factory.data.group;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import ink.techat.client.factory.data.helper.DbHelper;
import ink.techat.client.factory.data.helper.UserHelper;
import ink.techat.client.factory.data.message.MessageDispatcher;
import ink.techat.client.factory.model.card.GroupCard;
import ink.techat.client.factory.model.card.GroupMemberCard;
import ink.techat.client.factory.model.db.Group;
import ink.techat.client.factory.model.db.GroupMember;
import ink.techat.client.factory.model.db.User;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class GroupDispatcher implements GroupCenter {

    private static GroupCenter INSTANCE;
    private final Executor executor = newFixedThreadPool(2);
    public static GroupCenter instance(){
        if (INSTANCE != null){
            return INSTANCE;
        }
        synchronized (MessageDispatcher.class){
            if (INSTANCE == null){
                INSTANCE = new GroupDispatcher();
            }
        }
        return INSTANCE;
    }

    @Override
    public void dispatch(GroupCard... cards) {
        if (cards != null && cards.length > 0){
            // 丢到单线程池中
            executor.execute(new GroupCardHandler(cards));
        }
    }

    @Override
    public void dispatch(GroupMemberCard... cards) {
        if (cards != null && cards.length > 0){
            // 丢到单线程池中
            executor.execute(new GroupMemberCardHandler(cards));
        }
    }

    /**
     * 群组卡片的线程调度处理会触发run方法
     */
    private class GroupCardHandler implements Runnable{
        private final GroupCard[] cards;

        GroupCardHandler(GroupCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Group> groups = new ArrayList<>();
            for (GroupCard card : cards) {
                User owner = UserHelper.search(card.getOwnerId());
                if (owner != null) {
                    Group group = card.build(owner);
                    groups.add(group);
                }
            }
            if (groups.size() > 0) {
                DbHelper.save(Group.class, groups.toArray(new Group[0]));
            }
        }
    }

    /**
     * 群成员卡片的线程调度处理会触发run方法
     */
    private class GroupMemberCardHandler implements Runnable{
        private final GroupMemberCard[] cards;

        GroupMemberCardHandler(GroupMemberCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<GroupMember> members = new ArrayList<>();
            // 搜索群成员对应的人的信息
            for (GroupMemberCard model : cards) {
                // 群成员对应的群的信息
                User user = UserHelper.search(model.getUserId());
                Group group = GroupHelper.find(model.getGroupId());
                if (user != null && group != null) {
                    GroupMember member = model.build(group, user);
                    members.add(member);
                }
            }
            if (members.size() > 0) {
                DbHelper.save(GroupMember.class, members.toArray(new GroupMember[0]));
            }
        }
    }
}
