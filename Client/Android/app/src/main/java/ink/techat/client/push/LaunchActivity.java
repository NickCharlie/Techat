package ink.techat.client.push;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import ink.techat.client.access.RequestAccess;
import ink.techat.client.common.app.Activity;
import ink.techat.client.factory.persistence.Account;
import ink.techat.client.push.activities.AccountActivity;
import ink.techat.client.push.activities.MainActivity;

import static ink.techat.client.access.RequestAccess.hasExternalStoragePermission;

/**
 * @author NickCharlie
 */
public class LaunchActivity extends Activity {
    @BindView(R.id.launch_loading)
    Loading launchLoadView;

    /**
     * App运行所需要的基本权限
     */
    private static final int REQUEST_PERMISSION_CODE = 1;
    private static final String[] PERMISSIONS_STORAGE = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.GET_TASKS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
    };

    /**
     * 动态权限申请方法初始化
     */
    RequestAccess requestAccess = new RequestAccess(PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    protected void onResume() {
        super.onResume();
        launchLoadView.start();
        waitPushReceiverId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        requestAccess.applicationAccess(this);
    }

    /**
     * 等待个推sdk对PushId设置值
     */
    @SuppressWarnings("UnnecessaryReturnStatement")
    private void waitPushReceiverId(){

        if (Account.isLogin()){
            // 在登录状态, 判断是否已经绑定, 没有绑定则等待绑定
            if (Account.isBind()){
                skip();
                return;
            }
        }else {
            // 不在登录状态,
            // 如果拿到了 PushId 但不在登录状态则不能绑定pushId
            if (!TextUtils.isEmpty(Account.getPushId()) && Account.getPushId() != null){
                launchLoadView.stop();
                skip();
                return;
            }
        }
        // 循环等待
        getWindow().getDecorView()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 等待检查状态
                        waitPushReceiverId();
                    }
                }, 500);
    }

    @SuppressLint("ApplySharedPref")
    private void skip() {
        SharedPreferences shared=getSharedPreferences("is", MODE_PRIVATE);
        boolean isFirst=shared.getBoolean("isFirst", true);
        SharedPreferences.Editor editor=shared.edit();
        if(isFirst){
            //第一次进入跳转
            if(hasExternalStoragePermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                editor.putBoolean("isFirst", false);
                editor.commit();
                // 检查登录状态判断跳转页面
                if (Account.isLogin()){
                    MainActivity.show(this);
                }else {
                    AccountActivity.show(this);
                }
                finish();
            }
        }else{
            //第二次进入跳转
            if(hasExternalStoragePermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                // 检查登录状态判断跳转页面
                if (Account.isLogin()){
                    MainActivity.show(this);
                }else {
                    AccountActivity.show(this);
                }
                finish();
            }
        }
    }

    /**
     * 动态权限申请的回调方法
     *
     * @param requestCode  请求码
     * @param permissions  权限内容
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

}
