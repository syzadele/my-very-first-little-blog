package com.syzadele.blogsyzadele.controller;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.syzadele.blogsyzadele.model.Post;
import com.syzadele.blogsyzadele.repository.PostRepository;

@RestController
@RequestMapping("/PostController")
public class PostController {
	@Autowired
	PostRepository postRepository;
	
	@RequestMapping("/Create")
	public Post create(@RequestParam(value = "title", defaultValue = "1") String title,
			@RequestParam(value = "posteDate") @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Date posteDate,
			@RequestParam(value = "auther") String auther) {
		Post p = new Post(title, posteDate, auther);
		postRepository.save(p);
		return p;
	}

}
