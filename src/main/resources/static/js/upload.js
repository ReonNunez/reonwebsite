$(document).ready(function () {
    $('#changeprofileimg').on('change', function () {
        uploadFile();
    });

    function uploadFile() {
        const formData = new FormData($('#uploadForm')[0]);

        $.ajax({
            url: '/changeprofileimg',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                $('#message').html('<div class="alert alert-success">File uploaded successfully!</div>');
                
            },
            error: function (error) {
                $('#message').html('<div class="alert alert-danger">File upload failed: ' + error.responseText + '</div>');
            }
        });
    }

    // Automatically trigger file upload upon selecting a file
    $('#changeprofileimg').trigger('change');
});
