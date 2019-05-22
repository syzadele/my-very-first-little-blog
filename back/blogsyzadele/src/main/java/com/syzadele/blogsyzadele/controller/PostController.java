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
import com.syzadele.blogsyzadele.model.Topic;
import com.syzadele.blogsyzadele.repository.PostRepository;
import com.syzadele.blogsyzadele.repository.TopicRepository;

@RestController
@RequestMapping("/PostController")
public class PostController {
	@Autowired
	PostRepository postRepository;
	@Autowired
	TopicRepository topicRepository;
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/CreateOne")
	public Post create(@RequestParam(value = "title", defaultValue = "1") String title,
			@RequestParam(value = "topicID", defaultValue = "-1") int topicID,
			@RequestParam(value = "posteDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Date posteDate,
			@RequestParam(value = "auther") String auther,
			@RequestParam(value = "content") String content) {
		Optional<Topic> ot = topicRepository.findById(topicID);
		if (ot.isPresent()) {
			Topic t = ot.get();
			Post p = new Post(title, t, posteDate, auther, content);
			t.addPost(p);
			topicRepository.save(t);
			return p;
		} else {
			Post p = new Post(title, null, posteDate, auther, content);
			postRepository.save(p);
			return p;
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOne")
	public String deleteOne(@RequestParam(value = "id") int id) {
		Optional<Post> op = postRepository.findById(id);
		if (op.isPresent()) {
			postRepository.deleteById(id);
			return "Delete sucessful";
		} else {
			return "Post unexiste!";
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/UpdateOne")
	public void updateOne(Post p) {
		Optional<Post> op = postRepository.findById(p.getId());
		if (op.isPresent()) {
			postRepository.save(op.get());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetOne")
	public Post getOne(@RequestParam(value = "id") int id) {
		Optional<Post> op = postRepository.findById(id);
		if (op.isPresent()) {
			Post p = op.get();
			return p;
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAll")
	public List<Post> getAll() {
		return postRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddReadTimes")
	public void addReadTimes(@RequestParam(value = "id") int id) {
		Optional<Post> op = postRepository.findById(id);
		if (op.isPresent()) {
			Post p = op.get();
			p.setReadTimes(p.getReadTimes() + 1);
			postRepository.save(p);
		}
	}

}
