<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Management - List</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
</head>
<body>
<div class="container">
    <h1>User Management System</h1>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>

    <div class="header-actions">
        <a href="<c:url value='/users/new'/>" class="btn btn-primary">Create New User</a>
        <span class="user-count">Total Users: ${totalCount}</span>
    </div>

    <div class="filter-section">
        <form method="get" action="<c:url value='/users'/>">
            <label for="status">Filter by Status:</label>
            <select name="status" id="status" onchange="this.form.submit()">
                <option value="">All</option>
                <c:forEach items="${statuses}" var="stat">
                    <option value="${stat}" ${filterStatus == stat ? 'selected' : ''}>${stat}</option>
                </c:forEach>
            </select>
        </form>
    </div>

    <table class="user-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Full Name</th>
            <th>Status</th>
            <th>Role</th>
            <th>Created At</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty users}">
                <tr>
                    <td colspan="8" class="text-center">No users found</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.firstName} ${user.lastName}</td>
                        <td>
                            <span class="status-badge status-${user.status}">${user.status}</span>
                        </td>
                        <td>${user.role}</td>
                        <td>
                            <fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                        </td>
                        <td class="actions">
                            <a href="<c:url value='/users/${user.id}'/>" class="btn btn-sm btn-info">View</a>
                            <a href="<c:url value='/users/${user.id}/edit'/>" class="btn btn-sm btn-warning">Edit</a>
                            <form method="post" action="<c:url value='/users/${user.id}/delete'/>"
                                  style="display:inline;"
                                  onsubmit="return confirm('Are you sure you want to delete this user?');">
                                <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>

    <div class="api-info">
        <h3>REST API Endpoints</h3>
        <ul>
            <li>GET /api/users - Get all users</li>
            <li>GET /api/users/{id} - Get user by ID</li>
            <li>POST /api/users - Create new user</li>
            <li>PUT /api/users/{id} - Update user</li>
            <li>DELETE /api/users/{id} - Delete user</li>
        </ul>
    </div>
</div>
</body>
</html>