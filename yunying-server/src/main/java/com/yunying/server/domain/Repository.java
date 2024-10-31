package com.yunying.server.domain;

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
 * @since 2024-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("repository")
public class Repository implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库id
     */
    @TableId(value = "repo_id", type = IdType.AUTO)
    private Integer repoId;

    /**
     * 仓库名字
     */
    private String repoName;

    /**
     * 仓库全名（包含归属者）
     */
    private String repoFullName;

    /**
     * 仓库链接
     */
    private String repoHome;

    /**
     * 仓库描述
     */
    private String description;

    /**
     * 仓库作者id
     */
    private Integer ownerId;

    /**
     * 【JSON字段】仓库语言
     */
    private String language;

    /**
     * watch数量
     */
    private Integer watchCount;

    /**
     * fork数量
     */
    private Integer forkCount;

    /**
     * PR数量
     */
    private Integer prCount;

    /**
     * star数量
     */
    private Integer starCount;

    /**
     * issue数量
     */
    private Integer issueCount;

    /**
     * 【计算字段】仓库重要性评分
     */
    private Float importance;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更改时间
     */
    private LocalDateTime updateTime;


}
