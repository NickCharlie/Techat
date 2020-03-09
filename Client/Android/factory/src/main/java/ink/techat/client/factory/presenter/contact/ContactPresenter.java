package ink.techat.client.factory.presenter.contact;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.data.helper.UserHelper;
import ink.techat.client.factory.model.card.UserCard;
import ink.techat.client.factory.model.db.AppDatabase;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.model.db.User_Table;
import ink.techat.client.factory.persistence.Account;
import ink.techat.client.factory.presenter.BasePresenter;
import ink.techat.client.factory.utils.DiffUiDataCallback;

/**
 * 联系人的逻辑实现
 * @author NickCharlie
 */
public class ContactPresenter extends BasePresenter<ContactContract.View> implements ContactContract.Presenter {

    public ContactPresenter(ContactContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        // 加载本地数据库的数据
        SQLite.select().from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getmView().getRecyclerAdapter().replace(tResult);
                        getmView().onAdapterDataChanged();
                    }
                }).execute();

        // 加载网络数据
        UserHelper.refreshContacts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataLoaded(final List<UserCard> userCards) {

                final List<User> users = new ArrayList<>();
                for (UserCard userCard : userCards) {
                    users.add(userCard.build());
                }

                // 丢到事务中保存数据库
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        FlowManager.getModelAdapter(User.class)
                                .saveAll(users);
                    }
                }).build().execute();
                List<User> old = getmView().getRecyclerAdapter().getItems();
                // 新旧联系人数据对比
                diff(old, users);
            }

            @Override
            public void onDataNotAvailable(int strRes) {
                // 网络刷新失败, 因为本地有数据, 忽略错误
            }
        });

        // TODO: BUG修复
        //  1. 网络获取数据后没有刷新联系人
        //  2. 刷新内容为全部刷新, 需要刷新单条数据
        //  3. 网络和本地刷新添加到界面会出现冲突
    }
    private void diff(List<User> oldList, List<User> newList){
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldList, newList);
        DiffUtil.DiffResult result= DiffUtil.calculateDiff(callback);

        // 在对比完成后进行数据赋值
        getmView().getRecyclerAdapter().replace(newList);
        // 尝试刷新界面
        result.dispatchUpdatesTo(getmView().getRecyclerAdapter());
        getmView().onAdapterDataChanged();
    }

}
