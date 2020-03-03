package ink.techat.client.factory.presenter.user;

import android.text.TextUtils;
import android.util.Log;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.R;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.data.helper.UserHelper;
import ink.techat.client.factory.model.api.user.UserUpdateModel;
import ink.techat.client.factory.model.card.UserCard;
import ink.techat.client.factory.net.UploadHelper;
import ink.techat.client.factory.presenter.BasePresenter;


/**
 * @author NickCharlie
 */
public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
            implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {

    private static final String USER_INFO_FROM_SERVER = "INFO_FROM_SERVER";
    private static final String USER_IMAGE_UPDATE_FAILURE = "PHOTO_UPDATE_FAILURE";

    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void update(final String photoFilePath, final String name, final int sex, final String desc) {
        start();
        final UpdateInfoContract.View view = getmView();

        if (TextUtils.isEmpty(photoFilePath) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(name)){
            view.showError(R.string.data_account_update_invalid_parameter);
        }else {
            if (photoFilePath.equals(USER_INFO_FROM_SERVER)){
                userInfoUpdate(photoFilePath, name, sex, desc);
            }
        }
    }

    private void userInfoUpdate(String photoFilePath, final String name, final int sex, final String desc){

        final UpdateInfoContract.View view = getmView();
        // 拿到本地文件的地址, 异步上传头像到阿里OSS
        final String localPath = photoFilePath;
        Log.i("UpdatePhoto-localPath", "localPath:" + localPath);

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = USER_IMAGE_UPDATE_FAILURE;
                // 将头像上传到阿里云OSS
                if (!(localPath.equals(USER_INFO_FROM_SERVER))){
                    url = UploadHelper.uploadPortrait(localPath);
                    if (url.equals(USER_IMAGE_UPDATE_FAILURE)){
                        view.showError(R.string.data_upload_error);
                        Log.i("UpdatePhoto-InterPath", "url为空，头像上传失败");
                    }else {
                        url = USER_INFO_FROM_SERVER;
                    }
                }else {
                    url = localPath;
                }

                if (!(url.equals(USER_IMAGE_UPDATE_FAILURE))){
                    UserUpdateModel model = new UserUpdateModel(name, url, desc, sex);
                    UserHelper.update(model, UpdateInfoPresenter.this);
                    Log.i("UpdatePhoto-InterPath", "url:" + url);
                }else {
                    view.showError(R.string.data_upload_error);
                    Log.i("UpdatePhoto-InterPath", "url为空，头像上传失败");
                }
            }
        });

    }

    @Override
    public void onDataLoaded(UserCard userCard) {
        // 当网络请求成功, 更新成功, 回送用户信息, 告知界面更新成功
        final UpdateInfoContract.View view = getmView();
        if (view == null){
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.updateSucceed();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 更新失败
        final UpdateInfoContract.View view = getmView();
        if (view == null){
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
