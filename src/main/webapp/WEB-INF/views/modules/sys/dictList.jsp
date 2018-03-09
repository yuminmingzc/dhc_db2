<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<title>字典管理</title>
<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="widget-box widget-compact search-box">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h5 class="widget-title lighter">查询条件</h5>
				<div class="widget-toolbar">
					<a href="#" data-action="collapse"><i class="ace-icon fa fa-chevron-up"></i></a>
				</div>
			</div>
			<div class="widget-body">
				<div class="widget-main no-padding">
					<form class="form-horizontal">
						<div class="form-group">
							<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="typeQuery">类型:</label>
							<div class="col-xs-12 col-sm-3">
								<select id="typeQuery" class="chosen-select form-control" data-placeholder="点击选择...">
									<option value="" selected="selected"></option>
									<c:forEach items="${typeList}" var="item" varStatus="status">
										<option value="${item}">${item}</option>
									</c:forEach>
								</select>
							</div>
							<label class="control-label col-xs-12 col-sm-2 no-padding-right"
							       for="descriptionQuery">描述:</label>
							<div class="col-xs-12 col-sm-3">
								<input type="text" id="descriptionQuery" class="ace width-100"/>
							</div>
							<div class="col-xs-12 col-sm-2 no-padding-right">
								<button class="btn btn-info btn-sm" type="button" id="query">查询</button>
								<button class="btn btn-info btn-sm" type="reset" id="reset">重置</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="widget-box" id="fullScreenWidgetBox">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h4 class="widget-title lighter">字典列表</h4>
				<div class="widget-toolbar">
					<a href="#" data-action="fullscreen" class="orange2"><i class="ace-icon fa fa-expand"></i></a>
					<!--
						 <a href="#" data-action="reload"><i class="ace-icon fa fa-refresh"></i></a>
					  -->
					<a href="#" data-action="collapse"><i class="ace-icon fa fa-chevron-up"></i></a>
				</div>
			</div>
			<div class="widget-body">
				<div class="widget-main no-padding">
					<div class="row">
						<div class="col-xs-12">
							<table id="grid-table"></table>
							<div id="grid-pager"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="widget-box" style="display:none" id="editDivId"></div>
<script type="text/javascript">
	var scripts = [null, '${ctxStatic}/assets/js/fuelux/fuelux.spinner.js', null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function () {
		jQuery(function ($) {
			//选择框
			$('.chosen-select').chosenSuper({allow_single_deselect: true});

			var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";
			var lockFlag = false;//请求锁

			var heightBaseData = 340;
			var heightBaseDataTmp = 300;

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

			//全屏效果
			$('#fullScreenWidgetBox').off('fullscreened.ace.widget').on('fullscreened.ace.widget', function () {
				var self = $(this);
				if (self.hasClass('fullscreen')) {
					heightBaseDataTmp = heightBaseData;
					heightBaseData = 150;
				} else {
					heightBaseData = heightBaseDataTmp;
				}
				$(window).triggerHandler('resize.jqGrid');
			});

			jQuery(grid_selector).jqGrid({
				url: '${ctx}/sys/dict/list', //1这是数据的请求地址
				pager: pager_selector,//7
				colModel: [
					{name: 'value'},
					{name: 'label'},
					{name: 'type'},
					{name: 'description'},
					{name: 'sort'},
					{name: 'id', hidden: true}
				], //10
				rowList: [10, 20, 30],//11一个下拉选择框，用来改变显示记录数，当选择时会覆盖rowNum参数传递到后台
				colNames: ['键值', '标签', '类型', '描述', '排序', '操作'],//12
				loadComplete: function () {
					$.changeGridTable.changeStyle(this);
				},//37
				multiselect: true,//47
// 				caption: "字典列表",//51
				multiboxonly: true,//86
			}).jqGrid('navGrid', pager_selector,
				{ 	//navbar options
					edit: true,
					editfunc: openDialogEdit,
					add: true,
					addfunc: openDialogAdd,
					del: true,
					delfunc: doDelete,
					refresh: true,
				}, {}, {}, {}, {}, {}
			);

			//search list by condition
			$("#query").click(function () {
				var type = $("#typeQuery").val();
				var description = $("#descriptionQuery").val();
				$(grid_selector).jqGrid('setGridParam', {
					postData: {'description': description, 'type': type}, //发送数据
					page: 1
				}).trigger("reloadGrid"); //重新载入 
			});

			$("#reset").click(function () {
				$('.chosen-select').val('').trigger('chosen:updated');
				$(grid_selector).jqGrid('setGridParam', {
					postData: {'description': '', 'type': ''},
					page: 1
				}).trigger("reloadGrid"); //重新载入 
			});

			var optionsTmp = {
				base: {
					tableObj: $(grid_selector),
					formUrl: '${ctx}/sys/dict/form',
				},
				dialog: {
					title: '字典维护',
					titleIcon: 'ace-icon fa fa-book',
					create: function () {
						$('#dictDivId #sort').ace_spinner({
							value: 0,
							min: 0,
							max: 10000,
							step: 10,
							on_sides: true,
							icon_up: 'ace-icon fa fa-plus bigger-110',
							icon_down: 'ace-icon fa fa-minus bigger-110',
							btn_up_class: 'btn-success',
							btn_down_class: 'btn-danger'
						});
					},
				},
				validator: {
					group: '.form-group-custom',
					fields: {
						value: {
							validators: {
								notEmpty: {
									message: "键值不能为空"
								}
							}
						},
						label: {
							validators: {
								notEmpty: {
									message: "标签名称不能为空"
								}
							}
						},
						type: {
							validators: {
								notEmpty: {
									message: "类型不能为空"
								}
							}
						},
						description: {
							validators: {
								notEmpty: {
									message: "描述不能为空"
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

			function openDialogEdit() {
				if (lockFlag) {
					return false;
				}
				$.jGridCurdFn.doEdit(optionsTmp);
			}

			function doDelete() {
				if (lockFlag) {
					return false;
				}
				$.jGridCurdFn.doDelete({
					base: {
						tableObj: $(grid_selector),
						formUrl: '${ctx}/sys/dict/delete',
					},
				});
			}

			//---------------------------------resize------------------------------
			$.changeGridTable.changeSize([grid_selector], reSizeHeight);
			$(document).one('ajaxloadstart.page', function (e) {
				if (!!$.jgrid.gridUnload) {
					$.jgrid.gridUnload(grid_selector);
				}
				var $dialog = $("#editDivId");
				if (!!$dialog.data('ui-dialog')) {
					$dialog.dialog('destroy');
				}
				$('.ui-jqdialog').remove();
			});
			//------------------------------end------------------------------------
		});
	});
</script>