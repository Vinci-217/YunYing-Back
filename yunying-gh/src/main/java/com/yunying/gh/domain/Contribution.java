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
@TableName("contribution")
public class Contribution implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 贡献id
     */
    @TableId(value = "con_id", type = IdType.AUTO)
    private Integer conId;

    /**
     * 【外键字段】仓库id
     */
    private Integer repoId;

    /**
     * 【外键字段】开发者id
     */
    private Integer devId;

    /**
     * 【计算字段】贡献权重
     */
    private Double weight;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 【推测字段】开发者领域
     */
    private String field;


    private Double fieldConf;

    /**
     * commit的数量
     */
    private Integer commitCount;

    /**
     * PR的数量
     */
    private Integer prCount;

    /**
     * issue的数量
     */
    private Integer issueCount;


}
