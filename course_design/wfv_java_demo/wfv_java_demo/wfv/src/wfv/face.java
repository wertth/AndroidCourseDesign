package wfv;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 人脸比对 WebAPI 接口调用示例 接口文档（必看）：https://doc.xfyun.cn/rest_api/%E4%BA%BA%E8%84%B8%E6%AF%94%E5%AF%B9.html
 * 人脸比对图片格式必须为JPG（JPEG）,BMP,PNG,GIF,TIFF之一,宽和高必须大于 8px,小于等于 4000px,要求编码后图片大小不超过5M
 * (Very Important)创建完webapi应用添加合成服务之后一定要设置ip白名单，找到控制台--我的应用--设置ip白名单，如何设置参考：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=41891
 * 错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 * @author iflytek
 */



public class face {
			// webapi 接口地址
			private static final String WEBWFV_URL = "http://api.xfyun.cn/v1/service/v1/image_identify/face_verification";
			// 应用ID (必须为webapi类型应用，并开通人脸比对服务，参考帖子如何创建一个webapi应用：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=36481)
			private static final String APPID = "******";
			// 接口密钥(webapi类型应用开通人脸比对服务后，控制台--我的应用---人脸比对---相应服务的apikey)
			private static final String API_KEY = "******";			
			// 图片地址 
			private static final String FILE_PATH1 = "*****";
			private static final String FILE_PATH2 = "*****";
			/**
			 * OCR WebAPI 调用示例程序
			 * 
			 * @param args
			 * @throws IOException
			 * @throws JSONException 
			 */
			public static void main(String[] args) throws IOException, JSONException {
				Map<String, String> header = buildHttpHeader();
			//图片1和图片二Base64编码之后需要urlencode
				byte[] imageByteArray1 = FileUtil.read(FILE_PATH1);
				String imageBase641 = new String(Base64.encodeBase64(imageByteArray1), "UTF-8");				
				byte[] imageByteArray2 = FileUtil.read(FILE_PATH2);
				String imageBase642 = new String(Base64.encodeBase64(imageByteArray2), "UTF-8");				
				String result = HttpUtil.doPost1(WEBWFV_URL, header, "first_image=" + URLEncoder.encode(imageBase641, "UTF-8") + "&" + "second_image="+ URLEncoder.encode(imageBase642, "UTF-8"));
				System.out.println("人脸比对接口调用结果：" + result);
			//	错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
			}

			/**
			 * 组装http请求头
			 * @throws JSONException 
			 */
			private static Map<String, String> buildHttpHeader() throws UnsupportedEncodingException, JSONException {
				String curTime = System.currentTimeMillis() / 1000L + "";				
				JSONObject param = new JSONObject();
				param.put("get_image", true);
				String params = param.toString();
				String paramBase64 = new String(Base64.encodeBase64(params.getBytes("UTF-8")));
				
				String checkSum = DigestUtils.md5Hex(API_KEY + curTime + paramBase64);
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
				header.put("X-Param", paramBase64);
				header.put("X-CurTime", curTime);
				header.put("X-CheckSum", checkSum);
				header.put("X-Appid", APPID);
				return header;
			}
}
