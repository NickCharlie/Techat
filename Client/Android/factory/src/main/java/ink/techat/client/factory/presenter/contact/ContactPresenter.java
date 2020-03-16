package ink.techat.client.factory.presenter.contact;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;

import ink.techat.client.common.widget.recycler.RecycierAdapter;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.data.helper.UserHelper;
import ink.techat.client.factory.data.user.ContactDataSource;
import ink.techat.client.factory.data.user.ContactRepository;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.presenter.BaseSourcePresenter;
import ink.techat.client.factory.utils.DiffUiDataCallback;

/**
 * 联系人的逻辑实现
 * @author NickCharlie
 */
public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View>
                    implements ContactContract.Presenter, DataSource.SucceedCallback<List<User>> {

    public ContactPresenter(ContactContract.View view) {
        // 初始化数据仓库
        super(new ContactRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        // 进行本地的数据加载并且添加监听
        mSource.load(this);
        // 加载网络数据
        UserHelper.refreshContacts();
    }

    private void diff(List<User> oldList, List<User> newList){
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldList, newList);
        DiffUtil.DiffResult result= DiffUtil.calculateDiff(callback);

        // 在对比完成后进行数据赋值
        getmView().getRecyclerAdapter().replace(newList);
        // 尝试刷新界面
        result.dispatchUpdatesTo(getmView().getRecyclerAdapter());
        getmView().onAdapterDataChanged();
    }

    @Override
    public void onDataLoaded(List<User> users) {
        List<User> list = users;
        // 数据变更最终会通知到onDataLoaded
        final ContactContract.View view = getmView();
        if (view == null){
            return;
        }
        RecycierAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();

        // 进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, list);
        DiffUtil.DiffResult result= DiffUtil.calculateDiff(callback);

        // 调用基类方法进行界面刷新
        refreshData(result, users);
    }
}
