// Check login before proceeding
import Toastify from 'toastify-js'


const userId = sessionStorage.getItem("userId");
if (!userId) {
    window.location.href = "login.html";
}

// Popup elements
const createProjectButton = document.querySelector(".prolist-func__add");
const popupCreateProject = document.getElementById("popup_create_project");
const closeCreateProjectButton = popupCreateProject.querySelector(".popup_create_project-close");
const formCreateProject = document.getElementById("popup_create_project-form");
const projectCurrentList = document.querySelector(".prolist-ol");
const buttonCreateProject = document.getElementById("buttonCreateProject");
const urlCreateProject = "http://localhost:8083/api/projects";
const urlDeleteProject = "YOUR_DELETE_PROJECT_API_URL";

// Utility to show/hide popup
function togglePopup(popup, isVisible) {
    popup.style.display = isVisible ? "flex" : "none";
}

// Show/Hide popup events
createProjectButton.addEventListener("click", () => togglePopup(popupCreateProject, true));
closeCreateProjectButton.addEventListener("click", () => togglePopup(popupCreateProject, false));
popupCreateProject.addEventListener("click", (event) => {
    if (event.target === popupCreateProject) togglePopup(popupCreateProject, false);
});

// Form submission handling
formCreateProject.addEventListener("submit", (event) => {
    event.preventDefault();
    handleCreateProject();
});

// Handle project creation
function handleCreateProject() {
    const projectData = getDataCreateProject();
    handleSendCreateProject(projectData);
}

// Fetch all projects
function getAllProjects(userId) {
    fetch(urlCreateProject, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ userId }),
    })
    .then((response) => response.json())
    .then((data) => {
        projectCurrentList.innerHTML = data.map((item) => `
            <li class="prolist-item" id="prolist-project-${item.projectId}">
                <p class="prolist-item__name">${item.projectName}</p>
                <div class="prolist-item__remove" onclick="removeProject(${item.projectId}, ${item.adminID})">
                    <span class="material-symbols-outlined">delete</span>
                </div>
            </li>
        `).join("");
    })
    .catch((error) => console.error("Error fetching projects:", error));
}

// Remove project
function removeProject(projectId, adminID) {
    if (userId !== adminID) {
        alert("You don't have permission to delete this project");
        return;
    }
    if (confirm("Are you sure you want to delete this project?")) {
        const projectItem = document.getElementById(`prolist-project-${projectId}`);
        if (projectItem) projectItem.remove();

        fetch(urlDeleteProject, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({projectId}),
        })
        .then((response) => {
            if (response.ok) return response.json();
            else throw new Error("Failed to delete project");
        })
        .then((data) => console.log("Project deleted successfully:", data))
        .catch((error) => {
            console.error("Error deleting project:", error);
            alert("Failed to delete the project. Please try again.");
        });
    }
}

// Get project data from form
function getDataCreateProject() {
    const projectName = document.getElementById("popup_create_project-name").value.trim();
    const projectDesc = document.getElementById("popup_create_project-desc").value.trim();
    return { name: projectName, description: projectDesc, userId: userId};
}

// Send project data to server
function handleSendCreateProject(projectData) {
    fetch(urlCreateProject, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        mode: 'no-cors',
        body: JSON.stringify(projectData),
    })
    .then((response) => response.json())
    .then((data) => {
        Toastify({
            text: "Project added successfully!",
            duration: 3000,
            close: true,
            gravity: "bottom",
            position: "right",
            backgroundColor: "linear-gradient(to right, #00b09b, #96c93d)",
        }).showToast();
        formCreateProject.reset();
        getAllProjects(userId);
    })
    .catch((error) => {
        console.error("Error:", error);
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

// Initial project fetch
getAllProjects(userId);
