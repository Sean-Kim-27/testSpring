package com.example.test.service;

import com.example.test.dto.BoardResponseDto;
import com.example.test.dto.CommentResponseDto;
import com.example.test.entity.*;
import com.example.test.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final CommentLikeRepository commentLikeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public BoardResponseDto createBoard(String title, String content, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("그런 사람 없다 회원가입부터 해 씨발 ㅋㅋ"));
        Board board = Board.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
        boardRepository.save(board);
        return new BoardResponseDto(board, false, new ArrayList<>());
    }
    //기존에는 List<Board>로 보드에 기입되어있는 모든 정보를 보냈음. 하지만 보안상 매우 좋지 않음
    //따라서 Dto에 BoardResponseDto 클래스를 만들어, 멤버값의 닉네임만 꺼내와서 포장해서 보내도록 함.
    public List<BoardResponseDto> getAllBoards(String username) {
        Member member = (username != null)
                ? memberRepository.findByUsername(username).orElse(null)
                : null;
        return boardRepository.findAll().stream()
                .map(board -> {
                    boolean isLiked = member != null && boardLikeRepository.existsByBoardAndMember(board, member);
                    return new BoardResponseDto(board, isLiked, new ArrayList<>());
                }).collect(Collectors.toList());
    }

    public BoardResponseDto getBoard(Long id, String username) {
        Member member = (username != null)
                ? memberRepository.findByUsername(username).orElse(null)
                : null;
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글 없는데?"));
        boolean isLiked = member != null && boardLikeRepository.existsByBoardAndMember(board, member);
        List<CommentResponseDto> commentDtos = board.getComments().stream()
                .map(comment -> {
                    boolean isCommentLiked = member != null && commentLikeRepository.existsByCommentAndMember(comment, member);
                    return new CommentResponseDto(comment, isCommentLiked);
                }).collect(Collectors.toList());
        return new BoardResponseDto(board, isLiked, commentDtos);
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
        Member member = (username != null)
                ? memberRepository.findByUsername(username).orElse(null)
                : null;
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글이 없는데 수정 어케함?"));
        boolean isLiked = member != null && boardLikeRepository.existsByBoardAndMember(board, member);
        if (!board.getMember().getUsername().equals(username)) {
            throw new RuntimeException("니꺼나 수정해라 다른 글에 똥싸지르지 말고");
        }
        board.update(title, content);
        List<CommentResponseDto> commentDtos = board.getComments().stream()
                .map(comment -> {
                    boolean isCommentLiked = member != null && commentLikeRepository.existsByCommentAndMember(comment, member);
                    return new CommentResponseDto(comment, isCommentLiked);
                }).collect(Collectors.toList());

        return new BoardResponseDto(board, isLiked, commentDtos);
    }
    @Transactional
    public void toggleCommentLike(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글 없음"));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사람 없음"));

        if (commentLikeRepository.existsByCommentAndMember(comment, member)) {
            commentLikeRepository.deleteByCommentAndMember(comment, member);
        } else {
            commentLikeRepository.save(CommentLike.builder().comment(comment).member(member).build());
        }
    }

    @Transactional
    public void toggleLike(Long boardId, String username) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("글 없다."));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사람 없다."));

        if (boardLikeRepository.existsByBoardAndMember(board, member)) {
            boardLikeRepository.deleteByBoardAndMember(board, member);
        } else {
            boardLikeRepository.save(BoardLike.builder().board(board).member(member).build());
        }
    }

    @Transactional
    public void createComment(Long boardId, String content, String username) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("글 없다."));
        Member member = memberRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("사람 없다."));

        Comment comment = Comment.builder()
                .board(board)
                .member(member)
                .content(content)
                .build();
        commentRepository.save(comment);
    }
}

