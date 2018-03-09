/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.dao;

import java.util.List;

import com.dhc.rad.common.persistence.CrudDao;
import com.dhc.rad.common.persistence.annotation.MyBatisDao;
import com.dhc.rad.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 * @author DHC
 * @version 2014-05-16
 */
@MyBatisDao
public interface DictDao extends CrudDao<Dict> {

	/**
	 * 查询字段类型列表
	 * @param dict
	 * @return
	 */
	public List<String> findTypeList(Dict entity);
	
}
