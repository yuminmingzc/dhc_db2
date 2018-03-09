<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<title>区域管理</title>
<link href="${ctxStatic}/treeTable/themes/vsStyle/treeTable.min.css" rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/bootstrap-treeview/css/bootstrap-treeview.css" rel="stylesheet" type="text/css" />
<div class="row">
	<div class="col-xs-12">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th>区域名称</th><th>区域编码</th><th>区域类型</th><shiro:hasPermission name="sys:area:edit"><th>操作</th></shiro:hasPermission></tr></thead>
			<tbody><c:forEach items="${list}" var="area">
				<tr id="${area.id}" pId="${area.parent.id}">
					<td nowrap><a href="javascript:void(0);" data-action="view">${area.name}</a></td>
					<td title="${area.code}">${area.code}</td>
					<td >${fns:getDictLabel(area.type, 'sys_area_type', area.type)}</td>
					<shiro:hasPermission name="sys:area:edit">
					<td nowrap>
						<div class="action-buttons">
							<a data-action="edit" href="javascript:void(0);" class="tooltip-success green" data-rel="tooltip" title="编辑"><i class="ace-icon fa fa-pencil bigger-130"></i></a>
							<a data-action="delete" href="javascript:void(0);" class="tooltip-error red" data-rel="tooltip" title="删除"><i class="ace-icon fa fa-trash-o bigger-130"></i></a>
							<a data-action="add" href="javascript:void(0);" class="tooltip-info" data-rel="tooltip" title="添加下级区域"><i class="ace-icon fa fa-bars bigger-130"></i></a> 
						</div>
					</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach></tbody>
		</table>
	</div><!-- /.col -->
</div><!-- /.row -->
<div class="widget-box" style="display:none" id="editDivId"></div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var scripts = [null, '${ctxStatic}/treeTable/jquery.treeTable.min.js','${ctxStatic}/bootstrap-treeview/js/bootstrap-treeview.js', null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		jQuery(function($) {
			$("#treeTable").treeTable({expandLevel: 5});
			
			$("#treeTable").on('click', 'a[data-action=delete]', function(event) {
				var id = $(this).closest('tr').attr('id');
				deleteRow(id);
			}).on('click', 'a[data-action=edit]', function(event) {
				var id = $(this).closest('tr').attr('id');
				var params = {"id":id};
				editRow(params,'编辑区域');
			}).on('click', 'a[data-action=add]', function(event) {
				var id = $(this).closest('tr').attr('id');
				var params = {"parent.id":id};
				editRow(params,'新增区域');
			});
			
			function editRow(params,title){
				var optionsTmp = {
					base: {
						formUrl: '${ctx}/sys/area/form',
						formParams: params,
						success: function(){
							$(window).trigger('reload.ace_ajax');
						}
					},
					dialog: {
						title: title,
						titleIcon: 'ace-icon fa fa-delicious',
						width: 472,
						height: 500,
						create: function() {
							$( "#selectParentarea" ).on('click', function(e) {
								e.preventDefault();
								var $dialog = $("#selectParentareaDiv");
								if (!!$dialog.data('ui-dialog')) {
									$dialog.dialog('destroy');
								}
								$dialog.removeClass('hide').dialog({
									modal: true,
									width:300,
									height:400,
									title: "<div class='widget-header widget-header-small widget-header-flat'><h4 class='smaller'><i class='ace-icon fa fa-delicious'></i>&nbsp;选择上级区域</h4></div>",
									title_html: true,
									buttons: [ 
										{
											text: "确定",
											"class" : "btn btn-primary btn-minier",
											click: function() {
												$("#inputForm input#parent\\.name").val($('#treeview').attr('data-text'));
												$("#inputForm input#parent\\.id").val($('#treeview').attr('data-id'));
												$( this ).dialog( "close" ); 
											} 
										},
										{
											text: "取消",
											"class" : "btn btn-minier",
											click: function() {
												//$('#treeview').remove();
												$( this ).dialog( "close" ); 
											} 
										}
									],
									open: function( event, ui ) {
										$.getJSON( "${ctx}/sys/area/treeData",[],function(data) {
											$('#treeview').treeview({
												data: data,
												levels: 2,
												showBorder:true,
												emptyIcon:'fa fa-file-o',
												collapseIcon:'fa fa-folder-open-o',
												expandIcon:'fa fa-folder-o',
												onNodeSelected: function(event, node) {
													$('#treeview').attr('data-id',node.id);
													$('#treeview').attr('data-text',node.text);
												},
											});
										});
									}
								});
							});
						},
						open: function(){
							//选择框
							$('.chosen-select').chosenSuper({allow_single_deselect:true});
						},
					},
					validator: {
						fields : {
							name : {
								validators : {
									notEmpty : {
										message : "区域名称不能为空"
									}
								}
							}
						}
					}
				};
				if (!!params && !!params.id) {
					$.jGridCurdFn.doEdit(optionsTmp);
				} else {
					$.jGridCurdFn.doAdd(optionsTmp);
				}
			}
			
			function deleteRow(id){
				$.jGridCurdFn.doDelete({
					base: {
						formUrl: '${ctx}/sys/area/delete',
						formParams: {id: id},
						success: function() {
							var depth = $('tr#' + id).attr('depth');
							var result = new Array();
							result.push($('tr#' + id));
							$('tr#' + id + ' ~ tr').each(function(index,dom) {
								var trdepth = $(dom).attr('depth');
								if (trdepth <= depth) {
									return false;
								} else {
									result.push($(dom));
								}
							});
							$.each(result, function(n,value) {
								value.remove();
							});
						}
					},
					confirm: {
						msg: '要删除该区域及所有子区域项吗？',
					},
				});
			}
			
			/////////////////////////////////////////////////////
			$(document).one('ajaxloadstart.page', function(e) {
				var $dialog = $("#editDivId");
				if (!!$dialog.data('ui-dialog')) {
					$dialog.dialog('destroy');
				}
				$('.ui-dialog').remove();
			});
		});
	});
</script>
