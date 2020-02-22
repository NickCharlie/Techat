package ink.techat.web.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 申请记录表
 * @author NickCharlie
 */
@Entity
@Table(name = "TB_APPLY")
public class Apply {
    /**
     * 申请类型
     * APPLE_TYPE_ADD_USER 添加用户
     * APPLE_TYPE_ADD_GROUP 添加群
     * APPLE_TYPE_ADD_PUBLIC 关注公众号
     * APPLE_TYPE_OTHER 其他申请
     */
    public static final int APPLE_TYPE_ADD_USER = 1;
    public static final int APPLE_TYPE_ADD_GROUP = 2;
    public static final int APPLE_TYPE_ADD_PUBLIC = 3;
    public static final int APPLE_TYPE_OTHER = 0;

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
     * 描述部分，对申请信息做描述
     */
    @Column(nullable = false)
    private String description;

    /**
     * 申请附件，可为空
     */
    @Column(columnDefinition = "TEXT")
    private String attach;

    /**
     * 申请类型
     */
    @Column(nullable = false)
    private int type;

    /**
     * 申请目标Id targetId 不进行强关联，不建立主外键关系
     * eg: type -> APPLE_TYPE_ADD_USER: User.id
     */
    @Column(nullable = false)
    private String targetId;

    /**
     * 申请人 applicant
     * 可为空
     */
    @ManyToOne
    @JoinColumn(name = "applicantId")
    private User applicant;

    @Column(updatable = false, insertable = false)
    private String applicantId;

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
