package ink.techat.client.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ink.techat.client.common.R;
import ink.techat.client.common.widget.recycler.RecycierAdapter;

/**
 * @author NickCharlie
 */
public class GalleyView extends RecyclerView {

    /**
     * final int LOADER_ID            LoaderID
     * List<Image> mSelectedImages    图片类列表
     * Adapter mAdapter               老子的Adapter
     * SelectChangeListener mLisener  用于回调的图片选择器更改监听器
     * LoaderCallback mLoaderCallback Loader回调方法 用于图片加载
     * final int MAX_IMAGE_COUNT      最大图片选中数量
     * final int MIN_IMAGE_FILE_SIZE  上传图片大小限定
     */
    private static final int LOADER_ID = 0x0100;
    private List<Image> mSelectedImages = new LinkedList<>();
    private Adapter mAdapter = new Adapter();
    private SelectedChangeListener mLisener;
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private static final int MAX_IMAGE_COUNT = 3;
    private static final int MIN_IMAGE_FILE_SIZE = 10*1024;

    public GalleyView(Context context) {
        super(context);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecycierAdapter.AdapterListerImpl<Image>() {
            @Override
            public void onItemClick(RecycierAdapter.ViewHolder holder, Image image) {
                // Cell点击操作，如果点击运行，则跟新对应Cell的状态
                // 然后更新界面，如果不允许点击(如以达到最大选中数量)，则不刷新界面
                if (onItemSelectClick(image)){
                    //noinspection unchecked
                    holder.updataData(image);
                }
            }
        });
    }

    /**
     * setup(LoaderManager loaderManager)
     * 初始化方法
     * @param loaderManager Loader管理器
     * @return 任何一个LOADER_ID,用于销毁Loader
     */
    public int setup(LoaderManager loaderManager, SelectedChangeListener lisener){
        mLisener = lisener;
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        return LOADER_ID;
    }

    /**
     * onItemSelectClick(Image image)
     * Cell点击的具体逻辑
     *
     * @param image Image
     * @return True or False,进行了数据更改或没更改，更改了则刷新
     */
    @SuppressLint("StringFormatMatches")
    private boolean onItemSelectClick(Image image){
        // 是否需要进行刷新
        boolean notifyRefresh;
        if(mSelectedImages.contains(image)){
            // 如存在,则移除
            mSelectedImages.remove(image);
            image.isSelect = false;
            // 通知更新
            notifyRefresh = true;
        }else {
            if (mSelectedImages.size() >= MAX_IMAGE_COUNT){
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                Toast.makeText(getContext(),
                        String.format(str, MAX_IMAGE_COUNT),
                        Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            }else{
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }
        // 如数据有改变,则需通知外边的监听器数据选中改变了
        if (notifyRefresh) {
            notifySelectChanged();
        }
        return true;
    }

    /**
     * getSelectedPath()
     * 得到选中的图片的全部地址
     * @return paths 数组
     */
    public String[] getSelectedPath(){
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image : mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * clear()
     * 用于清空选中的图片
     */
    public void clear(){
        // 一定一定要先重置状态再通知更新
        for (Image image : mSelectedImages) {
            image.isSelect = false;
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();
        //通知选中数量改变
        notifySelectChanged();
    }

    /**
     * GalleyView 的内部类 class Image
     * 内部的数据结构
     */
    private static class Image {
        int id;             // 数据Id
        String path;        // 图片路就
        long date;          // 图片创建时间
        boolean isSelect;   // 图片是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Image image = (Image) o;
            return Objects.equals(path, image.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path);
        }
    }

    /**
     * notifySelectChanged()
     * 通知选中状态改变
     */
    private void notifySelectChanged(){
        // 得到监听者并且判断是否有监听,然后进行回调数量变化
        SelectedChangeListener listener = mLisener;
        if (listener != null){
            listener.onSelectedCountChanged(mSelectedImages.size());
        }
    }

    /**
     * 用于通知Adapter数据更改
     * @param images 新的图片数据
     */
    private void updateSource(List<Image> images){
        mAdapter.replace(images);
    }

    /**
     * GalleyView的子类 class LoaderCallback
     * 用于图片选择器内置正方形控件(Cell)的图片加载
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,        // ID
                MediaStore.Images.Media.DATA,       // 图片Url
                MediaStore.Images.Media.DATE_ADDED  // 图片创建时间
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            // 创建一个Loader
            if (id == LOADER_ID){
                // 如果ID相同则进行初始化
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC");  // 倒叙查询 DESC前有应该空格!!!
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            // 当Loader加载完成时调用
            List<Image> images = new ArrayList<>();
            if (data != null){
                int count = data.getCount();
                if (count > 0){
                    // 移动游标到Start
                    data.moveToFirst();

                    // 得到对应列的Index坐标
                    int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        // 循环读取直到没有下一条数据
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long dateTime = data.getLong(indexDate);

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE){
                            // 如果没有图片或图片大小不符合规范则跳过
                            continue;
                        }

                        // 添加一条新的数据
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = dateTime;
                        images.add(image);
                    }while (data.moveToNext());
                }
            }
            // 通知刷新
            updateSource(images);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            // 当Loader销毁或重置时调用 直接清空刷新
            updateSource(null);
        }
    }

    /**
     * Adapter适配器
     */
    private class Adapter extends RecycierAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;
        }

        @Override
        protected ViewHolder<Image> onCreateiewHolder(View root, int viewType) {
            return new GalleyView.ViewHolder(root);
        }
    }

    /**
     * Cell 对应的Holder
     */
    private class ViewHolder extends RecycierAdapter.ViewHolder<Image> {
        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPic = itemView.findViewById(R.id.img_image);
            mShade = itemView.findViewById(R.id.view_shade);
            mSelected = itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)                           // 加载路径
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // 直接使用原图加载,不进行缓存
                    .centerCrop()                               // 居中剪切
                    .placeholder(R.color.grey_200)              // 默认图片(颜色)
                    .into(mPic);
            // 设置阴影显示和选中状态
            mShade.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mSelected.setChecked(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }
    }

    /**
     * 对外监听器
     */
    public interface SelectedChangeListener{
        /**
         * 用于通知选中更改的监听器接口
         * @param count Count
         */
        void onSelectedCountChanged(int count);
    }
}
