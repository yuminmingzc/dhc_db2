/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.common.service;

import com.dhc.rad.common.persistence.CrudDao;
import com.dhc.rad.common.persistence.DataEntity;
import com.dhc.rad.common.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service基类
 * @author DHC
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends DataEntity<T>> extends BaseService {

	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(String id) {
		return dao.get(id);
	}

	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}

	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	/**
	 * 查询分页数据
	 * @param page   分页对象
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public Boolean save(T entity) {
		int successCount;
		if (entity.getIsNewRecord()) {
			entity.preInsert();
			successCount = dao.insert(entity);
		} else {
			entity.preUpdate();
			successCount = dao.update(entity);
		}
		return (successCount == 1);
	}

	/**
	 * 删除数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public Boolean delete(T entity) {
		int successCount = dao.delete(entity);
		return (successCount == 1);
	}

	//错误信息
	//使用前在controller先初始化为： 数据异常！
	protected String errmsg="数据异常！";

	@Override
    public String getErrmsg() {
		return errmsg;
	}

	@Override
    public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
