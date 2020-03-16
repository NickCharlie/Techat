package ink.techat.client.factory;

import android.util.Log;

import androidx.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;

import ink.techat.client.common.app.Application;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.data.group.GroupCenter;
import ink.techat.client.factory.data.group.GroupDispatcher;
import ink.techat.client.factory.data.message.MessageCenter;
import ink.techat.client.factory.data.message.MessageDispatcher;
import ink.techat.client.factory.data.user.UserCenter;
import ink.techat.client.factory.data.user.UserDispatcher;
import ink.techat.client.factory.model.api.PushModel;
import ink.techat.client.factory.model.api.RspModel;
import ink.techat.client.factory.model.card.GroupCard;
import ink.techat.client.factory.model.card.GroupMemberCard;
import ink.techat.client.factory.model.card.MessageCard;
import ink.techat.client.factory.model.card.UserCard;
import ink.techat.client.factory.persistence.Account;
import ink.techat.client.factory.utils.DBFlowExclusionStrategy;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @author NickCharlie
 */
public class Factory {

    private static final String TAG = Factory.class.getSimpleName();

    /**
     * 单例模式
     * Executor executor 全局的线程池
     * Gson gson 全局的Gson
     */
    private static final Factory INSTANCE;
    @SuppressWarnings("FieldCanBeLocal")
    private final Executor executor;
    private final Gson gson;

    static {
        INSTANCE = new Factory();
    }

    private Factory(){
        // 新建一个4个线程的线程池
        executor = newFixedThreadPool(4);
        // 设置Gson解析器, setDateFormat设置时间格式,
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    /**
     * Factory 中的初始化
     */
    public static void setup(){
        // 数据库初始化, 顺便打开数据库
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true)
                .build());

        // 持久化的数据进行初始化
        Account.load(app());
    }

    /**
     * 返回全局的Application
     * @return Application
     */
    public static Application app(){
        return Application.getInstance() ;
    }

    /**
     * 异步运行的方法
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable){
        // 拿到单例，拿到线程池，异步执行
        INSTANCE.executor.execute(runnable);
    }

    /**
     * 返回一个全局Gson
     * @return Gson
     */
    public static Gson getGson(){
        return INSTANCE.gson;
    }

    /**
     * 进行错误Code的解析，把网络返回的Code值进行统一的规划并且返回为一个string资源
     * @param model RspModel;
     * @param callback DataSource.FailedCallback
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback){
        if (model == null){
            return;
        }
        switch (model.getCode()){
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                INSTANCE.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId, final DataSource.FailedCallback callback){
        if (callback != null){
            callback.onDataNotAvailable(resId);
        }
    }

    /**
     * 收到账户退出的消息，需要进行账户退出重新登录
     */
    private void logout(){

    }

    /**
     * 处理推送过来的消息
     * @param message Message
     */
    public static void dispatchPush(String message){
        // 检查登录状态
        if (!Account.isLogin()){
            return;
        }
        PushModel model = PushModel.decode(message);
        if (model == null){
            return;
        }
        Log.i(TAG, model.getEntities().toString());
        // 对推送集合进行遍历
        for (PushModel.Entity entity : model.getEntities()) {
            switch (entity.type){
                case PushModel.ENTITY_TYPE_LOGOUT:
                    INSTANCE.logout();
                    return;

                case PushModel.ENTITY_TYPE_MESSAGE: {
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_FRIEND: {
                    UserCard card = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP: {
                    GroupCard card = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS: {
                    // 群成员变更, 群成员的列表
                    Type type = new TypeToken<List<GroupMemberCard>>(){}.getType();
                    List<GroupMemberCard> card = getGson().fromJson(entity.content, type);
                    getGroupCenter().dispatch(card.toArray(new GroupMemberCard[0]));
                    break;
                }

                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS:{
                    //TODO: 成员退出的推送

                }

                default:// TODO
            }
        }
    }

    /**
     * 获取一个用户中心的实现类
     * @return 用户中心的规范接口
     */
    public static UserCenter getUserCenter(){
        return UserDispatcher.instance();
    }

    /**
     * 获取一个消息中心的实现类
     * @return 消息中心的规范接口
     */
    public static MessageCenter getMessageCenter(){
        return MessageDispatcher.instance();
    }

    /**
     * 获取一个群中心的实现类
     * @return 群中心的规范接口
     */
    public static GroupCenter getGroupCenter(){
        return GroupDispatcher.instance();
    }
}
