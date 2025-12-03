package com.example.test.dto;

import com.example.test.entity.Comment;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private String username;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.username = comment.getMember().getUsername();
        this.createdAt = comment.getCreatedAt();
    }
}
