<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ut" uri="/WEB-INF/tag" %>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" isELIgnored="false" %>

<div class="modal fade" id="signUpModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="signUpModalTitle">
                    <ut:locale_tag key="modal.signup"/>
                </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form class="needs-validation" action="${pageContext.request.contextPath}/controller?command=signup"
                      method="post" novalidate>
                    <div class="form-group">
                        <label>
                            <ut:locale_tag key="name"/>
                        </label>
                        <input type="text" class="form-control" aria-describedby="nameHelp" placeholder="Enter name"
                               name="userName" minlength="4" required>
                        <div class="valid-feedback">
                            <ut:locale_tag key="valid"/>
                        </div>
                        <div class="invalid-feedback">
                            <ut:locale_tag key="invalid-name"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>
                            <ut:locale_tag key="password"/>
                        </label>
                        <input type="password" class="form-control" placeholder="Enter password" name="userPassword"
                               minlength="6" required>
                        <div class="valid-feedback">
                            <ut:locale_tag key="valid"/>
                        </div>
                        <div class="invalid-feedback">
                            <ut:locale_tag key="invalid-password"/>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-dark btn-lg btn-block">
                        <ut:locale_tag key="modal.button.create-acc"/>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Example starter JavaScript for disabling form submissions if there are invalid fields
    (function () {
        'use strict';
        window.addEventListener('load', function () {
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
            var forms = document.getElementsByClassName('needs-validation');
            // Loop over them and prevent submission
            var validation = Array.prototype.filter.call(forms, function (form) {
                form.addEventListener('submit', function (event) {
                    if (form.checkValidity() === false) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        }, false);
    })();
</script>