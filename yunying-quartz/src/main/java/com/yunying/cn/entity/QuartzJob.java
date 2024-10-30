package com.yunying.cn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartzJob implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "BOOT_JOB_PARAM_KEY";

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String beanName;

    private String params;

    private String cronExpres;

    private Integer state;


    private String remark;


    private Date createTime;
}