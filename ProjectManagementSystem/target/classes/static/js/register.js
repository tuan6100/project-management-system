function register(event) {
  event.preventDefault();
  const registerUrl = "/api/auth/register";
  const username = document.forms["register_form"]["username"].value;
  const password = document.forms["register_form"]["password"].value;
  const confirmPassword = document.forms["register_form"]["confirmPassword"].value;

  if (password !== confirmPassword) {
    Toastify({
      text: "Passwords do not match!",
      duration: 3000,
      close: true,
      gravity: "top",
      position: "right",
      backgroundColor: "linear-gradient(to right, #00b09b, #96c93d)",
      stopOnFocus: true,
    }).showToast();
    return;
  }

  const registerData = { username, password };

  fetch(registerUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(registerData),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error("Registration failed");
      }
    })
    .then((data) => {
      Toastify({
        text: "Registration successful!",
        duration: 3000,
        close: true,
        gravity: "top",
        position: "right",
        backgroundColor: "linear-gradient(to right, #00b09b, #96c93d)",
        stopOnFocus: true,
      }).showToast();
      window.location.href = "/api/auth/login"; // Redirect after registration
    })
    .catch((error) => {
      Toastify({
        text: "Registration failed: " + error.message,
        duration: 3000,
        close: true,
        gravity: "top",
        position: "right",
        backgroundColor: "linear-gradient(to right, #ff5f6d, #ffc371)",
        stopOnFocus: true,
      }).showToast();
    });
}
