package group.yrmjhtdjxh.punch.web;


import group.yrmjhtdjxh.punch.VO.Result;
import group.yrmjhtdjxh.punch.form.AnnouncementPublishForm;
import group.yrmjhtdjxh.punch.form.AnnouncementUpdateForm;
import group.yrmjhtdjxh.punch.service.AnnouncementService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 公告接口
 */
@RestController
@RequestMapping("/announcement")
@Api(tags = "公告模块")
public class AnnouncementController {

    private AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    /**
     * 发布公告
     * @param publishForm
     * @return
     */
    @PostMapping(value = "/publish", name = "发布公告")
    public Result publish(@RequestBody @Valid AnnouncementPublishForm publishForm, HttpSession session){
        return announcementService.publishAnnouncement(publishForm,session);
    }

    /**
     * 阅读公告详情
     * @param announcementId
     * @return
     */
    @GetMapping(value = "/readDetail", name = "阅读公告详情")
    public Result readDetail(Long announcementId,HttpSession session){
        if (announcementId == null){
            return Result.error(500,"参数不能为空");
        }
        return announcementService.readAnnouncementDetail(announcementId,session);
    }

    /**
     * 公告列表
     * @return
     */
    @GetMapping(value = "/list", name = "公告列表")
    public Result list(HttpSession session){
        return announcementService.getList(session);
    }


    /**
     *  删除公告
     * @param announcementId
     * @return
     */
    @GetMapping(value = "/delete", name = "删除公告")
    public Result delete(Long announcementId,HttpSession session){
        return announcementService.delete(announcementId,session);
    }

    /**
     * 修改公告
     * @param updateForm
     * @return
     */
    @PostMapping(value = "/update", name = "修改公告")
    public Result update(@Valid @RequestBody AnnouncementUpdateForm updateForm,HttpSession session){
        return announcementService.changeInfo(updateForm,session);
    }

    /**
     * 创建并保存公告
     * @param publishForm
     * @return
     */
    @PostMapping("/createAndSave")
    public Result createAndSave(@RequestBody @Valid AnnouncementPublishForm publishForm,HttpSession session){
        return announcementService.createAndSave(publishForm,session);
    }


    /**
     * 发布未发布的公告
     * @param announcementId
     * @return
     */
    @GetMapping("/publishSavedAnnouncement")
    public Result publishSavedAnnouncement(Long announcementId,HttpSession session){
        if (announcementId == null){
            return Result.error(500,"参数不为空");
        }
        return announcementService.publishSavedAnnouncement(announcementId,session);
    }

    /**
     * 取消发布公告
     * @param announcementId
     * @return
     */
    @GetMapping("/cancelPublish")
    public Result cancelPublish(Long announcementId,HttpSession session){
        if (announcementId == null){
            return Result.error(500,"参数不为空");
        }
        return announcementService.cancelPublish(announcementId,session);
    }

}
