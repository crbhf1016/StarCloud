package cn.com.bonc.sce.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.com.bonc.sce.entity.AppTopRankView;
import java.util.List;
import java.util.Map;

/**
 * @author yanmin
 * @version 0.1
 * @since 2018/12/14 14:26
 */
@Repository
public interface AppTopRankRepository extends JpaRepository< AppTopRankView, String >, JpaSpecificationExecutor<AppTopRankView>  {

    /**
     * @param userId
     * @param pageable
     * @return
     */
    @Query( value = "SELECT MAIN.APP_ID,MAIN.APP_NAME,MAIN.COMPANY_ID,MAIN.APP_ICON,MAIN.APP_NOTES,MAIN.APP_SOURCE,MAIN.APP_LINK,MAIN.CREATE_TIME,\n" +
            "CASE WHEN A.APP_ID IS NULL THEN '0' ELSE '1' END IS_OPEN,\n" +
            "CASE WHEN C.APP_ID IS NULL THEN '0' ELSE '1' END IS_DOWNLOAD,\n" +
            "CASE WHEN D.APP_ID IS NULL THEN '0' ELSE '1' END IS_COLLECT,\n" +
            "CASE WHEN T2.COUNT IS NULL THEN 0 ELSE T2.COUNT END AS DOWNLOAD\n" +
            "FROM (SELECT * FROM STARCLOUDMARKET.SCE_MARKET_APP_INFO WHERE IS_DELETE = 1 ) MAIN \n" +
            "LEFT JOIN STARCLOUDMARKET.SCE_MARKET_APP_OPEN A ON A.APP_ID=MAIN.APP_ID AND A.USER_ID = :userId AND A.IS_DELETE=1\n" +
            "LEFT JOIN (SELECT B.APP_ID,COUNT(*) FROM STARCLOUDMARKET.SCE_MARKET_APP_DOWNLOAD B WHERE B.USER_ID = :userId GROUP BY B.APP_ID) C ON C.APP_ID = MAIN.APP_ID\n" +
            "LEFT JOIN STARCLOUDMARKET.SCE_USER_APP_COLLECTION D ON D.APP_ID=MAIN.APP_ID AND D.USER_ID = :userId AND D.IS_DELETE=1\n" +
            "LEFT JOIN (SELECT APP_ID,COUNT(*) AS COUNT FROM STARCLOUDMARKET.SCE_MARKET_APP_DOWNLOAD GROUP BY APP_ID) T2 ON MAIN.APP_ID = T2.APP_ID \n" +
            " INNER JOIN (SELECT AVC.APP_ID, AVC.APP_STATUS, TEMPA.CREATE_TIME CREATE_TIME \n" +
            "                       FROM STARCLOUDMARKET.SCE_MARKET_APP_VERSION AVC \n" +
            "                       INNER JOIN ( SELECT AVB.APP_ID, MAX( AVB.CREATE_TIME ) CREATE_TIME \n" +
            "                       FROM STARCLOUDMARKET.SCE_MARKET_APP_VERSION AVB WHERE AVB.APP_STATUS='4' AND AVB.IS_DELETE = 1 GROUP BY AVB.APP_ID ) TEMPA ON AVC.APP_ID = TEMPA.APP_ID \n" +
            "                       AND TEMPA.CREATE_TIME = AVC.CREATE_TIME WHERE  APP_STATUS='4' AND IS_DELETE=1 \n" +
            "                       ) TEMPB ON MAIN.APP_ID = TEMPB.APP_ID \n" +
            "ORDER BY MAIN.CREATE_TIME DESC",
            countQuery = "SELECT COUNT(*)" +
                    "FROM (SELECT * FROM STARCLOUDMARKET.SCE_MARKET_APP_INFO WHERE IS_DELETE = 1 ) MAIN \n" +
                    "LEFT JOIN STARCLOUDMARKET.SCE_MARKET_APP_OPEN A ON A.APP_ID=MAIN.APP_ID AND A.USER_ID = :userId AND A.IS_DELETE=1\n" +
                    "LEFT JOIN (SELECT B.APP_ID,COUNT(*) FROM STARCLOUDMARKET.SCE_MARKET_APP_DOWNLOAD B WHERE B.USER_ID = :userId GROUP BY B.APP_ID) C ON C.APP_ID = MAIN.APP_ID\n" +
                    "LEFT JOIN STARCLOUDMARKET.SCE_USER_APP_COLLECTION D ON D.APP_ID=MAIN.APP_ID AND D.USER_ID = :userId AND D.IS_DELETE=1\n" +
                    "LEFT JOIN (SELECT APP_ID,COUNT(*) AS COUNT FROM STARCLOUDMARKET.SCE_MARKET_APP_DOWNLOAD GROUP BY APP_ID) T2 ON MAIN.APP_ID = T2.APP_ID \n" +
                    " INNER JOIN (SELECT AVC.APP_ID, AVC.APP_STATUS, TEMPA.CREATE_TIME CREATE_TIME \n" +
                    "                       FROM STARCLOUDMARKET.SCE_MARKET_APP_VERSION AVC \n" +
                    "                       INNER JOIN ( SELECT AVB.APP_ID, MAX( AVB.CREATE_TIME ) CREATE_TIME \n" +
                    "                       FROM STARCLOUDMARKET.SCE_MARKET_APP_VERSION AVB WHERE AVB.APP_STATUS='4' AND AVB.IS_DELETE = 1 GROUP BY AVB.APP_ID ) TEMPA ON AVC.APP_ID = TEMPA.APP_ID \n" +
                    "                       AND TEMPA.CREATE_TIME = AVC.CREATE_TIME WHERE  APP_STATUS='4' AND IS_DELETE=1 \n" +
                    "                       ) TEMPB ON MAIN.APP_ID = TEMPB.APP_ID \n",
            nativeQuery = true )
    List< Map<String,String> > selectTopAppList( @Param( value = "userId" ) String userId,
                                                 Pageable pageable );

}
