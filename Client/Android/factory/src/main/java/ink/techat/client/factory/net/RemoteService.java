package ink.techat.client.factory.net;


import ink.techat.client.factory.model.api.RspModel;
import ink.techat.client.factory.model.api.account.AccountRspModel;
import ink.techat.client.factory.model.api.account.LoginModel;
import ink.techat.client.factory.model.api.account.RegisterModel;
import ink.techat.client.factory.model.api.user.UserUpdateModel;
import ink.techat.client.factory.model.card.UserCard;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求的所有接口
 * @author NickCharlie
 */
public interface RemoteService {

    /**
     * 网络请求一个注册接口
     * @param model RegisterModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 网络请求的登录接口
     * @param model Model
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 网络请求的绑定PushId接口
     * @param pushId PushId
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    /**
     * 更新用户信息的接口
     * @param model UserUpdateModel
     * @return RspModel<UserCard>
     */
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);
}
