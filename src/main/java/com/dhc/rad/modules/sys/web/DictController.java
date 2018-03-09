/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhc.rad.common.config.Global;
import com.dhc.rad.common.persistence.Page;
import com.dhc.rad.common.utils.StringUtils;
import com.dhc.rad.common.web.BaseController;
import com.dhc.rad.modules.sys.entity.Dict;
import com.dhc.rad.modules.sys.service.DictService;
import com.google.common.collect.Maps;

/**
 * 字典Controller
 * @author DHC
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;

	@ModelAttribute
	public Dict findInfo(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return dictService.findInfo(new Dict(id));
		} else {
			return new Dict();
		}
	}

	/**
	 * 列表页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = { "index", "" })
	public String index(Model model) {
		List<String> typeList = dictService.findTypeList();
		model.addAttribute("typeList", typeList);
		return "modules/sys/dictList";
	}

	/**
	 * 列表数据
	 * @param entity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = { "list" })
	@ResponseBody
	public Map<String, Object> list(Dict entity, HttpServletRequest request, HttpServletResponse response) {
		Page<Dict> page = dictService.findPage(new Page<Dict>(request, response), entity);
		Map<String, Object> returnMap = Maps.newHashMap();
		addPageData(returnMap, page);
		return returnMap;
	}

	/**
	 * 编辑页面
	 * @param entity
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict entity, Model model) {
		if (entity == null) {
			entity = new Dict();
		}
		model.addAttribute("dict", entity);
		return "modules/sys/dictForm";
	}

	/**
	 * 保存数据
	 * @param entity
	 * @return
	 */
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(Dict entity) {
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
			boolean b = dictService.save(entity);
			if (b) {
				addMessageAjax(returnMap, Global.SUCCESS, "保存字典'" + entity.getLabel() + "'成功");
			} else {
				addMessageAjax(returnMap, Global.ERROR, "保存字典失败");
			}
		} catch (Exception e) {
			// 这个是必须的，可以在控制台打印错误信息，方便调试
			e.printStackTrace();
			addMessageAjax(returnMap, Global.ERROR, "系统异常");
		}
		return returnMap;
	}

	/**
	 * 删除数据（根据ID删除，支持批量删除）
	 * @param dict
	 * @return
	 */
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String[] ids) {
		Map<String, Object> returnMap = Maps.newHashMap();
		try {
			boolean b = dictService.delete(ids);
			if (b) {
				addMessageAjax(returnMap, Global.SUCCESS, "删除字典成功");
			}else{
				addMessageAjax(returnMap, Global.ERROR, "删除字典失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			addMessageAjax(returnMap, Global.ERROR, "系统异常");
		}
		return returnMap;
	}

}
