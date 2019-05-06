package com.syzadele.blogsyzadele.model;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Topic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique=true)
	private String name;
	@Lob
	private String presentation;

	private ArrayList<String> coverPhotos;
	private ArrayList<Post> posts;
	
	public Topic() {
		
	}
	public Topic(int id, String name, String presentation, ArrayList<String> coverPhotos) {
		this.id = id;
		this.name = name;
		this.presentation = presentation;
		this.coverPhotos = coverPhotos;
	}
	
	public Topic(String name, String presentation, ArrayList<String> coverPhotos) {
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
	
	public void addPost(Post post) {
		this.posts.add(post);
	}
	
	public void deletePost(int id) {
		this.posts.remove(this.posts.indexOf(this.getPost(id)));
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
	public ArrayList<Post> getPosts() {
		return posts;
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
		
}
