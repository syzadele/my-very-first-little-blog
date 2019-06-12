package com.syzadele.blogsyzadele.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/TopicController")
public class TopicController {
	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private PostRepository postRepository;
	/*
	@Autowired
	private TopicCoverPhotoService tcps;
	*/
	@Autowired
	private UploadService uploadService;
	@Autowired
	private ThumbnailService thumbnailService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/CreateOne")
	public Topic create(@RequestParam(value="name") String name,
			@RequestParam(value="presentation") String presentation,
			@RequestParam(value="coverPhotos", required=false) MultipartFile[] files){
		
		
		if (!topicRepository.existsByName(name)) {
			Topic t = new Topic(name, presentation);
			String uploadPath = "images/topicImages/";
			
			try {
				String realUploadPath = getClass().getClassLoader().getResource(uploadPath).getPath();
				File theDir = new File(realUploadPath + name);
				System.out.println(theDir);
	
				// if the directory does not exist, create it
				if (!theDir.exists()) {
				    System.out.println("creating directory: " + theDir.getName());
				    theDir.mkdir();
				} else {
					System.out.println("dir already existe");
				}
				if (files != null) {
					topicRepository.save(t);
					Topic tt = topicRepository.findByName(name);
					int id = tt.getId();
					addCoverPhotos(files, id);
					/*
					List<TopicCoverPhoto> tcp = tcps.storeMultipleFile(files, t);
					t.setCoverPhotos(tcp);
					*/
					return t;
				} else {
					return topicRepository.save(t);
				}
			} catch (Exception e) {
				System.out.println("topicImages folder not found");
				return null;
			}
		} else {
			return null;
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteOne")
	public String delete(@RequestParam(value="id") int id) {
		if (topicRepository.existsById(id)) {
			Topic t = topicRepository.findById(id).get();
			String uploadPath = "images/topicImages/";
			try {
				String realUploadPath = getClass().getClassLoader().getResource(uploadPath).getPath();
				
				File theDir = new File(realUploadPath + t.getName() + "/");
				if (theDir.exists()) {
					File[] contents = theDir.listFiles();
					if (contents != null) {
				        for (File f : contents) {
				            f.delete();
				        }
				    }
					theDir.delete();
					topicRepository.deleteById(id);
					return "delete successful.";
				} else {
					return "topic photo folder not found.";
				}
			} catch (Exception e) {
				return "topicImages folder not found";
			}
		}
		return "Topic not found.";
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
	public Map<String, String> addCoverPhoto(@RequestParam(value="file")  MultipartFile file, @RequestParam(value="id") int id) {
		if (topicRepository.existsById(id)) {
			
			Topic t = topicRepository.findById(id).get();
			
			String topicName = t.getName();
			String uploadPath = "images/topicImages/" + topicName + "/";
			try {
				String realUploadPath = getClass().getClassLoader().getResource(uploadPath).getPath();
				String imageUrl = uploadService.uploadImage(file, uploadPath, topicName, realUploadPath);
		        String thumImageUrl = thumbnailService.thumbnail(file, uploadPath,topicName, realUploadPath);
			
		        Map <String, String> imageURLSMap = new HashMap<String, String>();
		        imageURLSMap.put(file.getOriginalFilename(), imageUrl);
		        imageURLSMap.put("thum_"+file.getOriginalFilename(), thumImageUrl);
		        return imageURLSMap;
			} catch (Exception e) {
				System.out.println("Some folder is missing or the path is incorrect.");
				return null;
			}
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/AddCoverPhotos")
	public List<Map<String,String>>addCoverPhotos(@RequestParam(value="files")  MultipartFile[] files, @RequestParam(value="id") int id) {
		if (topicRepository.existsById(id)) {
	
			return	Arrays.asList(files)
		            .stream()
		            .map(file -> addCoverPhoto(file, id))
		            .collect(Collectors.toList());

		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/DeleteCoverPhoto")
	public String deleteCoverPhoto(@RequestParam(value="photoName") String photoName, @RequestParam(value="id") int id) {
		if (topicRepository.existsById(id)) {
			Topic t = topicRepository.findById(id).get();
			String topicName = t.getName();
			String downloadPath = "images/topicImages/";
			
			try {
				String realDownLoadPath = getClass().getClassLoader().getResource(downloadPath).getPath();
				File dir = new File(realDownLoadPath + topicName + "/");
				if (dir.exists()) {
					
					File [] files = dir.listFiles(new FilenameFilter() {
					    @Override
					    public boolean accept(File dir, String name) {
					        return name.contains("_" + photoName + ".");
					    }
					});
					for (File f : files) {
						f.delete();
					}
					return "photos deleted";
				} else {
					return "topic photo folder not found";
				}
			} catch (Exception e) {
				return "topicImages folder not found";
			}
		} 
		return "topic not found";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetCoverPhoto")
	public File[] getCoverPhoto(@RequestParam(value = "id") int id, @RequestParam(value = "photoName") String photoName) {
		if (topicRepository.existsById(id)) {
			Topic t = topicRepository.findById(id).get();
			String topicName = t.getName();
			String downloadPath = "images/topicImages/";
			
			try {
				String realdownloadPath = getClass().getClassLoader().getResource(downloadPath).getPath();
				File dir = new File(realdownloadPath + topicName + "/");
				if (dir.exists()) {
					File [] files = dir.listFiles(new FilenameFilter() {
					    @Override
					    public boolean accept(File dir, String name) {
					        return name.startsWith(topicName + "_" + photoName + ".");
					    }
					});
					return files;
				} else {
					System.out.println("Topic photo folder not found.");
					return null;
				}
			} catch (Exception e) {
				System.out.println("topicImages folder not found.");
				return null;
			}
		} else {
			System.out.println("Topic not found.");
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetThumCoverPhoto")
	public File[] getThumCoverPhoto(@RequestParam(value = "id") int id, @RequestParam(value = "photoName") String photoName) {
		if (topicRepository.existsById(id)) {
			Topic t = topicRepository.findById(id).get();
			String topicName = t.getName();
			String downloadPath = "images/topicImages/";
			
			try {
				String realdownloadPath = getClass().getClassLoader().getResource(downloadPath).getPath();
				File dir = new File(realdownloadPath + topicName + "/");
				if (dir.exists()) {
					File [] files = dir.listFiles(new FilenameFilter() {
					    @Override
					    public boolean accept(File dir, String name) {
					        return name.startsWith(topicName + "_" + "thum_" + photoName + ".");
					    }
					});
					return files;
				} else {
					System.out.println("Topic photo folder not found.");
					return null;
				}
			} catch (Exception e) {
				System.out.println("topicImages folder not found.");
				return null;
			}
		} else {
			System.out.println("Topic not found.");
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAllNorCoverPhotos")
	public File[] getAllNorCoverPhotos(@RequestParam(value = "id") int id) {
		if (topicRepository.existsById(id)) {
			Topic t = topicRepository.findById(id).get();
			String topicName = t.getName();
			String downloadPath = "images/topicImages/";
			
			try {
				String realdownloadPath = getClass().getClassLoader().getResource(downloadPath).getPath();
				File dir = new File(realdownloadPath + topicName + "/");
				if (dir.exists()) {
					File [] files = dir.listFiles(new FilenameFilter() {
					    @Override
					    public boolean accept(File dir, String name) {
					        return !name.contains("_thum_");
					    }
					});
					return files;
				} else {
					System.out.println("Topic photo folder not found.");
					return null;
				}
			} catch (Exception e) {
				System.out.println("topicImages folder not found.");
				return null;
			}
		} else {
			System.out.println("Topic not found.");
			return null;
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAllThumCoverPhotos")
	public File[] getAllThumCoverPhotos(@RequestParam(value = "id") int id) {
		if (topicRepository.existsById(id)) {
			Topic t = topicRepository.findById(id).get();
			String topicName = t.getName();
			String downloadPath = "images/topicImages/";
			
			try {
				String realdownloadPath = getClass().getClassLoader().getResource(downloadPath).getPath();
				File dir = new File(realdownloadPath + topicName + "/");
				if (dir.exists()) {
					File [] files = dir.listFiles(new FilenameFilter() {
					    @Override
					    public boolean accept(File dir, String name) {
					        return name.contains("_thum_");
					    }
					});
					return files;
				} else {
					System.out.println("Topic photo folder not found.");
					return null;
				}
			} catch (Exception e) {
				System.out.println("topicImages folder not found.");
				return null;
			}
			
		} else {
			System.out.println("Topic not found.");
			return null;
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/GetAllCoverPhotos")
	public File[] getAllCoverPhotos(@RequestParam(value = "id") int id) {
		if (topicRepository.existsById(id)) {
			Topic t = topicRepository.findById(id).get();
			String topicName = t.getName();
			String downloadPath = "images/topicImages/";
			try {
				String realdownloadPath = getClass().getClassLoader().getResource(downloadPath).getPath();
				
				File dir = new File(realdownloadPath + topicName + "/");
				if (dir.exists()) {
					return dir.listFiles();	
				} else {
					System.out.println("Topic photo folder not found.");
					return null;
				}
			} catch (Exception e) {
				System.out.println("topicImages folder not found");
				return null;
			}

		} else {
			System.out.println("Topic not found.");
			return null;
		}
	}
	


}
