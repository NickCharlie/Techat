package ink.techat.client.push.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.qiujuer.genius.ui.Ui;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.app.Activity;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.factory.persistence.Account;
import ink.techat.client.push.R;
import ink.techat.client.push.fragments.main.ActiveFragment;
import ink.techat.client.push.fragments.main.ContactFragment;
import ink.techat.client.push.fragments.main.GroupFragment;
import ink.techat.client.push.helper.NavHelper;

import static ink.techat.client.factory.persistence.Account.getUser;

/**
 * @author NickCharlie
 */
public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangeListener<Integer> {

    /**
     * 顶部栏绑定 @BindView(R.id.appbar)
     * 头像绑定   @BindView(R.id.img_portrait)
     * 标题绑定   @BindView(R.id.txt_title)
     * 容器绑定   @BindView(R.id.lay_container)
     * 导航栏绑定 @BindView(R.id.navigation)
     * 浮动钮绑定 @BindView(R.id.btn_action)
     */
    @BindView(R.id.appbar)
    View mLayAppbar;
    @BindView(R.id.img_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.lay_container)
    FrameLayout mContainer;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.btn_action)
    FloatingActionButton mAction;

    private NavHelper<Integer> mNavHelper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * MainActivity 显示的入口
     *
     * @param context 环境上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void initData() {
        super.initData();
        // 从底部导航接管Menu,手动触发第一次点击Home
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home, 0);

        if (!TextUtils.isEmpty(getUser().getPortrait())){
            mPortrait.setup(Glide.with(this), Account.getUser());
        }
    }

    /**
     * initWidget()
     * 用于初始化界面和应用的一些必要参数
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container,
                getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_home,
                new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_contact,
                        new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact))
                .add(R.id.action_group,
                        new NavHelper.Tab<>(GroupFragment.class, R.string.title_group));

        //设置底部导航Item点击监听
        mNavigation.setOnNavigationItemSelectedListener(this);

        //自定义Background
        int isPsnlBackground = 0;
        // 自定义背景设置
        if (isPsnlBackground != 0) {
            Glide.with(this)
                    .asDrawable()
                    .load(isPsnlBackground)
                    .centerCrop()
                    .into(new CustomViewTarget<View, Drawable>(mLayAppbar) {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            this.view.setBackground(resource);
                        }

                        @Override
                        protected void onResourceCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }

    /**
     * Search按钮的点击事件
     */
    @OnClick(R.id.img_search)
    void onSearchMenuClick() {
        int type = Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group) ?
        SearchActivity.TYPE_GROUP : SearchActivity.TYPE_USER;

        SearchActivity.show(this, type);
    }

    @OnClick(R.id.img_portrait)
    void onPortraitClick() {
        if (Account.isComplete()) {
            PersonalActivity.show(this, Account.getUserId());
        }else {
            UserActivity.show(this);
        }
    }

    /**
     * 添加按钮的点击事件
     */
    @OnClick(R.id.btn_action)
    void onActionClick() {
        if (Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group)){
            // TODO: 打开群创建界面
        }else {
            SearchActivity.show(this, SearchActivity.TYPE_USER);
        }

    }

    /**
     * onNavigationItemSelected(@NonNull MenuItem menuItem)
     * 底部导航栏Item点击Selected事件
     * 当底部导航栏被点击时触发
     *
     * @param menuItem 自动传入的MenuItem
     * @return boolean True or False 是否处理点击事件
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // 把事件流交给Helper工具类
        return mNavHelper.performClickMenu(menuItem.getItemId());
    }

    /**
     * onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab)
     * NavHelper 处理后回调的方法
     *
     * @param newTab NavHelper.Tab<Integer> 新的Tab
     * @param oldTab NavHelper.Tab<Integer> 旧的Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        // 从额外字段中取出Title资源ID
        mTitle.setText(newTab.extra);

        // 对浮动按钮进行隐藏与显示的动画
        float transY = 0;
        float rotation = 0;
        if (newTab.extra.equals(R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
            rotation = -360;
        } else {
            if (newTab.extra.equals(R.string.title_group)) {
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
            transY = 0;
        }

        // 开始动画
        // 旋转, Y轴位移, 差值器
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();
    }
}
