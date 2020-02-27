package ink.techat.client.factory.model.db;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

/**
 * 用户的Model
 * @author NickCharlie
 */
@Table(database = AppDatabase.class)
public class User extends BaseModel {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserPermissionType() {
        return userPermissionType;
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

    public void setUserPermissionType(int userPermissionType) {
        this.userPermissionType = userPermissionType;
    }
}
