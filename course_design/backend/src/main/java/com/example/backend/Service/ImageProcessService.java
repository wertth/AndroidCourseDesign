package com.example.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageProcessService {
    @Autowired
    FileService fileService;
    @Value("${baseUploadUrl}")
    private String url;

    public String uploadImg( MultipartFile upfile) {
        Map<String,Object> map = new HashMap<>();
        String fileName = upfile.getOriginalFilename();
        File file = new File(url + fileName);
        Object imageName = "";
        try{
            //将MulitpartFile文件转化为file文件格式
            upfile.transferTo(file);
            Map response = fileService.uploadFile(file);
            imageName = response.get("imgName");
            map.put("url",imageName);
            System.out.println("url == " + imageName);
            map.put("state","SUCESS");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "http://q2cngnz5c.bkt.clouddn.com/"+imageName;
    }

}
