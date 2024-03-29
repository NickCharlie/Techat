package ink.techat.client.factory.model.db;


import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Date;
import java.util.Objects;

import ink.techat.client.factory.model.Author;
import ink.techat.client.utils.TextMatch;

/**
 * 用户的Model
 * @author NickCharlie
 */
@Table(database = AppDatabase.class)
public class User extends BaseDbModel<User> implements Author {

    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 0;
    public static final int SEX_OTHER = 3;

    @PrimaryKey
    private String id;
    @Column
    private int userPermissionType;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String description;
    @Column
    private int sex = 0;
    @Column
    private int follows;
    @Column
    private int following;

    /**
     * 好友备注
     */
    @Column
    private String alias;

    /**
     * 与当前User的关系
     */
    @Column
    private boolean isFollow;


    /**
     * 用户信息最后的更新时间
     */
    @Column
    private Date modifyAt;

    public void setUserPermissionType(int userPermissionType) {
        this.userPermissionType = userPermissionType;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userPermissionType=" + userPermissionType +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", portrait='" + portrait + '\'' +
                ", description='" + description + '\'' +
                ", sex=" + sex +
                ", follows=" + follows +
                ", following=" + following +
                ", alias='" + alias + '\'' +
                ", isFollow=" + isFollow +
                ", modifyAt=" + modifyAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        TextMatch textMatch = new TextMatch();
        return sex == user.sex
                && follows == user.follows
                && following == user.following
                && isFollow == user.isFollow
                && textMatch.sunday(id, user.id)
                && textMatch.sunday(name, user.name)
                && textMatch.sunday(phone, user.phone)
                && Objects.equals(portrait, user.portrait)
                && textMatch.sunday(description, user.description)
                && textMatch.sunday(alias, user.alias)
                && Objects.equals(modifyAt, user.modifyAt);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isSame(User old) {
        return this == old || new TextMatch().sunday(id, old.getId());
    }

    @Override
    public boolean isUiContentSame(User old) {
        // 显示的内容是否相同
        return this == old || (
                Objects.equals(name, old.getName())
                && Objects.equals(userPermissionType, old.getUserPermissionType())
                && Objects.equals(sex, old.getSex())
                && Objects.equals(isFollow, old.isFollow)
                && Objects.equals(portrait, old.getPortrait()));
    }
}
