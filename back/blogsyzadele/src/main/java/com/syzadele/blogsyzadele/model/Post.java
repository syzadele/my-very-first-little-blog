package com.syzadele.blogsyzadele.model;

import java.sql.Date;

public class Post {
	
	private String topic;
	private String title;
	private Date posteDate;
	private String auther;
	private String content;
	private int readTimes;
	
	public Post(String topic, String title, Date posteDate, String auther, String content, int readTimes) {
		super();
		this.topic = topic;
		this.title = title;
		this.posteDate = posteDate;
		this.auther = auther;
		this.content = content;
		this.readTimes = readTimes;
	}
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
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
