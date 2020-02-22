package ink.techat.web.push.bean.api.account;

import com.google.gson.annotations.Expose;
import ink.techat.web.push.bean.card.UserCard;
import ink.techat.web.push.bean.db.User;

/**
 * �˻����ַ��ص�Model
 * @author NickCharlie
 */
public class AccountRespModel {
    /**
     * �û�������Ϣ
     * UserCard user �û���Ƭ
     * String account ��ǰ��¼���˺�
     * boolean isBind ��ǰ��ʶ�Ƿ��Ѿ��󶨵��豸
     * String token ��ǰ��½�ɹ����ȡ��token��token���Ի�ȡ�û���������Ϣ
     */
    @Expose
    private UserCard user;
    @Expose
    private String account;
    @Expose
    private String token;
    @Expose
    private boolean isBind;

    public AccountRespModel(User user){
        // Ĭ���ް�
        this(user, false);
    }

    public AccountRespModel(User user, boolean isBind){
        this.user = new UserCard(user);
        this.account = user.getPhone();
        this.token = user.getToken();
        this.isBind = isBind;
    }

    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
