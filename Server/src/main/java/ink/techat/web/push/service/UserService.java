package ink.techat.web.push.service;

import com.google.common.base.Strings;
import com.mysql.cj.api.xdevapi.BaseSession;
import ink.techat.web.push.bean.api.base.ResponseModel;
import ink.techat.web.push.bean.api.user.UpdateInfoModel;
import ink.techat.web.push.bean.card.UserCard;
import ink.techat.web.push.bean.db.User;
import ink.techat.web.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * @author NicKCharlie
 * 127.0.0.1/api/user
 */
@Path("/user")
public class UserService extends BaseService {

    /**
     * �����Լ��ĸ�����Ϣ
     * @param token Token
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
        UserFactory.update(self);
        // �����Լ����û���Ϣ
        UserCard card = new UserCard(self, true);
        return ResponseModel.buildOk(card);
    }
}
