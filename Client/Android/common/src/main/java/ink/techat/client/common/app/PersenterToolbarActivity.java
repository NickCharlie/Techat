package ink.techat.client.common.app;

import ink.techat.client.factory.presenter.BaseContract;

/**
 * @author NickCharlie
 */
public abstract class PersenterToolbarActivity<Presenter extends BaseContract.Presenter> extends ToolbarActivity
            implements BaseContract.View<Presenter>{

    protected Presenter mPresenter;

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    protected void initBefore() {
        super.initBefore();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.destroy();
        }
    }

    /**
     * 初始化Presenter
     * @return Presenter
     */
    protected abstract Presenter initPresenter();


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

    protected void hideLoading(){
        if (mPlaceHolderView != null){
            mPlaceHolderView.triggerOk();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        // View中赋值Presenter
        mPresenter = presenter;
    }
}
