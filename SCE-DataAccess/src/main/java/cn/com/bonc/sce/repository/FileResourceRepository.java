package cn.com.bonc.sce.repository;

import cn.com.bonc.sce.entity.FileResourceEntity;
import cn.com.bonc.sce.model.ExcelToUser;
import org.mapstruct.ObjectFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author BTW
 */
@Transactional(rollbackFor = Exception.class)
public interface FileResourceRepository extends JpaRepository< FileResourceEntity, String > {

    /**
     * 文件存储信息保存
     * @param s
     * @param <S>
     * @return
     */
    @Override
    < S extends FileResourceEntity > S save( S s );



    /**
     * 插入用户啊
     * @param
     * @return
     */
    @Modifying
    @Query( nativeQuery = true, value = "insert  into  STARCLOUDPORTAL.SCE_COMMON_USER(USER_ID,USER_NAME,GENDER,LOGIN_NAME,USER_TYPE," +
            "MAIL_ADDRESS,CERTIFICATE_TYPE,CERTIFICATE_NUMBER,PHONE_NUMBER,ADDRESS,ORGANIZATION_ID,BIRTHDATE,SECRET) VALUES " +
            "(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13)  " )
    int savaAllUserInfo(String id, String userName, String gender , String loginName
                        , String userType, String mailAddress, String certificateType
                        , String certificateNumber, String phoneNumber, String address
                        , BigDecimal organizationId, Date birthDate, String secret);

    /**
     * 插入用户啊
     * @param
     * @return
     */
    @Modifying
    @Query( nativeQuery = true, value = "insert  into  STARCLOUDPORTAL.SCE_COMMON_USER(USER_ID,LOGIN_NAME,USER_TYPE,ORGANIZATION_ID,SECRET) VALUES " +
            "(?1,?2,?3,?4,?5)  " )
    int savaAllUserInfo(String id, String loginName, String userType, String organizationId, String secret);

    /**
     * 插入用户的parent
     * @param
     * @return
     */
    @Modifying
    @Query( nativeQuery = true, value = "insert into STARCLOUDPORTAL.SCE_COMMON_USER(USER_ID,LOGIN_NAME,USER_TYPE,SECRET) VALUES(?1,?2,?3,?4)" )
    int saveParentOfUser(String userId, String loginName, String userType, String secret);

    /**
     * 插入家长数据
     * @param
     * @return
     */
    @Modifying
    @Query( nativeQuery = true, value = "insert  into  STARCLOUDPORTAL.SCE_COMMON_USER(USER_ID,USER_NAME,GENDER,LOGIN_NAME,USER_TYPE," +
            "MAIL_ADDRESS,CERTIFICATE_TYPE,CERTIFICATE_NUMBER,PHONE_NUMBER,ADDRESS,BIRTHDATE,SECRET) VALUES " +
            "(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12)  " )
    int saveParent(String id, String userName, String gender , String loginName
            , String userType, String mailAddress, String certificateType
            , String certificateNumber, String phoneNumber, String address
            , Date birthDate, String secret);

    /**
     *  插入数据到学生表
     * @param
     * @return
     */
    @Modifying
    @Query( nativeQuery = true, value = "insert into STARCLOUDPORTAL.SCE_INFO_STUDENT(USER_ID,CLASS_NUMBER,GRADE,STUDENT_NUMBER," +
            "HEALTH_CODE,NATIONALITY,MARRIAGE_CODE,ACCOUNT_AREA_CODE,NAME_SPELL,BIRTH_PLACE,NATIVE_PLACE,NATION_CODE," +
            "POLITICS_STATUS,ACCOUNT_CLASS_CODE,ENTRANCE_YEAR,LEARN_SPECIALTY) VALUES (?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16) ")
    int saveStudent(String userId, String classNumber, String grade, String studentNumber, String healthCode, String nationLity,
                    String marriAgeCode, String accountAreaCode, String nameSpell, String birthPlace, String nativePlace,
                    String nationCode, String politicsStatus, String accountClassCode, Date entranceYear, String learnSpecialty);

    /**
     * 插入家长信息
     * @param userId
     * @param familyRole
     * @return
     */
    @Modifying
    @Query( nativeQuery = true, value = "insert into STARCLOUDPORTAL.SCE_INFO_PARENT(USER_ID,FAMILY_ROLE,IS_DELETE) " +
            "VALUES (?1,?2,1)")
    int saveParent(String userId, String familyRole);

    /**
     * 插入家长-学生关系
     * @param id 表id
     * @param parentUserId 家长id
     * @param studentUserId 学生id
     * @return
     */
    @Modifying
    @Query( nativeQuery = true, value = "insert into STARCLOUDPORTAL.SCE_INFO_STUDENT_PARENT_REL(ID,PARENT_USER_ID,STUDENT_USER_ID) " +
            "VALUES (?1,?2,?3)")
    int saveStudentParentRel(long id, String parentUserId, String studentUserId);

    /**
     * 通过用户id查询用户的组织id
     * @param userId
     * @return
     */
    @Query( nativeQuery = true, value = "SELECT ORGANIZATION_ID FROM STARCLOUDPORTAL.SCE_COMMON_USER WHERE USER_ID = ?1")
    BigDecimal selectOrganizationId(String userId);

    /**
     * 通过用户身份证作唯一判断
     * @param certificateNumber
     * @return
     */
    @Query( nativeQuery = true, value = "SELECT USER_ID FROM STARCLOUDPORTAL.SCE_COMMON_USER WHERE CERTIFICATE_NUMBER = ?1")
    String selectUserCount(String certificateNumber);

    /**
     * 插入数据到教师表
     * @param
     * @return
     */
    @Modifying
    @Query( nativeQuery = true, value = "insert into STARCLOUDPORTAL.SCE_INFO_TEACHER(USER_ID,SCHOOL_ID,POSITION,SUBJECT,SCHOOL_AGE," +
            "NAME_SPELL,NATION_CODE,POLITICS_STATUS,WORK_NUMBER,MARRIAGE_CODE,BIRTH_PLACE,ACCOUNT_AREA_CODE,SCHOOL_TIME) VALUES (?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13)")
    int saveTeacher(String userID, String schoolId, String position, String subject, String schoolAge, String nameSpell, String nationCode,
                    String politicsStatus, String workNumber, String marriageCode, String birthPlace,String accountAreaCode, Date schoolTime);


    /**
     * 插入用户密码吗
     * @param
     * @return
     */
    @Modifying
    @Query( nativeQuery = true, value = "insert  into  STARCLOUDPORTAL.SCE_COMMON_USER_PASSWORD(USER_ID ,PASSWORD,IS_DELETE) VALUES " +
            "(?1,'star123!',1)  " )
    void saveUserPassword(String passWord);



    /**
     *@Desc: 根据文件id查询文件FILE_MAPPING_PATH
     *@Param: resourceId
     *@return: String
     *@Author: lyy
     *@date: 2018/12/27
     */
    @Query(value = "SELECT FILE_MAPPING_PATH FROM STARCLOUDPORTAL.SCE_FILE_RESOURCE where RESOURCE_ID =?1"
            ,nativeQuery = true)
    Map<String,Object> getFileResourceById(Integer resourceId);

    /**
     *@Desc: 根据文件id查询文件FILE_STORE_PATH
     *@Param: resourceId
     *@return: String
     *@Author: lyy
     *@date: 2018/12/27
     */
    @Query(value = "SELECT FILE_STORE_PATH FROM STARCLOUDPORTAL.SCE_FILE_RESOURCE where RESOURCE_ID =?1"
            ,nativeQuery = true)
    Map<String,Object> getFileStorePathById(Integer resourceId);




}
