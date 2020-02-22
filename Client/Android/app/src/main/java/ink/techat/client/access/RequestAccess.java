package ink.techat.client.access;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ink.techat.client.common.app.Application;


/**
 * @author NickCharlie
 * 用于Android 6.0 以上环境的动态权限请求
 */
public class RequestAccess {
    /**
     * PERMISSIONS_STORAGE
     * 需要请求的权限列表
     * REQUEST_PERMISSION_CODE
     * 请求状态码
     */
    private final String[] PERMISSIONS_STORAGE;
    private final int REQUEST_PERMISSION_CODE;

    public  RequestAccess(String[] permissionsStorage, int requestPermissionCode){
        this.PERMISSIONS_STORAGE = permissionsStorage;
        this.REQUEST_PERMISSION_CODE = requestPermissionCode;
    }


    /**
     * applicationAccess(Activity action)
     * 判断当前环境是否为Android 6.0 以上版本
     * 并且执行权限请求操作
     * @param action  Activity
     */
    public void applicationAccess(Activity action) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(action, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(action, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    public static boolean hasExternalStoragePermission(Context context, String EXTERNAL_STORAGE_PERMISSION) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
