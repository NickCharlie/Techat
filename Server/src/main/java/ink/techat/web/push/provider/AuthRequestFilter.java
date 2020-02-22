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
 * �������ع��˲�����
 * @author NickCharlie
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {
    final static String LOGIN_PATH = "account/login";
    final static String REGISTER_PATH = "account/register";

    /**
     * �ӿڵĹ��˷���
     * @param requestContext requestContext
     * @throws IOException IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // ����Ƿ�Ϊ��¼ע��ӿ� �����������߼�,������
        String relationPath = ((ContainerRequest)requestContext).getPath(false);
        if (relationPath.startsWith(LOGIN_PATH) || relationPath.startsWith(REGISTER_PATH)){
            return;
        }
        // ��Headers���ҵ���һ��token
        String token  = requestContext.getHeaders().getFirst("token");
        if (Strings.isNullOrEmpty(token)){
            // ��ѯself����Ϣ
            final User self = UserFactory.findByToken(token);
            if (self != null){
                // ����ǰ�������һ��������
                requestContext.setSecurityContext(new SecurityContext() {
                    /**
                     * ���岿��
                     * @return User
                     */
                    @Override
                    public Principal getUserPrincipal() {
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        // TODO:�û�Ȩ�޵�Ӧ��
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        // TODO:���HTTPS
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                // д�������ĺ󷵻�
                return;
            }
        }
        //
        //noinspection rawtypes
        ResponseModel model = ResponseModel.buildAccountError();
        // �������󣬷�������
        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();

        requestContext.abortWith(response);
    }
}
