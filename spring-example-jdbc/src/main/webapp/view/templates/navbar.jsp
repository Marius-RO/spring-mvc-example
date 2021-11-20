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

                <ul class="navbar-nav">

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
                                <a class="dropdown-item" href="<c:url value="${dynamic_productControllerFilterProductsLink}?categoryId=${category.id}" />">${category.name}</a>
                            </c:forEach>
                        </div>
                    </li>

                </ul>

                <div class="container-fluid">
                    <c:url var="searhProduct" value="${dynamic_productControllerFilterProductsLink}"/>
                    <form:form action="${searhProduct}" method="get" class="row custom-search-div">
                        <div class="col-auto w-50">
                            <c:if test="${param.categoryId != null}">
                                <input hidden type="text" name="categoryId" value="${param.categoryId}">
                            </c:if>
                            <input class="form-control" type="search" name="search">
                        </div>
                        <div class="col">
                            <button class="btn btn-outline-light" type="submit">Search</button>
                        </div>
                    </form:form>
                </div>

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
                                <a class="dropdown-item" href="${orderControllerGetOrdersLink}">Orders</a>

                                <sec:authorize access="hasAnyAuthority('${roleAdmin}', '${roleEmployee}')">
                                    <a class="dropdown-item" href="${productControllerAddProductLink}">Add product</a>
                                </sec:authorize>


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

                    <sec:authorize access="!hasAnyAuthority('${roleEmployee}', '${roleAdmin}')">
                        <li class="nav-item px-3">
                            <a class="btn icon-button" href="${productControllerGetCartPageLink}" role="button">
                                <span class="material-icons" id = "idIconCart">shopping_cart</span>
                                <span class="icon-button__badge" id="idCartBadge">${sessionScope.sessionCart == null ? 0 : sessionScope.sessionCart.cartSize()}</span>
                            </a>
                        </li>
                    </sec:authorize>

                </ul>
            </div>

        </div>
    </nav>
</header>
