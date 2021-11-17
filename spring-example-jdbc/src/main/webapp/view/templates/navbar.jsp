<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="paths.jsp" %>
<%@ include file="roles.jsp" %>

<header class="custom-header">

    <nav class="navbar navbar-expand-sm navbar-dark bg-dark">
        <div class="container-fluid ">

            <a class="navbar-brand " href="${rootControllerHomePageLink}">Your brand here</a>

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse " id="navbarSupportedContent">

                <ul class="navbar-nav me-auto">

                    <li class="nav-item px-2">
                        <a class="nav-link inactive" aria-current="page" href="${rootControllerHomePageLink}">Home</a>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownCategories" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Categories
                        </a>

                        <!-- categories -->
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <c:forEach var="category" items="${sessionScope.globalCategoriesList}">
                                <a class="dropdown-item" href="#">${category.name}</a>
                            </c:forEach>
                        </div>
                    </li>

                    <li class="nav-item custom-search">
                        <form class="form-inline my-2 my-lg-0">
                            <input class="form-control" type="search" placeholder="Search" aria-label="Search">
                        </form>
                    </li>

                </ul>

                <ul class="navbar-nav ms-auto">

                    <sec:authorize access="!isAuthenticated()">
                        <li class="nav-item px-2">
                            <a class="nav-link active" aria-current="page" href="${accountControllerLoginLink}">Login</a>
                        </li>
                    </sec:authorize>

                    <sec:authorize access="isAuthenticated()">

                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownAccount" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Hello <sec:authentication property="name"/>
                            </a>

                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="${accountControllerProfileLink}">Profile</a>
                                <a class="dropdown-item" href="#">Orders</a>

                                <sec:authorize access="hasAuthority('${roleAdmin}')">
                                    <a class="dropdown-item" href="${employeeControllerGetEmployeesLink}">Employees</a>
                                    <a class="dropdown-item" href="${categoryControllerGetCategoriesLink}">Categories</a>
                                </sec:authorize>

                                <c:url var="logoutLink" value="/logout"/>
                                <form:form action="${logoutLink}" method="post" cssClass="custom-logout-form">
                                    <input class="dropdown-item" type="submit" value="Logout">
                                </form:form>
                            </div>
                        </li>
                    </sec:authorize>

                    <sec:authorize access="isAuthenticated() && hasAuthority('${roleCustomer}')">
                        <li class="nav-item px-3">
                            <button type="button" class="icon-button">
                                <span class="material-icons">shopping_cart</span>
                                <span class="icon-button__badge">25</span>
                            </button>
                        </li>
                    </sec:authorize>

                </ul>
            </div>

        </div>
    </nav>
</header>
