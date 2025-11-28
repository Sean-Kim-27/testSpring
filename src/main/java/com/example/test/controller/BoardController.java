package com.example.test.controller;

import com.example.test.entity.Board;
import com.example.test.service.BoardService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public Board createBoard(@RequestBody Map<String, String> params,
                             @AuthenticationPrincipal UserDetails userdetails) {

        String username = userdetails.getUsername();
        return boardService.createBoard(
                params.get("title"),
                params.get("content"),
                username
        );
    }

    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetails userdetails) {
        boardService.deleteBoard(id, userdetails.getUsername());

        return "삭제했다. ";
    }
}
