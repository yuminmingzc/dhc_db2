/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import org.apache.ibatis.type.Alias;

import com.dhc.rad.common.persistence.DataEntity;

/**
 * ERP现存量表Entity
 * 
 * @author maliang
 * @version 2015-10-21
 */
@Alias("TestOnhandNumERP")
public class OnhandNumERP extends DataEntity<OnhandNumERP> {

	private static final long serialVersionUID = 1L;

	// --------------------------Entity---------------------------------
	private String cinvbasid; // 存货基本档案主键
	private String cinventoryid; // 存货管理档案主键
	private String cwarehouseid; // 项目仓库
	private String dr; // 删除标识
	private Double nonhandnum; // 库存存量
	
	public OnhandNumERP() {
		super();
	}

	public OnhandNumERP(String id) {
		super(id);
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public String getDr() {
		return dr;
	}

	public void setDr(String dr) {
		this.dr = dr;
	}

	public Double getNonhandnum() {
		return nonhandnum;
	}

	public void setNonhandnum(Double nonhandnum) {
		this.nonhandnum = nonhandnum;
	}

}