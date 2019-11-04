package com.yrmjhtdjxh.punch.form;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author dengg
 */
@Data
public class StudentRoleForm {

    @NotNull
    private Long userId;

    @Max(3)
    @Min(1)
    private Integer userRole;

}
