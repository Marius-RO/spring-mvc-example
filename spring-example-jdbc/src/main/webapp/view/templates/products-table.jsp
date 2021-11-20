<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="paths.jsp" %>
<%@ include file="roles.jsp" %>

<!-- START products table -->
<div class="container-fluid w-100 custom-table-div-spacing">
    <table class="table-borderless" id="productsTable">

        <thead>
            <tr>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
        </thead>

        <tbody>

            <c:set var="groupSize" value="4"/>
            <c:forEach var="pairGroupList" items="${groupedProductList}" varStatus="mainStatus">

                <tr>

                    <c:forEach var="pair" items="${pairGroupList}" varStatus="status">
                        <c:choose>

                            <%-- if is a valid product add the product details --%>
                            <c:when test="${pair.first}">

                                <c:set var="product" value="${pair.second}"/>

                                <td>
                                    <%-- last row item will get an extra class 'special-card' for extra space --%>
                                    <div class="card h-100 <c:if test="${(status.index + 1) == groupSize}">special-card</c:if>">
                                        <%-- Product image--%>
                                        <img class="card-img-top" src="data:image/png;base64, ${product.imageBase64}" alt="image" />

                                        <%-- Product details--%>
                                        <div class="card-body p-4">
                                            <div class="text-center">
                                                <%-- Product name--%>
                                                <h5 class="fw-bolder">${product.name}</h5>
                                                <%-- Product reviews--%>
                                                <div class="d-flex justify-content-center small text-warning mb-2">
                                                    <div class="bi-star-fill"></div>
                                                    <div class="bi-star-fill"></div>
                                                    <div class="bi-star-fill"></div>
                                                    <div class="bi-star-fill"></div>
                                                    <div class="bi-star-fill"></div>
                                                </div>

                                                <%-- Product price--%>
                                                $ ${product.price}
                                            </div>
                                        </div>

                                        <%-- Product actions--%>
                                        <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">

                                            <sec:authorize access="!hasAnyAuthority('${roleEmployee}', '${roleAdmin}')">

                                                <c:choose>
                                                    <c:when test="${product.addedToCart}">
                                                        <div class="text-center"><button disabled class="btn btn-outline-dark mt-auto" id="idAddToCartEnabled${product.id}">Is added to cart</button></div>
                                                    </c:when>
                                                    <c:when test="${product.stock > 0}">
                                                        <div class="text-center"><button class="btn btn-outline-dark mt-auto" onclick="addToCart(${product.id})" id="idAddToCartEnabled${product.id}">Add to cart</button></div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="text-center"><button disabled class="btn btn-outline-dark mt-auto" onclick="addToCart(${product.id})" id="idAddToCartEnabled${product.id}">Out of stock</button></div>
                                                    </c:otherwise>
                                                </c:choose>
                                                <br>
                                            </sec:authorize>

                                            <div class="text-center"><a class="btn btn-outline-dark mt-auto"
                                                                        href="<c:url value='${dynamic_productControllerSeeProductDetailsLink}/${product.id}'/>">View details</a></div>
                                        </div>
                                    </div>

                                </td>
                            </c:when>

                            <%-- otherwise add empty columns --%>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>

                </tr>
            </c:forEach>

        </tbody>
    </table>
</div>
<!-- END products table -->

<script>

    function markAsAdded(productId){
        let btnAddToCart = document.getElementById("idAddToCartEnabled" + productId);
        if(btnAddToCart){
            btnAddToCart.disabled = true;
            btnAddToCart.textContent = 'Is added to cart';
        }
    }

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
                    markAsAdded(productId);
                },
                error: function (err) {
                    console.error(err);
                }
            }
        );
    }
</script>