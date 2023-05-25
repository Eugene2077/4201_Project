/**
 * input form validation
 * @author Eugene Shin
 * @date: 25 Mar 2023
 */


// get the elements
const form = document.querySelector('form');
const idInput = document.querySelector('#id');
const passwordInput = document.querySelector('#inputPassword');
const confirmPasswordInput = document.querySelector('#inputPassword2');
const firstNameInput = document.querySelector('#inputFirst');
const lastNameInput = document.querySelector('#inputLast');
const emailInput = document.querySelector('#inputEmail');
// Get the button element
const btnRegSubmit = document.getElementById('btnRegSubmit');

// event listeners
idInput.addEventListener('blur', validateId);
passwordInput.addEventListener('blur', validatePassword);
confirmPasswordInput.addEventListener('blur', validateConfirmPassword);
firstNameInput.addEventListener('blur', validateFirstName);
lastNameInput.addEventListener('blur', validateLastName);
emailInput.addEventListener('blur', validateEmail);

// variable for validation success
let idVali = false;
let pwVali = false;
let fnVali = false;
let lnVali = false;
let emVali = false;

// submit button to disable by default
btnRegSubmit.disabled = true;

/**
 * id validation function
 */
function validateId() {
  // trim spaces
  const idValue = idInput.value.trim();
  const idErrorMessage = document.querySelector('#id-group .errorMessage');
    // if the input is empty
  if (idValue === '') {
    idErrorMessage.textContent = 'ID is required.';
    btnRegSubmit.disabled = true;
    // if the input is not 9 digit numeric
  } else if (!/^\d{9}$/.test(idValue)) {
	idErrorMessage.textContent = 'ID must be 9 digit Numeric value.';
	btnRegSubmit.disabled = true;
	// validation ok
  } else {
	// error message to empty
    idErrorMessage.textContent = '';
    // submit button to show when all the input fields are Valid
    if(idVali && pwVali && fnVali && lnVali && emVali) {
    btnRegSubmit.disabled = false;
    }
  }
}

/**
 * passwrod validation
 * Same as id validation
 */
function validatePassword() {
  const passwordValue = passwordInput.value.trim();
  if (passwordValue === '') {
    passwordInput.parentElement.querySelector('.errorMessage').textContent = 'Password is required.';
    btnRegSubmit.disabled = true;
  } else if (passwordValue.length < 8 || passwordValue.length > 40) {
    passwordInput.parentElement.querySelector('.errorMessage').textContent = 'Password must be between 8 and 40 characters long.';
    btnRegSubmit.disabled = true;
  } else {
    passwordInput.parentElement.querySelector('.errorMessage').textContent = '';
    if(idVali && pwVali && fnVali && lnVali && emVali) {
    btnRegSubmit.disabled = false;
    }
    
  }
}

/**
 * comfirm password validation
 * compare two passwords, rest are the same
 */
function validateConfirmPassword() {
  const confirmPasswordValue = confirmPasswordInput.value.trim();
  const confirmPasswordErrorMessage = document.querySelector('#pass2-group .errorMessage');
  if (confirmPasswordValue === '') {
    confirmPasswordErrorMessage.textContent = 'Confirm password is required.';
    btnRegSubmit.disabled = true;
  } else if (confirmPasswordValue !== passwordInput.value.trim()) {
    confirmPasswordErrorMessage.textContent = 'Passwords do not match.';
    btnRegSubmit.disabled = true;
  } else {
    confirmPasswordErrorMessage.textContent = '';
    if(idVali && pwVali && fnVali && lnVali && emVali) {
    btnRegSubmit.disabled = false;
    }
  }
}

/**
 * name validation
 */
function validateFirstName() {
  const firstNameValue = firstNameInput.value.trim();
  const firstNameErrorMessage = document.querySelector('#first-group .errorMessage');
  if (firstNameValue === '') {
    firstNameErrorMessage.textContent = 'First name is required.';
    btnRegSubmit.disabled = true;
    // name has to be not a number
  } else if (!isNaN(firstNameValue)) {
	firstNameErrorMessage.textContent = 'Name cannot be a numer.';
	btnRegSubmit.disabled = true;
  } else {
    firstNameErrorMessage.textContent = '';
    if(idVali && pwVali && fnVali && lnVali && emVali) {
    btnRegSubmit.disabled = false;
    }
  }
}

/**
 * name validation (the same as above)
 */
function validateLastName() {
  const lastNameValue = lastNameInput.value.trim();
  const lastNameErrorMessage = document.querySelector('#last-group .errorMessage');
  if (lastNameValue === '') {
    lastNameErrorMessage.textContent = 'Last name is required.';
    btnRegSubmit.disabled = true;
  } else if (!isNaN(lastNameValue)) {
	lastNameErrorMessage.textContent = 'Name cannot be a numer.';
	btnRegSubmit.disabled = true;
  } else {
    lastNameErrorMessage.textContent = '';
    if(idVali && pwVali && fnVali && lnVali && emVali) {
    btnRegSubmit.disabled = false;
    }
  }
}

/**
 * email validation
 * isValidEmail() method used
 */
function validateEmail() {
  const emailValue = emailInput.value.trim();
  const emailErrorMessage = document.querySelector('#email-group .errorMessage');
  if (emailValue === '') {
    emailErrorMessage.textContent = 'Email is required.';
    btnRegSubmit.disabled = true;
  } else if (!isValidEmail(emailValue)) {
    emailErrorMessage.textContent = 'Email is invalid.';
    btnRegSubmit.disabled = true;
  } else {
    emailErrorMessage.textContent = '';
    if(idVali && pwVali && fnVali && lnVali && emVali) {
    btnRegSubmit.disabled = false;
    }
  }
}

/**
 * used regex for email validation
 */
function isValidEmail(email) {
  const emailRegex = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;
  if(emailRegex.test(email)) {
	  btnRegSubmit.disabled = false;
	  return true;
  } else {
	  btnRegSubmit.disabled = true;
	  return false;
  }
}



  