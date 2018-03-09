/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import java.util.Date;

import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import com.dhc.rad.common.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 物料上架和下架Entity
 * @author maliang
 * @version 2015-11-10
 */
@Alias("TestInvShelves")
public class InvShelves extends DataEntity<InvShelves> {
	
	private static final long serialVersionUID = 1L;
	
	private String pkOnhandnumed;//现存量表主键
	
	public String getPkOnhandnumed() {
		return pkOnhandnumed;
	}

	public void setPkOnhandnumed(String pkOnhandnumed) {
		this.pkOnhandnumed = pkOnhandnumed;
	}

	//--------------------------Entity---------------------------------
	private String goodscode;		// 货位编码
	private String csourcebillcode;		// 来源单据编码
	private String storekeeper;		// 仓库管理员
	private Double storesnum;		// 上架数量
	private String vproducenum;		// 批次号
	private String pkInvid;		// 存货ID
	private String pkStationid;		// 工位ID
	private String pkGoodsid;		// 货位ID
	private String batchsequence;		// 批次序列号
	private String cwarehouseid;		// 仓库ID
	private String invcode;		// 存货编码
	private String stationcode;		// 工位编码
	private Date storesdate;	// 上架下架日期
	private String pkOnhandnum;//现存量表主键
	
	public InvShelves() {
		super();
	}

	public InvShelves(String id){
		super(id);
	}

	@Length(min=0, max=64, message="货位编码长度必须介于 0 和 64 之间")
	public String getGoodscode() {
		return goodscode;
	}

	public void setGoodscode(String goodscode) {
		this.goodscode = goodscode;
	}
	
	@Length(min=0, max=64, message="来源单据编码长度必须介于 0 和 64 之间")
	public String getCsourcebillcode() {
		return csourcebillcode;
	}

	public void setCsourcebillcode(String csourcebillcode) {
		this.csourcebillcode = csourcebillcode;
	}
	
	@Length(min=0, max=64, message="仓库管理员长度必须介于 0 和 64 之间")
	public String getStorekeeper() {
		return storekeeper;
	}

	public void setStorekeeper(String storekeeper) {
		this.storekeeper = storekeeper;
	}
	
	public Double getStoresnum() {
		return storesnum;
	}

	public void setStoresnum(Double storesnum) {
		this.storesnum = storesnum;
	}
	
	@Length(min=0, max=64, message="批次号长度必须介于 0 和 64 之间")
	public String getVproducenum() {
		return vproducenum;
	}

	public void setVproducenum(String vproducenum) {
		this.vproducenum = vproducenum;
	}
	
	@Length(min=0, max=64, message="存货ID长度必须介于 0 和 64 之间")
	public String getPkInvid() {
		return pkInvid;
	}

	public void setPkInvid(String pkInvid) {
		this.pkInvid = pkInvid;
	}
	
	@Length(min=0, max=64, message="工位ID长度必须介于 0 和 64 之间")
	public String getPkStationid() {
		return pkStationid;
	}

	public void setPkStationid(String pkStationid) {
		this.pkStationid = pkStationid;
	}
	
	@Length(min=0, max=64, message="货位ID长度必须介于 0 和 64 之间")
	public String getPkGoodsid() {
		return pkGoodsid;
	}

	public void setPkGoodsid(String pkGoodsid) {
		this.pkGoodsid = pkGoodsid;
	}
	
	@Length(min=0, max=64, message="批次序列号长度必须介于 0 和 64 之间")
	public String getBatchsequence() {
		return batchsequence;
	}

	public void setBatchsequence(String batchsequence) {
		this.batchsequence = batchsequence;
	}
	
	@Length(min=0, max=64, message="仓库ID长度必须介于 0 和 64 之间")
	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}
	
	@Length(min=0, max=64, message="存货编码长度必须介于 0 和 64 之间")
	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}
	
	@Length(min=0, max=64, message="工位编码长度必须介于 0 和 64 之间")
	public String getStationcode() {
		return stationcode;
	}

	public void setStationcode(String stationcode) {
		this.stationcode = stationcode;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStoresdate() {
		return storesdate;
	}

	public void setStoresdate(Date storesdate) {
		this.storesdate = storesdate;
	}

	public String getPkOnhandnum() {
		return pkOnhandnum;
	}

	public void setPkOnhandnum(String pkOnhandnum) {
		this.pkOnhandnum = pkOnhandnum;
	}

}