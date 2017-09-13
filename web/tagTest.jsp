<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%@taglib uri="http://www.jee-soft.cn/listTag" prefix="al" %>
<html>
<head>
    <title>标签测试</title>
    <script src="js/jquery/jquery.js" type="text/javascript" charset="utf-8"></script>
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
    <style type="text/css">


    </style>

</head>
<body>
    <div style="margin-top: 30px;">
            <al:attachment
                      canDel="true">
                <%--<td><a href="${pageContext.request.contextPath}/front/attachment/download.ht?fileid=${attachment.fileid}">${attachment.oriFilename}</a></td>--%>
        </al:attachment>
    </div>
</body>
</html>
