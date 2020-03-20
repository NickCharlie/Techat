package ink.techat.client.factory.presenter;

import ink.techat.client.factory.model.db.User;

/**
 * @author NickCharlie
 * @param <T> T
 */
public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter{
    private T mView;
    protected User globalUser;
    public BasePresenter(T view){
        setView(view);
    }

    /**
     * 设置View方法
     * @param view View
     */
    @SuppressWarnings("unchecked")
    protected void setView(T view){
        this.mView = view;
        this.mView.setPresenter(this);
    }

    /**
     * 给子类使用的获取View的操作
     * @return View
     */
    @SuppressWarnings("SpellCheckingInspection")
    protected final T getmView(){
        return mView;
    }

    @Override
    public void start() {
        // 开始的时候进行Loading调用
        T view = mView;
        if(view != null){
            view.showLoading();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        if(view != null){
            // 把Presenter设为空
            view.setPresenter(null);
        }
    }
}
