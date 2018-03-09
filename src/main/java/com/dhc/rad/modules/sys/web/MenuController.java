/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.web;

import com.dhc.rad.common.config.Global;
import com.dhc.rad.common.utils.StringUtils;
import com.dhc.rad.common.web.BaseController;
import com.dhc.rad.modules.sys.entity.Menu;
import com.dhc.rad.modules.sys.service.SystemService;
import com.dhc.rad.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 菜单Controller
 *
 * @author DHC
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends BaseController {

    @Autowired
    private SystemService systemService;

    @ModelAttribute("menu")
    public Menu findInfo(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return systemService.getMenu(id);
        } else {
            return new Menu();
        }
    }

    /**
     * 列表页面
     *
     * @param model
     * @return
     */
    @RequiresPermissions("sys:menu:view")
    @RequestMapping(value = {"index", ""})
    public String index(Model model) {
        List<Menu> list = Lists.newArrayList();
        List<Menu> sourcelist = systemService.findAllMenu();
        Menu.sortList(list, sourcelist, Menu.getRootId(), true);
        model.addAttribute("list", list);
        return "modules/sys/menuList";
    }

    /**
     * 编辑页面
     *
     * @param entity
     * @param model
     * @return
     */
    @RequiresPermissions("sys:menu:view")
    @RequestMapping(value = "form")
    public String form(Menu entity, Model model) {
        if (entity == null) {
            entity = new Menu();
        }
        if (entity.getParent() == null || entity.getParent().getId() == null) {
            entity.setParent(new Menu(Menu.getRootId()));
        }
        entity.setParent(systemService.getMenu(entity.getParent().getId()));
        // 获取排序号，最末节点排序号+30
        if (StringUtils.isBlank(entity.getId())) {
            List<Menu> list = Lists.newArrayList();
            List<Menu> sourcelist = systemService.findAllMenu();
            Menu.sortList(list, sourcelist, entity.getParentId(), false);
            if (list.size() > 0) {
                entity.setSort(list.get(list.size() - 1).getSort() + 30);
            }
        }
        model.addAttribute("menu", entity);
        return "modules/sys/menuForm";
    }

    @RequiresPermissions("user")
    @RequestMapping(value = "iconselect")
    public String iconselect(Model model) {

        return "modules/sys/iconSelect";
    }


    /**
     * 保存数据
     *
     * @param entity
     * @return
     */
    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "save")
    @ResponseBody
    public Map<String, Object> save(Menu entity) {
        Map<String, Object> returnMap = Maps.newHashMap();
        if (!UserUtils.getUser().isAdmin()) {
            addMessageAjax(returnMap, Global.ERROR, "越权操作，只有超级管理员才能添加或修改数据！");
            return returnMap;
        }
        if (Global.isDemoMode()) {
            addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
            return returnMap;
        }
        if (entity == null) {
            addMessageAjax(returnMap, Global.ERROR, "无数据！");
            return returnMap;
        }
        if (entity.getIsShow() != null && "on".equals(entity.getIsShow())) {
            entity.setIsShow(Global.YES);
        } else {
            entity.setIsShow(Global.NO);
        }
        if (entity.getIsMobile() != null && "on".equals(entity.getIsMobile())) {
            entity.setIsMobile(Global.YES);
        } else {
            entity.setIsMobile(Global.NO);
        }
        if (!beanValidatorAjax(returnMap, entity)) {
            return returnMap;
        }
        try {
            systemService.saveMenu(entity);
            addMessageAjax(returnMap, Global.SUCCESS, "保存菜单'" + entity.getName() + "'成功");
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "系统异常");
        }
        return returnMap;
    }

    /**
     * 删除数据
     *
     * @param entity
     * @return
     */
    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public Map<String, Object> delete(Menu entity) {
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
//		if (Menu.isRoot(entity.getId())){
//			addMessageAjax(returnMap, Global.ERROR, "删除菜单失败, 不允许删除顶级菜单或编号为空");
//			return returnMap;
//		}
        try {
            systemService.deleteMenu(entity);
            addMessageAjax(returnMap, Global.SUCCESS, "删除菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "系统异常");
        }
        return returnMap;
    }

    /**
     * 修改菜单排序
     *
     * @param entity
     * @return
     */
    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "updateSort")
    @ResponseBody
    public Map<String, Object> updateSort(Menu entity) {
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
        try {
            systemService.updateMenuSort(entity);
            addMessageAjax(returnMap, Global.SUCCESS, "保存菜单排序成功!'");
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "系统异常");
        }
        return returnMap;
    }

    /**
     * 获取菜单JSON数据。
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "treeData")
    @ResponseBody
    public List<Map<String, Object>> treeData() {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Menu> list = systemService.findAllMenu();
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", "1");
        map.put("text", "功能菜单");
        List<Map<String, Object>> nodes = convert2Tree(list, Menu.getRootId());
        if (nodes.size() > 0) {
            map.put("nodes", nodes);
        }
        mapList.add(map);
        return mapList;
    }

    /**
     * 格式化数据
     *
     * @param list
     * @param pid
     * @return
     */
    private List<Map<String, Object>> convert2Tree(List<Menu> list, String pid) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        for (Menu item : list) {
            if (pid.equals(item.getParentId())) {
                if (item.getIsShow() != null && Global.YES.equals(item.getIsShow())) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("id", item.getId());
                    map.put("text", item.getName());
                    if (item.getIcon() != null) {
                        if (item.getIcon().startsWith("glyphicon-")) {
                            map.put("icon", "glyphicon " + item.getIcon());
                        } else if (item.getIcon().startsWith("fa-")) {
                            map.put("icon", "fa " + item.getIcon());
                        } else {
                            map.put("icon", item.getIcon());
                        }
                    }
                    List<Map<String, Object>> nodes = convert2Tree(list, item.getId());
                    if (nodes.size() > 0) {
                        map.put("nodes", nodes);
                    }
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }

    /**
     * 函数功能说明 : 作者: fangzr 创建时间：2015-12-7 修改者名字: 修改日期 : 修改内容 :
     *
     * @throws
     * @参数： @param extId
     * @参数： @param response
     * @参数： @return
     */
    @ResponseBody
    @RequestMapping(value = "getMobileMenuTree")
    public Map<String, Object> getMobileMenutree() {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Menu> list = systemService.getMobileMenuList();
        List<Menu> listrole = systemService.getMobileRoleList();

        Map<String, Object> resulMap = Maps.newHashMap();
        if (listrole == null || listrole.size() == 0 || listrole.equals("")) {
            resulMap.put("resultStatus", "201");
        } else {
            String role = listrole.get(0).getName();
            for (Menu menu : list) {
                if (!menu.getName().equals("通知")) {
                    Map<String, Object> menuMap = Maps.newHashMap();
                    menuMap.put("href", menu.getHref());
                    menuMap.put("name", menu.getName());
                    mapList.add(menuMap);
                }
            }
            Map<String, Object> menuMap1 = Maps.newHashMap();
            menuMap1.put("href", "/sys/notify/notifyList");
            menuMap1.put("name", "通知");
            mapList.add(menuMap1);

            Map<String, Object> map = Maps.newHashMap();
            if (mapList.size() == 0) {
                map.put("roletype", role);
                map.put("menulist", mapList);
                resulMap.put("data", map);
                resulMap.put("resultStatus", "200");
            } else {
                map.put("roletype", role);
                map.put("menulist", mapList);
                resulMap.put("data", map);
                resulMap.put("resultStatus", "200");
            }
        }
        return resulMap;
    }

}
