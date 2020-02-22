package ink.techat.client.push.fragments.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.qiujuer.genius.ui.Ui;

import ink.techat.client.common.tools.UiTool;
import ink.techat.client.common.widget.GalleyView;
import ink.techat.client.push.R;

/**
 * @author NicKCharlie
 */
public class GalleryFragment extends BottomSheetDialogFragment
    implements GalleyView.SelectedChangeListener {
    private GalleyView mGallery;
    private OnSelectedListener mListener;

    /**
     * 设置事件监听，并且返回自己
     * @param mListener OnSelectedListener
     * @return Myself
     */
    public GalleryFragment setmListener(OnSelectedListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public GalleryFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 获取GalleryView
        View root =  inflater.inflate(R.layout.fragment_gallery, container);
        mGallery = root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if(count > 0){
            dismiss();
            if(mListener != null){
                String[] paths = mGallery.getSelectedPath();
                // 返回第一张
                mListener.onSelectedImage(paths[0]);
                // 取消与唤起者的引用
                mListener = null;
            }
        }
    }

    public interface OnSelectedListener{
        /**
         * 选中图片的监听器接口
         * @param path path
         */
        void onSelectedImage(String path);
    }

    private static class TransStatusBottomSheetDialog extends BottomSheetDialog{

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window window = getWindow();
            if(window == null){
                return;
            }
            // 得到屏幕高度
            int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
            // 得到状态栏的高度
            int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());
            // 计算Dialog高度并且设置
            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }
    }
}
