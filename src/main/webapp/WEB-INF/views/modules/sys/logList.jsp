<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link rel="stylesheet" href="${ctxStatic}/assets/css/bootstrap-datepicker.css" />
<title>日志管理</title>
<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="widget-box widget-compact">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h5 class="widget-title lighter">查询条件</h5>
				<div class="widget-toolbar">
					<a href="#" data-action="collapse"> <i class="ace-icon fa fa-chevron-up"></i> </a>
				</div>
			</div>
			<div class="widget-body">
				<div class="widget-main">
					<form class="form-horizontal">
						<div class="form-group">
							<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="titleQuery">标题:</label>
							<div class="col-xs-12 col-sm-3">
								<input type="text" id="titleQuery" maxlength="50" class="ace width-100" />
							</div>
							<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="loginNameQuery">用户登录名:</label>
							<div class="col-xs-12 col-sm-3">
								<input type="text" id="loginNameQuery" maxlength="50" class="ace width-100" />
							</div>
							<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="requestUriQuery">URI:</label>
							<div class="col-xs-12 col-sm-3">
								<input type="text" id="requestUriQuery" maxlength="50" class="ace width-100" />
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="beginDateQuery">日期范围:</label>
							<div class="col-xs-12 col-sm-3">
								<div class="input-daterange input-group">
									<input type="text" class="input-sm form-control" id="beginDateQuery" value="<fmt:formatDate value="${beginDate}" type="date"/>"/>
									<span class="input-group-addon"><i class="fa fa-exchange"></i></span>
									<input type="text" class="input-sm form-control" id="endDateQuery" value="<fmt:formatDate value="${endDate}" type="date"/>"/>
								</div>
							</div>
							<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="typeQuery">查询范围:</label>
							<div class="col-xs-12 col-sm-3">
								<select id="typeQuery" class="chosen-select form-control" data-placeholder="点击选择...">
									<option value="">全部</option>
									<option value="1">接入日志</option>
									<option value="2">异常日志</option>
								</select>
							</div>
							<div class="col-xs-12 col-sm-1 no-padding-right">&nbsp;</div>
							<div class="col-xs-12 col-sm-3 no-padding-right">
								<button class="btn btn-info btn-sm" type="button" id="query">查询</button>
								<button class="btn btn-info btn-sm" type="reset" id="reset">重置</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<table id="grid-table"></table>
				<div id="grid-pager"></div>
			</div>
		</div>
	</div>
</div>
<div class="widget-box" style="display:none" id="editDivId"></div>
<script type="text/javascript">
	var scripts = [null,'${ctxStatic}/assets/js/date-time/bootstrap-datepicker.js','${ctxStatic}/assets/js/date-time/bootstrap-datepicker.zh-CN.min.js', null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		jQuery(function($){
			//选择框
			$('.chosen-select').chosenSuper({allow_single_deselect:true});
			$('.input-daterange').datepicker({autoclose:true,zIndexOffset:100,format: "yyyy-mm-dd",language:"zh-CN"});

			var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";

			var pageData = {
				'sys_log_type': $.parseJSON('${fns:toJson(fns:getDictList("sys_log_type"))}'),
			};

			var reSizeHeight = function(){
				var strs = $.getWindowSize().toString().split(",");
				var jqgridHeight = strs[0] - 380;
				if (jqgridHeight < 150) {
					jqgridHeight = 150;
				}
				$(grid_selector).jqGrid('setGridHeight', jqgridHeight);
			};

			jQuery(grid_selector).jqGrid({
				url: '${ctx}/sys/log/list', //1这是数据的请求地址
				pager: pager_selector,//7
				colModel: [
					{name:'id',hidedlg:true,hidden:true,search:false,sortable:false,},
					{name:'title'},
					{name:'createBy.name',index:'u.name'},
					{name:'createBy.office.name',index:'o.name'},
					{name:'requestUri',index:'request_uri'},
					{name:'typeLabel',index:'a.type',
						formatter:function(cellvalue, options, rowObject){
							var valTmp = getDictLabel(pageData['sys_log_type'],rowObject['type']);
							return !!valTmp ? valTmp : '';
						}
					},
					{name:'remoteAddr',index:'remote_addr'},
					{name:'createDate',index:'a.create_date'},
					{name:'method',hidedlg:true,hidden:true,search:false,sortable:false,},
					{name:'params',hidedlg:true,hidden:true,search:false,sortable:false,},
					{name:'userAgent',hidedlg:true,hidden:true,search:false,sortable:false,},
					{name:'exception',hidedlg:true,hidden:true,search:false,sortable:false,},
				],//10
				rowList:[10,20,30],//11一个下拉选择框，用来改变显示记录数，当选择时会覆盖rowNum参数传递到后台
				colNames: ['ID','操作菜单','操作用户', '所在部门','URI','日志类型','操作者IP','操作时间','提交方式','提交参数','用户代理','异常信息'],//12
				subGrid: true, //23
				subGridOptions : {
					plusicon : "ace-icon fa fa-plus center bigger-110 blue",
					minusicon : "ace-icon fa fa-minus center bigger-110 blue",
					openicon : "ace-icon fa fa-chevron-right center orange"
				},
				subGridRowExpanded: function (subgridDivId, rowId) {
					var rowData = $(grid_selector).getRowData(rowId);
					var template = '<div class="profile-user-info profile-user-info-striped">\
						<div class="profile-info-row">\
							<div class="profile-info-name">用户代理</div>\
							<div class="profile-info-value">' + rowData.userAgent + '</div>\
						</div>\
						<div class="profile-info-row">\
							<div class="profile-info-name">方法</div>\
							<div class="profile-info-value">' + rowData.method + '</div>\
						</div>\
						<div class="profile-info-row">\
							<div class="profile-info-name">提交参数</div>\
							<div class="profile-info-value">' + rowData.params + '</div>\
						</div>\
						<div class="profile-info-row">\
							<div class="profile-info-name">异常信息</div>\
							<div class="profile-info-value">' + rowData.exception + '</div>\
						</div>\
					</div>';
					$("#" + subgridDivId).html(template);
				},
				loadComplete: function() {
					$.changeGridTable.changeStyle(this);
				},//37
				caption: "日志列表",//51
				toppager: false,//109
			});

			//search list by condition
			$("#query").click(function(){
				var loginName = $("#loginNameQuery").val();
				var title = $("#titleQuery").val();
				var uri = $("#requestUriQuery").val();
				var beginDate = $("#beginDateQuery").val();
				var endDate = $("#endDateQuery").val();
				var type = $("#typeQuery").val();
				$(grid_selector).jqGrid('setGridParam',{
					postData:{'createBy.loginName':loginName,'title':title,'requestUri':uri,'beginDate':beginDate,'endDate':endDate,'type':type}, //发送数据
					page:1
				}).trigger("reloadGrid"); //重新载入 
			});

			$("#reset").click(function(){
				$('.chosen-select').val('').trigger('chosen:updated');
				$(grid_selector).jqGrid('setGridParam',{
					postData:{'createBy.loginName':'','title':'','requestUri':'','beginDate':'','endDate':'','type':''},
					page:1
				}).trigger("reloadGrid"); //重新载入 
			});

			$.changeGridTable.changeSize([grid_selector], reSizeHeight);

			/////////////////////////////////////////////////////
			$(document).one('ajaxloadstart.page', function(e) {
				if (!!$.jgrid.gridUnload) {
					$.jgrid.gridUnload(grid_selector);
				}
				$('.ui-jqdialog').remove();
			});
		});
	});
</script>