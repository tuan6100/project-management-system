package com.project.oop.PMS.service;

import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;

public interface UserService {
	public User register(String username, String password) throws CodeException;
	public User login(String username, String password) throws CodeException;
}
