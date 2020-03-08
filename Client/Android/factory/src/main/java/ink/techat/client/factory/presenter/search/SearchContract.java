package ink.techat.client.factory.presenter.search;

import java.util.List;

import ink.techat.client.factory.model.card.GroupCard;
import ink.techat.client.factory.model.card.UserCard;
import ink.techat.client.factory.presenter.BaseContract;

public interface SearchContract {
    interface Presenter extends BaseContract.Presenter {
        void search(String content);
    }

    interface UserView extends BaseContract.View<Presenter>{
        void onSearchDone(List<UserCard> userCards);
    }

    interface GroupView extends BaseContract.View<Presenter>{
        void onSearchDone(List<GroupCard> groupCards);
    }
}
