package ink.techat.client.push.fragments.message;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.presenter.message.ChatContract;
import ink.techat.client.factory.presenter.message.ChatUserPresenter;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.PersonalActivity;

/**
 * 与联系人聊天的界面
 *
 * @author NickCharlie
 */
public class ChatUserFragment extends ChatFragment<User> implements ChatContract.UserView {

    @BindView(R.id.img_chat_user_portrait)
    PortraitView mPortrait;

    private MenuItem mUserInfoMenuItem;

    public ChatUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        // 初始化Presenter
        return new ChatUserPresenter(this, mReceiverId);
    }

    @Override
    public void onInit(User user) {
        // 对和你聊天的联系人的信息进行初始化
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        Toolbar toolbar = mToolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_person) {
                onPortraitClick();
            }
            return false;
        });
        // 拿到菜单Icon
        mUserInfoMenuItem = toolbar.getMenu().findItem(R.id.action_person);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = mPortraitView;
        MenuItem menuItem = mUserInfoMenuItem;
        if (view == null || menuItem == null){
            return;
        }
        if (verticalOffset == 0) {
            // 完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);
            // 隐藏菜单
            menuItem.setVisible(false);
            menuItem.getIcon().setAlpha(0);
        } else {
            verticalOffset = Math.abs(verticalOffset);
            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {
                // 完全关闭
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);
                // 显示菜单
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255);
            } else {
                // 中间状态
                float progress = 1 - (verticalOffset / (float) totalScrollRange);
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha((int) (255 - (255 * progress)));
            }
        }
    }

    @OnClick(R.id.img_chat_user_portrait)
    void onPortraitClick() {
        PersonalActivity.show(getContext(), mReceiverId);
    }
}
