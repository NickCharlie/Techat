package ink.techat.client.push.fragments.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.app.PresenterFragment;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.common.widget.adapter.TextWatcherAdapter;
import ink.techat.client.common.widget.recycler.RecycierAdapter;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.persistence.Account;
import ink.techat.client.factory.presenter.message.ChatContract;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.MessageActivity;

/**
 * 聊天界面的基本Fragment
 *
 * @author NickCharlie
 */
@SuppressWarnings({"WeakerAccess", "AlibabaAbstractClassShouldStartWithAbstractNaming"})
public abstract class ChatFragment<InitModel> extends PresenterFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener,
        ChatContract.View<InitModel> {

    protected String mReceiverId;
    protected Adapter mAdapter;

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
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        initAppbar();
        initEditContent();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
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
    void onFaceClick() {

    }

    @OnClick(R.id.btn_chat_user_record)
    void onRecordClick() {

    }

    @OnClick(R.id.btn_chat_user_submit)
    void onSubmitClick() {
        if (mSubmit.isActivated()) {
            // 发送
            String content = mContent.getText().toString();
            mContent.setText("");
            mPresenter.pushText(content);
        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {
        // TODO: 点击更多的操作
    }

    @Override
    public RecycierAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 界面没有展位布局, 是一直显示的, 不需要做处理
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private class Adapter extends RecycierAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {

            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());

            switch (message.getType()) {
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_photo_right : R.layout.cell_chat_photo_left;
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                case Message.TYPE_FILE:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateiewHolder(View root, int viewType) {
            switch (viewType) {
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextHolder(root);

                case R.layout.cell_chat_photo_right:
                case R.layout.cell_chat_photo_left:
                    return new PhotoHolder(root);

                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);

                default: return new TextHolder(root);
            }
        }
    }

    /**
     * Holder的基类
     */
    class BaseHolder extends RecycierAdapter.ViewHolder<Message> {

        @BindView(R.id.img_chat_portrait)
        PortraitView mPortrait;

        @Nullable
        @BindView(R.id.chat_loading)
        Loading mLoading;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            // 由于数据库里定义了sender的内容为懒加载, 所以这里需要手动加载
            sender.load();
            // 头像加载
            mPortrait.setup(Glide.with(ChatFragment.this), sender);
            int status = message.getStatus();

            if (mLoading != null) {
                // 当前布局是在右边
                if (status == Message.STATUS_DONE) {
                    // 发送成功, 隐藏Loading
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    // 正在发送中, 转圈圈
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    // 发送失败, Loading显示红色, 允许重新发送
                    mLoading.stop();
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.red_400));
                }
                // 发送失败时, 允许点击重新发送
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @OnClick(R.id.img_chat_portrait)
        void onRePushClick() {
            // 重新发送消息
            if (mLoading != null) {
                // TODO: 重新发送
            }
        }

    }

    /**
     * 文字的Holder
     */
    class TextHolder extends BaseHolder {

        @BindView(R.id.text_chat_content)
        TextView mContent;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // 把内容设置到布局上
            mContent.setText(message.getContent());
        }
    }

    /**
     * 图片的Holder
     */
    class PhotoHolder extends BaseHolder {


        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }

    /**
     * 音频的Holder
     */
    class AudioHolder extends BaseHolder {

        @BindView(R.id.text_chat_content)
        TextView mContent;

        public AudioHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }

    /**
     * 文件的Holder
     */
    class FileHolder extends BaseHolder {

        @BindView(R.id.text_chat_content)
        TextView mContent;

        public FileHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }
}
