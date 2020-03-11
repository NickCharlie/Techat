package ink.techat.web.push.service;

import ink.techat.web.push.bean.api.account.LoginModel;
import ink.techat.web.push.bean.api.account.RegisterModel;
import ink.techat.web.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class BaseService {

    /**
     * SecurityContext securityContext
     * ������ע��,���securityContext�������ֵ
     * ֵΪ���������ص�������
     */
    @Context
    protected SecurityContext securityContext;

    /**
     * ����������ֱ�ӻ�ȡ�Լ�����Ϣ
     * @return User
     */
    protected User getSelf(){
        if (securityContext != null){
            return (User) securityContext.getUserPrincipal();
        }
        return null;
    }
}
