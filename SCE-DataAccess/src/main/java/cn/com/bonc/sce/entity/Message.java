package cn.com.bonc.sce.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 总消息
 *
 * @author wzm
 * @version 0.1
 * @since 2018/12/21 9:00
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="SCE_COMMON_INFORMATION",schema = "STARCLOUDPORTAL")
public class Message {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_GEN_COMMON_INFORMATION")
    @SequenceGenerator(name="SEQ_GEN_COMMON_INFORMATION",allocationSize=1,initialValue=1, sequenceName="SEQ_COMMON_INFORMATION")
    @Column(name = "INFORMATION_ID")
    private Integer id;

    @Column(name = "INFORMATION_CONTENT")
    private String content;

    @Column(name = "INFORMATION_TYPE")
    private Integer type;

    @Column(name = "INFORMATION_TOPIC_TYPE")
    private Integer topicType;

    @Column(name = "TARGET_USER_ID",nullable = false,updatable = false)
    private String targetId;

    @Column(name = "INITIATE_USER_ID",nullable = false,updatable = false)
    private String sourceId;

    /*private String sourceAccount;*/

    @Column(name = "INFORMATION_STATUS")
    private String status;

    @CreatedDate
    @Column(name = "CREATE_TIME",nullable = false,updatable = false)
    private Timestamp createTime;

    @Column(name = "IS_DELETE")
    private Integer isDelete;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent( String content ) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType( Integer type ) {
        this.type = type;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType( Integer topicType ) {
        this.topicType = topicType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId( String targetId ) {
        this.targetId = targetId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId( String sourceId ) {
        this.sourceId = sourceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime( Timestamp createTime ) {
        this.createTime = createTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete( Integer isDelete ) {
        this.isDelete = isDelete;
    }

    public Message() {

    }

    public Message( String content, Integer type, Integer topicType, String targetId, String sourceId, String status, Timestamp createTime, Integer isDelete ) {

        this.content = content;
        this.type = type;
        this.topicType = topicType;
        this.targetId = targetId;
        this.sourceId = sourceId;
        this.status = status;
        this.createTime = createTime;
        this.isDelete = isDelete;
    }
}
