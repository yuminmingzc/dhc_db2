/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.web;

import com.dhc.rad.common.config.Global;
import com.dhc.rad.common.persistence.Page;
import com.dhc.rad.common.utils.StringUtils;
import com.dhc.rad.common.web.BaseController;
import com.dhc.rad.modules.sys.entity.Menu;
import com.dhc.rad.modules.sys.entity.Role;
import com.dhc.rad.modules.sys.service.OfficeService;
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
 * 角色Controller
 *
 * @author DHC
 * @version 2013-12-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private OfficeService officeService;

    @ModelAttribute("role")
    public Role findInfo(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return systemService.getRole(id);
        } else {
            return new Role();
        }
    }

    /**
     * 列表
     *
     * @return
     */
    @RequiresPermissions("sys:role:view")
    @RequestMapping(value = {"index", ""})
    public String index() {
        return "modules/sys/roleList";
    }

    /**
     * 列表数据
     *
     * @return
     */
    @RequiresPermissions("sys:role:view")
    @RequestMapping(value = {"list"})
    @ResponseBody
    public Map<String, Object> list() {
        Page<Role> page = new Page<Role>();
        page.setList(systemService.findAllRole());
        Map<String, Object> returnMap = Maps.newHashMap();
        addPageData(returnMap, page);
        return returnMap;
    }

    /**
     * 编辑页面
     *
     * @param entity
     * @param model
     * @return
     */
    @RequiresPermissions("sys:role:view")
    @RequestMapping(value = "form")
    public String form(Role entity, Model model) {
        if (entity == null) {
            entity = new Role();
        }
        if (entity.getOffice() == null) {
            entity.setOffice(UserUtils.getUser().getOffice());
        }
        model.addAttribute("role", entity);
        model.addAttribute("menuList", systemService.findAllMenu());
        model.addAttribute("officeList", officeService.findAll());
        return "modules/sys/roleForm";
    }

    /**
     * 保存数据
     *
     * @param entity
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "save")
    @ResponseBody
    public Map<String, Object> save(Role entity) {
        Map<String, Object> returnMap = Maps.newHashMap();
        if (!UserUtils.getUser().isAdmin() && entity.getSysData().equals(Global.YES)) {
            addMessageAjax(returnMap, Global.ERROR, "越权操作，只有超级管理员才能修改此数据！");
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

        if (entity.getUseable() != null && "on".equals(entity.getUseable())) {
            entity.setUseable(Global.YES);
        } else {
            entity.setUseable(Global.NO);
        }

        if (!beanValidatorAjax(returnMap, entity)) {
            return returnMap;
        }
        Map<String, Object> checkNameResult = checkNameAjax(entity.getName(), entity.getId());
//		if (!"true".equals(checkName(entity.getOldName(), entity.getName()))){
        if ("false".equals(checkNameResult.get("valid"))) {
            addMessageAjax(returnMap, Global.ERROR, "保存角色'" + entity.getName() + "'失败, 角色名已存在");
            return returnMap;
        }
        Map<String, Object> checkEnnameResult = checkEnnameAjax(entity.getName(), entity.getId());
//		if (!"true".equals(checkEnname(entity.getOldEnname(), entity.getEnname()))){
        if ("false".equals(checkEnnameResult.get("valid"))) {
            addMessageAjax(returnMap, Global.ERROR, "保存角色'" + entity.getName() + "'失败, 英文名已存在");
            return returnMap;
        }
        if (StringUtils.isNotBlank(entity.getId())) {
            Role original = systemService.getRole(entity.getId());
            if (!entity.getEnname().equals(original.getEnname())) {
                addMessageAjax(returnMap, "0", "保存角色'" + entity.getName() + "'失败, 不允许修改角色英文名称");
                return returnMap;
            }
        }
        try {
            systemService.saveRoleWithoutMenu(entity);
            addMessageAjax(returnMap, Global.SUCCESS, "保存角色'" + entity.getName() + "'成功");
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "系统异常");
        }
        return returnMap;
    }

    /**
     * 删除
     *
     * @param entity
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public Map<String, Object> delete(Role entity) {
        Map<String, Object> returnMap = Maps.newHashMap();
        if (!UserUtils.getUser().isAdmin() && entity.getSysData().equals(Global.YES)) {
            addMessageAjax(returnMap, Global.ERROR, "越权操作，只有超级管理员才能修改此数据！");
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
        if (StringUtils.isBlank(entity.getId())) {
            addMessageAjax(returnMap, Global.ERROR, "数据错误！");
            return returnMap;
        }
//		if (Role.isAdmin(entity.getId())) {
//			addMessageAjax(returnMap, Global.ERROR, "删除角色失败, 不允许内置角色或编号空");
//			return returnMap;
//		} else if (UserUtils.getUser().getRoleIdList().contains(entity.getId())) {
//			addMessageAjax(returnMap, Global.ERROR, "删除角色失败, 不能删除当前用户所在角色");
//			return returnMap;
//		}
        try {
            systemService.deleteRole(entity);
            addMessageAjax(returnMap, Global.SUCCESS, "删除角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "系统异常");
        }
        return returnMap;
    }

    /**
     * 验证角色名是否有效
     *
     * @param oldName
     * @param name
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "checkName")
    public String checkName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return "true";
        } else if (name != null && systemService.getRoleByName(name) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 验证角色英文名是否有效
     *
     * @param oldEnname
     * @param enname
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "checkEnname")
    public String checkEnname(String oldEnname, String enname) {
        if (enname != null && enname.equals(oldEnname)) {
            return "true";
        } else if (enname != null
                && systemService.getRoleByEnname(enname) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 验证角色名称是否被占用
     *
     * @param name
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkNameAjax")
    public Map<String, Object> checkNameAjax(String name, String id) {
        Map<String, Object> map = Maps.newHashMap();
        Role role = systemService.getRoleByName(name);
        if (role == null) {
            map.put("valid", true);
        } else if (id != null && !id.equals("")) {
            if (id.equals(role.getId())) {
                map.put("valid", true);
            } else {
                map.put("valid", false);
            }
        } else {
            map.put("valid", false);
        }
        return map;
    }

    /**
     * 验证英文名称是否被占用
     *
     * @param enname
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkEnnameAjax")
    public Map<String, Object> checkEnnameAjax(String enname, String id) {
        Map<String, Object> map = Maps.newHashMap();
        Role role = systemService.getRoleByEnname(enname);
        if (role == null) {
            map.put("valid", true);
        } else if (id != null && !id.equals("")) {
            if (id.equals(role.getId())) {
                map.put("valid", true);
            } else {
                map.put("valid", false);
            }
        } else {
            map.put("valid", false);
        }
        return map;
    }

    /**
     * 保存角色菜单
     *
     * @param id
     * @param menus
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "saveMenus")
    @ResponseBody
    public Map<String, Object> saveMenus(String id, String menus) {
        Map<String, Object> returnMap = Maps.newHashMap();
        if (Global.isDemoMode()) {
            addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
            return returnMap;
        }
        Role role = new Role(id);
        String[] menuArray = menus.split(",");
        if (menuArray.length != 0) {
            for (String menu : menuArray) {
                if (!"".equals(menu)) {
                    role.getMenuList().add(new Menu(menu));
                }
            }
        }
        try {
            systemService.saveRoleMenu(role);
            addMessageAjax(returnMap, Global.SUCCESS, "分配功能点成功");
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
    @RequestMapping(value = "menuTreeData")
    @ResponseBody
    public Map<String, Object> menuTreeData(String id) {
        Map<String, Object> returnMap = Maps.newHashMap();
        Role role = systemService.getRole(id);
        List<Menu> list = systemService.findAllMenu();
        List<String> selectList = role.getMenuIdList();
        returnMap.put("data", convert2Tree(list, Menu.getRootId(), selectList));
        returnMap.put("selectList", selectList);
        return returnMap;
    }

    /**
     * 格式化数据
     *
     * @param list
     * @param pid
     * @return
     */
    private List<Map<String, Object>> convert2Tree(List<Menu> list, String pid, List<String> selectList) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        for (Menu item : list) {
            if (pid.equals(item.getParentId())) {
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
                if (selectList.contains(item.getId())) {
                    Map<String, Boolean> checked = Maps.newHashMap();
                    checked.put("checked", true);
                    map.put("state", checked);
                }
                List<Map<String, Object>> nodes = convert2Tree(list, item.getId(), selectList);
                if (nodes.size() > 0) {
                    map.put("nodes", nodes);
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

}
