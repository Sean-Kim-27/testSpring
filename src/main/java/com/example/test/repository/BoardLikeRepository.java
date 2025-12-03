package com.example.test.repository;

import com.example.test.entity.Board;
import com.example.test.entity.BoardLike;
import com.example.test.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsByBoardAndMember(Board board, Member member);

    void deleteByBoardAndMember(Board board, Member member);
}
