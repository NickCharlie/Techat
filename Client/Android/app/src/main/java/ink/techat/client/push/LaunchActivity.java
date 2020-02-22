package ink.techat.client.push;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;

import ink.techat.client.access.RequestAccess;
import ink.techat.client.common.app.Activity;
import ink.techat.client.push.activities.MainActivity;

import static ink.techat.client.access.RequestAccess.hasExternalStoragePermission;

/**
 * @author NickCharlie
 */
public class LaunchActivity extends Activity {

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
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
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
    protected void onResume() {
        super.onResume();
        date();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        requestAccess.applicationAccess(this);
    }

    @SuppressLint("ApplySharedPref")
    private void date() {
        SharedPreferences shared=getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer=shared.getBoolean("isfer", true);
        SharedPreferences.Editor editor=shared.edit();
        if(isfer){
            //第一次进入跳转
            if(hasExternalStoragePermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                editor.putBoolean("isfer", false);
                editor.commit();
                finish();
                MainActivity.show(this);
            }
        }else{
            //第二次进入跳转
            if(hasExternalStoragePermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                MainActivity.show(this);
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
