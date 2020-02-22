package ink.techat.web.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author NicKCharlie
 */
@Entity
@Table(name = "TB_GROUP_MEMBER")
public class GroupMember {
    /**
     * 群组消息通知级别
     * NOTIFY_LEVEL_INVALID 不接受消息
     * NOTIFY_LEVEL_NONE 默认通知级别
     * NOTIFY_LEVEL_CLOSE 接受消息不提示
     */
    public static final int NOTIFY_LEVEL_INVALID = -1;
    public static final int NOTIFY_LEVEL_NONE = 0;
    public static final int NOTIFY_LEVEL_CLOSE = 1;

    /**
     * 群组成员权限级别
     * GROUP_PERMISSION_NONE 默认成员权限
     * GROUP_PERMISSION_ADMIN 管理员权限
     * GROUP_PERMISSION_ADMIN_SUPER 超级管理员权限
     */
    public static final int GROUP_PERMISSION_NONE = 0;
    public static final int GROUP_PERMISSION_ADMIN = 1;
    public static final int GROUP_PERMISSION_ADMIN_SUPER = 2;

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
     * String alias 别名
     * 可为null
     */
    @Column
    private String alias;

    /**
     * 消息通知级别 notifyLevel
     */
    @Column(nullable = false)
    private int notifyLevel = NOTIFY_LEVEL_NONE;

    /**
     * 群成员权限类型
     */
    @Column(nullable = false)
    private int permissionType = GROUP_PERMISSION_NONE;

    /**
     * 群成员对应的用户信息
     * fetch FetchType.EAGER 急加载
     */
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    @Column(nullable = false, updatable = false, insertable = false)
    private String userId;

    /**
     * 成员信息对应的群信息
     * fetch FetchType.EAGER 急加载
     */
    @JoinColumn(name = "groupId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Group group;

    @Column(nullable = false, updatable = false, insertable = false)
    private String groupId;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
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
}
