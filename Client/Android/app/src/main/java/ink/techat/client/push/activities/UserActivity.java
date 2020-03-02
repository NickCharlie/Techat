package ink.techat.client.push.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;
import ink.techat.client.common.app.Activity;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.push.R;
import ink.techat.client.push.fragments.user.UpdateInfoFragment;

/**
 * 用户信息界面
 * @author NickCharlie
 */
public class UserActivity extends Activity {
    private Fragment mCurFragment;

    @BindView(R.id.img_user_bg)
    ImageView mBackground;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();

        // 初始化背景
        Glide.with(this)
                .load("https://api.xygeng.cn/bing/1366.php")
                .centerCrop()
                .into(new ViewTarget<ImageView, Drawable>(mBackground) {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // 获取Glide的Drawable
                        Drawable drawable = resource.getCurrent();
                        // 用适配类进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        // 设置着色,着色的效果的颜色,蒙版模式
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent),
                                PorterDuff.Mode.SCREEN);
                        // 设置给ImageView
                        this.view.setImageDrawable(drawable);
                    }
                });

    }

    /**
     * 入口方法
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, UserActivity.class));
    }

    /**
     * Activity中收到剪切图片收到成功的回调
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }
}
