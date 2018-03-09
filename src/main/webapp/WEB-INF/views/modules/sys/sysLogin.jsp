<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="com.dhc.rad.common.config.Global" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>${fns:getConfig('productName')} 登录</title>

		<meta name="description" content="用户登录页面" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${ctxStatic}/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="${ctxStatic}/assets/css/font-awesome.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="${ctxStatic}/assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="${ctxStatic}/assets/css/ace.css" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${ctxStatic}/assets/css/ace-part2.css" />
		<![endif]-->
		<link rel="stylesheet" href="${ctxStatic}/assets/css/ace-rtl.css" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${ctxStatic}/assets/css/ace-ie.css" />
		<![endif]-->

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lt IE 9]>
		<script src="${ctxStatic}/assets/js/html5shiv.js"></script>
		<script src="${ctxStatic}/assets/js/respond.js"></script>
		<![endif]-->
	</head>

	<body class="login-layout blur-login">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center">
								<h1>
									<img alt="" src="${pageContext.request.contextPath}/static/images/logo.png"/>
								</h1>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger" id="loginMessageBox">
												<i class="ace-icon fa fa-coffee green"></i>
												请输入用户名和密码
											</h4>

											<div class="space-6"></div>

											<form id="loginForm" action="${ctx}/login" method="post">
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input id="username" name="username" type="text" class="form-control" placeholder="用户名" />
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input id="password" name="password" type="password" class="form-control" placeholder="密码" />
															<input type="hidden" id="ajaxReq" name="ajaxReq" value="true" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>

													<div class="space"></div>

													<div class="clearfix">
														<label class="inline">
															<input id="rememberMe" name="rememberMe" type="checkbox" class="ace" />
															<span class="lbl">记住我</span>
														</label>

														<button id="loginBtn" type="submit" class="width-35 pull-right btn btn-sm btn-primary" data-loading-text="登录中...">
															<i class="ace-icon fa fa-key"></i>
															<span class="bigger-110">登录</span>
														</button>
													</div>

													<div class="space-4"></div>
												</fieldset>
											</form>
										</div><!-- /.widget-main -->
									</div><!-- /.widget-body -->
								</div><!-- /.login-box -->
							</div><!-- /.position-relative -->

							<div class="navbar-fixed-top align-right">
								<br />
								&nbsp;
								<a id="btn-login-dark" href="#">暗色</a>
								&nbsp;
								<span class="blue">/</span>
								&nbsp;
								<a id="btn-login-blur" href="#">蓝色</a>
								&nbsp;
								<span class="blue">/</span>
								&nbsp;
								<a id="btn-login-light" href="#">浅色</a>
								&nbsp; &nbsp; &nbsp;
							</div>
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${ctxStatic}/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${ctxStatic}/assets/js/jquery1x.js'>"+"<"+"/script>");
		</script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${ctxStatic}/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		
		<script src="${ctxStatic}/assets/js/jquery.validate.js"></script>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
				var lockFlag = false;//请求锁
				//改变登录背景颜色（暗色）
				$('#btn-login-dark').on('click', function(e) {
					$('body').attr('class', 'login-layout');
					e.preventDefault();
				});
				
				//改变登录背景颜色（浅色）
				$('#btn-login-light').on('click', function(e) {
					$('body').attr('class', 'login-layout light-login');					
					e.preventDefault();
				});
				
				//改变登录背景颜色（蓝色）
				$('#btn-login-blur').on('click', function(e) {
					$('body').attr('class', 'login-layout blur-login');
					e.preventDefault();
				});
				
				//表单验证，采用jquery.validate
				$('#loginForm').validate({
					messages: {
						username: {required: "请输入用户名."},
						password: {required: "请输入密码."}
					},
					rules: {
						username: {
							required: true
						},
						password: {
							required: true
						}
					},
					errorClass: 'help-block',
					errorElement: 'div',
					highlight: function( element, errorClass, validClass ) {
						$(element).parent('span').removeClass('has-info').addClass('has-error');
					},
					success: function ( errElement, element ) {
						$(errElement).parent('span').removeClass('has-error');
						$(errElement).remove();
					},
					submitHandler: function(form,event){
						if (lockFlag) {
							return false;
						}
						lockFlag = true;
						$('#loginBtn').data('resetText',$('#loginBtn').children('span').text());
						$('#loginBtn').prop('disabled',true).children('span').text($('#loginBtn').attr('data-loading-text'));
						var self = $(form);
						$.post(self.attr("action"), self.serialize(), loginCallback, "json");
						return false;
					}
				});
				
				//ajax回调函数
				function loginCallback(data){
					if(data.messageStatus === '<%=Global.SUCCESS %>'){
						if ($('#loginMessageBox').hasClass('red')) {
							$('#loginMessageBox').removeClass('red').addClass('blue');
						}
						$('#loginMessageBox').html('<i class="ace-icon fa fa-coffee green"></i>登录成功.页面正在跳转~');
						setTimeout(function(){
							window.location.href = '${ctx}';
						},1500);
					} else {
						$('#loginMessageBox').html('<i class="ace-icon fa fa-coffee red"></i>' + data.message).removeClass('blue').addClass('red');
						$('#loginBtn').prop('disabled',false).children('span').text($('#loginBtn').data('resetText'));
						lockFlag = false;
					}
				}
			});
		</script>
	</body>
</html>