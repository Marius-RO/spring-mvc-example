<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="templates/paths.jsp" %>
<html>

    <head>
        <title>Login</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>
    </head>

    <body class="d-flex flex-column min-vh-100">

        <jsp:include page="templates/navbar.jsp"/>

        <div class="d-flex justify-content-center">
            <div class="w-25 p-3">

                <div class="d-flex flex-column">

                    <!-- START Custom messages -->
                    <c:if test="${param.error != null}">
                        <div class="p-2">
                            <div class="alert alert-danger" role="alert">
                                Invalid email or password
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${param.logout != null}">
                        <div class="p-2">
                            <div class="alert alert-success" role="alert">
                                Logout successfully
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${param.attributeSuccessRegister != null}">
                        <div class="p-2">
                            <div class="alert alert-success" role="alert">
                                Register successfully
                            </div>
                        </div>
                    </c:if>

                    <!-- END Custom messages -->

                    <div class="p-2">
                        <h4 class="text-center">Login into your account</h4>
                    </div>

                    <!-- START Login form -->

                    <div class="p-2">

                        <!-- Is used 'input' instead 'form:input' in order to avoid 'path' parameter for 'form:input' because login is managed automatically by framework -->
                        <form:form method="post">

                            <div class="form-group">
                                <input name="username" type="email" class="form-control" placeholder="Email">
                            </div>

                            <br />

                            <div class="form-group">
                                <input name="password" type="password" class="form-control" placeholder="Password">
                            </div>

                            <br />

                            <div class="text-center">
                                <button type="submit" class="h-25 w-100 btn btn-primary">Login</button>
                            </div>


                        </form:form>

                    </div>

                    <!-- END Login form -->

                    <div class="p-2">
                        <div class="text-center">
                                <h6>
                                    Not have an account? <a href="${accountControllerRegisterLink}">Register</a>
                                </h6>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <jsp:include page="templates/footer.jsp"/>

    </body>

</html>
