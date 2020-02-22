package ink.techat.client.common.widget.recycler;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author NickCharlie
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecycierAdapter.ViewHolder<Data> holder);
}
