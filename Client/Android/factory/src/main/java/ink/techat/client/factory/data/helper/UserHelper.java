package ink.techat.client.factory.data.helper;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.R;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.model.api.RspModel;
import ink.techat.client.factory.model.api.user.UserUpdateModel;
import ink.techat.client.factory.model.card.UserCard;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.net.Network;
import ink.techat.client.factory.net.RemoteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户信息更新
 * @author NickCharlie
 */
public class UserHelper {

    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback){
        RemoteService service = Network.remote();

        Call<RspModel<UserCard>> call = service.userUpdate(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel != null && rspModel.success()){
                    UserCard userCard = rspModel.getResult();
                    // 数据库存储用户信息
                    User user = userCard.build();
                    user.save();
                    // 返回成功
                    callback.onDataLoaded(userCard);
                }else {
                    // 错误解析
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {

                if (callback != null){
                    callback.onDataNotAvailable(R.string.data_network_error);
                }
            }
        });
    }
}
