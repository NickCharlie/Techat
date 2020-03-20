package ink.techat.client.factory.presenter.message;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import ink.techat.client.factory.data.message.MessageDataSource;
import ink.techat.client.factory.data.message.MessageHelper;
import ink.techat.client.factory.model.api.message.MessageCreateModel;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.persistence.Account;
import ink.techat.client.factory.presenter.BaseSourcePresenter;
import ink.techat.client.factory.utils.DiffUiDataCallback;

/**
 * 聊天的基础Presenter
 * @author NickCharlie
 */
public class ChatPresenter<View extends ChatContract.View> extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
            implements ChatContract.Presenter {

    /**
     * String mReceiverId 接收者Id
     * int mReceiverType 接收者类型
     */
    protected String mReceiverId;
    private int mReceiverType;

    @Override
    protected void refreshData(List<Message> dataList) {
        super.refreshData(dataList);
    }

    public ChatPresenter(MessageDataSource source, View view, String receiverId, int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }

    @Override
    public void pushText(String content) {
        MessageCreateModel model = new MessageCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(content, Message.TYPE_STR)
                .build();
        // 进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void pushAudio(String path) {
        // TODO: 发送语言
    }

    @Override
    public void pushImage(String[] paths) {
        // TODO: 发送图片
    }

    @Override
    public boolean rePush(Message message) {
        // 确定消息是可以重复发送的
        if (Account.isLogin() && Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED) {
            message.setStatus(Message.STATUS_CREATED);
            MessageCreateModel model = MessageCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDataLoaded(List<Message> messages) {
        super.onDataLoaded(messages);
        ChatContract.View view = getmView();
        if (view == null){
            return;
        }
        // 拿到old数据
        List<Message> old = view.getRecyclerAdapter().getItems();
        // 进行差异计算
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        // 进行界面刷新
        refreshData(result, messages);
    }
}
