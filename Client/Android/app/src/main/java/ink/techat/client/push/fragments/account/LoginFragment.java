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
import ink.techat.client.common.app.Fragment;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.push.R;

/**
 * 用于登录的Fragment
 * @author NickCharlie
 */
public class LoginFragment extends Fragment {
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

    public LoginFragment() {
        // Required empty public constructor
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

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 进行一次切换,默认为注册界面
        mAccountTrigger.triggerView();
    }
}
