package ink.techat.web.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * �����¼��
 * @author NickCharlie
 */
@Entity
@Table(name = "TB_APPLY")
public class Apply {
    /**
     * ��������
     * APPLE_TYPE_ADD_USER ����û�
     * APPLE_TYPE_ADD_GROUP ���Ⱥ
     * APPLE_TYPE_ADD_PUBLIC ��ע���ں�
     * APPLE_TYPE_OTHER ��������
     */
    public static final int APPLE_TYPE_ADD_USER = 1;
    public static final int APPLE_TYPE_ADD_GROUP = 2;
    public static final int APPLE_TYPE_ADD_PUBLIC = 3;
    public static final int APPLE_TYPE_OTHER = 0;

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
     * �������֣���������Ϣ������
     */
    @Column(nullable = false)
    private String description;

    /**
     * ���븽������Ϊ��
     */
    @Column(columnDefinition = "TEXT")
    private String attach;

    /**
     * ��������
     */
    @Column(nullable = false)
    private int type;

    /**
     * ����Ŀ��Id targetId ������ǿ�������������������ϵ
     * eg: type -> APPLE_TYPE_ADD_USER: User.id
     */
    @Column(nullable = false)
    private String targetId;

    /**
     * ������ applicant
     * ��Ϊ��
     */
    @ManyToOne
    @JoinColumn(name = "applicantId")
    private User applicant;

    @Column(updatable = false, insertable = false)
    private String applicantId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
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
