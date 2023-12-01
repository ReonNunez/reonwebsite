// Function to validate the password complexity
function validatePassword() {
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value
    // Add your password complexity validation logic here
    // For example, check if the password contains at least 8 characters, a digit, and a special character.
    if (password.length < 8 || !/\d/.test(password) || !/[!@#$%^&*]/.test(password)) {
        //alert("Password must be at least 8 characters long and contain at least one digit and one special character.");
        document.getElementById("password-error").innerHTML = "Password must be at least 8 characters long and contain at least one digit and one special character.";
        document.getElementById("signupbtn").disabled = true;
    } else if (password != confirmPassword){
        //alert("Passwords must be the same. Check your password inputs.");
        document.getElementById("password-error").innerHTML = "Passwords must be the same. Check your password inputs.";
        document.getElementById("signupbtn").disabled = true;
    } else {
        document.getElementById("password-error").innerHTML = "";
        document.getElementById("signupbtn").disabled = false;
    }
}

function validateEmail(){
    var email = document.getElementById("email").value;
    var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (!email.match(emailPattern)) {
        document.getElementById("email-error").innerHTML = "Invalid email format.";
        document.getElementById("signupbtn").disabled = true;
    } else {
        document.getElementById("email-error").innerHTML = "";
        document.getElementById("signupbtn").disabled = false;
    }
}

function validateUsername() {
    var username = document.getElementById("username").value;
    // Add your username validation logic here
    // For example, check if the username is at least 5 characters long.
    if (username.length < 5) {
        document.getElementById("username-error").innerHTML = "Username must be at least 5 characters.";
        document.getElementById("signupbtn").disabled = true;
    } else if (username.length > 30) {
        document.getElementById("username-error").innerHTML = "Username character limit reached.";
        document.getElementById("signupbtn").disabled = true;
    } else {
        document.getElementById("username-error").innerHTML = "";
        document.getElementById("signupbtn").disabled = false;
    }
}

document.getElementById("username").addEventListener("input", validateUsername);
document.getElementById("password").addEventListener("input", validatePassword);
document.getElementById("confirmPassword").addEventListener("input", validatePassword);
document.getElementById("email").addEventListener("input", validateEmail);