package com.example.backend.controller.api;


import com.example.backend.Service.GoodsService;
import com.example.backend.Service.ImageProcessService;
import com.example.backend.controller.UploadController;
import com.example.backend.entity.FiveImages;
import com.example.backend.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class index {

    @Autowired
    public UploadController uploadController;

    @Autowired
    public ImageProcessService imageProcessService;

    @Autowired
    public GoodsService goodsService;

    @RequestMapping("/goodsedit")
    ModelAndView index(){
        return new ModelAndView("goodsEdit");
    }
    @RequestMapping("/publishgoods")
    @ResponseBody
    public ModelAndView publish(Goods goods,
                         FiveImages fiveImages,
                         ModelAndView modelAndView){
        long goodsid = new Date().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String pubdate = sdf.format(new Date());

        goods.setGoodsid(goodsid);
        goods.setPubdate(pubdate);//发布时间



        String filename = "";

        if(!fiveImages.getImage_1().getOriginalFilename().isEmpty()){
            MultipartFile file = fiveImages.getImage_1();
            System.out.println("Image1 process.......");
            filename = imageProcessService.uploadImg(file);
            goods.setImage1(filename);
        }
        if(!fiveImages.getImage_2().getOriginalFilename().isEmpty()){
            MultipartFile file = fiveImages.getImage_2();
            filename = imageProcessService.uploadImg(file);
            goods.setImage2(filename);
        }
        if(!fiveImages.getImage_3().getOriginalFilename().isEmpty()){
            MultipartFile file = fiveImages.getImage_3();
            filename = imageProcessService.uploadImg(file);
            goods.setImage3(filename);
        }
        if(!fiveImages.getImage_4().getOriginalFilename().isEmpty()){
            MultipartFile file = fiveImages.getImage_4();
            filename = imageProcessService.uploadImg(file);
            goods.setImage4(filename);
        }
        if(!fiveImages.getImage_5().getOriginalFilename().isEmpty()){
            MultipartFile file = fiveImages.getImage_5();
            filename = imageProcessService.uploadImg(file);
            goods.setImage5(filename);
        }



        System.out.println(goods);

        goodsService.insertGoods(goods);
        //elasticSearchService.insertGoods(goods);

        //System.out.println("file: " + filename);

        modelAndView.setViewName("redirect:/goodsedit");
        return modelAndView;
    }
}
