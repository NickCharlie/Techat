package ink.techat.web.push.bean.db;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户的Model，对应数据库
 * @author NickCharlie
 */
@Entity
@Table(name = "TB_USER")
public class User implements Principal {
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

    /**
     * 主键Id
     * 主键生成存储的类型为UUID
     * 把uuid的生成器定义为uuid2，uuid2是常规的UUID toString
     * 不允许更改，不允许为空
     */
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy =  "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    /**
     * 用户权限
     */
    @Column(nullable = false)
    private int userPermissionType = USER_PERMISSION_NONE;

    /**
     * 用户名 name
     * 用户名必须唯一
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * 电话号码 phone
     * 电话号码 必须唯一
     */
    @Column(nullable = false, length = 62, unique = true)
    private String phone;

    /**
     * 用户密码 password
     * 不能为空
     */
    @Column(nullable = false)
    private String password;

    /**
     * 头像 portrait
     * 允许为null
     */
    @Column
    private String portrait;

    /**
     * 描述字段 description
     * 允许为null
     */
    @Column
    private String description;

    /**
     * 性别 sex
     * 有初始值，所以不为空
     */
    @Column(nullable = false)
    private int sex = 0;

    /**
     * Set<UserFollow> followSet
     * 我关注的人的列表方法 一对多
     * originId 对应的数据库表字段
     * 对应数据库表字段为 TB_USER_FOLLOW.originId
     * LazyCollection(LazyCollectionOption.EXTRA) 定义为懒加载
     * 默认加载User信息的时候，并不会查询这个集合
     */
    @JoinColumn(name = "originId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> followingSet = new HashSet<>();

    /**
     * Set<UserFollow> followsSet
     * 关注我的人列表方法 一对多
     * targetId 对应的数据库表字段
     * 对应数据库表字段为 TB_USER_FOLLOW.targetId
     * LazyCollection(LazyCollectionOption.EXTRA) 定义为懒加载
     * 默认加载User信息的时候，并不会查询这个集合
     */
    @JoinColumn(name = "targetId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> followsSet = new HashSet<>();

    /**
     * 我创建的群 Set<Group> groups
     * 对应的字段为 Group.ownerId
     * LazyCollection 懒加载集合方式: 尽可能不加载具体数据
     * fetch FetchType.LAZY 懒加载
     */
    @JoinColumn(name = "ownerId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Group> groups = new HashSet<>();

    /**
     * token 用于拉取用户信息，必须唯一
     */
    @Column(unique = true)
    private String token;

    /**
     * 用于推送的设备ID
     */
    @Column
    private String pushId;

    /**
     * 创建时间戳 createAt
     * 定义为创建时间戳，在创建时就已经写入
     * 不可为空
     */
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    /**
     * 更新时间戳 createAt
     * 定义为更新时间戳，在更新时写入
     * 不可为空
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    /**
     * 最后时间戳 createAt
     * 最后一次收到消息的时间
     * 可为空
     */
    @Column
    private LocalDateTime lastReceivedAt = LocalDateTime.now();



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDateTime getLastReceivedAt() {
        return lastReceivedAt;
    }

    public void setLastReceivedAt(LocalDateTime lastReceivedAt) {
        this.lastReceivedAt = lastReceivedAt;
    }

    public Set<UserFollow> getFollowingSet() {
        return followingSet;
    }

    public void setFollowingSet(Set<UserFollow> followingSet) {
        this.followingSet = followingSet;
    }

    public Set<UserFollow> getFollowsSet() {
        return followsSet;
    }

    public void setFollowsSet(Set<UserFollow> followsSet) {
        this.followsSet = followsSet;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public int getUserPermissionType() {
        return userPermissionType;
    }
}
