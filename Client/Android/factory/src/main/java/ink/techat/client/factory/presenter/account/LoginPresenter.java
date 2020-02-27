package ink.techat.client.factory.presenter.account;

import ink.techat.client.factory.presenter.BasePresenter;

/**
 * 登录的逻辑类
 * @author NickCharlie
 */
public class LoginPresenter extends BasePresenter<LoginContract.View>
            implements LoginContract.Presenter{


    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }
}
