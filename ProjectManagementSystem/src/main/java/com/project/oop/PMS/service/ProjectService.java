package com.project.oop.PMS.service;

import java.util.List;

import com.project.oop.PMS.dto.GetAllMemberForProjectResponse;
import com.project.oop.PMS.dto.ProjectRequest;
import com.project.oop.PMS.dto.TaskResponse;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;

public interface ProjectService {
	public Project createProject(ProjectRequest projectRequest, Integer userId) throws CodeException;
	public  List<Project> getAllProjects();
	public Project updateProject(Integer projectId, Integer managerId, ProjectRequest projectRequest) throws CodeException;
	 public Project getProjectById(Integer projectId) throws CodeException;
	 public User getManager(Integer projectId);
	 public List<GetAllMemberForProjectResponse> getMembers(Integer projectId);
	 public List<User> getMembersNotManager(Integer projectId);
	 public Project addMember(Integer projectId, Integer managerId, List<Integer> usersId) throws CodeException;
	 public void removeMember(Integer projectId, Integer managerId, Integer memberId) throws CodeException;
	 public void deleteProject(Integer projectId, Integer managerId) throws CodeException;
	 public List<Task> getTasks(Integer projectId) throws CodeException;
	 public List<TaskResponse> getTasksInProject(Integer projectId) throws CodeException;
}
