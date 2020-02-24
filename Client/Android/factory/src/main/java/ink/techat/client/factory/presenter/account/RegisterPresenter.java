package ink.techat.client.factory.presenter.account;

import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

import ink.techat.client.common.Common;
import ink.techat.client.factory.R;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.data.helper.AccountHelper;
import ink.techat.client.factory.model.api.account.RegisterModel;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.presenter.BasePresenter;

/**
 * @author NickCharlie
 */
@SuppressWarnings("FieldCanBeLocal")
public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {

    private final int MIN_NAME_LENGTH = 2;
    private final int MIN_PASSWORD_LENGTH = 5;

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        // 调用开始方法, 在start中默认启动了loading
        start();
        // 得到View接口
        RegisterContract.View view = getmView();

        // 参数校验
        if (!checkMobile(phone)){
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        }else if (name.length() < MIN_NAME_LENGTH) {
            // 用户名需要大于两位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        }else if (password.length() < MIN_PASSWORD_LENGTH){
            // 密码需要大于5位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        }else {
            // 参数没毛病, 构造Model, 进行网络请求调用, 设置回送口为this
            RegisterModel model = new RegisterModel(phone, password, name);
            AccountHelper.register(model, this);
        }
    }

    /**
     * 检查手机号是否合法
     * @param phone 手机号码
     * @return 合法为true，不合法为false
     */
    @Override
    public boolean checkMobile(String phone) {
        // 手机号不为空, 并且满足相应格式
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    @SuppressWarnings("SingleStatementInBlock")
    @Override
    public void onDataLoaded(User user) {
        // 当网络请求成功, 注册成功, 回送用户信息, 告知界面注册成功
        final RegisterContract.View view = getmView();
        if (view == null){
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 注册失败
        final RegisterContract.View view = getmView();
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
