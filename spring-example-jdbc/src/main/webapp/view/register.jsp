<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="templates/paths.jsp" %>
<html>
    <head>
        <title>Register</title>
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
                                Some errors occurred while registering
                            </div>
                        </div>
                    </c:if>

                    <div class="p-2">
                        <h4 class="text-center">Register a new account</h4>
                    </div>

                    <!-- Register form -->

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
                                <form:checkbox path="termsCheck" class="form-check-input" id="idTermsCheck"/>
                                <label class="form-check-label" for="idTermsCheck">I agree with <a href="#" onclick="openTermsModal()">terms and conditions</a></label>
                                <br/>
                                <form:errors path="termsCheck" cssClass="text-danger"/>
                            </div>

                            <br/>

                            <div class="form-group">
                                <form:checkbox path="employeeCheck" class="form-check-input" id="idEmployeeCheck"/>
                                <label class="form-check-label" for="idEmployeeCheck">Is an employee account</label>
                            </div>

                            <br/>

                            <div class="text-center">
                                <button type="submit" class="h-25 w-100 btn btn-primary">Register</button>
                            </div>

                        </form:form>

                    </div>

                </div>
            </div>
        </div>

        <!-- Modal -->
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

        <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script type="text/javascript">
            function openTermsModal(){
                $('#idTermsModal').modal('show')
            }

            function closeTermsModal(){
                $('#idTermsModal').modal('hide')
            }
        </script>
    </body>

</html>
