package group.yrmjhtdjxh.punch.service;


import group.yrmjhtdjxh.punch.VO.Result;
import group.yrmjhtdjxh.punch.form.AnnouncementPublishForm;
import group.yrmjhtdjxh.punch.form.AnnouncementUpdateForm;

import javax.servlet.http.HttpSession;


public interface AnnouncementService {
    

    /**
     * 发布公告
     * @param publishForm
     * @return
     */
    Result publishAnnouncement(AnnouncementPublishForm publishForm, HttpSession session);

    /**
     * 查看公告详情
     * @param announcementId
     * @return
     */
    Result readAnnouncementDetail(Long announcementId,HttpSession session);


    /**
     * 获取公告指定分页数据
     * @param
     * @return
     */
    Result getList(HttpSession session);

    Result changeInfo(AnnouncementUpdateForm updateForm, HttpSession session);

    Result createAndSave(AnnouncementPublishForm publishForm,HttpSession session);

    Result publishSavedAnnouncement(Long announcementId,HttpSession session);

    Result cancelPublish(Long announcementId,HttpSession session);

    Result delete(Long announcementId, HttpSession session);
}
