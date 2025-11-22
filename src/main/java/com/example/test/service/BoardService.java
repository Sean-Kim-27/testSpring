package com.example.test.service;

import com.example.test.entity.Board;
import com.example.test.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board createBoard(String title, String content, String writer) {
        Board board = Board.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();
        return boardRepository.save(board);
    }
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
}
