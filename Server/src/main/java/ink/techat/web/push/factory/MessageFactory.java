package ink.techat.web.push.factory;

import ink.techat.web.push.bean.api.message.MessageCreateModel;
import ink.techat.web.push.bean.db.Group;
import ink.techat.web.push.bean.db.Message;
import ink.techat.web.push.bean.db.User;
import ink.techat.web.push.utils.Hib;

/**
 * ��Ϣ���ݴ������
 * @author NickCharlie
 */
public class MessageFactory {
    public static Message findById(String id){
        return Hib.query(session -> session.get(Message.class, id));
    }

    /**
     * ���һ����ͨ��Ϣ
     * @param sender ������
     * @param receiver ������
     * @param model ��Ϣmodel
     * @return Message
     */
    public static Message add(User sender, User receiver, MessageCreateModel model){
        Message message = new Message(sender, receiver, model);
        return save(message);
    }

    /**
     * ���һ��Ⱥ��Ϣ
     * @param sender ������
     * @param receiver ������
     * @param model ��Ϣmodel
     * @return Message
     */
    public static Message add(User sender, Group receiver, MessageCreateModel model){
        Message message = new Message(sender, receiver, model);
        return save(message);
    }

    /**
     * ����Ϣ���浽���ݿ����ٲ�ѯ����
     * ��֤��ȡ������������Ϣ
     * @param message Message
     * @return Message
     */
    private static Message save(Message message){
        return Hib.query(session -> {
            session.save(message);
            // д�뵽���ݿ�
            session.flush();
            // �����ݿ��в�ѯ����
            session.refresh(message);
            return message;
        });
    }
}
