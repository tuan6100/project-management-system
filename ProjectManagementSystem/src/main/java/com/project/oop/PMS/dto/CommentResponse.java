package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Integer commentId;
    private String username;
    private String content;
    private Date commentDate;

    public static CommentResponse fromEntity(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setCommentId(comment.getCommentId());
        commentResponse.setUsername(comment.getUser().getUsername());
        commentResponse.setContent(comment.getContent());
        commentResponse.setCommentDate(comment.getCommentDate());
        return commentResponse;
    }
}
