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
                                    onclick="openEmployeeActivityModal(${activity.id})">View activity</button>

                            <!-- START employee activity modal -->
                            <div class="modal fade" id="idEmployeeActivityModal${activity.id}" tabindex="-1" role="dialog" aria-labelledby="titleEmployeeActivityModal${activity.id}" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">

                                        <div class="modal-header">
                                            <h5 class="modal-title" id="titleEmployeeActivityModal${activity.id}">Activity details (${activity.tag})</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeEmployeeActivityModal(${activity.id})">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>

                                        <div class="modal-body">

                                            <div class="container">
                                                <div class="row">
                                                    <div class="col">
                                                            <h4>Before</h4><br/><br/>
                                                            <pre id="idBeforeActivityModal${activity.id}">
                                                                    ${activity.before}
                                                            </pre>
                                                            <br/><br/>
                                                    </div>

                                                    <div class="col">
                                                            <h4>After</h4><br/><br/>

                                                            <pre id="idAfterActivityModal${activity.id}">
                                                                    ${activity.after}
                                                            </pre>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeEmployeeActivityModal(${activity.id})">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- END employee activity modal -->
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- END employee activity table -->



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

            function openEmployeeActivityModal(id){

                let elemBefore = document.getElementById("idBeforeActivityModal" + id);
                let beforeJsonValue = JSON.parse(elemBefore.textContent);
                if(JSON.stringify(beforeJsonValue, undefined, 2) === '{}'){
                    elemBefore.textContent = "{}";
                }
                else{
                    beforeJsonValue.imageBase64 = "";
                    elemBefore.textContent = JSON.stringify(beforeJsonValue, undefined, 2);
                }

                let elemAfter = document.getElementById("idAfterActivityModal" + id);
                let afterJsonValue = JSON.parse(elemAfter.textContent);
                if(JSON.stringify(afterJsonValue, undefined, 2) === '{}'){
                    elemAfter.textContent = "{}";
                }
                else{
                    afterJsonValue.imageBase64 = "";
                    elemAfter.textContent = JSON.stringify(afterJsonValue, undefined, 2);
                }

                $('#idEmployeeActivityModal' + id).modal('show');
            }

            function closeEmployeeActivityModal(id){
                $('#idEmployeeActivityModal' + id).modal('hide');
            }

        </script>
        <!-- END employee activity script  -->
    </body>
</html>