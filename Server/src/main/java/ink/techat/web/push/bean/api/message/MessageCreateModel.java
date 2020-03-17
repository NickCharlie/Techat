package ink.techat.web.push.bean.api.message;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import ink.techat.web.push.bean.db.Message;

/**
 * API请求的Model格式
 * @author NickCharlie
 */
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
    @Expose
    private String id;
    @Expose
    private String content;
    @Expose
    private String attach;
    @Expose
    private int type = Message.TYPE_STR;
    @Expose
    private String receiverId;
    @Expose
    private int receiverType = Message.RECEIVER_TYPE_NONE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    public static boolean check(MessageCreateModel model) {
        return model != null &&
                !(Strings.isNullOrEmpty(model.id)
                || Strings.isNullOrEmpty(model.content)
                || Strings.isNullOrEmpty(model.receiverId))

                && (model.receiverType == Message.RECEIVER_TYPE_NONE
                || model.receiverType == Message.RECEIVER_TYPE_GROUP)

                && (model.type == Message.TYPE_STR || model.type == Message.TYPE_AUDIO
                || model.type == Message.TYPE_FILE || model.type == Message.TYPE_PIC);
    }
}
