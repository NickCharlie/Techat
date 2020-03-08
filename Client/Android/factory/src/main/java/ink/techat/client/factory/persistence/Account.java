package ink.techat.client.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.model.api.account.AccountRspModel;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.model.db.User_Table;


/**
 * @author NicckCharlie
 */
public class Account extends BaseModel {

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    /**
     * String pushId PushID  设备绑定码
     * boolean isBind  设备Id的绑定状态
     * String token    当前登录的token
     * String userId   当前登录的用户Id
     * String account  当前登录的账户
     */
    private static String pushId = null;
    private static boolean isBind;
    private static String token;
    private static String userId;
    private static String account;

    /**
     * 存储到xml文件
     */
    private static void save(Context context) {
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences(Account.class.getSimpleName(),
                Context.MODE_PRIVATE);
        // 存储数据
        sp.edit().putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIND, isBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply();
    }

    /**
     * 获取推送Id
     *
     * @return pushId
     */
    public static String getPushId() {
        return pushId;
    }

    /**
     * 设置并且存储设备的Id
     *
     * @param pushId 设备的推送Id
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.app());
    }

    /**
     * 进行数据加载
     *
     * @param context Context
     */
    public static void load(Context context) {
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences(Account.class.getSimpleName(),
                Context.MODE_PRIVATE);
        // 加载设备Id, 设备Id绑定状态
        pushId = sp.getString(KEY_PUSH_ID, "");
        isBind = sp.getBoolean(KEY_IS_BIND, false);
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
        account = sp.getString(KEY_ACCOUNT, "");
    }

    /**
     * 判断当前登录状态
     *
     * @return 当前登录状态 True or false
     */
    public static boolean isLogin() {
        // userId和token 不为空则以登录
        return !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token);
    }

    /**
     * 是否已经完善了用户信息
     *
     * @return True
     */
    public static boolean isComplete() {
        // TODO: 用户信息完善判断
        if (isLogin()){
            User self = getUser();
            return !TextUtils.isEmpty(self.getDescription()) && !TextUtils.isEmpty(self.getPortrait())
                                                             && self.getSex() >= 0;
        }
        // 未登录, 返回false
        return false;
    }

    /**
     * 判断设备是否已经绑定服务器
     *
     * @return True or False
     */
    public static boolean isBind() {
        return isBind;
    }

    /**
     * 设置绑定状态
     *
     * @param isBind 是否已绑定
     */
    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        Account.save(Factory.app());
    }

    /**
     * 登录并且保存我自己的消息到持久化xml中
     *
     * @param model AccountRspModel
     */
    public static void login(AccountRspModel model) {
        // 储存当前登录的Account, token, userId, 方便从数据库查询信息
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();
        save(Factory.app());
    }
    /**
     * 获取当前登录的用户信息
     * @return User
     */
    public static User getUser(){
        // 如果为Null则返回一个New User, 否者就从数据库查询
        return TextUtils.isDigitsOnly(userId) ? new User(): SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }

    /**
     * 拿取用户Id
     * @return 用户Id
     */
    public static String getUserId(){
        return getUser().getId();
    }

    public static String getToken(){
        return token;
    }


}
