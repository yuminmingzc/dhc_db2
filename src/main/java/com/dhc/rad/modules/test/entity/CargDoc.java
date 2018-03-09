/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.entity;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.dhc.rad.common.persistence.DataEntity;

/**
 * 货位档案Entity
 * @author maliang
 * @version 2015-10-21
 */
@Alias("TestCargDoc")
public class CargDoc extends DataEntity<CargDoc> {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> idList;//ID集合
	
	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}
	
	//--------------------------Entity---------------------------------
	private String code; // 编号
	
	public CargDoc() {
		super();
	}

	public CargDoc(String id){
		super(id);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}