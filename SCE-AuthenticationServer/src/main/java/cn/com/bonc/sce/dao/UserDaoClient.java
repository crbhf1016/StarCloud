package cn.com.bonc.sce.dao;

import cn.com.bonc.sce.model.User;
import cn.com.bonc.sce.rest.RestRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leucippus
 * @version 0.1
 * @since 2018/12/22 17:58
 */
@FeignClient( "sce-data-access" )
public interface UserDaoClient {

    /**
     * 根据用户 id 获取用户详细信息
     *
     * @param userId 用户id
     *
     * @return 用户的详细数据
     */
    @RequestMapping( value = "/users/{userId}", method = RequestMethod.GET )
    public User getUserById( @PathVariable( "userId" ) String userId );

    /**
     * 根据用户的 登录id 获取用户信息
     *
     * @param loginName 登录的账号信息
     *
     * @return 用户数据
     */
    @RequestMapping( value = "/users/login-name-is/", method = RequestMethod.POST )
    public User getUserByLoginName( @RequestBody String loginName );

    /**
     * 获取用户详细信息
     *
     * @param userId userId
     *
     * @return 获取用户
     */
    @RequestMapping( value = "/users/{userId}/info/correction", method = RequestMethod.POST )
    public RestRecord changeUserInfoIntegrityAndAccuracyStatus( @PathVariable( "userId" ) String userId, @RequestBody Boolean isUserInfoCorrect );

    /**
     * 修改用户
     *
     * @return 修改用户
     */
    @RequestMapping( value = "/users/detailed", method = RequestMethod.POST )
    public RestRecord updateUserInfo( User user );

    /**
     * 获取用户详细信息
     *
     * @param userId userId
     *
     * @return 获取用户
     */
    @RequestMapping( value = "/users/detailed/{userId}", method = RequestMethod.GET )
    public RestRecord getUserInfo( @PathVariable( "userId" ) String userId );

    @RequestMapping( value = "/user-info/password", method = RequestMethod.PUT )
    RestRecord updatePasswordById( @RequestParam("userId")  String userId, @RequestParam("password")  String password );
}
