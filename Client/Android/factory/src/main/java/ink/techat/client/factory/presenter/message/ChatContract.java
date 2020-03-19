package ink.techat.client.factory.presenter.message;

import ink.techat.client.factory.model.db.Group;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.presenter.BaseContract;

/**
 * 聊天的基本契约
 */
public interface ChatContract {

    interface Presenter extends BaseContract.Presenter {
        void pushText(String content);
        void pushAudio(String path);
        void pushImage(String[] paths);
        boolean rePush(Message message);
    }

    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {
        void onInit(InitModel model);
    }

    interface UserView extends View<User> {

    }

    interface GroupView extends View<Group> {

    }
}
