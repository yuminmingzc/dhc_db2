<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<title>我的通知</title>
<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="widget-box widget-compact search-box">
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
					<form:form id="searchForm" modelAttribute="notify" class="form-horizontal">
						<div class="form-group">
							<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="title">标题:</label>
							<div class="col-xs-12 col-sm-3">
								<form:input path="title" htmlEscape="false" maxlength="200" class="ace width-100"/>
							</div>

							<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="type">类型:</label>

							<div class="col-xs-12 col-sm-3">
								<form:select path="type" class="chosen-select form-control" data-placeholder="点击选择...">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('sys_notify_type')}" itemLabel="label"
									              itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-xs-12 col-sm-1 no-padding-right"
							       for="readFlag">查阅状态:</label>

							<div class="col-xs-12 col-sm-3">
								<form:select path="readFlag" class="chosen-select form-control"
								             data-placeholder="点击选择...">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('sys_notify_read')}" itemLabel="label"
									              itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>

							<label class="control-label col-xs-12 col-sm-1 no-padding-right"
							       for="urgentFlag">紧急度:</label>

							<div class="col-xs-12 col-sm-3">
								<form:select path="urgentFlag" class="chosen-select form-control"
								             data-placeholder="点击选择...">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('sys_notify_urgent')}" itemLabel="label"
									              itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-1 no-padding-right">
								&nbsp;
							</div>
							<div class="col-xs-12 col-sm-3 no-padding-right">
								<button class="btn btn-info btn-sm" type="button" id="query">
									查询
								</button>
								<button class="btn btn-info btn-sm" type="reset" id="reset">
									重置
								</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		<table id="grid-table"></table>
		<div id="grid-pager"></div>
		<div class="widget-box" style="display:none" id="viewDivId">
			<div class="profile-user-info profile-user-info-striped">
				<div class="profile-info-row">
					<div class="profile-info-name"> 标题</div>

					<div class="profile-info-value">
						<span id="title"></span>
					</div>
				</div>

				<div class="profile-info-row">
					<div class="profile-info-name"> 发送人</div>
					<div class="profile-info-value">
						<span id="sender"></span>
					</div>
				</div>

				<div class="profile-info-row">
					<div class="profile-info-name">通知时间</div>
					<div class="profile-info-value">
						<span id="createDate"></span>
					</div>
				</div>

				<div class="profile-info-row">
					<div class="profile-info-name">通知内容</div>
					<div class="profile-info-value">
						<span id="content"></span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var scripts = [null, null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function () {
		jQuery(function ($) {
			//选择框
			if (!ace.vars['touch']) {
				$('.chosen-select').chosen({allow_single_deselect: true});
				//resize the chosen on window resize

				$(window).off('resize.chosen').on('resize.chosen', function () {
					$('.chosen-select').each(function () {
						var $this = $(this);
						$this.next().css({'width': $this.parent().width()});
					});
				}).trigger('resize.chosen');
				//resize chosen on sidebar collapse/expand
				$(document).on('settings.ace.chosen', function (e, event_name, event_val) {
					if (event_name != 'sidebar_collapsed') return;
					$('.chosen-select').each(function () {
						var $this = $(this);
						$this.next().css({'width': $this.parent().width()});
					});
				});
			}

			var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";

			var reSizeHeight = function () {

				var strs = $.getWindowSize().toString().split(",");
				var searchBox = $('div.search-box').first().outerHeight(true);
				var gridTitlebar = $('div.ui-jqgrid-titlebar.ui-jqgrid-caption').first().outerHeight(true);
				var gridToppager = $('div.ui-jqgrid-toppager').first().outerHeight(true);
				var jqgridHdiv = $('div.ui-jqgrid-hdiv').first().outerHeight(true);
				var jqgridPagerBottom = $('div.ui-jqgrid-pager').first().outerHeight(true);

				var jqgrid_height = strs[0] - (searchBox + gridTitlebar + gridToppager + jqgridHdiv + jqgridPagerBottom);
				//显示内容过少时，默认显示6行内容
				
				(jqgrid_height <= 26 * 3) && (jqgrid_height = 26 * 6);
				//var jqgrid_height = strs[0];
				$(grid_selector).jqGrid('setGridHeight', jqgrid_height);

			};

			jQuery(grid_selector).jqGrid({
				datatype: "json", //将这里改为使用JSON数据    
				url: '${ctx}/sys/notify/notifyList', //这是数据的请求地址
				height: 'auto',
				autowidth: true,

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
					viewoper: "view",
					subgridid: "id", // 当点击以载入数据到子表时，传递的数据名称
					npage: null,
					totalrows: "totalrows" // 表示需从Server得到总共多少行数据的参数名称，参见jqGrid选项中的rowTotal
				},
				colNames: ['标题', '类型', '紧急度', '查阅状态', '消息时间', '发送人', '操作', '内容', '主键'],
				colModel: [
					{name: 'title', index: 'title'},
					{name: 'type', index: 'type'},
					{name: 'urgentFlag', index: 'urgentFlag'},
					{name: 'readFlag', index: 'readFlag'},
					{name: 'createDate', index: 'createDate'},
					{name: 'sender.name', index: 'senderId'},
					{name: 'View', index: 'View', width: 30,},
					{name: 'content', index: 'content', title: false, hidden: true},
					{name: 'id', index: 'id', width: 30, hidden: true}
				],

				viewrecords: true,
				rowNum: 20,
				rowList: [10, 20, 30],
				pager: pager_selector,
				altRows: true,
				toppager: true,
				multiselect: true,

				//multiselect: true,
				//multiboxonly: true,


				loadComplete: function () {
					$.changeGridTable.changeStyle(this);
					$(grid_selector + "_toppager_center").remove();
					$(grid_selector + "_toppager_right").remove();
					$(pager_selector + "_left table").remove();
//                    autoChangeWidth(grid_selector);
				},

				caption: "通知列表",

				gridComplete: function () {
					var ids = $(grid_selector).jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = $(grid_selector).getRowData(id);
						var type = getDictLabel(${fns:toJson(fns:getDictList("sys_notify_type"))}, rowData.type);
						var readFlag = getDictLabel(${fns:toJson(fns:getDictList("sys_notify_read"))}, rowData.readFlag);
						var urgentFlag = getDictLabel(${fns:toJson(fns:getDictList("sys_notify_urgent"))}, rowData.urgentFlag);
						$(grid_selector).jqGrid('setRowData', ids[i], {
							type: type,
							readFlag: readFlag,
							urgentFlag: urgentFlag
						});

						if (urgentFlag == "紧急" && readFlag == "未读") {
							$(grid_selector).jqGrid('setRowData', ids[i], null, {background: "red"});
						}
						if (urgentFlag == "普通" && readFlag == "未读") {
							$(grid_selector).jqGrid('setRowData', ids[i], null, {background: "yellow"});
						}
						var viewBtn = '<div class="action-buttons" style="white-space:normal">\
			         		<a data-action="view" data-id="' + rowData.id + '"href="javascript:void(0);" class="tooltip-success green" data-rel="tooltip" title="查看">\
			         		<i class="ace-icon fa fa-file-text-o bigger-130"></i></a></div>';
						$(grid_selector).jqGrid('setRowData', ids[i], {View: viewBtn});
					}
					$(grid_selector).find('a[data-action=view]').on('click', function (event) {
						var id = $(this).attr('data-id');
						var rowData = $(grid_selector).getRowData(id);
						var params = {
							"id": id,
							"title": rowData.title,
							"sender": rowData['sender.name'],
							"createDate": rowData.createDate,
							"content": rowData.content,
							"readFlag": rowData.readFlag
						};
						viewNotify(params);
					});
				}
			});

			$.changeGridTable.changeSize([grid_selector, grid_selector + " ~ .widget-box"], reSizeHeight);

			//search list by condition
			$("#query").click(function () {
				var type = $("#type").val();
				var title = $("#title").val();
				var readFlag = $("#readFlag").val();
				var urgentFlag = $("#urgentFlag").val();
				$(grid_selector).jqGrid('setGridParam', {
					url: "${ctx}/sys/notify/notifyList",
					mtype: "post",
					postData: {'title': title, 'type': type, 'readFlag': readFlag, 'urgentFlag': urgentFlag}, //发送数据
					page: 1
				}).trigger("reloadGrid"); //重新载入
			});

			$("#reset").click(function () {
				$('.chosen-select').val('').trigger('chosen:updated');
				$(grid_selector).jqGrid('setGridParam', {
					url: "${ctx}/sys/notify/notifyList",
					mtype: "post",
					postData: {'title': '', 'type': '', 'readFlag': '', 'urgentFlag': ''},
					page: 1
				}).trigger("reloadGrid"); //重新载入
			});
			$('#type,#readFlag,#urgentFlag').on('change', function () {//选中下拉框值后自动查询
				$("#query").click();
			});
			var gridNav = jQuery(grid_selector).jqGrid('navGrid', pager_selector,
				{//navbar options
					edit: true,
					editicon: 'fa fa-envelope-open-o',
					editfunc: readAll,
					edittext: "标记为已读",
					edittitle: '',
					add: false,
					del: false,
					search: false,
					refresh: true,
					refreshicon: 'ace-icon fa fa-refresh green',
					refreshtext: "刷新",
					refreshtitle: '',
					view: false,
					viewicon: 'ace-icon fa fa-file-text-o green',
					//viewfunc: viewNotify(),
//                    viewfunc: readAll(),
					viewtext: "查看",
					viewtitle: '',
					cloneToTop: true
				},
				{}, // use default settings for edit
				{}, // use default settings for add
				{},  // delete instead that del:false we need this
				{multipleSearch: true},// enable the advanced searching
				{closeOnEscape: true}
			);

//            gridNav.navButtonAdd(pager_selector, {
//				caption: "测试按钮",
//				buttonicon: "ace-icon fa fa-pencil blue",
//				onClickButton: function () {
//					alert("test button");
//				},
//				position: "last"
//			});
//            $(grid_selector + "_toppager_center").remove();
//            $(grid_selector + "_toppager_right").remove();
//            //移动按钮-----------在此之前进行按钮自定义
//            $(grid_selector + '_toppager_left').append($(pager_selector + '_left table'));
			//override dialog's title function to allow for HTML titles


			$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
				_title: function (title) {
					var $title = this.options.title || '&nbsp;';
					if (("title_html" in this.options) && this.options.title_html == true)
						title.html($title);
					else title.text($title);
				}
			}));

			function readAll() {
				var selectedIds = $(grid_selector).jqGrid("getGridParam", "selarrrow");
				console.log(selectedIds);
				var arrayObj = "";
				for (i = 0; i < selectedIds.length; i++) {
					arrayObj = (arrayObj + selectedIds[i]) + (((i + 1) == selectedIds.length) ? '' : ',');
				}
				var params = {"ids": arrayObj};
				console.log(params);
				$.post("${ctx}/sys/notify/setAllRead", params, function (data, textStatus, object) {
					if (data.messageStatus == "1") {
						$(grid_selector).jqGrid('setRowData', params.id, {readFlag: '已读'});
						$.get("${ctx}/sys/notify/count?updateSession=0&t=" + new Date().getTime(), function (data) {
							if (data != '0') {
								if ($("#notifyLi:hidden").length != 0) {
									$("#notifyLi").show();
								}
								$("#unReadNotifys").html(data);
							} else {
								$("#notifyLi").hide();
							}
						});
						$.msg_show.Init({
							'msg': data.message,
							'type': 'success',
							'time': 1000
						});
						$('#grid-table').trigger("reloadGrid");
					} else {
						$.msg_show.Init({
							'msg': data.message,
							'type': 'error',
							'time': 3000
						});
					}
				});
			}


			function viewNotify(params) {
				$(".ui-dialog").remove();
				var title = params['title'] == null || params['title'] == '' ? "无" : params['title'];
				var sender = params['sender'] == null || params['sender'] == '' ? "无" : params['sender'];
				var content = params['content'] == null || params['content'] == '' ? "无" : params['content'];
				$("#viewDivId #title").html(title);
				$("#viewDivId #sender").html(sender);
				$("#viewDivId #createDate").html(params['createDate']);
				$("#viewDivId #content").html(content);
				$("#viewDivId").removeClass('hide').dialog({
					modal: false,
					width: 800,
					height: 'auto',
					title: "<div class='widget-header widget-header-small widget-header-flat'><h4 class='smaller'><i class='ace-icon fa fa-indent'></i>&nbsp;查看通知</h4></div>",
					title_html: true,
					buttons: [
						{
							text: "关闭",
							"class": "btn  btn-minier",
							click: function () {
								$(this).dialog("close");
							}
						}
					],
					open: function (event, ui) {
						if (params.readFlag == '未读') {
							$.get("${ctx}/sys/notify/setRead", params, function (data, textStatus, object) {
								if (textStatus == 'success') {
									$(grid_selector).jqGrid('setRowData', params.id, {readFlag: '已读'});
									$.get("${ctx}/sys/notify/count?updateSession=0&t=" + new Date().getTime(), function (data) {
										if (data != '0') {
											if ($("#notifyLi:hidden").length != 0) {
												$("#notifyLi").show();
											}
											$("#unReadNotifys").html(data);
										} else {
											$("#notifyLi").hide();
										}
									});
								}
							});
						}
						$("[aria-describedby='viewDivId']").css("box-shadow", "rgb(134, 134, 134) 6px 3px 9px 1px");
					},
				});
			}

			//自动根据列内容最大长度进行调整列宽 WARNING！：务必将按钮列添加[sortable: false]属性
			function autoChangeWidth(grid_selector) {
				var colModel = $(grid_selector).jqGrid('getGridParam', 'colModel');
				for (var col in colModel) {
					var model = colModel[col];
					if (model.name === 'cb'/*←checkBox*/) {
						continue;
					}
					if (model.hidden || !model.sortable) {
						continue;
					}
					var colValues = $(grid_selector).jqGrid('getCol', model.name, false);
					var maxLen = 0, maxLenStr = '';
					colValues.forEach(function (v, i, a) {
						var len = v.length;
						//maxLen = maxLen > len ? maxLen: len;
						if (maxLen < len) {
							maxLen = len;
							maxLenStr = v;
						}
					}, this);
					//console.log("len:" + maxLen + "   Str:" + maxLenStr);
					//$(grid_selector).jqGrid('setColProp', model.name, {width: {value: calcActualWidth(maxLenStr)}});
					//$(grid_selector).setColProp(model.name, {width: calcActualWidth(maxLenStr)});
					$(grid_selector).jqGrid('setColProp', model.name, {widthOrg: calcActualWidth(maxLenStr)});
					var gw = $(grid_selector).jqGrid('getGridParam', 'width');
					$(grid_selector).jqGrid('setGridWidth', gw, true);
				}
			}

			function calcActualWidth(str) {
				if (typeof str !== "string") {
					return 150;
				}    //未知错误下默认值
				var d = $('#divCompute');
				d.text(str);
				return d.width() < 60 ? 60 : d.width() + 10;
			}

			$(document).one('ajaxloadstart.page', function (e) {
				$.jgrid.gridUnload(grid_selector);
				$('.ui-dialog').remove();
				$('.ui-jqdialog').remove();
				$('[class*=select2]').remove();
				$('.ui-helper-hidden-accessible').remove();
			});
		});
	});
</script>