package com.yrmjhtdjxh.punch.mapper;

import com.yrmjhtdjxh.punch.domain.StudentRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRoleMapper {

    StudentRole getOneByUserId(@Param("userId")Long userId);

    int insert(@Param("userId")Long userId,@Param("userRole")Integer userRole);

    int updateUserRole(@Param("userId")Long userId,@Param("userRole")Integer userRole);

    int deleteByUserId(@Param("userId")Long userId);

}
