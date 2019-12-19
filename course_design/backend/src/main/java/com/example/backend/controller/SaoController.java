package com.example.backend.controller;

import com.alibaba.fastjson.JSON;
import com.example.backend.Service.BaiduImage;
import com.example.backend.Service.GoodsService;
import com.example.backend.entity.Goods;
import com.example.backend.entity.Result;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SaoController {

    @Autowired
    BaiduImage baiduImage;

    @Autowired
    GoodsService goodsService;


    @RequestMapping("/getSaoGoods")
    public String getSaoGoods(MultipartFile file) throws Exception {
        System.out.println("Hello World");
        InputStream is = file.getInputStream();
        byte[] data = readInputStream(is);
        Long t = System.currentTimeMillis();
        File file1 = new File(t + ".jpg");
        FileOutputStream outStream = new FileOutputStream(file1);
        outStream.write(data);

        System.out.println("file1.length() = "+file1.length());
        System.out.println("file1.path " + file1.getAbsolutePath());
        // 图片压缩
        File file2 = new File("hello.jpg");
        Thumbnails.of(file1).scale(0.8f).toFile(file2);//按比例缩小

        outStream.close();
        System.out.println("file2.length() = "+file1.length());
        System.out.println("file2.path " + file2.getAbsolutePath());


        String result = baiduImage.ingredient(file2.getAbsolutePath());
        System.out.println("扫一扫：" + result);

        com.alibaba.fastjson.JSONObject object = JSON.parseObject(result);
        String s = object.getString("result");
        List<Result> results = JSON.parseArray(s, Result.class);
        System.out.println(results);

        List<Goods> goods = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            //System.out.println(results.get(i).getRoot());
            //System.out.println(results.get(i).getKeyword());
            System.out.println("测试苹果："+results.get(i).getKeyword());

            List<Goods> g = goodsService.selectGoodsByName("%"+results.get(i).getKeyword()+"%");
            System.out.println("测试苹果："+g);
            for (Goods j : g) {
                goods.add(j);
            }
        }
        new File(t+".jpg").delete();
        System.out.println(goods);
        //去重
        List<Goods> list2 = goods.stream().distinct().collect(Collectors.toList());
        System.out.println("return : " + JSON.toJSONString(list2));
        return JSON.toJSONString(list2);


        //File imageFile = new File(t + ".jpg");
        //创建输出流


    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}
