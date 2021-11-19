<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
    <head>
        <title>Orders</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>

        <script>
            function loadOrderProductsTable(orderId){
                $('#idOrderProductsTable' + orderId).DataTable({
                    "pageLength": 10,
                    "paging": true,
                    "lengthChange": false,
                    "searching": false,
                    "ordering": true,
                    "info": false,
                    "autoWidth": true
                });
            }
        </script>
    </head>
    <body class="d-flex flex-column min-vh-100">
        <jsp:include page="templates/navbar.jsp"/>

        <h1>Orders</h1>

        <!-- START custom messages -->
        <c:if test="${param.attributeSuccessSendOrder != null}">
            <div class="container">
                <div class="alert alert-success" role="alert">
                    Order sent
                </div>
            </div>
        </c:if>
        <!-- END custom messages -->

        <!-- START orders table -->
        <div class="container px-3">
            <table class="table table-bordered" id="idOrdersTable">

                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Date</th>
                        <th>Amount</th>
                        <th>Customer</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>
                <c:forEach var="order" items="${orderList}">

                    <tr>
                        <td>${order.id}</td>
                        <td>${order.added}</td>
                        <td>$ ${order.amount}</td>
                        <td>${order.fullName}</td>
                        <td>
                            <div class="d-flex justify-content-center">
                                <button type="button" class="btn-view-details btn btn-primary" onclick="openViewDetailsModal(${order.id})">View details</button>
                            </div>

                            <!-- START view details modal -->
                            <div class="modal fade" id="idViewDetailsModal${order.id}" tabindex="-1" role="dialog" aria-labelledby="titleViewDetailsModal${order.id}" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">

                                        <div class="modal-header">
                                            <h5 class="modal-title" id="titleViewDetailsModal${order.id}">Order info</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeViewDetailsModal(${order.id})">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>

                                        <div class="modal-body">
                                            <div class="p-2">
                                                <span class="font-weight-bold">Amount:</span> &nbsp; $ ${order.amount} <br/><br/>
                                                <span class="font-weight-bold">Customer:</span> &nbsp; ${order.fullName} <br/><br/>
                                                <span class="font-weight-bold">Delivery address: </span> &nbsp; ${order.fullAddress} <br/><br/>
                                                <span class="font-weight-bold">Products:</span> <br/>

                                                <!-- START order products table -->
                                                <table class="table table-bordered" id="idOrderProductsTable${order.id}">

                                                    <thead>
                                                        <tr>
                                                            <th>Name</th>
                                                            <th>Quantity</th>
                                                            <th>Item price</th>
                                                            <th>Total price</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody>
                                                        <c:forEach var="cartProductDto" items="${order.productsList}">
                                                            <tr>
                                                                <td>${cartProductDto.productName}</td>
                                                                <td>${cartProductDto.quantity}</td>
                                                                <td>$ ${cartProductDto.productPrice}</td>
                                                                <td>$ ${cartProductDto.totalPrice}</td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>

                                                <script>
                                                    loadOrderProductsTable(${order.id});
                                                </script>
                                                <!-- END order products table -->
                                            </div>
                                        </div>

                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeViewDetailsModal(${order.id})">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- END view details modal -->
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>

        <!-- END orders table -->

        <jsp:include page="templates/footer.jsp"/>

        <!-- START orders table script  -->
        <script type="text/javascript">

            function openViewDetailsModal(orderId){
                $('#idViewDetailsModal' + orderId).modal('show')
            }

            function closeViewDetailsModal(orderId){
                $('#idViewDetailsModal' + orderId).modal('hide')
            }

            $(document).ready(function() {
                $('#idOrdersTable').DataTable({
                    "pageLength": 10,
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": true,
                    "order": [[1, 'desc']]
                });
            });

        </script>
        <!-- END orders table script  -->

    </body>
</html>
