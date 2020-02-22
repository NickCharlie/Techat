package ink.techat.web.push;

import ink.techat.web.push.provider.AuthRequestFilter;
import ink.techat.web.push.provider.GsonProvider;
import ink.techat.web.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/**
 * @author NickCharlie
 */
public class Application extends ResourceConfig {
    public Application(){
        //注册逻辑处理的包名
        packages(AccountService.class.getPackage().getName());

        // 注册全局请求拦截器
        register(AuthRequestFilter.class);

        //注册json解析器
        register(GsonProvider.class);

        //注册日志打印输出
        register(Logger.class);
    }
}
