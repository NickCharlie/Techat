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
 * ��Ϣ���͹�����
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
     * ���һ����Ϣ
     * @param receiver ������
     * @param model ���յ�����Model
     * @return �Ƿ���ӳɹ�
     */
    public boolean add(User receiver, PushModel model) {
        // �������
        if (receiver == null || model == null || Strings.isNullOrEmpty(receiver.getPushId())) {
            return false;
        }
        String pushString = model.getPushString();
        if (Strings.isNullOrEmpty(pushString)) {
            return false;
        }
        // ����һ��Ŀ������ݵĶ���
        BatchBean bean = buildMessage(receiver.getPushId(), pushString);
        beans.add(bean);
        return true;
    }

    /**
     * ��Ҫ���͵����ݽ��и�ʽ����װ
     *
     * @param clientId ������pushId
     * @param text     ����
     * @return BatchBean
     */
    private BatchBean buildMessage(String clientId, String text) {
        // ����͸����Ϣ, ��֪ͨ����ʾ����Ϣ, ���͵���Ϣ��app���д���
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(APP_ID);
        template.setAppkey(APP_KEY);
        // ���ݵ���Ϣ����
        template.setTransmissionContent(text);

        // ���TypeΪint�ͣ���д1���Զ�����app
        template.setTransmissionType(0);

        SingleMessage message = new SingleMessage();
        // ��͸����Ϣ���õ�����Ϣģ����
        message.setData(template);
        // �Ƿ��������߷���
        message.setOffline(true);
        // ������Ϣʱ��
        message.setOfflineExpireTime(MAX_OFFLINE_MESSAGE_SAVE_TIME);

        // ��������Ŀ�꣬����appId��clientId
        Target target = new Target();
        target.setAppId(APP_ID);
        target.setClientId(clientId);

        return new BatchBean(target, message);
    }

    /**
     * ��Ϣ�����ύ
     */
    public boolean submit() {
        // ��������Ĺ�����
        IBatch batch = pusher.getBatch();
        // �Ƿ���������Ҫ����
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
            // ʧ�ܺ��ظ�����
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
                .log(Level.WARNING, "���ͷ�������Ӧ�쳣");
        return false;
    }

    /**
     * ��ÿ���˷�����Ϣ��һ��Bean��װ
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
