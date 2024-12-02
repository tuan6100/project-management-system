package com.project.oop.PMS.repository;

import com.project.oop.PMS.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceReponsitory extends JpaRepository<Resource, Integer> {
}
