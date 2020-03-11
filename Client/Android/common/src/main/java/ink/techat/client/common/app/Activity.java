package ink.techat.client.common.app;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import java.util.List;

import butterknife.ButterKnife;
import ink.techat.client.common.widget.convention.PlaceHolderView;


/**
 * @author Nickcharlie
 */
public abstract class Activity extends AppCompatActivity {

    protected PlaceHolderView mPlaceHolderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在界面未初始化之前调用的初始化数据
        initWindows();

        if (initArgs(getIntent().getExtras())) {
            int layoutId = getContentLayoutId();
            setContentView(layoutId);
            initBefore();
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 初始化控件调用之前
     */
    protected void initBefore(){

    }

    /**
     * 初始化窗口
     */
    protected void initWindows() {

    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数bundle
     * @return 如果参数正确返回True，错误返回false
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件ID
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        // 当点击界面导航返回时 finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // 得到当前 Activity 下的所以Fragment
        // 此 Fragment 为 androidx.fragment.app.Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断fragments是否为空
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                // 判断此 fragment 它爸是否为 ink.techat.client.common.app.Fragment 如为false,不能处理
                if (fragment instanceof ink.techat.client.common.app.Fragment) {
                    // 判断是否已经拦截返回按钮 如有，则无需处理
                    if (((ink.techat.client.common.app.Fragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }

    public PlaceHolderView getmPlaceHolderView() {
        return mPlaceHolderView;
    }

    public void setmPlaceHolderView(PlaceHolderView mPlaceHolderView) {
        this.mPlaceHolderView = mPlaceHolderView;
    }
}
