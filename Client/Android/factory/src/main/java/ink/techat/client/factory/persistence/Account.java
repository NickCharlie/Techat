package ink.techat.client.factory.persistence;

import android.content.ContentProvider;
import android.content.Context;
import android.content.SharedPreferences;

import ink.techat.client.common.Common;
import ink.techat.client.factory.Factory;

/**
 * @author NicckCharlie
 */
public class Account {

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";

    /**
     *  String pushId PushID, 设备绑定码
     */
    private static String pushId = null;

    /**
     * 设备Id的绑定状态
     */
    private static boolean isBind;

    /**
     * 存储到xml文件
     */
    private static void save(Context context){
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences(Account.class.getSimpleName(),
                Context.MODE_PRIVATE);
        // 存储数据
        sp.edit().putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIND, isBind)
                .apply();
    }

    /**
     * 获取推送Id
     * @return pushId
     */
    public static String getPushId() {
        return pushId;
    }

    /**
     * 设置并且存储设备的Id
     * @param pushId 设备的推送Id
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.app());
    }

    /**
     * 进行数据加载
     * @param context Context
     */
    public static void load(Context context){
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences(Account.class.getSimpleName(),
                Context.MODE_PRIVATE);
        // 加载设备Id, 设备Id绑定状态
        pushId = sp.getString(KEY_PUSH_ID, "");
        isBind = sp.getBoolean(KEY_IS_BIND, false);
    }

    /**
     * 判断当前登录状态
     * @return 当前登录状态 True or false
     */
    public static boolean isLogin(){
        return true;
    }

    /**
     * 判断设备是否已经绑定服务器
     * @return True or False
     */
    public static boolean isBind(){
        return isBind;
    }

    /**
     * 设置绑定状态
     * @param isBind 是否已绑定
     */
    public static void setBind(boolean isBind){
        Account.isBind = isBind;
        Account.save(Factory.app());
    }
}
