package ink.techat.client.factory.presenter.account;

import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import ink.techat.client.factory.R;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.data.helper.AccountHelper;
import ink.techat.client.factory.model.api.account.LoginModel;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.persistence.Account;
import ink.techat.client.factory.presenter.BasePresenter;

/**
 * 登录的逻辑类
 * @author NickCharlie
 */
public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter, DataSource.Callback<User>{


    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();
        final LoginContract.View view = getmView();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)){
            view.showError(R.string.data_account_login_invalid_parameter);
        }else {
            // 尝试传递PushId
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, this);
        }
    }

    @Override
    public void onDataLoaded(User user) {
        globalUser = user;
        // 当网络请求成功, 登录成功, 回送用户信息, 告知界面登录成功
        final LoginContract.View view = getmView();
        if (view == null){
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 登录失败
        final LoginContract.View view = getmView();
        if (view == null){
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
