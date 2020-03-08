package ink.techat.client.push.fragments.search;

import ink.techat.client.common.app.PresenterFragment;
import ink.techat.client.factory.presenter.BaseContract;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.SearchActivity;

/**
 * @author NickCharlie
 */
public class SearchGroupFragment extends PresenterFragment implements SearchActivity.SearchFragment {


    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected BaseContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void search(String content) {

    }
}
