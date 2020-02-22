package ink.techat.web.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Message Model
 * @author NicKCharlie
 */
@Entity
@Table(name = "TB_MESSAGE")
public class Message {
    /**
     * 消息类型
     */
    public static final int TYPE_STR = 1;
    public static final int TYPE_PIC = 2;
    public static final int TYPE_FILE = 3;
    public static final int TYPE_AUDIO = 4;
    /**
     * 主键Id
     * 主键生成存储的类型为UUID
     * 把uuid的生成器定义为uuid2，uuid2是常规的UUID toString
     * 不自动生成UUID,Id由代码写入，客户端负责生成
     * 避免复炸的服务器和客户端的映射关系
     * 不允许更改，不允许为空
     */
    @Id
    @PrimaryKeyJoinColumn
    @GenericGenerator(name = "uuid", strategy =  "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    /**
     * String content 消息内容
     * 不允许为空，类型为TEXT
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * String attach 附件
     */
    @Column
    private String attach;

    /**
     * int type 消息类型
     */
    @Column(nullable = false)
    private int type;

    /**
     * User sender
     * 消息发送者，多对一
     * 不为空
     */
    @JoinColumn(name = "senderId")
    @ManyToOne(optional = false)
    private User sender;

    @Column(nullable = false, updatable = false, insertable = false)
    private String senderId;

    /**
     * User receiver
     * 接收者，多对一
     * 可为空
     */
    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;

    @Column(updatable = false, insertable = false)
    private String receiverId;

    /**
     * 群消息
     */
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;

    @Column(updatable = false, insertable = false)
    private String groupId;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
