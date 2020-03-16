package ink.techat.client.factory.data;

import java.util.List;

/**
 * 基础的数据库数据源接口定义
 * @author NickCharlie
 * @param <Data> T
 */
public interface DbDataSource<Data> extends DataSource {
    /**
     * 有一个基本的数据源加载方法
     * @param callback Callback回调, 一般毁掉的傲Presenter
     */
    void load(SucceedCallback<List<Data>> callback);
}