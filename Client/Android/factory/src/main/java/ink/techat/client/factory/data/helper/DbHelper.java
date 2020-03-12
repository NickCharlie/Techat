package ink.techat.client.factory.data.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.Arrays;

import ink.techat.client.factory.model.db.AppDatabase;

/**
 * 数据库的辅助工具类
 * 封装增删改操作
 * @author NickCharlie
 */
public class DbHelper {

    private static final DbHelper INSTANCE;

    static {
        INSTANCE = new DbHelper();
    }

    private DbHelper(){

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
    @SafeVarargs
    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass, final Model... models){

    }

    /**
     * 数据库通知更新的统一notifyDelete方法
     * @param tClass tClass
     * @param models models
     * @param <Model> Model extends BaseModel
     */
    @SafeVarargs
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass, final Model... models){

    }
}
