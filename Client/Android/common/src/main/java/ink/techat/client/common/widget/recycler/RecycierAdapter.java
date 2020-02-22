package ink.techat.client.common.widget.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ink.techat.client.common.R;

/**
 * @author NickCharlie
 * @param <Data>
 */
public abstract class RecycierAdapter<Data>
        extends RecyclerView.Adapter<RecycierAdapter.ViewHolder<Data>>
            implements  View.OnClickListener,View.OnLongClickListener,
                AdapterCallback<Data>{

    private final List<Data> mDataList = new ArrayList<>();
    private  AdapterLister<Data> mListener;

    /**
     * 三个构造方法:
     * 分别对于传双参 List<Data>, AdapterLister<Data>
     * 传单参 AdapterLister<Data> 和不传参
     * 对不传的参数自动生成(new)
     * @param dataList Data 传入的数据
     * @param lister AdapterLister<Data> 监听器
     */
    public RecycierAdapter(List<Data> dataList, AdapterLister<Data> lister){
        this.mListener = lister;
    }

    public RecycierAdapter(AdapterLister<Data> lister){
        this(new ArrayList<Data>(), lister);
    }

    public  RecycierAdapter(){
        this(null);
    }

    /**
     * 复写默认的布局类型返回
     * @param position 坐标
     * @return 类型，其实复写后返回的都是XML文件的Id
     */
    @Override
    public int getItemViewType(int position ) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     * @param position 坐标
     * @param data 当前的数据
     * @return XML文件的Id，用于创建ViewHolder
     */
    protected abstract int getItemViewType(int position, Data data );

    /**
     * 创建一个ViewHolder
     * @param parent RecyclerView
     * @param viewType 界面的类型,约定为XML布局的Id
     * @return ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 得到LayoutInflater用于把XML初始化为View
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // 把XML Id为viewType的文件初始化为一个root View;
        View root = layoutInflater.inflate(viewType, parent, false);
        // 通过子类必须试下的方法，得到一个ViewHolder
        ViewHolder<Data> holder = onCreateiewHolder(root, viewType);

        // 设置View的Tag为ViewHolder,进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        // 设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        //进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        //绑定callback
        holder.callback = this;
        return holder;
    }


    /**
     * 得到一个新的ViewHolder
     * @param root 根布局
     * @param viewType 布局类型,其实就是XML的Id
     * @return ViewHolder
     */
    protected abstract ViewHolder<Data> onCreateiewHolder(View root, int viewType);

    /**
     * 绑定数据到一个Holder上
     * @param holder ViewHolder
     * @param position 数据坐标
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        // 得到需要绑定的数据
        Data data = mDataList.get(position);
        // 触发Holder的绑定方法
        holder.bind(data);
    }

    /**
     * 获取List集合的Size
     * @return List总数
     **/
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入一个数据并且通知插入更新
     * @param data Data
     */
    public void add(Data data){
        mDataList.add(data);
        // 通知更新
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据并且通知插入更新
     * @param dataList Data
     */
    public void add(Data... dataList){
        if(dataList != null && dataList.length > 0){
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            // 通知更新
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据并且通知插入更新
     * @param dataList Data
     */
    public void add(Collection<Data> dataList){
        if(dataList != null && dataList.size() > 0){
            int startPos = mDataList.size();
            Collections.addAll(dataList);
            // 通知更新
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * mDataList删除操作
     */
    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 全部替换为一个新的集合，其中包括了清空
     * @param dataList Data
     */
    public void replace(Collection<Data> dataList){
        mDataList.clear();
        if(dataList != null && dataList.size() > 0){
            mDataList.addAll(dataList);
        }
        // 通知更新
        notifyDataSetChanged();
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {
        // 得到当前ViewHolder的坐标
        int pos = holder.getAdapterPosition();
        if (pos >= 0){
            // 进行数据的移除与更新
            mDataList.remove(pos);
            mDataList.add(pos, data);
            // 通知这个坐标上的数据更新
            notifyItemChanged(pos);
        }
    }

    /**
     * 复写的OnClick事件和OnLongClick事件
     * @param v View
     */
    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if(this.mListener != null){
            // 得到ViewHolder当前对应的适配器当中的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回调方法
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if(this.mListener != null){
            // 得到ViewHolder当前对应的适配器当中的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回调方法
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器监听
     * @param adapterLister AdapterLister<Data>
     */
    public void setListener(AdapterLister<Data> adapterLister){
        this.mListener = adapterLister;
    }

    /**
     * 我们的自定义监听器
     * @param <Data> 泛型
     */
    public interface AdapterLister<Data>{
        // 当Cell点击的时候触发
       void onItemClick(RecycierAdapter.ViewHolder holder, Data data);
        // 当Cell长按的时候触发
       void onItemLongClick(RecycierAdapter.ViewHolder holder, Data data);
    }

    /**
     * 自定义的ViewHolder
     * @param <Data> 泛型类型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder{
        protected Data mData;
        private Unbinder unbinder;
        private AdapterCallback<Data> callback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         * @param data 绑定的数据
         */
        void bind(Data data){
            this.mData = data;
            onBind(data);
        }

        /**
         * 当触发绑定数据的时候，的回调; 必须复写
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);

        /**
         * Holder自己对自己对应的Data进行更新操作
         *
        * @param data 绑定的数据
         */
        public void updataData(Data data){
            if(this.callback != null){
                this.callback.update(data, this);
            }
        }
    }

    /**
     * 对回调接口做移除实现AdapterListener
     * 因为在GalleyView.java里init的时候用RecycierAdapter.AdapterLister接口时
     * 老是要实现全部方法，特别不爽，所以新写了一个抽象类
     *
     * @param <Data>
     */
    public static abstract class AdapterListerImpl<Data> implements AdapterLister<Data>{

        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }

}
