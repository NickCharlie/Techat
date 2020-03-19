package ink.techat.client.factory.presenter.message;

import ink.techat.client.factory.data.helper.UserHelper;
import ink.techat.client.factory.data.message.MessageRepository;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.model.db.User;

/**
 * 与联系人聊天的Presenter
 *
 * @author NickCharlie
 */
public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView> implements ChatContract.Presenter {

    private User mReceiver;

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        // 数据源, View, 接收者, 接收者类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();
        // 从本地拿联系人信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getmView().onInit(receiver);
    }
}
