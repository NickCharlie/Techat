package ink.techat.web.push.service;

import com.google.common.base.Strings;
import ink.techat.web.push.bean.api.base.PushModel;
import ink.techat.web.push.bean.api.base.ResponseModel;
import ink.techat.web.push.bean.api.user.UpdateInfoModel;
import ink.techat.web.push.bean.card.UserCard;
import ink.techat.web.push.bean.db.User;
import ink.techat.web.push.factory.UserFactory;
import ink.techat.web.push.utils.PushDispatcher;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static ink.techat.web.push.factory.UserFactory.findByPhone;

/**
 * @author NicKCharlie
 * 127.0.0.1/api/user
 */
@Path("/user")
public class UserService extends BaseService {
    private static final String USER_INFO_FROM_SERVER = "INFO_FROM_SERVER";
    /**
     * 返回自己的个人信息
     * @param model Model
     * @return UserCard
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model){
        if (!UpdateInfoModel.check(model)){
            return ResponseModel.buildParameterError();
        }
        // 获取自身
        User self = getSelf();
        self = model.updateToUser(self);
        // 更新信息
        if ((self.getPortrait()).equals(USER_INFO_FROM_SERVER)){
            User user = findByPhone(self.getPhone());
            self.setPortrait(user.getPortrait());
        }
        UserFactory.update(self);
        // 构建自己的用户信息
        UserCard card = new UserCard(self, true);
        return ResponseModel.buildOk(card);
    }

    /**
     * 拉取self联系人的接口
     * @return ResponseModel<List<UserCard>>
     */
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact(){
        User self = getSelf();

        List<User> userList = UserFactory.contacts(self);
        List<UserCard> userCards = userList.stream()
                .map(user -> new UserCard(user, true))
                .collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }

    /**
     * 关注某个人的接口
     * @param followId followId
     * @return ResponseModel<UserCard>
     */
    @PUT
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId){
        User self = getSelf();
        User followUser = UserFactory.findById(followId);
        // 关注的条件判断: 1. 不能关注自己. 2. 不能重复关注
        if (self.getId().equalsIgnoreCase(followId) || Strings.isNullOrEmpty(followId)){
            return ResponseModel.buildParameterError();
        }else if (followUser == null){
            return ResponseModel.buildNotFoundUserError(null);
        }
        // 备注默认为Null
        followUser = UserFactory.follow(self, followUser, null);
        if (followUser == null){
            return ResponseModel.buildServiceError();
        }else {
            // TODO: 通知乙方被关注
            return ResponseModel.buildOk(new UserCard(followUser, true));
        }
    }

    /**
     * 通过id获取甲方与乙方的Follow情况
     * @param id id
     * @return ResponseModel<UserCard>
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id){
        if (Strings.isNullOrEmpty(id)){
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();
        User user = UserFactory.findById(id);
        boolean isFollow = UserFactory.getUserFollow(self, user) != null;

        // 所要查询的User是self, 直接返回，查个屁
        if (self.getId().equalsIgnoreCase(id)){
            return ResponseModel.buildOk(new UserCard(self, true));
        }else if (user == null){
            return ResponseModel.buildNotFoundUserError(null);
        }else {
            return ResponseModel.buildOk(new UserCard(user, isFollow));
        }
    }

    /**
     * 搜索人的接口
     * 如果name为空, 则返回最近的用户
     * 只返回20条数据
     * @param name Name
     * @return ResponseModel<List<UserCard>> 查询到的用户集合
     */
    @GET
    @Path("/search/{name:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name){

        User self = getSelf();
        List<User> userList = UserFactory.search(name);
        final List<User> contact = UserFactory.contacts(self);

        // User转换为UserCard, 并且进行联系人的任意匹配
        List<UserCard> userCards = userList.stream()
                .map(user -> {
                    // 判断是否为自己, 判断是否互为好友
                    boolean isFollow = user.getId().equalsIgnoreCase(self.getId())
                            || contact.stream().anyMatch(contactUser -> contactUser.getId().equalsIgnoreCase(user.getId()));
                    return new UserCard(user, isFollow);
                }).collect(Collectors.toList());
        return ResponseModel.buildOk(userCards);
    }
}
