package com.yunying.gh.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("developer")
public class Developer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开发者id
     */
    @TableId(value = "dev_id", type = IdType.AUTO)
    private Integer devId;

    /**
     * 开发者登录id
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
     * 开发者主页
     */
    private String home;

    /**
     * 博客链接
     */
    private String blog;

    /**
     * 地区
     */
    private String location;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 自我介绍
     */
    private String bio;

    /**
     * 粉丝
     */
    private Integer followers;

    /**
     * 关注人数
     */
    private Integer following;

    /**
     * 【计算字段】粉丝权重
     */
    private Double followersWeight;

    /**
     * 【计算字段】技能排序值
     */
    private Float talentRank;

    /**
     * 【推测字段】所属国家
     */
    private String nation;

    /**
     * 【推测字段】国家/地区可信度
     */
    private Double nationConf;

    /**
     * 【生成字段】开发者简介
     */
    private String profile;

    /**
     * 第一此创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
