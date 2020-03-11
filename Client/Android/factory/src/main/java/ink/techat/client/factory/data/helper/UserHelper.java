package ink.techat.client.factory.data.helper;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import ink.techat.client.factory.Factory;
import ink.techat.client.factory.R;
import ink.techat.client.factory.data.DataSource;
import ink.techat.client.factory.model.api.RspModel;
import ink.techat.client.factory.model.api.user.UserUpdateModel;
import ink.techat.client.factory.model.card.UserCard;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.model.db.User_Table;
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

    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);

        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    // 返回数据
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
                Log.e("错误测试:",t.getMessage());
            }
        });

        // 把当前的调度者返回
        return call;
    }

    public static void refreshContacts(final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<List<UserCard>>> call = service.userContacts();

        call.enqueue(new Callback<RspModel<List<UserCard>>>() {

            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    // 拿到集合
                    List<UserCard> cards = rspModel.getResult();
                    if (cards == null || cards.size() == 0) {
                        return;
                    }
                    // 返回数据
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
                Log.e("refreshContacts错误测试:",t.getMessage());
            }
        });
    }

    public static void follow(String userId, final DataSource.Callback<UserCard> callback){
        RemoteService service = Network.remote();
        final Call<RspModel<UserCard>> call = service.userFollow(userId);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    // 保存到本地数据库
                    User user = userCard.build();
                    user.save();
                    // TODO: 通知联系人列表刷新
                    // 返回数据
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
                Log.e("follow错误测试:", t.getMessage());
            }
        });
    }


    /**
     * 搜索用户, 优先本地缓存
     * 如果没有再从网络拉取
     * @param id Id
     * @return User
     */
    public static User search(String id){
        User user = findFromLocal(id);
        if (user == null){
            return findFromNet(id);
        }
        return user;
    }

    /**
     * 搜索用户, 优先从网络拉取
     * 如果没有再从本地缓存拉取
     * @param id Id
     * @return User
     */
    public static User searchFirstOfNet(String id){
        User user = findFromNet(id);
        if (user == null){
            return findFromLocal(id);
        }
        return user;
    }

    /**
     * 从本地查询一个用户的信息
     * @param id Id
     * @return User
     */
    public static User findFromLocal(String id){
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }

    /**
     * 从网络查询一个用户的信息
     * @param id Id
     * @return User
     */
    public static User findFromNet(String id){
        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();
            if (card != null){
                // TODO 数据库刷新, 但是没有通知
                User user = card.build();
                user.save();

                return card.build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
