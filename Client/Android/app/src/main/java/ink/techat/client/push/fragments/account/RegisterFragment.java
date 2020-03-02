package ink.techat.client.push.fragments.account;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.app.Application;
import ink.techat.client.common.app.PresenterFragment;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.factory.Factory;
import ink.techat.client.factory.net.UploadHelper;
import ink.techat.client.factory.presenter.account.RegisterContract;
import ink.techat.client.factory.presenter.account.RegisterPresenter;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.MainActivity;
import ink.techat.client.push.fragments.media.GalleryFragment;

/**
 * 用于注册的Fragment
 * @author NickCharlie
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter>
                implements RegisterContract.View {
    private AccountTrigger mAccountTrigger;
    private static String photoFilePath = null;

    @BindView(R.id.img_register_portraits)
    PortraitView mPortraits;

    @BindView(R.id.edit_register_phone)
    EditText mPhone;

    @BindView(R.id.edit_register_person)
    EditText mPerson;

    @BindView(R.id.edit_register_password)
    EditText mPassword;

    @BindView(R.id.register_loading)
    Loading mLoading;

    @BindView(R.id.btn_register_submit)
    Button mResisterBtn;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
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

    @OnClick(R.id.btn_register_submit)
    void onSubmitClick(){
        String phone = mPhone.getText().toString();
        String name = mPerson.getText().toString();
        String password = mPassword.getText().toString();
        // 提交注册
        mPresenter.register(phone, name, password, photoFilePath);
    }

    @OnClick(R.id.txt_go_login)
    void onShowLoginClick(){
        // 让AccountActivity进行界面切换
        mAccountTrigger.triggerView();
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
                                .start(getActivity());                     // 启动
                    }
                    // show的时候建议用getChildFragmentManager
                }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }


    /**
     * 加载Uri图像到当前的头像中
     * @param uri Uri
     */
    public void loadPortrait(Uri uri){
        if (this != null){
            Log.i("提醒", String.valueOf(this));
            Glide.with(this)
                    .asBitmap()
                    .load(uri)
                    .centerCrop()
                    .into(mPortraits);
        }

        // 拿到本地文件的地址
        photoFilePath = uri.getPath();
        Log.i("localPath-TAG", "localPath:" + photoFilePath);
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        // 显示错误时触发，loading结束了

        // 停止Loading
        mLoading.stop();
        // 让控件可以输入
        mPhone.setEnabled(true);
        mPerson.setEnabled(true);
        mPassword.setEnabled(true);
        mResisterBtn.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        // loading正在进时，界面不可操作
        // 开始Loading
        mLoading.start();
        // 让控件不可以输入
        mPhone.setEnabled(false);
        mPerson.setEnabled(false);
        mPassword.setEnabled(false);
        mResisterBtn.setEnabled(false);
    }

    @Override
    public void registerSuccess() {
        // 注册成功, 账户登陆, 跳转到MainActivity
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
