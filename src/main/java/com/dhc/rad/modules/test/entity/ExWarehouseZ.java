/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dhc.rad.common.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 出库子表Entity
 * 
 * @author maliang
 * @version 2015-11-30
 */
@Alias("TestExWarehouseZ")
public class ExWarehouseZ extends DataEntity<ExWarehouseZ> {

	private static final long serialVersionUID = 1L;
	
	// ------------------------------------关联实体-----------------------------
	private StorDoc sd;// 项目仓库
	private InvBasDoc ibd;// 物料
	private InvManDoc imd;// 物料管理档案
	private String kited;// 齐套

	public StorDoc getSd() {
		return sd;
	}

	public void setSd(StorDoc sd) {
		this.sd = sd;
	}

	public InvBasDoc getIbd() {
		return ibd;
	}

	public void setIbd(InvBasDoc ibd) {
		this.ibd = ibd;
	}

	public InvManDoc getImd() {
		return imd;
	}

	public void setImd(InvManDoc imd) {
		this.imd = imd;
	}

	public String getKited() {
		return kited;
	}

	public void setKited(String kited) {
		this.kited = kited;
	}

	// ---------------------------------------实体---------------------------
	private String parId; // 主表主键ID
	private String cinventoryid; // 存货ID
	private String vbomcode; // 成本对象
	private String csourcebillhid; // 备料计划单ID
	private Date outdate; // 出库日期
	private String cprojectid; // 项目ID
	private Double nshouldoutnum; // 应出数量
	private Double noutnum; // 实出数量
	private String pkStationid; // 工位ID（实际出料工位）
	private String pkCspaceid; // 货位ID
	private String csourcebillbid; // 备料计划单行ID
	private String ckckid; // 出库仓库ID
	private String unshelvesflag;// 下架标识（0：未下架 1：下架）
	private String zdy18;// 城轨发料号
	private String gxh; // 工序号
	private String zdy20;// 工艺文件号
	private String cbizid; // 采购员ID
	private String cproviderid; // 供应商管理档案ID
	private String invonlycode;// 出库物料唯一编码
	private String kitflag;// 齐套标识
	private String shelfType;// 货架类型 （1自动货柜 ）
	private String bljhsta;// 备料计划中的工位

	public ExWarehouseZ() {
		super();
	}

	public ExWarehouseZ(String id) {
		super(id);
	}

	public String getParId() {
		return parId;
	}

	public void setParId(String parId) {
		this.parId = parId;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public String getVbomcode() {
		return vbomcode;
	}

	public void setVbomcode(String vbomcode) {
		this.vbomcode = vbomcode;
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOutdate() {
		return outdate;
	}

	public void setOutdate(Date outdate) {
		this.outdate = outdate;
	}

	public String getCprojectid() {
		return cprojectid;
	}

	public void setCprojectid(String cprojectid) {
		this.cprojectid = cprojectid;
	}

	public Double getNshouldoutnum() {
		return nshouldoutnum;
	}

	public void setNshouldoutnum(Double nshouldoutnum) {
		this.nshouldoutnum = nshouldoutnum;
	}

	public Double getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(Double noutnum) {
		this.noutnum = noutnum;
	}

	public String getPkStationid() {
		return pkStationid;
	}

	public void setPkStationid(String pkStationid) {
		this.pkStationid = pkStationid;
	}

	public String getPkCspaceid() {
		return pkCspaceid;
	}

	public void setPkCspaceid(String pkCspaceid) {
		this.pkCspaceid = pkCspaceid;
	}

	public String getCsourcebillbid() {
		return csourcebillbid;
	}

	public void setCsourcebillbid(String csourcebillbid) {
		this.csourcebillbid = csourcebillbid;
	}

	public String getCkckid() {
		return ckckid;
	}

	public void setCkckid(String ckckid) {
		this.ckckid = ckckid;
	}

	public String getUnshelvesflag() {
		return unshelvesflag;
	}

	public void setUnshelvesflag(String unshelvesflag) {
		this.unshelvesflag = unshelvesflag;
	}

	public String getZdy18() {
		return zdy18;
	}

	public void setZdy18(String zdy18) {
		this.zdy18 = zdy18;
	}

	public String getGxh() {
		return gxh;
	}

	public void setGxh(String gxh) {
		this.gxh = gxh;
	}

	public String getZdy20() {
		return zdy20;
	}

	public void setZdy20(String zdy20) {
		this.zdy20 = zdy20;
	}

	public String getCbizid() {
		return cbizid;
	}

	public void setCbizid(String cbizid) {
		this.cbizid = cbizid;
	}

	public String getCproviderid() {
		return cproviderid;
	}

	public void setCproviderid(String cproviderid) {
		this.cproviderid = cproviderid;
	}

	public String getInvonlycode() {
		return invonlycode;
	}

	public void setInvonlycode(String invonlycode) {
		this.invonlycode = invonlycode;
	}

	public String getKitflag() {
		return kitflag;
	}

	public void setKitflag(String kitflag) {
		this.kitflag = kitflag;
	}

	public String getShelfType() {
		return shelfType;
	}

	public void setShelfType(String shelfType) {
		this.shelfType = shelfType;
	}

	public String getBljhsta() {
		return bljhsta;
	}

	public void setBljhsta(String bljhsta) {
		this.bljhsta = bljhsta;
	}

}