<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<title>菜单管理</title>
<link href="${ctxStatic}/treeTable/themes/vsStyle/treeTable.min.css" rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/bootstrap-treeview/css/bootstrap-treeview.css" rel="stylesheet" type="text/css" />
<div class="row">
	<div class="col-xs-12">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th>名称</th><th>链接</th><th style="text-align:center;">排序</th><th>可见</th><th>权限标识</th><shiro:hasPermission name="sys:menu:edit"><th>操作</th></shiro:hasPermission></tr></thead>
			<tbody>
			<c:forEach items="${list}" var="menu">
				<tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
					<td nowrap><i class="menu-icon fa ${not empty menu.icon?menu.icon:' hide'}"></i><a href="javascript:void(0);" data-action="view">${menu.name}</a></td>
					<td title="${menu.href}">${fns:abbr(menu.href,50)}</td>
					<td style="text-align:center;">
						<shiro:hasPermission name="sys:menu:edit">
						<span class="editable" id="sort-${menu.id}">${menu.sort}</span>
						</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">
							${menu.sort}
						</shiro:lacksPermission>
					</td>
					<td>${menu.isShow eq '1'?'显示':'隐藏'}</td>
					<td title="${menu.permission}">${fns:abbr(menu.permission,50)}</td>
					<shiro:hasPermission name="sys:menu:edit">
					<td nowrap>
						<div class="action-buttons">
						<a data-action="edit" href="javascript:void(0);" class="tooltip-success green" data-rel="tooltip" title="编辑"><i class="ace-icon fa fa-pencil bigger-130"></i></a>
						<a data-action="delete" href="javascript:void(0);" class="tooltip-error red" data-rel="tooltip" title="删除"><i class="ace-icon fa fa-trash-o bigger-130"></i></a>
						<a data-action="add" href="javascript:void(0);" class="tooltip-info" data-rel="tooltip" title="添加下级菜单"><i class="ace-icon fa fa-bars bigger-130"></i></a> 
						</div>
					</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div><!-- /.col -->
</div><!-- /.row -->
<div class="widget-box" style="display:none" id="editDivId"></div>
<div id="selectIconDiv" class="hide widget-body"></div>
<!-- page specific plugin scripts -->
<script type="text/javascript">	
	var scripts = [null, '${ctxStatic}/treeTable/jquery.treeTable.min.js','${ctxStatic}/bootstrap-treeview/js/bootstrap-treeview.js',
					'${ctxStatic}/assets/js/x-editable/bootstrap-editable.js','${ctxStatic}/assets/js/x-editable/ace-editable.js','${ctxStatic}/assets/js/fuelux/fuelux.spinner.js',null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		jQuery(function($) {
			$("#treeTable").treeTable({expandLevel: 5});

			$("#treeTable").on('click', 'a[data-action=delete]', function(event) {
				var id = $(this).closest('tr').attr('id');
				deleteRow(id);
			}).on('click', 'a[data-action=edit]', function(event) {
				var id = $(this).closest('tr').attr('id');
				var params = {"id":id};
				editRow(params,'编辑菜单');
			}).on('click', 'a[data-action=add]', function(event) {
				var id = $(this).closest('tr').attr('id');
				var params = {"parent.id":id};
				editRow(params,'新增菜单');
			});

			$.plugInit.editable('popup');

			$("span[id^=sort-]").each(function(index,sortDom){
				var id = $(sortDom).attr('id').slice(5);
				$(sortDom).editable({
					emptytext: '未配置',
					success: function(response,newValue){
						if(response.messageStatus=="1"){
							$.msg_show.Init({
								'msg':response.message,
								'type':'success'
							});
							return {newValue:newValue};
						} else {
							$.msg_show.Init({
								'msg':response.message,
								'type':'error'
							});
						}
						return;
					},
					type: 'spinner',
					name: 'sort',
					params: function(params){
						var paramsTmp = {'id':params['pk'], 'sort':params['value']};
						return paramsTmp;
					},
					pk: id,
					url:"${ctx}/sys/menu/updateSort",
					spinner: {
						min: 0,
						max: 1000,
						step: 10,
						on_sides: true
					},
					
				});
			});

			function editRow(params,title){
				var optionsTmp = {
					base: {
						formUrl: '${ctx}/sys/menu/form',
						formParams: params,
						success: function(){
							$(window).trigger('reload.ace_ajax');
						}
					},
					dialog: {
						title: title,
						titleIcon: 'ace-icon fa fa-bars',
						width: 472,
						height: 665,
						create: function() {
							$('#menuDivId #sort').ace_spinner({value:0,min:0,max:10000,step:10, on_sides: true, icon_up:'ace-icon fa fa-plus bigger-110', icon_down:'ace-icon fa fa-minus bigger-110', btn_up_class:'btn-success' , btn_down_class:'btn-danger'});
							$( "#selectParentMenu" ).on('click', function(e) {
								e.preventDefault();
								var $dialog = $("#selectParentMenuDiv");
								if (!!$dialog.data('ui-dialog')) {
									$dialog.dialog('destroy');
								}
								$dialog.removeClass('hide').dialog({
									modal: true,
									width:300,
									height:400,
									title: "<div class='widget-header widget-header-small widget-header-flat'><h4 class='smaller'><i class='ace-icon fa fa-bars'></i>&nbsp;选择上级菜单</h4></div>",
									title_html: true,
									buttons: [ 
										{
											text: "确定",
											"class" : "btn btn-primary btn-minier",
											click: function() {
											//	$('#treeview').remove();
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
										$.getJSON( "${ctx}/sys/menu/treeData",[],function(data) {
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
							$("#selectIconMenu").on('click', function (e) {
														e.preventDefault();
														$.post('${ctx}/sys/menu/iconselect', {}, function (data, textStatus, object) {
															var $selectIconDiv = $("#selectIconDiv");
															$selectIconDiv.html(object.responseText).removeClass('hide').dialog({
																modal: true,
																width: 1000,
																height: 618,
																title: "<div class='widget-header widget-header-small widget-header-flat'><h4 class='smaller'><i class='ace-icon fa fa-key'></i>&nbsp;图标选择</h4></div>",
																title_html: true,
																buttons: [
																	{
																		text: "取消",
																		"class": "btn btn-minier",
																		click: function () {
																			$(this).dialog("destroy");
																		}
																	}
																],
																open: function (event, ui) {
																	$("#icons .fa-hover a").dblclick(function () {
																		var icon = $(this).find('i').attr('class');
																		$("#icon").val(icon);
																		$selectIconDiv.dialog("close");
																	});
																}
															});
														});
													});
						},
					},
					validator: {
						fields : {
							name : {
								validators : {
									notEmpty : {
										message : "菜单名称不能为空"
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
						formUrl: '${ctx}/sys/menu/delete',
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
						msg: '要删除该菜单及所有子菜单项吗？',
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
