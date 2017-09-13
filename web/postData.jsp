<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>接口测试</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery.js"></script>
    <script type="text/javascript">
      $(function() {
        $("#butId").click(function() {
          if ($("#reqContent").val() == null || $("#reqContent").val() == "") {
            alert("JSON参数不能为空!!!");
            return;
          }

          $("#tipMsg").show();

          $.ajax({
            url: $("#interfaceID option:selected").val(),
            type:'post',
            data:$("#reqContent").val(),
            contentType:"application/json",
            dataType:"json",
            cache:false,
            error:function() {
              alert("请求接口失败...");
            },
            success:function(data) {
                $("#resContent").val(JSON.stringify(data, null, 4));
                $("#tipMsg").hide();
            }
          });
        });
      });
    </script>
</head>
<body>
  <form id="formId" method="post">
      <div align="center">
          接口：
          <select id="interfaceID">
            <option value="${pageContext.request.contextPath}/front/attachment/list.ht">获取公告附件列表</option>
            <option value="${pageContext.request.contextPath}/front/attachment/delete.ht">删除公告单个附件</option>
            <option value="${pageContext.request.contextPath}/front/attachment/download.ht">下载公告单个附件</option>
          </select>
      </div>
      <div align="center" style="margin-top: 30px;">
          JSON参数：<textarea id="reqContent" rows="12" cols="150"></textarea>
      </div>
      <div id="tipMsg" align="center" style="margin-top: 30px; display: none;">
          数据请求中，请稍候。。。
      </div>
      <div align="center" style="margin-top: 30px;">
          <input id="butId" type="button" value="提交"/>
      </div>
      <div align="center" style="margin-top: 30px;">
          请求结果：<textarea id="resContent" rows="20" cols="150"></textarea>
      </div>
  </form>

  <form action="${pageContext.request.contextPath}/front/attachment/uploadTest.ht" method="post" enctype="multipart/form-data">
      <input type="hidden" name="businessId" value="1"/>
      <input type="hidden" name="businessType" value="项目公告"/>
      <input type="hidden" name="fileType" value="澄清"/>
      <div align="center">
          文件：<input type="file" name="uploadFile" />
          提交<input type="submit" value="上传"/>
      </div>
  </form>
</body>
</html>
