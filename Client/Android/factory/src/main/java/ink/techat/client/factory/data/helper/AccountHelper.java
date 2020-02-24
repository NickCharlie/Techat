package ink.techat.client.factory.data.helper;

import ink.techat.client.factory.R;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.model.api.account.RegisterModel;
import ink.techat.client.factory.model.db.User;

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
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.onDataNotAvailable(R.string.data_rsp_error_parameters);
            }
        }.start();
    }
}
