package ink.techat.client.push.fragments.account;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.app.Application;
import ink.techat.client.common.app.Fragment;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.factory.Factory;
import ink.techat.client.factory.net.UploadHelper;
import ink.techat.client.push.R;
import ink.techat.client.push.fragments.media.GalleryFragment;

import static android.app.Activity.RESULT_OK;

/**
 * 用于注册的Fragment
 * @author NickCharlie
 */
public class RegisterFragment extends Fragment implements{
    private AccountTrigger mAccountTrigger;
    @BindView(R.id.img_register_portraits)
    PortraitView mPortraits;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 拿到Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }



    @OnClick(R.id.img_register_portraits)
    void onPortraitClick(){
        new GalleryFragment()
                .setmListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        // 设置图片处理的格式JPEG
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        // 设置压缩后的图片精度
                        options.setCompressionQuality(96);
                        // 得到头像的缓存地址
                        File dPath = Application.getPortraitTmpFile();

                        UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                                .withAspectRatio(1, 1)                  // 1:1比例
                                .withMaxResultSize(520, 520)    // 返回最大的尺寸
                                .withOptions(options)                        // 加载相关参数
                                .start(getActivity(),200);                     // 启动
                    }
                    // show的时候建议用getChildFragmentManager
                }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if(resultUri != null){
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            //noinspection ThrowableNotThrown
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 加载Uri图像到当前的头像中
     * @param uri Uri
     */
    private void loadPortrait(Uri uri){
        if (this != null){
            Log.i("提醒", String.valueOf(this));
            Glide.with(this)
                    .asBitmap()
                    .load(uri)
                    .centerCrop()
                    .into(mPortraits);
        }

        // 拿到本地文件的地址
        final String localPath = uri.getPath();
        Log.i("localPath-TAG", "localPath:" + localPath);

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = UploadHelper.uploadPortrait(localPath);
                Log.i("localPath-URL", "url:" + url);
            }
        });
    }
}
