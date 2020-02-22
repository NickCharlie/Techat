package ink.techat.web.push.service;

import com.google.common.base.Strings;
import ink.techat.web.push.bean.api.account.AccountRespModel;
import ink.techat.web.push.bean.api.account.LoginModel;
import ink.techat.web.push.bean.api.account.RegisterModel;
import ink.techat.web.push.bean.api.base.ResponseModel;
import ink.techat.web.push.bean.db.User;
import ink.techat.web.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author NickCharlie
 * 127.0.0.1/api/account/...
 */
@Path("/account")
public class AccountService extends BaseService {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRespModel> login(LoginModel model){
        if(!LoginModel.check(model)){
            // ���ز����쳣
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if(user != null){
            if(!Strings.isNullOrEmpty(model.getPushId())){
                // �����Я��pushId
                return bind(user, model.getPushId());
            }
            // ���ص�ǰ���˻�
            AccountRespModel respModel = new AccountRespModel(user);
            return ResponseModel.buildOk(respModel);
        }else {
            // ��½ʧ��
            return ResponseModel.buildLoginError();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRespModel> register(RegisterModel model){
        if(!RegisterModel.check(model)){
            // ���ز����쳣
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if(user != null){
            if(!Strings.isNullOrEmpty(model.getPushId())){
                // �����Я��pushId
                return bind(user, model.getPushId());
            }
            // �˺��Ѵ���
            return ResponseModel.buildHaveAccountError();
        }
        // ��ʼע���߼�
        user = UserFactory.register(model.getAccount(),
                model.getPassword(),
                model.getName());

        if(user != null){
            // ���ص�ǰ���˻�
            AccountRespModel respModel = new AccountRespModel(user);
            return ResponseModel.buildOk(respModel);
        }else {
            // ע���쳣
            return ResponseModel.buildRegisterError();
        }
    }

    /**
     * ��pushId
     * @param token Token
     * @param pushId pushId
     * @return User
     */
    @POST
    @Path("/bind/{pushId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRespModel> bind(@HeaderParam("token") String token,
                                                @PathParam("pushId") String pushId){
        // @HeaderParam("token") ������ͷ�л�ȡtoken�ֶ�
        // pushId��Url��ַ�л�ȡ
        if(Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(pushId)){
            // ���ز����쳣
            return ResponseModel.buildParameterError();
        }
        // �õ��Լ��ĸ�����Ϣ���Ұ�
        User self = getSelf();
        return bind(self, pushId);
    }

    /**
     * pushIdb�󶨲���
     * @param self User
     * @param pushId pushId
     * @return User
     */
    private ResponseModel<AccountRespModel> bind(User self, String pushId){
        // �����豸�Ű�
        self = UserFactory.bindPushId(self, pushId);
        if(self != null){
            // ���ص�ǰ���˻������ҽ�isBind��Ϊtrue
            AccountRespModel respModel = new AccountRespModel(self, true);
            return ResponseModel.buildOk(respModel);
        }else {
            return ResponseModel.buildServiceError();
        }
    }
}
