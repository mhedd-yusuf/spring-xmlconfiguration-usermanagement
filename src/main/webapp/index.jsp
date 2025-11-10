<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Management System</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
</head>
<body>
<div class="container">
    <h1>Welcome to User Management System</h1>

    <div class="welcome-content">
        <p>A comprehensive Spring MVC application with Hibernate ORM for managing users.</p>

        <h2>Features:</h2>
        <ul>
            <li>Create, Read, Update, and Delete users</li>
            <li>RESTful API endpoints</li>
            <li>JSP-based web interface</li>
            <li>Hibernate with automatic table generation</li>
            <li>Input validation and error handling</li>
            <li>Transaction management</li>
        </ul>

        <div class="form-actions">
            <a href="<c:url value='/users'/>" class="btn btn-primary btn-lg">Manage Users</a>
        </div>

        <div class="api-info">
            <h3>REST API Endpoints</h3>
            <table class="api-table">
                <tr>
                    <th>Method</th>
                    <th>Endpoint</th>
                    <th>Description</th>
                </tr>
                <tr>
                    <td>GET</td>
                    <td>/api/users</td>
                    <td>Get all users</td>
                </tr>
                <tr>
                    <td>GET</td>
                    <td>/api/users/{id}</td>
                    <td>Get user by ID</td>
                </tr>
                <tr>
                    <td>POST</td>
                    <td>/api/users</td>
                    <td>Create new user</td>
                </tr>
                <tr>
                    <td>PUT</td>
                    <td>/api/users/{id}</td>
                    <td>Update user</td>
                </tr>
                <tr>
                    <td>DELETE</td>
                    <td>/api/users/{id}</td>
                    <td>Delete user</td>
                </tr>
                <tr>
                    <td>GET</td>
                    <td>/api/users/check-username?username=x</td>
                    <td>Check username availability</td>
                </tr>
                <tr>
                    <td>GET</td>
                    <td>/api/users/check-email?email=x</td>
                    <td>Check email availability</td>
                </tr>
                <tr>
                    <td>GET</td>
                    <td>/api/users/count</td>
                    <td>Get total user count</td>
                </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>