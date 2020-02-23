package ink.techat.client.push.activities;

import android.annotation.SuppressLint;
import android.content.Intent;

import ink.techat.client.common.app.Activity;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.push.R;
import ink.techat.client.push.fragments.user.UpdateInfoFragment;

/**
 * @author NickCharlie
 */
public class UserActivity extends Activity {
    private Fragment mCurFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
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
