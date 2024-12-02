// Popup create project 
document.addEventListener("DOMContentLoaded", () => {
    const addButton = document.querySelector(".prolist-func__add");
    const popup = document.getElementById("popup_create_project");
    const closeButton = popup.querySelector(".popup_create_project-close");
    const projectForm = document.getElementById("popup_create_project-form");
    const projectList = document.querySelector(".prolist-ol");

    // Hiển thị popup khi nhấn nút Add
    addButton.addEventListener("click", () => {
        popup.style.display = "flex";
    });

    // Ẩn popup khi nhấn nút Close
    closeButton.addEventListener("click", () => {
        popup.style.display = "none";
    });

    // Ẩn popup khi click bên ngoài popup-content
    popup.addEventListener("click", (event) => {
        if (event.target === popup) {
            popup.style.display = "none";
        }
    });

    // Gửi data về backend   
    document.addEventListener("DOMContentLoaded", () => {
        const form = document.getElementById("popup_create_project-form");
    
        form.addEventListener("submit", (event) => {
            event.preventDefault(); // Ngăn chặn hành động submit mặc định
    
            // Lấy ID từ localStorage
            const userId = localStorage.getItem("userId");
            if (!userId) {
                Toastify({
                    text: "User ID not found. Please login again.",
                    duration: 3000,
                    close: true,
                    gravity: "top",
                    position: "right",
                    backgroundColor: "linear-gradient(to right, #ff5f6d, #ffc371)",
                }).showToast();
                return;
            }
    
            // Lấy giá trị từ các trường input
            const projectName = document.getElementById("popup_create_project-name").value.trim();
            const projectDesc = document.getElementById("popup_create_project-desc").value.trim();
    
            // Tạo object chứa dữ liệu, bao gồm cả ID
            const projectData = {
                name: projectName,
                description: projectDesc,
                userId: userId, // Gửi kèm ID người dùng
            };
    
            // Gửi request đến backend
            fetch("http://localhost:8083/api/projects", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(projectData),
            })
                .then((response) => {
					console.log(response); 
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error("Failed to add project");
                    }
                })
                .then((data) => {
                    console.log("Project added successfully:", data);
    
                    // Hiển thị Toastify thông báo thành công
                    Toastify({
                        text: "Project added successfully!",
                        duration: 3000,
                        close: true,
                        gravity: "top",
                        position: "right",
                        backgroundColor: "linear-gradient(to right, #00b09b, #96c93d)",
                    }).showToast();
    
                    form.reset(); // Reset form
                })
                .catch((error) => {
                    console.error("Error:", error);
    
                    // Hiển thị Toastify thông báo lỗi
                    Toastify({
                        text: "An error occurred: " + error.message,
                        duration: 3000,
                        close: true,
                        gravity: "top",
                        position: "right",
                        backgroundColor: "linear-gradient(to right, #ff5f6d, #ffc371)",
                    }).showToast();
                });
        });
    });    
    
});
