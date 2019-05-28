package com.syzadele.blogsyzadele.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("Topic")
@Table(name="TopicCoverPhotos")
public class TopicCoverPhotos extends DBFile{
	
	@ManyToOne
	@JoinColumn(name="topic_id")
	@JsonIgnoreProperties(ignoreUnknown = true, value = {"posts", "TopicCoverPhotos"})
	private Topic topic;

	public TopicCoverPhotos() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TopicCoverPhotos(String fileName, String fileType, byte[] data, Topic topic) {
		super(fileName, fileType, data);
		this.topic = topic;
		// TODO Auto-generated constructor stub
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

}
