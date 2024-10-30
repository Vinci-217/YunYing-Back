package com.yunying.cn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartzLog {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Integer jobId;

    private String beanName;

    private String params;

    private Integer state;

    private String error;

    private Integer times;

    private Date createTime;

}