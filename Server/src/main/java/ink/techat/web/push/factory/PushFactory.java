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
 * ��Ϣ�ֲ��봦��Ĺ�����
 *
 * @author NickCharlie
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "MismatchedQueryAndUpdateOfCollection", "SameParameterValue"})
public class PushFactory {

    /**
     * ����һ����Ϣ, �����ڵ�ǰ�ķ�����ʷ��¼�д洢
     *
     * @param sender  ������
     * @param message Message
     */
    public static void pushNewMessage(User sender, Message message) {
        if (sender == null || message == null) {
            return;
        }
        // ��Ϣ��Ƭ, ���ڷ���
        MessageCard card = new MessageCard(message);
        String entity = TextUtil.toJson(card);
        // ������
        PushDispatcher dispatcher = new PushDispatcher();

        if (message.getReceiver() != null && !Strings.isNullOrEmpty(message.getReceiverId())) {
            // ����ϵ�˷�����Ϣ
            User receiver = UserFactory.findById(message.getReceiverId());
            if (receiver == null) {
                return;
            }
            // �洢��Ϣ��ʷ��¼
            PushHistory history = new PushHistory();
            history.setEntityType(PushModel.ENTITY_TYPE_MESSAGE);
            history.setEntity(entity);
            history.setSenderId(sender.getId());
            history.setReceiver(receiver);
            history.setReceiverId(receiver.getId());
            history.setReceiverPushId(receiver.getPushId());

            // ������Ϣ����ʵModel
            PushModel pushModel = new PushModel();
            // ÿһ����ʷ��¼���Ƕ�����, ���Ե�������
            pushModel.add(history.getEntityType(), history.getEntity());
            // ����Ҫ���͵����ݶ��������߽�����Ϣ��Ϣ����
            dispatcher.add(receiver, pushModel);
            // ������ʷ��Ϣ�����ݿ�
            Hib.queryOnly(session -> session.save(history));
        } else if (message.getGroup() != null && !Strings.isNullOrEmpty(message.getGroupId())) {
            // ��Ⱥ����Ϣ
            Group group = message.getGroup();
            // ��Ϊ�ӳټ���, group�п���ΪNull, ��Ҫͨ��Id��ѯ
            if (group == null){
                group = GroupFactory.findById(message.getGroupId());
            }
            // ���Ⱥ���û��, �򷵻�
            if (group == null){
                return;
            }
            Set<GroupMember> members = GroupFactory.getMembers(group);
            if (members == null || members.size() <= 0){
                return;
            }
            // �������Լ�
            members.stream().filter(groupMember ->
                    !groupMember.getUserId().equalsIgnoreCase(sender.getId()))
                    .collect(Collectors.toSet());

            if (members.size() <= 0){
                return;
            }

            List<PushHistory> historyList = new ArrayList<>();

            /*
                dispatcher ��Ϣ���͵�������
                histories  ���ݿ�Ҫ������б�
                members    ���еĳ�Ա
                entity     ��Ϣ���ݹ���
                PushModel.ENTITY_TYPE_MESSAGE ��Ϣ���͵�����
             */
            addGroupMembersPushModel(
                    dispatcher,
                    historyList,
                    members,
                    entity,
                    PushModel.ENTITY_TYPE_MESSAGE);

            // ��������ݿ�Ĳ���
            Hib.queryOnly(session -> {
                for (PushHistory history : historyList) {
                    session.saveOrUpdate(history);
                }
                return null;
            });
        } else {
            return;
        }
        // ������Ϣ�ύ
        dispatcher.submit();
    }

    /**
     * ��Ⱥ��Ա����һ����Ϣ, ����Ϣ�洢�����ݿ����ʷ��¼��
     * ÿ����, ÿ����Ϣ����һ����¼
     * @param dispatcher ��Ϣ���͵�������
     * @param historyList ���ݿ�Ҫ������б�
     * @param members ���еĳ�Ա
     * @param entity ��Ϣ���ݹ���
     * @param entityTypeMessage ��Ϣ���͵�����
     */
    private static void addGroupMembersPushModel(PushDispatcher dispatcher, List<PushHistory> historyList,
                                                 Set<GroupMember> members, String entity, int entityTypeMessage) {
        for (GroupMember member : members) {
            // GroupMember�е�UserΪ������, ����ͨ��Id�����û�
            User receiver = member.getUser();

            if (receiver == null){
                return;
            }

            // ������ʷ��Ϣ�ֶ�
            PushHistory history = new PushHistory();
            history.setEntityType(entityTypeMessage);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverId(receiver.getId());
            history.setReceiverPushId(receiver.getPushId());
            historyList.add(history);

            // ����һ����ϢModel
            PushModel pushModel = new PushModel();
            pushModel.add(history.getEntityType(), history.getEntity());

            // ��ӵ������ߵ����ݼ�
            dispatcher.add(receiver, pushModel);
        }
    }
}
