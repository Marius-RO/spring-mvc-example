<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.company.config.WebAppSecurityConfig" %>

<%-- Roles --%>
<%
    final String roleCustomer = WebAppSecurityConfig.Roles.ROLE_CUSTOMER;
    final String roleEmployee =  WebAppSecurityConfig.Roles.ROLE_EMPLOYEE;
    final String roleAdmin =  WebAppSecurityConfig.Roles.ROLE_ADMIN;
%>