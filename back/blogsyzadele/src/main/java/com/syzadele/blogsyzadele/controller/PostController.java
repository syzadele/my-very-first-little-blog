package com.syzadele.blogsyzadele.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.syzadele.blogsyzadele.model.Post;
import com.syzadele.blogsyzadele.model.Topic;
import com.syzadele.blogsyzadele.repository.PostRepository;
import com.syzadele.blogsyzadele.repository.TopicRepository;
import com.syzadele.blogsyzadele.service.ThumbnailService;
import com.syzadele.blogsyzadele.service.UploadService;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/PostController")
public class PostController {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private UploadService uploadService;
	@Autowired
	private ThumbnailService thumbnailService;
	
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/CreateOne")
	public Post create(@RequestParam(value = "title") String title,
			@RequestParam(value = "topicID", required = false) Integer topicID,
			@RequestParam(value = "posteDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Date posteDate,
			@RequestParam(value = "auther", required = false) String auther,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value="coverPhotos", required=false) MultipartFile[] files) throws ParseException {
		if (!postRepository.existsByTitle(title)) {
			String uploadPath = "images/postImages/";
			String realUploadPath = getClass().getClassLoader().getResource(uploadPath).getPath();
			File theDir = new File(realUploadPath + title);
			System.out.println(theDir);

			// if the directory does not exist, create it
			if (!theDir.exists()) {
			    System.out.println("creating directory: " + theDir.getName());
			    theDir.mkdir();
			} else {
				System.out.println("dir already existe");
			}
			if (topicID != null) {
				if (topicRepository.existsById(topicID)) {
					Optional<Topic> ot = topicRepository.findById(topicID);
					Topic t = ot.get();
					Post p = new Post(title, t, posteDate, auther, content);
					t.addPost(p);
					topicRepository.save(t);
					
					if (files != null) {
						Post pp = postRepository.findByTitle(title);
						int id = pp.getId();
						addCoverPhotos(files, id);
					}
					return p;
				} else {
					return null;
				}
			} else {
				Post p = new Post(title, null, posteDate, auther, content);
				postRepository.save(p);
				if (files != null) {
					Post pp = postRepository.findByTitle(title);
					int id = pp.getId();
					addCoverPhotos(files, id);
				}
				return p;
			}
		} else {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOne")
	public String deleteOne(@RequestParam(value = "id") int id) throws IOException {
		
		if (postRepository.existsById(id)) {
			Post p = postRepository.findById(id).get();
			String uploadPath = "images/postImages/";
			String realUploadPath = getClass().getClassLoader().getResource(uploadPath).getPath();
			
			File theDir = new File(realUploadPath + p.getTitle() + "/");
			if (theDir.exists()) {
				File[] contents = theDir.listFiles();
				if (contents != null) {
			        for (File f : contents) {
			            f.delete();
			        }
			    }
				theDir.delete();
			} else {
				System.out.println("folder not found");
			}
			postRepository.deleteById(id);
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddCoverPhoto")
	public Map<String, String> addCoverPhoto(@RequestParam(value="file")  MultipartFile file, @RequestParam(value="id") int id) {
		if (postRepository.existsById(id)) {
			
			Post p = postRepository.findById(id).get();
			
			String postTitle = p.getTitle();
			String uploadPath = "images/postImages/" + postTitle + "/";
			String realUploadPath = getClass().getClassLoader().getResource(uploadPath).getPath();
			
			String imageUrl = uploadService.uploadImage(file, uploadPath, postTitle, realUploadPath);
	        String thumImageUrl = thumbnailService.thumbnail(file, uploadPath,postTitle, realUploadPath);
			
	        Map <String, String> imageURLSMap = new HashMap<String, String>();
	        imageURLSMap.put(file.getOriginalFilename(), imageUrl);
	        imageURLSMap.put("thum_"+file.getOriginalFilename(), thumImageUrl);
	        return imageURLSMap;
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddCoverPhotos")
	public List<Map<String,String>>addCoverPhotos(@RequestParam(value="files")  MultipartFile[] files, @RequestParam(value="id") int id) {
		if (postRepository.existsById(id)) {
	
			return	Arrays.asList(files)
		            .stream()
		            .map(file -> addCoverPhoto(file, id))
		            .collect(Collectors.toList());

		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteCoverPhoto")
	public boolean deleteCoverPhoto(@RequestParam(value="photoName") String photoName, @RequestParam(value="id") int id) {
		if (postRepository.existsById(id)) {
			Post p = postRepository.findById(id).get();
			String postTitle = p.getTitle();
			String uploadPath = "images/postImages/" + postTitle + "/";
			String realUploadPath = getClass().getClassLoader().getResource(uploadPath).getPath();
			
			String filePath = realUploadPath + postTitle + "_" + photoName;
			String thumFilePath = realUploadPath + postTitle + "_" + "thum_" + photoName;
			System.out.println(filePath);
			System.out.println(thumFilePath);
			
			if (new File(filePath + ".jpg").exists()) {
				File fileToDelete = new File(filePath + ".jpg");
				File thumFileToDelete = new File(thumFilePath + ".jpg");
				boolean result1 = fileToDelete.delete();
			    boolean result2 = thumFileToDelete.delete();
			    return result1 & result2;
			} else {
				File fileToDelete = new File(filePath + ".PNG");
				File thumFileToDelete = new File(thumFilePath + ".PNG");
				boolean result1 = fileToDelete.delete();
			    boolean result2 = thumFileToDelete.delete();
			    return result1 & result2;
			}
		} 
		return false;
	}
	
	
	

}
