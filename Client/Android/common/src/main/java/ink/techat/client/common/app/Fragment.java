package ink.techat.client.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ink.techat.client.common.widget.convention.PlaceHolderView;

/**
 * @author Nickcharlie
 * 封装了一个新的Fragment类
 * extends androidx.fragment.app.Fragment
 */
public abstract class Fragment extends androidx.fragment.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnBinder;
    protected PlaceHolderView mPlaceHolderView;
    protected boolean mIsFirstInitData = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 初始化参数
        initArgs(getArguments());
    }

    /**
     * initArgs(Bundle bundle)
     * 初始化相关参数
     *
     * @param bundle 参数bundle
     * @return 如果参数正确返回True，错误返回false
     */
    protected void initArgs(Bundle bundle) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前的根布局，但是不在创建时就添加到container里边
            View root = inflater.inflate(layId, container, false);
            // 初始化控件
            initWidget(root);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                // 把当前Root从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitData){
            // 首次初始化数据
            onFirstInitData();
        }
        // 初始化数据
        initData();
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root) {
        mRootUnBinder = ButterKnife.bind(this, root);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 首次初始化数据
     */
    protected void onFirstInitData() {

    }

    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表已经处理返回逻辑，Activity不用关心
     * 返回False代表没有处理返回逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

    public PlaceHolderView getmPlaceHolderView() {
        return mPlaceHolderView;
    }

    public void setmPlaceHolderView(PlaceHolderView mPlaceHolderView) {
        this.mPlaceHolderView = mPlaceHolderView;
    }
}
