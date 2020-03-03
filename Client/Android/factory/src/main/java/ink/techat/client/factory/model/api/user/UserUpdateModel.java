package ink.techat.client.factory.model.api.user;

/**
 * @author NicKCharlie
 */
public class UserUpdateModel {
    private String name;
    private String portrait;
    private String description;
    private int sex;

    public UserUpdateModel(String name, String portrait, String desc, int sex) {
        this.name = name;
        this.portrait = portrait;
        this.description = desc;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserUpdateModel{" +
                "name='" + name + '\'' +
                ", portrait='" + portrait + '\'' +
                ", desc='" + description + '\'' +
                ", sex=" + sex +
                '}';
    }
}
