package ink.techat.client.factory.model.api.account;

import androidx.annotation.NonNull;

/**
 * 注册使用的请求Model
 * @author NickCharlie
 */
public class RegisterModel {
    private String account;
    private String password;
    private String name;
    private String pushId;
    private String portrait;

    public RegisterModel(String account, String password, String name) {
        this(account, password, name, null, null);
    }

    public RegisterModel(String account, String password, String name, String portrait) {
        this(account, password, name, null, portrait);
    }

    public RegisterModel(String account, String password, String name, String pushId, String portrait) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.pushId = pushId;
        this.portrait = portrait;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
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

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    @NonNull
    @Override
    public String toString() {
        return "RegisterModel{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", pushId='" + pushId + '\'' +
                '}';
    }
}
