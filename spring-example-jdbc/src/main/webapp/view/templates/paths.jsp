<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- -------------------------------------------------
                   RootController
     ------------------------------------------------- --%>
<c:url var="rootControllerHomePageLink" value="/index"/>
<c:url var="rootControllerAboutUsPageLink" value="/about-us"/>

<%-- -------------------------------------------------
                   AccountController
     ------------------------------------------------- --%>
<c:url var="accountControllerLoginLink" value="/account/login"/>
<c:url var="accountControllerRegisterLink" value="/account/register"/>
<c:url var="accountControllerProcessRegisterLink" value="/account/process-register"/>
<c:url var="accountControllerProfileLink" value="/account/profile"/>
<c:url var="accountControllerProcessUpdateProfileLink" value="/account/process-update"/>
<c:url var="accountControllerProcessUpdatePasswordLink" value="/account/process-update-password"/>
<c:url var="accountControllerProcessDeleteAccountLink" value="/account/process-delete"/>


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


<%-- -------------------------------------------------
                   ProductController
     ------------------------------------------------- --%>
<c:url var="productControllerAddProductLink" value="/products/add"/>
<c:url var="productControllerProcessAddProductLink" value="/products/process-add"/>
<c:url var="productControllerGetCartPageLink" value="/products/cart"/>

<%-- dinamyc links --%>
<c:set var="dynamic_productControllerSeeProductDetailsLink" value="/products/details" scope="application"/>
<c:set var="dynamic_productControllerGetUpdateProductLink" value="/products/update" scope="application"/>
<c:set var="dynamic_productControllerProcessUpdateProductLink" value="/products/process-update" scope="application"/>
<c:set var="dynamic_productControllerProcessDeleteProductLink" value="/products/process-delete" scope="application"/>
<c:set var="dynamic_productControllerProcessDeleteFromCartLink" value="/products/delete-from-cart" scope="application"/>
<c:set var="dynamic_productControllerProcessAddToCartLink" value="/products/add-to-cart" scope="application"/>
<c:set var="dynamic_productControllerProcessUpdateCartQuantityLink" value="/products/update-cart-quantity" scope="application"/>
<c:set var="dynamic_productControllerFilterProductsLink" value="/products/filter" scope="application"/>


<%-- -------------------------------------------------
                   OrdersController
     ------------------------------------------------- --%>
<c:url var="orderControllerGetOrdersLink" value="/orders/all"/>
<c:url var="orderControllerProcessSendOrderLink" value="/orders/process-send"/>





<%-- -------------------------------------------------
                   EmployeeController
     ------------------------------------------------- --%>
<c:url var="employeeControllerGetEmployeesLink" value="/employees/all"/>

<%-- dinamyc links --%>
<c:set var="dynamic_employeeControllerSeeActivityLink" value="/employees/activity" scope="application"/>
<c:set var="dynamic_employeeControllerProcessDeleteEmployeeLink" value="/employees/process-delete" scope="application"/>