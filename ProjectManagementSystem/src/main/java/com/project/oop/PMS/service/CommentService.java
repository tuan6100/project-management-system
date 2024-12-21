package com.project.oop.PMS.service;

import com.project.oop.PMS.entity.Comment;
import com.project.oop.PMS.exception.CodeException;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

    public Comment getCommentById(Integer commentId) throws CodeException;

    public Comment addComment(Integer taskId, Integer userId, String content) throws CodeException;

    public Comment editComment(Integer commentId, Integer userId, String content) throws CodeException;

    public void deleteComment(Integer commentId, Integer userId) throws CodeException;
}
