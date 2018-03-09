/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.web;

import com.dhc.rad.common.persistence.Page;
import com.dhc.rad.common.utils.DateUtils;
import com.dhc.rad.common.web.BaseController;
import com.dhc.rad.modules.sys.entity.Log;
import com.dhc.rad.modules.sys.service.LogService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 日志Controller
 *
 * @author DHC
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController {

    @Autowired
    private LogService logService;

    /**
     * 列表页面
     *
     * @param model
     * @return
     */
    @RequiresPermissions("sys:log:view")
    @RequestMapping(value = {"index", ""})
    public String index(Model model) {
        Date beginDate = DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", DateUtils.addMonths(beginDate, 1));
        return "modules/sys/logList";
    }

    /**
     * 列表数据
     *
     * @param entity
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("sys:log:view")
    @RequestMapping(value = {"list"})
    @ResponseBody
    public Map<String, Object> list(Log entity, HttpServletRequest request, HttpServletResponse response) {
        Page<Log> page = logService.findPage(new Page<Log>(request, response), entity);
        Map<String, Object> returnMap = Maps.newHashMap();
        addPageData(returnMap, page);
        return returnMap;
    }

    /**
     * 系统更新日志
     * 发布系统更新内容
     * @param request  the request
     * @param response the response
     * @return the string
     */
    @RequiresPermissions("sys:updateLog:view")
    @RequestMapping(value = {"updateLog"})
    public String updateLog(HttpServletRequest request, HttpServletResponse response) {

        return "modules/sys/updateLogList";
    }

}
