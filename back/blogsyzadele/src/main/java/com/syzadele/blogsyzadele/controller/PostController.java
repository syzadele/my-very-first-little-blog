package com.syzadele.blogsyzadele.controller;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.syzadele.blogsyzadele.model.Post;
import com.syzadele.blogsyzadele.repository.PostRepository;

@RestController
@RequestMapping("/PostController")
public class PostController {
	@Autowired
	PostRepository postRepository;
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/CreateOne")
	public Post create(@RequestParam(value = "title", defaultValue = "1") String title,
			@RequestParam(value = "posteDate") @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Date posteDate,
			@RequestParam(value = "auther") String auther,
			@RequestParam(value = "content") String content,
			@RequestParam(value = "readTimes", defaultValue = "0") int readTimes) {
		Post p = new Post(title, posteDate, auther, content, readTimes);
		postRepository.save(p);
		return p;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOne")
	public String deleteOne(@RequestParam(value = "id") int id) {
		if (postRepository.existsById(id)) {
			postRepository.deleteById(id);
			return "Delete sucessful";
		} else {
			return "Post unexiste!";
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/UpdateOne")
	public void updateOne(Post p) {
		if (postRepository.existsById(p.getId())) {
			postRepository.save(p);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetOne")
	public Optional<Post> getOne(@RequestParam(value = "id") int id) {
		return postRepository.findById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAll")
	public List<Post> getAll() {
		return postRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddReadTimes")
	public void addReadTimes(@RequestParam(value = "id") int id) {
		if (postRepository.existsById(id)) {
			Optional<Post> op = postRepository.findById(id);
			Post p = op.get();
			p.setReadTimes(p.getReadTimes() + 1);
			postRepository.save(p);
		}
	}

}
