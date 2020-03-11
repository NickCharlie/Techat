package ink.techat.client.common.app;

import android.content.Context;

import androidx.annotation.NonNull;

import ink.techat.client.factory.presenter.BaseContract;

/**
 * @author NickCharlie
 */
public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    /**
     * 初始化Presenter
     * @return Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // 界面onAttach之后就触发初始化Presenter
        initPresenter();
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    @Override
    public void showError(int str) {
        if (mPlaceHolderView != null){
            mPlaceHolderView.triggerError(str);
            return;
        }else {
            // 显示错误
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        // TODO: 显示一个Loading
        if (mPlaceHolderView != null){
            mPlaceHolderView.triggerLoading();
        }
    }



    @Override
    public void setPresenter(Presenter presenter) {
        // View中赋值Presenter
        mPresenter = presenter;
    }
}
