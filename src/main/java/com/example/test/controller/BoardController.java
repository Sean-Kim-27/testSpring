package com.example.test.controller;

import com.example.test.entity.Board;
import com.example.test.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public Board createBoard(@RequestBody Map<String, String> params) {
        return boardService.createBoard(
                params.get("title"),
                params.get("content"),
                params.get("writer")
        );
    }

    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }
}
