package ink.techat.web.push.bean.card;

import com.google.gson.annotations.Expose;
import ink.techat.web.push.bean.db.User;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author NickCharlie
 */
public class UserCard {
    /**
     * 用户权限级别
     * USER_PERMISSION_NONE 默认用户权限
     * USER_PERMISSION_TEACHER 教师权限
     * USER_PERMISSION_ADMIN 管理员权限
     * USER_PERMISSION_DEVELOPER 开发者权限
     */
    public static final int USER_PERMISSION_NONE = 0;
    public static final int USER_PERMISSION_TEACHER = 1;
    public static final int USER_PERMISSION_ADMIN = 3;
    public static final int USER_PERMISSION_DEVELOPER = 5;

    public UserCard(final User user){
        this(user, false);
        // TODO 得到关注人和粉丝的数量
    }

    public UserCard(final User user, boolean isFollow){
        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.portrait = user.getPortrait();
        this.description = user.getDescription();
        this.sex = user.getSex();
        this.modifyAt = user.getUpdateAt();
        this.isFollow = isFollow;
        // TODO 得到关注人和粉丝的数量
    }

    @Expose
    private String id;

    @Expose
    private int userPermissionType = USER_PERMISSION_NONE;

    @Expose
    private String name;

    @Expose
    private String phone;

    @Expose
    private String portrait;

    @Expose
    private String description;

    @Expose
    private int sex = 0;

    @Expose
    private int follows;

    @Expose
    private int following;

    /**
     * 与当前User的关系
     */
    @Expose
    private boolean isFollow;

    /**
     * 用户信息最后的更新时间
     */
    @Expose
    private LocalDateTime modifyAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserPermissionType() {
        return userPermissionType;
    }

    public void setUserPermissionType(int userPermissionType) {
        this.userPermissionType = userPermissionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}
