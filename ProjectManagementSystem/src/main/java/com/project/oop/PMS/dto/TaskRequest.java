package com.project.oop.PMS.dto;


import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    private String title;
    private String description;
    private Date dueDate;

    public Task toTask() {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        return task;
    }
}
