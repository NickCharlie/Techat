package ink.techat.client.factory.presenter.contact;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.data.helper.UserHelper;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.persistence.Account;
import ink.techat.client.factory.presenter.BasePresenter;

/**
 * @author NickCharlie
 */
public class PersonalPresenter extends BasePresenter<PersonalContract.View> implements PersonalContract.Presenter {

    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        // 个人界面用户数据, 优先从网络拉取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getmView();
                if (view != null){
                    String id = getmView().getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(view, user);
                }
            }
        });
    }

    /**
     * 进行界面的设置
     * @param view View
     * @param user User
     */
    private void onLoaded(final PersonalContract.View view, final User user){
        this.user = user;
        // 判断是不是自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        // 是否以及关注
        final boolean isFollow = isSelf || user.isFollow();
        // 已经关注, 同时不是自己的情况下可以显示开始聊天
        final boolean allowSayHello = isFollow && !isSelf;
        // 切换到Ui线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                final PersonalContract.View view = getmView();
                if (view != null) {
                    view.onLoadDone(user);
                    view.setFollowStatus(isFollow);
                    view.allowSayHello(allowSayHello);
                }
            }
        });
    }

    @Override
    public User getUserPersonal() {
        return user;
    }


}
