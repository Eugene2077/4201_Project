/**
 * 
 */

console.log("Script loaded:");

function deleteUser() {
  let userId = this.getAttribute("data-userid");
  let usertype = this.getAttribute("data-usertype");

  // Prompt the user for confirmation
  let confirmDelete = confirm("Are you sure you want to delete this user?");

  // If the user confirms deletion, perform the deletion
  if (confirmDelete) {
    // Perform the deletion using AJAX or form submission
    
    $.ajax({
      type: "POST",
      url: "./update",
      data: {
		  id: userId,
		  command: "delete",
		  type: usertype
		  },
      success: function() {
        // Refresh the page or update the table to reflect the deletion
        location.reload();
      }
    });
  }
}

// Get all delete buttons and add event listener
let deleteButtons = document.querySelectorAll(".delete-user");
for (let i = 0; i < deleteButtons.length; i++) {
  deleteButtons[i].addEventListener("click", deleteUser);
}