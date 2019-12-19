package com.example.backend.Service;


import com.qiniu.common.QiniuException;

import java.io.File;
import java.util.Map;

public interface FileService {

    /**
     * @Author :
     * @Description : //TODO 多文件上传
     * @Date : 10:04 2019/4/3
     * @Param :
     * @return :
     **/
    Map uploadFile(File file) throws QiniuException;

}

