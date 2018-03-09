/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import com.dhc.rad.common.persistence.DataEntity;

/**
 * 存货基本档案Entity
 * @author maliang
 * @version 2015-10-13
 */
@Alias("TestInvBasDoc")
public class InvBasDoc extends DataEntity<InvBasDoc> {
	
	private static final long serialVersionUID = 1L;

	//--------------------------Entity---------------------------------
	private String invcode;		// 存货编码
	private String invname;		// 存货名称
	private String invspec;		// 规格
	private String invtype;		// 型号
	private String pkMeasdoc;		// 主计量单位主键
	
	public InvBasDoc() {
		super();
	}

	public InvBasDoc(String id){
		super(id);
	}

	@Length(min=0, max=64, message="存货编码长度必须介于 0 和 64 之间")
	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}
	
	@Length(min=0, max=256, message="存货名称长度必须介于 0 和 256 之间")
	public String getInvname() {
		return invname;
	}

	public void setInvname(String invname) {
		this.invname = invname;
	}

	@Length(min=0, max=256, message="规格长度必须介于 0 和 256 之间")
	public String getInvspec() {
		return invspec;
	}

	public void setInvspec(String invspec) {
		this.invspec = invspec;
	}
	
	@Length(min=0, max=256, message="型号长度必须介于 0 和 256 之间")
	public String getInvtype() {
		return invtype;
	}

	public void setInvtype(String invtype) {
		this.invtype = invtype;
	}
	
	@Length(min=0, max=64, message="主计量单位主键长度必须介于 0 和 64 之间")
	public String getPkMeasdoc() {
		return pkMeasdoc;
	}

	public void setPkMeasdoc(String pkMeasdoc) {
		this.pkMeasdoc = pkMeasdoc;
	}

}