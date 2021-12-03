<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>
<%@ include file="templates/roles.jsp" %>

<html>
    <head>
        <title>Product details</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>
        <style>
            .custom-details-column {
                margin-left: 40px;
            }

            .custom-button-width {
                width: 25%;
                margin-bottom: 10px;
            }

            .custom-price {
                font-size: x-large;
            }

            .custom-text-out-of-stock {
                color: #972828;
                font-size: x-large;
            }
        </style>
    </head>

    <body class="d-flex flex-column min-vh-100">

        <jsp:include page="templates/navbar.jsp"/>

        <div class="d-flex justify-content-center">
            <div class="w-25 p-3">
                <div class="d-flex flex-column">
                    <!-- START Custom messages -->
                    <c:if test="${param.attributeSuccessAddProduct != null}">
                        <div class="p-2">
                            <div class="alert alert-success" role="alert">
                                Product added
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${param.attributeSuccessUpdateProduct != null}">
                        <div class="p-2">
                            <div class="alert alert-success" role="alert">
                                Product updated
                            </div>
                        </div>
                    </c:if>
                    <!-- END Custom messages -->

                    <div class="p-2">
                        <h4 class="text-center">Product details</h4>
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
            <div class="row">
                <div class="col">
                    <br/>
                    <img src="data:image/png;base64, ${productDto.imageBase64}" class="img-fluid" alt="Product image"/>
                    <br/>
                </div>

                <div class="col custom-details-column">

                        <span class="font-weight-bold">Stock:</span>

                            <c:choose>
                                <c:when test="${productDto.stock > 0}">
                                    Yes
                                    <sec:authorize access="hasAnyAuthority('${roleEmployee}', '${roleAdmin}')">
                                        (${productDto.stock})
                                    </sec:authorize>
                                </c:when>
                                <c:otherwise>
                                    <span class="custom-text-out-of-stock">&nbsp;&nbsp;Out of stock</span>
                                </c:otherwise>
                            </c:choose>

                        <br/>
                        <br/>
                        <span class="font-weight-bold">Name:</span> ${productDto.name}
                        <br/>
                        <br/>
                        <span class="font-weight-bold">Description:</span> ${productDto.description}
                        <br/>
                        <br/>
                        <span class="font-weight-bold">Categories:</span>
                        <ul>
                            <c:forEach var="category" items="${sessionScope.globalCategoriesList}">
                                <c:forEach var="id" items="${productDto.categoriesIds}">
                                    <c:if test="${id == category.id}">
                                        <li>${category.name}</li>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                        </ul>

                        <div class="custom-price">
                            <span class="font-weight-bold">Price:</span> $ ${productDto.price}
                        </div>

                        <br/>
                        <br/>

                        <sec:authorize access="!hasAnyAuthority('${roleEmployee}', '${roleAdmin}')">

                            <c:choose>
                                <c:when test="${productDto.addedToCart}">
                                    <button class="btn btn-outline-dark" disabled>Is added to cart</button>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${productDto.stock > 0}">
                                            <button class="btn btn-outline-dark" onclick="addToCart(${productId})" id="idAddToCartEnabled">Add to cart</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button disabled class="btn btn-outline-dark" onclick="addToCart(${productId})" id="idAddToCartEnabled">Add to cart</button>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                            <br>
                        </sec:authorize>

                        <sec:authorize access="hasAnyAuthority('${roleEmployee}', '${roleAdmin}')">

                            <c:url var="getUpdateProductPage" value="${dynamic_productControllerGetUpdateProductLink}/${productId}"/>
                            <form:form action="${getUpdateProductPage}" method="get">
                                <button type="submit" class="btn btn-outline-dark custom-button-width ">Update</button>
                            </form:form>

                            <c:url var="processDeleteProduct" value="${dynamic_productControllerProcessDeleteProductLink}/${productId}"/>
                            <form:form action="${processDeleteProduct}" method="post">
                                <button type="submit" class="btn btn-outline-danger custom-button-width">Delete</button>
                            </form:form>

                        </sec:authorize>


                </div>

            </div>
        </div>

        <jsp:include page="templates/footer.jsp"/>

        <script>
            function addToCart(productId){
                $.ajax(
                    {
                        data: {
                            "_csrf": "${_csrf.token}"
                        },
                        url: "${pageContext.request.contextPath}${dynamic_productControllerProcessAddToCartLink}/" + productId,
                        method: "POST",
                        success: function (response) {
                            document.getElementById("idCartBadge").textContent = response;

                            let btnAddToCart = document.getElementById("idAddToCartEnabled");
                            if(btnAddToCart){
                                btnAddToCart.disabled = true;
                                btnAddToCart.textContent = 'Is added to cart';
                            }
                        },
                        error: function (err) {
                            console.error(err);
                        }
                    }
                );
            }
        </script>

    </body>
</html>
