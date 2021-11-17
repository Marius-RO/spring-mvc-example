<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>
<%@ include file="templates/roles.jsp" %>

<html>
    <head>
        <title>Register</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>
    </head>

    <body class="d-flex flex-column min-vh-100">

        <jsp:include page="templates/navbar.jsp"/>

        <div class="d-flex justify-content-center">
            <div class="w-25 p-3">

                <div class="d-flex flex-column">

                    <!-- START custom messages -->

                    <c:if test="${param.error != null}">
                        <div class="p-2">
                            <div class="alert alert-danger" role="alert">
                                Some errors occurred while registering
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${param.successDeletedAccount != null}">
                        <div class="p-2">
                            <div class="alert alert-success" role="alert">
                                Your account was deleted
                            </div>
                        </div>
                    </c:if>

                    <!-- END custom messages -->

                    <div class="p-2">
                        <h4 class="text-center">Register a new account</h4>
                    </div>

                    <!-- START register form -->

                    <div class="p-2">

                        <form:form action="${accountControllerProcessRegisterLink}" method="POST" modelAttribute="registerDto">

                            <div class="form-group">
                                <form:input path="email" class="form-control" placeholder="Email"/>
                                <form:errors path="email" cssClass="text-danger"/>
                            </div>

                            <br/>

                            <div class="form-group">
                                <form:password path="passwordDto.password" class="form-control" placeholder="Password"/>
                                <form:errors path="passwordDto.password" cssClass="text-danger"/>
                            </div>

                            <br/>

                            <div class="form-group">
                                <form:password path="passwordDto.confirmedPassword" class="form-control" placeholder="Confirm password"/>
                                <form:errors path="passwordDto.confirmedPassword" cssClass="text-danger"/>
                                <form:errors path="passwordDto" cssClass="text-danger"/>
                            </div>

                            <br/>

                            <div class="form-group">
                                &nbsp;&nbsp;&nbsp;&nbsp; <form:checkbox path="termsCheck" class="form-check-input" id="idTermsCheck"/>
                                <label class="form-check-label" for="idTermsCheck"> &nbsp; I agree with <a href="#" onclick="openTermsModal()">terms and conditions</a></label>
                                <br/>
                                <form:errors path="termsCheck" cssClass="text-danger"/>
                            </div>

                            <br/>

                            <sec:authorize access="isAuthenticated() && hasAuthority('${roleAdmin}')">
                                <div class="form-group">
                                    &nbsp;&nbsp;&nbsp;&nbsp; <form:checkbox path="isEmployeeCheck" class="form-check-input" id="idEmployeeCheck" checked="true"/>
                                    <label class="form-check-label" for="idEmployeeCheck"> &nbsp; Is an employee account</label>
                                    <br/>
                                    <form:errors path="isEmployeeCheck" cssClass="text-danger"/>
                                </div>

                                <br/>
                            </sec:authorize>

                            <div class="text-center">
                                <button type="submit" class="h-25 w-100 btn btn-primary">Register</button>
                            </div>

                        </form:form>

                    </div>

                    <!-- END register form -->

                </div>
            </div>
        </div>

        <!-- START terms modal -->
        <div class="modal fade" id="idTermsModal" tabindex="-1" role="dialog" aria-labelledby="titleTermsModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title" id="titleTermsModal">Terms and conditions</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeTermsModal()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="modal-body">
                        Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                        Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
                        when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                        It has survived not only five centuries, but also the leap into electronic typesetting,
                        remaining essentially unchanged. It was popularised in the 1960s with the release of
                        Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing
                        software like Aldus PageMaker including versions of Lorem Ipsum.
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeTermsModal()">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- END terms modal -->

        <jsp:include page="templates/footer.jsp"/>

        <!-- START terms modal script -->
        <script type="text/javascript">
            function openTermsModal(){
                $('#idTermsModal').modal('show')
            }

            function closeTermsModal(){
                $('#idTermsModal').modal('hide')
            }
        </script>

        <!-- END terms modal script -->
    </body>

</html>
