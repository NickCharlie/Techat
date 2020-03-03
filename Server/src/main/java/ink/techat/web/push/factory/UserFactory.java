package ink.techat.web.push.factory;

import com.google.common.base.Strings;
import ink.techat.web.push.bean.db.UserFollow;
import ink.techat.web.push.utils.TextUtil;
import ink.techat.web.push.bean.db.User;
import ink.techat.web.push.utils.Hib;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author NickCharlie
 */
public class UserFactory {

    /**
     * 通过Phone查找User
     * @param phone phone
     * @return User
     */
    public static User findByPhone(String phone){
        return Hib.query(session -> (User) session
                .createQuery("from User where phone=:inPhone")
                .setParameter("inPhone", phone)
                .uniqueResult());
    }

    /**
     * 通过id查找User
     * @param id userId
     * @return User
     */
    public static User findById(String id){
        return Hib.query(session -> session.get(User.class, id));
    }

    /**
     * 通过Token查找User
     * 只能自己使用，查询的信息是个人信息
     * @param token token
     * @return User
     */
    public static User findByToken(String token){
        return Hib.query(session -> (User) session
                .createQuery("from User where token=:token")
                .setParameter("token", token)
                .uniqueResult());
    }

    /**
     * 更新用户信息方法
     * @param user User
     * @return User
     */
    public static User update(User user){
        return Hib.query(sessions -> {
            sessions.saveOrUpdate(user);
            return user;
        });
    }

    /**
     * 给当前的账户绑定pushId
     * @param user User
     * @param pushId 当前设备pushId
     * @return User
     */
    public static User bindPushId(User user, String pushId){
        if(Strings.isNullOrEmpty(pushId)){
            return null;
        }

        // 注意：查询的列表不能包括自己
        Hib.queryOnly(session -> {
            @SuppressWarnings("unchecked")
            List<User> userList = (List<User>) session
                .createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                .setParameter("pushId", pushId.toLowerCase())
                .setParameter("userId", user.getId())
                .list();

            for (User userNew : userList) {
                // 更新为null
                userNew.setPushId(null);
                session.saveOrUpdate(userList);
            }
            return 200;
        });
        if (pushId.equalsIgnoreCase(user.getPushId())){
            // 如果当前需要绑定的设备Id已经绑定国了，不需要额外绑定
            return user;
        }else {
            // 如果当前账户之前的设备Id，和需要绑定的不同那么需要单点登录
            // 让之前的设备退出账户，推送一条退出消息
            if (Strings.isNullOrEmpty(user.getPushId())){
                // TODO: 推送一个退出消息
            }
            user.setPushId(pushId);
            // 更新数据库信息
            return update(user);
        }
    }

    /**
     * 使用账号和密码进行登录的操作
     * @param account 账号
     * @param passWord 密码
     * @return User
     */
    public static User login(String account, String passWord){
        final String accountStr = account.trim();
        final String encodePassWord = encodePassword(passWord);

        User user = Hib.query(session -> (User) session
                .createQuery("from User where phone=:phone and password=:password")
                .setParameter("phone", accountStr)
                .setParameter("password", encodePassWord)
                .uniqueResult());
        if(user != null){
            // 对User进行登录，更新Token
            user = login(user);
        }
        return user;
    }

    /**
     * 用户注册 register
     * 注册操作需要写入数据库，并且返回数据库中的User信息
     * @param account 账户
     * @param password 密码
     * @param name 用户名
     * @return User
     */
    public static User register(String account, String password, String name){
            // 账户去除首尾空格
            account = account.trim();
            password = TextUtil.encodeBase64(password);
            User user = createUser(account, password, name);
            if(user != null){
                user = login(user);
            }
            return user;
    }

    /**
     * User登录操作 login
     * @param user User
     * @return User
     */
    private static User login(User user){
        // 随机UUID值作为Token
        String newToken = UUID.randomUUID().toString();

        // 将newToken进行一次加密
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);

        // 保存或更新数据库信息
        return update(user);
    }

    /**
     * 创建用户操作 createUser
     * @param account 账号
     * @param passWord 加密后的密码
     * @param name 名称
     * @return 成功则返回User，否则返回null
     */
    private static User createUser(String account, String passWord, String name){
        User user = new User();
        user.setName(name);
        user.setPassword(passWord);
        user.setPhone(account);

        // 数据库存储
        return Hib.query(session -> {
            try {
                session.save(user);
                return user;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * 获取self联系人列表
     * @param self User
     * @return List<User>
     */
    public static List<User> contacts(User self){
        return Hib.query(sessions -> {
            // 重新加载一次用户信息到self, 和当前的session绑定
            sessions.load(self, self.getId());
            Set<UserFollow> followSet = self.getFollowingSet();
            return followSet.stream().map(UserFollow::getTarget)
                    .collect(Collectors.toList());
        });
    }

    /**
     * 关注某人的操作
     * @param origin 关注人
     * @param target 被关注人
     * @param alias 对被关注人的备注
     * @return User 被关注人的User
     */
    public static User follow(final User origin, final User target, final String alias){
        UserFollow follow = getUserFollow(origin, target);
        if (follow != null){
            // 甲方与乙方是好友关系, 无法重复关注
            return follow.getTarget();
        }
        return Hib.query(sessions -> {
            // 由于follow信息的加载方式为懒加载, 所以直接操作需要load
            sessions.load(origin, origin.getId());
            sessions.load(target, target.getId());
            // 甲方关注对方, 乙方默认自动关注甲方
            UserFollow originFollow = new UserFollow();
            originFollow.setOrigin(origin);
            originFollow.setTarget(target);
            originFollow.setAlias(alias);

            UserFollow targetFollow = new UserFollow();
            targetFollow.setOrigin(target);
            targetFollow.setTarget(origin);

            sessions.save(originFollow);
            sessions.save(targetFollow);
            return target;
        });
    }

    /**
     * 查询两个User之间的关系
     * @param origin 甲方
     * @param target 乙方
     * @return UserFollow 返回中间类
     */
    public static UserFollow getUserFollow(final User origin, final User target){
        return (UserFollow) Hib.query(sessions ->
                sessions.createQuery("from UserFollow where originId=:originId and targetId=:targetId")
                .setParameter("originId", origin.getId())
                .setParameter("targetId", target.getId())
                .setMaxResults(1)
                .uniqueResult());
    }

    /**
     * 搜索联系人
     * @param name 查询的name
     * @return List<User>
     */
    @SuppressWarnings("unchecked")
    public static List<User> search(String name) {
        if (Strings.isNullOrEmpty(name)){
            name = "";
        }
        final String searchName = "%" + name + "%";
        return Hib.query(sessions -> {
            return (List<User>) sessions.createQuery("from User where lower(name) like :name")
                    .setParameter("name", searchName)
                    .setMaxResults(20)
                    .list();
        });
    }

    /**
     * 对密码的加密操作
     * @param password 原文
     * @return 密文
     */
    private static String encodePassword(String password){
        // 密码去首位空格
        password = password.trim();
        // MD5加密
        // password = TextUtil.getMD5(password);
        // 再进行一次对称的Base64加密
        return TextUtil.encodeBase64(password);
    }

}
