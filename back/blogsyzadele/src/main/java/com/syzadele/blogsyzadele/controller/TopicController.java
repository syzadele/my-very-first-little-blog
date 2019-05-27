package com.syzadele.blogsyzadele.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syzadele.blogsyzadele.model.Post;
import com.syzadele.blogsyzadele.model.Topic;
import com.syzadele.blogsyzadele.repository.PostRepository;
import com.syzadele.blogsyzadele.repository.TopicRepository;


@RestController
@RequestMapping("/TopicController")
public class TopicController {
	@Autowired
	TopicRepository topicRepository;
	@Autowired
	PostRepository postRepository;
	
	@RequestMapping(method = RequestMethod.POST, value = "/CreateOne")
	public Topic create(@RequestParam(value="name") String name,
			@RequestParam(value="presentation") String presentation,
			@RequestParam(value="coverPhotos", required=false) List<String> coverPhotos){
		
		Topic t = new Topic(name, presentation, coverPhotos);
		return topicRepository.save(t);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOne")
	public String delete(@RequestParam(value="id") int id) {
		if (topicRepository.existsById(id)) {
			topicRepository.deleteById(id);
			return "delete successful.";
		}
		return "Topic not found.";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/UpdateOne")
	public void update(Topic t) {
		if (topicRepository.existsById(t.getId())) {
			List<Post> originPosts = topicRepository.findById(t.getId()).get().getPosts();
			t.setPosts(originPosts);
			topicRepository.saveAndFlush(t);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetOne") 
	public Topic getTopic(@RequestParam(value="id") int id) {
		Optional<Topic> ot = topicRepository.findById(id);
		if (ot.isPresent()) {
			return ot.get();
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAll")
	public List<Topic> getAllTopic(){
		return topicRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddPost")
	public String addPost(@RequestParam(value="postID") int postID, @RequestParam(value="topicID") int topicID) {
		if (topicRepository.existsById(topicID) && postRepository.existsById(postID)) {
			Topic t = topicRepository.findById(topicID).get();
			Post p = postRepository.findById(postID).get();
			p.setTopic(t);
			t.addPost(p);
			topicRepository.saveAndFlush(t);
			return "Post Added.";
		} else {
			return "Topic or post not found.";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAllPost")
	public List<Post> getAllPost(@RequestParam(value="topicID") int topicID) {
		Optional<Topic> ot = topicRepository.findById(topicID);
		if (ot.isPresent()) {
			Topic t = ot.get();
			List<Post> posts= t.getPosts();
			return posts;
		}
		return null;
	}
	

}
