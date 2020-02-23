package ink.techat.client.push.fragments.account;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import ink.techat.client.common.app.Fragment;
import ink.techat.client.push.R;

/**
 * 用于注册的Fragment
 * @author NickCharlie
 */
public class RegisterFragment extends Fragment {
    private AccountTrigger mAccountTrigger;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 拿到Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

}