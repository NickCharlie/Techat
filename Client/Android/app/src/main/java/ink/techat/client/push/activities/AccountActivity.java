package ink.techat.client.push.activities;

import android.content.Context;
import android.content.Intent;

import ink.techat.client.common.app.Activity;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.push.R;
import ink.techat.client.push.fragments.account.AccountTrigger;
import ink.techat.client.push.fragments.account.LoginFragment;
import ink.techat.client.push.fragments.account.RegisterFragment;

/**
 * @author NickCharlie
 */
public class AccountActivity extends Activity implements AccountTrigger {
    private Fragment mCurFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;

    /**
     * 账户Activity显示的入口
     * @param context Context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment){
            if (mRegisterFragment == null){
                // 默认情况下为null，第一次之后不为空
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        }else {
            fragment = mLoginFragment;
        }
        // 重新赋值当前正在显示的Fragment
        mCurFragment = fragment;
        // commit切换显示
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container, fragment)
                .commit();
    }
}

