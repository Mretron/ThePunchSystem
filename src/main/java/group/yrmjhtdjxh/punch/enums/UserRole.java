package group.yrmjhtdjxh.punch.enums;

import lombok.Getter;

/**
 * @author dengg
 */
@Getter
public enum  UserRole {

    /**
     *
     */
    ADMINISTRATOR(1,"管理员"),
    AVERAGE_USER(2,"普通用户"),
    REGISTER_USER(3,"注册用户");

    private Integer value;

    private String tips;

    UserRole(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }

}
