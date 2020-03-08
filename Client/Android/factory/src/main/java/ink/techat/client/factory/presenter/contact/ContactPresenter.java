package ink.techat.client.factory.presenter.contact;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.model.db.User_Table;
import ink.techat.client.factory.persistence.Account;
import ink.techat.client.factory.presenter.BasePresenter;

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
        // TODO: 加载数据
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

    }
}
