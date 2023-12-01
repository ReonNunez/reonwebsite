// Function to load and display blog posts
function loadPosts(page) {
    $.ajax({
        type: 'GET',
        url: `/api/posts/all?page=${page}`,
        success: function (data) {
            displayPosts(data.content);
            createPagination(data);
        },
        error: function (error) {
            console.error('Error loading posts:', error);
        }
    });
}

// Function to display blog posts
function displayPosts(posts) {
    // Update your HTML to display the posts
}

// Function to create pagination links
function createPagination(data) {
    // Create pagination links based on data
    // Update your HTML to display pagination links
}

// Load the initial page of content when the page loads
$(document).ready(function () {
    loadPosts(1);
});
