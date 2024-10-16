<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>My JSP Page</title>
</head>
<body>
<h1>Hello, JSP World!</h1>

<%
    Integer sum = (Integer) request.getSession().getAttribute("sum");
    if (sum != null) {
%>
<p>The sum is: <%= sum %></p>
<%
} else {
%>
<p>No result available.</p>
<%
    }
%>

</body>
</html>