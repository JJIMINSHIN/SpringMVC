<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
 <meta charset="UTF-8">
</head>
<body>
성공
<ul>

<%--
    getAttribute 버전
    <li> id =<%=((Member)requset.getAttribute("member")).getId()%></li>
    <li> username =<%=((Member)requset.getAttribute("username")).getUsername()%></li>
    <li> age =<%=((Member)requset.getAttribute("age")).getAge()%></li>
--%>
    <%-- property 접근법 --%>
     <li>id=${member.id}</li>
     <li>username=${member.username}</li>
     <li>age=${member.age}</li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>