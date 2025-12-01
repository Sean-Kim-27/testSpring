package com.example.test.dto;

import com.example.test.entity.Board;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String username;
    private LocalDateTime createdAt;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getMember() != null ? board.getMember().getNickname() : "알 수 없음";
        this.username = board.getMember().getUsername();
        this.createdAt = board.getCreatedAt();
    }
}
