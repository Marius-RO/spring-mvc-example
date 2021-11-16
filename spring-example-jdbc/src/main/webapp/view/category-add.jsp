<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="templates/paths.jsp" %>

<html>
      <head>
          <title>Add category</title>
          <jsp:include page="templates/head-footer-dependencies.jsp"/>
      </head>

      <body class="d-flex flex-column min-vh-100">

            <jsp:include page="templates/navbar.jsp"/>

            <div class="d-flex justify-content-center">
                <div class="w-25 p-3">

                    <div class="d-flex flex-column">

                        <spring:hasBindErrors name="categoryDto">
                            <div class="container">
                                <div class="alert alert-danger" role="alert">
                                    Some completion errors occurred
                                </div>
                            </div>
                        </spring:hasBindErrors>

                        <div class="p-2">
                            <h4 class="text-center">Add category</h4>
                        </div>

                        <div class="p-2">

                            <form:form action="${categoryControllerProcessAddCategoryLink}" method="post" modelAttribute="categoryDto">
                                <div class="form-group">
                                    <form:input id="idCategoryName" path="name" class="form-control" placeholder="Category name"/>
                                    <form:errors path="name" cssClass="text-danger"/>
                                </div>

                                <br/>

                                <div class="text-center">
                                    <button type="submit" class="h-25 w-100 btn btn-primary">Add</button>
                                </div>

                            </form:form>

                        </div>
                    </div>
                </div>
            </div>

            <jsp:include page="templates/footer.jsp"/>
      </body>
</html>
