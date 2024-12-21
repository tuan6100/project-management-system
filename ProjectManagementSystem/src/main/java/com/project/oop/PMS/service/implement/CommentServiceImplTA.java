package com.project.oop.PMS.service.implement;

import com.project.oop.PMS.entity.Comment;
import com.project.oop.PMS.entity.Project;
import com.project.oop.PMS.entity.Task;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.repository.CommentRepository;
import com.project.oop.PMS.service.CommentService;
import com.project.oop.PMS.service.ProjectService;
import com.project.oop.PMS.service.TaskService;
import com.project.oop.PMS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentServiceImplTA implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    @Lazy
    private TaskService taskService;

    @Qualifier("projectServiceImplement")
    @Autowired
    @Lazy
    private ProjectService projectService;

    @Autowired
    @Lazy
    private UserService userService;


    @Override
    public Comment getCommentById(Integer commentId) throws CodeException {
        return commentRepository.findById(commentId).
                orElseThrow(() -> new CodeException("Comment not found"));
    }

    @Override
    public Comment addComment(Integer taskId, Integer userId, String content) throws CodeException {
        Task task = taskService.getTaskById(taskId);
        User user = userService.getUserById(userId);
        Project project = task.getProject();
        if (!(projectService.getMembersIdOfProject(project.getProjectId()).contains(userId))) {
            throw new CodeException("User" + user.getUsername() + "is not a member of this project");
        }
        Comment comment = new Comment(task, user, content, new Date());
        return commentRepository.save(comment);
    }

    @Override
    public Comment editComment(Integer commentId, Integer userId, String content) throws CodeException {
        Comment comment = getCommentById(commentId);
        User user = userService.getUserById(userId);
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new CodeException("You cannot edit this comment");
        }
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Integer commentId, Integer userId) throws CodeException {
        Comment comment = getCommentById(commentId);
        User user = userService.getUserById(userId);
        Task task = comment.getTask();
        Project project = task.getProject();
        if ((!comment.getUser().getUserId().equals(user.getUserId())) && (!projectService.getManager(project.getProjectId()).getUserId().equals(userId))) {
            throw new CodeException("You cannot delete this comment");
        }
        commentRepository.delete(comment);
    }
}
