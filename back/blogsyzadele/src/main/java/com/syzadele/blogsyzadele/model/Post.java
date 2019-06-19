package com.syzadele.blogsyzadele.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="POST")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique=true, length=100)
	private String title;
	@Temporal(TemporalType.TIMESTAMP)
	private Date posteDate;
	private String auther;
	@Lob
	private String content;
	private int readTimes;
	@ManyToOne
	@JoinColumn(name="topic_id")
	@JsonIgnoreProperties("posts")
	private Topic topic;
	
	public Post() {

	}
	
	public Post(String title, Topic topic, Date posteDate, String auther, String content) {
		super();
		this.title = title;
		this.topic = topic;
		this.posteDate = posteDate;
		this.auther = auther;
		this.content = content;
		this.readTimes = 0;
	}

	public Post(int id, Topic topic, String title, Date posteDate, String auther, String content) {
		super();
		this.id = id;
		this.topic = topic;
		this.title = title;
		this.posteDate = posteDate;
		this.auther = auther;
		this.content = content;
		this.readTimes = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPosteDate() {
		return posteDate;
	}

	public void setPosteDate(Date posteDate) {
		this.posteDate = posteDate;
	}

	public String getAuther() {
		return auther;
	}

	public void setAuther(String auther) {
		this.auther = auther;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getReadTimes() {
		return readTimes;
	}

	public void setReadTimes(int readTimes) {
		this.readTimes = readTimes;
	}
	

}
