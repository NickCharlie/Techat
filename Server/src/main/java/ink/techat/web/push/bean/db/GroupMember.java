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
     * Ⱥ����Ϣ֪ͨ����
     * NOTIFY_LEVEL_INVALID ��������Ϣ
     * NOTIFY_LEVEL_NONE Ĭ��֪ͨ����
     * NOTIFY_LEVEL_CLOSE ������Ϣ����ʾ
     */
    public static final int NOTIFY_LEVEL_INVALID = -1;
    public static final int NOTIFY_LEVEL_NONE = 0;
    public static final int NOTIFY_LEVEL_CLOSE = 1;

    /**
     * Ⱥ���ԱȨ�޼���
     * GROUP_PERMISSION_NONE Ĭ�ϳ�ԱȨ��
     * GROUP_PERMISSION_ADMIN ����ԱȨ��
     * GROUP_PERMISSION_ADMIN_SUPER ��������ԱȨ��
     */
    public static final int GROUP_PERMISSION_NONE = 0;
    public static final int GROUP_PERMISSION_ADMIN = 1;
    public static final int GROUP_PERMISSION_ADMIN_SUPER = 2;

    /**
     * ����Id
     * �������ɴ洢������ΪUUID
     * ��uuid������������Ϊuuid2��uuid2�ǳ����UUID toString
     * ��������ģ�������Ϊ��
     */
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy =  "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    /**
     * String alias ����
     * ��Ϊnull
     */
    @Column
    private String alias;

    /**
     * ��Ϣ֪ͨ���� notifyLevel
     */
    @Column(nullable = false)
    private int notifyLevel = NOTIFY_LEVEL_NONE;

    /**
     * Ⱥ��ԱȨ������
     */
    @Column(nullable = false)
    private int permissionType = GROUP_PERMISSION_NONE;

    /**
     * Ⱥ��Ա��Ӧ���û���Ϣ
     * fetch FetchType.EAGER ������
     */
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    @Column(nullable = false, updatable = false, insertable = false)
    private String userId;

    /**
     * ��Ա��Ϣ��Ӧ��Ⱥ��Ϣ
     * fetch FetchType.EAGER ������
     */
    @JoinColumn(name = "groupId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Group group;

    @Column(nullable = false, updatable = false, insertable = false)
    private String groupId;

    /**
     * ����ʱ��� createAt
     * ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
     * ����Ϊ��
     */
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    /**
     * ����ʱ��� createAt
     * ����Ϊ����ʱ������ڸ���ʱд��
     * ����Ϊ��
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
