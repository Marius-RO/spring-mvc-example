<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="templates/paths.jsp" %>
<html>

    <head>
        <title>Login</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    </head>

    <body>

        <div class="d-flex justify-content-center">
            <div class="w-25 p-3">

                <div class="d-flex flex-column">

                    <!-- Link param messages -->
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
                                You are logout
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${param.successRegister != null}">
                        <div class="p-2">
                            <div class="alert alert-success" role="alert">
                                Register successfully
                            </div>
                        </div>
                    </c:if>

                    <div class="p-2">
                        <h4 class="text-center">Login into your account</h4>
                    </div>

                    <!-- Login form -->

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

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>

</html>
