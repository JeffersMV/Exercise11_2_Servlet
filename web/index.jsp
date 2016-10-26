<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>

<form action="ShowServlet" method="GET">
    <input type="hidden" name="parameter" value="student">
    <input type="submit" value="Отобразить список студентов"/>
</form>
<form action="ShowServlet" method="GET">
    <input type="hidden" name="parameter" value="object">
    <input type="submit" value="Отобразить список предметов"/>
</form>

</body>
</html>
