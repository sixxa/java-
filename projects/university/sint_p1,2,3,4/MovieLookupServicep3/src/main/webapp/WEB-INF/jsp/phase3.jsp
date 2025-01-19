<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h1>Movies Featuring the Selected Actor/Actress</h1>
<ul>
    <c:forEach var="movie" items="${movies}">
        <li>${movie.title} (${movie.year})</li>
    </c:forEach>
</ul>
<a href="?pphase=2&plang=${lang}">Back</a>
</body>
</html>