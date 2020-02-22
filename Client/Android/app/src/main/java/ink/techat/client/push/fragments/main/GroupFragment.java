package ink.techat.client.push.fragments.main;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ink.techat.client.push.R;

/**
 * @author NickCharlie
 */
public class GroupFragment extends ink.techat.client.common.app.Fragment {


    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

}
