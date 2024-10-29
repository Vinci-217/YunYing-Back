package com.yunying.gh.domain;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("Repository")
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
    @TableField("repo_name")
    private String repoName;

    /**
     * 仓库全名（包含归属者）
     */
    @TableField("repo_full_name")
    private String repoFullName;

    /**
     * 仓库链接
     */
    @TableField("repo_home")
    private String repoHome;

    /**
     * 仓库描述
     */
    @TableField("description")
    private String description;

    /**
     * 仓库作者id
     */
    @TableField("owner_id")
    private Integer ownerId;

    /**
     * 【JSON字段】仓库语言
     */
    @TableField("language")
    private String language;

    /**
     * watch数量
     */
    @TableField("watch_count")
    private Integer watchCount;

    /**
     * fork数量
     */
    @TableField("fork_count")
    private Integer forkCount;

    /**
     * PR数量
     */
    @TableField("pr_count")
    private Integer prCount;

    /**
     * star数量
     */
    @TableField("star_count")
    private Integer starCount;

    /**
     * issue数量
     */
    @TableField("issue_count")
    private Integer issueCount;

    /**
     * 【计算字段】仓库重要性评分
     */
    @TableField("importance")
    private Float importance;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


}
