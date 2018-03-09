<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<title>用户管理</title>
<div class="row">
	<div class="col-sm-3 no-padding-right">
		<div class="widget-box widget-compact">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h5 class="widget-title lighter">组织机构</h5>
			</div>
			<div class="widget-body">
				<div class="widget-main no-padding">
					<div id="treeview" class="" data-id="" data-text=""></div>
				</div>
			</div>
		</div>
	</div>
	<div class="col-sm-9 user-pane">
		<div class="row">
		<div class="col-xs-12">
		<div class="widget-box widget-compact">
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
							<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="nameQuery">姓名:</label>
							<div class="col-xs-12 col-sm-3">
								<div class="clearfix input-group">
									<input type="text" id="nameQuery" class="ace width-100" />
								</div>
							</div>

							<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="loginNameQuery">登录名:</label>
							<div class="col-xs-12 col-sm-3">
								<div class="clearfix input-group">
									<input type="text" id="loginNameQuery" class="ace width-100" />
								</div>
							</div>

							<div class="col-xs-12 col-sm-2 no-padding-right">
								<div class="clearfix input-group">
									<button class="btn btn-info btn-sm" type="button" id="query">查询</button>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
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
	var scripts = [null,'${ctxStatic}/bootstrap-treeview/js/bootstrap-treeview.js',null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		jQuery(function($) {
			var lockFlag = false;//请求锁
			var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";
			var toolbarTop = grid_selector + '_toppager';
			
			var pageData = $.parseJSON('${pageData}');
			
			var officeId = '';//部门ID，用于传到后台
			var officeName = '';//部门名称，用于传到后台
			
			//用于获取查询条件
			function queryParams(){
				return {
					'office.id': officeId,
					'name': $("#nameQuery").val(),
					'loginName': $("#loginNameQuery").val()
				};
			}
			
			//查询方法
			function queryFn(){
				$("#grid-table").jqGrid('setGridParam',{ 
					postData: queryParams(), //发送数据 
					page: 1 
				}).trigger("reloadGrid"); //重新载入 
			}
			
			//查询事件
			$("#query").off('click').on('click', function(){
				queryFn();
			});
			
			$treeviewContainer = $('#treeview').closest('.widget-main');
			var reSizeHeight = function(first){
				var strs = $.getWindowSize().toString().split(",");
				var treeHeight = strs[0] - 40;
				var jqgridHeight = strs[0] - 240;
				if (jqgridHeight < 150) {
					jqgridHeight = 150;
				}
				$treeviewContainer.height(treeHeight);
				if(first){
					$treeviewContainer.ace_scroll({size: treeHeight});
				}else{
					$treeviewContainer.ace_scroll('update',{size: treeHeight});
					$(grid_selector).jqGrid('setGridHeight', jqgridHeight);
				}
			};
			
			$.getJSON( "${ctx}/sys/office/treeData",{treetype:'1'},function(data) {
				officeId = data[0].id;
				officeName = data[0].text;
				$('#treeview').treeview({
					data: data,
					levels: 3,
					showBorder: true,
					selectedBackColor: "skyblue",
					emptyIcon: 'fa fa-file-o red',
					collapseIcon: 'fa fa-folder-open-o red',
					expandIcon: 'fa fa-folder-o red',
					onNodeSelected: function(event, node) {
						$('#treeview').attr('data-id',node.id);
						$('#treeview').attr('data-text',node.text);
						officeId = node.id;
						officeName = node.text;
						queryFn();
					},
					onNodeCollapsed: function(event, node) {
						reSizeHeight();
					},
					onNodeExpanded: function(event, node) {
						reSizeHeight();
					},
				});
			});	
		
			//================================== 数据表格 start ==============================================
			jQuery(grid_selector).jqGrid({
				url: '${ctx}/sys/user/list', //1这是数据的请求地址
				pager: pager_selector,//7
				colModel:[
					{name:'id',hidedlg:true,hidden:true,search:false},
					{name:'name'},
					{name:'loginName',index:'login_name'},
					{name:'office.name'},
					{name:'mobile'},
					{name:'userTypeLabel',index:'user_type',
						formatter:function(cellvalue, options, rowObject){
							var valTmp = getDictLabel(pageData['sys_user_type'],rowObject['userType']);
							return !!valTmp ? valTmp : '';
						}
					},
					{name:'loginFlagLabel',index:'login_flag',
						formatter:function(cellvalue, options, rowObject){
							var valTmp = getDictLabel(pageData['yes_no'],rowObject['loginFlag']);
							return !!valTmp ? valTmp : '';
						}
					},
					{name:'opt',width:60,sortable:false,
						formatter:function(cellvalue, options, rowObject){
							var htmlTmp = '<div class="action-buttons" style="white-space:normal">\
							<a data-action="resetPwd" data-id="' + rowObject.id + '"href="javascript:void(0);" class="tooltip-success green" data-rel="tooltip" title="重置密码">\
							<i class="ace-icon fa fa-key bigger-130"></i></a></div>';
							return htmlTmp;
						}
					}
				],//10
				rowList:[10,20,30],//11一个下拉选择框，用来改变显示记录数，当选择时会覆盖rowNum参数传递到后台
				colNames: ['ID','姓名','登录名', '归属部门','手机号码','用户类型','是否允许登录','操作'],//12
				loadComplete: function() {
					$.changeGridTable.changeStyle(this);
				},//37
				multiselect: true,//47
				caption: "用户列表",//51
				multiboxonly: true,//86
			});
			
			//用户操作
			var userActionFn = {
				//导出
				'exportFile': function(){
					var formData = queryParams();
					var inputArrHtml = [];
					$.each(formData, function(index, item){
						inputArrHtml.push('<input type="text" name="' + index + '" value="' + item + '" />');
					});
					// 创建Form
					var $form = $('<form></form>');
					// 设置属性
					$form.attr('action', '${ctx}/sys/user/export');
					$form.attr('method', 'post');
					$form.attr('id', 'exportFileForm');
					// form的target属性决定form在哪个页面提交
					// _self -> 当前页面 _blank -> 新页面
					$form.attr('target', '_blank');
					// 附加到Form
					$form.html(inputArrHtml.join(''));
					// 提交表单
					$form.submit();
					// 注意return false取消链接的默认动作
					return false;
				},
				importFile: function(){
					$.uploadFn.dialogMode({
						upload: {
							allowExt: ["xlsx"],
							allowMime: ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"],
							doneFn: function(result, dialogObjTmp){
								if(result.messageStatus == '1') {
									dialogObjTmp.dialog('close');
									dialogObjTmp.dialog('destroy');
									$(grid_selector).jqGrid('clearGridData').trigger('reloadGrid');
								}
							},
						},
						dialogMode: {
							uploadUrl: '${ctx}/sys/user/import'
						}
					});
				},
				importFileTemplate: function(){
					// 创建Form
					var $form = $('<form></form>');
					// 设置属性
					$form.attr('action', '${ctx}/sys/user/import/template');
					$form.attr('method', 'post');
					// form的target属性决定form在哪个页面提交
					// _self -> 当前页面 _blank -> 新页面
					$form.attr('target', '_blank');
					// 附加到Form
					// 提交表单
					$form.submit();
					// 注意return false取消链接的默认动作
					return false;
				}
			};
			
			//navButtons
			jQuery(grid_selector).jqGrid('navGrid', pager_selector,
				{ 	//navbar options
					edit: true,editfunc: openDialogEdit,add: true,addfunc: openDialogAdd,del: true,delfunc: doDelete,refresh: true,
				}, {}, {}, {}, {}, {}
			).jqGrid('navButtonAdd', toolbarTop, {
				caption: '导出',
				buttonicon: 'ace-icon fa fa-file-excel-o bigger-115',
				onClickButton: userActionFn.exportFile,
				position: 'last',
				title: '导出excel',
				id: 'exportFile'
			}).jqGrid('navButtonAdd', toolbarTop, {
				caption: '导入',
				buttonicon: 'ace-icon fa fa-upload bigger-120',
				onClickButton: userActionFn.importFile,
				position: 'last',
				title: '导入excel',
				id: 'importFile'
			}).jqGrid('navButtonAdd', toolbarTop, {
				caption: '下载模板',
				buttonicon: 'ace-icon fa fa-download bigger-120',
				onClickButton: userActionFn.importFileTemplate,
				position: 'last',
				title: '下载导入用户数据模板',
				id: 'importFileTemplate'
			});
			
			var optionsTmp = {
				base: {
					tableObj: $(grid_selector),
					formUrl: '${ctx}/sys/user/form',
				},
				dialog: {
					title: '用户维护',
					titleIcon: 'ace-icon fa fa-users',
					width: 800,
					height: 560,
					create: function() {
						$("#selectOfficeMenu").on('click', function(e) {
							e.preventDefault();
							$( "#selectOfficeTreeDiv" ).removeClass('hide').dialog({
								modal: true,
								width:300,
								height:400,
								title: "<div class='widget-header widget-header-small widget-header-flat'><h4 class='smaller'><i class='ace-icon fa fa-university'></i>&nbsp;选择部门</h4></div>",
								title_html: true,
								buttons: [ 
									{
										text: "确定",
										"class" : "btn btn-primary btn-minier",
										click: function() {
											$("#inputForm input#office\\.name").val($('#popuptreeview').attr('data-text'));
											$("#inputForm input#office\\.id").val($('#popuptreeview').attr('data-id'));
											$( this ).dialog( "close" ); 
										} 
									},
									{
										text: "取消",
										"class" : "btn btn-minier",
										click: function() {
											$( this ).dialog( "close" ); 
										} 
									}
								],
								open: function() {
									$.getJSON( "${ctx}/sys/office/treeData",{"treetype":1},function(data) {
										$('#popuptreeview').treeview({
											data: data,
											levels: 3,
											showBorder:true,
											emptyIcon:'fa fa-file-o',
											collapseIcon:'fa fa-folder-open-o',
											expandIcon:'fa fa-folder-o',
											onNodeSelected: function(event, node) {
												$('#popuptreeview').attr('data-id',node.id);
												$('#popuptreeview').attr('data-text',node.text);
											},
										});
									});
								}
							});
						});
					},
					open: function() {
						//选择框 
						$('.select2').select2({allowClear:true});
					},
				},
				validator: {
					group: '.form-group-custom',
					fields: {
						name: {
							validators : {
								notEmpty : {
									message : "姓名不能为空"
								}
							}
						},
						loginName: {
	// 						enabled:false,
							validators : {
								notEmpty: {//非空验证：提示消息
									message: '登录名不能为空'
								},
	// 							threshold : 6 , //有6字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
	// 							stringLength: {
	// 								min: 6,
	// 								max: 30,
	// 								message: '登录名长度必须在6到30之间'
	// 							},
								remote : {
									message : "登录名不能重复",
									url:'${ctx}/sys/user/checkLoginNameAjax',
									delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
									type: 'POST',//请求方式
									//自定义提交数据，默认值提交当前input value
									data: function(validator){
										return {id : validator.getFieldElements('id').val()	};
									}
								},
								regexp: {
									regexp: /^[a-zA-Z0-9_\.]+$/,
									message: '登录名由数字字母下划线和.组成'
								}
							}
						},
						no: {
							validators : {
								notEmpty : {
									message : "工号不能为空"
								}
							}
						},
						email: {
							validators: {
								emailAddress: {
									message: '邮箱地址不正确'
								}
							}
						}
					}
				},
			};
			
			function openDialogAdd(){
				if (lockFlag) {
					return false;
				}
				$.jGridCurdFn.doAdd($.extend(true,{},optionsTmp,{base:{'formParams':{"pageOfficeId":officeId,"pageOfficeName":officeName}}}));
			}
			
			function openDialogEdit(){
				if (lockFlag) {
					return false;
				}
				$.jGridCurdFn.doEdit(optionsTmp);
			}
			
			function doDelete(){
				if (lockFlag) {
					return false;
				}
				$.jGridCurdFn.doDelete({
					base: {
						tableObj: $(grid_selector),
						formUrl: '${ctx}/sys/user/delete',
					},
					confirm: {
						msg: '要删除当前所选的用户吗？',
					},
				});
			}
			//================================== 数据表格 end ==============================================
			
			//================================== 重置密码 start ==============================================
			//绑定重置密码操作事件
			$(grid_selector).on('click', 'a[data-action=resetPwd]', function(event) {
				var idTmp = $(this).attr('data-id');
				var rowDataTmp = $(grid_selector).getRowData(idTmp);
				var paramsTmp = {'id':idTmp, 'loginName':rowDataTmp.loginName};
				resetPwd(paramsTmp);
			});
			
			function resetPwd(params){
				//信息确认插件
				$.msg_confirm.Init({
					'msg': '确定要重置当前用户的密码吗？',//这个参数可选，默认值：'这是信息提示！'
					'confirm_fn':function(){
						$.post("${ctx}/sys/user/resetPwd", params,function(result){
							if(result.messageStatus == "1"){
								$.msg_show.Init({
									'msg': result.message,
									'type': 'success'
								});
							} else {
								$.msg_show.Init({
									'msg': result.message,
									'type': 'error'
								});	
							}	
						});
					},//这个参数可选，默认值：function(){} 空的方法体
					'cancel_fn':function(){
						//点击取消后要执行的操作
					}//这个参数可选，默认值：function(){} 空的方法体
				});
			}
			//================================== 重置密码 end ==============================================
			reSizeHeight(true);
			$.changeGridTable.changeSize([grid_selector], reSizeHeight);
			
			/////////////////////////////////////////////////////
			$(document).one('ajaxloadstart.page', function(e) {
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