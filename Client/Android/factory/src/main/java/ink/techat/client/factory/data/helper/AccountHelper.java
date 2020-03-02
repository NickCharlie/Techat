package ink.techat.client.factory.data.helper;

import android.text.TextUtils;
import android.util.Log;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.R;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.model.api.RspModel;
import ink.techat.client.factory.model.api.account.AccountRspModel;
import ink.techat.client.factory.model.api.account.LoginModel;
import ink.techat.client.factory.model.api.account.RegisterModel;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.net.Network;
import ink.techat.client.factory.net.RemoteService;
import ink.techat.client.factory.persistence.Account;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户操作的工具类
 * @author NickCharlie
 */
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
public class AccountHelper {

    /**
     * 注册的调用, 异步调用
     * @param model 注册Model
     * @param callback 成功或失败的回调方法
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback){
        // 调用Retrofit对网络接口做代理, 得到Call
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        // 异步请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 登录的调用, 异步调用
     * @param model 登录Model
     * @param callback 成功或失败的回调方
     */
    public static void login(LoginModel model, final DataSource.Callback<User> callback){
        // 调用Retrofit对网络接口做代理, 得到Call
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        // 异步请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 对设备Id进行绑定
     * @param callback DataSource.Callback<User>
     */
    public static void bindPush(final DataSource.Callback<User> callback){
        String pushId = Account.getPushId();
        // 检查是否为空
        if(TextUtils.isEmpty(pushId)){
            return;
        }
        // 调用Retrofit对网络接口做代理, 得到Call
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 请求的回调部分封装
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>>{

        final DataSource.Callback<User> callback;

        public AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            // 网络请求成功返回, 从返回中得到全局Model, 内部为Json解析
            RspModel<AccountRspModel> rspModel = response.body();

            if (response != null && rspModel.success()){
                // 拿到实体, 获取我的用户信息
                AccountRspModel accountRspModel = rspModel.getResult();
                final User user = accountRspModel.getUser();
                // 保存到数据库, 将用户信息存储到持久化xml
                user.save();
                Account.login(accountRspModel);

                // 判断绑定状态
                if(accountRspModel.isBind()) {
                    Account.setBind(true);
                    if (callback != null) {
                        callback.onDataLoaded(user);
                    }
                }else {
                    callback.onDataLoaded(accountRspModel.getUser());
                    // 发现没有对pushId进行绑定, 绑定设备Id
                    bindPush(callback);
                }
            }else {
                // 错误解析
                Factory.decodeRspCode(rspModel, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            if (callback == null){
                return;
            }
            // 网络请求失败
            callback.onDataNotAvailable(R.string.data_network_error);
            Log.i("通知：",t.getMessage());
        }
    }
}
