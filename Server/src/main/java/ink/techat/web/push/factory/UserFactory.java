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
     * ͨ��Phone����User
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
     * ͨ��Token����User
     * ֻ���Լ�ʹ�ã���ѯ����Ϣ�Ǹ�����Ϣ
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
     * ����ǰ���˻���pushId
     * @param user User
     * @param pushId ��ǰ�豸pushId
     * @return User
     */
    public static User bindPushId(User user, String pushId){
        if(Strings.isNullOrEmpty(pushId)){
            return null;
        }

        // ע�⣺��ѯ���б��ܰ����Լ�
        Hib.queryOnly(session -> {
            @SuppressWarnings("unchecked")
            List<User> userList = (List<User>) session
                .createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                .setParameter("pushId", pushId.toLowerCase())
                .setParameter("userId", user.getId())
                .list();

            for (User userNew : userList) {
                // ����Ϊnull
                userNew.setPushId(null);
                session.saveOrUpdate(userList);
            }
            return 200;
        });
        if(pushId.equalsIgnoreCase(user.getPushId())){
            // �����ǰ��Ҫ�󶨵��豸Id�Ѿ��󶨹��ˣ�����Ҫ�����
            return user;
        }else {
            // �����ǰ�˻�֮ǰ���豸Id������Ҫ�󶨵Ĳ�ͬ��ô��Ҫ�����¼
            // ��֮ǰ���豸�˳��˻�������һ���˳���Ϣ
            if(Strings.isNullOrEmpty(user.getPushId())){
                // TODO: ����һ���˳���Ϣ
            }
            user.setPushId(pushId);
            // �������ݿ���Ϣ
            return Hib.query(sessions -> {
                sessions.saveOrUpdate(user);
                return user;
            });
        }
    }

    /**
     * ʹ���˺ź�������е�¼�Ĳ���
     * @param account �˺�
     * @param passWord ����
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
            // ��User���е�¼������Token
            user = login(user);
        }
        return user;
    }

    /**
     * �û�ע�� register
     * ע�������Ҫд�����ݿ⣬���ҷ������ݿ��е�User��Ϣ
     * @param account �˻�
     * @param password ����
     * @param name �û���
     * @return User
     */
    public static User register(String account, String password, String name){
            // �˻�ȥ����β�ո�
            account = account.trim();
            password = TextUtil.encodeBase64(password);
            User user = createUser(account, password, name);
            if(user != null){
                user = login(user);
            }
            return user;
    }

    /**
     * User��¼���� login
     * @param user User
     * @return User
     */
    private static User login(User user){
        // ���UUIDֵ��ΪToken
        String newToken = UUID.randomUUID().toString();

        // ��newToken����һ�μ���
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);

        // �����������ݿ���Ϣ
        return Hib.query(sessions -> {
            sessions.saveOrUpdate(user);
            return user;
        });
    }

    /**
     * �����û����� createUser
     * @param account �˺�
     * @param passWord ���ܺ������
     * @param name ����
     * @return �ɹ��򷵻�User�����򷵻�null
     */
    private static User createUser(String account, String passWord, String name){
        User user = new User();
        user.setName(name);
        user.setPassword(passWord);
        user.setPhone(account);

        // ���ݿ�洢
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
     * ������ļ��ܲ���
     * @param password ԭ��
     * @return ����
     */
    private static String encodePassword(String password){
        // ����ȥ��λ�ո�
        password = password.trim();
        // MD5����
        // password = TextUtil.getMD5(password);
        // �ٽ���һ�ζԳƵ�Base64����
        return TextUtil.encodeBase64(password);
    }
}
