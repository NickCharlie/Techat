package ink.techat.client.factory.presenter.user;

import ink.techat.client.factory.presenter.BaseContract;

/**
 * 更新用户信息基本契约
 * @author NickCharlie
 **/
public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter{
        // 更新
        void update(String photoFilePath, String name, int sex, String desc);
    }

    interface View extends BaseContract.View<Presenter>{
        // 更新成功
        void updateSucceed();
    }
}
