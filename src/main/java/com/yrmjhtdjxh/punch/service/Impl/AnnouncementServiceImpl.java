package com.yrmjhtdjxh.punch.service.Impl;


import com.yrmjhtdjxh.punch.VO.AnnouncementListVO;
import com.yrmjhtdjxh.punch.VO.AnnouncementVO;
import com.yrmjhtdjxh.punch.VO.Result;
import com.yrmjhtdjxh.punch.VO.StudentVO;
import com.yrmjhtdjxh.punch.domain.Announcement;
import com.yrmjhtdjxh.punch.enums.AnnouncementStatus;
import com.yrmjhtdjxh.punch.enums.UserRole;
import com.yrmjhtdjxh.punch.form.AnnouncementPublishForm;
import com.yrmjhtdjxh.punch.form.AnnouncementUpdateForm;
import com.yrmjhtdjxh.punch.mapper.AnnouncementMapper;
import com.yrmjhtdjxh.punch.redis.AnnouncementKey;
import com.yrmjhtdjxh.punch.redis.RedisService;
import com.yrmjhtdjxh.punch.service.AnnouncementService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author dengg
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private RedisService redisService;

    private boolean insertAndPublish(Announcement announcement) {
        if (announcementMapper.insert(announcement) == 1){
            redisService.set(AnnouncementKey.getById, announcement.getId() + "", announcement);
            return true;
        }
        return false;
    }

    public boolean update(Announcement announcement) {
        announcement.setUpdateTime(new Date());
        if (announcementMapper.updateByPrimaryKey(announcement) == 1){
            if (announcement.getStatus().equals(AnnouncementStatus.PUBLISHED.getValue())){
                redisService.set(AnnouncementKey.getById, announcement.getId() + "", announcement);
            }
            return true;
        }
        return false;
    }

    @Override
    public Result delete(Long id,HttpSession session) {
        if (!userAuthentication(session,UserRole.ADMINISTRATOR)){
            return Result.error(403,"权限不足");
        }
        if (announcementMapper.selectByPrimaryKeyAndStatus(id,null) == null){
            throw new IllegalArgumentException("公告不存在");
        }
        redisService.delete(AnnouncementKey.getById, id + "");
        redisService.delete(AnnouncementKey.getClickTimesById, id + "");
        announcementMapper.deleteByPrimaryKey(id);
        return Result.success();
    }

    private Announcement selectByIdAndStatus(Long announcementId, Integer status) {
        Announcement announcement = redisService.get(AnnouncementKey.getById, announcementId + "", Announcement.class);
        if (announcement == null){
            announcement = announcementMapper.selectByPrimaryKeyAndStatus(announcementId, status);
            if (announcement != null && announcement.getStatus().equals(AnnouncementStatus.PUBLISHED.getValue())){
                redisService.set(AnnouncementKey.getById, announcementId + "", announcement);
            }
        }
        return announcement;
    }

    @Override
    public Result publishAnnouncement(AnnouncementPublishForm publishForm,HttpSession session) {
        if (!userAuthentication(session,UserRole.ADMINISTRATOR)){
            return Result.error(403,"权限不足");
        }
        Announcement announcement = convertFormToModel(publishForm,AnnouncementStatus.PUBLISHED);
        if (insertAndPublish(announcement)){
            //设置阅读次数
            redisService.set(AnnouncementKey.getClickTimesById, announcement.getId() + "", 0);
            return Result.success();
        }
        return Result.error(500,"添加错误");
    }

    @Override
    public Result readAnnouncementDetail(Long announcementId,HttpSession session) {

        // 判断普通用户能否查看
        if (!userAuthentication(session,UserRole.AVERAGE_USER)){
            return Result.error(403,"权限不足");
        }

        // 根据ID得到已经发布的公告 或者没有发布的公告
        Announcement announcement = selectByIdAndStatus(announcementId, AnnouncementStatus.PUBLISHED.getValue());

        // 如果是管理员，已经发布的没有找到，就去找保存但是还没有发布的
        if(announcement == null &&  userAuthentication(session, UserRole.ADMINISTRATOR)) {
            announcement = selectByIdAndStatus(announcementId, AnnouncementStatus.SAVE.getValue());
        }

        if (announcement != null){
            AnnouncementVO announcementVO = new AnnouncementVO();
            BeanUtils.copyProperties(announcement, announcementVO);
            if (announcement.getStatus().equals(AnnouncementStatus.PUBLISHED.getValue())) {
                redisService.incr(AnnouncementKey.getClickTimesById, announcementId + "");
                Integer clickTimes = redisService.get(AnnouncementKey.getClickTimesById, announcementId + "", Integer.class);
                announcementVO.setClickTimes(clickTimes);
            }
            return Result.success(announcementVO);
        }


        return Result.error(500,"公告不存在");
    }

    @Override
    public Result getList(HttpSession session) {
        if (!userAuthentication(session,UserRole.AVERAGE_USER)){
            return Result.error(403,"权限不足");
        }
        //使用无条件查询
        List<AnnouncementListVO> listVOS = announcementMapper.selectAllByStatus(null);
        return Result.success(listVOS);
    }

    @Override
    public Result changeInfo(AnnouncementUpdateForm updateForm,HttpSession session) {
        if (!userAuthentication(session,UserRole.ADMINISTRATOR)){
            return Result.error(403,"权限不足");
        }
        Announcement announcement = selectByIdAndStatus(updateForm.getAnnouncementId(),null);
        if (announcement == null){
            return Result.error(500,"公告不存在");
        }
        announcement.setTitle(updateForm.getTitle());
        announcement.setContent(updateForm.getContent());
        if (update(announcement)) {
            return Result.success();
        }
        return Result.error(500,"更新失败");
    }

    @Override
    public Result createAndSave(AnnouncementPublishForm publishForm,HttpSession session) {
        if (!userAuthentication(session,UserRole.ADMINISTRATOR)){
            return Result.error(403,"权限不足");
        }
        Announcement announcement = convertFormToModel(publishForm,AnnouncementStatus.SAVE);
        announcementMapper.insert(announcement);
        return Result.success();
    }

    @Override
    public Result publishSavedAnnouncement(Long announcementId,HttpSession session) {
        if (!userAuthentication(session,UserRole.ADMINISTRATOR)){
            return Result.error(403,"权限不足");
        }
        Announcement announcement = announcementMapper.selectByPrimaryKeyAndStatus(announcementId,AnnouncementStatus.SAVE.getValue());
        if (announcement == null){
            return Result.error(500,"公告不存在");
        }
        //先更新缓存状态
        announcement.setStatus(AnnouncementStatus.PUBLISHED.getValue());
        redisService.set(AnnouncementKey.getById, announcement.getId() + "", announcement);
        //设置阅读次数
        redisService.set(AnnouncementKey.getClickTimesById, announcement.getId() + "", 0);
        announcementMapper.updateAnnouncementStatusById(AnnouncementStatus.PUBLISHED.getValue(),announcementId);
        return Result.success();
    }

    @Override
    public Result cancelPublish(Long announcementId,HttpSession session) {
        if (!userAuthentication(session,UserRole.ADMINISTRATOR)){
            return Result.error(403,"权限不足");
        }
        redisService.delete(AnnouncementKey.getById, announcementId + "");
        announcementMapper.updateAnnouncementStatusById(AnnouncementStatus.SAVE.getValue(),announcementId);
        return Result.success();
    }


    private Announcement convertFormToModel(AnnouncementPublishForm publishForm,AnnouncementStatus status){
        //可设置发布者
        Announcement announcement = new Announcement();
        announcement.setTitle(publishForm.getTitle());
        announcement.setContent(publishForm.getContent());
        announcement.setPublishTime(new Date());
        announcement.setUpdateTime(new Date());
        announcement.setStatus(status.getValue());
        return announcement;
    }

    private boolean userAuthentication(HttpSession httpSession, UserRole userRole) {
        StudentVO studentVO = (StudentVO) httpSession.getAttribute("student");
        if (studentVO == null){
            return false;
        }else {
            // userRole 越小权限越大
            return userRole == null || userRole.getValue() >= (studentVO.getUserRole());
        }
    }
}
