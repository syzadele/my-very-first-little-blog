package com.syzadele.blogsyzadele.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	@Column
	@ElementCollection(targetClass=String.class)
	private List<String> coverPhotos;
	@OneToMany(mappedBy="topic", cascade = CascadeType.ALL)
	private List<Post> posts;
	
	public Topic() {
		
	}
	public Topic(int id, String name, String presentation, List<String> coverPhotos) {
		this.id = id;
		this.name = name;
		this.presentation = presentation;
		this.coverPhotos = coverPhotos;
	}
	
	public Topic(String name, String presentation, List<String> coverPhotos, List<Post> posts) {
		super();
		this.name = name;
		this.presentation = presentation;
		this.coverPhotos = coverPhotos;
		this.posts = posts;
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
	public List<String> getCoverPhotos() {
		return coverPhotos;
	}
	
	public void setCoverPhotos(List<String> coverPhotos) {
		this.coverPhotos = coverPhotos;
	}
	
	public void addCoverPhotos(String photo) {
		if (!this.coverPhotos.contains(photo)) {
			this.coverPhotos.add(photo);
		}
	}
	
	public void delete(String photo) {
		if (this.coverPhotos.contains(photo)) {
			this.coverPhotos.remove(photo);
		}
	}
	public List<Post> getPosts() {
		return posts;
	}
	public Post getPost(int id) {
		for (Post p : this.posts) {
			if (p.getId() == id) return p;
		}
		return null;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
		
	public void addPost(Post post) {
		this.posts.add(post);
	}
	
}
