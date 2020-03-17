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
 * ��Ϣ���͵����
 *
 * @author NickCharlie
 */
@Path("/message")
public class MessageService extends BaseService {

    /**
     * ������Ϣ�Ľӿ�
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
        // ��ѯ��Ϣ�Ƿ��Ѿ�����
        Message message = MessageFactory.findById(model.getId());
        if (message != null) {
            // ��Ϣ�Ѵ���, ��������
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
            // û���ҵ�������, ���ش���
            return ResponseModel.buildNotFoundUserError("Can't find receiver");
        }
        if (receiver.getId().equalsIgnoreCase(sender.getId())) {
            // �����߽�������ͬһ����, ���ش���ʧ���쳣
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }
        Message message = MessageFactory.add(sender, receiver, model);
        return buildAndPushResponse(sender, message);
    }

    private ResponseModel<MessageCard> pushToGroup(User sender, MessageCreateModel model) {
        // ����Ⱥ��Ȩ������, ����㲻�Ǹ�ȺȺԱ, ���޷�����
        Group group = GroupFactory.findById(sender, model.getReceiverId());
        if (group == null){
            // û���ҵ�������, ���ش���, �������㲻�Ǹ�Ⱥ��Ա
            return ResponseModel.buildNotFoundUserError("Can't find group");
        }
        Message message = MessageFactory.add(sender, group, model);
        return buildAndPushResponse(sender, message);
    }
    /**
     * ���Ͳ�����һ��������Ϣ
     * @param sender ������
     * @param message ��Ϣ
     * @return ResponseModel<MessageCard>
     */
    private ResponseModel<MessageCard> buildAndPushResponse(User sender, Message message) {
        if (message == null){
            // �洢���ݿ�ʧ��
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }
        // ��������
        PushFactory.pushNewMessage(sender, message);
        return ResponseModel.buildOk(new MessageCard(message));
    }
}
