package com.syzadele.blogsyzadele.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syzadele.blogsyzadele.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
