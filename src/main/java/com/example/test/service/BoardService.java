package com.example.test.service;

import com.example.test.dto.BoardResponseDto;
import com.example.test.entity.Member;
import com.example.test.repository.MemberRepository;
import com.example.test.entity.Board;
import com.example.test.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardResponseDto createBoard(String title, String content, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("그런 사람 없다 회원가입부터 해 씨발 ㅋㅋ"));
        Board board = Board.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
        return new BoardResponseDto(board);
    }
    //기존에는 List<Board>로 보드에 기입되어있는 모든 정보를 보냈음. 하지만 보안상 매우 좋지 않음
    //따라서 Dto에 BoardResponseDto 클래스를 만들어, 멤버값의 닉네임만 꺼내와서 포장해서 보내도록 함.
    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new).collect(Collectors.toList());
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
    public BoardResponseDto updateBoard(Long id, String title, String content, String username) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글이 없는데 수정 어케함?"));
        if (!board.getMember().getUsername().equals(username)) {
            throw new RuntimeException("니꺼나 수정해라 다른 글에 똥싸지르지 말고");
        }
        board.update(title, content);
        return new BoardResponseDto(board);
    }
}

