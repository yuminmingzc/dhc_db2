/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.service;

import com.dhc.rad.common.service.TreeService;
import com.dhc.rad.modules.sys.dao.OfficeDao;
import com.dhc.rad.modules.sys.entity.Office;
import com.dhc.rad.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 机构Service
 * @author DHC
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	public List<Office> findAll() {
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll) {
		if (isAll != null && isAll) {
			return UserUtils.getOfficeAllList();
		} else {
			return UserUtils.getOfficeList();
		}
	}

	@Override
    @Transactional(readOnly = true)
	public List<Office> findList(Office office) {
		if (office != null) {
			office.setParentIds(office.getParentIds() + "%");
			return dao.findByParentIdsLike(office);
		}
		return new ArrayList<Office>();
	}

	@Override
    @Transactional(readOnly = false)
	public Boolean save(Office office) {
		Boolean isSuccess =super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_ALL_LIST);
		return isSuccess;
	}

	@Override
    @Transactional(readOnly = false)
	public Boolean delete(Office office) {
		Boolean isSuccess =super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_ALL_LIST);
		return isSuccess;
	}

	public List<Office> findCompanyList() {
		return UserUtils.findCompanyList();
	}

}
