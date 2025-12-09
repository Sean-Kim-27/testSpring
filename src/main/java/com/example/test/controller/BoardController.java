package com.example.test.controller;

import com.example.test.dto.BoardResponseDto;
import com.example.test.dto.CommentResponseDto;
import com.example.test.entity.Board;
import com.example.test.service.BoardService;
import com.example.test.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@CrossOrigin(origins ={"http://localhost:3000", "https://test-react-delta-woad.vercel.app"})
public class BoardController {
    private final BoardService boardService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ImageService imageService;

    @PostMapping
    public BoardResponseDto createBoard(@RequestBody Map<String, String> params,
                                        @AuthenticationPrincipal UserDetails userdetails) {

        BoardResponseDto newBoard = boardService.createBoard(
                params.get("title"),
                params.get("content"),
                params.get("imageUrl"),
                userdetails.getUsername()
        );
        try {
            messagingTemplate.convertAndSend("/topic/new-board", "REFRESH");
        } catch (Exception e) {
            // 에러 나면 로그만 찍고 넘어감 (글 쓰기는 성공 처리)
            System.out.println("⚠️ 웹소켓 방송 실패 (근데 알빠임?): " + e.getMessage());
        }
        return newBoard;
    }
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,
                              @AuthenticationPrincipal UserDetails userdetails) {
        if(file.isEmpty()) throw new RuntimeException("파일 비었네");
        return imageService.uploadImage(file);
    }
    @PostMapping("/{id}/like")
    public String toggleLike(@PathVariable Long id, @AuthenticationPrincipal UserDetails userdetails) {
        boardService.toggleLike(id, userdetails.getUsername());
        return "개추 반영 ㅋㅋ";
    }
    @PostMapping("/comments/{commentId}/like")
    public String toggleCommentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userdetails) {
        boardService.toggleCommentLike(commentId, userdetails.getUsername());
        return "댓개추 반영 ㅋㅋ";
    }
    @PostMapping("/{id}/comments")
    public String createComment(@PathVariable Long id,
                                @RequestBody Map<String, String> params,
                                @AuthenticationPrincipal UserDetails userdetails) {
        boardService.createComment(id, params.get("content"), userdetails.getUsername());
        return "배설댓글 반영 ㅋㅋ";
    }

    @GetMapping
    public List<BoardResponseDto> getAllBoards(@AuthenticationPrincipal UserDetails userdetails) {
        String username = (userdetails != null) ? userdetails.getUsername() : null;
        return boardService.getAllBoards(username);
    }

    @GetMapping("/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetails userdetails) {
        String username = (userdetails != null) ? userdetails.getUsername() : null;
        return boardService.getBoard(id, username);
    }


    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetails userdetails) {
        boardService.deleteBoard(id, userdetails.getUsername());

        return "삭제했다. ";
    }
    @PutMapping("/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody Map<String, String> params,
                             @AuthenticationPrincipal UserDetails userdetails) {

        return boardService.updateBoard(id,
                params.get("title"),
                params.get("content"),
                userdetails.getUsername());
    }
}
