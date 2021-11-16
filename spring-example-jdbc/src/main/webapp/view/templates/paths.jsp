<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- -------------------------------------------------
                   AccountController
     ------------------------------------------------- --%>
<c:url var="accountControllerRegisterLink" value="/account/register"/>
<c:url var="accountControllerProcessRegisterLink" value="/account/process-register"/>


<%-- -------------------------------------------------
                   CategoryController
     ------------------------------------------------- --%>
<c:url var="categoryControllerGetCategoriesLink" value="/categories/all"/>
<c:url var="categoryControllerAddCategoryLink" value="/categories/add"/>
<c:url var="categoryControllerProcessAddCategoryLink" value="/categories/process-add"/>

<%-- dinamyc links --%>
<c:set var="dynamic_categoryControllerUpdateCategoryLink" value="/categories/update" scope="application"/>
<c:set var="dynamic_categoryControllerProcessUpdateCategoryLink" value="/categories/process-update" scope="application"/>
<c:set var="dynamic_categoryControllerProcessDeleteCategoryLink" value="/categories/process-delete" scope="application"/>