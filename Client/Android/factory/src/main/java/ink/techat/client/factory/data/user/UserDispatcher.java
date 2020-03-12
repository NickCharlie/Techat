package ink.techat.client.factory.data.user;


import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import ink.techat.client.factory.data.helper.DbHelper;
import ink.techat.client.factory.model.card.UserCard;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.utils.CollectionUtil;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @author NickCharlie
 */
public class UserDispatcher implements UserCenter {

    private static UserCenter INSTANCE;
    private final Executor executor = newFixedThreadPool(1);
    public static UserCenter instance(){
        if (INSTANCE != null){
            return INSTANCE;
        }
        synchronized (UserDispatcher.class){
            if (INSTANCE == null){
                INSTANCE = new UserDispatcher();
            }
        }
        return INSTANCE;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards != null && cards.length > 0){
            // 丢到单线程池中
            executor.execute(new UserCardHandler(cards));
        }
    }

    /**
     * 老子的 UserCardHandler implements Runnable
     * 线程调度的时候会触发run方法
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private class UserCardHandler implements Runnable{
        private final UserCard[] cards;

        private UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                // 进行card过滤操作
                if (card == null || TextUtils.isEmpty(card.getId())){
                    continue;
                }
                // 添加一个User
                users.add(card.build());
            }
            // 进行数据库存储并且分发, 异步
            DbHelper.save(User.class, CollectionUtil.toArray(users, User.class));
        }
    }
}
