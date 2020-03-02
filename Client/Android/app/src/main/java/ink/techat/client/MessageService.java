package ink.techat.client;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.data.helper.AccountHelper;
import ink.techat.client.factory.persistence.Account;

/**
 * 个推框架消息接收Service类
 * @author NickCharlie
 */
public class MessageService extends GTIntentService {
    private static final String TAG = MessageService.class.getSimpleName();

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    /**
     * 接收cid
     * @param context Context
     * @param s String
     */
    @Override
    public void onReceiveClientId(Context context, String s) {
        Log.i(TAG, "GET_CLIENTID-设备ID" + s);
        // 设备Id初始化, 获取设备Id
        onClientInit(s);
    }

    /**
     * 处理穿透消息
     * @param context Context
     * @param gtTransmitMessage Message
     */
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {

    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    /**
     * Id初始化
     * @param cid 设备Id
     */
    private void onClientInit(String cid){
        // 设置设备Id
        Account.setPushId(cid);
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
