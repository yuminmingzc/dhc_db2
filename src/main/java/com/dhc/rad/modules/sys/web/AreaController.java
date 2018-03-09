/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.web;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhc.rad.common.config.Global;
import com.dhc.rad.common.utils.StringUtils;
import com.dhc.rad.common.web.BaseController;
import com.dhc.rad.modules.sys.entity.Area;
import com.dhc.rad.modules.sys.service.AreaService;
import com.dhc.rad.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 区域Controller
 * @author DHC
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;

	@ModelAttribute("area")
	public Area findInfo(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return areaService.get(id);
		} else {
			return new Area();
		}
	}

	/**
	 * 区域列表
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = { "index", "" })
	public String index(Model model) {
		List<Area> list = areaService.findAll();
		List<Area> list4Format = Lists.newArrayList();
		formatList(list4Format, list, Area.getRootId());
		model.addAttribute("list", list4Format);
		return "modules/sys/areaList";
	}

	/**
	 * 排序
	 * @param list4Format
	 * @param list
	 * @param pid
	 */
	private void formatList(List<Area> list4Format, List<Area> list, String pid){
		for (Area item : list) {
			if(pid.equals(item.getParentId())){
				list4Format.add(item);
				formatList(list4Format, list, item.getId());
			}
		}
	}

	/**
	 * 区域表单页
	 * @param entity
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area entity, Model model) {
		if (entity == null) {
			entity = new Area();
		}
		if (entity.getParent() == null || entity.getParent().getId() == null) {
			entity.setParent(UserUtils.getUser().getOffice().getArea());
		}
		entity.setParent(areaService.get(entity.getParent().getId()));
//		// 自动获取排序号
//		if (StringUtils.isBlank(entity.getId())) {
//			int size = 0;
//			List<Area> list = areaService.findAll();
//			for (int i = 0; i < list.size(); i++) {
//				Area e = list.get(i);
//				if (e.getParent() != null && e.getParent().getId() != null && e.getParent().getId().equals(entity.getParent().getId())) {
//					size++;
//				}
//			}
//			entity.setCode(entity.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
//		}
		model.addAttribute("area", entity);
		return "modules/sys/areaForm";
	}

	/**
	 * 区域保存
	 * @param entity
	 * @return
	 */
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(Area entity) {
		Map<String, Object> returnMap = Maps.newHashMap();
		if (Global.isDemoMode()) {
			addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
			return returnMap;
		}
		if (entity == null) {
			addMessageAjax(returnMap, Global.ERROR, "无数据！");
			return returnMap;
		}
		if (!beanValidatorAjax(returnMap, entity)) {
			return returnMap;
		}
		try {
			areaService.save(entity);
			addMessageAjax(returnMap, Global.SUCCESS, "保存区域'" + entity.getName() + "'成功");
		} catch (Exception e) {
			e.printStackTrace();
			addMessageAjax(returnMap, Global.ERROR, "系统异常");
		}
		return returnMap;
	}

	/**
	 * 区域删除
	 * @param entity
	 * @return
	 */
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(Area entity) {
		Map<String, Object> returnMap = Maps.newHashMap();
		if (Global.isDemoMode()) {
			addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
			return returnMap;
		}
		if (entity == null) {
			addMessageAjax(returnMap, Global.ERROR, "无数据！");
			return returnMap;
		}
		if (StringUtils.isBlank(entity.getId())) {
			addMessageAjax(returnMap, Global.ERROR, "数据错误！");
			return returnMap;
		}
//		if (Area.isRoot(entity.getId())) {
//			addMessageAjax(returnMap, Global.ERROR, "删除区域失败, 不允许删除顶级区域或编号为空");
//			return returnMap;
//		}
		try {
			areaService.delete(entity);
			addMessageAjax(returnMap, Global.SUCCESS, "删除区域成功");
		} catch (Exception e) {
			e.printStackTrace();
			addMessageAjax(returnMap, Global.ERROR, "系统异常");
		}
		return returnMap;
	}

	/**
	 * 获取区域JSON数据。
	 * @param type
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String type) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findAll();
		Map<String, Object> map = Maps.newHashMap();
		if (type != null && type.equals("1")) {
			mapList = convert2Tree(list, Area.getRootId());
		} else {
			map.put("id", "0");
			map.put("text", "根节点");
			List<Map<String, Object>> nodes = convert2Tree(list, Area.getRootId());
			if (nodes.size() > 0) {
				map.put("nodes", nodes);
			}
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 格式化数据
	 * @param list
	 * @param pid
	 * @return
	 */
	private List<Map<String, Object>> convert2Tree(List<Area> list, String pid) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		for (Area item : list) {
			if (pid.equals(item.getParentId())) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", item.getId());
				map.put("text", item.getName());
				List<Map<String, Object>> nodes = convert2Tree(list, item.getId());
				if (nodes.size() > 0) {
					map.put("nodes", nodes);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

}
