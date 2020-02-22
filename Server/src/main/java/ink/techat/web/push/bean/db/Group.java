package ink.techat.web.push.bean.db;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Ⱥ��� Model
 * @author NickCharlie
 */
@Entity
@Table(name = "TB_GROUP")
public class Group {

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
     * Ⱥ���� name
     * Ⱥ���Ʊ���Ψһ
     */
    @Column(nullable = false)
    private String name;

    /**
     * Ⱥ���� description
     */
    @Column(nullable = false)
    private String description;

    /**
     * Ⱥͷ�� picture
     */
    @Column(nullable = false)
    private String picture;

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
     * Ⱥ������ owner
     * fetch FetchType.EAGER �����أ�����Ⱥ��Ϣʱ������ش�������Ϣ
     * optional ��ѡΪfalse�������д�����
     * cascade CascadeType.ALL ��������ΪALL�����Ե����ݸ��Ķ����й�ϵ����
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId")
    private User owner;
    @Column(nullable = false, updatable = false, insertable = false)
    private String ownerId;

    public Group() {

    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
