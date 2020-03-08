package ink.techat.client.push.fragments.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.EditText;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import butterknife.OnLongClick;
import ink.techat.client.common.app.Application;
import ink.techat.client.common.app.PresenterFragment;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.presenter.user.UpdateInfoContract;
import ink.techat.client.factory.presenter.user.UpdateInfoPresenter;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.MainActivity;
import ink.techat.client.push.fragments.media.GalleryFragment;

import static android.app.Activity.RESULT_OK;
import static ink.techat.client.factory.persistence.Account.getUser;


/**
 * @author NickCharlie
 * 更新信息的界面
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View{

    private static final String USER_INFO_FROM_SERVER = "INFO_FROM_SERVER";
    private static final String USER_SEX_FEMALE_STRING = "女";
    private static final String USER_SEX_MALE_STRING = "男";
    private static final String USER_SEX_OTHER_STRING = "你猜";
    private static final int USER_SEX_MALE = 1;
    private static final int USER_SEX_FEMALE = 0;
    private static final int USER_SEX_OTHER = 3;

    @BindView(R.id.img_portraits)
    ImageView mPortraits;

    @BindView(R.id.txt_user_phone)
    TextView mAccount;

    @BindView(R.id.txt_user_sex)
    TextView mUserSex;

    @BindView(R.id.edit_user_name)
    EditText mUserName;

    @BindView(R.id.edit_user_desc)
    EditText mUserDesc;

    @BindView(R.id.btn_user_finish)
    Button mSubmit;

    @BindView(R.id.updateInfo_scrollView)
    ScrollView mScrollView;

    @BindView(R.id.update_user_info_loading)
    Loading mLoading;

    private String mPortraitPath = null;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_updata_info;
    }

    @Override
    protected void initData() {
        super.initData();
        User self = getUser();
        String mPhotoUri = self.getPortrait();
        mAccount.setText(self.getPhone());
        mUserName.setText(self.getName());
        mUserDesc.setText(self.getDescription());

        if (!TextUtils.isEmpty(mPhotoUri)){
            loadPortrait(mPhotoUri);
            mPortraitPath = USER_INFO_FROM_SERVER;
        }

        if (self.getSex() == USER_SEX_MALE){
            mUserSex.setText(USER_SEX_MALE_STRING);
        }else if(self.getSex() == USER_SEX_FEMALE){
            mUserSex.setText(USER_SEX_FEMALE_STRING);
        }else {
            mUserSex.setText(USER_SEX_OTHER_STRING);
        }
    }

    @OnClick(R.id.btn_user_finish)
    void onSubmitClick(){
        int sex;
        String name = mUserName.getText().toString();
        String desc = mUserDesc.getText().toString();

        if (USER_SEX_MALE_STRING.equals(mUserSex.getText().toString())){
            sex = USER_SEX_MALE;
        }else if (USER_SEX_FEMALE_STRING.equals(mUserSex.getText().toString())){
            sex = USER_SEX_FEMALE;
        }else {
            sex = USER_SEX_OTHER;
        }

        // 提交更新到Presenter
        mPresenter.update(mPortraitPath, name, sex, desc);
    }

    @OnClick(R.id.txt_user_sex)
    void onSexTxtClick(){
        if (USER_SEX_MALE_STRING.equals(mUserSex.getText().toString())){
            mUserSex.setText(USER_SEX_FEMALE_STRING);
        }else {
            mUserSex.setText(USER_SEX_MALE_STRING);
        }
    }

    @OnLongClick(R.id.txt_user_sex)
    void onSexTxtLongClick(){
        mUserSex.setText(USER_SEX_OTHER_STRING);
    }

    @OnClick(R.id.img_portraits)
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
                                .start(getActivity());                       // 启动
                    }
                    // show的时候建议用getChildFragmentManager
                }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            if(resultUri != null){
                loadPortrait(resultUri.getPath());
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
    private void loadPortrait(String uri){

        if (this != null){
            Log.i("提醒", String.valueOf(this));
            Glide.with(this)
                    .asBitmap()
                    .load(uri)
                    .centerCrop()
                    .into(mPortraits);
        }

    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }

    @Override
    public void updateSucceed() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        // loading正在进时，界面不可操作
        // 开始Loading
        mLoading.start();
        // 让控件不可以输入
        mPortraits.setEnabled(false);
        mPortraits.setClickable(false);
        mUserSex.setClickable(false);
        mUserDesc.setEnabled(false);
        mUserName.setEnabled(false);
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        // 显示错误时触发，loading结束了

        // 停止Loading
        mLoading.stop();
        // 让控件可以输入
        mPortraits.setEnabled(true);
        mPortraits.setClickable(true);
        mUserSex.setClickable(true);
        mUserDesc.setEnabled(true);
        mUserName.setEnabled(true);
    }
}
