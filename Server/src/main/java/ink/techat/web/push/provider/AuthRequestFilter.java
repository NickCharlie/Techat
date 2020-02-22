package ink.techat.web.push.provider;

import com.google.common.base.Strings;
import ink.techat.web.push.bean.api.base.ResponseModel;
import ink.techat.web.push.bean.db.User;
import ink.techat.web.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * 请求拦截过滤操作类
 * @author NickCharlie
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {
    final static String LOGIN_PATH = "account/login";
    final static String REGISTER_PATH = "account/register";

    /**
     * 接口的过滤方法
     * @param requestContext requestContext
     * @throws IOException IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 检查是否为登录注册接口 是则走正常逻辑,不拦截
        String relationPath = ((ContainerRequest)requestContext).getPath(false);
        if (relationPath.startsWith(LOGIN_PATH) || relationPath.startsWith(REGISTER_PATH)){
            return;
        }
        // 从Headers中找到第一个token
        String token  = requestContext.getHeaders().getFirst("token");
        if (Strings.isNullOrEmpty(token)){
            // 查询self的信息
            final User self = UserFactory.findByToken(token);
            if (self != null){
                // 给当前请求添加一个上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    /**
                     * 主体部分
                     * @return User
                     */
                    @Override
                    public Principal getUserPrincipal() {
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        // TODO:用户权限的应用
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        // TODO:检查HTTPS
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                // 写入上下文后返回
                return;
            }
        }
        //
        //noinspection rawtypes
        ResponseModel model = ResponseModel.buildAccountError();
        // 拦截请求，返回请求
        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();

        requestContext.abortWith(response);
    }
}
