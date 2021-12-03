<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>
<%@ include file="templates/roles.jsp" %>

<html>
      <head>
          <title>Cart</title>
          <jsp:include page="templates/head-footer-dependencies.jsp"/>
      </head>

      <body class="d-flex flex-column min-vh-100">
            <jsp:include page="templates/navbar.jsp"/>

            <div class="container">

                <!-- START Custom messages -->
                <c:if test="${attributeErrorAddress}">
                    <div class="p-2">
                        <div class="alert alert-danger text-center" role="alert">
                            Some address error occurred
                        </div>
                    </div>
                </c:if>

                <c:if test="${attributeErrorSendOrder}">
                    <div class="p-2">
                        <div class="alert alert-danger text-center" role="alert">
                            Order could not be send
                        </div>
                    </div>
                </c:if>
                <!-- END Custom messages -->

                <sec:authorize access="!isAuthenticated()">
                    <div class="alert alert-warning text-center" role="alert"><h5>You are not logged in!</h5></div>
                </sec:authorize>

                <div class="p-2">
                    <h2 class="text-center">Your cart</h2>
                </div>

                <sec:authorize access="hasAuthority('${roleCustomer}')">
                    <button class="btn btn-primary" onclick="openAddressModalModal()">Send order</button>
                </sec:authorize>
                <h4 class="float-right">Total: $ <span id="idTotalAmount">${sessionScope.sessionCart.totalAmount}</span></h4>
            </div>

            <br/>

            <!-- START cart table -->
            <div class="container px-3">
                <table class="table table-bordered" id="idCartTable">

                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Quantity</th>
                            <th>Available</th>
                            <th>Item price</th>
                            <th>Total price</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody>

                    <c:if test="${sessionScope.sessionCart != null}">
                            <c:forEach var="cartProductDto" items="${sessionScope.sessionCart.cart}">

                                <tr>
                                    <td>${cartProductDto.productName}</td>

                                    <td>
                                        <div class="d-inline-flex">
                                            <select class="item-quantity" value="${cartProductDto.quantity}"
                                                    onchange="updateQuantity(this, ${cartProductDto.productId}, ${cartProductDto.productPrice})">

                                                <c:forEach var="idx" items="${sessionScope.sessionCart.idxList}">
                                                    <c:choose>
                                                        <c:when test="${cartProductDto.quantity == idx}">
                                                            <option value="${idx}" selected>${idx}</option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${idx}">${idx}</option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>

                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                            <div class="alert-success w-75 h-25 p-1" id="idQuantityModified${cartProductDto.productId}" hidden>
                                                Modified
                                            </div>
                                        </div>

                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${cartProductDto.outOfStock}">
                                                <div class="alert alert-danger" role="alert">Out of stock</div>
                                            </c:when>
                                            <c:when test="${cartProductDto.tooMuchQuantity}">
                                                <div class="alert alert-danger" role="alert">Too much quantity (current stock is ${cartProductDto.currentStock})</div>
                                            </c:when>
                                            <c:otherwise>Yes</c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>${cartProductDto.productPrice}</td>
                                    <td id="idTotalPrice${cartProductDto.productId}"> ${cartProductDto.totalPrice} </td>

                                    <td>
                                        <div class="d-flex justify-content-center">
                                            <a class="btn btn-primary" role="button" href="<c:url value="${dynamic_productControllerSeeProductDetailsLink}/${cartProductDto.productId}"/>">
                                               View details
                                            </a> &nbsp;&nbsp;

                                            <button type="button" class="btn-delete btn btn-danger" data-id="${cartProductDto.productId}">Delete</button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>

                    </tbody>
                </table>
            </div>
            <!-- END cart table -->


            <!-- START address modal -->
            <div class="modal fade" id="idAddressModal" tabindex="-1" role="dialog" aria-labelledby="titleAddressModalModal" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">

                        <div class="modal-header">
                            <h5 class="modal-title" id="titleAddressModalModal">Order details</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeAddressModalModal()">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>

                        <div class="modal-body">
                            <div class="p-2">

                                <form:form action="${orderControllerProcessSendOrderLink}" method="post" modelAttribute="orderDetailsDto">

                                    <div class="form-group">
                                        <form:input path="fullName" class="form-control" placeholder="First name"/>
                                        <form:errors path="fullName" cssClass="text-danger"/>
                                    </div>

                                    <div class="form-group">
                                        <form:textarea path="fullAddress" class="form-control" placeholder="Address"/>
                                        <form:errors path="fullAddress" cssClass="text-danger"/>
                                    </div>

                                    <div class="text-center">
                                        <button type="submit" class="h-25 w-100 btn btn-primary">Finalize order</button>
                                    </div>

                                </form:form>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeAddressModalModal()">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- END address modal -->

            <jsp:include page="templates/footer.jsp"/>


            <script type="text/javascript">

                // START address modal script
                function openAddressModalModal(){
                    $('#idAddressModal').modal('show')
                }

                function closeAddressModalModal(){
                    $('#idAddressModal').modal('hide')
                }

                $(document).ready(function (){
                    if('${attributeErrorAddress}'){
                        openAddressModalModal()
                    }
                });
                // END address modal script

                // START cart table script
                // update quantity
                function updateQuantity(input, productId, productPrice){
                    const newQuantity = parseInt(input.value, 10);
                    if (isNaN(newQuantity) || newQuantity < 0) {
                        return;
                    }

                    $.ajax(
                        {
                            data: {
                                "_csrf": "${_csrf.token}"
                            },
                            url: "${pageContext.request.contextPath}${dynamic_productControllerProcessUpdateCartQuantityLink}/" + productId + "/" + newQuantity,
                            method: "POST",
                            dataType: 'json',
                            success: function (response) {
                                document.getElementById("idTotalPrice" + productId).textContent = (productPrice * newQuantity).toString()
                                document.getElementById("idQuantityModified" + productId).hidden = false
                                document.getElementById("idTotalAmount").textContent = response.totalAmount;
                            },
                            error: function (err) {
                                console.error(err);
                            }
                        }
                    );
                }


                // delete product
                $(document).ready(function() {

                    const table = $('#idCartTable').DataTable({
                        "paging": false,
                        "lengthChange": false,
                        "searching": false,
                        "ordering": true,
                        "info": false,
                        "autoWidth": true
                    });

                    $("#idCartTable").on("click", ".btn-delete", function () {
                        const btnDelete = $(this);
                        const productId = btnDelete.attr("data-id");

                        $.ajax(
                            {
                                data: {
                                    "_csrf": "${_csrf.token}"
                                },
                                url: "${pageContext.request.contextPath}${dynamic_productControllerProcessDeleteFromCartLink}/" + productId,
                                method: "POST",
                                dataType: 'json',
                                success: function (response) {
                                    table.row(btnDelete.parents("tr")).remove().draw();
                                    document.getElementById("idCartBadge").textContent = response.cartItems;
                                    document.getElementById("idTotalAmount").textContent = response.totalAmount;
                                },
                                error: function (err) {
                                    console.error(err);
                                }
                            }
                        );

                    });
                });
                // END cart table script

            </script>


      </body>
</html>
