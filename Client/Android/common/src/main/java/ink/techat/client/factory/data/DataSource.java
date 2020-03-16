package ink.techat.client.factory.data;

import androidx.annotation.StringRes;

/**
 * 数据源接口定义
 * @author NickCharlie
 */
public interface DataSource {

    /**
     * 包括了成功和失败的回调接口
     * @param <T> T
     */
    interface Callback<T> extends SucceedCallback<T>, FailedCallback{

    }

    /**
     * 成功的信息
     * @param <T> T
     */
    interface SucceedCallback<T>{
        // 数据加载成功，网络请求成功
        void onDataLoaded(T t);
    }

    /**
     * 失败的信息
     * @param <T> T
     */
    interface FailedCallback<T>{
        // 数据加载失败，网络请求失败
        void onDataNotAvailable(@StringRes int strRes);
    }

    /**
     * 销毁操作
     */
    void dispose();
}
