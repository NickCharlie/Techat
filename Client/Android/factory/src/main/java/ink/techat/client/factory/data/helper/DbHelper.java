package ink.techat.client.factory.data.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ink.techat.client.factory.model.db.AppDatabase;
import ink.techat.client.factory.model.db.Group;
import ink.techat.client.factory.model.db.GroupMember;
import ink.techat.client.factory.model.db.Group_Table;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.model.db.Session;
import ink.techat.client.utils.CollectionUtil;

/**
 * 数据库的辅助工具类
 * 封装增删改操作
 * @author NickCharlie
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "WeakerAccess"})
public class DbHelper {

    private static final DbHelper INSTANCE;

    static {
        INSTANCE = new DbHelper();
    }

    private DbHelper(){

    }

    /**
     * 观察者集合
     * Class<?> 观察的表
     * Set<ChangedListener> 每一个表对应的多个观察者
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    /**
     * 获取目标表的监听器集合
     * @param tClass 目标表
     * @param <Model> 表的泛型
     * @return Set<ChangedListener> 监听器集合
     */
    public static <Model extends BaseModel> Set<ChangedListener> getListener(final Class<Model> tClass){
        if (INSTANCE.changedListeners.containsKey(tClass)){
            return INSTANCE.changedListeners.get(tClass);
        }
        return null;
    }

    /**
     * 添加一个监听器
     * @param tClass 目标表
     * @param listener 监听器
     * @param <Model> 表的泛型
     */
    public static <Model extends BaseModel> void addChangedListener(final Class<Model> tClass, ChangedListener<Model> listener){
        Set<ChangedListener> changedListeners = getListener(tClass);
        if (changedListeners == null){
            changedListeners = new HashSet<>();
            // 添加到总的Map
            INSTANCE.changedListeners.put(tClass, changedListeners);
        }
        changedListeners.add(listener);
    }

    /**
     * 删除一个表的一个监听器
     * @param tClass 目标表
     * @param listener 监听器
     * @param <Model> 表的泛型
     */
    public static <Model extends BaseModel> void removeChangedListener(final Class<Model> tClass, ChangedListener<Model> listener){
        Set<ChangedListener> changedListeners = getListener(tClass);
        if (changedListeners != null){
            // 从容器中删除监听者
            changedListeners.remove(listener);
        }
    }

    /**
     * Model储存到本地数据库的统一save方法
     * @param tClass tClass
     * @param models Models
     * @param <Model> Model extends BaseModel
     */
    @SafeVarargs
    public static<Model extends BaseModel> void save(final Class<Model> tClass, final Model... models){
        if (models == null || models.length == 0){
            return;
        }
        // 丢到事务中保存数据库
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                if (models.length > 1){
                    adapter.saveAll(Arrays.asList(models));
                }else {
                    adapter.save(models[0]);
                }
                INSTANCE.notifySave(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 本地数据库删除内容的统一delete方法
     * @param tClass tClass
     * @param models models
     * @param <Model> Model extends BaseModel
     */
    @SafeVarargs
    public static<Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        // 丢到事务中保存数据库
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.deleteAll(Arrays.asList(models));
                INSTANCE.notifyDelete(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 数据库通知更新的统一notifySave方法
     * @param tClass tClass
     * @param models models
     * @param <Model> Model extends BaseModel
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass, final Model... models){
        final Set<ChangedListener> listeners = getListener(tClass);
        if (listeners == null || listeners.size() <= 0){
            return;
        }
        for (ChangedListener<Model> listener : listeners) {
            listener.onDataSave(models);
        }
        if (GroupMember.class.equals(tClass)){
            // 群成员变更, 通知对应的群信息更新
            updateGroup((GroupMember[]) models);
        }else if (Message.class.equals(tClass)){
            // 消息变化, 通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    /**
     * 数据库通知更新的统一notifyDelete方法
     * @param tClass tClass
     * @param models models
     * @param <Model> Model extends BaseModel
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass, final Model... models){
        final Set<ChangedListener> listeners = getListener(tClass);
        if (listeners == null || listeners.size() <= 0){
            return;
        }
        for (ChangedListener<Model> listener : listeners) {
            listener.onDataDelete(models);
        }
        if (GroupMember.class.equals(tClass)){
            // 群成员变更, 通知对应的群信息更新
            updateGroup((GroupMember[]) models);
        }else if (Message.class.equals(tClass)){
            // 消息变化, 通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    /**
     * 对群成员和群进行更新
     * @param members 群成员列表
     */
    private void updateGroup(GroupMember... members){
        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            // 添加群Id
            groupIds.add(member.getGroup().getId());
        }
        // 丢到事务中保存数据库
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                List<Group> groups = SQLite.select().from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();
                // 进行一次通知分发
                INSTANCE.notifySave(Group.class, CollectionUtil.toArray(groups, Group.class));
            }
        }).build().execute();
    }

    /**
     * 对Sessions会话进行更新
     * @param messages 消息列表
     */
    private void updateSession(final Message... messages){
        // 标识一个Session的唯一性
        final Set<Session.Identify> identifies = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);
            identifies.add(identify);
        }
        if (identifies.size() <= 0){
            return;
        }
        // 丢到事务中保存数据库
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                int index = 0;
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identifies.size()];
                for (Session.Identify identify : identifies){
                    Session session = SessionHelper.findFromLocal(identify.id);
                    if (session == null){
                        // 第一次聊天, 创建一个你和对方的会话
                        session = new Session(identify);
                    }
                    // 把会话刷新到当前Message的最新状态
                    session.refreshToNow();
                    adapter.save(session);
                    // 添加到集合
                    sessions[index++] = session;
                }
                // 找到需要通知的群, 进行一次通知分发
                INSTANCE.notifySave(Session.class, sessions);
            }
        }).build().execute();
    }

    /**
     * 通知监听器
     */
    @SuppressWarnings({"unused", "unchecked"})
    public interface ChangedListener<Data>{
        void onDataSave(Data... data);
        void onDataDelete(Data... data);
    }
}
