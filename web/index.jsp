<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>

<form action="ShowServlet" method="POST">
    <input type="hidden" name="action" value="student">
    <input type="submit" value="Отобразить список студентов"/>
</form>
<form action="ShowServlet" method="POST">
    <input type="hidden" name="action" value="object">
    <input type="submit" value="Отобразить список предметов"/>
</form>

</body>
</html>
