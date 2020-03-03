package ink.techat.client.factory.net;

import android.text.TextUtils;

import java.io.IOException;

import ink.techat.client.common.Common;
import ink.techat.client.factory.Factory;
import ink.techat.client.factory.persistence.Account;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 * @author NickCharlie
 */
public class Network {

    private static Network instance;
    private Retrofit retrofit;

    private Network(){

    }

    static {
        instance = new Network();
    }

    /**
     * 构建 Retrofit 网络框架
     * @return Retrofit
     */
    public static Retrofit getRetrofit(){

        if (instance.retrofit != null){
            return instance.retrofit;
        }

        // 得到OkHttp Client
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // 拿到请求, 重新进行Build
                        // 为了避免重要的东西被全局拦截器拦下, 给Client的请求强制加一个token
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())){
                            builder.addHeader("token", Account.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                }).build();

        Retrofit.Builder builder = new Retrofit.Builder();
        // 设置链接URL, Json解析器
        return instance.retrofit =  builder.baseUrl(Common.Constance.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

    }

    /**
     * 构建一个请求代理
     * @return RemoteService
     */
    public static RemoteService remote(){
        // 调用Retrofit对网络接口做代理, 得到Call
         return Network.getRetrofit().create(RemoteService.class);
    }
}
