package com.yunying.cn.constant;


public enum JobState {

    JOB_RUN(1, "运行"),
    JOB_STOP(2, "暂停"),
    JOB_DEL(3, "删除");

    private int status;
    private String desc;

    JobState(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

}