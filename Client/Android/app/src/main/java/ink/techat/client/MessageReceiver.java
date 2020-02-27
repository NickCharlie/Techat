package ink.techat.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.data.helper.AccountHelper;
import ink.techat.client.factory.persistence.Account;

/**
 * 个推框架, 广播消息接收器
 * @author NicKCharlie
 */
@SuppressWarnings("SingleStatementInBlock")
public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null){
            return;
        }
        Bundle bundle = intent.getExtras();
        // 判断当前消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:{
                Log.i(TAG, "GET_CLIENTID-设备ID" + bundle.toString());
                // 设备Id初始化, 获取设备Id
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA:{
                // 常规消息送达
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null){
                    String message = new String(payload);
                    Log.i(TAG, "GET_MSG_DATA-消息送达" + message);
                    onMessageArrived(message);
                }
                break;
            }
            default:
                Log.i(TAG, "OTHER其他消息:" + bundle.toString());
                break;
        }
    }

    /**
     * Id初始化
     * @param pid 设备Id
     */
    private void onClientInit(String pid){
        // 设置设备Id
        Account.setPushId(pid);
        if (Account.isLogin()){
            // 判断账户登录状态, 绑定pushId 不在登录状态则不能绑定pushId
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息送达处理
     * @param message Message
     */
    private void onMessageArrived(String message){
        // 把收到的消息交给Factory处理
        Factory.dispatchPush(message);
    }
}
