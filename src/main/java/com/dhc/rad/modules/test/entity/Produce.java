/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import org.apache.ibatis.type.Alias;

import com.dhc.rad.common.persistence.DataEntity;

/**
 * 物料生产档案Entity
 * 
 * @author maliang
 * @version 2015-11-20
 */
@Alias("TestProduce")
public class Produce extends DataEntity<Produce> {

	private static final long serialVersionUID = 1L;

	private String pkInvbasdoc; // 存货基本档案主键
	private String pkInvmandoc; // 存货管理档案主键
	private String materstate; // 物料形态
	private String matertype; // 物料类型
	private String outtype; // 委外类型
	private String bomtype; // BOM类型
	private String roadtype; // 工艺路线类型
	private String materclass; // 物料分类
	private String sfcbdx; // 是否成本对象
	private String issend; // 是否发料
	private String stockbycheck; // 是否必须依据检验结果入库
	private String chkfreeflag; // 是否免检
	private String sealflag; // 封存标志
	private Double safetystocknum; // 安全库存
	private Double lowstocknum; // 最低库存
	private Double maxstornum; // 最高库存
	private Double ckcb; // 参考成本
	private String pricemethod; // 记价方式
	private String pkStordoc; // 主仓库
	private String sfpchs; // 是否批次核算
	private String pkCalbody; // 库存组织主键
	private String pkCorp; // 公司主键
	private String isused; // 是否出入库
	private String outflag; // 是否委外
	private String roadflag; // 是否使用工艺路线
	private String coststatflag; // 是否按成本中心统计产量
	private String oldstore; // 老区主仓库
	private String stationcode; // 工位信息
	private String cspacecode; // 货位信息

	public Produce() {
		super();
	}

	public Produce(String id) {
		super(id);
	}

	public String getPkInvbasdoc() {
		return pkInvbasdoc;
	}

	public void setPkInvbasdoc(String pkInvbasdoc) {
		this.pkInvbasdoc = pkInvbasdoc;
	}

	public String getPkInvmandoc() {
		return pkInvmandoc;
	}

	public void setPkInvmandoc(String pkInvmandoc) {
		this.pkInvmandoc = pkInvmandoc;
	}

	public String getMaterstate() {
		return materstate;
	}

	public void setMaterstate(String materstate) {
		this.materstate = materstate;
	}

	public String getMatertype() {
		return matertype;
	}

	public void setMatertype(String matertype) {
		this.matertype = matertype;
	}

	public String getOuttype() {
		return outtype;
	}

	public void setOuttype(String outtype) {
		this.outtype = outtype;
	}

	public String getBomtype() {
		return bomtype;
	}

	public void setBomtype(String bomtype) {
		this.bomtype = bomtype;
	}

	public String getRoadtype() {
		return roadtype;
	}

	public void setRoadtype(String roadtype) {
		this.roadtype = roadtype;
	}

	public String getMaterclass() {
		return materclass;
	}

	public void setMaterclass(String materclass) {
		this.materclass = materclass;
	}

	public String getSfcbdx() {
		return sfcbdx;
	}

	public void setSfcbdx(String sfcbdx) {
		this.sfcbdx = sfcbdx;
	}

	public String getIssend() {
		return issend;
	}

	public void setIssend(String issend) {
		this.issend = issend;
	}

	public String getStockbycheck() {
		return stockbycheck;
	}

	public void setStockbycheck(String stockbycheck) {
		this.stockbycheck = stockbycheck;
	}

	public String getChkfreeflag() {
		return chkfreeflag;
	}

	public void setChkfreeflag(String chkfreeflag) {
		this.chkfreeflag = chkfreeflag;
	}

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

	public Double getCkcb() {
		return ckcb;
	}

	public void setCkcb(Double ckcb) {
		this.ckcb = ckcb;
	}

	public String getPricemethod() {
		return pricemethod;
	}

	public void setPricemethod(String pricemethod) {
		this.pricemethod = pricemethod;
	}

	public String getPkStordoc() {
		return pkStordoc;
	}

	public void setPkStordoc(String pkStordoc) {
		this.pkStordoc = pkStordoc;
	}

	public String getSfpchs() {
		return sfpchs;
	}

	public void setSfpchs(String sfpchs) {
		this.sfpchs = sfpchs;
	}

	public String getPkCalbody() {
		return pkCalbody;
	}

	public void setPkCalbody(String pkCalbody) {
		this.pkCalbody = pkCalbody;
	}

	public String getPkCorp() {
		return pkCorp;
	}

	public void setPkCorp(String pkCorp) {
		this.pkCorp = pkCorp;
	}

	public String getIsused() {
		return isused;
	}

	public void setIsused(String isused) {
		this.isused = isused;
	}

	public String getOutflag() {
		return outflag;
	}

	public void setOutflag(String outflag) {
		this.outflag = outflag;
	}

	public String getRoadflag() {
		return roadflag;
	}

	public void setRoadflag(String roadflag) {
		this.roadflag = roadflag;
	}

	public String getCoststatflag() {
		return coststatflag;
	}

	public void setCoststatflag(String coststatflag) {
		this.coststatflag = coststatflag;
	}

	public String getOldstore() {
		return oldstore;
	}

	public void setOldstore(String oldstore) {
		this.oldstore = oldstore;
	}

	public String getStationcode() {
		return stationcode;
	}

	public void setStationcode(String stationcode) {
		this.stationcode = stationcode;
	}

	public String getCspacecode() {
		return cspacecode;
	}

	public void setCspacecode(String cspacecode) {
		this.cspacecode = cspacecode;
	}

}