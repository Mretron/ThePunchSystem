package group.yrmjhtdjxh.punch.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentForm {
    /**
     * 作为自增主键
     */
    @Id
    @Column(keyColumn = "id")
    private int id;

    /**
     * 学号
     */
    @NotNull
    private Long studentID;

    /**
     * 密码
     */
    private String password;

    /**
     * 学生姓名
     */
    @NotNull
    private String name;

    /**
     * 性别 1：男 0：女
     */
    @Max(1)
    @Min(0)
    private int sex;

    /**
     * 学生的年级
     */
    @NotNull
    private int grade;

    /**
     * 学生的初始照片
     */
    private String photo;

    /**
     * 学生的头像
     */
    private String avatar;

    /**
     * 创建当前学生的日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否在打卡
     */
    private boolean isPunch;


}
