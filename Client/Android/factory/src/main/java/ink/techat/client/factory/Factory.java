package ink.techat.client.factory;

import java.util.concurrent.Executor;

import ink.techat.client.common.app.Application;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @author NickCharlie
 */
public class Factory {
    /**
     * 单例模式
     */
    private static final Factory INSTANCE;
    @SuppressWarnings("FieldCanBeLocal")
    private final Executor executor;

    static {
        INSTANCE = new Factory();
    }

    private Factory(){
        // 新建一个4个线程的线程池
        executor = newFixedThreadPool(4);
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
}
