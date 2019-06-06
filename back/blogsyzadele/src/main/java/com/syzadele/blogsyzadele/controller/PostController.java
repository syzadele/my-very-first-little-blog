package com.syzadele.blogsyzadele.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.syzadele.blogsyzadele.model.Post;
import com.syzadele.blogsyzadele.model.Topic;
import com.syzadele.blogsyzadele.repository.PostRepository;
import com.syzadele.blogsyzadele.repository.TopicRepository;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/PostController")
public class PostController {
	@Autowired
	PostRepository postRepository;
	@Autowired
	TopicRepository topicRepository;
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/CreateOne")
	public Post create(@RequestParam(value = "title", defaultValue = "untitle post") String title,
			@RequestParam(value = "topicID", defaultValue = "-1") Integer topicID,
			@RequestParam(value = "posteDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Date posteDate,
			@RequestParam(value = "auther", required = false) String auther,
			@RequestParam(value = "content", required = false) String content) throws ParseException {
		
		if (topicID != null) {
			if (topicRepository.existsById(topicID)) {
				Optional<Topic> ot = topicRepository.findById(topicID);
				Topic t = ot.get();
				Post p = new Post(title, t, posteDate, auther, content);
				t.addPost(p);
				topicRepository.save(t);
				return p;
			} else {
				return null;
			}
		} else {
			Post p = new Post(title, null, posteDate, auther, content);
			postRepository.save(p);
			return p;
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/CreateOneByTopicName")
	public Post createByTopicName(@RequestParam(value = "title", defaultValue = "untitle post") String title,
			@RequestParam(value = "name", defaultValue = "-1") String topicName,
			@RequestParam(value = "posteDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Date posteDate,
			@RequestParam(value = "auther", required = false) String auther,
			@RequestParam(value = "content", required = false) String content) {
		
		if (topicName != null) {
			if (topicRepository.existsByName(topicName)) {
				Topic t = topicRepository.findByName(topicName);
				Post p = new Post(title, t, posteDate, auther, content);
				t.addPost(p);
				topicRepository.save(t);
				return p;
			} else {
				return null;
			}
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
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOneByTitle")
	public String deleteOneByTitle(@RequestParam(value = "title") String title) {
		
		if (postRepository.existsByTitle(title)) {
			postRepository.deleteByTitle(title);
			return "Delete sucessful";
		} else {
			return "Post unexiste!";
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/UpdateOne")
	public Post updateOne(Post p, @RequestParam(value="topicID", required=false) Integer topicID) {
		
		if (postRepository.existsById(p.getId())) {
			if(topicID == null) {
				Post originP = postRepository.findById(p.getId()).get();
				p.setTopic(originP.getTopic());
				return postRepository.saveAndFlush(p);
				
			} else if (topicRepository.findById(topicID).isPresent()) {
				p.setTopic(topicRepository.findById(topicID).get());
				return postRepository.saveAndFlush(p);
			} else {
				System.out.println("topic not found");
			}
		} else {
			System.out.println("post not found");
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/UpdateOneByTitleAndName")
	public Post updateOneByTitleAndName(Post p, @RequestParam(value="title", required=false) String title, @RequestParam(value="name", required=false) String name) {
		
		if (postRepository.existsByTitle(title)) {
			if(name == null) {
				Post originP = postRepository.findByTitle(title);
				originP.setAuther(p.getAuther());
				originP.setContent(p.getContent());
				originP.setPosteDate(p.getPosteDate());
				return postRepository.saveAndFlush(originP);
				
			} else if (topicRepository.existsByName(name)) {
				Post originP = postRepository.findByTitle(title);
				Topic t = topicRepository.findByName(name);
				originP.setAuther(p.getAuther());
				originP.setContent(p.getContent());
				originP.setPosteDate(p.getPosteDate());
				originP.setTopic(t);
				return postRepository.saveAndFlush(originP);
				
			} else {
				System.out.println("Topic not found");
			}
			
		} else {
			System.out.println("Post not found");
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetOne")
	public Post getOne(@RequestParam(value = "id") int id) {
		Optional<Post> op = postRepository.findById(id);
		if (op.isPresent()) {
			Post p = op.get();
			return p;
		}
		System.out.println("Post not found");
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetOneByTitle")
	public Post getOneByTitle(@RequestParam(value = "title") String title) {
		if (postRepository.existsByTitle(title)) {
			Post p = postRepository.findByTitle(title);
			return p;
		}
		System.out.println("Post not found");
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAll")
	public List<Post> getAll() {
		return postRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddReadTimes")
	public Post addReadTimes(@RequestParam(value = "id") int id) {
		Optional<Post> op = postRepository.findById(id);
		if (op.isPresent()) {
			Post p = op.get();
			p.setReadTimes(p.getReadTimes() + 1);
			return postRepository.saveAndFlush(p);
		}
		System.out.println("Post not found");
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddReadTimesByTitle")
	public Post addReadTimesByTitle(@RequestParam(value = "title") String title) {
		if (postRepository.existsByTitle(title)) {
			Post p = postRepository.findByTitle(title);
			p.setReadTimes(p.getReadTimes() + 1);
			return postRepository.saveAndFlush(p);
		}
		System.out.println("Post not found");
		return null;
	}

}
