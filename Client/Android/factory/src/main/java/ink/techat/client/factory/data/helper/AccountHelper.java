package ink.techat.client.factory.data.helper;

import android.util.Log;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.R;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.model.api.RspModel;
import ink.techat.client.factory.model.api.account.AccountRspModel;
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
     * 注册的接口, 异步调用
     * @param model 注册Model
     * @param callback 成功或失败的回调方法
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback){
        // 调用Retrofit对网络接口做代理, 得到Call
        RemoteService service = Network.getRetrofit().create(RemoteService.class);
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        // 异步请求
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
                // 网络请求成功返回, 从返回中得到全局Model, 内部为Json解析
                RspModel<AccountRspModel> rspModel = response.body();
                if (response != null && rspModel.success()){
                    // 拿到实体
                    AccountRspModel accountRspModel = rspModel.getResult();
                    // 判断绑定状态
                    if(accountRspModel.isBind()) {
                        User user = accountRspModel.getUser();
                        // TODO: 写入数据库和缓存绑定, 然后返回
                        callback.onDataLoaded(user);
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
                // 网络请求失败
                callback.onDataNotAvailable(R.string.data_network_error);
                Log.i("通知：",t.getMessage());
            }
        });
    }

    /**
     * 对设备Id进行绑定
     * @param callback DataSource.Callback<User>
     */
    public static void bindPush(final DataSource.Callback<User> callback){
        Account.setBind(true);
    }
}
