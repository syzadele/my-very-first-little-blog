package com.syzadele.blogsyzadele.model;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "files")
public class DBFile {
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
	@Column(unique=true, length = 100)
	private String fileName;

	private String fileType;
	
	@Lob
	private byte[] data;
    
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

    public DBFile() {

    }
    
    public DBFile(String fileName, String fileType, byte[] data) {
        this.setFileName(fileName);
        this.setFileType(fileType);
        this.data = data;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
