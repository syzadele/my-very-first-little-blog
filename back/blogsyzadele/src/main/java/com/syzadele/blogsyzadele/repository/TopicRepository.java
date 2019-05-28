package com.syzadele.blogsyzadele.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syzadele.blogsyzadele.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
	
}
