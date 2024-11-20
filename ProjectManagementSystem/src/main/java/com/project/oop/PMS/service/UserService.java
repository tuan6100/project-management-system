package com.project.oop.PMS.service;

import com.project.oop.PMS.dto.ProjectResponse;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.repository.ProjectRepository;
import com.project.oop.PMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    
    public User registerUser(String username, String password) {
        // Kiểm tra trùng lặp username
        if (userRepository.findByUsernameAndPassword(username, password).isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        // Tạo và lưu người dùng mới
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    public Optional<User> findUserById(Integer userId) {
        return userRepository.findById(userId);
    }
    public List<ProjectResponse> getAllProjectsByUserId(int memberId) {
        List<Project> projects = projectRepository.findAllProjectsByUserId(memberId);
        return projects.stream()
                       .map(ProjectResponse::fromEntity)  // Ánh xạ từ entity sang DTO
                       .collect(Collectors.toList());
    }
}
