package ink.techat.client.factory.presenter.account;

import androidx.annotation.StringRes;

import ink.techat.client.factory.presenter.BaseContract;

/**
 * @author NickCharlie
 */
public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{
        // 登录成功
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        // 发起一个注册
        void login(String phone, String password);
    }
}
