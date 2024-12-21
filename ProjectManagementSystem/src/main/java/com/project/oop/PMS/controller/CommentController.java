package com.project.oop.PMS.controller;

import com.project.oop.PMS.entity.Comment;
import com.project.oop.PMS.exception.CodeException;
import com.project.oop.PMS.service.CommentService;
import com.project.oop.PMS.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TaskService taskService;


    @PostMapping("/create/{userId}")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment, @PathVariable Integer projectId, @PathVariable Integer taskId, @PathVariable Integer userId) throws CodeException, CodeException {
        return ResponseEntity.ok().body(commentService.addComment(taskId, userId, comment.getContent()));
    }

    @GetMapping("get-all")
    public ResponseEntity<List<Comment>> getAllComment(@PathVariable Integer taskId) throws CodeException {
        return ResponseEntity.ok().body(taskService.getTaskById(taskId).getComments());
    }

    @PutMapping("{commentId}/edit/{userId}")
    public ResponseEntity<Comment> editComment(@RequestBody Comment comment, @PathVariable Integer commentId, @PathVariable Integer userId) throws CodeException {
        return ResponseEntity.ok().body(commentService.editComment(commentId, userId, comment.getContent()));
    }

    @DeleteMapping("{commentId}/delete/{userId}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer commentId, @PathVariable Integer userId) throws CodeException {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().body("Comment deleted successfully");
    }
}