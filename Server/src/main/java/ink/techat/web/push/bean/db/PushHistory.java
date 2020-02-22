package ink.techat.web.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * ��Ϣ������ʷ��¼��
 * @author NickCharlie
 */
@Entity
@Table(name = "TB_PUSH_HISTORY")
public class PushHistory {

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
     * ���͵ľ���ʵ�� entity
     * �洢�Ķ���JSON�ַ���
     * columnDefinition BLOB ���ֶ�����
     */
    @Column(nullable = false, columnDefinition = "BLOB")
    private String entity;

    /**
     * ���͵�ʵ������ entityType
     */
    @Column(nullable = false)
    private int entityType;

    /**
     * ��Ϣ������ receiver
     * fetch FetchType.EAGER ������
     * ���һ������Ϊ��
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "receiverId")
    private User receiver;

    @Column(nullable = false, updatable = false, insertable = false)
    private String receiverId;

    /**
     * ��Ϣ������ sender
     * fetch FetchType.EAGER ������
     * ���һ����Ϊ�գ���Ϊ������ϵͳ��Ϣ
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "senderId")
    private User sender;

    @Column(updatable = false, insertable = false)
    private String senderId;

    /**
     * �����ߵ�ǰ״̬�µ�pushId
     * ��Ӧ User.pushId ��ΪNull
     */
    @Column
    private String receiverPushId;

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
     * ��Ϣ�ʹ��ʱ�� arrivalAt
     * ��Ϊnull
     */
    @Column
    private LocalDateTime arrivalAt = LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverPushId() {
        return receiverPushId;
    }

    public void setReceiverPushId(String receiverPushId) {
        this.receiverPushId = receiverPushId;
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

    public LocalDateTime getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(LocalDateTime arrivalAt) {
        this.arrivalAt = arrivalAt;
    }
}
