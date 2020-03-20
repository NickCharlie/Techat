package ink.techat.client.factory.model.api.message;

import java.util.Date;
import java.util.UUID;

import ink.techat.client.factory.model.card.MessageCard;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.persistence.Account;

public class MessageCreateModel {
    /**
     * id: UUID
     * content: 内容
     * attach: 附件
     * type: 消息类型
     * senderId: 发送者Id
     * receiverId: 接收者Id
     * receiverType: 接收者类型， Group or man
     */
    private String id;
    private String content;
    private String attach;
    private int type = Message.TYPE_STR;
    private String receiverId;
    private int receiverType = Message.RECEIVER_TYPE_NONE;

    private MessageCreateModel() {
        // 随机生成一个UUID
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAttach() {
        return attach;
    }

    public int getType() {
        return type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }



    private MessageCard card;
    public MessageCard buildCard() {
        if (card == null) {
            MessageCard card = new MessageCard();
            card.setId(id);
            card.setContent(content);
            card.setAttach(attach);
            card.setType(type);
            card.setSenderId(Account.getUserId());

            if (receiverType == Message.RECEIVER_TYPE_GROUP) {
                card.setGroupId(receiverId);
            } else {
                card.setReceiverId(receiverId);
            }
            // 通过当前model建立的Card就是一个初步状态的Card
            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
            this.card = card;
        }
        return this.card;
    }

    /**
     * 建造者模式, 快速的建立一个发送Model
     */
    public static class Builder {

        private MessageCreateModel model;

        public Builder() {
            this.model = new MessageCreateModel();
        }

        /**
         * 设置接收者
         * @param receiverId 接收者Id
         * @param receiverType 接收者类型
         * @return Builder
         */
        public Builder receiver(String receiverId, int receiverType) {
            model.receiverId = receiverId;
            model.receiverType = receiverType;
            return this;
        }

        /**
         * 设置内容
         * @param content 内容
         * @param type 内容类型
         * @return Builder
         */
        public Builder content(String content, int type) {
            this.model.content = content;
            this.model.type = type;
            return this;
        }

        /**
         * 设置附件
         * @param attach 附件
         * @return Builder
         */
        public Builder attach(String attach) {
            this.model.attach = attach;
            return this;
        }

        public MessageCreateModel build() {
            return this.model;
        }
    }

    /**
     * 把一个Message消息, 转换为一个创建状态的CreateModel
     * @param message Message
     * @return MessageCreateModel
     */
    public static MessageCreateModel buildWithMessage(Message message) {
        MessageCreateModel model = new MessageCreateModel();
        model.id = message.getId();
        model.content = message.getContent();
        model.type = message.getType();
        model.attach = message.getAttach();

        // 如果接收者不为null, 则是给人发送消息
        if (message.getReceiver() != null) {
            model.receiverId = message.getReceiver().getId();
            model.receiverType = Message.RECEIVER_TYPE_NONE;
        } else {
            model.receiverId = message.getGroup().getId();
            model.receiverType = Message.RECEIVER_TYPE_GROUP;
        }
        return model;
    }
}
