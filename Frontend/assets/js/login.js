function login(event) {
  event.preventDefault();
  const loginUrl = "http://localhost:8083/api/auth/login";
  const username = document.forms["login_form"]["username"].value;
  const password = document.forms["login_form"]["password"].value;

  const loginData = { username, password };

  fetch(loginUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(loginData),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      } else if (response.status === 401) {
        throw new Error("Invalid username or password");
      } else {
        throw new Error("Unexpected response from server");
      }
    })
    .then((data) => {
      sessionStorage.setItem("userId", data.data.id);
      sessionStorage.setItem("username", data.data.username);

      Toastify({
        text: "Login successful",
        duration: 3000,
        close: true,
        gravity: "top",
        position: "right",
        backgroundColor: "linear-gradient(to right, #00b09b, #96c93d)",
        stopOnFocus: true,
      }).showToast();

      // Chuyển hướng sang trang home
      window.location.href = "/home";
    })
    .catch((error) => {
      // Hiển thị thông báo lỗi
      Toastify({
        text: "Login failed: " + error.message,
        duration: 3000,
        close: true,
        gravity: "top",
        position: "right",
        backgroundColor: "linear-gradient(to right, #ff5f6d, #ffc371)",
        stopOnFocus: true,
      }).showToast();
    });
}
