package ink.techat.client.factory.data.group;

import java.util.concurrent.Executor;

import ink.techat.client.factory.data.message.MessageDispatcher;
import ink.techat.client.factory.model.card.GroupCard;
import ink.techat.client.factory.model.card.GroupMemberCard;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class GroupDispatcher implements GroupCenter {

    private static GroupCenter INSTANCE;
    private final Executor executor = newFixedThreadPool(1);
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

        }
    }
}
