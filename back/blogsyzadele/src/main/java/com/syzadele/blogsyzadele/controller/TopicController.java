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
			@RequestParam(value="coverPhotos", required=false) List<String> coverPhotos,
			@RequestParam(value="posts", required=false) List<Post> posts){
		Topic t = new Topic(name, presentation, coverPhotos, posts);
		return topicRepository.save(t);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOne")
	public void delete(@RequestParam(value="id") int id) {
		topicRepository.deleteById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/UpdateOne")
	public void update(Topic t) {
		if (topicRepository.existsById(t.getId())) {
			topicRepository.save(t);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetOne") 
	public Optional<Topic> get(@RequestParam(value="id") int id) {
		return topicRepository.findById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAll")
	public List<Topic> getAll(){
		return topicRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddPost")
	public void addPost(Post p, @RequestParam(value="topicID") Integer topicID) {
		if (topicRepository.existsById(topicID)) {
			Optional<Topic> ot = topicRepository.findById(topicID);
			Topic t = ot.get();
			t.addPost(p);
			p.setTopic(t);
			postRepository.save(p);
			topicRepository.save(t);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAllPost")
	public List<Post> getAllPost(@RequestParam(value="topicID") Integer topicID) {
		Optional<Topic> ot = topicRepository.findById(topicID);
		Topic t = ot.get();
		List<Post> posts= t.getPosts();

		return posts;
	}
	

}
