package ink.techat.client.push.fragments.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.common.widget.adapter.TextWatcherAdapter;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.MessageActivity;

/**
 * 聊天界面的基本Fragment
 *
 * @author NickCharlie
 */
@SuppressWarnings("WeakerAccess")
public abstract class ChatFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    protected String mReceiverId;

    @BindView(R.id.toolbar_chat_user)
    Toolbar mToolbar;

    @BindView(R.id.appbar_chat_user)
    AppBarLayout mAppBar;

    @BindView(R.id.recycler_chat_user)
    RecyclerView mRecyclerView;

    @BindView(R.id.img_chat_user_portrait)
    PortraitView mPortraitView;

    @BindView(R.id.CollapsingToolbar_chat_user)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.edit_chat_user)
    EditText mContent;

    @BindView(R.id.btn_chat_user_submit)
    View mSubmit;

    @BindView(R.id.btn_chat_user_face)
    View mFace;

    @BindView(R.id.btn_chat_user_record)
    View mRecord;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initToolbar();
        // RecyclerView 基本设置
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAppbar();
        initEditContent();
    }

    protected void initToolbar() {
        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().finish());
    }

    private void initAppbar() {
        mAppBar.addOnOffsetChangedListener(this);
    }

    private void initEditContent() {
        mContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String content = s.toString().trim();
                boolean needSendMessage = !TextUtils.isEmpty(content);
                // 设置状态, 改变对应Icon
                mSubmit.setActivated(needSendMessage);
            }
        });
    }

    /**
     * 进行 CollapsingToolbarLayout 滑动的综合运算, 透明头像和Icon
     *
     * @param appBarLayout   AppBarLayout
     * @param verticalOffset 缩放程度
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @OnClick(R.id.btn_chat_user_face)
    void onFaceClick(){

    }

    @OnClick(R.id.btn_chat_user_record)
    void onRecordClick(){

    }

    @OnClick(R.id.btn_chat_user_submit)
    void onSubmitClick(){
        if (mSubmit.isActivated()) {
            // TODO: 发送西澳西
        } else {
            onMoreClick();
        }
    }

    private void onMoreClick(){
        // TODO: 点击更多的操作
    }
}
