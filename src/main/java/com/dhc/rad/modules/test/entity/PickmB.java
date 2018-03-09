/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.dhc.rad.common.persistence.DataEntity;

/**
 * 备料计划单体Entity
 * 
 * @author maliang
 * @version 2015-11-23
 */
@Alias("TestPickmB")
public class PickmB extends DataEntity<PickmB> {

	private static final long serialVersionUID = 1L;

	// --------------------------自定义属性---------------------------------
	private List<String> pkPickmidList;
	private String ljckslbfg;

	public List<String> getPkPickmidList() {
		return pkPickmidList;
	}

	public void setPkPickmidList(List<String> pkPickmidList) {
		this.pkPickmidList = pkPickmidList;
	}
	
	public String getLjckslbfg() {
		return ljckslbfg;
	}

	public void setLjckslbfg(String ljckslbfg) {
		this.ljckslbfg = ljckslbfg;
	}

	// --------------------------其他Entity---------------------------------
	private Produce produce;//物料生产档案

	public Produce getProduce() {
		return produce;
	}

	public void setProduce(Produce produce) {
		this.produce = produce;
	}

	// --------------------------Entity---------------------------------
	private String ckckid; // 出库仓库ID
	private String gzzxid; // 工作中心ID
	private String pkPickmid; // 备料计划单头主键
	private String pkProduce; // 物料PK
	private Double deyl;// 定额用量
	private Double dwde;// 单位定额
	private Double jhcksl;// 计划出库数量
	private Double ljcksl;// 累计出库数量

	public PickmB() {
		super();
	}

	public PickmB(String id) {
		super(id);
	}

	public String getCkckid() {
		return ckckid;
	}

	public void setCkckid(String ckckid) {
		this.ckckid = ckckid;
	}

	public String getGzzxid() {
		return gzzxid;
	}

	public void setGzzxid(String gzzxid) {
		this.gzzxid = gzzxid;
	}

	public String getPkPickmid() {
		return pkPickmid;
	}

	public void setPkPickmid(String pkPickmid) {
		this.pkPickmid = pkPickmid;
	}

	public String getPkProduce() {
		return pkProduce;
	}

	public void setPkProduce(String pkProduce) {
		this.pkProduce = pkProduce;
	}

	public Double getDeyl() {
		return deyl;
	}

	public void setDeyl(Double deyl) {
		this.deyl = deyl;
	}

	public Double getDwde() {
		return dwde;
	}

	public void setDwde(Double dwde) {
		this.dwde = dwde;
	}

	public Double getJhcksl() {
		return jhcksl;
	}

	public void setJhcksl(Double jhcksl) {
		this.jhcksl = jhcksl;
	}

	public Double getLjcksl() {
		return ljcksl;
	}

	public void setLjcksl(Double ljcksl) {
		this.ljcksl = ljcksl;
	}

}