package com.example.backend.entity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 李东雷
 * @TIME：
 */
public class FiveImages {

    private MultipartFile image_1;
    private MultipartFile image_2;
    private MultipartFile image_3;
    private MultipartFile image_4;
    private MultipartFile image_5;



    public MultipartFile getImage_1() {
        return image_1;
    }

    public void setImage_1(MultipartFile image_1) {
        this.image_1 = image_1;
    }

    public MultipartFile getImage_2() {
        return image_2;
    }

    public void setImage_2(MultipartFile image_2) {
        this.image_2 = image_2;
    }

    public MultipartFile getImage_3() {
        return image_3;
    }

    public void setImage_3(MultipartFile image_3) {
        this.image_3 = image_3;
    }

    public MultipartFile getImage_4() {
        return image_4;
    }

    public void setImage_4(MultipartFile image_4) {
        this.image_4 = image_4;
    }

    public MultipartFile getImage_5() {
        return image_5;
    }

    public void setImage_5(MultipartFile image_5) {
        this.image_5 = image_5;
    }
}
