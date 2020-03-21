package ink.techat.client.factory.data.message;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

import ink.techat.client.factory.data.BaseDbRepository;
import ink.techat.client.factory.model.db.Session;
import ink.techat.client.factory.model.db.Session_Table;

/**
 * 最近聊天列表仓库, 是对SessionDataSource的是西安
 * @author NickCharlie
 */
public class SessionRepository extends BaseDbRepository<Session> implements SessionDataSource {

    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);
        SQLite.select().from(Session.class)
                .orderBy(Session_Table.modifyAt, false)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Session session) {
        // 显示所以的Session, 不需要过滤
        return true;
    }

    @Override
    protected void insert(Session session) {
        // 复写方法, 让新的数据加到头部
        dataList.addFirst(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        // 复写数据库回来的方法, 进行一次反转
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
