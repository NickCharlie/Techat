package ink.techat.client.factory.data.user;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import ink.techat.client.factory.data.BaseDbRepository;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.model.db.User_Table;
import ink.techat.client.factory.persistence.Account;

/**
 * 联系人仓库
 * @author NickCharlie
 */
public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {

    private DataSource.SucceedCallback<List<User>> callback;

    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        super.load(callback);
        SQLite.select().from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    /**
     * 检查一个User是否是我需要的数据
     * @param user User
     * @return true or false
     */
    @Override
    protected boolean isRequired(User user){
        return user.isFollow() && !(user.getId().equals(Account.getUserId()));
    }
}
