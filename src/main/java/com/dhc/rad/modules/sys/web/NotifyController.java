/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.web;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhc.rad.common.persistence.Page;
import com.dhc.rad.common.utils.StringUtils;
import com.dhc.rad.common.web.BaseController;
import com.dhc.rad.modules.sys.entity.Notify;
import com.dhc.rad.modules.sys.entity.User;
import com.dhc.rad.modules.sys.service.NotifyService;
import com.dhc.rad.modules.sys.service.SystemService;
import com.dhc.rad.modules.sys.utils.UserUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 通知Controller
 * @author lijunjie
 * @version 2015-11-21
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/notify")
public class NotifyController extends BaseController {

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private SystemService systemService;

	@ModelAttribute
	public Notify findInfo(@RequestParam(required = false) String id) {
		Notify entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = notifyService.get(id);
		}
		if (entity == null) {
			entity = new Notify();
		}
		return entity;
	}

	/**
	 * 通知信息列表页
	 * @return
	 */
	@RequestMapping(value = { "index", "" })
	public String index() {
		return "modules/sys/notifyList";
	}

	/**
	 * 通知信息列表数据
	 */
	@RequestMapping(value = "notifyList")
	@ResponseBody
	public Map<String, Object> findList(Notify entity, HttpServletRequest request, HttpServletResponse response) {
		// 查询当前用户
		User user = UserUtils.getUser();
		if (StringUtils.isBlank(user.getId())) {
			return null;
		}
		entity.setReceiver(user);
		Page<Notify> page = notifyService.findPage(new Page<Notify>(request, response), entity);
		
		Map<String, Object> returnMap = Maps.newHashMap();
		addPageData(returnMap, page);
		returnMap.put("resultStatus", "200");
		return returnMap;
	}

	@RequestMapping(value = "save")
	public Map<String, Object> save(Notify entity) {
		Map<String, Object> returnMap = Maps.newHashMap();
		if (!beanValidatorAjax(returnMap, entity)) {
			return returnMap;
		}
		notifyService.save(entity);
		addMessageAjax(returnMap, "1", "保存通知'" + entity.getTitle() + "'成功");
		return returnMap;
	}

	/**
	 * 通知设置已读 接口
	 */
	@RequestMapping(value = "setRead")
	@ResponseBody
	public Map<String, Object> readNotify(Notify notify) {
		User user = UserUtils.getUser();
		if (user.getId() == null || user.getId().equals("")) {
			return null;
		}
		notify.setReceiver(user);
		notify.setReadDate(new Date());
		notify.setReadFlag(Notify.READ_FLAG_YES);
		notify.preUpdate();

		int str = notifyService.readNotify(notify);
		Map<String, Object> resulMap = Maps.newHashMap();
		if (str == 1) {
			resulMap.put("data", "已浏览此通知");
			resulMap.put("resultStatus", "200");
			return resulMap;
		} else {
			// TODO 此处应该也是执行成功了，只是标识阅读的通知已阅读罢了，返回操作失败和201是不正确的
			resulMap.put("resultStatus", "200");
			return resulMap;
		}
	}

	@RequestMapping(value = "setAllRead")
	@ResponseBody
	public Map<String, Object> setAllRead(String ids[]) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		User user = UserUtils.getUser();
		if (user.getId() == null || user.getId().equals("")) {
			return null;
		}
		List<String> list = Arrays.asList(ids);
		for(String id : list){
			Notify temp = notifyService.get(id);
			temp.setReceiver(user);
			temp.setReadDate(new Date());
			temp.setReadFlag(Notify.READ_FLAG_YES);
			temp.preUpdate();
			if(notifyService.readNotify(temp) != 0){
				addMessageAjax(returnMap, "1", "设置为已读成功");
			}else{
				addMessageAjax(returnMap, "0", "设置为已读失败");
			}
		}
		return returnMap;

	}
	
	/**
	 * 获取未读通知数目
	 */
	@RequestMapping(value = "count")
	@ResponseBody
	public String count(Notify notify) {
		return systemService.unReadNotifyCount(Notify.READ_FLAG_NO);
	}

	/**
	 * APP接口功能： 轮询 获取全部通知数目、以及最新通知
	 */
	@RequestMapping(value = "countAll")
	@ResponseBody
	public Map<String, Object> countAll(Notify notify, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = Maps.newHashMap();
		// 查询当前用户
		User user = UserUtils.getUser();
		if (user.getId() == null || user.getId().equals("")) {
			return null;
		}
		notify.setReceiver(user);
		Page<Notify> page = notifyService.findPage(new Page<Notify>(request, response), notify);
		// 通知总数
		long all = page.getCount();
		if (all < 1) {
			resultMap.put("resultStatus", "201");
			return resultMap;
		}
		// 取得第一条通知
		Notify firstNotify = page.getList().get(0);
		// 未读通知数
		String unReadNum = systemService.unReadNotifyCount(Notify.READ_FLAG_NO);

		resultMap.put("total", all);
		resultMap.put("unReadNum", unReadNum);
		resultMap.put("title", firstNotify.getTitle());
		resultMap.put("content", firstNotify.getContent());
		resultMap.put("resultStatus", "200");
		return resultMap;
	}

}
