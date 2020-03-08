package ink.techat.client.factory.presenter.contact;

import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.presenter.BaseContract;

public interface ContactContract {

    interface Presenter extends BaseContract.Presenter{

    }

    interface View extends BaseContract.RecyclerView<Presenter, User>{

    }
}
