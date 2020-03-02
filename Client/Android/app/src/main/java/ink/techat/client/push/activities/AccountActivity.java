package ink.techat.client.push.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.compat.UiCompat;

import ink.techat.client.common.app.Application;
import ink.techat.client.push.fragments.account.RegisterFragment;
import butterknife.BindView;
import ink.techat.client.common.app.Activity;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.push.R;
import ink.techat.client.push.fragments.account.AccountTrigger;
import ink.techat.client.push.fragments.account.LoginFragment;

/**
 * @author NickCharlie
 */
public class AccountActivity extends Activity implements AccountTrigger {
    private Fragment mCurFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;

    @BindView(R.id.img_bg)
    ImageView mBackground;

    /**
     * 账户Activity显示的入口
     * @param context Context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        // 初始化Fragment
        mCurFragment = mLoginFragment = new LoginFragment();
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


    @SuppressWarnings("ThrowableNotThrown")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                ((RegisterFragment)mRegisterFragment).loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown);
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment){
            if (mRegisterFragment == null){
                // 默认情况下为null，第一次之后不为空
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        }else {
            fragment = mLoginFragment;
        }
        // 重新赋值当前正在显示的Fragment
        mCurFragment = fragment;
        // commit切换显示
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container, fragment)
                .commit();
    }

}

