<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<style>
	.updateLog ul > li, .updateLog ol > li {
		font-size: 16px;
	}

	.updateLog ul p, .updateLog ol p {
		font-size: 14px;
	}

</style>
<title>系统更新日志</title>
<div class="updateLog row">
	<div class="widget-box">
		<div class="widget-header widget-header-flat">
			<h4 class="widget-title">系统更新日志</h4>
		</div>

		<div class="widget-body">
			<div class="widget-main">
				<div class="row">
					<div class="col-sm-6">
						<%--以下是系统更新日志记录--%>
						<hr/>
						<h3>v2017.08.19</h3>
						<ol>
							<li>系统初版发布</li>
						</ol>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	var scripts = [null, null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function () {});
</script>
