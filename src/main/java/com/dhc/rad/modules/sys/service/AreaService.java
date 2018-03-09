/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.service;

import com.dhc.rad.common.service.TreeService;
import com.dhc.rad.modules.sys.dao.AreaDao;
import com.dhc.rad.modules.sys.entity.Area;
import com.dhc.rad.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域Service
 * @author DHC
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	@Override
    @Transactional(readOnly = false)
	public Boolean save(Area area) {
		Boolean isSuccess =super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		return isSuccess;
	}
	
	@Override
    @Transactional(readOnly = false)
	public Boolean delete(Area area) {
		Boolean isSuccess =super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		return isSuccess;
	}
	
}
