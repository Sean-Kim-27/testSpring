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

    @Transactional
    public void deleteBoard(Long id, String username) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("그런 글 없다."));

        if (!board.getMember().getUsername().equals(username)) {
            throw new RuntimeException("남의 글 지울라 하네 ㅋㅋ 뒤질라고");
        }
        boardRepository.delete(board);
    }
    @Transactional
    public Board updateBoard(Long id, String title, String content, String username) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글이 없는데 수정 어케함?"));
        if (!board.getMember().getUsername().equals(username)) {
            throw new RuntimeException("니꺼나 수정해라 다른 글에 똥싸지르지 말고");
        }
        board.update(title, content);
        return board;
    }
}

