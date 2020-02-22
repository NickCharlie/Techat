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
            // 返回参数异常
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if(user != null){
            if(!Strings.isNullOrEmpty(model.getPushId())){
                // 如果有携带pushId
                return bind(user, model.getPushId());
            }
            // 返回当前的账户
            AccountRespModel respModel = new AccountRespModel(user);
            return ResponseModel.buildOk(respModel);
        }else {
            // 登陆失败
            return ResponseModel.buildLoginError();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRespModel> register(RegisterModel model){
        if(!RegisterModel.check(model)){
            // 返回参数异常
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if(user != null){
            if(!Strings.isNullOrEmpty(model.getPushId())){
                // 如果有携带pushId
                return bind(user, model.getPushId());
            }
            // 账号已存在
            return ResponseModel.buildHaveAccountError();
        }
        // 开始注册逻辑
        user = UserFactory.register(model.getAccount(),
                model.getPassword(),
                model.getName());

        if(user != null){
            // 返回当前的账户
            AccountRespModel respModel = new AccountRespModel(user);
            return ResponseModel.buildOk(respModel);
        }else {
            // 注册异常
            return ResponseModel.buildRegisterError();
        }
    }

    /**
     * 绑定pushId
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
        // @HeaderParam("token") 从请求头中获取token字段
        // pushId从Url地址中获取
        if(Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(pushId)){
            // 返回参数异常
            return ResponseModel.buildParameterError();
        }
        // 拿到自己的个人信息并且绑定
        User self = getSelf();
        return bind(self, pushId);
    }

    /**
     * pushIdb绑定操作
     * @param self User
     * @param pushId pushId
     * @return User
     */
    private ResponseModel<AccountRespModel> bind(User self, String pushId){
        // 进行设备号绑定
        self = UserFactory.bindPushId(self, pushId);
        if(self != null){
            // 返回当前的账户，并且将isBind设为true
            AccountRespModel respModel = new AccountRespModel(self, true);
            return ResponseModel.buildOk(respModel);
        }else {
            return ResponseModel.buildServiceError();
        }
    }
}
