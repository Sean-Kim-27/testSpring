package com.example.test.service;

import com.example.test.entity.Member;
import com.example.test.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Transactional
    public Board createBoard(String title, String content, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("그런 사람 없다 회원가입부터 해 씨발 ㅋㅋ"));
        Board board = Board.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
        return boardRepository.save(board);
    }
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
}
