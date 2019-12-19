package com.example.backend.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.backend.Service.ImageProcessService;
import com.example.backend.Service.UserService;
import com.example.backend.entity.User;
import com.example.backend.face.face;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class LoginAndReg {

    @Autowired
    public ImageProcessService imageProcessService;
    @Autowired
    UserService userService;

    @Autowired
    face face1;

    @RequestMapping("api/reg")
    public void reg(String user){
        System.out.println(user);
        User userInfo = JSON.parseObject(user,User.class);
        System.out.println(userInfo);
        //String password
        userService.registerUser(userInfo);

    }
    @PostMapping(value = "api/loginWithPassword")
    public String Login(String username, String password) {
        System.out.println("username:"+username+"password:"+password);
        //String pass = userService.findPasswordByUsername(username);
        User user = userService.selectUser(username);
        String pass = user.getPassword();
        System.out.println(pass);
        String userInfo = JSON.toJSONString(user);
        if(pass == null) return "false";
        else if(pass.equals(password))
            return userInfo;
        else return "false";
    }
    @RequestMapping("/uploadImage")
    public String Login(MultipartFile file){
        String fileName = file.getOriginalFilename();

        String url = imageProcessService.uploadImg(file);

        System.out.println(fileName);
        return url;
    }

    @PostMapping("/isOne")
    public String isOnePerson(@RequestParam(value = "username")String username,
                            @RequestParam(value = "headpic")String headpic){
        System.out.println("username"+username);
        System.out.println("headpic"+headpic);
        String userImg = userService.getHeadpic(username);
        System.out.println("userImg" + userImg);
        String res = "";
        try {
             res = face1.isOne(userImg, headpic);
            System.out.println("res = "+res);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.parseObject(res);

        String t = jsonObject.getString("desc");
        double rate = jsonObject.getDouble("data");

        if("success".equals(t)){
            if(rate >= 0.5){
                User user = userService.selectUser(username);
                String str = JSON.toJSONString(user);
                return str;
            }else return "false";

        }
        //headpichttp://q2cngnz5c.bkt.clouddn.com/FsNo3y_3I06ESkKQRAj0Tm5HWweN
        //userImghttp://q2cngnz5c.bkt.clouddn.com/FpHS5t2MismNhB8LxMiIX93gDSI9
        else return "false";

    }
}
