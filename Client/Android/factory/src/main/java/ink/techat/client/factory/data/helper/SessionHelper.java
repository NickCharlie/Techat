package ink.techat.client.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import ink.techat.client.factory.model.db.Session;
import ink.techat.client.factory.model.db.Session_Table;

/**
 * 会话辅助工具类
 * @author NickCharlie
 */
public class SessionHelper {

    /**
     * 从本地查询Session
     * @param id Id
     * @return Session
     */
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
