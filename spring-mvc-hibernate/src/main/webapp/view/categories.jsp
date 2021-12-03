<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="templates/paths.jsp" %>

<html>
    <head>
        <title>Categories</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>
    </head>

    <body class="d-flex flex-column min-vh-100">

        <jsp:include page="templates/navbar.jsp"/>

        <!-- START custom messages -->
        <c:if test="${param.attributeSuccessAddCategory != null}">
            <div class="container">
                <div class="alert alert-success" role="alert">
                    Category added
                </div>
            </div>
        </c:if>

        <c:if test="${param.attributeSuccessUpdateCategory != null}">
            <div class="container">
                <div class="alert alert-success" role="alert">
                    Category updated
                </div>
            </div>
        </c:if>

        <div id="idDeleteMessage" class="container" hidden>
            <div class="alert alert-success" role="alert">
                Category deleted
            </div>
        </div>

        <!-- END custom messages -->

        <div class="container">
            <a href="${categoryControllerAddCategoryLink}" class="btn btn-primary" role="button">Add category</a>
        </div>

        <br/>

        <!-- START categories table -->
        <div class="container px-3">
            <table class="table table-bordered" id="idCategoriesTable" width="100%" cellspacing="0">

                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Name</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="temp" items="${allCategories}">

                        <tr>
                            <td>${temp.id}</td>
                            <td>${temp.name}</td>
                            <td>
                                <div class="d-flex justify-content-center">
                                    <a href="#" class="btn btn-primary" role="button">View products</a> &nbsp;&nbsp;&nbsp;&nbsp;

                                    <a href="<c:url value="${dynamic_categoryControllerUpdateCategoryLink}/${temp.id}"/>"
                                       class="btn btn-primary" role="button">Update</a> &nbsp;&nbsp;&nbsp;&nbsp;

                                    <button type="button" class="btn-delete btn btn-danger" data-id="${temp.id}">Delete</button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                </tbody>
            </table>
        </div>

        <!-- END categories table -->

        <jsp:include page="templates/footer.jsp"/>

        <!-- START Categories Table script  -->
        <script type="text/javascript">

            $(document).ready(function() {

                const table = $('#idCategoriesTable').DataTable({
                    "pageLength": 10,
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": true,
                    "order": [[0, 'asc']],
                    "columnDefs": [
                         {"width": "8%", "targets": 0}
                    ]
                });

                $("#idCategoriesTable").on("click", ".btn-delete", function () {
                    const btnDelete = $(this);
                    const categoryId = btnDelete.attr("data-id");
                    if (confirm("Are you sure?\n\nIf you delete this category some products will remain without category!")) {
                        $.ajax(
                            {
                                data: {
                                    "_csrf": "${_csrf.token}"
                                },
                                url: "${pageContext.request.contextPath}${dynamic_categoryControllerProcessDeleteCategoryLink}/" + categoryId,
                                method: "POST",
                                success: function (response) {
                                    console.log(response);
                                    console.log("[SUCCESS-delete] categoryId = " + categoryId);
                                    table.row(btnDelete.parents("tr")).remove().draw();
                                    document.getElementById("idDeleteMessage").hidden = false;
                                },
                                error: function (err) {
                                    console.error(err);
                                    console.error("[ERROR-delete] categoryId = " + categoryId);
                                }
                            }
                        );
                    }
                });
            });

        </script>
        <!-- END Categories Table script  -->

    </body>
</html>
