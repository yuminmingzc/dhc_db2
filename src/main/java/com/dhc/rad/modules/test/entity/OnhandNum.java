/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import com.dhc.rad.common.persistence.DataEntity;

/**
 * 现存量表Entity
 * 
 * @author maliang
 * @version 2015-10-21
 */
@Alias("TestOnhandNum")
public class OnhandNum extends DataEntity<OnhandNum> {

	private static final long serialVersionUID = 1L;
	
	// --------------------------自定义属性---------------------------------
	private Double currentamountMinus;//减去库存存量

	public Double getCurrentamountMinus() {
		return currentamountMinus;
	}

	public void setCurrentamountMinus(Double currentamountMinus) {
		this.currentamountMinus = currentamountMinus;
	}

	// --------------------------其他Entity---------------------------------
	private StorDoc sd;//项目仓库
	private InvManDoc imd;//物料管理档案

	public StorDoc getSd() {
		return sd;
	}

	public void setSd(StorDoc sd) {
		this.sd = sd;
	}

	public InvManDoc getImd() {
		return imd;
	}

	public void setImd(InvManDoc imd) {
		this.imd = imd;
	}

	// --------------------------Entity---------------------------------
	private String pkInvbasdoc; // 存货基本档案主键
	private String pkInvmandoc; // 存货管理档案主键
	private String sealflag; // 封存标志
	private Double safetystocknum; // 安全库存
	private Double lowstocknum; // 最低库存
	private Double maxstornum; // 最高库存
	private String pkStordoc; // 主仓库
	private String pkCorp; // 公司主键
	private Double currentamount; // 库存存量
	private String oldstore; // 老区主仓库
	private String pkStationid; // 工位信息
	private String pkGoodid; // 货位信息
	private String goodcode; // 货位编码
	private Double nochargenum;// 未出账数量
	private String cbizid;// 采购员ID
	private String cproviderid;// 供应商管理档案ID
	private Double profitlossamount;// 库存盘亏盘盈数量
	private Double snochargenum;// 未下架数量

	public OnhandNum() {
		super();
	}

	public OnhandNum(String id) {
		super(id);
	}

	@Length(min = 0, max = 64, message = "存货基本档案主键长度必须介于 0 和 64 之间")
	public String getPkInvbasdoc() {
		return pkInvbasdoc;
	}

	public void setPkInvbasdoc(String pkInvbasdoc) {
		this.pkInvbasdoc = pkInvbasdoc;
	}

	@Length(min = 0, max = 64, message = "存货管理档案主键长度必须介于 0 和 64 之间")
	public String getPkInvmandoc() {
		return pkInvmandoc;
	}

	public void setPkInvmandoc(String pkInvmandoc) {
		this.pkInvmandoc = pkInvmandoc;
	}

	@Length(min = 0, max = 1, message = "封存标志长度必须介于 0 和 1 之间")
	public String getSealflag() {
		return sealflag;
	}

	public void setSealflag(String sealflag) {
		this.sealflag = sealflag;
	}

	public Double getSafetystocknum() {
		return safetystocknum;
	}

	public void setSafetystocknum(Double safetystocknum) {
		this.safetystocknum = safetystocknum;
	}

	public Double getLowstocknum() {
		return lowstocknum;
	}

	public void setLowstocknum(Double lowstocknum) {
		this.lowstocknum = lowstocknum;
	}

	public Double getMaxstornum() {
		return maxstornum;
	}

	public void setMaxstornum(Double maxstornum) {
		this.maxstornum = maxstornum;
	}

	@Length(min = 0, max = 64, message = "主仓库长度必须介于 0 和 64 之间")
	public String getPkStordoc() {
		return pkStordoc;
	}

	public void setPkStordoc(String pkStordoc) {
		this.pkStordoc = pkStordoc;
	}

	@Length(min = 0, max = 64, message = "公司主键长度必须介于 0 和 64 之间")
	public String getPkCorp() {
		return pkCorp;
	}

	public void setPkCorp(String pkCorp) {
		this.pkCorp = pkCorp;
	}

	public Double getCurrentamount() {
		return currentamount;
	}

	public void setCurrentamount(Double currentamount) {
		this.currentamount = currentamount;
	}

	@Length(min = 0, max = 1, message = "老区主仓库长度必须介于 0 和 1 之间")
	public String getOldstore() {
		return oldstore;
	}

	public void setOldstore(String oldstore) {
		this.oldstore = oldstore;
	}

	@Length(min = 0, max = 64, message = "工位信息长度必须介于 0 和 64 之间")
	public String getPkStationid() {
		return pkStationid;
	}

	public void setPkStationid(String pkStationid) {
		this.pkStationid = pkStationid;
	}

	@Length(min = 0, max = 64, message = "货位信息长度必须介于 0 和 64 之间")
	public String getPkGoodid() {
		return pkGoodid;
	}

	public void setPkGoodid(String pkGoodid) {
		this.pkGoodid = pkGoodid;
	}

	@Length(min = 0, max = 64, message = "货位编码长度必须介于 0 和 64 之间")
	public String getGoodcode() {
		return goodcode;
	}

	public void setGoodcode(String goodcode) {
		this.goodcode = goodcode;
	}

	public Double getNochargenum() {
		return nochargenum;
	}

	public void setNochargenum(Double nochargenum) {
		this.nochargenum = nochargenum;
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

	public Double getProfitlossamount() {
		return profitlossamount;
	}

	public void setProfitlossamount(Double profitlossamount) {
		this.profitlossamount = profitlossamount;
	}

	public Double getSnochargenum() {
		return snochargenum;
	}

	public void setSnochargenum(Double snochargenum) {
		this.snochargenum = snochargenum;
	}

}