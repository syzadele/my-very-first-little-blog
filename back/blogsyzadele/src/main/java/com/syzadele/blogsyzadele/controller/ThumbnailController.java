package com.syzadele.blogsyzadele.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.syzadele.blogsyzadele.service.ThumbnailService;
import com.syzadele.blogsyzadele.service.UploadService;


@RestController
@RequestMapping(value="/thumbnailController", method = RequestMethod.POST)
public class ThumbnailController {
    
    private static Logger logger = LoggerFactory.getLogger(ThumbnailController.class);
    
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ThumbnailService thumbnailService;
    
    @PostMapping(value="/uploadImages")
    public Map<String, String> thumbnail(MultipartFile image,HttpServletRequest request){
 
        String uploadPath = "images/";
        URL location = ThumbnailController.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println(location);
        String realUploadPath = getClass().getClassLoader().getResource(uploadPath).getPath();
        
        logger.info("上传相对目录：{}", uploadPath);
        logger.info("上传绝对目录：{}", uploadPath);
        
        String imageUrl = uploadService.uploadImage(image, uploadPath, "", realUploadPath);
        String thumImageUrl = thumbnailService.thumbnail(image, uploadPath, "", realUploadPath);
        
        Map <String, String> imageURLSMap = new HashMap<String, String>();
        imageURLSMap.put(image.getOriginalFilename(), imageUrl);
        imageURLSMap.put("thum_"+image.getOriginalFilename(), thumImageUrl);
  
        return imageURLSMap;
    }
    
}
