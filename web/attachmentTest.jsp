<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>接口测试</title>
    <script src="js/jquery/jquery.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
<%--AJAX提交--%>
<input type="file" id="file" onchange="ajaxFileUpload();" />
<%--表单提交--%>
<form
        action="${pageContext.request.contextPath}/front/attachment/upload.ht"
        method="POST"
        enctype="multipart/form-data">
    <table border="1" align="center">
        <tr>
            <th>选择上传文件</th>
            <td>
                <input type="hidden" name="uploaderId" value="020">
                <input type="hidden" name="uploaderName" value="jason">
                <input type="hidden" name="fileType" value="澄清">
                <input type="hidden" name="businessId" value="1">
                <input type="hidden" name="businessType" value="项目公告">
                <input type="hidden" name="remark" value="helloWorld">
            </td>
        </tr>
        <tr>
            <td><input type="file" name="files"/></td>
        </tr>
        <tr>
            <td><input type="file" name="files"/></td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="submit" value="上传" style="width:111px"/>
            </td>
        </tr>
    </table>
</form>






</body>
<script>
    function ajaxFileUpload() {
        var form = new FormData;                                           //创建一个FormData对象
        form.append('file',$("#file").get(0).files[0]);            //将文件放到FormData对象中
        console.log(form);
        $.ajax({
            url : "${pageContext.request.contextPath}/front/attachment/upload.ht",//访问路径
            contentType: false,
            processData: false,
            type:"post",
            data :form,
            dataType : "json"

        }).success(function(data,status){
            if(status=="success"){
                if(data.code==200){
                    alert("文件上传成功");
                }
                if(data.code==400){
                    alert("文件上传失败");
                }
            }

        }).error(function(){
            alert("服务器未响应");
        });

    }

</script>
</html>
