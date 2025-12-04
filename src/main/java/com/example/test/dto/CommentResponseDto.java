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
    private int likeCount;
    private boolean liked;

    public CommentResponseDto(Comment comment, boolean liked) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.username = comment.getMember().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = comment.getLikes().size();
        this.liked = liked;
    }
}
