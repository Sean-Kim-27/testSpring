package com.example.test.dto;

import com.example.test.entity.Board;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getMember() != null ? board.getMember().getUsername() : "알 수 없음";
        this.createdAt = board.getCreatedAt();
    }
}
