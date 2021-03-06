package cn.com.bonc.sce.controller;

import cn.com.bonc.sce.annotation.CurrentUserId;
import cn.com.bonc.sce.constants.WebMessageConstants;
import cn.com.bonc.sce.exception.ImportUserFailedException;
import cn.com.bonc.sce.model.ExcelToUser;
import cn.com.bonc.sce.model.UploadFileModel;
import cn.com.bonc.sce.rest.RestRecord;
import cn.com.bonc.sce.service.FileUploadService;
import cn.com.bonc.sce.tool.ParseExcel;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 通用文件上传接口
 *
 * @author BTW
 * @version 0.1
 * @since 2018/12/22 14:36
 */
@Slf4j
@Api( value = "通用文件上传接口" )
@ApiResponses( { @ApiResponse( code = 500, message = "服务器内部错误", response = RestRecord.class ) } )
@RestController
@RequestMapping( "/file-upload" )
public class FileUploadController {

    private FileUploadService fileUploadService;

    public static final String STUDENT_CODE = "1";
    public static final String TEACHER_CODE = "2";

    @Autowired
    public FileUploadController( FileUploadService fileUploadService ) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * @param multipartFile 上传文件
     * @param fileType      上传文件类型
     * @return 返回对应文件在资源表中ID
     */
    @ApiOperation( value = "文件上传通用接口", notes = "文件上传通用接口", httpMethod = "POST" )
    @ApiResponses( {
            @ApiResponse( code = 200, message = WebMessageConstants.SCE_PORTAL_MSG_200, response = RestRecord.class )
    } )
    @PostMapping( "" )
    @ResponseBody
    public RestRecord uploadPicture( @RequestParam( "file" ) @ApiParam( name = "file", value = "上传文件", required = true ) MultipartFile multipartFile,
                                     @RequestParam( "fileType" ) @ApiParam( name = "fileType", value = "文件类型", allowableValues = "pic,soft,document,war" ) String fileType ) {
        if ( multipartFile == null || multipartFile.isEmpty() ) {
            return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_450 );
        }
        return fileUploadService.uploadMultipart( multipartFile, fileType );
    }

    /**
     * 批量上传Excel解析用户数据
     *
     * @return 返回成功与否
     */
    @ApiOperation( value = "文件上传解析Excel", notes = "文件上传解析Excel", httpMethod = "POST" )
    @ApiResponses( {
            @ApiResponse( code = 200, message = WebMessageConstants.SCE_PORTAL_MSG_200, response = RestRecord.class )
    } )
    @PostMapping( "/upload-user-info" )
    @ResponseBody
    public RestRecord uploadParseExcel(@ModelAttribute @ApiParam( name = "file", value = "上传信息", required = true, example = "{multipartFile:'file',fileType:'document',userType:2}" ) UploadFileModel uploadFileModel, @CurrentUserId String currentUserId) {
        if ( uploadFileModel.getFile() == null || uploadFileModel.getFile().isEmpty()
                || uploadFileModel.getFileType().isEmpty() || uploadFileModel.getUserType().isEmpty() ) {
            return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_450 );
        }
        List< ExcelToUser > list;

        if ( STUDENT_CODE.equals( uploadFileModel.getUserType() ) ) {
            //解析学生用户Excel
            list = ParseExcel.importExcel( uploadFileModel.getFile(), 1, 1, ExcelToUser.class );

        } else if (TEACHER_CODE.equals( uploadFileModel.getUserType() ) ) {
            //解析教师用户Excel
            list = ParseExcel.importExcel( uploadFileModel.getFile(), 1, 1, ExcelToUser.class );
        }else {
            return new RestRecord( 453, WebMessageConstants.SCE_PORTAL_MSG_453 );
        }

        log.info( "解析Excel成功" );
        RestRecord restRecord = null;
        try {
            restRecord = fileUploadService.uploadUserInfo( list, uploadFileModel.getUserType(),currentUserId );
        }catch (Exception e){
            e.printStackTrace();
            try {
                restRecord = new RestRecord(423, e.getMessage().substring(e.getMessage().indexOf("message") + 10, e.getMessage().indexOf("path") - 3));
            }catch (Exception e1){
                e1.printStackTrace();
                restRecord = new RestRecord(423,WebMessageConstants.SCE_PORTAL_MSG_423);
            }
        }
        return restRecord;
    }

    /**
     * @param multipartFileAll 上传文件
     * @param fileType         上传文件类型
     * @return 返回对应文件在资源表中ID
     */
    @ApiOperation( value = "多文件上传通用接口", notes = "多文件上传通用接口", httpMethod = "POST" )
    @ApiResponses( {
            @ApiResponse( code = 200, message = WebMessageConstants.SCE_PORTAL_MSG_200, response = RestRecord.class )
    } )
    @PostMapping( "/all" )
    @ResponseBody
    public RestRecord uploadPictureAll( @RequestParam( "file" ) @ApiParam( name = "file", value = "上传文件", required = true ) MultipartFile[] multipartFileAll,
                                        @RequestParam( "fileType" ) @ApiParam( name = "filType", value = "文件类型", allowableValues = "pic,soft,document" ) String fileType ) {
        if ( multipartFileAll == null || multipartFileAll.length == 0 ) {
            return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_450 );
        }
        return fileUploadService.uploadMultipartAll( multipartFileAll, fileType );
    }

    /**
     * 文件删除
     *
     * @param idChar
     */
    @ApiOperation( value = "多文件删除接口", notes = "根据文件id的json数组删除文件接口", httpMethod = "delete" )
    @ApiResponses( {
            @ApiResponse( code = 200, message = WebMessageConstants.SCE_PORTAL_MSG_200, response = RestRecord.class ),
            @ApiResponse( code = 422, message = WebMessageConstants.SCE_PORTAL_MSG_422, response = RestRecord.class )
    } )
    @DeleteMapping
    public RestRecord deleteFile( @RequestBody @ApiParam( "文件id，json数组，例如[1,2,3,4]" ) List< Integer > idChar ) {
        log.trace( "删除文件id数组:{}", idChar );
        return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_200 );
    }


    /**
     * 根据id获取文件储存路径
     *
     * @param resourceId
     * @return
     */
    @ApiOperation( value = "根据文件id查询文件路径", notes = "根据文件id查询文件路径", httpMethod = "get" )
    @ApiResponses( {
            @ApiResponse( code = 200, message = WebMessageConstants.SCE_PORTAL_MSG_200, response = RestRecord.class ),
    } )
    @GetMapping( "/getFileResource" )
    @ResponseBody
    public RestRecord getFileResourceById( @ApiParam( "文件id" ) @RequestParam( "resourceId" ) Integer resourceId ) {

        return fileUploadService.getFileResourceById( resourceId );
    }

}
