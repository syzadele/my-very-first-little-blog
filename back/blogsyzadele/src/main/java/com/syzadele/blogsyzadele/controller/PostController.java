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
	public Post create(@RequestParam(value = "title", defaultValue = "untitle post") String title,
			@RequestParam(value = "topicID", defaultValue = "-1") int topicID,
			@RequestParam(value = "posteDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Date posteDate,
			@RequestParam(value = "auther", required = false) String auther,
			@RequestParam(value = "content", required = false) String content) {
		
		if (topicRepository.existsById(topicID)) {
			Optional<Topic> ot = topicRepository.findById(topicID);
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
		
		if (postRepository.existsById(id)) {
			postRepository.deleteById(id);
			return "Delete sucessful";
		} else {
			return "Post unexiste!";
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/UpdateOne")
	public String updateOne(Post p, @RequestParam(value="topicID", required=false) Integer topicID) {
		
		if (postRepository.existsById(p.getId())) {
			if(topicID == null) {
				Post originP = postRepository.findById(p.getId()).get();
				p.setTopic(originP.getTopic());
				postRepository.saveAndFlush(p);
				return "Update sucessful";
				
			} else if (topicRepository.findById(topicID).isPresent()) {
				p.setTopic(topicRepository.findById(topicID).get());
				postRepository.saveAndFlush(p);
				
				return "Update sucessful";
				
			} else {
				return "Topic not found";
			}
			
		} else {
			return "Post not found";
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
			postRepository.saveAndFlush(p);
		}
	}

}
