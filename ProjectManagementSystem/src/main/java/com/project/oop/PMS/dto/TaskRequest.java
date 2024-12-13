package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    private String title;
    private String description;
    private Date dueDate;
    private Task.TaskStatus status; // Thêm trường này

    public Task toTask() {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        // Nếu status được truyền thì gán, nếu không mặc định là pending
        task.setStatus(status != null ? status : Task.TaskStatus.pending);
        return task;
    }
}
