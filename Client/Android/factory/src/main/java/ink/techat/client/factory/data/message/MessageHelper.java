package ink.techat.client.factory.data.message;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.model.api.RspModel;
import ink.techat.client.factory.model.api.message.MessageCreateModel;
import ink.techat.client.factory.model.card.MessageCard;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.model.db.Message_Table;
import ink.techat.client.factory.net.Network;
import ink.techat.client.factory.net.RemoteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息辅助工具类
 *
 * @author NickCharlie
 */
public class MessageHelper {

    public static Message findFromLocal(String id) {
        return SQLite.select().from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    /**
     * 发送消息, 异步进行
     *
     * @param model MessageCreateModel
     */
    public static void push(final MessageCreateModel model) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                // 如果是一个已经发送过的消息, 则不能重新发送
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED) {
                    return;
                }
                // TODO 如果是文件类型的消息 (语音, 图片, 文件), 需要先上传后再发送
                // 直接发送, 进行网络调度
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);

                RemoteService service = Network.remote();
                service.messagePush(model).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            Log.i("消息来啦:", rspCard.toString());
                            if (rspCard != null) {
                                // 成功的调度
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            // 检查是否为账户异常
                            Factory.decodeRspCode(rspModel, null);
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        // 通知失败
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * 查询一个群中的最后一条消息
     *
     * @param groupId 群Id
     * @return 群中聊天的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {
        return SQLite.select().from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    /**
     * 查询与一个联系人聊天的最后一条消息
     *
     * @param userId userId
     * @return 联系人聊天的最后一条消息
     */
    public static Message findLastWithUser(String userId) {
        return SQLite.select().from(Message.class)
                .where(OperatorGroup.clause().and(Message_Table.sender_id.eq(userId)))
                .and(Message_Table.group_id.isNull())
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }
}
