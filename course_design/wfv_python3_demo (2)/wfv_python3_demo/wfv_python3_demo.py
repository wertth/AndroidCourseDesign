# -*- coding: utf-8 -*-
"""
  人脸比对WebA调用例接口文档(必看)：https://doc.xfyun.cn/rest_api/%E4%BA%BA%E8%84%B8%E6%AF%94%E5%AF%B9.html
  人脸比对图片格式必须为JPG（JPEG）,BMP,PNG,GIF,TIFF之一,宽和高必须大于 8px,小于等于 4000px,要求编码后图片大小不超过5M
  (Very Important)创建完webapi应用添加合成服务之后一定要设置ip白名单，找到控制台--我的应用--设置ip白名单，如何设置参考：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=41891
  错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
  @author iflytek
"""

# !/usr/bin/python
# -*- coding: UTF-8 -*-
import requests
import time
import json
import hashlib
import base64


def main():
    # 应用ID (必须为webapi类型应用，并开通人脸比对服务，参考帖子如何创建一个webapi应用：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=36481)
    x_appid = '*****'
    # 接口密钥(webapi类型应用开通人脸比对服务后，控制台--我的应用---人脸比对---相应服务的apikey)
    api_key = '*****'
    # webapi接口地址
    url = 'http://api.xfyun.cn/v1/service/v1/image_identify/face_verification'
    # 组装http请求头
    x_time = str(int(time.time()))
    param = {'auto_rotate': True}
    param = json.dumps(param)
    x_param = base64.b64encode(param.encode('utf-8'))
    m2 = hashlib.md5()
    m2.update(str(api_key + x_time + str(x_param, 'utf-8')).encode('utf-8'))
    x_checksum = m2.hexdigest()
    x_header = {
        'X-Appid': x_appid,
        'X-CurTime': x_time,
        'X-CheckSum': x_checksum,
        'X-Param': x_param,
    }
    # 对图片一和图片二base64编码
    with open(r'E:\\1.jpg', 'rb') as f:
        f1 = f.read()
    with open(r'E:\\2.jpg', 'rb') as f:
        f2 = f.read()
    f1_base64 = str(base64.b64encode(f1), 'utf-8')
    f2_base64 = str(base64.b64encode(f2), 'utf-8')
    data = {
        'first_image': f1_base64,
        'second_image': f2_base64,
    }
    req = requests.post(url, data=data, headers=x_header)
    result = str(req.content, 'utf-8')
    print(result)  # 错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
    return


if __name__ == '__main__':
    main()
