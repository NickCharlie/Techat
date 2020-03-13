package ink.techat.client.factory.presenter;

import androidx.recyclerview.widget.DiffUtil;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import ink.techat.client.common.widget.recycler.RecycierAdapter;

/**
 * 对RecyclerView进行的一个简单的Presenter封装
 * @author NickCharlie
 * @param <View> View
 */
@SuppressWarnings("unchecked")
public class BaseRecyclerPresenter<ViewMode, View extends BaseContract.RecyclerView> extends BasePresenter<View> {

    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    /**
     * 刷新一堆新数据到界面中
     * @param dataList 新数据
     */
    protected void refreshData(final List<ViewMode> dataList){
        Run.onUiAsync(() -> {
            View view = getmView();
            if (view == null){
                return;
            }
            // 基本的更新数据并刷新界面
            RecycierAdapter<ViewMode> adapter = view.getRecyclerAdapter();
            adapter.replace(dataList);
            view.onAdapterDataChanged();
        });
    }

    /**
     * 刷新界面操作, 执行方法在主线程进行
     * @param diffResult 一个差异的结果集
     * @param dataList 新数据
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<ViewMode> dataList){
        Run.onUiAsync(() -> {
            // 在主线程运行
            refreshDataOnUiThread(diffResult, dataList);
        });
    }

    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult, final List<ViewMode> dataList){
        View view = getmView();
        if (view == null){
            return;
        }
        // 改变数据集合并不通知界面刷新
        RecycierAdapter<ViewMode> adapter = view.getRecyclerAdapter();
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
        // 通知界面刷新占位布局
        view.onAdapterDataChanged();
        // 进行增量更新
        diffResult.dispatchUpdatesTo(adapter);
    }
}
