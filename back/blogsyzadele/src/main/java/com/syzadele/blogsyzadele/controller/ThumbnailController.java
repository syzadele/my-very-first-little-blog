package com.syzadele.blogsyzadele.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.syzadele.blogsyzadele.service.ThumbnailService;
import com.syzadele.blogsyzadele.service.UploadService;


@RestController
@RequestMapping("/thumbnail")
public class ThumbnailController {
    
    private static Logger logger = LoggerFactory.getLogger(ThumbnailController.class);
    
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ThumbnailService thumbnailService;
    
    @RequestMapping("/aa")
    public ModelAndView thumbnail(MultipartFile image,HttpServletRequest request){
        
        ModelAndView mav = new ModelAndView();
        
        String uploadPath = "/static/images/";
        String realUploadPath = getClass().getClassLoader().getResource(uploadPath).getPath();
        
        logger.info("上传相对目录：{}", uploadPath);
        logger.info("上传绝对目录：{}", uploadPath);
        
        String imageUrl = uploadService.uploadImage(image, uploadPath, realUploadPath);
        String thumImageUrl = thumbnailService.thumbnail(image, uploadPath, realUploadPath);
        
        mav.addObject("imageURL", imageUrl);
        mav.addObject("thumImageUrl", thumImageUrl);
        mav.setViewName("/thumbnail");
        return mav;
    }
    
}
