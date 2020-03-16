package ink.techat.client.factory.presenter;

import java.util.List;

import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.data.DbDataSource;

/**
 * 基础的仓库源的Presenter
 * @author NickCharlie
 */
public class BaseSourcePresenter<Data, ViewModel,Source extends DbDataSource<Data>, View extends BaseContract.RecyclerView>
                extends BaseRecyclerPresenter<ViewModel, View> implements DataSource.SucceedCallback<List<Data>> {

    protected Source mSource;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null){
            mSource.load(this);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }

    @Override
    public void onDataLoaded(List<Data> data) {

    }
}
