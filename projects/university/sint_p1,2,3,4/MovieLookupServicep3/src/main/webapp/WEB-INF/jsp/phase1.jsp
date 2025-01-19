<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h1>Select a Language</h1>
<ul>
    <c:forEach var="lang" items="${languages}">
        <li><a href="?pphase=2&plang=${lang}">${lang}</a></li>
    </c:forEach>
</ul>
</body>
</html>