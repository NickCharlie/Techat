package ink.techat.client.factory.presenter;

import androidx.annotation.StringRes;

import ink.techat.client.common.widget.recycler.RecycierAdapter;

/**
 * 公共基本契约
 * @author NickCharlie
 */
public interface BaseContract {
    interface View<T extends Presenter>{

        // 公共: 显示错误
        void showError(@StringRes int str);

        // 公共: 显示进度条
        void showLoading();

        // 支持设置一个Presenter
        void setPresenter(T presenter);
    }

    interface Presenter{

        // 公共: start方法
        void start();

        // 公共: 销毁方法
        void destroy();
    }

    interface RecyclerView<T extends Presenter, ViewMode> extends View<T>{
        // 拿到一个适配器, 然后进行自主刷新
        RecycierAdapter<ViewMode> getRecyclerAdapter();

        // 数据更改触发
        void onAdapterDataChanged();
    }
}
