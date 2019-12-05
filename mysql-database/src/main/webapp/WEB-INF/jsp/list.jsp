
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Data center</title>
    <script type="text/javascript" src="/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript">
        function getUid(){
            var code = $("#uid option:selected").attr("value");
            alert(code);

            if(code!=null && ""!=code){
                $.post(
                    "queryBase",
                    {id:code},
                    function(data){
                        for(var i=0;i<data.length;i++){
                            $("#rids").append("<option value='"+data[i].Database+"'>"+data[i].Database+"</option>");
                        }
                    },"json"
                )
            }else{
                $("#rids").hide();
            }


        }

        function getRid() {
            var dataTableName = $("#rids option:selected").attr("value");
            var code = $("#uid option:selected").attr("value");
            alert(dataTableName);
            alert(code);
            if(dataTableName!=null && ""!=dataTableName){
                $.post(
                    "queryDatabase",
                    {dataTableName:dataTableName,id:code},
                    function(data){
                        alert(data)
                        for(var i=0;i<data.length;i++){
                            $("#cids").append("<option value='"+data[i]+"'>"+data[i]+"</option>");
                        }
                    },"json"
                )
            }else{
                $("#cids").hide();
            }
        }
        function getCid() {
            var dataTableName = $("#cids option:selected").attr("value");
            var code = $("#uid option:selected").attr("value");
            alert(dataTableName);
            alert(code);
            if(dataTableName!=null && ""!=dataTableName){
                $.post(
                    "queryDatabase",
                    {dataTableName:dataTableName,id:code},
                    function(data){
                        alert(data)
                        for(var i=0;i<data.length;i++){
                            $("#cids").append("<input value='+data[i]+'>");

                        }
                    },"json"
                )
            }else{
                $("#ids").hide();
            }
        }


    </script>
</head>
<body>
    <h1 align="center">爱峰峰 爱生活</h1>

<table>

    <tr>
        <td>Data center</td>
    </tr>
    <tr>
        <td>
        <select name="uid" id="uid" onchange="getUid()">
            <option>请选择用户</option>
            <c:forEach items="${database}" var="d">
                <option value="${d.id}">${d.name}</option>
            </c:forEach>
        </select>
        </td>
    </tr>
    <tr>
        <td>
        <select name="rid" id="rids" onchange="getRid()">
            <option>请选择数据库</option>
        </select>
        </td>
    </tr>
    <tr>
        <td>
        <select name="cid" id="cids" onchange="getCid()">
            <option>请选择数据表</option>
        </select>
        </td>
    </tr>
    <tr>
        <td id="ids"></td>
    </tr>

</table>
</body>
</html>
