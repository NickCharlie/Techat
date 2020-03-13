package ink.techat.client.factory.data.message;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import ink.techat.client.factory.data.group.GroupHelper;
import ink.techat.client.factory.data.helper.DbHelper;
import ink.techat.client.factory.data.helper.UserHelper;
import ink.techat.client.factory.model.card.MessageCard;
import ink.techat.client.factory.model.db.Group;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.model.db.User;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * 消息中心的实现类
 * @author NickCharlie
 */
public class MessageDispatcher implements MessageCenter {

    private static MessageCenter INSTANCE;
    private final Executor executor = newFixedThreadPool(1);
    public static MessageCenter instance(){
        if (INSTANCE != null){
            return INSTANCE;
        }
        synchronized (MessageDispatcher.class){
            if (INSTANCE == null){
                INSTANCE = new MessageDispatcher();
            }
        }
        return INSTANCE;
    }

    @Override
    public void dispatch(MessageCard... cards) {
        if (cards != null && cards.length > 0){
            // 丢到单线程池中
            executor.execute(new MessageCardHandler(cards));
        }
    }

    /**
     * 消息卡片的线程调度处理会触发run方法
     */
    private class MessageCardHandler implements Runnable{
        private final MessageCard[] cards;

        MessageCardHandler(MessageCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Message> messages = new ArrayList<>();
            for (MessageCard card : cards) {
                if (card == null || TextUtils.isEmpty(card.getSenderId())
                        || TextUtils.isEmpty(card.getId())
                        || (TextUtils.isEmpty(card.getReceiverId())
                        && TextUtils.isEmpty(card.getGroupId()))) {
                    continue;
                }
                // 消息卡片有可能是推送到客户端，也有可能是直接造的
                // 推送到客户端的卡片服务器一定会有, 可通过网络查询
                // 如果是自己造的, 先储存本地, 后发送网络
                // 写消息 --> 存储本地 --> 发送到服务端 --> 服务端返回 --> 刷新本地状态
                Message message = MessageHelper.findFromLocal(card.getId());
                if (message != null) {
                    // 如果已经完成则不做处理
                    // 本地有, 同时本地显示的消息状态为完成状态, 则不必处理
                    // 因为此时回来的消息和本地一定一模一样
                    // 如果本地消息显示完成则不做处理
                    if (message.getStatus() == Message.STATUS_DONE) {
                        continue;
                    }
                    // 新状态为完成才更新服务器时间，不然不做更新
                    if (card.getStatus() == Message.STATUS_DONE) {
                        // 代表网络发送成功, 此时需要修改时间为服务器时间
                        message.setCreateAt(card.getCreateAt());
                    }
                    // 更新一些会变化的内容
                    message.setContent(card.getContent());
                    message.setAttach(card.getAttach());
                    message.setStatus(card.getStatus());
                } else {
                    // 没找到本地消息执行的操作, 初次在数据库存储
                    User sender = UserHelper.search(card.getSenderId());
                    User receiver = null;
                    Group group = null;
                    if (!TextUtils.isEmpty(card.getReceiverId())) {
                        receiver = UserHelper.search(card.getReceiverId());
                    } else if (!TextUtils.isEmpty(card.getGroupId())) {
                        group = GroupHelper.findFromLocal(card.getGroupId());
                    }
                    if (receiver == null && group == null && sender != null) {
                        continue;
                    }

                    message = card.build(sender, receiver, group);
                }
                messages.add(message);
            }
            if (messages.size() > 0) {
                DbHelper.save(Message.class, messages.toArray(new Message[0]));
            }
        }
    }
}
