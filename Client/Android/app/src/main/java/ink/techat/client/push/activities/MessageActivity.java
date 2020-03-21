package ink.techat.client.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import ink.techat.client.common.app.Activity;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.factory.model.Author;
import ink.techat.client.factory.model.db.Group;
import ink.techat.client.factory.model.db.Message;
import ink.techat.client.factory.model.db.Session;
import ink.techat.client.push.R;
import ink.techat.client.push.fragments.message.ChatGroupFragment;
import ink.techat.client.push.fragments.message.ChatUserFragment;

/**
 * 聊天界面
 *
 * @author NickCharlie
 */
public class MessageActivity extends Activity {

    @BindView(R.id.message_action_bg)
    ImageView imageView;

    /**
     * KEY_RECEIVER_ID 接收者Id, 可以是群, 也可以是联系人
     * KEY_RECEIVER_IS_GROUP 标识是否为群聊
     */
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    public static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";
    private String mReceiverId;
    private boolean mIsGroup;

    /**
     * 显示对应的会话
     *
     * @param context Context
     * @param session Session
     */
    public static void show(Context context, Session session) {
        if (session == null || context == null || TextUtils.isEmpty((session.getId()))) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, session.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }

    /**
     * 显示与对应联系人的聊天界面
     *
     * @param context Context
     * @param author  联系人的信息
     */
    public static void show(Context context, Author author) {
        if (author == null || context == null || TextUtils.isEmpty((author.getId()))) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        context.startActivity(intent);
    }

    /**
     * 显示与对应群的聊天界面
     *
     * @param context Context
     * @param group   群的信息
     */
    public static void show(Context context, Group group) {
        if (group == null || context == null || TextUtils.isEmpty(group.getId())) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        Fragment fragment;
        if (mIsGroup) {
            fragment = new ChatGroupFragment();
        } else {
            fragment = new ChatUserFragment();
        }
        // 从Activity传递参数到fragment
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_chat_container, fragment)
                .commit();

        Glide.with(this)
                .asBitmap()
                .load(R.drawable.bg_src_default)
                .centerCrop()
                .into(imageView);
    }
}
