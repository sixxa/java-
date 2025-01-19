document.addEventListener("DOMContentLoaded", function () {
    // Set the browser info in the hidden field
    document.getElementById("cbrowser").value = navigator.userAgent;

    const form = document.getElementById("mForm");
    const validationStatus = { name: true, login: true, password: true, dni: true, email: true };

    // Validation function to show and clear error messages
    function showError(element, message) {
        document.getElementById(element.id + "-error").textContent = message;
    }

    function clearError(element) {
        document.getElementById(element.id + "-error").textContent = "";
    }

    // Field validation functions
    function validateField(field, pattern, message, statusKey) {
        if (!pattern.test(field.value)) {
            validationStatus[statusKey] = false;
            showError(field, message);
        } else {
            validationStatus[statusKey] = true;
            clearError(field);
        }
    }

    // Form submission validation
    form.addEventListener("submit", function (event) {
        event.preventDefault();  // Prevent form from submitting immediately

        // Validate each field when the form is submitted
        validateField(document.getElementById("cname"), /^[A-Za-zñÑáéíóúÁÉÍÓÚ]{3,}\s[A-Za-zñÑáéíóúÁÉÍÓÚ]{3,}$/, 
            "The name must consist of two words, each at least 3 letters long, and can only contain letters and accents.", "name");

        validateField(document.getElementById("clogin"), /^[a-z0-9]{4,8}$/, 
            "The login must be 4 to 8 characters long and contain only lowercase letters and numbers.", "login");

        validateField(document.getElementById("cpasswd"), /(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[+\-*/]).{6,12}/,
            "The password must be between 6 and 12 characters long, containing both uppercase and lowercase letters, a number, and an arithmetic symbol.", "password");

        validateField(document.getElementById("cdni"), /^[0-7]\d{7}[A-Z]$/, 
            "The DNI should begin with a digit from 0 to 7, followed by 7 additional digits, and end with a capital letter.", "dni");

        // Email validation
        validateField(document.getElementById("cemail"), /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, 
            "Please enter a valid email address.", "email");

        // If all validations pass, submit the form
        if (Object.values(validationStatus).every(Boolean)) {
            form.submit();  // Submit form if all validations pass
        } else {
            alert("Please correct the highlighted fields.");
        }
    });

    // Reset form error messages on reset
    form.addEventListener("reset", function () {
        document.querySelectorAll(".error-message").forEach(e => e.textContent = "");
    });
});
