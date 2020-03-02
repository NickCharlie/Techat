package ink.techat.client.push.fragments.account;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.common.app.PresenterFragment;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.factory.presenter.account.LoginContract;
import ink.techat.client.factory.presenter.account.LoginPresenter;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.MainActivity;

/**
 * 用于登录的Fragment
 * @author NickCharlie
 */
public class LoginFragment extends PresenterFragment<LoginContract.Presenter>
            implements LoginContract.View{
    private AccountTrigger mAccountTrigger;

    @BindView(R.id.img_login_portraits)
    PortraitView mPortraits;

    @BindView(R.id.edit_login_phone)
    EditText mPhone;

    @BindView(R.id.edit_login_password)
    EditText mPassword;

    @BindView(R.id.login_loading)
    Loading mLoading;

    @BindView(R.id.btn_login_submit)
    Button mLoginBtn;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 拿到Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
    }

    @OnClick(R.id.btn_login_submit)
    void onSubmitClick(){
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        // 提交注册
        mPresenter.login(phone, password);
    }

    @OnClick(R.id.txt_go_register)
    void onShowRegisterClick(){
        // 让AccountActivity进行界面切换
        mAccountTrigger.triggerView();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        // loading正在进时，界面不可操作
        // 开始Loading
        mLoading.start();
        // 让控件不可以输入
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        // 显示错误时触发，loading结束了

        // 停止Loading
        mLoading.stop();
        // 让控件可以输入
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
