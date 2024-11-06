package com.yunying.gh.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author vinci
 * @since 2024-11-06
 */
@TableName("following")
public class Following implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开发者id
     */
    private Integer devId;

    /**
     * 开发者登录
     */
    private String devLogin;

    /**
     * 开发者名字
     */
    private String devName;

    /**
     * 开发者头像
     */
    private String avatar;

    /**
     * 【外键字段】粉丝名字
     */
    private Integer followerId;

    /**
     * 地区
     */
    private String location;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public Integer getDevId() {
        return devId;
    }

    public void setDevId(Integer devId) {
        this.devId = devId;
    }
    public String getDevLogin() {
        return devLogin;
    }

    public void setDevLogin(String devLogin) {
        this.devLogin = devLogin;
    }
    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Following{" +
            "devId=" + devId +
            ", devLogin=" + devLogin +
            ", devName=" + devName +
            ", avatar=" + avatar +
            ", followerId=" + followerId +
            ", location=" + location +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
