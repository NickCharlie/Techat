package ink.techat.client.factory.model.api.account;

import ink.techat.client.factory.model.db.User;

public class AccountRspModel {

    /**
     * 用户基本信息
     * UserCard user 用户名片
     * String account 当前登录的账号
     * boolean isBind 当前标识是否已经绑定到设备
     * String token 当前登陆成功后获取的token，token可以获取用户的所有信息
     * int userPermission 用户权限信息
     */
    private User user;
    private String account;
    private String token;
    private boolean isBind;
    private int userPermissionType;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public int getUserPermissionType() {
        return userPermissionType;
    }

    public void setUserPermissionType(int userPermissionType) {
        this.userPermissionType = userPermissionType;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "user=" + user +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBind=" + isBind +
                ", userPermissionType=" + userPermissionType +
                '}';
    }
}
