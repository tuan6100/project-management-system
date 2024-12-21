package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private String username;
    private String content;
    private String commentDate;

    public static CommentResponse fromEntity(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setUsername(comment.getUser().getUsername());
        commentResponse.setContent(comment.getContent());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String formattedDate = dateFormat.format(comment.getCommentDate());
        commentResponse.setCommentDate(formattedDate);
        return commentResponse;
    }
}
