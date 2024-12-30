package com.project.oop.PMS.service;

import com.project.oop.PMS.dto.ProjectResponseForGetAll;
import com.project.oop.PMS.dto.TaskResponse;
import com.project.oop.PMS.dto.UserTaskResponse;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;

import java.util.List;

public interface UserService {

	public User getUserById(Integer userId) throws CodeException;

	public User getUserByAuth(String username, String password) throws CodeException;
	public User register(String username, String password) throws CodeException;
	public User login(String username, String password) throws CodeException;

	public List<ProjectResponseForGetAll> getAllProjects(Integer userId);

	public List<TaskResponse> getAllTasksByUser(Integer userId);

	Integer getUserIdByUsername(String username);

	List<UserTaskResponse> getTasksForUser(Integer userId);
}
