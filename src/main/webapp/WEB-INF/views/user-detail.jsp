<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Management - User Detail</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
</head>
<body>
<div class="container">
    <h1>User Details</h1>

    <div class="user-detail">
        <div class="detail-row">
            <span class="label">ID:</span>
            <span class="value">${user.id}</span>
        </div>

        <div class="detail-row">
            <span class="label">Username:</span>
            <span class="value">${user.username}</span>
        </div>

        <div class="detail-row">
            <span class="label">Email:</span>
            <span class="value">${user.email}</span>
        </div>

        <div class="detail-row">
            <span class="label">Full Name:</span>
            <span class="value">${user.firstName} ${user.lastName}</span>
        </div>

        <div class="detail-row">
            <span class="label">Phone Number:</span>
            <span class="value">${not empty user.phoneNumber ? user.phoneNumber : 'N/A'}</span>
        </div>

        <div class="detail-row">
            <span class="label">Status:</span>
            <span class="value">
                    <span class="status-badge status-${user.status}">${user.status}</span>
                </span>
        </div>

        <div class="detail-row">
            <span class="label">Role:</span>
            <span class="value">${user.role}</span>
        </div>

        <div class="detail-row">
            <span class="label">Created At:</span>
            <span class="value">
                    <fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </span>
        </div>

        <div class="detail-row">
            <span class="label">Updated At:</span>
            <span class="value">
                    <fmt:formatDate value="${user.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </span>
        </div>
    </div>

    <div class="form-actions">
        <a href="<c:url value='/users/${user.id}/edit'/>" class="btn btn-warning">Edit User</a>
        <a href="<c:url value='/users'/>" class="btn btn-secondary">Back to List</a>
        <form method="post" action="<c:url value='/users/${user.id}/delete'/>"
              style="display:inline;"
              onsubmit="return confirm('Are you sure you want to delete this user?');">
            <button type="submit" class="btn btn-danger">Delete User</button>
        </form>
    </div>
</div>
</body>
</html>