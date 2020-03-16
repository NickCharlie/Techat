package ink.techat.client.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;

import ink.techat.client.factory.utils.DiffUiDataCallback;

/**
 * 基础的BaseDbModel, 继承了数据库框架中的基础类
 * 同时定义了一些方法
 * @author NickCharlie
 */
public abstract class BaseDbModel<Model> extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model> {

}
