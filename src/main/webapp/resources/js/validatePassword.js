function validatePassword() {
	var strength = PrimeFaces.widget.Password.prototype.testStrength($("#changePasswordForm\\:password").val());
	if (strength <= 30) {
		$("#changePasswordForm\\:passwordMsg").html('<span class="ui-message-error-icon"></span><span class="ui-message-error-detail">please set a strong password</span>');
		$("#changePasswordForm\\:password").addClass("ui-state-error");
		return false;
	} else {
		$("#changePasswordForm\\:passwordMsg").html('');
		$("#changePasswordForm\\:password").removeClass("ui-state-error");
		return true;
	}

}