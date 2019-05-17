package com.syzadele.blogsyzadele.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syzadele.blogsyzadele.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
	@Query("SELECT * FROM Post p where p.topic_id = :topicID") 
	Optional<Post> findPostByTopicId(@Param("topicID") Integer topicID);
	Optional<Post> findPostByTopicId(Integer topicID);
}
