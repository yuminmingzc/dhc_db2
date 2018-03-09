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
import com.dhc.rad.modules.sys.entity.Office;
import com.dhc.rad.modules.sys.entity.User;
import com.dhc.rad.modules.sys.service.OfficeService;
import com.dhc.rad.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 机构Controller
 * @author DHC
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;

	@ModelAttribute("office")
	public Office findInfo(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return officeService.get(id);
		} else {
			return new Office();
		}
	}

	/**
	 * 组织机构列表
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "index", "" })
	public String index(Model model) {
		Office entity = new Office();
		entity.setParentIds("%");
		List<Office> list = officeService.findList(entity);
		List<Office> list4Format = Lists.newArrayList();
		formatList(list4Format, list, Office.getRootId());
		model.addAttribute("list", list4Format);
		return "modules/sys/officeList";
	}
	
	/**
	 * 排序
	 * @param list4Format
	 * @param list
	 * @param pid
	 */
	private void formatList(List<Office> list4Format, List<Office> list, String pid){
		for (Office item : list) {
			if(pid.equals(item.getParentId())){
				list4Format.add(item);
				formatList(list4Format, list, item.getId());
			}
		}
	}
	
	/**
	 * 组织机构表单页
	 * @param entity
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office entity, Model model) {
		User user = UserUtils.getUser();
		if (entity == null) {
			entity = new Office();
		}
		if (entity.getParent() == null || entity.getParent().getId() == null) {
			entity.setParent(user.getOffice());
		}
		entity.setParent(officeService.get(entity.getParent().getId()));
		if (entity.getArea() == null) {
			entity.setArea(user.getOffice().getArea());
		}
		// 自动获取排序号
		if (StringUtils.isBlank(entity.getId()) && entity.getParent() != null) {
			int size = 0;
			List<Office> list = officeService.findAll();
			for (int i = 0; i < list.size(); i++) {
				Office e = list.get(i);
				if (e.getParent() != null && e.getParent().getId() != null && e.getParent().getId().equals(entity.getParent().getId())) {
					size++;
				}
			}
			entity.setCode(entity.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size + 1 : 1), 3, "0"));
		}
		List<Office> companyList = officeService.findCompanyList();
		model.addAttribute("office", entity);
		model.addAttribute("companyList", companyList);
		return "modules/sys/officeForm";
	}

	/**
	 * 组织机构保存
	 * @param entity
	 * @return
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(Office entity) {
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
			officeService.save(entity);
			addMessageAjax(returnMap, Global.SUCCESS, "保存机构'" + entity.getName() + "'成功");
		} catch (Exception e) {
			e.printStackTrace();
			addMessageAjax(returnMap, Global.ERROR, "系统异常");
		}
		return returnMap;
	}

	/**
	 * 组织机构删除
	 * @param entity
	 * @return
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(Office entity) {
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
//		if (Office.isRoot(entity.getId())){
//			addMessageAjax(returnMap, Global.ERROR, "删除机构失败, 不允许删除顶级机构或编号空");
//			return returnMap;
//		}
		try {
			officeService.delete(entity);
			addMessageAjax(returnMap, Global.SUCCESS, "删除机构成功");
		} catch (Exception e) {
			e.printStackTrace();
			addMessageAjax(returnMap, Global.ERROR, "系统异常");
		}
		return returnMap;
	}

	/**
	 * 获取机构JSON数据。
	 * @param treetype
	 * @param extId   排除的ID
	 * @param type    类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade   显示级别
	 * @param isAll
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String treetype, @RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Long grade, @RequestParam(required = false) Boolean isAll) {
		//TODO:maliang 参数暂定
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(isAll);
		Map<String, Object> map = Maps.newHashMap();
		if (treetype != null && treetype.equals("1")) {
			mapList = convert2Tree(list, Office.getRootId());
		} else {
			map.put("id", "0");
			map.put("text", "根节点");
			List<Map<String, Object>> nodes = convert2Tree(list, Office.getRootId());
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
	private List<Map<String, Object>> convert2Tree(List<Office> list, String pid) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		for (Office item : list) {
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
