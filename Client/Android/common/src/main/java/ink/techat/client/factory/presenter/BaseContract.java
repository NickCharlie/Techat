package ink.techat.client.factory.presenter;

import androidx.annotation.StringRes;

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
}
