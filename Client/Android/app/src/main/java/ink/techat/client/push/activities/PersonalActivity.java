package ink.techat.client.push.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.app.PersenterToolbarActivity;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.presenter.contact.PersonalContract;
import ink.techat.client.factory.presenter.contact.PersonalPresenter;
import ink.techat.client.push.R;

/**
 * @author NickCharlie
 */
public class PersonalActivity extends PersenterToolbarActivity<PersonalContract.Presenter> implements PersonalContract.View {
    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String userId;

    @BindView(R.id.img_personal_header)
    ImageView mHeader;
    @BindView(R.id.img_personal_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_personal_name)
    TextView mName;
    @BindView(R.id.txt_personal_desc)
    TextView mDesc;
    @BindView(R.id.txt_personal_follows)
    TextView mFollows;
    @BindView(R.id.txt_personal_following)
    TextView mFollowing;
    @BindView(R.id.btn_say_hello)
    Button mSayHello;

    private MenuItem mFollowItem;
    private boolean mIsFollowUser = false;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
    }

    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.personal, menu);
        mFollowItem = menu.findItem(R.id.action_follow);
        changeFollowItemStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_follow) {
            // 进行关注操作
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_say_hello)
    void onSayHelloClick() {
        // 发起聊天的点击
        User user = mPresenter.getUserPersonal();
        if (user == null) {
            return;
        }
        MessageActivity.show(this, user);
    }

    @Override
    public String getUserId() {
        return userId;
    }

    private void changeFollowItemStatus(){
        if (mFollowItem == null){
            return;
        }
        Drawable drawable = mIsFollowUser ?
                getResources().getDrawable(R.drawable.ic_favorite):
                getResources().getDrawable(R.drawable.ic_favorite_border);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, UiCompat.getColor(getResources(), R.color.white));
        mFollowItem.setIcon(drawable);
        mFollowItem.setVisible(true);
    }


    @SuppressLint("StringFormatMatches")
    @Override
    public void onLoadDone(User user) {
        if (user == null){
            return;
        }
        mPortrait.setup(Glide.with(this), user);
        mName.setText(user.getName());
        mDesc.setText(user.getDescription());
        mFollows.setText(String.format(getString(R.string.label_follows), user.getFollows()));
        mFollowing.setText(String.format(getString(R.string.label_following), user.getFollowing()));
        hideLoading();
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        mIsFollowUser = isFollow;
        changeFollowItemStatus();
    }

    @Override
    public void allowSayHello(boolean isAllow) {
        mSayHello.setVisibility(isAllow?View.VISIBLE:View.GONE);
    }
}
