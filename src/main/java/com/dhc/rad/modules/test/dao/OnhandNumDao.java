/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.dao;

import java.util.List;

import com.dhc.rad.common.persistence.CrudDao;
import com.dhc.rad.common.persistence.annotation.MyBatisDao;
import com.dhc.rad.modules.test.entity.CargDoc;
import com.dhc.rad.modules.test.entity.ExWarehouseZ;
import com.dhc.rad.modules.test.entity.InvShelves;
import com.dhc.rad.modules.test.entity.OnhandNum;
import com.dhc.rad.modules.test.entity.OnhandNumERP;
import com.dhc.rad.modules.test.entity.Pickm;
import com.dhc.rad.modules.test.entity.PickmB;
import com.dhc.rad.modules.test.entity.Station;
import com.dhc.rad.modules.test.entity.UnInvShelves;

/**
 * 现存量DAO接口
 * 
 * @author maliang
 * @version 2016-06-03
 */
@MyBatisDao
public interface OnhandNumDao extends CrudDao<OnhandNum> {

	/**
	 * 工位信息
	 * @param entity
	 * @return
	 */
	public List<Station> findStationList(Station entity);

	/**
	 * 货位信息
	 * @param entity
	 * @return
	 */
	public List<CargDoc> findCargDocList(CargDoc entity);

	/**
	 * 插入现存量新表
	 * @param entity
	 * @return
	 */
	public int insertNewOnhandNum(OnhandNum entity);

	/**
	 * 更新上架表
	 * @param entity
	 * @return
	 */
	public int updateInvShelves(InvShelves entity);

	/**
	 * 更新下架表
	 * @param entity
	 * @return
	 */
	public int updateUnInvShelves(UnInvShelves entity);

	/**
	 * 出库子表数据
	 * @param entity
	 * @return
	 */
	public List<ExWarehouseZ> findExWarehouseZList(ExWarehouseZ entity);

	/**
	 * ERP现存量
	 * @param entity
	 * @return
	 */
	public List<OnhandNumERP> findOnhandNumERPList(OnhandNumERP entity);

	/**
	 * 备料计划单主表
	 * @param entity
	 * @return
	 */
	public List<Pickm> findPickmList(Pickm entity);

	/**
	 * 备料计划单子表
	 * @param entity
	 * @return
	 */
	public List<PickmB> findPickmBList(PickmB entity);

	/**
	 * 备料计划单ERP子表
	 * @param entity
	 * @return
	 */
	public List<PickmB> findPickmBERPList(PickmB entity);
	
	/**
	 * 更新备料计划单主表
	 * @param entity
	 * @return
	 */
	public int updatePickm(Pickm entity);

	/**
	 * 更新备料计划单子表
	 * @param entity
	 * @return
	 */
	public int updatePickmB(PickmB entity);

}
