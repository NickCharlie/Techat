package ink.techat.client.factory.presenter.message;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import ink.techat.client.factory.data.message.SessionDataSource;
import ink.techat.client.factory.data.message.SessionRepository;
import ink.techat.client.factory.model.db.Session;
import ink.techat.client.factory.presenter.BaseSourcePresenter;
import ink.techat.client.factory.utils.DiffUiDataCallback;

/**
 * 最近聊天列表的Presenter
 * @author NickCharlie
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSource, SessionContract.View>
                implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        super.onDataLoaded(sessions);
        SessionContract.View view = getmView();
        if (view == null) {
            return;
        }
        // 差异对比
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        // 刷新界面
        refreshData(result, sessions);
    }
}
