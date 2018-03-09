<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<div class="widget-body" id="dictDivId">
	<div class="widget-main" >
	<form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post" class="form-horizontal">		
		<div class="form-group">
			<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="value">键值:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:input path="value" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入键值"/>
					</div>
				</div>
			</div>
			<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="label">标签:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:input path="label" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入键值标签名称" />
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="type">类型:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:input path="type" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入键值的类型" />
					</div>
				</div>
			</div>
			<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="description">描述:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:input path="description" htmlEscape="false" maxlength="50" class="form-control width-100" placeholder="请输入键值描述信息"/>
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="sort">排序:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:input path="sort" htmlEscape="false" maxlength="11" class="form-control width-100"/>
					</div>
				</div>
			</div>
			<div class="form-group-custom col-xs-12 col-sm-6 no-padding">
				<label class="control-label col-xs-12 col-sm-2 no-padding" for="remarks">备注:</label>
				<div class="col-xs-12 col-sm-10">
					<div class="clearfix">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control width-100" placeholder="请输入备注信息" />
					</div>
				</div>
			</div>
		</div>
		<form:hidden path="id"/>
	</form:form>
	</div>
</div>
