<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>

<html>
    <head>
        <title>Product deleted</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>
    </head>
    <body class="d-flex flex-column min-vh-100">
         <jsp:include page="templates/navbar.jsp"/>

         <div class="d-flex justify-content-center">
             <div class="p-3">
                 <div class="d-flex flex-column">
                     <!-- START Custom messages -->
                     <c:if test="${param.attributeErrorDeleteProduct != null}">
                         <div class="p-2">
                             <div class="alert alert-danger text-center" role="alert">
                                 <h2>Product was not deleted</h2>
                             </div>
                         </div>
                     </c:if>

                     <c:if test="${param.attributeSuccessDeleteProduct != null}">
                         <div class="p-2">
                             <div class="alert alert-success text-center" role="alert">
                                 <h3>Product was deleted. <a href="${productControllerAddProductLink}">Add a new product</a></h3>
                             </div>
                         </div>
                     </c:if>
                     <!-- END Custom messages -->

                 </div>
             </div>
         </div>

         <jsp:include page="templates/footer.jsp"/>
    </body>
</html>
