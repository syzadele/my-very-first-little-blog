package com.syzadele.blogsyzadele.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.syzadele.blogsyzadele.model.DBFile;

@Repository	
public interface DBFileRepository extends JpaRepository<DBFile, String> {

}
