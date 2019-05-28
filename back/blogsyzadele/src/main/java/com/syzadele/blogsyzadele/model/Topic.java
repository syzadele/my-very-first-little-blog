package com.syzadele.blogsyzadele.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
//import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="TOPIC")
public class Topic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique=true, length = 100)
	private String name;
	@Lob
	private String presentation;
	@OneToMany(mappedBy="topic", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonIgnoreProperties("topic")
	private List<TopicCoverPhotos> coverPhotos;
	@OneToMany(mappedBy="topic", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonIgnoreProperties("topic")
	private List<Post> posts;
	
	public Topic() {
		
	}
	public Topic(String name, String presentation) {
		this.name = name;
		this.presentation = presentation;
	}
	
	public Topic(String name, String presentation, List<TopicCoverPhotos> coverPhotos) {
		this.name = name;
		this.presentation = presentation;
		this.coverPhotos = coverPhotos;
	}
	
	public Topic(String name, String presentation, List<TopicCoverPhotos> coverPhotos, List<Post> posts) {
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
	public List<TopicCoverPhotos> getCoverPhotos() {
		return coverPhotos;
	}
	
	public void setCoverPhotos(List<TopicCoverPhotos> coverPhotos) {
		this.coverPhotos = coverPhotos;
	}
	
	public void addCoverPhotos(TopicCoverPhotos photo) {
		if (!this.coverPhotos.contains(photo)) {
			this.coverPhotos.add(photo);
		}
	}
	
	public void addMCoverPhotos(List<TopicCoverPhotos> photos) {
		this.coverPhotos.addAll(photos);
	}
	
	public void deleteCoverPhotos(TopicCoverPhotos photo) {
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
