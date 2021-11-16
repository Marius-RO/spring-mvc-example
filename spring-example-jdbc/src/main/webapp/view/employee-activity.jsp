<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>

<html>
    <head>
        <title>Employees activity</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>
    </head>

    <body class="d-flex flex-column min-vh-100">

        <jsp:include page="templates/navbar.jsp"/>

        <!-- START employee activity table -->
        <div class="container px-3">
            <table class="table table-bordered" id="idEmployeeActivityTable">

                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Description</th>
                        <th>Registered</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>
                <c:forEach var="activity" items="${employeeActivityList}">

                    <tr>
                        <td>${activity.id}</td>
                        <td>${activity.tag}</td>
                        <td>${activity.added}</td>
                        <td>
                            <button type="button" class="btn-view-activity btn btn-primary"
                                    onclick="openEmployeeActivityModal('${activity.before}', '${activity.after}')">View activity</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- END employee activity table -->

        <!-- START employee activity modal -->
        <div class="modal fade" id="idEmployeeActivityModal" tabindex="-1" role="dialog" aria-labelledby="titleEmployeeActivityModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title" id="titleEmployeeActivityModal">Activity details</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeEmployeeActivityModal()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="modal-body">
                        <div class="container">
                            <div class="row">
                                <div id="idBeforeActivityModal" class="col-sm">

                                </div>

                                <div id="idAfterActivityModal" class="col-sm">

                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeEmployeeActivityModal()">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- END employee activity modal -->

        <jsp:include page="templates/footer.jsp"/>

        <!-- START employee activity script  -->
        <script type="text/javascript">

            $(document).ready(function() {
                $('#idEmployeeActivityTable').DataTable({
                    "pageLength": 10,
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": true,
                    "order": [[2, 'desc']]
                });
            });

            function openEmployeeActivityModal(before, after){
                document.getElementById("idBeforeActivityModal").textContent = before;
                document.getElementById("idAfterActivityModal").textContent = after;
                $('#idEmployeeActivityModal').modal('show')
            }

            function closeEmployeeActivityModal(){
                $('#idEmployeeActivityModal').modal('hide')
            }

        </script>
        <!-- END employee activity script  -->
    </body>
</html>