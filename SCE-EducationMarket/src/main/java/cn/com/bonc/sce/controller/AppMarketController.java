package cn.com.bonc.sce.controller;

import cn.com.bonc.sce.constants.WebMessageConstants;
import cn.com.bonc.sce.rest.RestRecord;
import cn.com.bonc.sce.service.AppMarketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app商城
 * author wf
 */
@Slf4j
@Api( value = "应用商城接口", tags = "应用商城接口" )
@ApiResponses( { @ApiResponse( code = 500, message = "服务器内部错误", response = RestRecord.class ) } )
@RestController
@RequestMapping("/app-market")
public class AppMarketController {

    private AppMarketService appMarketService;

    @Autowired
    public AppMarketController(AppMarketService appMarketService){
        this.appMarketService = appMarketService;
    }

    @GetMapping("/appCount")
    public RestRecord appCount(){
        System.out.println("我的Market");
        return new RestRecord(200, WebMessageConstants.SCE_PORTAL_MSG_200, appMarketService.getAppCount());
    }
}
