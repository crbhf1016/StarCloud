package cn.com.bonc.sce.api;

import cn.com.bonc.sce.constants.MessageConstants;
import cn.com.bonc.sce.entity.Notification;
import cn.com.bonc.sce.rest.RestRecord;
import cn.com.bonc.sce.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通知增删改
 *
 * @author wzm
 * @version 0.1
 * @since 2018/12/21 9:00
 */
@Slf4j
@RestController
@RequestMapping( "/notifications" )
public class NotificationApiController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 新增通知公告
     *
     * @param notification 通知公告
     * @return 添加通知公告是否成功
     */
    @PostMapping
    @ResponseBody
    public RestRecord insertNotification( @RequestBody Notification notification ) {
        try {
            return notificationService.insertNotification( notification );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return new RestRecord( 409, MessageConstants.SCE_MSG_409, e );
        }
    }

    /**
     * 删除通知公告
     *
     * @param list 通知公告Id列表
     * @return 删除通知公告是否成功
     */
    @DeleteMapping( "/{list}" )
    @ResponseBody
    public RestRecord deleteNotificationByIdList( @PathVariable( "list" ) String list ) {
        try {
            return notificationService.deleteNotificationByIdList( list );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return new RestRecord( 408, MessageConstants.SCE_MSG_408, e );
        }
    }

    /**
     * 更改通知公告
     *
     * @param notification 通知信息
     * @return 更新通知公告是否成功
     */
    @PutMapping
    @ResponseBody
    public RestRecord updateNotification( @RequestBody Notification notification ) {
        try {
            return notificationService.updateNotification( notification );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return new RestRecord( 407, MessageConstants.SCE_MSG_407, e );
        }
    }

    /**
     * 查询通知公告列表
     *
     * @param auditStatus 通知公告审核状态
     * @param content     内容
     * @param startDate   查询起始日期
     * @param endDate     查询结束日期
     * @param pageNum     分页页码
     * @param pageSize    分页每页条数
     * @param type        通知公告类型
     * @param province    省
     * @param city        市
     * @param district    区
     * @return 分页后的通知公告列表
     */
    @GetMapping( "/list/{auditStatus}" )
    @ResponseBody
    public RestRecord getNotificationList( @RequestParam( value = "type", required = false ) Integer type,
                                           @RequestParam( value = "content", required = false ) String content,
                                           @PathVariable( "auditStatus" ) String auditStatus,
                                           @RequestParam( value = "startDate", required = false ) String startDate,
                                           @RequestParam( value = "endDate", required = false ) String endDate,
                                           @RequestParam( value = "province", required = false ) String province,
                                           @RequestParam( value = "city", required = false ) String city,
                                           @RequestParam( value = "district", required = false ) String district,
                                           @RequestParam( value = "pageNum", required = false, defaultValue = "0" ) Integer pageNum,
                                           @RequestParam( value = "pageSize", required = false, defaultValue = "10" ) Integer pageSize ) {
        try {
            RestRecord rr;
            if ( StringUtils.isEmpty( province ) ) {
                rr = notificationService.getNotificationList( type, content, auditStatus, startDate, endDate, pageNum, pageSize );
            } else {
                //有地址筛选
                rr = notificationService.getNotificationList( type, content, auditStatus, startDate, endDate, province, city, district, pageNum, pageSize );
            }
            List< Notification > list = ( List< Notification > ) ( ( Map< String, Object > ) rr.getData() ).get( "info" );
            for ( Notification notification : list ) {
                if ( notification.getUser() == null ) {
                    continue;
                }
                notification.setUserName( notification.getUser().getUserName() );
                notification.setUser( null );
                if ( notification.getFile() == null ) {
                    continue;
                }
                notification.setFileUrl( notification.getFile().getFileStorePath() );
                notification.setFile( null );
            }

            return rr;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return new RestRecord( 406, MessageConstants.SCE_MSG_406, e );
        }
    }

    /**
     * 查询通知公告详情
     *
     * @param notificationId 通知公告id
     * @return 分页后的通知公告列表
     */
    @GetMapping( "/{notificationId}" )
    @ResponseBody
    public RestRecord getNotification( @PathVariable( "notificationId" ) Integer notificationId ) {
        try {
            Notification notification = notificationService.getNotification( notificationId );
            if ( notification.getUser() != null ) {
                notification.setUserName( notification.getUser().getUserName() );
                notification.setUser( null );
            }
            return new RestRecord( 200, notification );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return new RestRecord( 406, MessageConstants.SCE_MSG_406, e );
        }
    }
}
