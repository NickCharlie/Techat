package ink.techat.web.push.bean.db;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * �û���ϵ��Model
 * �����û�֮����к��ѹ�ϵ��ʵ��
 * @author NickCharlie
 */

@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {

    /**
     * �û�Id
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
     * User origin
     * ��ע�����ˣ��൱��myself
     * ���һ���ɴ��������ע��Ϣ User ��Ӧ��� UserFollow
     * optional false ����ѡ������洢������Ҫ�й�ע��
     * originId �����ı��ֶΣ���ӦUser�е�Id�ֶ�
     * JoinColumn ������ȡ��Model����
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "originId")
    private User origin;

    @Column(nullable = false, updatable = false, insertable = false)
    private String originId;

    /**
     * User target
     * ��עĿ�꣬�൱��You
     * ���һ�����UserFollow ��Ӧһ�� User
     * optional false ����ѡ������洢
     * targetId �����ı��ֶΣ���ӦUser�е�Id�ֶ�
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "targetId")
    private User target;

    @Column(nullable = false, updatable = false, insertable = false)
    private String targetId;

    /**
     * String alias ��������target�ı�ע
     * ��Ϊnull
     */
    @Column
    private String alias;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
