package ink.techat.client.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.app.ToolbarActivity;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.push.R;

/**
 * @author NickCharlie
 */
public class PersonalActivity extends ToolbarActivity {
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

    private MenuItem mFollow;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.personal, menu);
        mFollow = menu.findItem(R.id.action_follow);
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
        // TODO
        //MessageActivity.show(this, null);
    }
}
