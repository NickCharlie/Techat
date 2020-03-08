package ink.techat.client.factory.presenter.contact;

import ink.techat.client.factory.model.card.UserCard;
import ink.techat.client.factory.presenter.BaseContract;

/**
 * @author NickCharlie
 */
public interface FollowContract {
    /**
     * 任务调度
     */
    interface Presenter extends BaseContract.Presenter{
        void follow(String userId);
    }

    interface View extends BaseContract.View<Presenter>{
        void onFollowSucceed(UserCard userCard);
    }
}
