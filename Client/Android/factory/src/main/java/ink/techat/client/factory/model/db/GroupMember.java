package ink.techat.client.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.Objects;

import ink.techat.client.utils.TextMatch;


/**
 * 群成员Model表
 */
@Table(database = AppDatabase.class)
public class GroupMember extends BaseDbModel<GroupMember> {
    // 消息通知级别
    public static final int NOTIFY_LEVEL_INVALID = -1; // 关闭消息
    public static final int NOTIFY_LEVEL_NONE = 0; // 正常

    // 主键
    @PrimaryKey
    private String id;
    // 别名，备注名
    @Column
    private String alias;
    // 是否是管理员
    @Column
    private boolean isAdmin;
    // 是否是群创建者
    @Column
    private boolean isOwner;
    // 更新时间
    @Column
    private Date modifyAt;
    // 对应的群外键
    @ForeignKey(tableClass = Group.class, stubbedRelationship = true)
    private Group group;
    // 对应的用户外键
    @ForeignKey(tableClass = User.class, stubbedRelationship = true)
    private User user;

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupMember that = (GroupMember) o;

        return isAdmin == that.isAdmin
                && isOwner == that.isOwner
                && Objects.equals(id, that.id)
                && Objects.equals(alias, that.alias)
                && Objects.equals(modifyAt, that.modifyAt)
                && Objects.equals(group, that.group)
                && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isSame(GroupMember old) {
        return new TextMatch().sunday(id, old.id) ;
    }

    @Override
    public boolean isUiContentSame(GroupMember that) {
        return isAdmin == that.isAdmin
                && isOwner == that.isOwner
                && Objects.equals(alias, that.alias)
                && Objects.equals(modifyAt, that.modifyAt);
    }
}
