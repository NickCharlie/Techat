package ink.techat.web.push.factory;

import com.google.common.base.Strings;
import ink.techat.web.push.bean.api.base.PushModel;
import ink.techat.web.push.bean.card.MessageCard;
import ink.techat.web.push.bean.db.*;
import ink.techat.web.push.utils.Hib;
import ink.techat.web.push.utils.PushDispatcher;
import ink.techat.web.push.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息粗糙与处理的工具类
 *
 * @author NickCharlie
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "MismatchedQueryAndUpdateOfCollection", "SameParameterValue"})
public class PushFactory {

    /**
     * 发送一条消息, 并且在当前的发送历史记录中存储
     *
     * @param sender  发送者
     * @param message Message
     */
    public static void pushNewMessage(User sender, Message message) {
        if (sender == null || message == null) {
            return;
        }
        // 消息卡片, 用于发送
        MessageCard card = new MessageCard(message);
        String entity = TextUtil.toJson(card);
        // 推送者
        PushDispatcher dispatcher = new PushDispatcher();

        if (message.getReceiver() != null && !Strings.isNullOrEmpty(message.getReceiverId())) {
            // 给联系人发送消息
            User receiver = UserFactory.findById(message.getReceiverId());
            if (receiver == null) {
                return;
            }
            // 存储消息历史记录
            PushHistory history = new PushHistory();
            history.setEntityType(PushModel.ENTITY_TYPE_MESSAGE);
            history.setEntity(entity);
            history.setSenderId(sender.getId());
            history.setReceiver(receiver);
            history.setReceiverId(receiver.getId());
            history.setReceiverPushId(receiver.getPushId());

            // 推送消息的真实Model
            PushModel pushModel = new PushModel();
            // 每一条历史记录都是独立的, 可以单独发送
            pushModel.add(history.getEntityType(), history.getEntity());
            // 把需要发送的数据丢给推送者进行消息消息推送
            dispatcher.add(receiver, pushModel);
            // 保存历史消息到数据库
            Hib.queryOnly(session -> session.save(history));
        } else if (message.getGroup() != null && !Strings.isNullOrEmpty(message.getGroupId())) {
            // 给群发消息
            Group group = message.getGroup();
            // 因为延迟加载, group有可能为Null, 需要通过Id查询
            if (group == null){
                group = GroupFactory.findById(message.getGroupId());
            }
            // 如果群真的没有, 则返回
            if (group == null){
                return;
            }
            Set<GroupMember> members = GroupFactory.getMembers(group);
            if (members == null || members.size() <= 0){
                return;
            }
            // 过滤我自己
            members.stream().filter(groupMember ->
                    !groupMember.getUserId().equalsIgnoreCase(sender.getId()))
                    .collect(Collectors.toSet());

            if (members.size() <= 0){
                return;
            }

            List<PushHistory> historyList = new ArrayList<>();

            /*
                dispatcher 消息推送的推送者
                histories  数据库要储存的列表
                members    所有的成员
                entity     消息内容构造
                PushModel.ENTITY_TYPE_MESSAGE 消息推送的类型
             */
            addGroupMembersPushModel(
                    dispatcher,
                    historyList,
                    members,
                    entity,
                    PushModel.ENTITY_TYPE_MESSAGE);

            // 保存的数据库的操作
            Hib.queryOnly(session -> {
                for (PushHistory history : historyList) {
                    session.saveOrUpdate(history);
                }
                return null;
            });
        } else {
            return;
        }
        // 推送消息提交
        dispatcher.submit();
    }

    /**
     * 给群成员构建一个消息, 把消息存储到数据库的历史记录中
     * 每个人, 每条消息都是一个记录
     * @param dispatcher 消息推送的推送者
     * @param historyList 数据库要储存的列表
     * @param members 所有的成员
     * @param entity 消息内容构造
     * @param entityTypeMessage 消息推送的类型
     */
    private static void addGroupMembersPushModel(PushDispatcher dispatcher, List<PushHistory> historyList,
                                                 Set<GroupMember> members, String entity, int entityTypeMessage) {
        for (GroupMember member : members) {
            // GroupMember中的User为急加载, 无需通过Id查找用户
            User receiver = member.getUser();

            if (receiver == null){
                return;
            }

            // 建立历史消息字段
            PushHistory history = new PushHistory();
            history.setEntityType(entityTypeMessage);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverId(receiver.getId());
            history.setReceiverPushId(receiver.getPushId());
            historyList.add(history);

            // 构建一个消息Model
            PushModel pushModel = new PushModel();
            pushModel.add(history.getEntityType(), history.getEntity());

            // 添加到推送者的数据集
            dispatcher.add(receiver, pushModel);
        }
    }
}
