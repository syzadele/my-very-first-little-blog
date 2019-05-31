package com.syzadele.blogsyzadele.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.syzadele.blogsyzadele.exception.FileStorageException;
import com.syzadele.blogsyzadele.exception.MyFileNotFoundException;
import com.syzadele.blogsyzadele.model.Topic;
import com.syzadele.blogsyzadele.model.TopicCoverPhotos;
import com.syzadele.blogsyzadele.repository.DBFileRepository;

@Service
public class TopicCoverPhotoService {
	@Autowired
	private DBFileRepository dbfRepository;
	
	public TopicCoverPhotos storeFile(MultipartFile file, Topic topic) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            TopicCoverPhotos tCoverPhotos = new TopicCoverPhotos(fileName, file.getContentType(), file.getBytes(), topic);
            
            return dbfRepository.save(tCoverPhotos);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
	
	public List<TopicCoverPhotos> storeMultipleFile(MultipartFile[] files, Topic topic) {
		return Arrays.asList(files)
                .stream()
                .map(file -> storeFile(file, topic))
                .collect(Collectors.toList());
	}
	
	public TopicCoverPhotos getFile(String fileId) {
        return (TopicCoverPhotos) dbfRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
	
	public TopicCoverPhotos getFileByName(String fileName) {
		if (dbfRepository.existsByFileName(fileName)) {
			return (TopicCoverPhotos) dbfRepository.findByFileName(fileName);
		} else {
			throw new MyFileNotFoundException("File not found with file name " + fileName);
		}
	}
	
	public boolean deleteFile(String id) {
		if (dbfRepository.existsById(id)) {
			dbfRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
	public boolean deleteFileByFileName(String fileName) {
		if (dbfRepository.existsByFileName(fileName)) {
			dbfRepository.deleteByFileName(fileName);
			return true;
		} 
		return false;
	}
}
