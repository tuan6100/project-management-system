package com.project.oop.PMS.service;

import java.util.List;

import com.project.oop.PMS.dto.*;
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
	 public void removeMember(Integer projectId, Integer managerId, Integer memberId) throws CodeException;
	 public void deleteProject(Integer projectId, Integer managerId) throws CodeException;
	 public List<Task> getTasks(Integer projectId) throws CodeException;
	 public List<TaskResponse> getTasksInProject(Integer projectId) throws CodeException;
	public List<GetAllMemberForProjectResponse> addMember(Integer projectId, Integer managerId, List<String> usersName) throws CodeException;
	public List<RateReport> rateCompleteTaskByProjectOfUser(Integer projectId) throws CodeException;
	public List<TaskResponseForGetAll> OverdueTask(Integer projectId) throws CodeException;
	public List<RateReport> getRateCompleteOfTask(Integer projectId) throws CodeException;

}
