<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<title>修改密码</title>
<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="widget-box">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h4 class="widget-title lighter">密码设置</h4>
			</div>
			<div class="widget-body">
				<div class="widget-main no-padding">
					<form id="inputForm" action="${ctx}/sys/user/doModifyPwd" method="post" class="form-horizontal">
						<div class="form-group">
							<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="oldPassword">旧密码:</label>
							<div class="col-xs-12 col-sm-3">
								<div class="clearfix input-group">
									<span class="input-group-addon"><i class="ace-icon fa fa-lock"></i></span>
									<input type="password" name="oldPassword" id="oldPassword" class="width-100" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="newPassword">新密码:</label>
							<div class="col-xs-12 col-sm-3">
								<div class="clearfix input-group">
									<span class="input-group-addon"><i class="ace-icon fa fa-lock"></i></span>
									<input type="password" name="newPassword" id="newPassword" class="width-100" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="confirmNewPassword">确认新密码:</label>
							<div class="col-xs-12 col-sm-3">
								<div class="clearfix input-group">
									<span class="input-group-addon"><i class="ace-icon fa fa-lock"></i></span>
									<input type="password" name="confirmNewPassword" id="confirmNewPassword" class="width-100" />
								</div>
							</div>
						</div>
						<div class="clearfix form-actions">
							<div class="col-md-offset-3 col-md-9 no-padding-left">
								<button class="btn btn-info btn-sm" type="button" id="submitBtn">
									<i class="ace-icon fa fa-check bigger-110"></i> 保存
								</button>
								&nbsp; &nbsp; &nbsp;
								<button class="btn btn-sm" type="button" id="resetBtn">
									<i class="ace-icon fa fa-undo bigger-110"></i> 重置
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var scripts = [ null, null ];
	$('.page-content-area').ace_ajax('loadScripts',scripts,function() {
		jQuery(function($) {
			$('#inputForm').bootstrapValidator({
				message : '请录入一个有效值',
				excluded:':disabled, :hidden, :not(:visible)',
				feedbackIcons : {
					valid : 'glyphicon glyphicon-ok',
					invalid : 'glyphicon glyphicon-remove',
					validating : 'glyphicon glyphicon-refresh'
				},
				fields : {
					oldPassword : {
						validators : {
							notEmpty : {
								message : '旧密码不能位空'
							}
						}
					},
					newPassword : {
						validators : {
							notEmpty : {
								message : '新密码不能位空'
							}
						}
					},
					confirmNewPassword : {
						validators : {
							notEmpty : {
								message : '确认密码不能为空'
							},
							identical : {
								field : 'newPassword',
								message : '两次密码不一致'
							}
						}
					}
				},
				submitButtons: '#submitBtn',
				onSuccess: function(e) {
					var $form = $(e.target);
					$.post($form.attr('action'), $form.serialize(), function(result) {
						var status = result.messageStatus,type='success';
						if(status == '0'){type='error';}
						$.msg_show.Init({
							'msg':result.message,
							'type':type
						});
						$form.bootstrapValidator('disableSubmitButtons', false);
					}, 'json');
				},
			});

			$("#submitBtn").click(function() {
				$('#inputForm').submit();
			});

			$('#resetBtn').click(function() {
				$('#inputForm').data('bootstrapValidator').resetForm(true);
			});
			
			/////////////////////////////////////
			$(document).one('ajaxloadstart.page', function(e) {
				//in ajax mode, remove remaining elements before leaving page
				
			});
		});
	});
</script>