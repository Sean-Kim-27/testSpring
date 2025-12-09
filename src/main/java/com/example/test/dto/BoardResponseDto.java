package com.example.test.dto;

import com.example.test.entity.Board;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String username;
    private LocalDateTime createdAt;
    private int likeCount;
    private List<CommentResponseDto> comments;
    private boolean liked;
    private int commentCount;
    private String imageUrl;


    public BoardResponseDto(Board board, boolean liked, List<CommentResponseDto> commentDtos) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.imageUrl = board.getImageUrl();
        this.nickname = board.getMember() != null ? board.getMember().getNickname() : "알 수 없음";
        this.username = board.getMember().getUsername();
        this.createdAt = board.getCreatedAt();
        this.likeCount = board.getLikes().size();
        this.liked = liked;
        this.comments = commentDtos;
        this.commentCount = board.getComments().size();
    }
}
