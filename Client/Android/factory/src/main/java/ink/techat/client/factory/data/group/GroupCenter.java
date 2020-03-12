package ink.techat.client.factory.data.group;

import ink.techat.client.factory.model.card.GroupCard;
import ink.techat.client.factory.model.card.GroupMemberCard;

/**
 * 群中心的接口定义
 * @author NickCharlie
 */
public interface GroupCenter {
    void dispatch(GroupCard... cards);
    void dispatch(GroupMemberCard... cards);
}
