package ink.techat.web.push.bean.api.account;

import com.google.gson.annotations.Expose;
import ink.techat.web.push.bean.card.UserCard;
import ink.techat.web.push.bean.db.User;

/**
 * 账户部分返回的Model
 * @author NickCharlie
 */
public class AccountRespModel {
    /**
     * 用户基本信息
     * UserCard user 用户名片
     * String account 当前登录的账号
     * boolean isBind 当前标识是否已经绑定到设备
     * String token 当前登陆成功后获取的token，token可以获取用户的所有信息
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
        // 默认无绑定
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
