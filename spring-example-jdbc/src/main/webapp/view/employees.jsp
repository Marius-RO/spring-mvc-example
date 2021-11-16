<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>

<html>
    <head>
        <title>Employees</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>
    </head>

    <body class="d-flex flex-column min-vh-100">

        <jsp:include page="templates/navbar.jsp"/>

        <!-- START custom messages -->
        <c:if test="${param.attributeSuccessRegister != null}">
            <div class="container">
                <div class="alert alert-success" role="alert">
                    Employee added
                </div>
            </div>
        </c:if>

        <div id="idDeleteMessage" class="container" hidden>
            <div class="alert alert-success" role="alert">
                Employee deleted
            </div>
        </div>
        <!-- END custom messages -->

        <div class="container">
            <a href="${accountControllerRegisterLink}" class="btn btn-primary" role="button">Add employee</a>
        </div>

        <br/>

        <!-- START employees table -->
        <div class="container px-3">
            <table class="table table-bordered" id="idEmployeesTable">

                <thead>
                    <tr>
                        <th>Email</th>
                        <th>First name</th>
                        <th>Last name</th>
                        <th>Last activity</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="temp" items="${allEmployeesList}">

                        <tr>
                            <td>${temp.first == null ? "" : temp.first.email}</td>
                            <td>${temp.first == null ? "" : temp.first.firstName}</td>
                            <td>${temp.first == null ? "" : temp.first.lastName}</td>
                            <td>${temp.second}</td>
                            <td>
                                <div class="d-flex justify-content-center">
                                    <a href="<c:url value="${dynamic_employeeControllerSeeActivityLink}?email=${temp.first == null ? '' : temp.first.email}"/>"
                                       class="btn btn-primary" role="button">See activity</a> &nbsp;&nbsp;&nbsp;&nbsp;

                                    <button type="button" class="btn-delete btn btn-danger" data-id="${temp.first == null ? '' : temp.first.email}">Delete</button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- END employees table -->

        <jsp:include page="templates/footer.jsp"/>

        <!-- START employees Table script  -->
        <script type="text/javascript">

            $(document).ready(function() {

                const table = $('#idEmployeesTable').DataTable({
                    "pageLength": 10,
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": true,
                     "order": [[0, 'asc']]
                });

                $("#idEmployeesTable").on("click", ".btn-delete", function () {
                    const btnDelete = $(this);
                    const employeeEmail = btnDelete.attr("data-id");
                    if (confirm("Are you sure?\n\nIf you delete this all employee activity will be deleted!")) {
                        $.ajax(
                            {
                                data: {
                                    "_csrf": "${_csrf.token}"
                                },
                                url: "${pageContext.request.contextPath}${dynamic_employeeControllerProcessDeleteEmployeeLink}?email=" + employeeEmail,
                                method: "POST",
                                success: function (response) {
                                    console.log(response);
                                    console.log("[SUCCESS-delete] employeeEmail = " + employeeEmail);
                                    table.row(btnDelete.parents("tr")).remove().draw();
                                    document.getElementById("idDeleteMessage").hidden = false;
                                },
                                error: function (err) {
                                    console.error(err);
                                    console.error("[ERROR-delete] employeeEmail = " + employeeEmail);
                                }
                            }
                        );
                    }
                });
            });

        </script>
        <!-- END employees table script  -->

    </body>
</html>

