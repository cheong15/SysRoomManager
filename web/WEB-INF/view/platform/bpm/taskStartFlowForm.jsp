<%@ page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
	<head>
		<title>流程启动--${bpmDefinition.subject} --版本:${bpmDefinition.versionNo}</title>
		<%@include file="/commons/include/customForm.jsp" %>
		<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/BpmImageDialog.js"></script>
		<script type="text/javascript">
			var isExtForm=${isExtForm};
			var isFormEmpty=${isFormEmpty};
			var isNeedSubmitConfirm=${bpmDefinition.submitConfirm==1};
			var bpmGangedSets=[];
			var hasLoadComplete=false;
			var actDefId="${bpmDefinition.actDefId}";
			var form;
			$(function(){
				//设置表单。
				initForm();
				//启动流程事件绑定。
				$("a.run").click(function(){
					var flowNodes = $("input[name='flowNode']");
					if(flowNodes && flowNodes.length>1){
						var flowNode = $("input[type='radio']:checked");
						if(flowNode && flowNode.length==1){
							startWorkFlow();
						}
						else{
							$.ligerDialog.warn("请选择一个跳转节点!", $lang.tip.warn);
							return;
						}
					}else{
						startWorkFlow();
					}
				});
				//保存表单
				$("a.save").click(function(){
					saveForm(this);
				});	
				//重置表单
				$("a.reset").click(function(){
					var fieldName=$(this).attr("name");
					if(fieldName!=undefined&&fieldName!=null&&fieldName!=""){
						return;
					}
					$("#frmWorkFlow").resetForm();
					var parentObj = $(this).parent();
					$("input",parentObj).each(function(){
						$(this).val('');
					})
				});
				
				$("#flowNodeList").delegate("input", "click", function() {
					$("#startNode").val($(this).val());
				});
				
				//选择第一步任务的执行人
				chooseJumpType();
				//初始化联动设置				
				<c:if test="${!empty bpmGangedSets}">
					bpmGangedSets = ${bpmGangedSets};
					FormUtil.InitGangedSet(bpmGangedSets);
				</c:if>
				//隐藏意见控件
				var opinion = $("textarea[opinionname]");
				if(opinion){
					opinion.parent().hide();
				}
			});
			
			//设置表单。
			function initForm(){
				if(isFormEmpty) return;
				//表单不为空的情况。
				if(isExtForm){
					form=$('#frmWorkFlow').form({excludes:"[type=append]"});
					var formUrl=$('#divExternalForm').attr("formUrl");
					$('#divExternalForm').load(formUrl, function() {
						hasLoadComplete=true;
						//动态执行第三方表单指定执行的js
						try{
							afterOnload();
						}catch(e){}

						initSubForm();
					});
				}
				else{
					initSubForm();
				}
			};
			
			function selExeUsers(){
				var span=$("#spanSelectUser");
				var objExecutor=$("#_executors_");
				// "user^10000000440017^用户1#user^10000000440018^用户2#user^10000000440013^用户3"
				var value = objExecutor.val();
			    var selectUsers = null;
			    if(value!=undefined && value!=null && value!=""){
			    	selectUsers = new Array();
			    	var objArry=value.split("#");
			    	for ( var i = 0; i < objArry.length; i++) {
						var obj = objArry[i].split("^");
						var selectUser = null;
						if(obj.length==3){
			    			selectUser={
				    				type:obj[0],
				    				id:obj[1],
									name:obj[2]
							}
			    		}else{
			    			selectUser={
				    				type:'user',
				    				id:obj[1],
									name:obj[2]
							}
			    		}
			    		selectUsers.push(selectUser);
						
					}
			    }
				FlowUserDialog({selectUsers:selectUsers,callback:function(aryTypes,aryIds,aryNames){
					if(aryIds==null) return;
					var aryTmp=[];
					var aryUserName=[];
					for(var i=0;i<aryIds.length;i++){
						var val=aryTypes[i] +"^" + aryIds[i] +"^" +aryNames[i];
						aryTmp.push(val);
						aryUserName.push(aryNames[i]);
					}
					span.html(aryUserName.join(','));
					objExecutor.val(aryTmp.join("#"));
				}});
			}
			
			//是否点击了开始按钮。
			var isStartFlow=true;
			
			function saveForm(obj){
				isStartFlow=false;
				var  action="";
				if($(obj).hasClass('isDraft')){
					action="${ctx}/platform/bpm/task/saveForm.ht";
				}else{
					action="${ctx}/platform/bpm/task/saveData.ht";
				}
				submitForm(action,"a.save");
			}
			
			function startWorkFlow(){
				isStartFlow=true;
				var  action="${ctx}/platform/bpm/task/startFlow.ht";
				if(isNeedSubmitConfirm){
					$.ligerDialog.confirm("确认启动流程吗?","提示",function(rtn){
						if(rtn){
							submitForm(action,"a.run");
						}
					});
				}
				else{
					submitForm(action,"a.run");	
				}
			}
			
			//表单数据提交。
			//action:表单提交到的URL
			//button：点击按钮的样式。
			function submitForm(action,button){
				var ignoreRequired=false;
				if(button=="a.save"){
					ignoreRequired=true;
				}
				var operatorType=(isStartFlow)?1:6;
				//前置事件处理
				var rtn=beforeClick(operatorType);
				if( rtn==false){
					return;
				}
				if($(button).hasClass("disabled"))return;
				if(isFormEmpty){
					$.ligerDialog.warn('流程表单为空，请先设置流程表单!',"提示信息");
					return;
				}
				$('#frmWorkFlow').attr("action",action);
						
	            if(isExtForm){
	            	//提交第三方表单时检查该表单的参数
					var rtn = true;
					if(button!="a.save"){
						rtn=form.valid()
					}
					if(rtn){
						var frm=$('#frmWorkFlow');
						if(frm.setData)frm.setData();
						$(button).addClass("disabled");
						$('#frmWorkFlow').submit();
					}
				}else{
					var rtn=CustomForm.validate({ignoreRequired:ignoreRequired});
					if(!rtn){
						$.ligerDialog.warn("表单验证不成功,请检查表单是否填写正确！","提示信息");
						return;
					}
					if(button!="a.save"){
						//子表必填检查(兼容新旧版本)					
						rtn = SubtablePermission.subRequired();
						if(!rtn){	
							$.ligerDialog.warn("子表必须有一条记录，请检查！","提示信息");
							return;
						}
					}
					//获取自定义表单的数据
					var data=CustomForm.getData();
					
					//WebSign控件提交。 有控件时才提交   xcx
					if(WebSignPlugin.hasWebSignField){
						WebSignPlugin.submit();
					}	
					
					$(button).addClass("disabled");
					
					var uaName=navigator.userAgent.toLowerCase();				
					if(uaName.indexOf("firefox")>=0||uaName.indexOf("chrome")>=0){  // 火狐和谷歌 的文档提交
						//Office控件提交。 有可以提交的文档
						if(OfficePlugin.submitNum>0){
							OfficePlugin.submit();       
							//火狐和谷歌 的文档提交包括了  业务提交代码部分（完成  OfficePlugin.submit()后面的回调 函数 有 业务提交代码），所以 后面就不用加上业务提交代码
						}else{   //没有可提交的文档时 直接做 业务提交代码
							data=CustomForm.getData();
							//设置表单数据
							$("#formData").val(data);					
							$('#frmWorkFlow').submit();
						}	
					}else{        //IE内核的等 
						//Office控件提交。 有可以提交的文档
						if(OfficePlugin.submitNum>0){
							OfficePlugin.submit();
						}
						//获取自定义表单的数据
						data=CustomForm.getData();
						//设置表单数据
						$("#formData").val(data);
						$('#frmWorkFlow').submit();
					}
				}
			}
		
			function showBpmImageDlg(){
				BpmImageDialog({actDefId:"${bpmDefinition.actDefId}"});
			}
			
			function initSubForm(){
				$('#frmWorkFlow').ajaxForm({success:showResponse }); 
			}
			
			function showResponse(responseText){
				var button=(isStartFlow)? "a.run":"a.save";
				var operatorType=(isStartFlow)?1:6;
				
				$(button).removeClass("disabled");
				var obj=new com.hotent.form.ResultMessage(responseText);
				if(obj.isSuccess()){
					var msg=(isStartFlow)?"启动流程成功!":"保存表单数据成功!";
					$.ligerDialog.success(msg,'提示信息',function(){
						//添加后置事件处理
						var rtn=afterClick(operatorType);
						if( rtn==false){
							return;
						}
						if(window.opener){
							window.opener.location.reload();
							window.close();
						}else{
							window.close();
						}
					});
					
				}
				else{
					var msg=(isStartFlow)?"启动流程失败!":"保存表单数据失败!";
					$.ligerDialog.err('提示信息',msg,obj.getMessage());
				}
			}
			
			function chooseJumpType(){
				var obj=$('#jumpDiv');
				var url="${ctx}/platform/bpm/task/selExecutors.ht";
				url=url.getNewUrl();
				obj.html(obj.attr("tipInfo")).show().load(url);
			}
			
			function openHelpDoc(fileId){
 				var h=screen.availHeight-35;
 				var w=screen.availWidth-5;
 				var vars="top=0,left=0,height="+h+",width="+w+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
 				var showUrl = __ctx+"/platform/form/office/get.ht?fileId=" + fileId;
 				window.open(showUrl,"myWindow",vars);	
			}
			
			//增加Web签章
			function addWebSigns(){
				AddSecSignFromServiceX();          //WebSignPlugin JS类     
			}
			
			//增加手写签章
			function addHangSigns(){
				AddSecHandSignNoPromptX();          //WebSignPlugin JS类     
			}

		</script>
		<style type="text/css" media="print">
			.noprint{display:none;} 
			.printForm{display:block !important;} 
			.noForm{font-size: 14px;font-weight: bold;text-align: center;}
			input, select { border:none!important;}
			.link { display:none!important;}
			select { position:absolute; clip: rect(0px,100px,21px,0px); width: 120px; margin-top: -10px;}
			.l-text { border:none;}
			.l-trigger { display:none;}
			td { text-align: left!important; min-width:120px;}
		</style>
	</head>
	<body>
		<form id="frmWorkFlow" method="post" >
			<input type="hidden" name="actDefId" value="${bpmDefinition.actDefId}"/>
			<input type="hidden" name="defId" value="${bpmDefinition.defId}"/>
			<input type="hidden" name="businessKey" value="${businessKey}"/>
			<input type="hidden" name="runId" value="${runId}" />
			<input type="hidden" id="startNode" name="startNode" value="" />
			<div class="panel">
					<%@include file="incToolBarStart.jsp" %>									
					<div style="padding:6px 8px 3px 12px;" class="noprint">
						<b>流程简述：</b>${bpmDefinition.descp}
					</div>
					
					<div class="panel-body printForm" style="overflow: auto;">
							<c:choose>
								<c:when test="${isMultipleFirstNode}">
									<div id="flowNodeList">
										<table class="table-grid">
											<thead>
											<tr>
												<th height="28" width="20%">选择起始路径</th>
												<td>
													<c:forEach items="${flowNodeList}" var="flowNode">
									    				<lable>${flowNode.nodeName}<input type="radio" name="flowNode" value="${flowNode.nodeId}" /></lable>
									    			</c:forEach>
												</td>
											</tr>
											</thead>
										</table>
						    		</div>
					    		</c:when>
				    		</c:choose>
							<c:choose>
								<c:when test="${bpmDefinition.showFirstAssignee==1}">
									<div id="jumpDiv" class="noprint" style="display:none;" tipInfo="正在加载表单请稍候..."></div>
								</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${isFormEmpty==true}">
									<div class="noForm">没有设置流程表单。</div>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${isExtForm}">
											<div id="divExternalForm" formUrl="${form}"></div>
										</c:when>
										<c:otherwise>
											<div type="custform">${form}</div>
											<input type="hidden" name="formKey" value="${formKey}"/>
											<input type="hidden" name="formData" id="formData" />
											<c:if test="${not empty  paraMap}">
												<c:forEach items="${paraMap}" var="item">
												<input type="hidden" name="${item.key}" value="${item.value}" />
          										</c:forEach>
											</c:if>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						
							
					</div>
			</div>
		</form>
	</body>
	
</html>