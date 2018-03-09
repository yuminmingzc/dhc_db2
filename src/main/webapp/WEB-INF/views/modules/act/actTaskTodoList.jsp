<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<script>
	/**
	 * 签收任务
	 */
	<%--function claim(taskId) {--%>
			<%--$.get('${ctx}/act/task/claim' ,{taskId: taskId}, function(data) {--%>
			<%--if (data == 'true') {--%>
				<%--top.$.jBox.tip('签收完成');--%>
				<%--location = '${ctxPage}/act/task/todo/';--%>
			<%--} else {--%>
				<%--top.$.jBox.tip('签收失败');--%>
			<%--}--%>
		<%--});--%>
	<%--}--%>
    src="${ctxStatic}/assets/js/bootstrap-tab.js"
</script>
<title>待办任务</title>
<ul  class="nav nav-tabs">
	<li class="active"><a href="${ctx}/act/task/todo/">待办任务</a></li>
	<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
	<li><a href="${ctx}/act/task/process/">新建任务</a></li>
</ul>
<form:form id="searchForm" modelAttribute="act" method="get" action="${ctx}/act/task/todo/" class="form-horizontal">
	<div class="form-group">
		<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="procDefKey"> 流程类型: </label>
		<div class="col-xs-12 col-sm-1">
			<form:select path="procDefKey" class="chosen-select form-control">
				<form:option value="" label="全部流程"/>
				<form:options items="${fns:getDictList('act_type')}" itemLabel="label" itemValue="value"
				              htmlEscape="false"/>
			</form:select>
		</div>
		<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="procDefKey"> 创建时间: </label>
		<div class="col-xs-12 col-sm-3">
			<input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20"
			       class="input-medium Wdate"
			       style="width:163px;"
			       value="<fmt:formatDate value="${act.beginDate}" pattern="yyyy-MM-dd"/>"
			       onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			　--　
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
			       style="width:163px;"
			       value="<fmt:formatDate value="${act.endDate}" pattern="yyyy-MM-dd"/>"
			       onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
		</div>
		<div class="col-xs-12 col-sm-1">
			<input id="btnSubmit" class="btn btn-info btn-sm" type="submit" value="查询"/>
		</div>
	</div>
</form:form>
<sys:message content="${message}"/>

<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<th>标题</th>
		<th>当前环节</th>
		<%--
						<th>任务内容</th> --%>
		<th>流程名称</th>
		<th>流程版本</th>
		<th>创建时间</th>
		<th>操作</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${list}" var="act">
		<c:set var="task" value="${act.task}"/>
		<c:set var="vars" value="${act.vars}"/>
		<c:set var="procDef" value="${act.procDef}"/><%--
				<c:set var="procExecUrl" value="${act.procExecUrl}" /> --%>
		<c:set var="status" value="${act.status}"/>
		<tr>
			<td>
				<c:if test="${empty task.assignee}">
					<a href="javascript:claim('${task.id}');"
					   title="签收任务">${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}</a>
				</c:if>
				<c:if test="${not empty task.assignee}">
					<a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}">${fns:abbr(not empty vars.map.title ? vars.map.title : task.id, 60)}</a>
				</c:if>
			</td>
			<td>
				<a target="_blank"
				   href="${pageContext.request.contextPath}/act/rest/diagram-viewer?processDefinitionId=${task.processDefinitionId}&processInstanceId=${task.processInstanceId}&projectUrl=${pageContext.request.contextPath}">${task.name}</a>
			</td>
				<%--
									<td>${task.description}</td> --%>
			<td>${procDef.name}</td>
			<td><b title='流程版本号'>V: ${procDef.version}</b></td>
			<td><fmt:formatDate value="${task.createTime}" type="both"/></td>
			<td>
				<c:if test="${empty task.assignee}">
					<a href="javascript:claim('${task.id}');">签收任务</a>
				</c:if>
				<c:if test="${not empty task.assignee}"><%--
							<a href="${ctx}${procExecUrl}/exec/${task.taskDefinitionKey}?procInsId=${task.processInstanceId}&act.taskId=${task.id}">办理</a> --%>
					<a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}">任务办理</a>
				</c:if>
				<shiro:hasPermission name="act:process:edit">
					<c:if test="${empty task.executionId}">
						<a href="${ctx}/act/task/deleteTask?taskId=${task.id}&reason="
						   onclick="return promptx('删除任务','删除原因',this.href);">删除任务</a>
					</c:if>
				</shiro:hasPermission>
				<a target="_blank"
				   href="${pageContext.request.contextPath}/act/rest/diagram-viewer?processDefinitionId=${task.processDefinitionId}&processInstanceId=${task.processInstanceId}&projectUrl=${pageContext.request.contextPath}">跟踪1</a>
				<a target="_blank" href="${ctx}/act/task/trace/photo/${task.processDefinitionId}/${task.executionId}">跟踪2</a><%--
						<a target="_blank" href="${ctx}/act/task/trace/info/${task.processInstanceId}">跟踪信息</a> --%>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</body>
</html>
<script type="text/javascript">

	var scripts = [null, null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function () {
		jQuery(function ($) {
			$('.chosen-select').chosenSuper({allow_single_deselect: true});
			$('#btnSubmit').on('click', function () {
				$
			})

		});
	});
</script>