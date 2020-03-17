package ink.techat.web.push.factory;

import ink.techat.web.push.bean.api.message.MessageCreateModel;
import ink.techat.web.push.bean.db.Group;
import ink.techat.web.push.bean.db.Message;
import ink.techat.web.push.bean.db.User;
import ink.techat.web.push.utils.Hib;

/**
 * 消息数据处理的类
 * @author NickCharlie
 */
public class MessageFactory {
    public static Message findById(String id){
        return Hib.query(session -> session.get(Message.class, id));
    }

    /**
     * 添加一条普通消息
     * @param sender 发送者
     * @param receiver 接收者
     * @param model 消息model
     * @return Message
     */
    public static Message add(User sender, User receiver, MessageCreateModel model){
        Message message = new Message(sender, receiver, model);
        return save(message);
    }

    /**
     * 添加一条群消息
     * @param sender 发送者
     * @param receiver 接收者
     * @param model 消息model
     * @return Message
     */
    public static Message add(User sender, Group receiver, MessageCreateModel model){
        Message message = new Message(sender, receiver, model);
        return save(message);
    }

    /**
     * 将消息储存到数据库中再查询出来
     * 保证获取到的是最新消息
     * @param message Message
     * @return Message
     */
    private static Message save(Message message){
        return Hib.query(session -> {
            session.save(message);
            // 写入到数据库
            session.flush();
            // 从数据库中查询出来
            session.refresh(message);
            return message;
        });
    }
}
