package com.syzadele.blogsyzadele.model;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="TOPIC")
public class Topic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique=true)
	private String name;
	@Lob
	private String presentation;
	
	private ArrayList<String> coverPhotos;
	@OneToMany(mappedBy="topic")
	private List<Post> posts;
	
	public Topic() {
		
	}
	public Topic(int id, String name, String presentation, ArrayList<String> coverPhotos) {
		this.id = id;
		this.name = name;
		this.presentation = presentation;
		this.coverPhotos = coverPhotos;
	}
	
	public Topic(String name, String presentation, ArrayList<String> coverPhotos, ArrayList<Post> posts) {
		super();
		this.name = name;
		this.presentation = presentation;
		this.coverPhotos = coverPhotos;
		this.posts = posts;
	}

	public void addCoverPhoto(String photo) {
		this.coverPhotos.add(photo);
	}
	
	public void deletePhoto(String photo) {
		this.coverPhotos.remove(this.coverPhotos.indexOf(photo));
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPresentation() {
		return presentation;
	}
	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}
	public ArrayList<String> getCoverPhotos() {
		return coverPhotos;
	}
	public void setCoverPhotos(ArrayList<String> coverPhotos) {
		this.coverPhotos = coverPhotos;
	}
	public List<Post> getPosts() {
		if (this.posts != null) {
			return (ArrayList<Post>) posts;
		}
		else {
			return new ArrayList<>();
		}
	}
	public Post getPost(int id) {
		for (Post p : this.posts) {
			if (p.getId() == id) return p;
		}
		return null;
	}
	public void setPosts(ArrayList<Post> posts) {
		this.posts = posts;
	}
		
	public void addPost(Post post) {
		if(this.posts == null) this.posts = new ArrayList<>();
		this.posts.add(post);
	}
	
	public void deletePost(int id) {
		this.posts.remove(this.posts.indexOf(this.getPost(id)));
	}
}
