package ink.techat.web.push.bean.db;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 用户关系的Model
 * 用于用户之间进行好友关系的实现
 * @author NickCharlie
 */

@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {

    /**
     * 用户Id
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
     * User origin
     * 关注发起人，相当于myself
     * 多对一，可创建多个关注信息 User 对应多个 UserFollow
     * optional false 不可选，必须存储，必须要有关注人
     * originId 关联的表字段，对应User中的Id字段
     * JoinColumn 把列提取到Model当中
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "originId")
    private User origin;

    @Column(nullable = false, updatable = false, insertable = false)
    private String originId;

    /**
     * User target
     * 关注目标，相当于You
     * 多对一，多个UserFollow 对应一个 User
     * optional false 不可选，必须存储
     * targetId 关联的表字段，对应User中的Id字段
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "targetId")
    private User target;

    @Column(nullable = false, updatable = false, insertable = false)
    private String targetId;

    /**
     * String alias 别名，对target的备注
     * 可为null
     */
    @Column
    private String alias;

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
