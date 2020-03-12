package ink.techat.client.factory.data.message;


import ink.techat.client.factory.model.card.MessageCard;

/**
 * 消息中心, 进行消息卡片的消费
 * @author NickCharlie
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
