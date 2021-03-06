package cn.com.bonc.sce.service;

import cn.com.bonc.sce.constants.WebMessageConstants;
import cn.com.bonc.sce.dao.AccountDao;
import cn.com.bonc.sce.model.Account;
import cn.com.bonc.sce.rest.RestRecord;
import cn.com.bonc.sce.tool.SendMessage;
import cn.com.bonc.sce.tool.VaildSecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * 账号安全信息相关
 *
 * @author wzm
 * @version 0.1
 * @since 2018/14/12 12:00
 */
@Slf4j
@Service
public class AccountService {

    private AccountDao accountSecurityDao;

    @Autowired
    public AccountService( AccountDao accountSecurityDao ) {
        this.accountSecurityDao = accountSecurityDao;
    }

    /**
     * 获取安全验证信息
     *
     * @param phone 手机号
     * @return 验证码
     */
    public RestRecord sendSecurityPhoneValid( String phone ) {
        String valid;
        try {
            //valid = VaildSecurityUtils.randomStr();
            valid="123456";
            VaildSecurityUtils.addValid(getAccountEncryptionCode(phone,valid));
            SendMessage.postMsgToPhone(valid,phone);
        } catch ( UnsupportedEncodingException e ) {
            return new RestRecord(409,WebMessageConstants.SCE_PORTAL_MSG_409);
        }
        return new RestRecord(200,valid);
    }

    /**
     * 验证安全信息
     *
     * @param phone 手机号
     * @param valid 验证码
     * @return 验证结果和安全码
     */
    public RestRecord validInfo( String phone, String valid ) {
        String valid_ = getAccountEncryptionCode( phone, valid );
        if ( VaildSecurityUtils.checkValid( valid_ ) ) {
            VaildSecurityUtils.delValid( valid_ );
            //创建安全码
            String randomStr = VaildSecurityUtils.randomStr();
            VaildSecurityUtils.addCode( getAccountEncryptionCode( phone, randomStr ) );
            return new RestRecord( 200, WebMessageConstants.SCE_PORTAL_MSG_200, randomStr );
        }
        return new RestRecord( 411, WebMessageConstants.SCE_PORTAL_MSG_411 );
    }

    /**
     * 修改账号信息
     *
     * @param accountSecurity 安全码和账号信息
     * @return 修改结果
     */
    public RestRecord updateAccount( Account accountSecurity ) {
        Integer successCode = 200;
        if(!StringUtils.isEmpty( accountSecurity.getUserId())&&
                !StringUtils.isEmpty( accountSecurity.getPassword())&&
                !StringUtils.isEmpty( accountSecurity.getNewPassword())){
            RestRecord rr = accountSecurityDao.updateAccount( accountSecurity );
            if ( rr.getCode() == successCode ) {
                rr.setMsg( WebMessageConstants.SCE_PORTAL_MSG_200 );
            }
            return rr;
        }
        //加密加工
        //accountSecurity.getPassword();
        String code;
        if( StringUtils.isEmpty( accountSecurity.getPhone())||StringUtils.isEmpty( accountSecurity.getCode())) {
            return new RestRecord( 412, WebMessageConstants.SCE_PORTAL_MSG_412 );
        }else{
            code = getAccountEncryptionCode(
                    accountSecurity.getPhone(), accountSecurity.getCode() );
        }
        if ( VaildSecurityUtils.checkCode( code ) ) {
            VaildSecurityUtils.delCode( code );
            RestRecord rr = accountSecurityDao.updateAccount( accountSecurity );
            if ( rr.getCode() == successCode ) {
                rr.setMsg( WebMessageConstants.SCE_PORTAL_MSG_200 );
            }
            return rr;
        } else {
            return new RestRecord( 412, WebMessageConstants.SCE_PORTAL_MSG_412 );
        }
    }

    private String getAccountEncryptionCode( String str1, String str2 ) {
        StringBuilder sb = new StringBuilder( str1 );
        sb.append( str2 );
        return DigestUtils.md5DigestAsHex( sb.toString().getBytes() );
    }
}
