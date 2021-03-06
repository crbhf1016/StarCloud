package cn.com.bonc.sce.service;

import cn.com.bonc.sce.constants.WebMessageConstants;
import cn.com.bonc.sce.entity.AppInfoEntity;
import cn.com.bonc.sce.entity.AppTypeRelEntity;
import cn.com.bonc.sce.entity.MarketAppVersion;
import cn.com.bonc.sce.model.AppAddModel;
import cn.com.bonc.sce.model.AppTypeMode;
import cn.com.bonc.sce.model.PlatformAddModel;
import cn.com.bonc.sce.repository.*;
import cn.com.bonc.sce.rest.RestRecord;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author jc_D
 * @description
 * @date 2019/1/3
 **/
@Slf4j
@Service
public class AppManageService {
    @Autowired
    private FileResourceRepository fileResourceRepository;

    @Autowired
    private AppInfoRepository appInfoRepository;
    @Autowired
    private AppTypeRelRepository appTypeRelRepository;

    @Autowired
    private MarketAppVersionRepository marketAppVersionRepository;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Transactional( rollbackFor = Exception.class )
    public RestRecord addAppInfo( AppAddModel appInfo, String uid ) {
        //根据uid查companyId
        Long companyId = companyInfoRepository.getCompanyIdByUid( uid );
        if ( companyId == null ) {
            return new RestRecord( 423, "您不是厂商用户，无法新增软件应用" );
        }
        //取icon
        String iconAddress = getFileUrlById( appInfo.getAppIcon() );
        //1.appinfo表
        AppInfoEntity appInfoEntity = new AppInfoEntity();
        appInfoEntity.setAppIcon( iconAddress );
        appInfoEntity.setAppName( appInfo.getAppName() );
        appInfoEntity.setCreateUserId( uid );
        appInfoEntity.setCreateTime( new Date() );
        appInfoEntity.setIsDelete( 1 );
        appInfoEntity.setAppSource( "rj" );
        appInfoEntity.setAppNotes( appInfo.getAppNotes() );
        appInfoEntity.setCompanyId( companyId );
        //开发者信息
        String idCardUrl = getFileUrlById( appInfo.getDeveloperIdPic() );
        appInfoEntity.setDeveloperName( appInfo.getDeveloperName() );
        appInfoEntity.setDeveloperIdNumber( appInfo.getDeveloperIdNumber() );
        appInfoEntity.setDeveloperPhone( appInfo.getDeveloperPhone() );
        appInfoEntity.setMainContact( appInfo.getMainContact() );
        appInfoEntity.setDeveloperIdPic( idCardUrl );
        appInfoEntity.setChargeMode( appInfo.getChargeMode() );
        //软件凭证
        String appCopyright = getFilesUrlById( appInfo.getAppCopyright() );
        appInfoEntity.setAppCopyright( appCopyright );
        //财务凭证
        String auditVoucher = getFilesUrlById( appInfo.getAuditVoucher() );
        appInfoEntity.setAuditVoucher( auditVoucher );
        //appinfo入库操作
        AppInfoEntity info = appInfoRepository.saveAndFlush( appInfoEntity );
        String appId = info.getAppId();
        //2.类型关系表
        AppTypeRelEntity appTypeRelEntity = new AppTypeRelEntity();
        appTypeRelEntity.setAppId( appId );
        appTypeRelEntity.setAppTypeId( appInfo.getAppTypeId() );
        AppTypeRelEntity rel = appTypeRelRepository.saveAndFlush( appTypeRelEntity );

        //根据id取pc图片链接
        String pcUrl = getFilesUrlById( appInfo.getAppPcPic() );

        //根据id取phone图片链接
        String phoneUrl = getFilesUrlById( appInfo.getAppPhonePic() );

        //3.版本表
        Set< AppTypeMode > pcSet = appInfo.getPc();
        pcSet.forEach( pc -> {
            String version = pc.getAppVersion();
            //根据addressId获取软件存储路径
            String addressId = pc.getAddress();
            String softwareAddress = getFileUrlById( Integer.parseInt( addressId ) );
            //往版本表存东西
            MarketAppVersion marketAppVersion = new MarketAppVersion();
            marketAppVersion.setAppId( appId );
            marketAppVersion.setAppDownloadAddress( softwareAddress );
            marketAppVersion.setAppVersion( version );
            marketAppVersion.setVersionInfo( appInfo.getAppNotes() );
            marketAppVersion.setPackageName( pc.getPackageName() );
            marketAppVersion.setVersionSize( pc.getVersionSize() );
            marketAppVersion.setAppStatus( "1" );
            marketAppVersion.setNewFeatures( appInfo.getNewFeatures() );
            marketAppVersion.setAuthDetail( appInfo.getAuthDetail().toString().replace( "[", "" ).replace( "]", "" ).replaceAll( "\\s*", "" ) );
            marketAppVersion.setAppPcPic( pcUrl );
            marketAppVersion.setAppPhonePic( phoneUrl );
            marketAppVersion.setCreateTime( new Date() );
            marketAppVersion.setIsDelete( 1L );
            marketAppVersion.setRunningPlatform( pc.getRunningPlatform() );
            marketAppVersion.setCreateUserId( uid );
            marketAppVersionRepository.saveAndFlush( marketAppVersion );
        } );

        return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_200, appId );
    }

    @Transactional( rollbackFor = Exception.class )
    public RestRecord addPlatFormInfo( PlatformAddModel platFormInfo, String uid ) {
        //根据uid查companyId
        Long companyId = companyInfoRepository.getCompanyIdByUid( uid );
        //取icon
        if ( companyId == null ) {
            return new RestRecord( 423, "您不是厂商用户，无法新增平台应用" );
        }
        String iconAddress = getFileUrlById( platFormInfo.getAppIcon() );
        //1.appinfo表
        AppInfoEntity appInfoEntity = new AppInfoEntity();
        appInfoEntity.setAppIcon( iconAddress );
        appInfoEntity.setAppName( platFormInfo.getAppName() );
        appInfoEntity.setCreateUserId( uid );
        appInfoEntity.setCreateTime( new Date() );
        appInfoEntity.setIsDelete( 1 );
        appInfoEntity.setAppSource( "pt" );
        appInfoEntity.setAppNotes( platFormInfo.getAppNotes() );
        appInfoEntity.setCompanyId( companyId );

        //开发者信息
        String idCardUrl = getFileUrlById( platFormInfo.getDeveloperIdPic() );
        appInfoEntity.setDeveloperName( platFormInfo.getDeveloperName() );
        appInfoEntity.setDeveloperIdNumber( platFormInfo.getDeveloperIdNumber() );
        appInfoEntity.setDeveloperPhone( platFormInfo.getDeveloperPhone() );
        appInfoEntity.setMainContact( platFormInfo.getMainContact() );
        appInfoEntity.setDeveloperIdPic( idCardUrl );
        appInfoEntity.setChargeMode( platFormInfo.getChargeMode() );

        //财务凭证
        String auditVoucher = getFilesUrlById( platFormInfo.getAuditVoucher() );
        appInfoEntity.setAuditVoucher( auditVoucher );

        ////appinfo入库操作
        AppInfoEntity info = appInfoRepository.saveAndFlush( appInfoEntity );
        String appId = info.getAppId();
        //2.类型关系表
        AppTypeRelEntity appTypeRelEntity = new AppTypeRelEntity();
        appTypeRelEntity.setAppId( appId );
        appTypeRelEntity.setAppTypeId( platFormInfo.getAppTypeId() );
        AppTypeRelEntity rel = appTypeRelRepository.saveAndFlush( appTypeRelEntity );

        //根据id取pc图片链接
        String pcUrl = getFilesUrlById( platFormInfo.getAppPcPic() );

        //3.版本表
        //根据addressId获取软件存储路径
        String platFormAddress = getFileUrlById( platFormInfo.getStoreLocation() );
        //往版本表存东西
        MarketAppVersion marketAppVersion = new MarketAppVersion();
        marketAppVersion.setAppId( appId );
        // marketAppVersion.setAppDownloadAddress( platFormAddress );
        marketAppVersion.setAppVersion( platFormInfo.getAppVersion() );
        marketAppVersion.setVersionInfo( platFormInfo.getAppNotes() );
        marketAppVersion.setPackageName( platFormInfo.getPackageName() );
        marketAppVersion.setAppStatus( "1" );
        marketAppVersion.setAppPcPic( pcUrl );
        marketAppVersion.setCreateTime( new Date() );
        marketAppVersion.setIsDelete( 1L );
        marketAppVersion.setCreateUserId( uid );
        marketAppVersion.setIndexUrl( platFormInfo.getIndexUrl() );

        marketAppVersion.setTokenAddress( platFormInfo.getTokenAddress() );
        marketAppVersion.setStoreLocation( platFormAddress );
        marketAppVersion.setTestUrl( platFormInfo.getTestUrl() );
        String installInfo = platFormInfo.getInstallInfo();
        String newFeatures = platFormInfo.getNewFeatures();
        String md5Code = platFormInfo.getMd5Code();
        if ( StringUtils.isNotEmpty( newFeatures ) ) {
            marketAppVersion.setNewFeatures( newFeatures );
        }
        if ( StringUtils.isNotEmpty( installInfo ) ) {
            marketAppVersion.setInstallInfo( installInfo );
        }
        if ( StringUtils.isNotEmpty( md5Code ) ) {
            marketAppVersion.setMd5Code( md5Code );
        }
        marketAppVersionRepository.saveAndFlush( marketAppVersion );
        return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_200, appId );
    }


    /**
     * 根据文件id取文件路径
     *
     * @param ids 一批文件id
     * @return
     */
    private String getFilesUrlById( Set< Integer > ids ) {
        if ( CollUtil.isEmpty( ids ) ) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for ( Integer id : ids ) {
            Map< String, Object > fileStorePath = fileResourceRepository.getFileStorePathById( id );
            String p = fileStorePath.get( "FILE_STORE_PATH" ).toString();
            sb.append( p ).append( "," );
        }
        return StringUtils.substring( sb.toString(), 0, sb.length() - 1 );
    }

    /**
     * 根据id获取文件路径
     *
     * @param FileId
     * @return
     */
    private String getFileUrlById( Integer FileId ) {
        if ( FileId == null ) {
            return null;
        }
        Map< String, Object > map = fileResourceRepository.getFileStorePathById( FileId );
        if ( CollUtil.isEmpty( map ) ) {
            return null;
        }
        String URL = map.get( "FILE_STORE_PATH" ) == null ? null : map.get( "FILE_STORE_PATH" ).toString();
        return URL;
    }


}
