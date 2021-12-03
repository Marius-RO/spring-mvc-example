<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>
<%@ include file="templates/roles.jsp" %>

<html>
    <head>
        <title>Home</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>

        <style>
            /*// https://stackoverflow.com/questions/46249541/change-arrow-colors-in-bootstraps-carousel/53359424#53359424*/
            .carousel-control-next,
            .carousel-control-prev {
                filter: invert(100%);
            }

            .custom-header {
                margin-bottom: 0;
            }

        </style>

        <script>
            function markAsAdded(productId){
                let btnAddToCart = document.getElementById("idAddToCartEnabled" + productId);
                if(btnAddToCart){
                    btnAddToCart.disabled = true;
                    btnAddToCart.textContent = 'Is added to cart';
                }
            }
        </script>
    </head>

    <body class="d-flex flex-column min-vh-100">

        <jsp:include page="templates/navbar.jsp"/>

        <header class="bg-custom-header py-5">
            <div class="container px-4 px-lg-5 my-5">
                <div class="text-center text-dark">
                    <h1 class="display-4 fw-bolder">Your personal shop</h1>
                    <p class="lead fw-normal text-dark-50 mb-0">Find new items every day</p>
                </div>
            </div>
        </header>

        <section class="container-fluid w-100">

            <!-- START products carousel -->
            <div id="carouselControls" class="carousel slide" data-ride="carousel">

                <!-- START carousel slides -->
                <div class="carousel-inner">

                    <!-- START first slide -->
                    <div class="carousel-item active">

                        <div class="container px-4 px-5 mt-5">
                            <div class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">

                                 <c:forEach var="product" items="${firstSlideProductList}" >

                                    <div class="col mb-5">
                                        <div class="card h-100">

                                            <!-- Product image-->
                                            <img class="card-img-top" src="data:image/png;base64, ${product.imageBase64}" alt="image" />

                                            <!-- Product details-->
                                            <div class="card-body p-4">
                                                <div class="text-center">
                                                    <!-- Product name-->
                                                    <h5 class="fw-bolder">${product.name}</h5>

                                                    <!-- Product price-->
                                                    $ ${product.price}
                                                </div>
                                            </div>

                                            <!-- Product actions-->
                                            <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">

                                                <sec:authorize access="!hasAnyAuthority('${roleEmployee}', '${roleAdmin}')">
                                                    <c:choose>
                                                        <c:when test="${product.stock > 0}">
                                                            <div class="text-center"><button class="btn btn-outline-dark mt-auto" onclick="addToCart(${product.id})" id="idAddToCartEnabled${product.id}">Add to cart</button></div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="text-center"><button disabled class="btn btn-outline-dark mt-auto" onclick="addToCart(${product.id})" id="idAddToCartEnabled${product.id}">Out of stock</button></div>
                                                        </c:otherwise>
                                                    </c:choose>

                                                    <c:if test="${product.addedToCart}">
                                                        <script>
                                                            markAsAdded(${product.id});
                                                        </script>
                                                    </c:if>
                                                    <br>
                                                </sec:authorize>

                                                <div class="text-center"><a class="btn btn-outline-dark mt-auto"
                                                                            href="<c:url value='${dynamic_productControllerSeeProductDetailsLink}/${product.id}'/>">View details</a></div>
                                            </div>
                                        </div>
                                    </div>

                                </c:forEach>

                            </div>
                        </div>

                    </div>
                    <!-- END first slide -->

                    <!-- START second slide -->
                    <div class="carousel-item">

                        <div class="container px-4 px-lg-5 mt-5">
                            <div class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">

                                <c:forEach var="product" items="${secondSlideProductList}" >

                                    <div class="col mb-5">
                                        <div class="card h-100">

                                            <!-- Product image-->
                                            <img class="card-img-top" src="data:image/png;base64, ${product.imageBase64}" alt="image" />

                                            <!-- Product details-->
                                            <div class="card-body p-4">
                                                <div class="text-center">
                                                    <!-- Product name-->
                                                    <h5 class="fw-bolder">${product.name}</h5>
                                                    <!-- Product reviews-->
                                                    <div class="d-flex justify-content-center small text-warning mb-2">
                                                        <div class="bi-star-fill"></div>
                                                        <div class="bi-star-fill"></div>
                                                        <div class="bi-star-fill"></div>
                                                        <div class="bi-star-fill"></div>
                                                        <div class="bi-star-fill"></div>
                                                    </div>

                                                    <!-- Product price-->
                                                    $ ${product.price}
                                                </div>
                                            </div>

                                            <!-- Product actions-->
                                            <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                                                <sec:authorize access="!hasAnyAuthority('${roleEmployee}', '${roleAdmin}')">
                                                    <c:choose>
                                                        <c:when test="${product.stock > 0}">
                                                            <div class="text-center"><button class="btn btn-outline-dark mt-auto" onclick="addToCart(${product.id})" id="idAddToCartEnabled${product.id}">Add to cart</button></div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="text-center"><button disabled class="btn btn-outline-dark mt-auto" onclick="addToCart(${product.id})" id="idAddToCartEnabled${product.id}">Out of stock</button></div>
                                                        </c:otherwise>
                                                    </c:choose>


                                                    <c:if test="${product.addedToCart}">
                                                        <script>
                                                            markAsAdded(${product.id});
                                                        </script>
                                                    </c:if>
                                                    <br>
                                                </sec:authorize>

                                                <div class="text-center"><a class="btn btn-outline-dark mt-auto"
                                                                            href="<c:url value='${dynamic_productControllerSeeProductDetailsLink}/${product.id}'/>">View details</a></div>
                                            </div>
                                        </div>
                                    </div>

                                </c:forEach>


                            </div>
                        </div>

                    </div>
                    <!-- END second slide -->

                </div>
                <!-- END carousel slides -->

                <!-- START carousel controls -->
                <a class="carousel-control-prev" href="#carouselControls" role="button" data-slide="prev">
                    <span class="carousel-control-prev-icon custom-nav-icon" aria-hidden="false"></span>
                </a>

                <a class="carousel-control-next" href="#carouselControls" role="button" data-slide="next">
                    <span class="carousel-control-next-icon custom-nav-icon" aria-hidden="false"></span>
                </a>
                <!-- END carousel controls -->

            </div>
            <!-- END products carousel -->

        </section>

        <jsp:include page="templates/footer.jsp"/>

        <script>
            $('.carousel').carousel({
                interval: 10000
            })

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

    </body>
</html>
