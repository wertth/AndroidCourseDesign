# -*- coding: utf-8 -*-
# !/usr/bin/python
# -*- coding: UTF-8 -*-
import requests
import time
import json
import hashlib
import base64


def main():
	
    x_appid = '5def91d5'
    api_key = '17ec4e51dfeac812bda781f67c72bf28'
    url = 'http://api.xfyun.cn/v1/service/v1/image_identify/face_verification'
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
    with open(r'1.jpg', 'rb') as f:
        f1 = f.read()
    with open(r'2.jpg', 'rb') as f:
        f2 = f.read()
    f1_base64 = str(base64.b64encode(f1), 'utf-8')
    f2_base64 = str(base64.b64encode(f2), 'utf-8')
    data = {
        'first_image': f1_base64,
        'second_image': f2_base64,
    }
    req = requests.post(url, data=data, headers=x_header)
    result = str(req.content, 'utf-8')
    print(result)
    return


if __name__ == '__main__':
    main()
