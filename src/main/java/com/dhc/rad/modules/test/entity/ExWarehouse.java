/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dhc.rad.common.persistence.DataEntity;

/**
 * 出库主表Entity
 * @author maliang
 * @version 2016-02-28
 */
@Alias("TestExWarehouse")
public class ExWarehouse extends DataEntity<ExWarehouse> {
	
	private static final long serialVersionUID = 1L;

	//--------------------------Entity---------------------------------
	private String vbillcode;		// 单据号
	private Date dbilldate;		// 单据日期
	private String cwhsmanagerid;		// 库管员ID
	private String cwarehouseid;		// 仓库id
	private String workcenterid;		// 工作中心
	private String lastsn;		// 流水号
	private String exflag; // '0' 出库标识
	private String taskcode;// 任务号
	private String lydjh; // 来源单据号
	
	public ExWarehouse() {
		super();
	}

	public ExWarehouse(String id){
		super(id);
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	public Date getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(Date dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getCwhsmanagerid() {
		return cwhsmanagerid;
	}

	public void setCwhsmanagerid(String cwhsmanagerid) {
		this.cwhsmanagerid = cwhsmanagerid;
	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public String getWorkcenterid() {
		return workcenterid;
	}

	public void setWorkcenterid(String workcenterid) {
		this.workcenterid = workcenterid;
	}

	public String getLastsn() {
		return lastsn;
	}

	public void setLastsn(String lastsn) {
		this.lastsn = lastsn;
	}

	public String getExflag() {
		return exflag;
	}

	public void setExflag(String exflag) {
		this.exflag = exflag;
	}

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getLydjh() {
		return lydjh;
	}

	public void setLydjh(String lydjh) {
		this.lydjh = lydjh;
	}

}