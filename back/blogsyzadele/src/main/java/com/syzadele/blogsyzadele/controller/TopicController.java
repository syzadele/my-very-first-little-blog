package com.syzadele.blogsyzadele.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syzadele.blogsyzadele.model.Topic;
import com.syzadele.blogsyzadele.repository.TopicRepository;

@RestController
@RequestMapping("/TopicController")
public class TopicController {
	@Autowired
	TopicRepository topicRepository;
	
	@RequestMapping(method = RequestMethod.POST, value = "/CreateOne")
	public Topic create(@RequestParam(value="name") String name,
			@RequestParam(value="presentation") String presentation,
			@RequestParam(value="coverPhotos") ArrayList<String> coverPhotos){
		Topic t = new Topic(name, presentation, coverPhotos);
		return topicRepository.save(t);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOne")
	public void delete(@RequestParam(value="id") int id) {
		topicRepository.deleteById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/UpdateOne")
	public void update(Topic t) {
		topicRepository.save(t);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetOne") 
	public Optional<Topic> get(@RequestParam(value="id") int id) {
		return topicRepository.findById(id));
	}
	
	
	
}
