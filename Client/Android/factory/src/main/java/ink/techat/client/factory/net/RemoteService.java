package ink.techat.client.factory.net;


import ink.techat.client.factory.model.api.RspModel;
import ink.techat.client.factory.model.api.account.AccountRspModel;
import ink.techat.client.factory.model.api.account.RegisterModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
}
