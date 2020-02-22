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
 * 群组的 Model
 * @author NickCharlie
 */
@Entity
@Table(name = "TB_GROUP")
public class Group {

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
     * 群名称 name
     * 群名称必须唯一
     */
    @Column(nullable = false)
    private String name;

    /**
     * 群描述 description
     */
    @Column(nullable = false)
    private String description;

    /**
     * 群头像 picture
     */
    @Column(nullable = false)
    private String picture;

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

    /**
     * 群创建者 owner
     * fetch FetchType.EAGER 急加载，加载群信息时必须加载创建者信息
     * optional 可选为false，必须有创建者
     * cascade CascadeType.ALL 联级级别为ALL，所以的内容更改都进行关系更新
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
