package ink.techat.client.factory.presenter.contact;

import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.presenter.BaseContract;

/**
 * @author NickCharlie
 */
public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter{
        // 获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter>{

        String getUserId();
        // 加载数据完成
        void onLoadDone(User user);
        // 是否显示发起聊天
        void allowSayHello(boolean isAllow);
        // 设置关注状态
        void setFollowStatus(boolean isFollow);
    }
}
