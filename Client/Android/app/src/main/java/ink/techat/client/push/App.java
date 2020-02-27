package ink.techat.client.push;

import android.text.TextUtils;

import com.igexin.sdk.PushManager;

import ink.techat.client.common.app.Application;
import ink.techat.client.factory.Factory;

/**
 * @author NickCharlie
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Factory调用初始化
        Factory.setup();
        // 推送框架初始化
        com.igexin.sdk.PushManager.getInstance().initialize(getApplicationContext(), ink.techat.client.push.PushService.class);
        com.igexin.sdk.PushManager.getInstance().registerPushIntentService(getApplicationContext(), ink.techat.client.MessageService.class);
    }
}
