/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.test.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhc.rad.common.service.BaseService;
import com.dhc.rad.modules.test.dao.OnhandNumDao;
import com.dhc.rad.modules.test.entity.CargDoc;
import com.dhc.rad.modules.test.entity.ExWarehouseZ;
import com.dhc.rad.modules.test.entity.InvManDoc;
import com.dhc.rad.modules.test.entity.InvShelves;
import com.dhc.rad.modules.test.entity.OnhandNum;
import com.dhc.rad.modules.test.entity.OnhandNumERP;
import com.dhc.rad.modules.test.entity.Pickm;
import com.dhc.rad.modules.test.entity.PickmB;
import com.dhc.rad.modules.test.entity.Produce;
import com.dhc.rad.modules.test.entity.Station;
import com.dhc.rad.modules.test.entity.StorDoc;
import com.dhc.rad.modules.test.entity.UnInvShelves;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 现存量Service
 * 
 * @author maliang
 * @version 2016-06-03
 */
@Service
@Transactional(readOnly = true)
public class OnhandNumService extends BaseService {

	/**
	 * 持久层对象
	 */
	@Autowired
	protected OnhandNumDao dao;

	/**
	 * 保存数据（插入或更新）
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean changeOnhandNumTable() {
		boolean b = false;
		//自动货柜固定工位检测
		Station staWhereTmp = new Station();
		staWhereTmp.setGzzxbm("SLPWKGW");
		staWhereTmp.setDelFlag(Station.DEL_FLAG_NORMAL);
		List<Station> staListTmp = dao.findStationList(staWhereTmp);
		if (staListTmp == null || staListTmp.size() < 1) {
			return b;
		} else if (staListTmp.size() > 1) {
			return b;
		}
		Station staInfoSLPWKTmp = staListTmp.get(0);
		
		//自动货柜固定货位检测
		CargDoc cdWhereTmp = new CargDoc();
		cdWhereTmp.setCode("WK0101");
		cdWhereTmp.setDelFlag(CargDoc.DEL_FLAG_NORMAL);
		List<CargDoc> cdListTmp = dao.findCargDocList(cdWhereTmp);
		if (cdListTmp == null || cdListTmp.size() < 1) {
			return b;
		} else if (cdListTmp.size() > 1) {
			return b;
		}
		CargDoc cdInfoSLPWKTmp = cdListTmp.get(0);
		
		OnhandNum ohnWhere = new OnhandNum();
		ohnWhere.setDelFlag(OnhandNum.DEL_FLAG_NORMAL);
		StorDoc sd = new StorDoc();
		sd.setDelFlag(StorDoc.DEL_FLAG_NORMAL);
		ohnWhere.setSd(sd);
		InvManDoc imd = new InvManDoc();
		imd.setDelFlag(InvManDoc.DEL_FLAG_NORMAL);
		ohnWhere.setImd(imd);
		List<OnhandNum> ohnList = dao.findList(ohnWhere);//现存量表全部未删除数据
		Map<String,OnhandNum> ohnNewAddMap = Maps.newHashMap();//新的现存量表数据
		List<InvShelves> isUpdateList = Lists.newArrayList();//更新上架表
		List<UnInvShelves> unisUpdateList = Lists.newArrayList();//更新下架表
		
		String slpwkType = "1";
		for (OnhandNum ohnItem : ohnList) {
			if (ohnItem.getSafetystocknum() == null) {
				ohnItem.setSafetystocknum(0.0);
			}
			if (ohnItem.getLowstocknum() == null) {
				ohnItem.setLowstocknum(0.0);
			}
			if (ohnItem.getMaxstornum() == null) {
				ohnItem.setMaxstornum(0.0);
			}
			if (ohnItem.getCurrentamount() == null) {
				ohnItem.setCurrentamount(0.0);
			}
			if (ohnItem.getNochargenum() == null) {
				ohnItem.setNochargenum(0.0);
			}
			if (ohnItem.getProfitlossamount() == null) {
				ohnItem.setProfitlossamount(0.0);
			}
			if (ohnItem.getSnochargenum() == null) {
				ohnItem.setSnochargenum(0.0);
			}
			if (ohnItem.getImd() == null || ohnItem.getSd() == null) {
				return b;
			}
			boolean slpwkFlag = false;
			//自动货柜
			if (slpwkType.equals(ohnItem.getSd().getShelfType()) && slpwkType.equals(ohnItem.getImd().getShelfType())) {
				slpwkFlag = true;
			}
			StringBuffer strKeyBuffer = new StringBuffer();
			strKeyBuffer.append(ohnItem.getPkStordoc());//项目仓库
			if (slpwkFlag) {
				strKeyBuffer.append(staInfoSLPWKTmp.getId());//工位
			} else {
				strKeyBuffer.append(ohnItem.getPkStationid());//工位
			}
			strKeyBuffer.append(ohnItem.getPkInvbasdoc());//物料
			String strKey = strKeyBuffer.toString();
			if (ohnNewAddMap.containsKey(strKey)) {
				OnhandNum ohnAddTmp = ohnNewAddMap.get(strKey);
				ohnAddTmp.setSafetystocknum(ohnItem.getSafetystocknum() + ohnAddTmp.getSafetystocknum()); // 安全库存
				ohnAddTmp.setLowstocknum(ohnItem.getLowstocknum() + ohnAddTmp.getLowstocknum()); // 最低库存
				ohnAddTmp.setMaxstornum(ohnItem.getMaxstornum() + ohnAddTmp.getMaxstornum()); // 最高库存
				ohnAddTmp.setCurrentamount(ohnItem.getCurrentamount() + ohnAddTmp.getCurrentamount()); // 库存存量
				ohnAddTmp.setNochargenum(ohnItem.getNochargenum() + ohnAddTmp.getNochargenum());// 未出账数量
				ohnAddTmp.setProfitlossamount(ohnItem.getProfitlossamount() + ohnAddTmp.getProfitlossamount());// 库存盘亏盘盈数量
				ohnAddTmp.setSnochargenum(ohnItem.getSnochargenum() + ohnAddTmp.getSnochargenum());// 未下架数量
				
				InvShelves isUpdate = new InvShelves();
				isUpdate.setPkOnhandnumed(ohnItem.getId());
				isUpdate.setPkOnhandnum(ohnAddTmp.getId());
				isUpdate.setDelFlag(InvShelves.DEL_FLAG_NORMAL);
				isUpdateList.add(isUpdate);
				
				UnInvShelves unisUpdate = new UnInvShelves();
				unisUpdate.setPkOnhandnumed(ohnItem.getId());
				unisUpdate.setPkOnhandnum(ohnAddTmp.getId());
				unisUpdate.setDelFlag(UnInvShelves.DEL_FLAG_NORMAL);
				unisUpdateList.add(unisUpdate);
			} else {
				OnhandNum ohnAddTmp = new OnhandNum();
				ohnAddTmp.setId(ohnItem.getId());
				ohnAddTmp.setCreateBy(ohnItem.getCreateBy());
				ohnAddTmp.setCreateDate(ohnItem.getCreateDate());
				ohnAddTmp.setUpdateBy(ohnItem.getUpdateBy());
				ohnAddTmp.setUpdateDate(ohnItem.getUpdateDate());
				ohnAddTmp.setRemarks(ohnItem.getRemarks());
				
				ohnAddTmp.setPkInvbasdoc(ohnItem.getPkInvbasdoc()); // 存货基本档案主键
				ohnAddTmp.setPkInvmandoc(ohnItem.getPkInvmandoc()); // 存货管理档案主键
				ohnAddTmp.setSealflag(ohnItem.getSealflag()); // 封存标志
				ohnAddTmp.setSafetystocknum(ohnItem.getSafetystocknum()); // 安全库存
				ohnAddTmp.setLowstocknum(ohnItem.getLowstocknum()); // 最低库存
				ohnAddTmp.setMaxstornum(ohnItem.getMaxstornum()); // 最高库存
				ohnAddTmp.setPkStordoc(ohnItem.getPkStordoc()); // 主仓库
				ohnAddTmp.setPkCorp(ohnItem.getPkCorp()); // 公司主键
				ohnAddTmp.setCurrentamount(ohnItem.getCurrentamount()); // 库存存量
				ohnAddTmp.setOldstore(ohnItem.getOldstore()); // 老区主仓库
				if (slpwkFlag) {
					ohnAddTmp.setPkStationid(staInfoSLPWKTmp.getId()); // 工位信息
					ohnAddTmp.setPkGoodid(cdInfoSLPWKTmp.getId()); // 货位信息
					ohnAddTmp.setGoodcode(cdInfoSLPWKTmp.getCode()); // 货位编码
				} else {
					ohnAddTmp.setPkStationid(ohnItem.getPkStationid()); // 工位信息
					ohnAddTmp.setPkGoodid(ohnItem.getPkGoodid()); // 货位信息
					ohnAddTmp.setGoodcode(ohnItem.getGoodcode()); // 货位编码
				}
				ohnAddTmp.setNochargenum(ohnItem.getNochargenum());// 未出账数量
				ohnAddTmp.setCbizid(ohnItem.getCbizid());// 采购员ID
				ohnAddTmp.setCproviderid(ohnItem.getCproviderid());// 供应商管理档案ID
				ohnAddTmp.setProfitlossamount(ohnItem.getProfitlossamount());// 库存盘亏盘盈数量
				ohnAddTmp.setSnochargenum(ohnItem.getSnochargenum());// 未下架数量
				
				ohnNewAddMap.put(strKey, ohnAddTmp);
			}

		}
		
		for (String item : ohnNewAddMap.keySet()) {
			dao.insertNewOnhandNum(ohnNewAddMap.get(item));
		}
		for (InvShelves item : isUpdateList) {
			dao.updateInvShelves(item);
		}
		for (UnInvShelves item : unisUpdateList) {
			dao.updateUnInvShelves(item);
		}
		
		logger.info("现存量表（" + ohnNewAddMap.size() + "）");
		logger.info("上架表（" + isUpdateList.size() + "）");
		logger.info("下架表（" + unisUpdateList.size() + "）");
		b = true;
		return b;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean changeNochargenum() {
		boolean b = false;
		//自动货柜固定工位检测
		Station staWhereTmp = new Station();
		staWhereTmp.setGzzxbm("SLPWKGW");
		staWhereTmp.setDelFlag(Station.DEL_FLAG_NORMAL);
		List<Station> staListTmp = dao.findStationList(staWhereTmp);
		if (staListTmp == null || staListTmp.size() < 1) {
			return b;
		} else if (staListTmp.size() > 1) {
			return b;
		}
		Station staInfoSLPWKTmp = staListTmp.get(0);
				
		//查找未齐套的出库单
		ExWarehouseZ exwhzWhere = new ExWarehouseZ();
		exwhzWhere.setKited("NO");
		exwhzWhere.setDelFlag(ExWarehouseZ.DEL_FLAG_NORMAL);
		StorDoc sd = new StorDoc();
		sd.setDelFlag(StorDoc.DEL_FLAG_NORMAL);
		exwhzWhere.setSd(sd);
		InvManDoc imd = new InvManDoc();
		imd.setDelFlag(InvManDoc.DEL_FLAG_NORMAL);
		exwhzWhere.setImd(imd);
		List<ExWarehouseZ> exwhzList = dao.findExWarehouseZList(exwhzWhere);
		Map<String,OnhandNum> ohnUpdateMap = Maps.newHashMap();//需要更新的现存量表数据
		String slpwkType = "1";
		for (ExWarehouseZ item : exwhzList) {
			boolean slpwkFlag = false;
			//自动货柜
			if (slpwkType.equals(item.getSd().getShelfType()) && slpwkType.equals(item.getImd().getShelfType())) {
				slpwkFlag = true;
			}
			StringBuffer strKeyBuffer = new StringBuffer();
			strKeyBuffer.append(item.getCkckid());//项目仓库
			if (slpwkFlag) {
				strKeyBuffer.append(staInfoSLPWKTmp.getId());//工位
			} else {
				strKeyBuffer.append(item.getPkStationid());//工位
			}
			strKeyBuffer.append(item.getCinventoryid());//物料（管理档案ID）
			String strKey = strKeyBuffer.toString();
			if (ohnUpdateMap.containsKey(strKey)) {
				OnhandNum ohnUpdateTmp = ohnUpdateMap.get(strKey);
				ohnUpdateTmp.setNochargenum(item.getNoutnum() + ohnUpdateTmp.getNochargenum());// 未出账数量
			} else {
				OnhandNum ohnUpdateTmp = new OnhandNum();
				ohnUpdateTmp.setPkInvmandoc(item.getCinventoryid()); // 存货管理档案主键
				ohnUpdateTmp.setPkStordoc(item.getCkckid()); // 主仓库
				if (slpwkFlag) {
					ohnUpdateTmp.setPkStationid(staInfoSLPWKTmp.getId()); // 工位信息
				} else {
					ohnUpdateTmp.setPkStationid(item.getPkStationid()); // 工位信息
				}
				ohnUpdateTmp.setNochargenum(item.getNoutnum());// 未出账数量
				ohnUpdateTmp.preUpdate();
				
				ohnUpdateMap.put(strKey, ohnUpdateTmp);
			}

		}
		StringBuffer strMsg = new StringBuffer();
		int count = 0;
		for (String item : ohnUpdateMap.keySet()) {
			OnhandNum ohnUpdateTmp = ohnUpdateMap.get(item);
			int res = dao.update(ohnUpdateTmp);
			if (res < 1) {
				count++;
				strMsg.append(ohnUpdateTmp.getPkInvmandoc());
				strMsg.append("	");
				strMsg.append(ohnUpdateTmp.getPkStationid());
				strMsg.append("	");
				strMsg.append(ohnUpdateTmp.getPkStordoc());
				strMsg.append("	");
				strMsg.append(ohnUpdateTmp.getNochargenum());
				strMsg.append("\n");
			}
		}
		logger.info("现存量表（" + ohnUpdateMap.size() + "）");
		logger.info("未更新（" + count + "）");
		logger.info(strMsg.toString());
		b = true;
		return b;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean changeSnochargenum() {
		boolean b = false;
		//自动货柜固定工位检测
		Station staWhereTmp = new Station();
		staWhereTmp.setGzzxbm("SLPWKGW");
		staWhereTmp.setDelFlag(Station.DEL_FLAG_NORMAL);
		List<Station> staListTmp = dao.findStationList(staWhereTmp);
		if (staListTmp == null || staListTmp.size() < 1) {
			return b;
		} else if (staListTmp.size() > 1) {
			return b;
		}
		Station staInfoSLPWKTmp = staListTmp.get(0);
				
		//查找未下架的出库单
		ExWarehouseZ exwhzWhere = new ExWarehouseZ();
		exwhzWhere.setUnshelvesflag("0");
		exwhzWhere.setDelFlag(ExWarehouseZ.DEL_FLAG_NORMAL);
		StorDoc sd = new StorDoc();
		sd.setDelFlag(StorDoc.DEL_FLAG_NORMAL);
		exwhzWhere.setSd(sd);
		InvManDoc imd = new InvManDoc();
		imd.setDelFlag(InvManDoc.DEL_FLAG_NORMAL);
		exwhzWhere.setImd(imd);
		List<ExWarehouseZ> exwhzList = dao.findExWarehouseZList(exwhzWhere);
		Map<String,OnhandNum> ohnUpdateMap = Maps.newHashMap();//需要更新的现存量表数据
		String slpwkType = "1";
		for (ExWarehouseZ item : exwhzList) {
			boolean slpwkFlag = false;
			//自动货柜
			if (slpwkType.equals(item.getSd().getShelfType()) && slpwkType.equals(item.getImd().getShelfType())) {
				slpwkFlag = true;
			}
			StringBuffer strKeyBuffer = new StringBuffer();
			strKeyBuffer.append(item.getCkckid());//项目仓库
			if (slpwkFlag) {
				strKeyBuffer.append(staInfoSLPWKTmp.getId());//工位
			} else {
				strKeyBuffer.append(item.getPkStationid());//工位
			}
			strKeyBuffer.append(item.getCinventoryid());//物料（管理档案ID）
			String strKey = strKeyBuffer.toString();
			if (ohnUpdateMap.containsKey(strKey)) {
				OnhandNum ohnUpdateTmp = ohnUpdateMap.get(strKey);
				ohnUpdateTmp.setSnochargenum(item.getNoutnum() + ohnUpdateTmp.getSnochargenum());// 未下架数量
			} else {
				OnhandNum ohnUpdateTmp = new OnhandNum();
				ohnUpdateTmp.setPkInvmandoc(item.getCinventoryid()); // 存货管理档案主键
				ohnUpdateTmp.setPkStordoc(item.getCkckid()); // 主仓库
				if (slpwkFlag) {
					ohnUpdateTmp.setPkStationid(staInfoSLPWKTmp.getId()); // 工位信息
				} else {
					ohnUpdateTmp.setPkStationid(item.getPkStationid()); // 工位信息
				}
				ohnUpdateTmp.setSnochargenum(item.getNoutnum());// 未出账数量
				ohnUpdateTmp.preUpdate();
				
				ohnUpdateMap.put(strKey, ohnUpdateTmp);
			}

		}
		StringBuffer strMsg = new StringBuffer();
		int count = 0;
		for (String item : ohnUpdateMap.keySet()) {
			OnhandNum ohnUpdateTmp = ohnUpdateMap.get(item);
			int res = dao.update(ohnUpdateTmp);
			if (res < 1) {
				count++;
				strMsg.append(ohnUpdateTmp.getPkInvmandoc());
				strMsg.append("	");
				strMsg.append(ohnUpdateTmp.getPkStationid());
				strMsg.append("	");
				strMsg.append(ohnUpdateTmp.getPkStordoc());
				strMsg.append("	");
				strMsg.append(ohnUpdateTmp.getNochargenum());
				strMsg.append("\n");
			}
		}
		logger.info("现存量表（" + ohnUpdateMap.size() + "）");
		logger.info("未更新（" + count + "）");
		logger.info(strMsg.toString());
		b = true;
		return b;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean changeProfitlossamount() {
		boolean b = false;
		//1、查询现存量表
		OnhandNum ohnWhere = new OnhandNum();
//		ohnWhere.setId("BE450C358B0840A8BDBCBD783280B336");
		ohnWhere.setDelFlag(OnhandNum.DEL_FLAG_NORMAL);
		List<OnhandNum> ohnList = dao.findList(ohnWhere);
		//2、遍历现存量表，根据项目仓库ID，和物料ID，查询ERP存量和L2M存量
		Map<String,String> sdInvMap = Maps.newHashMap();
		List<OnhandNum> ohnUpdateList = Lists.newArrayList();
		
		int wqppERP = 0;
		int invNum = 0;
		int pkwlNum = 0;
		int pywlNum = 0;
		int noERPNum = 0;
		for (OnhandNum ohnItem : ohnList) {
			if (ohnItem.getCurrentamount() == null) {
				ohnItem.setCurrentamount(0.0);
			}
			if (ohnItem.getNochargenum() == null) {
				ohnItem.setNochargenum(0.0);
			}
			if (ohnItem.getSnochargenum() == null) {
				ohnItem.setSnochargenum(0.0);
			}
			StringBuffer keyBuffTmp = new StringBuffer();
			keyBuffTmp.append(ohnItem.getPkStordoc());
			keyBuffTmp.append(ohnItem.getPkInvbasdoc());
			String keyTmp = keyBuffTmp.toString();
			if (!sdInvMap.containsKey(keyTmp)) {
				invNum++;
				sdInvMap.put(keyTmp, keyTmp);
				OnhandNum ohnWhereTmp = new OnhandNum();
				ohnWhereTmp.setPkStordoc(ohnItem.getPkStordoc());
				ohnWhereTmp.setPkInvbasdoc(ohnItem.getPkInvbasdoc());
				ohnWhereTmp.setDelFlag(OnhandNum.DEL_FLAG_NORMAL);
				List<OnhandNum> ohnListTmp = dao.findList(ohnWhereTmp);
				double currentamountTmp = 0;
				double nochargenumTmp = 0;
				double snochargenumTmp = 0;
				for (OnhandNum itemTmp : ohnListTmp) {
					if (itemTmp.getCurrentamount() == null) {
						itemTmp.setCurrentamount(0.0);
					}
					if (itemTmp.getNochargenum() == null) {
						itemTmp.setNochargenum(0.0);
					}
					if (itemTmp.getSnochargenum() == null) {
						itemTmp.setSnochargenum(0.0);
					}
					currentamountTmp = currentamountTmp + itemTmp.getCurrentamount();
					nochargenumTmp = nochargenumTmp + itemTmp.getNochargenum();
					snochargenumTmp = snochargenumTmp + itemTmp.getSnochargenum();
				}
				OnhandNumERP ohnERPWhere = new OnhandNumERP();
				ohnERPWhere.setCinvbasid(ohnItem.getPkInvbasdoc());
				ohnERPWhere.setCwarehouseid(ohnItem.getPkStordoc());
				ohnERPWhere.setDr("0");
				List<OnhandNumERP> ohnERPList = dao.findOnhandNumERPList(ohnERPWhere);
				double erpCuNum = 0;
				if (ohnERPList == null || ohnERPList.size() < 1) {
					noERPNum++;
//					logger.info("ERP无此存量：" + ohnItem.getId());
//					return b;
				} else if (ohnERPList.size() > 0) {
//					logger.info("ERP存量有多条：" + ohnItem.getId());
//					return b;
					for (OnhandNumERP itemTmp : ohnERPList) {
						erpCuNum = erpCuNum + itemTmp.getNonhandnum();
					}
				}
				//3、比较ERP存量和L2M存量
				double profitlossamountTmp = currentamountTmp - (nochargenumTmp - snochargenumTmp) - erpCuNum;
				//4、如果不一致，更新盘库盘盈
				if (profitlossamountTmp > 0 || profitlossamountTmp < 0) {
					if (profitlossamountTmp > 0) {
						pywlNum++;
					} else {
						pkwlNum++;
					}
					OnhandNum ohnUpdate = new OnhandNum();
					ohnUpdate.preUpdate();
					ohnUpdate.setId(ohnItem.getId());
					ohnUpdate.setProfitlossamount(profitlossamountTmp);
					ohnUpdateList.add(ohnUpdate);
				} else {
					wqppERP++;
				}
			}
		}
		OnhandNum ohnUpdateALL = new OnhandNum();
		ohnUpdateALL.preUpdate();
		ohnUpdateALL.setProfitlossamount(0.0);
		ohnUpdateALL.setDelFlag(OnhandNum.DEL_FLAG_NORMAL);
		dao.update(ohnUpdateALL);
		int gxsNum = 0;
		for (OnhandNum item : ohnUpdateList) {
			int updateNum = dao.update(item);
			gxsNum = gxsNum + updateNum;
		}
		logger.info("物料总计：" + invNum);
		logger.info("物料存量与ERP完全匹配：" + wqppERP);
		logger.info("物料盘盈数：" + pywlNum);
		logger.info("物料盘亏数：" + pkwlNum);
		logger.info("物料更新数：" + gxsNum);
		logger.info("ERP没有的物料数：" + noERPNum);
		b = true;
		return b;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean wwyl(String bljhdhs) {
		boolean b = false;
		if (StringUtils.isBlank(bljhdhs)) {
			return b;
		}
		String[] bljhdhArr = bljhdhs.split(",");
		List<String> bljhdhList = Lists.newArrayList();
		for (String item : bljhdhArr) {
			if (StringUtils.isNotBlank(item)) {
				bljhdhList.add(item);
			}
		}
		if (bljhdhList.size() < 1) {
			return b;
		}
		Pickm pmWhere = new Pickm();
		pmWhere.setBljhdhList(bljhdhList);
		pmWhere.setDelFlag(Pickm.DEL_FLAG_NORMAL);
		List<Pickm> pmList = dao.findPickmList(pmWhere);
		if (pmList.size() != bljhdhArr.length) {
			logger.info("备料与传入的个数不符");
			return b;
		}

		List<String> pkPickmidList = Lists.newArrayList();
		for (Pickm item : pmList) {
			if (!"100612100000002P7ZT5".equals(item.getSflbid())) {
				logger.info("备料不是委外");
				return b;
			}
			pkPickmidList.add(item.getId());
		}
		PickmB pmbWhere = new PickmB();
		pmbWhere.setPkPickmidList(pkPickmidList);
		pmbWhere.setDelFlag(PickmB.DEL_FLAG_NORMAL);
		Produce produce = new Produce();
		produce.setDelFlag(Produce.DEL_FLAG_NORMAL);
		pmbWhere.setProduce(produce);
		List<PickmB> pmbList = dao.findPickmBList(pmbWhere);
		for (PickmB item : pmbList) {
			if (item.getLjcksl() > 0) {
				logger.info("L2M出库了");
				return b;
			}
		}
		List<PickmB> pmbERPList = dao.findPickmBERPList(pmbWhere);
		if (pmbList.size() != pmbERPList.size()) {
			logger.info("与ERP数据不符");
			return b;
		}
		
		List<OnhandNum> ohnUpdateList = Lists.newArrayList();
		List<PickmB> pmbUpdateList = Lists.newArrayList();
		List<OnhandNum> ohnNotExistsList = Lists.newArrayList();
		for (PickmB item : pmbERPList) {
			for (PickmB itemTmp : pmbList) {
				if (item.getId().equals(itemTmp.getId())) {
					OnhandNum ohnWhereTmp = new OnhandNum();
					ohnWhereTmp.setPkStordoc(item.getCkckid());
					ohnWhereTmp.setPkStationid(item.getGzzxid());
					ohnWhereTmp.setPkInvbasdoc(itemTmp.getProduce().getPkInvbasdoc());
					ohnWhereTmp.setDelFlag(OnhandNum.DEL_FLAG_NORMAL);
					List<OnhandNum> ohnListTmp = dao.findList(ohnWhereTmp);
					if (ohnListTmp == null || ohnListTmp.size() < 1) {
						ohnNotExistsList.add(ohnWhereTmp);
						break;
					} else if (ohnListTmp.size() > 1) {
						logger.info("现存量多条");
						return b;
					}
					OnhandNum ohnInfo = ohnListTmp.get(0);
					
					OnhandNum ohnUpdate = new OnhandNum();
					ohnUpdate.preUpdate();
					ohnUpdate.setId(ohnInfo.getId());
					ohnUpdate.setCurrentamountMinus(item.getLjcksl() - itemTmp.getLjcksl());
					if (ohnUpdate.getCurrentamountMinus() != 0) {
						ohnUpdateList.add(ohnUpdate);
					}
					
					PickmB pmbUpdate = new PickmB();
					pmbUpdate.preUpdate();
					pmbUpdate.setId(itemTmp.getId());
					pmbUpdate.setCkckid(item.getCkckid());
					pmbUpdate.setDeyl(item.getDeyl());
					pmbUpdate.setDwde(item.getDwde());
					pmbUpdate.setLjcksl(item.getLjcksl());
					pmbUpdate.setJhcksl(item.getJhcksl());
					
					pmbUpdateList.add(pmbUpdate);
					break;
				}
			}
		}
		
		for (PickmB item : pmbUpdateList) {
			dao.updatePickmB(item);
		}
		
		for (OnhandNum item : ohnUpdateList) {
			dao.update(item);
		}
		
		for (String item : pkPickmidList) {
			PickmB pmbWhereTmp = new PickmB();
			pmbWhereTmp.setPkPickmid(item);
			pmbWhereTmp.setDelFlag(PickmB.DEL_FLAG_NORMAL);
			List<PickmB> pmbListTmp = dao.findPickmBList(pmbWhereTmp);
			boolean updateFlagTmp = true;
			for (PickmB itemTmp : pmbListTmp) {
				if (itemTmp.getJhcksl() != itemTmp.getLjcksl()) {
					updateFlagTmp = false;
					break;
				}
			}
			if (updateFlagTmp) {
				Pickm pmUpdate = new Pickm();
				pmUpdate.preUpdate();
				pmUpdate.setId(item);
				pmUpdate.setZt("C");
				dao.updatePickm(pmUpdate);
			}
		}
		
		for (OnhandNum item : ohnUpdateList) {
			logger.info("更新现存量----" + item.getId() + "：" + item.getCurrentamountMinus());
		}
		for (OnhandNum item : ohnNotExistsList) {
			logger.info("不存在的现存量----" + item.getPkStordoc() + "," + item.getPkStationid() + "," + item.getPkInvbasdoc());
		}
		logger.info("更新备料子表:" + pmbUpdateList.size());
		logger.info("更新现存量:" + ohnUpdateList.size());
		logger.info("不存在的现存量:" + ohnNotExistsList.size());
		b = true;
		return b;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean blyl(String bljhdhs) {
		boolean b = false;
		if (StringUtils.isBlank(bljhdhs)) {
			return b;
		}
		String[] bljhdhArr = bljhdhs.split(",");
		List<String> bljhdhList = Lists.newArrayList();
		for (String item : bljhdhArr) {
			if (StringUtils.isNotBlank(item)) {
				bljhdhList.add(item);
			}
		}
		if (bljhdhList.size() < 1) {
			return b;
		}
		Pickm pmWhere = new Pickm();
		pmWhere.setBljhdhList(bljhdhList);
		pmWhere.setDelFlag(Pickm.DEL_FLAG_NORMAL);
		List<Pickm> pmList = dao.findPickmList(pmWhere);
		if (pmList.size() != bljhdhArr.length) {
			logger.info("备料与传入的个数不符");
			return b;
		}

		List<String> pkPickmidList = Lists.newArrayList();
		for (Pickm item : pmList) {
			pkPickmidList.add(item.getId());
		}
		PickmB pmbWhere = new PickmB();
		pmbWhere.setPkPickmidList(pkPickmidList);
		pmbWhere.setDelFlag(PickmB.DEL_FLAG_NORMAL);
		Produce produce = new Produce();
		produce.setDelFlag(Produce.DEL_FLAG_NORMAL);
		pmbWhere.setProduce(produce);
		List<PickmB> pmbList = dao.findPickmBList(pmbWhere);
		for (PickmB item : pmbList) {
			if (item.getLjcksl() > 0) {
				logger.info("L2M出库了");
				return b;
			}
		}
		List<PickmB> pmbERPList = dao.findPickmBERPList(pmbWhere);
		if (pmbList.size() != pmbERPList.size()) {
			logger.info("与ERP数据不符");
			return b;
		}
		
		List<OnhandNum> ohnUpdateList = Lists.newArrayList();
		List<PickmB> pmbUpdateList = Lists.newArrayList();
		List<OnhandNum> ohnNotExistsList = Lists.newArrayList();
		List<OnhandNum> ohnGwbppList = Lists.newArrayList();
		for (PickmB item : pmbERPList) {
			for (PickmB itemTmp : pmbList) {
				if (item.getId().equals(itemTmp.getId())) {
					OnhandNum ohnWhereTmp = new OnhandNum();
					ohnWhereTmp.setPkStordoc(item.getCkckid());
//					ohnWhereTmp.setPkStationid(item.getGzzxid());
					ohnWhereTmp.setPkInvbasdoc(itemTmp.getProduce().getPkInvbasdoc());
					ohnWhereTmp.setDelFlag(OnhandNum.DEL_FLAG_NORMAL);
					List<OnhandNum> ohnListTmp = dao.findList(ohnWhereTmp);
					OnhandNum ohnInfo = null;
					if (ohnListTmp == null || ohnListTmp.size() < 1) {
						ohnNotExistsList.add(ohnWhereTmp);
						break;
					}
					for (OnhandNum ohnItem : ohnListTmp) {
						if (ohnItem.getPkStationid().equals(item.getGzzxid())) {
							ohnInfo = ohnItem;
							break;
						}
					}
					if (ohnInfo == null) {
						ohnInfo = ohnListTmp.get(0);
						ohnGwbppList.add(ohnInfo);
					}
					OnhandNum ohnUpdate = new OnhandNum();
					ohnUpdate.preUpdate();
					ohnUpdate.setId(ohnInfo.getId());
					ohnUpdate.setCurrentamountMinus(item.getLjcksl() - itemTmp.getLjcksl());
					if (ohnUpdate.getCurrentamountMinus() != 0) {
						ohnUpdateList.add(ohnUpdate);
					}
					
					PickmB pmbUpdate = new PickmB();
					pmbUpdate.preUpdate();
					pmbUpdate.setId(itemTmp.getId());
					pmbUpdate.setCkckid(item.getCkckid());
					pmbUpdate.setDeyl(item.getDeyl());
					pmbUpdate.setDwde(item.getDwde());
					pmbUpdate.setLjcksl(item.getLjcksl());
					pmbUpdate.setJhcksl(item.getJhcksl());
					
					pmbUpdateList.add(pmbUpdate);
					break;
				}
			}
		}
		
		for (PickmB item : pmbUpdateList) {
			dao.updatePickmB(item);
		}
		
		for (OnhandNum item : ohnUpdateList) {
			dao.update(item);
		}
		
		for (String item : pkPickmidList) {
			PickmB pmbWhereTmp = new PickmB();
			pmbWhereTmp.setPkPickmid(item);
			pmbWhereTmp.setDelFlag(PickmB.DEL_FLAG_NORMAL);
			List<PickmB> pmbListTmp = dao.findPickmBList(pmbWhereTmp);
			boolean updateFlagTmp = true;
			for (PickmB itemTmp : pmbListTmp) {
				if (itemTmp.getJhcksl() != itemTmp.getLjcksl()) {
					updateFlagTmp = false;
					break;
				}
			}
			if (updateFlagTmp) {
				Pickm pmUpdate = new Pickm();
				pmUpdate.preUpdate();
				pmUpdate.setId(item);
				pmUpdate.setZt("C");
				dao.updatePickm(pmUpdate);
			}
		}
		
		for (OnhandNum item : ohnUpdateList) {
			logger.info("更新现存量----" + item.getId() + "：" + item.getCurrentamountMinus());
		}
		for (OnhandNum item : ohnNotExistsList) {
			logger.info("不存在的现存量----" + item.getPkStordoc() + "," + item.getPkStationid() + "," + item.getPkInvbasdoc());
		}
		for (OnhandNum item : ohnGwbppList) {
			logger.info("现存量工位不匹配----" + item.getId());
		}
		logger.info("更新备料子表:" + pmbUpdateList.size());
		logger.info("更新现存量:" + ohnUpdateList.size());
		logger.info("不存在的现存量:" + ohnNotExistsList.size());
		logger.info("现存量工位不匹配:" + ohnGwbppList.size());
		b = true;
		return b;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean ljcksl() {
		boolean b = false;
		PickmB pmbWhere = new PickmB();
		pmbWhere.setDelFlag(PickmB.DEL_FLAG_NORMAL);
		pmbWhere.setLjckslbfg("YES");
		List<PickmB> pmbList = dao.findPickmBList(pmbWhere);
		List<PickmB> pmbUpdateList = Lists.newArrayList();
		for (PickmB item : pmbList) {
			ExWarehouseZ exwzWhere = new ExWarehouseZ();
			exwzWhere.setCsourcebillbid(item.getId());
			exwzWhere.setDelFlag(ExWarehouseZ.DEL_FLAG_NORMAL);
			List<ExWarehouseZ> exwzList = dao.findExWarehouseZList(exwzWhere);
			PickmB pmbUpdateTmp = new PickmB();
			pmbUpdateTmp.setId(item.getId());
			pmbUpdateTmp.setLjcksl(0.0);
			for (ExWarehouseZ exwzItem : exwzList) {
				pmbUpdateTmp.setLjcksl(pmbUpdateTmp.getLjcksl() + exwzItem.getNoutnum());
			}
			pmbUpdateTmp.preUpdate();
			pmbUpdateList.add(pmbUpdateTmp);
		}
		for (PickmB item : pmbUpdateList) {
			dao.updatePickmB(item);
		}
		logger.info("更新备料子表:" + pmbUpdateList.size());
		b = true;
		return b;
	}
	
}
