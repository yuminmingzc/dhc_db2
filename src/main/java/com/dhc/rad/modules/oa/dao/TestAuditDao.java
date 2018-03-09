/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.oa.dao;

import com.dhc.rad.common.persistence.CrudDao;
import com.dhc.rad.common.persistence.annotation.MyBatisDao;
import com.dhc.rad.modules.oa.entity.TestAudit;

/**
 * 审批DAO接口
 * @author DHC
 * @version 2014-05-16
 */
@MyBatisDao
public interface TestAuditDao extends CrudDao<TestAudit> {

	TestAudit getByProcInsId(String procInsId);
	
	int updateInsId(TestAudit testAudit);
	
	int updateHrText(TestAudit testAudit);
	
	int updateLeadText(TestAudit testAudit);
	
	int updateMainLeadText(TestAudit testAudit);
	
}
