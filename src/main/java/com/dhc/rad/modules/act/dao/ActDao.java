/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.act.dao;

import com.dhc.rad.common.persistence.CrudDao;
import com.dhc.rad.common.persistence.annotation.MyBatisDao;
import com.dhc.rad.modules.act.entity.Act;

/**
 * 审批DAO接口
 * @author DHC
 * @version 2014-05-16
 */
@MyBatisDao
public interface ActDao extends CrudDao<Act> {

	int updateProcInsIdByBusinessId(Act act);
	
}
