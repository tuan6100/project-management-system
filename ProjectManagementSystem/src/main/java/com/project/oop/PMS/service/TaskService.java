package com.project.oop.PMS.service;

import java.util.List;

import com.project.oop.PMS.dto.RateReport;
import com.project.oop.PMS.dto.TaskRequest;
import com.project.oop.PMS.dto.TaskResponseForGetAll;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;

public interface TaskService {
	public Task getTaskById(Integer taskId) throws CodeException;
	public Task createTask(TaskRequest taskRequest, Integer projectId, Integer managerId) throws CodeException;
	public String assignMember(Integer taskId, Integer memberId, Integer managerId) throws CodeException;
	public String removeMember(Integer taskId, Integer memberId, Integer managerId) throws CodeException;
	public List<User> getMembers(Integer taskId);
}
