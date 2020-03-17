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
     * �����Լ��ĸ�����Ϣ
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
        // ��ȡ����
        User self = getSelf();
        self = model.updateToUser(self);
        // ������Ϣ
        if ((self.getPortrait()).equals(USER_INFO_FROM_SERVER)){
            User user = findByPhone(self.getPhone());
            self.setPortrait(user.getPortrait());
        }
        UserFactory.update(self);
        // �����Լ����û���Ϣ
        UserCard card = new UserCard(self, true);
        return ResponseModel.buildOk(card);
    }

    /**
     * ��ȡself��ϵ�˵Ľӿ�
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
     * ��עĳ���˵Ľӿ�
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
        // ��ע�������ж�: 1. ���ܹ�ע�Լ�. 2. �����ظ���ע
        if (self.getId().equalsIgnoreCase(followId) || Strings.isNullOrEmpty(followId)){
            return ResponseModel.buildParameterError();
        }else if (followUser == null){
            return ResponseModel.buildNotFoundUserError(null);
        }
        // ��עĬ��ΪNull
        followUser = UserFactory.follow(self, followUser, null);
        if (followUser == null){
            return ResponseModel.buildServiceError();
        }else {
            // TODO: ֪ͨ�ҷ�����ע
            return ResponseModel.buildOk(new UserCard(followUser, true));
        }
    }

    /**
     * ͨ��id��ȡ�׷����ҷ���Follow���
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

        // ��Ҫ��ѯ��User��self, ֱ�ӷ��أ����ƨ
        if (self.getId().equalsIgnoreCase(id)){
            return ResponseModel.buildOk(new UserCard(self, true));
        }else if (user == null){
            return ResponseModel.buildNotFoundUserError(null);
        }else {
            return ResponseModel.buildOk(new UserCard(user, isFollow));
        }
    }

    /**
     * �����˵Ľӿ�
     * ���nameΪ��, �򷵻�������û�
     * ֻ����20������
     * @param name Name
     * @return ResponseModel<List<UserCard>> ��ѯ�����û�����
     */
    @GET
    @Path("/search/{name:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name){

        User self = getSelf();
        List<User> userList = UserFactory.search(name);
        final List<User> contact = UserFactory.contacts(self);

        // Userת��ΪUserCard, ���ҽ�����ϵ�˵�����ƥ��
        List<UserCard> userCards = userList.stream()
                .map(user -> {
                    // �ж��Ƿ�Ϊ�Լ�, �ж��Ƿ�Ϊ����
                    boolean isFollow = user.getId().equalsIgnoreCase(self.getId())
                            || contact.stream().anyMatch(contactUser -> contactUser.getId().equalsIgnoreCase(user.getId()));
                    return new UserCard(user, isFollow);
                }).collect(Collectors.toList());
        return ResponseModel.buildOk(userCards);
    }
}
