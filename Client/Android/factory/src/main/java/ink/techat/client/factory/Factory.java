package ink.techat.client.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;

import ink.techat.client.common.app.Application;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @author NickCharlie
 */
public class Factory {
    /**
     * 单例模式
     * Executor executor 全局的线程池
     * Gson gson 全局的Gson
     */
    private static final Factory INSTANCE;
    @SuppressWarnings("FieldCanBeLocal")
    private final Executor executor;
    private final Gson gson;

    static {
        INSTANCE = new Factory();
    }

    private Factory(){
        // 新建一个4个线程的线程池
        executor = newFixedThreadPool(4);
        // 设置Gson解析器, setDateFormat设置时间格式,
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // TODO: 设置一个过滤器, 数据库级别的Model不进行Json转换
                // .setExclusionStrategies
                .create();
    }

    /**
     * 返回全局的Application
     * @return Application
     */
    public static Application app(){
        return Application.getInstance() ;
    }

    /**
     * 异步运行的方法
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable){
        // 拿到单例，拿到线程池，异步执行
        INSTANCE.executor.execute(runnable);
    }

    /**
     * 返回一个全局Gson
     * @return Gson
     */
    public static Gson getGson(){
        return INSTANCE.gson;
    }
}
