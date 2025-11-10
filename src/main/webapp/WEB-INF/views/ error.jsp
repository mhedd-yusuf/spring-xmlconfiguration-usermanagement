<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error - User Management</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
</head>
<body>
<div class="container">
    <h1>Error ${not empty errorCode ? errorCode : '500'}</h1>

    <div class="alert alert-error">
        <h3>Oops! Something went wrong</h3>
        <p>${not empty errorMessage ? errorMessage : 'An unexpected error occurred'}</p>
    </div>

    <div class="form-actions">
        <a href="<c:url value='/users'/>" class="btn btn-primary">Go to Users List</a>
    </div>
</div>
</body>
</html>