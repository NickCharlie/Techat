package ink.techat.web.push.factory;

import ink.techat.web.push.bean.db.Group;
import ink.techat.web.push.bean.db.GroupMember;
import ink.techat.web.push.bean.db.User;

import java.util.Set;
/**
 * Ⱥ���ݴ�����
 * @author NickCharlie
 */
public class GroupFactory {
    public static Group findById(String groupId) {
        // TODO ��ѯȺ
        return null;
    }

    public static Group findById(User sender, String groupId) {
        // TODO ��ѯȺ, ͬʱ��User����ΪȺ��Ա, ���߷���Null
        return null;
    }

    public static Set<GroupMember> getMembers(Group group) {
        // TODO ��ѯȺ��Ա
        return null;
    }
}
