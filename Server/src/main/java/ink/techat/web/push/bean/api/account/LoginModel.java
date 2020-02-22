package ink.techat.web.push.bean.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

/**
 * 登录Model
 * @author NickCharlie
 */
public class LoginModel {
    @Expose
    private String account;
    @Expose
    private String password;
    @Expose
    private String pushId;
    /**
     * 输入model检查
     * @param model Model
     * @return 输入是否合格
     */
    public static boolean check(LoginModel model){
        return model != null
                && !Strings.isNullOrEmpty(model.account)
                && !Strings.isNullOrEmpty(model.password);
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
