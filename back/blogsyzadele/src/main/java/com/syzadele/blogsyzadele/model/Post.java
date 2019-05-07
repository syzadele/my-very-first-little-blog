package com.syzadele.blogsyzadele.model;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="POST")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String topic;
	private String title;
	@Temporal(TemporalType.TIMESTAMP)
	private Date posteDate;
	private String auther;
	@Lob
	private String content;
	private int readTimes;
	
	public Post() {

	}
	
	public Post(String title, Date posteDate, String auther, String content, int readTimes) {
		super();
		this.title = title;
		this.posteDate = posteDate;
		this.auther = auther;
		this.content = content;
		this.readTimes = readTimes;
	}

	public Post(int id, String topic, String title, Date posteDate, String auther, String content, int readTimes) {
		super();
		this.id = id;
		this.topic = topic;
		this.title = title;
		this.posteDate = posteDate;
		this.auther = auther;
		this.content = content;
		this.readTimes = readTimes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
