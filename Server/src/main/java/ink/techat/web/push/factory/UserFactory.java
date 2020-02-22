package ink.techat.web.push.factory;

import com.google.common.base.Strings;
import ink.techat.web.push.bean.api.base.ResponseModel;
import ink.techat.web.push.utils.TextUtil;
import ink.techat.web.push.bean.db.User;
import ink.techat.web.push.utils.Hib;
import org.hibernate.Session;

import java.util.List;
import java.util.UUID;

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
        if(pushId.equalsIgnoreCase(user.getPushId())){
            // 如果当前需要绑定的设备Id已经绑定国了，不需要额外绑定
            return user;
        }else {
            // 如果当前账户之前的设备Id，和需要绑定的不同那么需要单点登录
            // 让之前的设备退出账户，推送一条退出消息
            if(Strings.isNullOrEmpty(user.getPushId())){
                // TODO: 推送一个退出消息
            }
            user.setPushId(pushId);
            // 更新数据库信息
            return Hib.query(sessions -> {
                sessions.saveOrUpdate(user);
                return user;
            });
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
        return Hib.query(sessions -> {
            sessions.saveOrUpdate(user);
            return user;
        });
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
