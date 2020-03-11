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
 * �û���Model����Ӧ���ݿ�
 * @author NickCharlie
 */
@Entity
@Table(name = "TB_USER")
public class User implements Principal {
    /**
     * �û�Ȩ�޼���
     * USER_PERMISSION_NONE Ĭ���û�Ȩ��
     * USER_PERMISSION_TEACHER ��ʦȨ��
     * USER_PERMISSION_ADMIN ����ԱȨ��
     * USER_PERMISSION_DEVELOPER ������Ȩ��
     */
    public static final int USER_PERMISSION_NONE = 0;
    public static final int USER_PERMISSION_TEACHER = 1;
    public static final int USER_PERMISSION_ADMIN = 3;
    public static final int USER_PERMISSION_DEVELOPER = 5;

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
     * �û�Ȩ��
     */
    @Column(nullable = false)
    private int userPermissionType = USER_PERMISSION_NONE;

    /**
     * �û��� name
     * �û�������Ψһ
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * �绰���� phone
     * �绰���� ����Ψһ
     */
    @Column(nullable = false, length = 62, unique = true)
    private String phone;

    /**
     * �û����� password
     * ����Ϊ��
     */
    @Column(nullable = false)
    private String password;

    /**
     * ͷ�� portrait
     * ����Ϊnull
     */
    @Column
    private String portrait;

    /**
     * �����ֶ� description
     * ����Ϊnull
     */
    @Column
    private String description;

    /**
     * �Ա� sex
     * �г�ʼֵ�����Բ�Ϊ��
     */
    @Column(nullable = false)
    private int sex = 0;

    /**
     * Set<UserFollow> followSet
     * �ҹ�ע���˵��б��� һ�Զ�
     * originId ��Ӧ�����ݿ���ֶ�
     * ��Ӧ���ݿ���ֶ�Ϊ TB_USER_FOLLOW.originId
     * LazyCollection(LazyCollectionOption.EXTRA) ����Ϊ������
     * Ĭ�ϼ���User��Ϣ��ʱ�򣬲������ѯ�������
     */
    @JoinColumn(name = "originId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> followingSet = new HashSet<>();

    /**
     * Set<UserFollow> followsSet
     * ��ע�ҵ����б��� һ�Զ�
     * targetId ��Ӧ�����ݿ���ֶ�
     * ��Ӧ���ݿ���ֶ�Ϊ TB_USER_FOLLOW.targetId
     * LazyCollection(LazyCollectionOption.EXTRA) ����Ϊ������
     * Ĭ�ϼ���User��Ϣ��ʱ�򣬲������ѯ�������
     */
    @JoinColumn(name = "targetId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> followsSet = new HashSet<>();

    /**
     * �Ҵ�����Ⱥ Set<Group> groups
     * ��Ӧ���ֶ�Ϊ Group.ownerId
     * LazyCollection �����ؼ��Ϸ�ʽ: �����ܲ����ؾ�������
     * fetch FetchType.LAZY ������
     */
    @JoinColumn(name = "ownerId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Group> groups = new HashSet<>();

    /**
     * token ������ȡ�û���Ϣ������Ψһ
     */
    @Column(unique = true)
    private String token;

    /**
     * �������͵��豸ID
     */
    @Column
    private String pushId;

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

    /**
     * ���ʱ��� createAt
     * ���һ���յ���Ϣ��ʱ��
     * ��Ϊ��
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
