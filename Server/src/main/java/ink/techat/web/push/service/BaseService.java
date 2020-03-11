package ink.techat.web.push.service;

import ink.techat.web.push.bean.api.account.LoginModel;
import ink.techat.web.push.bean.api.account.RegisterModel;
import ink.techat.web.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class BaseService {

    /**
     * SecurityContext securityContext
     * 上下文注解,会给securityContext赋具体的值
     * 值为拦截器返回的上下文
     */
    @Context
    protected SecurityContext securityContext;

    /**
     * 从上下文中直接获取自己的信息
     * @return User
     */
    protected User getSelf(){
        if (securityContext != null){
            return (User) securityContext.getUserPrincipal();
        }
        return null;
    }
}
