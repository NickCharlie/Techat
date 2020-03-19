package ink.techat.client.push.fragments.message;

import ink.techat.client.factory.model.db.Group;
import ink.techat.client.factory.presenter.message.ChatContract;
import ink.techat.client.push.R;

/**
 * 与联系人聊天的界面
 * @author NickCharlie
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {

    public ChatGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}
