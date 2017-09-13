<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>接口测试</title>
    <script src="js/jquery/jquery.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
      function download() {
          var form = $("<form>");
          form.attr("style","display:none");
          form.attr("target","");
          form.attr("method","post");
          form.attr("action",$("#interfaceID option:selected").val());
          var input1 = $("<input>");
          input1.attr("type","hidden");
          input1.attr("name","fileid");
          input1.val($("#fileid").val());
//          input1.attr("value",strZipPath);
          $("body").append(form);
          form.append(input1);
          form.submit();
          form.remove();
      }


    </script>
</head>
<body>
<form id="formId" method="post">
    <div align="center">
        接口：
        <select id="interfaceID">
            <option value="${pageContext.request.contextPath}/front/attachment/downloadTest.ht">url="/front/attachment/downloadTest.ht"</option>
        </select>
        <span>fileid</span>:<input id="fileid" name="instr" value="f68bc4dd-3591-4f12-b729-4c42330c4312"/>
    </div>
</form>
<a href="javascript:download()">点击下载</a>
<a href="${pageContext.request.contextPath}/front/attachment/downloadTest.ht?fileid=f68bc4dd-3591-4f12-b729-4c42330c4312">点击下载a</a>
</body>
</html>
