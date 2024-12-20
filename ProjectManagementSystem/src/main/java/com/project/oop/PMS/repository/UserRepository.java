package com.project.oop.PMS.repository;

import com.project.oop.PMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPassword(String userId, String password);
    User findByUserId(Integer userId);
}
