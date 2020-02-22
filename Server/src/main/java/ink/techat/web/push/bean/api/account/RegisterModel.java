package ink.techat.web.push.bean.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

/**
 * @author NickCharlie
 */
public class RegisterModel {
    @Expose
    private String account;
    @Expose
    private String password;
    @Expose
    private String name;
    @Expose
    private String pushId;
    /**
     * ����model���
     * @param model Model
     * @return �����Ƿ�ϸ�
     */
    public static boolean check(RegisterModel model){
        return model != null
                && !Strings.isNullOrEmpty(model.account)
                && !Strings.isNullOrEmpty(model.password)
                && !Strings.isNullOrEmpty(model.name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
