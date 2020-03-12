package ink.techat.client.factory.data.message;

import java.util.concurrent.Executor;

import ink.techat.client.factory.model.card.MessageCard;

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

        }
    }
}
