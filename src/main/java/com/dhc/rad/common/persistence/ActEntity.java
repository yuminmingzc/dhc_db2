/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.common.persistence;

import com.dhc.rad.modules.act.entity.Act;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Activiti Entity类
 *
 * @author DHC
 * @version 2013-05-28
 */
public abstract class ActEntity<T> extends DataEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Act act;        // 流程任务对象

    protected String taskName;//审核数据列表中显示当前正在执行的任务名

    public ActEntity() {
        super();
    }

    public ActEntity(String id) {
        super(id);
    }

    @JsonIgnore
    public Act getAct() {
        if (act == null) {
            act = new Act();
        }
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    /**
     * 获取流程实例ID
     *
     * @return
     */
    public String getProcInsId() {
        return this.getAct().getProcInsId();
    }

    /**
     * 设置流程实例ID
     *
     * @param procInsId
     */
    public void setProcInsId(String procInsId) {
        this.getAct().setProcInsId(procInsId);
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
