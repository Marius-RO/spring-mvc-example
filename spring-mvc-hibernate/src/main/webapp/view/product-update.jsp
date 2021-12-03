<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fomr" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>
<%@ include file="templates/roles.jsp" %>

<html>
    <head>
        <title>Update product</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>
    </head>

    <body class="d-flex flex-column min-vh-100">
        <jsp:include page="templates/navbar.jsp"/>

        <div class="d-flex justify-content-center">
            <div class="w-25 p-3">

                <div class="d-flex flex-column">

                    <!-- START Custom messages -->
                    <c:if test="${attributeErrorUpdateProduct}">
                        <div class="p-2">
                            <div class="alert alert-danger" role="alert">
                                Product was not updated
                            </div>
                        </div>
                    </c:if>
                    <!-- END Custom messages -->

                    <div class="p-2">
                        <h4 class="text-center">Update product</h4>
                    </div>


                    <!-- START update product form -->
                    <div class="p-2">

                        <!--
                             For multipart enctype seems that CSRF token must be added to the request link because
                             CSRF token created automatically by 'form:form' will not be send. Actually data is send using
                             other format (because of the enctype = multipart) so CSRF token will not be visible).
                             If CSRF token is not added then request will be rejected by Spring Security.

                            https://stackoverflow.com/questions/25612984/spring-mvc-upload-file-is-blocked-by-spring-security/25821369#25821369

                         -->
                        <c:url var="processUpdateProduct" value="${dynamic_productControllerProcessUpdateProductLink}/${productId}?${_csrf.parameterName}=${_csrf.token}"/>
                        <form:form action="${processUpdateProduct}" method="post" modelAttribute="productDto" enctype="multipart/form-data">

                            <div class="form-group">
                                <label for="idStock">Stock</label>
                                <form:input type="number" step="1" path="stock" class="form-control" placeholder="Stock" id="idStock"/>
                                <form:errors path="stock" cssClass="text-danger"/>
                            </div>

                            <div class="form-group">
                                <label for="idPrice">Price</label>
                                <form:input type="number" step="0.01" path="price" class="form-control" placeholder="Price" id="idPrice"/>
                                <form:errors path="price" cssClass="text-danger"/>
                            </div>

                            <div class="form-group">
                                <form:input path="name" class="form-control" placeholder="Product name"/>
                                <form:errors path="name" cssClass="text-danger"/>
                            </div>

                            <div class="form-group">
                                <form:textarea path="description" class="form-control" placeholder="Description"/>
                                <form:errors path="description" cssClass="text-danger"/>
                            </div>

                            <div class="form-group">
                                <div class="dropdown">
                                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        Select categories
                                    </button>
                                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                        <c:forEach var="category" items="${sessionScope.globalCategoriesList}">
                                            <div class="dropdown-item form-group">
                                                &nbsp; <form:checkbox path="categoriesIds" class="form-check-input" id="idCategory${category.id}" value="${category.id}"/>
                                                <label class="form-check-label" for="idCat${category.id}"> &nbsp; ${category.name}</label>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <form:errors path="categoriesIds" cssClass="text-danger"/>
                            </div>

                            <br>
                            <div class="form-group">
                                <img src="data:image/png;base64, ${productDto.imageBase64}" class="img-fluid" alt="Product image"/>
                                <form:input path="imageBase64" class="form-control" hidden="true"/>
                                <br>
                                <br>
                                <label for="idChooseImage">Change image</label>
                                <form:input type="file" path="imageMultipartFile" class="form-control" id="idChooseImage"/>
                            </div>

                            <div class="text-center">
                                <button type="submit" class="h-25 w-100 btn btn-primary">Update product</button>
                            </div>

                        </form:form>

                    </div>

                    <!-- END update product form -->

                </div>
            </div>
        </div>

        <jsp:include page="templates/footer.jsp"/>

    </body>
</html>
