package ink.techat.web.push.factory;

import ink.techat.web.push.bean.db.Group;
import ink.techat.web.push.bean.db.GroupMember;
import ink.techat.web.push.bean.db.User;

import java.util.Set;
/**
 * 群数据处理类
 * @author NickCharlie
 */
public class GroupFactory {
    public static Group findById(String groupId) {
        // TODO 查询群
        return null;
    }

    public static Group findById(User sender, String groupId) {
        // TODO 查询群, 同时该User必须为群成员, 否者返回Null
        return null;
    }

    public static Set<GroupMember> getMembers(Group group) {
        // TODO 查询群成员
        return null;
    }
}
