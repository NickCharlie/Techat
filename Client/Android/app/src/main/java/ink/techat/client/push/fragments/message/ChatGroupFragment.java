package ink.techat.client.push.fragments.message;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ink.techat.client.push.R;

/**
 * 与联系人聊天的界面
 * @author NickCharlie
 */
public class ChatGroupFragment extends ChatFragment {

    public ChatGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }
}
