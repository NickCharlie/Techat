package ink.techat.web.push.utils;

import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.google.common.base.Strings;
import ink.techat.web.push.bean.api.base.PushModel;
import ink.techat.web.push.bean.db.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 消息推送工具类
 *
 * @author NickCharlie
 */
public class PushDispatcher {

    private static final String CID = "";
    private static final String APP_ID = "BZZFmUq8186xXe6Qs6f4r5";
    private static final String APP_KEY = "2lpS3g5HgR8uJCYinCZPz2";
    private static final String MASTER_SECRET = "LBo1JvFPV27E9qCObFSWx5";
    private static final String HOST = "http://sdk.open.api.igexin.com/apiex.htm";
    private static final int MAX_OFFLINE_MESSAGE_SAVE_TIME = 24 * 3600 * 1000;

    private final IGtPush pusher;
    private final List<BatchBean> beans = new ArrayList<>();

    public PushDispatcher() {
        pusher = new IGtPush(HOST, APP_KEY, MASTER_SECRET);
    }

    /**
     * 添加一条消息
     * @param receiver 接受者
     * @param model 接收的推送Model
     * @return 是否添加成功
     */
    public boolean add(User receiver, PushModel model) {
        // 基础检查
        if (receiver == null || model == null || Strings.isNullOrEmpty(receiver.getPushId())) {
            return false;
        }
        String pushString = model.getPushString();
        if (Strings.isNullOrEmpty(pushString)) {
            return false;
        }
        // 构建一个目标加内容的对象
        BatchBean bean = buildMessage(receiver.getPushId(), pushString);
        beans.add(bean);
        return true;
    }

    /**
     * 对要发送的数据进行格式化封装
     *
     * @param clientId 接收者pushId
     * @param text     内容
     * @return BatchBean
     */
    private BatchBean buildMessage(String clientId, String text) {
        // 构建透传消息, 非通知栏显示的消息, 推送的消息由app自行处理
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(APP_ID);
        template.setAppkey(APP_KEY);
        // 传递的消息内容
        template.setTransmissionContent(text);

        // 这个Type为int型，填写1则自动启动app
        template.setTransmissionType(0);

        SingleMessage message = new SingleMessage();
        // 把透传消息设置到单消息模板中
        message.setData(template);
        // 是否允许离线发送
        message.setOffline(true);
        // 离线消息时长
        message.setOfflineExpireTime(MAX_OFFLINE_MESSAGE_SAVE_TIME);

        // 设置推送目标，填入appId和clientId
        Target target = new Target();
        target.setAppId(APP_ID);
        target.setClientId(clientId);

        return new BatchBean(target, message);
    }

    /**
     * 消息最终提交
     */
    public boolean submit() {
        // 构建打包的工具类
        IBatch batch = pusher.getBatch();
        // 是否有数据需要发送
        boolean isHaveData = false;

        try {
            for (BatchBean bean : beans) {
                batch.add(bean.message, bean.target);
                isHaveData = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isHaveData) {
            return false;
        }
        IPushResult result = null;
        try {
            result = batch.submit();
        } catch (IOException e) {
            e.printStackTrace();
            // 失败后重复发送
            try {
                batch.retry();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (result != null) {
            try {
                Logger.getLogger("PushDispatcher")
                        .log(Level.INFO, (String) result.getResponse().get("result"));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Logger.getLogger("PushDispatcher")
                .log(Level.WARNING, "推送服务器响应异常");
        return false;
    }

    /**
     * 给每个人发送消息的一个Bean封装
     */
    private static class BatchBean {
        Target target;
        SingleMessage message;

        public BatchBean(Target target, SingleMessage message) {
            this.target = target;
            this.message = message;
        }
    }
}
