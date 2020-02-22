package ink.techat.client.push.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import ink.techat.client.common.app.Activity;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.push.R;
import ink.techat.client.push.fragments.account.UpdateInfoFragment;

/**
 * @author NickCharlie
 */
public class AccountActivity extends Activity {
    private Fragment mCurFragment;
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
        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    /**
     * Activity中收到剪切图片收到成功的回调
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }
}

