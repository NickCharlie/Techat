package ink.techat.client.factory.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * @author NickCharlie
 * @param <T> T
 */
@SuppressWarnings("unchecked")
public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer> extends DiffUtil.Callback {
    private List<T> mOldData, mNawData;

    public DiffUiDataCallback(List<T> mOldData, List<T> mNawData) {
        this.mOldData = mOldData;
        this.mNawData = mNawData;
    }

    @Override
    public int getOldListSize() {
        // 旧的数据Size
        return mOldData.size();
    }

    @Override
    public int getNewListSize() {
        // 新的数据Size
        return mNawData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // 两个类是否为同类, 比如Id相等的User
        T beanOld = mOldData.get(oldItemPosition);
        T beanNew = mNawData.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // 在经过相等判断后, 进一步判断是否有数据更改, 比如同一个用户的不同实例, 其中的name不同
        T beanOld = mOldData.get(oldItemPosition);
        T beanNew = mNawData.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }

    /**
     * 进行实际比较的数据类型
     * @param <T> 泛型的目的: 你和你同类型的数据比较
     */
    public interface UiDataDiffer<T>{
        // 传递一个旧的数据
        boolean isSame(T old);
        // 和旧的数据对比, 内容是否相同
        boolean isUiContentSame(T old);
    }
}
