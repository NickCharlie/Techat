package ink.techat.client.factory.model.card;

import java.time.LocalDateTime;
import java.util.Date;

import ink.techat.client.factory.model.Author;
import ink.techat.client.factory.model.db.User;

/**
 * 用户信息卡片
 * @author NicKCharlie
 */
public class UserCard implements Author {
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
        this.modifyAt = user.getModifyAt();
        this.isFollow = isFollow;
        // TODO 得到关注人和粉丝的数量
    }

    private String id;

    private int userPermissionType;

    private String name;

    private String phone;

    private String portrait;

    private String description;

    private int sex = 0;

    private int follows;

    private int following;

    /**
     * 与当前User的关系
     */
    private boolean isFollow;

    /**
     * 用户信息最后的更新时间
     */
    private Date modifyAt;

    /**
     * 用于缓存的User
     * transient 不会被GSON自动解析
     */
    private transient User user;

    public User build(){
        if (user == null){
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setPhone(phone);
            user.setPortrait(portrait);
            user.setDescription(description);
            user.setSex(sex);
            user.setFollows(follows);
            user.setFollowing(following);
            user.setFollow(isFollow);
            this.user = user;
        }
        return user;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getUserPermissionType() {
        return userPermissionType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPortrait() {
        return portrait;
    }

    @Override
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

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }
}
