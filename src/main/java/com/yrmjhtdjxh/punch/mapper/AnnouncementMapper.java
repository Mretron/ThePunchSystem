package com.yrmjhtdjxh.punch.mapper;


import com.yrmjhtdjxh.punch.VO.AnnouncementListVO;
import com.yrmjhtdjxh.punch.domain.Announcement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Announcement record);

    Announcement selectByPrimaryKeyAndStatus(@Param("id") Long id, @Param("status") Integer status);

    List<AnnouncementListVO> selectAllByStatus(@Param("status") Integer status);

    int updateByPrimaryKey(Announcement record);

    int updateAnnouncementStatusById(@Param("status") Integer status, @Param("id") Long id);
}