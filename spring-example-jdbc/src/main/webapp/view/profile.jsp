<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>
<%@ include file="templates/roles.jsp" %>

<html>
  <head>
      <title>Profile</title>
      <jsp:include page="templates/head-footer-dependencies.jsp"/>
      <style>
          .custom-change-pass-button-spacer {
              margin-bottom: 8px;
              margin-top: 12px;
          }
      </style>
  </head>

  <body class="d-flex flex-column min-vh-100">

        <jsp:include page="templates/navbar.jsp"/>

        <div class="d-flex justify-content-center">
          <div class="w-25 p-3">

              <div class="d-flex flex-column">

                  <!-- START Custom messages -->
                  <c:if test="${param.attributeSuccessUpdate != null}">
                      <div class="p-2">
                          <div class="alert alert-success" role="alert">
                              Profile updated
                          </div>
                      </div>
                  </c:if>

                  <c:if test="${param.attributeSuccessUpdatePassword != null}">
                      <div class="p-2">
                          <div class="alert alert-success" role="alert">
                              Password updated
                          </div>
                      </div>
                  </c:if>

                  <c:if test="${errorUpdatePassword}">
                      <div class="p-2">
                          <div class="alert alert-danger" role="alert">
                              Password was not updated
                          </div>
                      </div>
                  </c:if>

                  <c:if test="${errorDeletedAccount}">
                      <div class="p-2">
                          <div class="alert alert-danger" role="alert">
                              Your account could not be deleted
                          </div>
                      </div>
                  </c:if>
                  <!-- END Custom messages -->

                  <div class="p-2">
                      <h4 class="text-center">Your profile</h4>
                  </div>

                  <!-- START profile form -->

                  <div class="p-2">

                      <form:form action="${accountControllerProcessUpdateProfileLink}" method="post" modelAttribute="profileDto">

                          <div class="form-group">
                              <form:input path="firstName" class="form-control" placeholder="First name"/>
                              <form:errors path="firstName" cssClass="text-danger"/>
                          </div>

                          <div class="form-group">
                              <form:input path="lastName" class="form-control" placeholder="Last name"/>
                              <form:errors path="lastName" cssClass="text-danger"/>
                          </div>

                          <div class="form-group">
                              <form:textarea path="addressDto.fullAddress" class="form-control" placeholder="Address"/>
                              <form:errors path="addressDto.fullAddress" cssClass="text-danger"/>
                          </div>

                          <div class="form-group">
                              <form:input path="addressDto.phone" class="form-control" placeholder="Phone"/>
                              <form:errors path="addressDto.phone" cssClass="text-danger"/>
                          </div>

                          <div class="text-center">
                              <button type="submit" class="h-25 w-100 btn btn-primary">Save changes</button>
                          </div>

                      </form:form>

                      <!-- END profile form -->

                      <div class="text-center">
                          <button class="h-25 w-100 btn btn-primary custom-change-pass-button-spacer" onclick="openChangePasswordModal()">Change password</button>
                      </div>

                      <sec:authorize access="hasAnyAuthority('${roleCustomer}', '${roleEmployee}')">
                          <div class="text-center">
                              <button class="h-25 w-100 btn btn-danger" onclick="openDeleteAccountModal()">Delete account</button>
                          </div>
                      </sec:authorize>

                  </div>

              </div>
          </div>
        </div>

        <!-- START change password modal -->
        <div class="modal fade" id="idChangePasswordModal" tabindex="-1" role="dialog" aria-labelledby="titleChangePasswordModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title" id="titleChangePasswordModal">Change password</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeChangePasswordModal()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="modal-body">
                        <div class="p-2">

                            <form:form action="${accountControllerProcessUpdatePasswordLink}" method="post" modelAttribute="changePasswordDto">

                                <div class="form-group">
                                    <form:password path="oldPassword" class="form-control" placeholder="Old password"/>
                                    <form:errors path="oldPassword" cssClass="text-danger"/>
                                </div>

                                <div class="form-group">
                                    <form:password path="passwordDto.password" class="form-control" placeholder="New password"/>
                                    <form:errors path="passwordDto.password" cssClass="text-danger"/>
                                </div>

                                <div class="form-group">
                                    <form:password path="passwordDto.confirmedPassword" class="form-control" placeholder="Confirm new password"/>
                                    <form:errors path="passwordDto.confirmedPassword" cssClass="text-danger"/>
                                    <form:errors path="passwordDto" cssClass="text-danger"/>
                                </div>

                                <div class="text-center">
                                    <button type="submit" class="h-25 w-100 btn btn-primary">Change password</button>
                                </div>

                            </form:form>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeChangePasswordModal()">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- END change password modal -->

        <!-- START delete account modal -->
        <div class="modal fade" id="idDeleteAccountModal" tabindex="-1" role="dialog" aria-labelledby="titleDeleteAccountModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title" id="titleCDeleteAccountModal">Delete account</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeDeleteAccountModal()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="modal-body">
                        <div class="p-2">
                            Are you sure you want to delete your account?
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeDeleteAccountModal()">No</button>
                        <form:form action="${accountControllerProcessDeleteAccountLink}" method="post">
                            <div class="text-center">
                                <button type="submit" class="h-25 w-100 btn btn-danger">Yes</button>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>

        <!-- END delete account modal -->

        <jsp:include page="templates/footer.jsp"/>


        <!-- START change password script -->
        <script type="text/javascript">

            function openDeleteAccountModal(){
                $('#idDeleteAccountModal').modal('show')
            }

            function closeDeleteAccountModal(){
                $('#idDeleteAccountModal').modal('hide')
            }

            function openChangePasswordModal(){
                $('#idChangePasswordModal').modal('show')
            }

            function closeChangePasswordModal(){
                $('#idChangePasswordModal').modal('hide')
            }

            $(document).ready(function (){
                if('${errorUpdatePassword}'){
                    openChangePasswordModal()
                }
            });
        </script>
        <!-- END change password script -->

  </body>
</html>
