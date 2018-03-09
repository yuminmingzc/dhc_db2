<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<div class="widget-body" id="areaDivId">
	<div class="widget-main" >
	<form:form id="inputForm" modelAttribute="area" action="${ctx}/sys/area/save" method="post" class="form-horizontal">
		<div class="form-group">
			<label class="control-label col-xs-12 col-sm-2 no-padding" for="parent.name">上级区域:</label>
			<div class="col-xs-12 col-sm-10">
				<div class="clearfix check-out input-group">
					<input readonly="" type="text" id="parent.name" name="parent.name" value="${area.parent.name=='' || area.parent.name == null?'根节点':area.parent.name}" class="form-control"/>
					<span class="input-group-btn">
						<button type="button" id="selectParentarea" class="btn btn-purple btn-sm">
							<span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
							选择
						</button>
					</span>
					<input type="hidden" id="parent.id" name="parent.id" value="${area.parent.id}"/>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-12 col-sm-2 no-padding" for="name">区域名称:</label>
			<div class="col-xs-12 col-sm-10">
				<div class="clearfix">
					<form:input path="name" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入区域名称" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-12 col-sm-2 no-padding" for="code">区域编码:</label>
			<div class="col-xs-12 col-sm-10">
				<div class="clearfix">
					<form:input path="code" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入区域编码" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-12 col-sm-2 no-padding" for="type">区域类型:</label>
			<div class="col-xs-12 col-sm-10">
				<div class="clearfix input-group width-100">
					<form:select path="type" class="chosen-select form-control width-100" data-placeholder="点击选择...">
						<form:options items="${fns:getDictList('sys_area_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-12 col-sm-2 no-padding" for="remarks">备注:</label>
			<div class="col-xs-12 col-sm-10">
				<div class="clearfix">
					<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control width-100" placeholder="请输入备注信息" />
				</div>
			</div>
		</div>
		<form:hidden path="id"/>
	</form:form>
	</div>
</div>
<div id="selectParentareaDiv" class="hide widget-body">
	<div class="widget-main padding-8">
		<div id="treeview" class="" data-id="" data-text=""></div>
	</div>
</div>
