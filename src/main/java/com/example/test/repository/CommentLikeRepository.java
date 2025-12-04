package com.example.test.repository;

import com.example.test.entity.CommentLike;
import com.example.test.entity.Comment;
import com.example.test.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndMember(Comment comment, Member member);
    void deleteByCommentAndMember(Comment comment, Member member);
}
