package ink.techat.web.push.service;

import ink.techat.web.push.bean.api.base.ResponseModel;
import ink.techat.web.push.bean.api.message.MessageCreateModel;
import ink.techat.web.push.bean.card.MessageCard;
import ink.techat.web.push.bean.db.Group;
import ink.techat.web.push.bean.db.Message;
import ink.techat.web.push.bean.db.User;
import ink.techat.web.push.factory.GroupFactory;
import ink.techat.web.push.factory.MessageFactory;
import ink.techat.web.push.factory.PushFactory;
import ink.techat.web.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 消息发送的入口
 *
 * @author NickCharlie
 */
@Path("/message")
public class MessageService extends BaseService {

    /**
     * 发送消息的接口
     *
     * @return ResponseModel<MessageCard>
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<MessageCard> pushMessage(MessageCreateModel model) {
        if (!MessageCreateModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();
        // 查询消息是否已经存在
        Message message = MessageFactory.findById(model.getId());
        if (message != null) {
            // 消息已存在, 正常返回
            return ResponseModel.buildOk(new MessageCard(message));
        }
        if (model.getReceiverType() == Message.RECEIVER_TYPE_NONE) {
            return pushToUser(self, model);
        } else if (model.getReceiverType() == Message.RECEIVER_TYPE_GROUP) {
            return pushToGroup(self, model);
        } else {
            return ResponseModel.buildParameterError();
        }
    }

    private ResponseModel<MessageCard> pushToUser(User sender, MessageCreateModel model) {
        User receiver = UserFactory.findById(model.getReceiverId());
        if (receiver == null) {
            // 没有找到接收者, 返回错误
            return ResponseModel.buildNotFoundUserError("Can't find receiver");
        }
        if (receiver.getId().equalsIgnoreCase(sender.getId())) {
            // 发送者接收者是同一个人, 返回创建失败异常
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }
        Message message = MessageFactory.add(sender, receiver, model);
        return buildAndPushResponse(sender, message);
    }

    private ResponseModel<MessageCard> pushToGroup(User sender, MessageCreateModel model) {
        // 查找群有权限限制, 如果你不是该群群员, 则无法查找
        Group group = GroupFactory.findById(sender, model.getReceiverId());
        if (group == null){
            // 没有找到接收者, 返回错误, 可能是你不是该群成员
            return ResponseModel.buildNotFoundUserError("Can't find group");
        }
        Message message = MessageFactory.add(sender, group, model);
        return buildAndPushResponse(sender, message);
    }
    /**
     * 推送并构建一个返回消息
     * @param sender 发送者
     * @param message 消息
     * @return ResponseModel<MessageCard>
     */
    private ResponseModel<MessageCard> buildAndPushResponse(User sender, Message message) {
        if (message == null){
            // 存储数据库失败
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }
        // 进行推送
        PushFactory.pushNewMessage(sender, message);
        return ResponseModel.buildOk(new MessageCard(message));
    }
}
