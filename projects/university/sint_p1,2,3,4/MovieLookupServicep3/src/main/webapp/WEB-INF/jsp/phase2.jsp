<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h1>Select an Actor/Actress</h1>
<ul>
    <c:forEach var="cast" items="${cast}">
        <li><a href="?pphase=3&plang=${lang}&pidC=${cast.id}">${cast.name} (${cast.role})</a></li>
    </c:forEach>
</ul>
<a href="?pphase=1">Back</a>
</body>
</html>