package cn.com.bonc.sce.api;

import cn.com.bonc.sce.constants.WebMessageConstants;
import cn.com.bonc.sce.entity.AppInfoEntity;
import cn.com.bonc.sce.repository.TopAppRepository;
import cn.com.bonc.sce.rest.RestRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用推荐-重点推荐应用接口
 * @author jc_D
 */
@Slf4j
@RestController
@RequestMapping( "/top-app" )
public class TopAppController {
    @Autowired
    private TopAppRepository topAppRepository;

    /**
     * 添加重点推荐应用
     *
     * @param appIdList app的id用","分割
     * @param userId
     * @return
     */
    @PostMapping("/{userId}")
    public RestRecord addTopRecommendAppList( @RequestBody List< String > appIdList,
                                              @PathVariable( "userId" ) String userId ) {
        List< String > appIdList1 = topAppRepository.getAllTopAppId();
        topAppRepository.updateTopApp( appIdList1, 0L, userId );
        Integer i = topAppRepository.updateTopApp( appIdList, 1L, userId );
        return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_200 );
    }


    /**
     * 查询所有重点推荐应用
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return
     */
    @GetMapping( "/{pageNum}/{pageSize}" )
    public RestRecord selectTopRecommendAppList( @PathVariable Integer pageNum,
                                                 @PathVariable Integer pageSize ) {
        Pageable pageable = PageRequest.of( pageNum-1, pageSize );
        Page< AppInfoEntity > appInfoList = topAppRepository.findByIsTopRecommend( 1L, pageable );
        // 查询应用表中热门推荐状态为1的应用
        return new RestRecord( 200, appInfoList );
    }

    /**
     * 添加重点推荐应用
     *
     * @param appId
     * @param userId
     * @return
     */
    @PostMapping( "/new-one" )
    public RestRecord addTopRecommendAppList( @RequestParam( "appId" ) String appId,
                                              @RequestParam( "userId" ) String userId ) {
        Integer i = topAppRepository.updateTopAppById( appId, 1L, userId );
        if ( i >= 0 ) {
            return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_200 );
        } else {
            return new RestRecord( 500,WebMessageConstants.SCE_PORTAL_MSG_421 );
        }

    }

    /**
     * 取消重点推荐应用
     *
     * @param appId
     * @param userId
     * @return
     */
    @PostMapping( "/sub-one" )
    public RestRecord cancelHotRecommendAppList( @RequestParam( "appId" ) String appId,
                                                 @RequestParam( "userId" ) String userId ) {
        Integer i = topAppRepository.updateTopAppById( appId, 0L, userId );
        if ( i >= 0 ) {
            return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_200 );
        } else {
            return new RestRecord( 500,WebMessageConstants.SCE_PORTAL_MSG_421 );
        }

    }

    /**
     * 查询所有重点推荐应用列表
     *
     * @return
     */
    @GetMapping( "/detail/{pageNum}/{pageSize}/{userId}" )
    public RestRecord selectTopAppList( @PathVariable( "pageNum" ) Integer pageNum,
                                        @PathVariable( "pageSize" ) Integer pageSize,
                                        @PathVariable( "userId" ) String userId) {
        Pageable pageable = PageRequest.of( pageNum-1, pageSize );
        return new RestRecord( 200, topAppRepository.selectTopAppList( userId, pageable ) );
    }

}
