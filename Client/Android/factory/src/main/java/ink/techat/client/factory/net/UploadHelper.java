package ink.techat.client.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.util.Date;

import ink.techat.client.factory.Factory;
import ink.techat.client.utils.HashUtil;

/**
 * @author NickCharlie
 * 上传工具类，用于上传任意文件到阿里OSS
 */
public class UploadHelper {

    private static final String TAG = UploadHelper.class.getSimpleName();
    private static final String ENDPOINT = "https://oss-accelerate.aliyuncs.com";
    private static final String BUCKET_NAME = "techat";

    private static OSS getClient(){

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                "LTAI4FebgwahZeN1GSzzWh71", "mIgzP4l2q9HZb39WbLeKOS4AUcD9r0");
        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
    }

    /**
     * 上传最终方法
     * @param objKey 上传后在服务器上独立的key
     * @param path  需要上传的文件的路径
     * @return  成功返回一个存储的路径
     */
    private static String upload(String objKey, String path){
        // 构造上传请求。
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME,
                objKey, path);

        try {
            // 初始化上传的Client
            OSS client = getClient();
            // 开始同步上传
            PutObjectResult result = client.putObject(request);
            // 得到一个外网可访问的地址
            String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);

            Log.d(TAG, String.format("PublicObjectURL:%s", url));
            return url;
        } catch (ClientException e) {
            // 本地异常，如网络异常等。
            Log.e(TAG + " 本地异常:", "");
            e.printStackTrace();
            return null;
        } catch (ServiceException e) {
            // 服务异常。
            Log.e(TAG + " 服务异常RequestId", e.getRequestId());
            Log.e(TAG + " 服务异常ErrorCode", e.getErrorCode());
            Log.e(TAG + " 服务异常HostId", e.getHostId());
            Log.e(TAG + " 服务异常RawMessage", e.getRawMessage());
            return null;
        }
    }

    /**
     * 上传普通图片
     * @param path 本地图片路径
     * @return 服务器图片地址
     */
    public static String uploadImage(String path){
        String key = getImageObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像图片
     * @param path 本地图片路径
     * @return 服务器图片地址
     */
    public static String uploadPortrait(String path){
        String key = getPortraitObjKey(path);
        return upload(key, path);
    }

    /**上传音频
     * @param path 本地音频路径
     * @return 服务器音频地址
     */
    public static String uploadAudio(String path){
        String key = getAudioObjKey(path);
        return upload(key, path);
    }

    /**
     * 分月份存储，避免同个文件夹储存太多文件
     * @return yyyyMM
     */
    private static String getDataString(){
        return DateFormat.format("yyyyMM", new Date())
                .toString();
    }

    /**
     * 获取各种类型的Key
     * @param path 本地文件路径
     * @return 服务器文件地址
     */
    public static String getImageObjKey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDataString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    public static String getPortraitObjKey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDataString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    public static String getAudioObjKey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDataString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }
}
