// Check login before 
const userId = sessionStorage.getItem("userId");
console.log(userId);
if (!userId) {
    window.location.href = "http://localhost:8083/api/auth/login";
}

// Popup create project
const createProjectButton = document.querySelector(".prolist-func__add");
const popupCreateProject = document.getElementById("popup_create_project");
const closeCreateProjectButton = popupCreateProject.querySelector(".popup_create_project-close");
const projectCreateForm = document.getElementById("popup_create_project-form");
const projectCurrentList = document.querySelector(".prolist-ol");
const formCreateProject = document.getElementById("popup_create_project-form");
const buttonCreateProject = document.getElementById("buttonCreateProject");
const urlCreateProject = "http://localhost:8083/api/projects/add";

// Hiển thị popup khi nhấn nút Add
createProjectButton.addEventListener("click", () => {
    popupCreateProject.style.display = "flex";
});

// Ẩn popup khi nhấn nút Close
closeCreateProjectButton.addEventListener("click", () => {
    popupCreateProject.style.display = "none";
});

// Ẩn popup khi click bên ngoài popup-content
popupCreateProject.addEventListener("click", (event) => {
    if (event.target === popupCreateProject) {
        popupCreateProject.style.display = "none";
    }
});


// Add event listener for form submission (only once)
formCreateProject.addEventListener("submit", (event) => {
    event.preventDefault(); // Prevent default form submission
    handleCreateProject();
});

// Function to handle project creation
function handleCreateProject() {
    const projectData = getDataCreateProject();
    handleSendCreateProject(projectData);
}

// Function to gather project data from the form
function getDataCreateProject() {
    const projectName = document.getElementById("popup_create_project-name").value.trim();
    const projectDesc = document.getElementById("popup_create_project-desc").value.trim();

    return {
        name: projectName,
        description: projectDesc,
        userId: userId, // Ensure userId is defined in your script
    };
}

// Function to send the project data to the server
function handleSendCreateProject(projectData) {
    fetch(urlCreateProject, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(projectData),
    })
    .then((response) => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Create project failed");
        }
    })
    .then((data) => {
        console.log("Project added successfully:", data);

        // Display success notification
        Toastify({
            text: "Project added successfully!",
            duration: 3000,
            close: true,
            gravity: "bottom",
            position: "right",
            backgroundColor: "linear-gradient(to right, #00b09b, #96c93d)",
        }).showToast();

        formCreateProject.reset(); // Reset the form after successful submission
    })
    .catch((error) => {
        console.error("Error:", error);

        // Display error notification
        Toastify({
            text: "An error occurred: " + error.message,
            duration: 3000,
            close: true,
            gravity: "bottom",
            position: "right",
            backgroundColor: "linear-gradient(to right, #ff5f6d, #ffc371)",
        }).showToast();
    });
}