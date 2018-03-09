/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.web;

import com.dhc.rad.common.beanvalidator.BeanValidators;
import com.dhc.rad.common.config.Global;
import com.dhc.rad.common.mapper.JsonMapper;
import com.dhc.rad.common.persistence.Page;
import com.dhc.rad.common.utils.DateUtils;
import com.dhc.rad.common.utils.StringUtils;
import com.dhc.rad.common.utils.excel.ExportExcel;
import com.dhc.rad.common.utils.excel.ImportExcel;
import com.dhc.rad.common.web.BaseController;
import com.dhc.rad.common.web.Servlets;
import com.dhc.rad.modules.sys.entity.Notify;
import com.dhc.rad.modules.sys.entity.Office;
import com.dhc.rad.modules.sys.entity.Role;
import com.dhc.rad.modules.sys.entity.User;
import com.dhc.rad.modules.sys.service.SystemService;
import com.dhc.rad.modules.sys.utils.DictUtils;
import com.dhc.rad.modules.sys.utils.PhotoUtils;
import com.dhc.rad.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskQuery;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author DHC
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

    @Autowired
    private SystemService systemService;
    @Autowired
    private TaskService taskService;

    @ModelAttribute
    public User findInfo(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            User entity = systemService.getUser(id);
            entity.setOldLoginName(entity.getLoginName());
            return entity;
        } else {
            return new User();
        }
    }

    /**
     * 用户列表页
     *
     * @param model
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"index"})
    public String index(Model model) {
        Map<String, Object> pageData = Maps.newHashMap();
        pageData.put("sys_user_type", DictUtils.getDictList("sys_user_type"));
        pageData.put("yes_no", DictUtils.getDictList("yes_no"));
        //将传入页面的值全部封装到一个变量中
        model.addAttribute("pageData", JsonMapper.toJsonString(pageData));
        return "modules/sys/userList";
    }

    /**
     * 用户列表页数据，分页显示
     *
     * @param entity
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"list"})
    @ResponseBody
    public Map<String, Object> list(User entity, HttpServletRequest request, HttpServletResponse response) {
        if (entity == null) {
            entity = new User();
        }
        Page<User> page = systemService.findUser(new Page<User>(request, response), entity);
        // 返回值
        Map<String, Object> returnMap = Maps.newHashMap();
        addPageData(returnMap, page);
        return returnMap;
    }

    /**
     * 用户编辑页面
     *
     * @param entity
     * @param pageOfficeId
     * @param pageOfficeName
     * @param model
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "form")
    public String form(User entity, String pageOfficeId, String pageOfficeName, Model model) {
        //update by maliang 页面请求先执行上面的get方法，返回user对象，此时user内的部门从数据库中查询
        //是正确的，放入缓存中，随后页面传来的office.id和office.name覆盖user对象中的相应属性，缓存中的user也被改变，导致此bug。
        //这里有安全隐患，页面传入的如果和对象匹配，可随意修改缓存中的值。
        //解决方法，页面值改变名称
        if (entity == null) {
            entity = new User();
        }
        Office office = new Office(pageOfficeId);
        office.setName(pageOfficeName);
        if (StringUtils.isBlank(entity.getId())) {
            entity.setOffice(office);
        }
        if (entity.getCompany() == null) {
            entity.setCompany(new Office());
        }
        if (entity.getOffice() == null) {
            entity.setOffice(new Office());
        }
        model.addAttribute("user", entity);
        model.addAttribute("allRoles", systemService.findAllRole());
        return "modules/sys/userForm";
    }

    /**
     * 上传用户头像
     *
     * @author lijunjie
     * @version 2016-3-15
     */
//	@RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "saveAvatar")
    @ResponseBody
    public Map<String, Object> saveAvatar(@RequestParam MultipartFile avatar) {
        Map<String, Object> returnMap = Maps.newHashMap();
        if (Global.isDemoMode()) {
            addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
            return returnMap;
        }
        User user = UserUtils.getUser();
        if (avatar.isEmpty()) {
            addMessageAjax(returnMap, Global.ERROR, "请选择头像上传");
            return returnMap;
        }
        String imgName = avatar.getOriginalFilename();
        String suffix = imgName.substring(imgName.lastIndexOf(".") + 1, imgName.length());
        // 验证图片格式
        if (!"jpg".equals(suffix) && !"png".equals(suffix) && !"gif".equals(suffix) && !"jpeg".equals(suffix)) {
            addMessageAjax(returnMap, Global.ERROR, "图片格式错误，请重试");
            return returnMap;
        }
        String name = user.getId() + "_large";
        String urlString = "/uploads/userAvatar/";

        // web端访问路径
        String filePath = Servlets.getRequest().getContextPath() + urlString + name + "." + suffix;
        // 本地保存路径
        String fileLarge = Global.getUserfilesBaseDir() + urlString + user.getId() + "_large" + "." + suffix;
        String fileMiddle = Global.getUserfilesBaseDir() + urlString + user.getId() + "_middle" + "." + suffix;
        String fileSmall = Global.getUserfilesBaseDir() + urlString + user.getId() + "_small" + "." + suffix;
        String fileOr = Global.getUserfilesBaseDir() + urlString + user.getId() + "." + suffix;
        // 创建目录
        File oldFile = new File(fileOr);
        if (!oldFile.exists()) {
            oldFile.mkdirs();
        }
        // 保存原图片
        try {
            avatar.transferTo(oldFile);
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "存储用户'" + user.getLoginName() + "'头像失败,请重试");
            return returnMap;
        }
        try {
            PhotoUtils.zoomImage(fileOr, fileLarge, 200, 200);
            PhotoUtils.zoomImage(fileOr, fileMiddle, 100, 100);
            PhotoUtils.zoomImage(fileOr, fileSmall, 50, 50);
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "存储用户'" + user.getLoginName() + "'头像失败,请重试");
            return returnMap;
        }
        // 此处上传只存储图片的格式，具体的图片由 uploads/userAvatar/user.id_large+user.photo
        user.setPhoto("." + suffix);
        try {
            systemService.updateUserInfo(user);
        } catch (Exception e) {
            addMessageAjax(returnMap, Global.ERROR, "保存用户'" + user.getLoginName() + "'头像失败");
            return returnMap;
        }
        addMessageAjax(returnMap, Global.SUCCESS, "");
        returnMap.put("url", filePath);
        return returnMap;
    }

    /**
     * 保存用户
     *
     * @param entity
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "save")
    @ResponseBody
    public Map<String, Object> save(User entity) {
        Map<String, Object> returnMap = Maps.newHashMap();
        if (Global.isDemoMode()) {
            addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
            return returnMap;
        }
        if (entity == null) {
            addMessageAjax(returnMap, Global.ERROR, "无数据！");
            return returnMap;
        }
        if (StringUtils.isNoneBlank(entity.getLoginFlag()) && "on".equals(entity.getLoginFlag())) {
            entity.setLoginFlag(Global.YES);
        } else {
            entity.setLoginFlag(Global.NO);
        }
        if (entity.getOffice() == null) {
            entity.setOffice(new Office());
        }
        if (StringUtils.isNotBlank(entity.getOffice().getId())) {
            Office company = systemService.findCompanyInfo(entity.getOffice().getId());
            entity.setCompany(company);
        }
        if (entity.getCompany() == null) {
            entity.setCompany(new Office());
        }
        // 新增用户，默认密码
        if (StringUtils.isBlank(entity.getId())) {
            entity.setNewPassword("123456");
            entity.setPassword(SystemService.entryptPassword(entity.getNewPassword()));
        }
        if (!beanValidatorAjax(returnMap, entity)) {
            return returnMap;
        }
        Map<String, Object> checkResult = checkLoginNameAjax(entity.getLoginName(), entity.getId());
        if ("false".equals(checkResult.get("valid").toString())) {
            addMessageAjax(returnMap, Global.ERROR, "保存用户'" + entity.getLoginName() + "'失败，登录名已存在");
            return returnMap;
        }
        // 角色数据有效性验证，过滤不在授权内的角色
        List<Role> roleList = Lists.newArrayList();
        List<String> roleIdList = entity.getRoleIdList();
        List<Role> roleList4All = systemService.findAllRole();
        for (Role r : roleList4All) {
            if (roleIdList.contains(r.getId())) {
                roleList.add(r);
            }
        }
        if (roleList.size() == 0 && Global.YES.equals(entity.getLoginFlag())) {
            addMessageAjax(returnMap, Global.ERROR, "保存用户'" + entity.getLoginName() + "'失败，允许登录用户没有分配角色");
            return returnMap;
        }
        entity.setRoleList(roleList);
        // 保存用户信息
        try {
            systemService.saveUser(entity);
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "系统异常");
            return returnMap;
        }
        // 清除当前用户缓存
        if (entity.getLoginName().equals(UserUtils.getUser().getLoginName())) {
            UserUtils.clearCache();
        }
        addMessageAjax(returnMap, Global.SUCCESS, "保存用户'" + entity.getLoginName() + "'成功");
        return returnMap;
    }

    /**
     * 删除用户（包含批量删除）
     *
     * @param ids
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public Map<String, Object> delete(String[] ids) {
        Map<String, Object> returnMap = Maps.newHashMap();
        if (Global.isDemoMode()) {
            addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
            return returnMap;
        }
        List<String> idList = Arrays.asList(ids);
        for (String item : idList) {
            if (StringUtils.isBlank(item)) {
                addMessageAjax(returnMap, Global.ERROR, "数据错误");
                return returnMap;
            }
            if (UserUtils.getUser().getId().equals(item)) {
                addMessageAjax(returnMap, Global.ERROR, "删除用户失败, 不允许删除当前用户");
                return returnMap;
            } else if (User.isAdmin(item)) {
                addMessageAjax(returnMap, Global.ERROR, "删除用户失败, 不允许删除超级管理员用户");
                return returnMap;
            }
        }
        try {
            for (String item : idList) {
                systemService.deleteUser(new User(item));
            }
            addMessageAjax(returnMap, Global.SUCCESS, "删除用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "系统异常");
            return returnMap;
        }
        return returnMap;
    }

    /**
     * 重置用户密码
     *
     * @param entity
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "resetPwd")
    @ResponseBody
    public Map<String, Object> resetPwd(User entity) {
        Map<String, Object> returnMap = Maps.newHashMap();
        if (Global.isDemoMode()) {
            addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
            return returnMap;
        }
        if (!UserUtils.getUser().isAdmin()) {
            addMessageAjax(returnMap, Global.ERROR, "非超级管理员用户不可以重置用户密码！");
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
            systemService.updatePasswordById(entity.getId(), entity.getLoginName(), "123456");
            addMessageAjax(returnMap, Global.SUCCESS, "重置密码成功");
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "系统异常");
            return returnMap;
        }
        return returnMap;
    }

    /**
     * 导出用户数据
     *
     * @param entity
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(User entity, HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), entity);
            new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导入用户数据
     *
     * @param file
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> importFile(MultipartFile file) {
        Map<String, Object> returnMap = Maps.newHashMap();
        if (Global.isDemoMode()) {
            addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
            return returnMap;
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<User> list = ei.getDataList(User.class);
            for (User user : list) {
                try {
                    if ("true".equals(checkLoginName("", user.getLoginName()))) {
                        user.setPassword(SystemService.entryptPassword("123456"));
                        BeanValidators.validateWithException(validator, user);
                        user.setLoginFlag(Global.NO);
                        systemService.saveUser(user);
                        successNum++;
                    } else {
                        failureMsg.append("<br/>登录名 " + user.getLoginName() + " 已存在; ");
                        failureNum++;
                    }
                } catch (ConstraintViolationException ex) {
                    failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList) {
                        failureMsg.append(message + "; ");
                        failureNum++;
                    }
                } catch (Exception ex) {
                    failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：" + ex.getMessage());
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条用户，导入信息如下：");
            }
            addMessageAjax(returnMap, Global.SUCCESS, "已成功导入 " + successNum + " 条用户" + failureMsg);
        } catch (Exception e) {
            addMessageAjax(returnMap, Global.ERROR, "导入用户失败！失败信息：" + e.getMessage());
        }
        return returnMap;
    }

    /**
     * 下载导入用户数据模板
     *
     * @param response
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "用户数据导入模板.xlsx";
            List<User> list = Lists.newArrayList();
            list.add(UserUtils.getUser());
            new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            // 导入模板下载失败！失败信息：
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证登录名是否有效
     *
     * @param oldLoginName
     * @param loginName
     * @return
     */
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "checkLoginName")
    public String checkLoginName(String oldLoginName, String loginName) {
        if (loginName != null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 验证登录名是否被占用
     *
     * @param loginName
     * @param id
     * @return
     */
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "checkLoginNameAjax")
    public Map<String, Object> checkLoginNameAjax(String loginName, String id) {
        Map<String, Object> map = Maps.newHashMap();
        User user = systemService.getUserByLoginName(loginName);
        if (user == null) {
            map.put("valid", true);
        } else if (id != null && !id.equals("")) {
            if (id.equals(user.getId())) {
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
     * 登录用户信息显示
     *
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "info")
    public String info(User user, HttpServletResponse response, Model model) {
        User currentUser = UserUtils.getUser();
//		model.addAttribute("user", currentUser);
        String unReadNotifys = systemService.unReadNotifyCount(Notify.READ_FLAG_NO);
        //已经签收的任务
        TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(currentUser.getLoginName()).active()
                .includeProcessVariables();
        //等待签收的任务
        TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(currentUser.getLoginName())
                .includeProcessVariables().active();
        Long todoTasks = todoTaskQuery.count() + toClaimQuery.count();
        model.addAttribute("unReadNotifys", unReadNotifys);
        model.addAttribute("todoTasks", todoTasks);
        model.addAttribute("user", currentUser);
        model.addAttribute("Global", new Global());
        return "modules/sys/userInfo";
    }

//	/**
//	 * 用户信息保存
//	 * @param entity
//	 */
//	@RequiresPermissions("user")
//	@RequestMapping(value = "saveInfo")
//	@ResponseBody
//	public void saveInfo(User entity) {
//		
//	}

    /**
     * 修改个人用户密码页面
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "modifyPwd")
    public String modifyPwd() {
        return "modules/sys/userModifyPwd";
    }

    /**
     * 修改个人用户密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "doModifyPwd")
    @ResponseBody
    public Map<String, Object> doModifyPwd(String oldPassword, String newPassword) {
        User user = UserUtils.getUser();
        Map<String, Object> returnMap = Maps.newHashMap();
        if (Global.isDemoMode()) {
            addMessageAjax(returnMap, Global.ERROR, "演示模式，不允许操作！");
            return returnMap;
        }
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            addMessageAjax(returnMap, Global.ERROR, "表单数据异常");
            return returnMap;
        }
        if (!SystemService.validatePassword(oldPassword, user.getPassword())) {
            addMessageAjax(returnMap, Global.ERROR, "修改密码失败，旧密码错误");
            return returnMap;
        }
        try {
            systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
            addMessageAjax(returnMap, Global.SUCCESS, "修改密码成功");
        } catch (Exception e) {
            e.printStackTrace();
            addMessageAjax(returnMap, Global.ERROR, "系统异常");
            return returnMap;
        }
        return returnMap;
    }

//	@InitBinder
//	public void initBinder(WebDataBinder b) {
//		b.registerCustomEditor(List.class, "roleList", new PropertyEditorSupport(){
//			@Autowired
//			private SystemService systemService;
//			@Override
//			public void setAsText(String text) throws IllegalArgumentException {
//				String[] ids = StringUtils.split(text, ",");
//				List<Role> roles = new ArrayList<Role>();
//				for (String id : ids) {
//					Role role = systemService.getRole(Long.valueOf(id));
//					roles.add(role);
//				}
//				setValue(roles);
//			}
//			@Override
//			public String getAsText() {
//				return Collections3.extractToString((List) getValue(), "id", ",");
//			}
//		});
//	}

}
