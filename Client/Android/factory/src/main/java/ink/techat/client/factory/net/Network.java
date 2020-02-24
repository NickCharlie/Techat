package ink.techat.client.factory.net;

import ink.techat.client.common.Common;
import ink.techat.client.factory.Factory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 * @author NickCharlie
 */
public class Network {

    /**
     * 构建 Retrofit 网络框架
     * @return Retrofit
     */
    public static Retrofit getRetrofit(){

        // 得到OkHttp Client
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        // 设置链接URL, Json解析器
        return builder.baseUrl(Common.Constance.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

    }
}
