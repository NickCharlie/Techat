package ink.techat.client.factory.presenter.message;

import ink.techat.client.factory.model.db.Session;
import ink.techat.client.factory.presenter.BaseContract;


public interface SessionContract {

    interface Presenter extends BaseContract.Presenter{

    }

    interface View extends BaseContract.RecyclerView<Presenter, Session>{

    }
}
