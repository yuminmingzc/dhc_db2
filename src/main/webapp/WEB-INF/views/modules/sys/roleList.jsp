<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<link href="${ctxStatic}/bootstrap-treeview/css/bootstrap-treeview.css" rel="stylesheet" type="text/css"/>
<title>角色管理</title>
<div class="row">
	<div class="col-xs-12 col-sm-12">
		<table id="grid-table"></table>
		<div id="grid-pager"></div>
	</div>
</div>
<div class="widget-box" style="display:none" id="editDivId"></div>
<div id="assignFunctionDiv" class="hide widget-body">
	<div class="widget-main padding-8">
		<div id="treeview" class="" data-id="" data-text=""></div>
	</div>
</div>
<script type="text/javascript">
	var scripts = [null, '${ctxStatic}/bootstrap-treeview/js/bootstrap-treeview.js', null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function () {
		jQuery(function ($) {
			var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";
			var lockFlag = false;//请求锁	

			var pageData = {
				'sys_data_scope': $.parseJSON('${fns:toJson(fns:getDictList("sys_data_scope"))}'),
			};

			//resize to fit page size
			var reSizeHeight = function () {
				var strs = $.getWindowSize().toString().split(",");
				var gridTitlebar = $('div.ui-jqgrid-titlebar.ui-jqgrid-caption').first().outerHeight(true);
				var gridToppager = $('div.ui-jqgrid-toppager').first().outerHeight(true);
				var jqgridHdiv = $('div.ui-jqgrid-hdiv').first().outerHeight(true);
				var jqgridPagerBottom = $('div.ui-jqgrid-pager').first().outerHeight(true);

				var jqgrid_height = strs[0] - ( gridTitlebar + gridToppager + jqgridHdiv + jqgridPagerBottom);
				//显示内容过少时，默认显示6行内容

				(jqgrid_height <= 26 * 3) && (jqgrid_height = 26 * 6);
				//var jqgrid_height = strs[0];
				$(grid_selector).jqGrid('setGridHeight', jqgrid_height);
			};

			jQuery(grid_selector).jqGrid({
				url: '${ctx}/sys/role/list', //1这是数据的请求地址
				rowNum: 10000,//4
				pager: pager_selector,//7
// 				pgbuttons: false,//8定义上一页，下一页4个如上图所示的箭头导航按钮是否显示
// 				pginput: false,//9定义上图的“Page输入框 Of”是否显示
				colModel: [
					{name: 'id', hidedlg: true, hidden: true, search: false, sortable: false,},
					{name: 'name',},
					{name: 'enname'},
					{name: 'office.name'},
					{
						name: 'dataScope',
						formatter: function (cellvalue, options, rowObject) {
							var valTmp = getDictLabel(pageData['sys_data_scope'], rowObject['dataScope']);
							return !!valTmp ? valTmp : '';
						}
					},
					{
						name: 'opt', width: 50, sortable: false,
						formatter: function (cellvalue, options, rowObject) {
							var htmlTmp = '<div class="action-buttons" style="white-space:normal">\
								<a data-action="assign" data-id="' + rowObject.id + '"href="javascript:void(0);" class="tooltip-info" data-rel="tooltip" title="分配功能点">\
								<i class="ace-icon fa fa-bars bigger-130"></i></a>\
								<a data-action="edit" data-id="' + rowObject.id + '"href="javascript:void(0);" class="tooltip-success green" data-rel="tooltip" title="修改">\
								<i class="ace-icon fa fa-pencil bigger-130"></i></a>\
								<a data-action="delete" data-id="' + rowObject.id + '"href="javascript:void(0);" class="tooltip-error red" data-rel="tooltip" title="删除">\
								<i class="ace-icon fa fa-trash-o bigger-130"></i></a></div>';
							return htmlTmp;
						}
					},
				],//10
				rowList: [10, 20, 30],//11一个下拉选择框，用来改变显示记录数，当选择时会覆盖rowNum参数传递到后台
				colNames: ['ID', '角色名称', '英文名称', '归属机构', '数据范围', '操作'],//12
				loadComplete: function () {
					$.changeGridTable.changeStyle(this);
				},//37
				loadonce: true,//46
				caption: "角色管理",//51
			});

			$(grid_selector).on('click', 'a[data-action=assign]', function (event) {
				var idTmp = $(this).attr('data-id');
				var paramsTmp = {'id': idTmp};
				assignFunction(paramsTmp);
			}).on('click', 'a[data-action=edit]', function (event) {
				var idTmp = $(this).attr('data-id');
				openDialogEdit(idTmp);
			}).on('click', 'a[data-action=delete]', function (event) {
				var idTmp = $(this).attr('data-id');
				doDelete(idTmp);
			});

			//navButtons
			jQuery(grid_selector).jqGrid('navGrid', pager_selector,
				{ 	//navbar options
					add: true, addfunc: openDialogAdd, refresh: true,
				}, {}, {}, {}, {}, {}
			);

			function assignFunction(params) {
				var functions = new Array();
				var $dialog = $("#assignFunctionDiv");
				if (!!$dialog.data('ui-dialog')) {
					$dialog.dialog('destroy');
				}
				$dialog.removeClass('hide').dialog({
					modal: true,
					width: 300,
					height: 600,
					title: "<div class='widget-header widget-header-small widget-header-flat'><h4 class='smaller'><i class='ace-icon fa fa-bars'></i>&nbsp;分配功能点</h4></div>",
					title_html: true,
					buttons: [
						{
							text: "确定",
							"class": "btn btn-primary btn-minier",
							click: function () {
								var menus = functions.join(',');
								params.menus = menus;
								var that = $(this);
								$.post("${ctx}/sys/role/saveMenus", params, function (result) {
									if (result.messageStatus == "1") {
										$.msg_show.Init({
											'msg': result.message,
											'type': 'success'
										});
										that.dialog("close");
										$("#assignFunctionDiv").hide();
									} else {
										$.msg_show.Init({
											'msg': result.message,
											'type': 'error'
										});
									}
								});
							}
						},
						{
							text: "取消",
							"class": "btn btn-minier",
							click: function () {
								$(this).dialog("close");
								$("#assignFunctionDiv").hide();
							}
						}
					],
					open: function (event, ui) {
						$.getJSON("${ctx}/sys/role/menuTreeData", params, function (data) {
							functions = data.selectList;
							functions.remove("");
							$checkableTree = $('#treeview').treeview({
								data: data.data,
								color: "#428bca",
								levels: 4,
								showBorder: true,
								showIcon: true,
								showCheckbox: true,
								onNodeChecked: function (event, node) {
									functions.push(node.id);
									/* if(node.nodes != undefined){
										$checkableTree.treeview('checkNode',[ node.nodes, { silent: true }]);
										setChildrenChecked(node,'checkNode');
									} */
								},
								onNodeUnchecked: function (event, node) {
									functions.remove(node.id);
									/* if(node.nodes != undefined){
										$checkableTree.treeview('uncheckNode',[ node.nodes, { silent: true }]);
										setChildrenChecked(node,'uncheckNode');
									} */
								}
							});
							/* function setChildrenChecked(node,status){
								$.each(node.nodes,function(i,n){
									if(n.nodes != undefined){
										$checkableTree.treeview(status,[ n.nodes, { silent: true }]);
										setChildrenChecked(n,status);
									}
								});
							} */
						});
					}
				});
			}

			var optionsTmp = {
				base: {
					tableObj: $(grid_selector),
					formUrl: '${ctx}/sys/role/form',
				},
				dialog: {
					title: '角色维护',
					titleIcon: 'ace-icon fa fa-cogs',
					width: 500,
					height: 560,
					create: function () {
						$("#selectOfficeMenu").on('click', function (e) {
							e.preventDefault();
							$("#selectOfficeTreeDiv").removeClass('hide').dialog({
								modal: true,
								width: 300,
								height: 400,
								title: "<div class='widget-header widget-header-small widget-header-flat'><h4 class='smaller'><i class='ace-icon fa fa-university'></i>&nbsp;选择机构</h4></div>",
								title_html: true,
								buttons: [
									{
										text: "确定",
										"class": "btn btn-primary btn-minier",
										click: function () {
											$("#inputForm input#office\\.name").val($('#popuptreeview').attr('data-text'));
											$("#inputForm input#office\\.id").val($('#popuptreeview').attr('data-id'));
											$(this).dialog("close");
											$("#selectOfficeTreeDiv").hide();
										}
									},
									{
										text: "取消",
										"class": "btn btn-minier",
										click: function () {
											$(this).dialog("close");
										}
									}
								],
								open: function (event, ui) {
									$.getJSON("${ctx}/sys/office/treeData", {"treetype": 1}, function (data) {
										$('#popuptreeview').treeview({
											data: data,
											levels: 3,
											showBorder: true,
											emptyIcon: 'fa fa-file-o',
											collapseIcon: 'fa fa-folder-open-o',
											expandIcon: 'fa fa-folder-o',
											onNodeSelected: function (event, node) {
												$('#popuptreeview').attr('data-id', node.id);
												$('#popuptreeview').attr('data-text', node.text);
											},
										});
									});
								}
							});
						});
					},
					open: function () {
						//选择框 
						$('.select2').select2({allowClear: true});
					},
				},
				validator: {
// 					group: '.form-group-custom',
					fields: {
						name: {
							//enabled:false,
							validators: {
								notEmpty: {
									message: "角色名称不能为空"
								},
								remote: {
									message: "角色名称不能重复",
									url: '${ctx}/sys/role/checkNameAjax',
									data: function (validator) {
										return {id: validator.getFieldElements('id').val()};
									}
								}
							}
						},
						enname: {
							//enabled:false,
							validators: {
								notEmpty: {
									message: "英文名称不能为空"
								},
								remote: {
									message: "英文名称不能重复",
									url: '${ctx}/sys/role/checkEnnameAjax',
									data: function (validator) {
										return {id: validator.getFieldElements('id').val()};
									}
								}
							}
						}
					}
				}
			};

			function openDialogAdd() {
				if (lockFlag) {
					return false;
				}
				$.jGridCurdFn.doAdd(optionsTmp);
			}

			function openDialogEdit(id) {
				if (lockFlag) {
					return false;
				}
				$.jGridCurdFn.doEdit($.extend(true, {}, optionsTmp, {base: {'formParams': {'id': id}}}));
			}

			function doDelete(id) {
				if (lockFlag) {
					return false;
				}
				$.jGridCurdFn.doDelete({
					base: {
						tableObj: $(grid_selector),
						formUrl: '${ctx}/sys/role/delete',
						formParams: {'id': id},
					},
					confirm: {
						msg: '确认删除该角色吗？',
					},
				});
			}

			$.changeGridTable.changeSize([grid_selector], reSizeHeight);

			/////////////////////////////////////////////////////
			$(document).one('ajaxloadstart.page', function (e) {
				if (!!$.jgrid.gridUnload) {
					$.jgrid.gridUnload(grid_selector);
				}
				var $dialog = $("#editDivId");
				if (!!$dialog.data('ui-dialog')) {
					$dialog.dialog('destroy');
				}
				$('.ui-jqdialog').remove();
				$('[class*=select2]').remove();
			});
		});
	});
</script>
