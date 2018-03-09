<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<div class="widget-body" id="menuDivId">
	<div class="widget-main">
		<form:form id="inputForm" modelAttribute="menu" action="${ctx}/sys/menu/save" method="post"
		           class="form-horizontal">
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="name">名称:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:input path="name" htmlEscape="false" maxlength="50" class="form-control width-100"
						            placeholder="请输入菜单名称"/>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="parent.name">上级菜单:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix check-out input-group">
						<input readonly="" type="text" id="parent.name" name="parent.name" value="${menu.parent.name}"
						       class="form-control"/>
						<span class="input-group-btn">
						<button type="button" id="selectParentMenu" class="btn btn-purple btn-sm">
							<span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
							选择
						</button>
					</span>
						<input type="hidden" id="parent.id" name="parent.id" value="${menu.parent.id}"/>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="href">链接:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:input path="href" htmlEscape="false" maxlength="2000" class="form-control width-100"
						            placeholder="请输入链接地址(点击菜单跳转的页面)"/>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="icon">图标:</label>
				<div class="col-xs-12 col-sm-10">
						<%--<div class="clearfix">--%>
						<%--<form:input path="icon" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入图标名称"/> --%>
						<%--</div>--%>
					<div class="clearfix check-out input-group">
						<form:input type="text" path="icon" class="form-control" placeholder="请选择一个图标"/>
						<span class="input-group-btn">
							<button type="button" id="selectIconMenu" class="btn btn-purple btn-sm">
								<span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
								图标选择
							</button>
						</span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="remarks">排序:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:input path="sort" htmlEscape="false" maxlength="11" class="form-control width-100"/>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="isShow">显示:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<label>
							<input id="isShow" name="isShow" ${menu.isShow=='1'?'checked':''}
							       class="ace ace-switch ace-switch-4 btn-flat" type="checkbox"/>
							<span class="lbl"></span>
						</label>
						<span class="help-inline">该菜单或操作是否显示到系统菜单中</span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="isMobile">手机菜单:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<label>
							<input id="isMobile" name="isMobile" ${menu.isMobile=='0'?'checked':''}
							       class="ace ace-switch ace-switch-4 btn-flat" type="checkbox"/>
							<span class="lbl"></span>
						</label>

					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="permission">权限标识:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:input path="permission" htmlEscape="false" maxlength="200"
						            class="form-control width-100"/>
					</div>
					<span class="help-inline">控制器中定义的权限标识，如：@RequiresPermissions("权限标识")</span>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="remarks">备注:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200"
						               class="form-control width-100" placeholder="请输入备注信息"/>
					</div>
				</div>
			</div>
			<form:hidden path="id"/>
		</form:form>
	</div>
</div>
<div id="selectParentMenuDiv" class="hide widget-body">
	<div class="widget-main padding-8">
		<div id="treeview" class="" data-id="" data-text=""></div>
	</div>
</div>