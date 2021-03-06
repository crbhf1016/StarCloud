package cn.com.bonc.sce.controller;

import cn.com.bonc.sce.annotation.CurrentUserId;
import cn.com.bonc.sce.constants.WebMessageConstants;
import cn.com.bonc.sce.model.AppAddModel;
import cn.com.bonc.sce.model.AppTypeMode;
import cn.com.bonc.sce.model.PlatformAddModel;
import cn.com.bonc.sce.rest.RestRecord;
import cn.com.bonc.sce.service.AppManageService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 应用管理
 * author jc_D
 */
@Slf4j
@Api( value = "应用管理接口", tags = "应用管理接口" )
@ApiResponses( { @ApiResponse( code = 500, message = "服务器内部错误", response = RestRecord.class ) } )
@RestController
@RequestMapping( "/manage-app" )
public class AppManageController {

    private AppManageService appManageService;

    @Autowired
    public AppManageController( AppManageService appManageService ) {
        this.appManageService = appManageService;
    }

    /**
     * 新增软件应用
     *
     * @param appInfo 应用信息， value为json格式
     * @return
     */
    @ApiOperation( value = "新增软件应用", notes = "新增软件应用", httpMethod = "POST" )
    @ApiImplicitParams( {
            @ApiImplicitParam( name = "authentication", value = "用户信息", paramType = "header" )
    } )
    @ApiResponses( {
            @ApiResponse( code = 200, message = WebMessageConstants.SCE_PORTAL_MSG_200, response = RestRecord.class ),
            @ApiResponse( code = 423, message = WebMessageConstants.SCE_PORTAL_MSG_423, response = RestRecord.class )
    } )
    @PostMapping
    public RestRecord addAppInfo( @Valid @RequestBody @ApiParam( "新增软件对象" ) AppAddModel appInfo,
                                  BindingResult results,
                                  @CurrentUserId @ApiParam( hidden = true ) String userId ) {
        log.trace( "appinfo::{}", appInfo );
        if ( results.hasErrors() ) {
            return new RestRecord( 423, results.getFieldError().getDefaultMessage() );
        }
        Set< AppTypeMode > pc = appInfo.getPc();
        for ( AppTypeMode appTypeMode : pc ) {
            if ( StringUtils.isEmpty( appTypeMode.getAddress() ) ) {
                return new RestRecord( 423, "请上传软件" );
            }
            if ( StringUtils.isEmpty( appTypeMode.getAppVersion() ) ) {
                return new RestRecord( 423, "版本号不能为空" );
            }

            if ( StringUtils.isEmpty( appTypeMode.getRunningPlatform() ) ) {
                return new RestRecord( 423, "运行平台不能为空" );
            }

            if ( StringUtils.isEmpty( appTypeMode.getVersionSize() ) ) {
                return new RestRecord( 423, "软件大小不能为空" );
            }

            if ( StringUtils.isEmpty( appTypeMode.getPackageName() ) ) {
                return new RestRecord( 423, "包名不能为空" );
            }

        }
        return appManageService.addAppInfo( appInfo, userId );
    }

    /**
     * 新增平台应用
     *
     * @param platFormInfo 平台应用信息， value为json格式
     * @return
     */
    @ApiOperation( value = "新增平台应用", notes = "新增平台应用", httpMethod = "POST" )
    @ApiImplicitParams( {
            @ApiImplicitParam( name = "authentication", value = "用户信息", paramType = "header" )
    } )
    @ApiResponses( {
            @ApiResponse( code = 200, message = WebMessageConstants.SCE_PORTAL_MSG_200, response = RestRecord.class ),
            @ApiResponse( code = 423, message = WebMessageConstants.SCE_PORTAL_MSG_423, response = RestRecord.class )
    } )
    @PostMapping( "/pt" )
    public RestRecord addPlatFormInfo( @Valid @RequestBody @ApiParam( "新增平台对象" ) PlatformAddModel platFormInfo,
                                       BindingResult results,
                                       @CurrentUserId @ApiParam( hidden = true ) String userId ) {

        log.trace( "platFormInfo::{}", platFormInfo );
        if ( results.hasErrors() ) {
            return new RestRecord( 423, results.getFieldError().getDefaultMessage() );
        }
        return appManageService.addPlatFormInfo( platFormInfo, userId );
    }

    /**
     * 删除应用
     *
     * @param appIdList appId数组
     * @return
     */
    @ApiOperation( value = "删除应用", notes = "删除应用", httpMethod = "DELETE" )

    @ApiImplicitParams( {
            @ApiImplicitParam( name = "appIdList", value = "appId,json数组", paramType = "body", required = true ),
            @ApiImplicitParam( name = "authentication", value = "用户信息", paramType = "header" )
    } )
    @ApiResponses( {
            @ApiResponse( code = 200, message = "成功", response = RestRecord.class )
    } )
    @DeleteMapping
    public RestRecord deleteApps( @RequestBody List< String > appIdList,
                                  @CurrentUserId @ApiParam( hidden = true ) String userId ) {
        return appManageService.deleteApps( appIdList, userId );
    }

    /**
     * 编辑应用
     *
     * @param updateInfo 应用需更新的字段与更新的值，json字符串
     * @param appId      所需更新的应用ID
     * @return
     */
    @ApiOperation( value = "编辑应用", notes = "编辑应用", httpMethod = "PUT" )
    @ApiImplicitParams( {
            @ApiImplicitParam( name = "appId", value = "appid", paramType = "query", required = true ),
            @ApiImplicitParam( name = "updateInfo", value = "app修改的信息，json格式", paramType = "body", required = true )

    } )
    @ApiResponses( {
            @ApiResponse( code = 0, message = "成功", response = RestRecord.class )
    } )
    @PutMapping( "/{appId}" )
    public RestRecord updateAppInfo( @RequestBody Map updateInfo,
                                     @PathVariable String appId ) {
        return appManageService.updateAppInfo( updateInfo, appId );
    }

    /**
     * 查询全部应用
     *
     * @param platformType 平台类型（平台应用或软件应用）
     * @return
     */
    @ApiOperation( value = "查询全部应用", notes = "查询全部应用", httpMethod = "GET" )
    @ApiResponses( {
            @ApiResponse( code = 0, message = "成功", response = RestRecord.class )
    } )
    @GetMapping( "/all-app" )
    public RestRecord getAllAppList( @RequestParam( value = "platformType", required = false, defaultValue = "0" ) @ApiParam( name = "platformType", value = "应用类型(平台应用pt|软件应用rj)", required = false ) String platformType,
                                     @RequestParam( value = "pageNum", required = false, defaultValue = "1" ) @ApiParam( name = "pageNum", value = "页数", required = false ) Integer pageNum,
                                     @RequestParam( value = "pageSize", required = false, defaultValue = "10" ) @ApiParam( name = "pageSize", value = "页数大小", required = false ) Integer pageSize
    ) {
        return appManageService.getAllAppList( platformType, pageNum, pageSize );
    }


    /**
     * 查询指定类型id应用信息
     *
     * @param appType      查询的应用类型
     * @param orderType    查询排序字段，可为多个字段
     * @param platformType 平台类型（平台应用或软件应用）
     * @return
     */
    @ApiOperation( value = "查询指定类型应用信息", notes = "查询指定类型应用信息", httpMethod = "GET" )
    @ApiImplicitParams( {
            @ApiImplicitParam( name = "appType", value = "查询的应用类型id（0查全部）", paramType = "query", required = false ),
            @ApiImplicitParam( name = "orderType", value = "排序字段（download|time）", paramType = "query", required = false ),
            @ApiImplicitParam( name = "sort", value = "升降序(asc|desc)", paramType = "query", required = false ),
            @ApiImplicitParam( name = "platformType", value = "应用类型(平台应用pt|软件应用rj)", paramType = "query", required = false )

    } )
    @ApiResponses( {
            @ApiResponse( code = 0, message = "成功", response = RestRecord.class )
    } )
    @GetMapping( "/app-by-type" )
    public RestRecord selectAppListByType( @RequestParam( value = "appType", required = false, defaultValue = "0" ) Integer appType,
                                           @RequestParam( value = "orderType", required = false, defaultValue = "download" ) String orderType,
                                           @RequestParam( value = "platformType", required = false, defaultValue = "0" ) String platformType,
                                           @RequestParam( value = "sort", required = false, defaultValue = "desc" ) String sort,
                                           @RequestParam( value = "pageNum", required = false, defaultValue = "1" ) Integer pageNum,
                                           @RequestParam( value = "pageSize", required = false, defaultValue = "10" ) Integer pageSize ) {
        return appManageService.selectAppListByType( appType, orderType, platformType, sort, pageNum, pageSize );
    }

    /**
     * 根据输入名模糊查询应用
     *
     * @param appName      查询的名字
     * @param platformType 平台类型（平台应用或软件应用）
     * @return
     */
    @ApiOperation( value = "根据输入名模糊查询应用", notes = "根据输入名模糊查询应用", httpMethod = "GET" )
    @ApiImplicitParams( {
            @ApiImplicitParam( name = "appName", value = "appName", paramType = "query", required = false ),
            @ApiImplicitParam( name = "platformType", value = "应用类型（pt|rj)", paramType = "query", required = false )
    } )
    @ApiResponses( {
            @ApiResponse( code = 200, message = "成功", response = RestRecord.class )
    } )
    @GetMapping( "/apps-by-name" )
    public RestRecord selectAppListByName( @RequestParam( value = "appName", required = false ) String appName,
                                           @RequestParam( value = "platformType", required = false, defaultValue = "0" ) String platformType,
                                           @RequestParam( value = "pageNum", required = false, defaultValue = "1" ) Integer pageNum,
                                           @RequestParam( value = "pageSize", required = false, defaultValue = "10" ) Integer pageSize ) {
        return appManageService.selectAppListByName( appName, platformType, pageNum, pageSize );
    }

    /**
     * 根据输入名和选择类别查询应用
     *
     * @param appName
     * @param appType
     * @param orderType
     * @param sort
     * @param platformType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation( value = "根据输入名和选择类别查询应用", notes = "根据输入名和选择类别查询应用", httpMethod = "GET" )
    @ApiImplicitParams( {
            @ApiImplicitParam( name = "appName", value = "appName", paramType = "query", required = false ),
            @ApiImplicitParam( name = "appType", value = "应用分类id(传0查全部)", paramType = "query", required = false ),
            @ApiImplicitParam( name = "orderType", value = "排序字段（download|time）", paramType = "query", required = true ),
            @ApiImplicitParam( name = "sort", value = "升降序(asc|desc)", paramType = "query", required = false ),
            @ApiImplicitParam( name = "platformType", value = "平台类型(pt|rj)", paramType = "query", required = false )

    } )
    @ApiResponses( {
            @ApiResponse( code = 200, message = "成功", response = RestRecord.class )
    } )
    @GetMapping( "/apps-by-name-type" )
    public RestRecord selectAppListByNameAndType( @RequestParam( value = "appName", required = false ) String appName,
                                                  @RequestParam( value = "appType", required = false, defaultValue = "0" ) Integer appType,
                                                  @RequestParam String orderType,
                                                  @RequestParam( value = "sort", required = false, defaultValue = "desc" ) String sort,
                                                  @RequestParam( value = "platformType", required = false, defaultValue = "0" ) String platformType,
                                                  @RequestParam( value = "pageNum", required = false, defaultValue = "1" ) Integer pageNum,
                                                  @RequestParam( value = "pageSize", required = false, defaultValue = "10" ) Integer pageSize ) {


        return appManageService.selectAppListByNameAndType( appName, appType, orderType, sort, platformType, pageNum, pageSize );
    }

    /**
     * 前台全部应用页面展示
     *
     * @param appName
     * @param appType
     * @param orderType
     * @param sort
     * @param platformType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation( value = "前台全部应用页面展示", notes = "前台全部应用页面展示", httpMethod = "GET" )
    @ApiImplicitParams( {
            @ApiImplicitParam( name = "appName", value = "appName", paramType = "query", required = false ),
            @ApiImplicitParam( name = "appType", value = "应用分类id(传0查全部rj)", paramType = "query", required = false ),
            @ApiImplicitParam( name = "orderType", value = "排序字段（download|time）", paramType = "query", required = false ),
            @ApiImplicitParam( name = "sort", value = "升降序(asc|desc)", paramType = "query", required = false ),
            @ApiImplicitParam( name = "platformType", value = "平台类型(pt|rj)", paramType = "query", required = false ),
            @ApiImplicitParam( name = "authentication", value = "用户信息", paramType = "header" )

    } )
    @ApiResponses( {
            @ApiResponse( code = 200, message = "成功", response = RestRecord.class )
    } )
    @GetMapping( "/condition" )
    public RestRecord getAppListInfoByCondition( @RequestParam( value = "appName", required = false ) String appName,
                                                 @RequestParam( value = "appType", required = false, defaultValue = "0" ) Integer appType,
                                                 @RequestParam( value = "orderType", required = false, defaultValue = "download" ) String orderType,
                                                 @RequestParam( value = "sort", required = false, defaultValue = "desc" ) String sort,
                                                 @RequestParam( value = "platformType", required = false, defaultValue = "0" ) String platformType,
                                                 @RequestParam( value = "pageNum", required = false, defaultValue = "1" ) Integer pageNum,
                                                 @RequestParam( value = "pageSize", required = false, defaultValue = "10" ) Integer pageSize,
                                                 @CurrentUserId @ApiParam( hidden = true ) String userId ) {

        return appManageService.getAppListInfoByCondition( appName, appType, orderType, sort, platformType, pageNum, pageSize, userId );
    }

    /**
     * 查询单个应用详情
     *
     * @param appId
     * @return
     */
    @ApiOperation( value = "查询单个应用详情", notes = "查询单个应用详情", httpMethod = "GET" )
    @ApiImplicitParams( {
            @ApiImplicitParam( name = "appId", value = "appId", paramType = "path", required = true )

    } )
    @ApiResponses( {
            @ApiResponse( code = 200, message = "成功", response = RestRecord.class )
    } )
    @GetMapping( "/detail-by-id/{appId}" )
    public RestRecord selectAppById( @PathVariable String appId ) {
        return appManageService.selectAppById( appId );
    }

    /**
     * DESC: 检测用户是否开通了该APP
     *
     * @param appId 应用ID
     * @return Map
     * @Auther mkl
     */
    @ApiOperation( value = "查询用户是否开通了该应用", notes = "根据应用ID查询用户是否开通了该应用", httpMethod = "GET" )
    @GetMapping( "/detail/open/{appId}" )
    public RestRecord isOpenApp( @PathVariable @ApiParam( name = "appId", value = "appId", required = true ) String appId, @ApiParam( name = "userId", value = "用户ID", required = true ) @CurrentUserId String userId ) {
        return appManageService.isOpenApp( appId, userId );
    }

    /**
     * 查询[应用审核状态码表]中相关信息
     *
     * @return
     */
    //@ApiOperation( value = "查询[应用审核状态码表]中相关信息", notes = "查询[应用审核状态码表]中相关信息", httpMethod = "GET" )
    @GetMapping( "all-audit-status" )
    public RestRecord getAllAuditStatus() {
        return appManageService.getAllAuditStatus();
    }

    /**
     * 通过审核状态查询app列表
     *
     * @param auditStatus
     * @param typeId
     * @param keyword
     * @return
     */
    @ApiOperation( value = "通过审核状态查询app列表", notes = "1，审核中2，迭代审核3，未通过审核，4已上架（运营中）， 5应用下架", httpMethod = "GET" )
    @ApiImplicitParams( {
            @ApiImplicitParam( name = "authentication", value = "用户信息", paramType = "header" )
    } )
    @ApiResponses( {
            @ApiResponse( code = 200, message = "成功", response = RestRecord.class )
    } )
    @GetMapping( "/list-by-audit-status" )
    public RestRecord getAppListByAuditStatus( @RequestParam( "auditStatus" ) @ApiParam( "1，审核中2，迭代审核3，未通过审核，4已上架（运营中）， 5应用下架" ) String auditStatus,
                                               @RequestParam( value = "typeId", required = false, defaultValue = "0" ) @ApiParam( "app分类id" ) Integer typeId,
                                               @RequestParam( value = "keyword", required = false ) @ApiParam( "搜索关键词" ) String keyword,
                                               @RequestParam( value = "downloadCount", required = false, defaultValue = "desc" ) @ApiParam( "下载量排序（asc|desc）" ) String downloadCount,
                                               @RequestParam( value = "platformType", required = false, defaultValue = "rj" ) @ApiParam( "平台类型 rj||pt，默认查询软件的列表" )String platformType,
                                               @RequestParam( value = "pageNum", required = false, defaultValue = "1" ) Integer pageNum,
                                               @RequestParam( value = "pageSize", required = false, defaultValue = "10" ) Integer pageSize,
                                               @CurrentUserId String userId
    ) {
        return appManageService.getAppListByAuditStatus( auditStatus, typeId, keyword, downloadCount, pageNum, pageSize, userId ,platformType);
    }


    /**
     * app统计总量
     *
     * @return
     */
    @ApiOperation( value = "应用统计总量", tags = "应用统总量", notes = "应用统计总量", httpMethod = "GET" )
    @ApiResponses( {
            @ApiResponse( code = 200, message = "成功", response = RestRecord.class )
    } )
    @GetMapping( "/count-app" )
    public RestRecord getAppCountInfo() {
        return appManageService.getAppCountInfo();
    }


    /**
     * app分类信息统计
     *
     * @return
     */
    @ApiOperation( value = "应用统计详情", tags = "应用统计详情", notes = "应用统计详情", httpMethod = "GET" )
    @ApiResponses( {
            @ApiResponse( code = 200, message = "成功", response = RestRecord.class )
    } )
    @GetMapping( "/app-info" )
    public RestRecord getAppInfo() {
        return appManageService.getAppInfo();
    }

}
