package ink.techat.client.factory.data;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import ink.techat.client.factory.data.helper.DbHelper;
import ink.techat.client.factory.model.db.BaseDbModel;
import ink.techat.client.utils.CollectionUtil;

/**
 * 基础的数据库仓库
 * 实现对数据库基础的监听操作
 * @author NickCharlie
 */
@SuppressWarnings("unchecked")
public abstract class BaseDbRepository<Data extends BaseDbModel<Data>> implements DbDataSource<Data>, DbHelper.ChangedListener<Data>,
                QueryTransaction.QueryResultListCallback<Data> {

    private Class<Data> dataClass;
    private final List<Data> dataList = new LinkedList<>();
    private SucceedCallback<List<Data>> callback;

    public BaseDbRepository(){
        // 拿当前类的泛型数组信息
        Type[] types = Reflector.getActualTypeArguments(BaseDbRepository.class, this.getClass());
        dataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SucceedCallback<List<Data>> callback) {
        this.callback = callback;
        // 进行数据库监听操作
        registerDbChangedListener();
    }

    @Override
    public void dispose() {
        // 取消监听, 销毁数据
        this.callback = null;
        DbHelper.removeChangedListener(dataClass, this);
        dataList.clear();
    }

    /**
     * 数据库统一通知的地方 增加, 更改
     * @param data 数据
     */
    @Override
    public void onDataSave(Data[] data) {
        // 当数据库数据变更的操作
        boolean isChanged = false;
        for (Data data1 : data) {
            if (isRequired(data1)){
                insertOrUpdate(data1);
                isChanged = true;
            }
        }
        if (isChanged){
            notifyDataChange();
        }
    }

    @Override
    public void onDataDelete(Data[] data) {
        // 当数据库数据删除的操作
        boolean isChanged = false;
        for (Data user : data) {
            if(dataList.remove(user)){
                isChanged = true;
            }
        }
        // 有数据变更则进行界面刷新
        if(isChanged){
            notifyDataChange();
        }
    }

    private void insertOrUpdate(Data data){
        int index = indexOf(data);
        if (index >= 0){
            replace(index, data);
        }else {
            insert(data);
        }
    }

    private int indexOf(Data newData){
        int index = -1;
        for (Data data1 : dataList) {
            index++;
            if(data1.isSame(newData)){
                return index;
            }
        }
        return -1;
    }

    /**
     * 添加方法
     * @param data data
     */
    private void insert(Data data){
        dataList.add(data);
    }

    /**
     * 替换方法
     * @param user User
     */
    private void replace(int index, Data user){
        dataList.remove(index);
        dataList.add(index, user);
    }

    /**
     * 添加数据库的监听操作
     */
    protected void registerDbChangedListener(){
        DbHelper.addChangedListener(dataClass, this);
    }

    private void notifyDataChange(){
        SucceedCallback<List<Data>> callback = this.callback;
        if (callback != null){
            callback.onDataLoaded(dataList);
        }
    }

    /**
     * DBFlow 框架通知的回调
     * @param transaction Transaction
     * @param tResult 结果
     */
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        // 当数据库加载成功
        if (tResult == null || tResult.size() == 0){
            dataList.clear();
            notifyDataChange();
            return;
        }
        Data[] users = CollectionUtil.toArray(tResult, dataClass);
        // 回到数据集更新的操作中
        onDataSave(users);
    }

    /**
     * 检查一个User是否是我需要的数据
     * @param data data
     * @return true or false
     */
    protected abstract boolean isRequired(Data data);
}
