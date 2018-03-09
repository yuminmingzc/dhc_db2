<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<div class="widget-body">
	<div class="widget-main">
		<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
			<div class="form-group">
				<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
					<label class="control-label col-xs-12 col-sm-2 no-padding" for="name">姓名:</label>
					<div class="col-xs-12 col-sm-10">
						<div class="clearfix">
							<form:input path="name" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入姓名" />
						</div>
					</div>
				</div>
				<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
					<label class="control-label col-xs-12 col-sm-2 no-padding" for="loginName">登录名:</label>
					<div class="col-xs-12 col-sm-10">
						<div class="clearfix">
							<form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入登录名" />
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
					<label class="control-label col-xs-12 col-sm-2 no-padding" for="no">工号:</label>
					<div class="col-xs-12 col-sm-10">
						<div class="clearfix">
							<form:input path="no" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入工号" />
						</div>
					</div>
				</div>
				<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
					<label class="control-label col-xs-12 col-sm-2 no-padding" for="loginFlag">允许登录:</label>
					<div class="col-xs-12 col-sm-10">
						<div class="clearfix">
							<label>
								<input id="loginFlag" name="loginFlag" value="on" ${user.loginFlag=='1'?'checked':''} class="ace ace-switch ace-switch-4 btn-flat" type="checkbox" />
								<span class="lbl"></span>
							</label>
							<span class="help-inline">该用户是否允许登录系统</span>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-1 no-padding" for="sex">性别:</label>
				<div class="col-xs-12 col-sm-5">
					<form:select  path="sex" class="chosen-select form-control width-100" data-placeholder="点击选择...">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
				<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
					<label class="control-label col-xs-12 col-sm-2 no-padding" for="office.name">部门:</label>
					<div class="col-xs-12 col-sm-10">
						<div class="clearfix check-out input-group">
							<input readonly="" type="text" id="office.name" name="office.name" value="${user.office.name}" class="form-control"/>
							<span class="input-group-btn">
								<button type="button" id="selectOfficeMenu" class="btn btn-purple btn-sm">
									<span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
									选择
								</button>
							</span>
							<input type="hidden" id="office.id" name="office.id" value="${user.office.id}"/>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
					<label class="control-label col-xs-12 col-sm-2 no-padding" for="phone">办公电话:</label>
					<div class="col-xs-12 col-sm-10">
						<div class="clearfix">
							<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入办公电话"/> 
						</div>
					</div>
				</div>
				<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
					<label class="control-label col-xs-12 col-sm-2 no-padding" for="mobile">手机:</label>
					<div class="col-xs-12 col-sm-10">
						<div class="clearfix">
							<form:input path="mobile" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入手机号码"/> 
						</div>
					</div>
				</div>
			</div>
			<div class="form-group form-group-custom">
				<label class="control-label col-xs-12 col-sm-1 no-padding" for="userType">用户类型:</label>
				<div class="col-xs-12 col-sm-5">
					<form:select path="userType" class="select2 width-100" data-placeholder="点击选择...">
						<form:option value=""></form:option>
						<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
				<label class="control-label col-xs-12 col-sm-1 no-padding" for="email">邮箱:</label>
				<div class="col-xs-12 col-sm-5">
					<div class="clearfix">
						<form:input path="email" htmlEscape="false" maxlength="100" class="form-control width-100" placeholder="请输入邮箱" />
					</div>
				</div>
			</div>
			<div class="form-group form-group-custom">
				<label class="control-label col-xs-12 col-sm-1 no-padding" for="userType">角色:</label>
				<div class="col-xs-12 col-sm-11">
					<form:select path="roleIdList" multiple="multiple" class="select2 tag-input-style width-100" data-placeholder="点击选择...">
						<form:options items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="form-group form-group-custom">
				<label class="control-label col-xs-12 col-sm-1 no-padding" for="remarks">备注:</label>
				<div class="col-xs-12 col-sm-11">
					<div class="clearfix">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control width-100" placeholder="请输入备注信息" />
					</div>	
				</div>
			</div>
			<form:hidden path="id"/>
		</form:form>
 	</div>
</div>
<div id="selectOfficeTreeDiv" class="hide widget-body">
	<div class="widget-main padding-8">
		<div id="popuptreeview" class="" data-id="" data-text=""></div>
	</div>
</div>