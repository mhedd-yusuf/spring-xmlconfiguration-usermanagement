<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Management - ${formAction == 'edit' ? 'Edit' : 'Create'} User</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
</head>
<body>
<div class="container">
    <h1>${formAction == 'edit' ? 'Edit User' : 'Create New User'}</h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <form:form method="post"
               action="${formAction == 'edit' ? '/users/'.concat(userDTO.id) : '/users'}"
               modelAttribute="userDTO"
               cssClass="user-form">

        <div class="form-group">
            <label for="username">Username *</label>
            <form:input path="username" id="username" cssClass="form-control" required="true"/>
            <form:errors path="username" cssClass="error-message"/>
        </div>

        <div class="form-group">
            <label for="email">Email *</label>
            <form:input path="email" type="email" id="email" cssClass="form-control" required="true"/>
            <form:errors path="email" cssClass="error-message"/>
        </div>

        <div class="form-group">
            <label for="password">Password ${formAction == 'edit' ? '(leave blank to keep current)' : '*'}</label>
            <form:password path="password" id="password" cssClass="form-control"
                           required="${formAction != 'edit'}"/>
            <form:errors path="password" cssClass="error-message"/>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="firstName">First Name *</label>
                <form:input path="firstName" id="firstName" cssClass="form-control" required="true"/>
                <form:errors path="firstName" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label for="lastName">Last Name *</label>
                <form:input path="lastName" id="lastName" cssClass="form-control" required="true"/>
                <form:errors path="lastName" cssClass="error-message"/>
            </div>
        </div>

        <div class="form-group">
            <label for="phoneNumber">Phone Number</label>
            <form:input path="phoneNumber" id="phoneNumber" cssClass="form-control"
                        placeholder="10-15 digits"/>
            <form:errors path="phoneNumber" cssClass="error-message"/>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="status">Status</label>
                <form:select path="status" id="status" cssClass="form-control">
                    <c:forEach items="${statuses}" var="stat">
                        <form:option value="${stat}">${stat}</form:option>
                    </c:forEach>
                </form:select>
            </div>

            <div class="form-group">
                <label for="role">Role</label>
                <form:select path="role" id="role" cssClass="form-control">
                    <c:forEach items="${roles}" var="r">
                        <form:option value="${r}">${r}</form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">
                    ${formAction == 'edit' ? 'Update User' : 'Create User'}
            </button>
            <a href="<c:url value='/users'/>" class="btn btn-secondary">Cancel</a>
        </div>
    </form:form>
</div>
</body>
</html>