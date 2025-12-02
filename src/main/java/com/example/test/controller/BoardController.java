package com.example.test.controller;

import com.example.test.dto.BoardResponseDto;
import com.example.test.entity.Board;
import com.example.test.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@CrossOrigin(origins ={"http://localhost:3000", "https://test-react-delta-woad.vercel.app"})
public class BoardController {
    private final BoardService boardService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public BoardResponseDto createBoard(@RequestBody Map<String, String> params,
                                        @AuthenticationPrincipal UserDetails userdetails) {

        BoardResponseDto newBoard = boardService.createBoard(
                params.get("title"),
                params.get("content"),
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

    @GetMapping
    public List<BoardResponseDto> getAllBoards() {
        return boardService.getAllBoards();
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
