/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import com.dhc.rad.common.persistence.DataEntity;

/**
 * 存货管理档案Entity
 * @author maliang
 * @version 2015-10-24
 */
@Alias("TestInvManDoc")
public class InvManDoc extends DataEntity<InvManDoc> {
	
	private static final long serialVersionUID = 1L;
		
	//--------------------------Entity---------------------------------
	private String pkInvbasdoc;		// 存货档案主键
	private String serialmanaflag;		// 是否进行序列号管理
	private String wholemanaflag;		// 是否批次管理
	private String instancyflag;		// 是否紧急用料
	private String shelfType;		// 货架类型
	
	public InvManDoc() {
		super();
	}

	public InvManDoc(String id){
		super(id);
	}

	@Length(min=0, max=64, message="存货档案主键长度必须介于 0 和 64 之间")
	public String getPkInvbasdoc() {
		return pkInvbasdoc;
	}

	public void setPkInvbasdoc(String pkInvbasdoc) {
		this.pkInvbasdoc = pkInvbasdoc;
	}

	@Length(min=0, max=1, message="是否进行序列号管理长度必须介于 0 和 1 之间")
	public String getSerialmanaflag() {
		return serialmanaflag;
	}

	public void setSerialmanaflag(String serialmanaflag) {
		this.serialmanaflag = serialmanaflag;
	}
	
	@Length(min=0, max=1, message="是否批次管理长度必须介于 0 和 1 之间")
	public String getWholemanaflag() {
		return wholemanaflag;
	}

	public void setWholemanaflag(String wholemanaflag) {
		this.wholemanaflag = wholemanaflag;
	}
	
	@Length(min=0, max=1, message="是否紧急用料长度必须介于 0 和 1 之间")
	public String getInstancyflag() {
		return instancyflag;
	}

	public void setInstancyflag(String instancyflag) {
		this.instancyflag = instancyflag;
	}

	public String getShelfType() {
		return shelfType;
	}

	public void setShelfType(String shelfType) {
		this.shelfType = shelfType;
	}
	
}