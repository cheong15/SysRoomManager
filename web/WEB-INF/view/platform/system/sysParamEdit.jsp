<%--
	time:2012-02-23 17:43:35
	desc:edit the 组织或人员参数属性
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 组织或人员参数属性</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=sysParam"></script>
	<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			if(${sysParam.paramId ==null}){
				valid(showRequest,showResponse,1);
			}else{
				valid(showRequest,showResponse);
			}
			$("a.save").click(function() {
				$('#sysParamForm').submit(); 
			});
		});
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
			    <c:choose>
			        <c:when test="${sysParam.paramId !=null}">
			            <span class="tbar-label">编辑组织或人员参数属性</span>
			        </c:when>
			        <c:otherwise>
			            <span class="tbar-label">添加组织或人员参数属性</span>
			        </c:otherwise>
			    </c:choose>
				
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="#"><span></span>保存</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link back" href="list.ht"><span></span>返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			
				<form id="sysParamForm" method="post" action="save.ht">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">参数名: </th>
							<td><input type="text" id="paramName" name="paramName" value="${sysParam.paramName}"  class="inputText"/></td>
						</tr>
						<tr>
							<th width="20%">参数Key: </th>
							<td><input type="text" id="paramKey" name="paramKey" value="${sysParam.paramKey}"  class="inputText"/></td>
						</tr>
						
						<tr>
							<th width="20%">参数类型: </th>
							<td>
								<select id="effect" name="effect" >
									<option value="1" <c:if test="${sysParam.effect==1}">selected="selected"</c:if>>用户参数</option>
									<option value="2" <c:if test="${sysParam.effect==2}">selected="selected"</c:if>>组织参数</option>
								</select>
							</td>
						</tr>
						
						<tr>
							<th width="20%">数据类型: </th>
							<td>
								<select id="dataType" name="dataType" >
									<c:forEach items="${dataTypeMap}" var="t">
										<option value="${t.key }" <c:if test="${sysParam.dataType==t.key}">selected="selected"</c:if>>${t.value }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
					</table>
					<input type="hidden" name="paramId" value="${sysParam.paramId}" />
				</form>
			
		</div>
</div>
</body>
</html>
