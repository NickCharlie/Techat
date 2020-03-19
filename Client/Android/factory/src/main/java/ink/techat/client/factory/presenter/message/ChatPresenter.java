package ink.techat.client.factory.presenter.message;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import ink.techat.client.factory.data.message.MessageDataSource;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.presenter.BaseSourcePresenter;
import ink.techat.client.factory.utils.DiffUiDataCallback;

/**
 * 聊天的基础Presenter
 * @author NickCharlie
 */
public class ChatPresenter<View extends ChatContract.View> extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
            implements ChatContract.Presenter {

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

    }

    @Override
    public void pushAudio(String path) {

    }

    @Override
    public void pushImage(String[] paths) {

    }

    @Override
    public boolean rePush(Message message) {
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
