package group.yrmjhtdjxh.punch.service.Impl;


import group.yrmjhtdjxh.punch.VO.AnnouncementListVO;
import group.yrmjhtdjxh.punch.VO.AnnouncementVO;
import group.yrmjhtdjxh.punch.VO.Result;
import group.yrmjhtdjxh.punch.VO.StudentVO;
import group.yrmjhtdjxh.punch.domain.Announcement;
import group.yrmjhtdjxh.punch.enums.AnnouncementStatus;
import group.yrmjhtdjxh.punch.enums.UserRole;
import group.yrmjhtdjxh.punch.form.AnnouncementPublishForm;
import group.yrmjhtdjxh.punch.form.AnnouncementUpdateForm;
import group.yrmjhtdjxh.punch.mapper.AnnouncementMapper;
import group.yrmjhtdjxh.punch.redis.AnnouncementKey;
import group.yrmjhtdjxh.punch.redis.RedisService;
import group.yrmjhtdjxh.punch.service.AnnouncementService;
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
    public Result delete(Long id, HttpSession session) {
        if (!userAuthentication(session, UserRole.ADMINISTRATOR)){
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

    private Announcement selectByIdAndStatus(Long announcementId) {
        Announcement announcement = redisService.get(AnnouncementKey.getById, announcementId + "", Announcement.class);
        if (announcement == null){
            announcement = announcementMapper.selectByPrimaryKeyAndStatus(announcementId, null);
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
        if (!userAuthentication(session,UserRole.AVERAGE_USER)){
            return Result.error(403,"权限不足");
        }
        Announcement announcement = selectByIdAndStatus(announcementId);
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
        Announcement announcement = selectByIdAndStatus(updateForm.getAnnouncementId());
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
