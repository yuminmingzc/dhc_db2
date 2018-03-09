/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhc.rad.common.persistence.Page;
import com.dhc.rad.common.service.BaseService;
import com.dhc.rad.common.utils.CacheUtils;
import com.dhc.rad.modules.sys.dao.DictDao;
import com.dhc.rad.modules.sys.entity.Dict;
import com.dhc.rad.modules.sys.utils.DictUtils;

/**
 * 字典Service
 * @author DHC
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class DictService extends BaseService {
	
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DictDao dao;
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public Dict findInfo(Dict entity) {
		return dao.findInfo(entity);
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<Dict> findPage(Page<Dict> page, Dict entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean save(Dict entity) {
		boolean b = false;
		if (entity.getIsNewRecord()){
			entity.preInsert();
			dao.insert(entity);
		}else{
			entity.preUpdate();
			dao.update(entity);
		}
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
		b = true;
		return b;
	}

	/**
	 * 删除数据
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean delete(String[] ids) {
		boolean b = false;
		if (ids == null) {
			return b;
		}
		for (String idItem : ids) {
			Dict dict = new Dict(idItem);
			dao.delete(dict);
		}
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
		b = true;
		return b;
	}
	
}
