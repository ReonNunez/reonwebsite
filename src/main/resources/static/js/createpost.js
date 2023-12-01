// Assume you have the HTML content in a variable named 'htmlContent'
var title = document.getElementById("blogtitle").value;
var privacyOption = document.querySelector('input[name="radioprivacy"]:checked').value;

var content = {
    title: title,
    html: tinymce.get("mytextarea").getContent(),
    privacyOption: privacyOption
};

// Make an AJAX POST request to your Spring MVC controller
$.ajax({
    type: "POST",
    url: "/CreateNewPost", // Replace with your actual endpoint URL
    data: content,
    dataType: "json",
    contentType: "application/json;charset=UTF-8",
    success: function(response) {
        console.log("Response from server: " + response);
    },
    error: function(error) {
        // Handle any errors that occur during the request
        console.log(error.responseText);
    }
});

