/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.dhc.rad.common.persistence.DataEntity;

/**
 * 备料计划Entity
 * 
 * @author maliang
 * @version 2015-11-23
 */
@Alias("TestPickm")
public class Pickm extends DataEntity<Pickm> {

	private static final long serialVersionUID = 1L;
	
	// --------------------------自定义属性---------------------------------
	private List<String> bljhdhList;

	public List<String> getBljhdhList() {
		return bljhdhList;
	}

	public void setBljhdhList(List<String> bljhdhList) {
		this.bljhdhList = bljhdhList;
	}

	// --------------------------Entity---------------------------------
	private String bljhdh; // 备料计划单号
	private String zdy9; // 流水号
	private String zdy17; // 台位
	private String zdy3; // 成本对象
	private String bljhlx;// 备料计划单类型
	private String pkJobmngfil;// 项目管理档案主键
	private String zt;//状态(A计划 B审核 C完成)
	private String sflbid;//收发类别

	public Pickm() {
		super();
	}

	public Pickm(String id) {
		super(id);
	}

	public String getBljhdh() {
		return bljhdh;
	}

	public void setBljhdh(String bljhdh) {
		this.bljhdh = bljhdh;
	}

	public String getZdy9() {
		return zdy9;
	}

	public void setZdy9(String zdy9) {
		this.zdy9 = zdy9;
	}

	public String getZdy17() {
		return zdy17;
	}

	public void setZdy17(String zdy17) {
		this.zdy17 = zdy17;
	}

	public String getZdy3() {
		return zdy3;
	}

	public void setZdy3(String zdy3) {
		this.zdy3 = zdy3;
	}

	public String getBljhlx() {
		return bljhlx;
	}

	public void setBljhlx(String bljhlx) {
		this.bljhlx = bljhlx;
	}

	public String getPkJobmngfil() {
		return pkJobmngfil;
	}

	public void setPkJobmngfil(String pkJobmngfil) {
		this.pkJobmngfil = pkJobmngfil;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getSflbid() {
		return sflbid;
	}

	public void setSflbid(String sflbid) {
		this.sflbid = sflbid;
	}

}