package ink.techat.client.factory.data.user;

import ink.techat.client.factory.model.card.UserCard;

/**
 * 用户中心的基本定义
 * @author NickCharlie
 */
public interface UserCenter {
    /**
     * 方法处理一坨卡片的信息, 并且更新到数据库
     * @param cards 用户卡片
     */
    void dispatch(UserCard... cards);
}
