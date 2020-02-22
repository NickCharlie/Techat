package ink.techat.client.push.fragments.main;


import android.util.Log;

import butterknife.BindView;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.common.widget.GalleyView;
import ink.techat.client.common.widget.GalleyView.SelectedChangeListener;
import ink.techat.client.push.R;

/**
 * @author NickCharlie
 */
public class ActiveFragment extends Fragment {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }
    /*@BindView(R.id.galleyView)
    GalleyView mGalley;

    @Override
    protected void initData() {
        super.initData();
        Log.i("TEST_mGalley",mGalley == null ? "yes":"no");
        mGalley.setup(getLoaderManager(), new SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }*/
}
