package com.syzadele.blogsyzadele.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.syzadele.blogsyzadele.model.Post;
import com.syzadele.blogsyzadele.model.Topic;
import com.syzadele.blogsyzadele.model.TopicCoverPhoto;
import com.syzadele.blogsyzadele.repository.PostRepository;
import com.syzadele.blogsyzadele.repository.TopicRepository;
import com.syzadele.blogsyzadele.service.TopicCoverPhotoService;


@RestController
@RequestMapping("/TopicController")
public class TopicController {
	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private TopicCoverPhotoService tcps;
	
	@RequestMapping(method = RequestMethod.POST, value = "/CreateOne")
	public Topic create(@RequestParam(value="name") String name,
			@RequestParam(value="presentation") String presentation,
			@RequestParam(value="files", required=false) MultipartFile[] files){
		
		Topic t = new Topic(name, presentation);
		
		if (files != null) {
			topicRepository.save(t);
			List<TopicCoverPhoto> tcp = tcps.storeMultipleFile(files, t);
			t.setCoverPhotos(tcp);
			return topicRepository.save(t);
		} else {
			return topicRepository.save(t);
		}
		
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOne")
	public String delete(@RequestParam(value="id") int id) {
		if (topicRepository.existsById(id)) {
			topicRepository.deleteById(id);
			return "delete successful.";
		}
		return "Topic not found.";
	}
	
	@Transactional 
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOneByName")
	public void deleteOneByName(@RequestParam(value="name") String name) {
		if (topicRepository.findByName(name) != null) {
			topicRepository.deleteByName(name);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/UpdateOne")
	public Topic update(Topic t) {
		if (topicRepository.existsById(t.getId())) {
			Topic originT = topicRepository.findById(t.getId()).get();
			originT.setName(t.getName());
			originT.setPresentation(t.getPresentation());
			return topicRepository.saveAndFlush(originT);
		}
		return null;
	}
	
	// In fact you can only update presentation here
	@RequestMapping(method = RequestMethod.POST, value = "UpdateOneByName")
	public Topic updateOneByName(Topic t, @RequestParam(value="name") String name) {
		if (topicRepository.existsByName(name)) {
			Topic originT = topicRepository.findByName(name);
			originT.setPresentation(t.getPresentation());
			return topicRepository.save(originT);
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetOne") 
	public Topic getTopic(@RequestParam(value="id") int id) {
		Optional<Topic> ot = topicRepository.findById(id);
		if (ot.isPresent()) {
			return ot.get();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/GetOneByName")
	public Topic getTopicByName(@RequestParam(value="name") String name) {
		if (topicRepository.existsByName(name)) {
			return topicRepository.findByName(name);
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAll")
	public List<Topic> getAllTopic(){
		return topicRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddPost")
	public Topic addPost(@RequestParam(value="postID") int postID, @RequestParam(value="topicID") int topicID) {
		if (topicRepository.existsById(topicID) && postRepository.existsById(postID)) {
			Topic t = topicRepository.findById(topicID).get();
			Post p = postRepository.findById(postID).get();
			p.setTopic(t);
			t.addPost(p);
			return topicRepository.saveAndFlush(t);
		} else {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddPostByNameAndTitle")
	public Topic addPostByNames(@RequestParam(value = "title") String title, @RequestParam(value = "name") String name) {
		if (topicRepository.existsByName(name) && postRepository.existsByTitle(title)) {
			Topic t = topicRepository.findByName(name);
			Post p = postRepository.findByTitle(title);
			p.setTopic(t);
			t.addPost(p);
			return topicRepository.saveAndFlush(t);
		} else {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAllPost")
	public List<Post> getAllPost(@RequestParam(value="topicID") int topicID) {
		Optional<Topic> ot = topicRepository.findById(topicID);
		if (ot.isPresent()) {
			Topic t = ot.get();
			return t.getPosts();
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAllPostByName")
	public List<Post> getAllPostByName(@RequestParam(value="name") String name) {
		if (topicRepository.existsByName(name)) {
			Topic t = topicRepository.findByName(name);
			return t.getPosts();
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddCoverPhoto")
	public Topic addCoverPhoto(@RequestParam(value="file")  MultipartFile file, @RequestParam(value="id") int id) {
		if (topicRepository.existsById(id)) {
			
			Topic t = topicRepository.findById(id).get();
			t.addCoverPhotos(tcps.storeFile(file, t));
			return topicRepository.save(t);
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddCoverPhotoByName")
	public Topic addCoverPhotoByName(@RequestParam(value="file") MultipartFile file, @RequestParam(value="name") String name) {
		if (topicRepository.existsByName(name)) {
			Topic t = topicRepository.findByName(name);
			t.addCoverPhotos(tcps.storeFile(file, t));
			return topicRepository.save(t);
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddCoverPhotos")
	public Topic addCoverPhotos(@RequestParam(value="files")  MultipartFile[] files, @RequestParam(value="id") int id) {
		if (topicRepository.existsById(id)) {
			
			Topic t = topicRepository.findById(id).get();
			List<TopicCoverPhoto> tcp = tcps.storeMultipleFile(files, t);
			t.addMCoverPhotos(tcp);
			return topicRepository.saveAndFlush(t);
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddCoverPhotosByName")
	public Topic addCoverPhotosByName(@RequestParam(value="files", required=true) MultipartFile[] files, @RequestParam(value="name") String name) {
		if (topicRepository.existsByName(name)) {
			Topic t = topicRepository.findByName(name);
			List<TopicCoverPhoto> tcp = tcps.storeMultipleFile(files, t);
			t.addMCoverPhotos(tcp);
			return topicRepository.saveAndFlush(t);
		}
		return null;
 	}
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteCoverPhoto")
	public Topic deleteCoverPhoto(@RequestParam(value="photoId") String photoID, @RequestParam(value="id") int id) {
		if (topicRepository.existsById(id)) {
			boolean result = tcps.deleteFile(photoID);
			if (result == true) {
				return topicRepository.findById(id).get();
			}
		} 
		return null;
	}
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteCoverPhotoByNames")
	public Topic deleteCoverPhotosByNames(@RequestParam(value="fileName") String fileName, @RequestParam(value="name") String name) {
		if (topicRepository.existsByName(name)) {
			boolean result = tcps.deleteFileByFileName(fileName);
			if (result == true) {
				return topicRepository.findByName(name);
			}
		}
		return null;
	}
	
	

	

}
