<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<link rel="stylesheet" href="${ctxStatic}/assets/css/bootstrap-datepicker.css"/>
<title>待办任务</title>
<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="widget-box widget-compact">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h5 class="widget-title lighter">
					查询条件
				</h5>
				<div class="widget-toolbar">
					<a href="#" data-action="collapse"> <i
							class="ace-icon fa fa-chevron-up"></i> </a>
				</div>
			</div>
			<div class="widget-body">
				<div class="widget-main no-padding">
					<form:form id="searchForm" modelAttribute="act" method="get" action=""
					           class="form-horizontal">
						<div class="form-group">
							<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="procDefKey">
								流程类型: </label>
							<div class="col-xs-12 col-sm-1">
								<form:select path="procDefKey" class="chosen-select form-control">
									<form:option value="" label="全部流程"/>
									<form:options items="${fns:getDictList('act_type')}" itemLabel="label"
									              itemValue="value"
									              htmlEscape="false"/>
								</form:select>
							</div>
							<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="procDefKey">
								创建时间: </label>
							<div class="col-xs-12 col-sm-3">

								<div class="input-daterange input-group">
									<input type="text" class="input-sm form-control" id="beginDate" name="beginDate"
									       value="<fmt:formatDate value="${act.beginDate}" type="date"/>"/>
										<%--　--　--%>
									<span class="input-group-addon">--</span>
									<input type="text" class="input-sm form-control" id="endDate" name="endDate"
									       value="<fmt:formatDate value="${act.endDate}" type="date"/>"/>
								</div>

							</div>
							<div class="col-xs-12 col-sm-2">
								<button class="btn btn-info btn-sm" type="button" id="query">查询
								</button>
								<button class="btn btn-info btn-sm" type="reset" id="reset">重置
								</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		<table id="grid-table"></table>
		<div id="grid-pager"></div>
	</div>
</div>
<%--<sys:message content="${message}"/>--%>
<script type="text/javascript">

	var scripts = [null, '${ctxStatic}/assets/js/date-time/bootstrap-datepicker.js', '${ctxStatic}/assets/js/date-time/bootstrap-datepicker.zh-CN.min.js', null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function () {
		jQuery(function ($) {
			$('.chosen-select').chosenSuper({allow_single_deselect: true});
			$('.input-daterange').datepicker({
				autoclose: true,
				zIndexOffset: 100,
				format: "yyyy-mm-dd",
				language: "zh-CN"
			});

			var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";
			var reSizeHeight = function () {
				//必要时 下面代码加入适当的延时，解决部分控件还未初始化的问题
				var strs = $.getWindowSize().toString().split(",");
				var navbar_h = $('#navbar').height();
				var breadcrumbs_h = $('#breadcrumbs').height();
				var padding = 20 + 2;  //   +2校准

				var searchBox = $('div.page-content-area div.widget-box.widget-compact').first().outerHeight(true);
				var gridTitlebar = $('div.ui-jqgrid-titlebar.ui-jqgrid-caption').first().outerHeight(true);
				var gridToppager = $('div.ui-jqgrid-toppager').first().outerHeight(true);
				var jqgridHdiv = $('div.ui-jqgrid-hdiv').first().outerHeight(true);
				var jqgridPagerBottom = $('div.ui-jqgrid-pager').first().outerHeight(true);

				var jqgrid_height = strs[0] - (navbar_h + breadcrumbs_h + padding + searchBox +
					gridTitlebar + gridToppager + jqgridHdiv + jqgridPagerBottom);
				//显示内容过少时，默认显示6行内容

				(jqgrid_height <= 26 * 3) && (jqgrid_height = 26 * 6);
				$(grid_selector).jqGrid('setGridHeight', jqgrid_height);
			};

			jQuery(grid_selector).jqGrid({
				datatype: "json", //将这里改为使用JSON数据
				url: '${ctx}/task/todoTaskData', //这是数据的请求地址
				height: 'auto',
				autowidth: false,
				shrinkToFit: true,
				jsonReader: {
					root: "rows",   // json中代表实际模型数据的入口
					page: "page",   // json中代表当前页码的数据
					total: "total", // json中代表页码总数的数据
					records: "records", // json中代表数据行总数的数据
					repeatitems: false
				},
				prmNames: {
					page: "pageNo",    // 表示请求页码的参数名称
					rows: "rows",    // 表示请求行数的参数名称
					sort: "sidx", // 表示用于排序的列名的参数名称
					order: "sord", // 表示采用的排序方式的参数名称
					search: "_search", // 表示是否是搜索请求的参数名称
					nd: "nd", // 表示已经发送请求的次数的参数名称
					id: "id", // 表示当在编辑数据模块中发送数据时，使用的id的名称
					oper: "oper",    // operation参数名称（我暂时还没用到）
					editoper: "edit", // 当在edit模式中提交数据时，操作的名称
					addoper: "add", // 当在add模式中提交数据时，操作的名称
					deloper: "del", // 当在delete模式中提交数据时，操作的名称
					subgridid: "id", // 当点击以载入数据到子表时，传递的数据名称
					npage: null,
					totalrows: "totalrows" // 表示需从Server得到总共多少行数据的参数名称，参见jqGrid选项中的rowTotal
				},

				colNames: ['操作', '标题', '当前环节', '流程名称', '流程版本', '创建时间', 'task.id', 'task.assignee', '', '', '', '', '', 'status'],
				colModel: [
					{name: 'View', index: 'View', width: 200, sortable: false},
					{name: 'taskTitle', index: 'taskTitle', width: 200},
					{name: 'taskName', index: 'task.name', width: 150},
					{name: 'procDefName', index: 'procDef.name', width: 100},
					{name: 'procDefVersion', index: 'procDef.version'},
					{name: 'taskCreateTime', index: 'task.createTime'},
					{name: 'taskId', index: '', width: 200, hidden: true},
					{name: 'taskAssignee', index: '', width: 200, hidden: true},
					{name: 'taskDefKey', index: '', width: 200, hidden: true},
					{name: 'procInsId', index: '', width: 200, hidden: true},
					{name: 'procDefId', index: '', width: 200, hidden: true},
					{name: 'taskExecutionId', index: '', width: 200, hidden: true},
					{name: 'varsMapTitle', index: '', width: 200, hidden: true},
					{name: 'status', index: 'status', hidden: true}
				],

				viewrecords: true,
				rowNum: 20,
				rowList: [10, 20, 30],
				pager: pager_selector,
				altRows: true,
				toppager: true,

				multiselect: false,

				loadComplete: function () {
					$.changeGridTable.changeStyle(this);  //改变复选框的样式
					$(grid_selector + "_toppager_center").remove();
					$(grid_selector + "_toppager_right").remove();
					$(pager_selector + "_left table").remove();
				},

				editurl: "/dummy.html",//nothing is saved
				caption: "工序列表",
				gridComplete: function () {
					var ids = jQuery("#grid-table").jqGrid('getDataIDs');
					var userid = "${fns:getUser().id}";

					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = $("#grid-table").getRowData(id);
						var taskId = rowData['taskId'];
						var taskName = rowData['taskName'];
						var taskNameEncode = encodeURI(taskName);
						var procDefVersion = "V:" + rowData['procDefVersion'];
						var taskDefKey = rowData.taskDefKey;
						var procInsId = rowData.procInsId;
						var procDefId = rowData.procDefId;
						var taskExecutionId = rowData.taskExecutionId;
						var status = rowData.status;
						var varsMapTitle = rowData.varsMapTitle;
						var taskNameShow = '<a target="_blank" href="${pageContext.request.contextPath}/act/rest/diagram-viewer?processDefinitionId='
							+ procDefId + '&processInstanceId='
							+ procInsId + '&projectUrl=${pageContext.request.contextPath}">'
							+ taskName + '</a>';
						var taskTitle = '';
						var view = '';
						if (!rowData.taskAssignee) {
							view += '<a href="javascript:claim(' + taskId + ');">签收任务</a> ';
							taskTitle += '<a href="javascript:claim(' + taskId + ');"' + ' title="签收任务">' +
								abbr(varsMapTitle ? varsMapTitle : taskId, 60) + '</a>';
						} else {
							view += '<a href="${ctxPage}/act/task/form?taskId=' + taskId + '&taskName='
								+ taskNameEncode + '&taskDefKey=' + taskDefKey +
								'&procInsId=' + procInsId +
								'&procDefId=' + procDefId + '&status=' + status + '">任务办理</a> ';
							taskTitle += '<a href="${ctxPage}/act/task/form?taskId=' + taskId
								+ '&taskName=' + taskNameEncode + '&taskDefKey=' + taskDefKey
								+ '&procInsId=' + procInsId
								+ '&procDefId=' + procDefId
								+ '&status=' + status + '">'
								+ abbr(varsMapTitle ? varsMapTitle : taskId, 60) + '</a>';
						}
						<shiro:hasPermission name="act:process:edit">
						if (!taskExecutionId) {
							view += '<a href="${ctx}/act/task/deleteTask?taskId=' + taskId + '&reason="onclick="return promptx(\'删除任务\',\'删除原因\',this.href);">删除任务</a> ';
						}
						</shiro:hasPermission>
						view += '<a target="_blank" ' +
							'href="${pageContext.request.contextPath}/act/rest/diagram-viewer?processDefinitionId=' + procDefId + '&processInstanceId=' + procInsId + '&projectUrl=${pageContext.request.contextPath}">跟踪1</a> ' +
							'<a target="_blank" href="${ctx}/act/task/trace/photo/' + procDefId + '/' + taskExecutionId + '">跟踪2</a>'
						$(grid_selector).jqGrid('setRowData', ids[i], {
							procDefVersion: procDefVersion,
							taskName: taskNameShow,
							taskTitle: taskTitle,
							View: view
						});

					}
				}
			});

			$.changeGridTable.changeSize([grid_selector, grid_selector + " ~ .widget-box"], reSizeHeight);
			//隐藏水平滚动条
			$(grid_selector).closest(".ui-jqgrid-bdiv").css({'overflow-x': 'hidden'});

			jQuery(grid_selector).jqGrid('navGrid', pager_selector,
				{//navbar options
					edit: false,
					editicon: 'ace-icon fa fa-pencil blue',
					//					editfunc: openDialogEdit,
					edittext: "编辑",
					edittitle: '',
					add: false,
					addicon: 'ace-icon fa fa-plus-circle purple',
					//					addfunc: openDialogAdd,
					addtext: "新增",
					addtitle: '',
					del: false,
					delicon: 'ace-icon fa fa-trash-o red',
					//					delfunc: doDelete,
					deltext: "删除",
					deltitle: '',
					search: false,
					refresh: true,
					refreshicon: 'ace-icon fa fa-refresh green',
					refreshtext: "刷新",
					refreshtitle: '',
					view: false,
					viewicon: 'ace-icon fa fa-search-plus grey',
					viewtext: "查看",
					viewtitle: '',
					cloneToTop: true
				},
				{}, // use default settings for edit
				{}, // use default settings for add
				{},  // delete instead that del:false we need this
				{multipleSearch: true}, // enable the advanced searching
				{closeOnEscape: true} /* allow the view dialog to be closed when user press ESC key*/
			);
			//override dialog's title function to allow for HTML titles
			$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
				_title: function (title) {
					var $title = this.options.title || '&nbsp;';
					if (("title_html" in this.options) && this.options.title_html == true)
						title.html($title);
					else title.text($title);
				}
			}));

			$('#query').on('click', function () {
				var postData = {
					procDefKey: $('#procDefKey').val(),
					beginDate: $('#beginDate').val(),
					endDate: $('#endDate').val()
				};
				$(grid_selector).jqGrid('setGridParam', {
					url: "${ctx}/task/todoTaskData",
					mtype: "post",
					postData: postData, //发送数据
					page: 1
				}).trigger("reloadGrid"); //重新载入
			});
			$("#reset").click(function () {
				$('.chosen-select').val('').trigger('chosen:updated');
				var postData = {
					procDefKey: '',
					beginDate: '',
					endDate: ''
				};
				$(grid_selector).jqGrid('setGridParam', {
					url: "${ctx}/task/todoTaskData",
					mtype: "post",
					postData: postData,
					page: 1
				}).trigger("reloadGrid"); //重新载入
			});
			$('#procDefKey').on("change", function () {
				$('#query').click();
			});

			/**
			 * 签收任务
			 */
			function claim(taskId) {
				$.get('${ctx}/act/task/claim', {taskId: taskId}, function (data) {
					if (data == 'true') {
						top.$.jBox.tip('签收完成');
						location = '${ctxPage}/act/task/todo/';
					} else {
						top.$.jBox.tip('签收失败');
					}
				});
			}


		});
	});
</script>