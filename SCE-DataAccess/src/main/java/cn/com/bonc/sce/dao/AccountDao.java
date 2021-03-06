package cn.com.bonc.sce.dao;

import cn.com.bonc.sce.entity.Account;
import cn.com.bonc.sce.entity.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 账号安全信息相关
 *
 * @author wzm
 * @version 0.1
 * @since 2018/14/12 12:00
 */
@Repository
public interface AccountDao extends JpaRepository<Account, Integer> {

    Account getAccountByUserIdAndPassword( String userId, String password );

    Account findByUserIdAndIsDelete(String userId,Integer isDelete);

    @Override
    Account save( Account account );

    @Modifying
    @Query( value="UPDATE STARCLOUDPORTAL.SCE_COMMON_USER_PASSWORD SET PASSWORD=?2 WHERE USER_ID IN (SELECT USER_ID FROM STARCLOUDPORTAL.SCE_COMMON_USER WHERE PHONE_NUMBER = ?1 AND IS_DELETE <> 0) AND IS_DELETE <> 0",nativeQuery=true)
    Integer updatePasswordByPhone( String phone, String password );

    @Modifying
    @Query( value="UPDATE STARCLOUDPORTAL.SCE_COMMON_USER_PASSWORD SET PASSWORD=?2 WHERE USER_ID=?1 AND IS_DELETE <> 0",nativeQuery=true)
    Integer updatePasswordById( String userId, String password );
}
