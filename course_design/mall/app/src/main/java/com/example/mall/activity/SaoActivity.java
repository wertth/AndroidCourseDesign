package com.example.mall.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.example.mall.*;
import com.example.mall.entity.Goods;
import com.example.mall.entity.User;
import okhttp3.*;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SaoActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks{

    public Config config;
    private ImageView ivTest;


    public TextView input_username;
    public ProgressBar progress_bar_sao;


    private File cameraSavePath;//拍照照片路径
    private Uri uri;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sao);

        // TakePhoto 权限
        ActivityCompat.requestPermissions(SaoActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        config = new Config();

        progress_bar_sao = findViewById(R.id.progress_bar_sao);
        //代码中控制显隐藏
        //progress_bar_sao = (ProgressBar) findViewById(R.id.progress_bar_take_photo);
        progress_bar_sao.setVisibility(View.INVISIBLE);

        Button btnGetPicFromCamera = findViewById(R.id.btn_get_pic_from_camera);
        Button btnGetPicFromPhotoAlbum = findViewById(R.id.btn_get_pic_form_photo_album);
        Button btnGetPermission = findViewById(R.id.btn_get_Permission);
        ivTest = findViewById(R.id.iv_test);

        input_username = findViewById(R.id.input_username);

        btnGetPicFromCamera.setOnClickListener(this);
        btnGetPicFromPhotoAlbum.setOnClickListener(this);
        btnGetPermission.setOnClickListener(this);

        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_get_pic_from_camera:
                goCamera();
                break;
            case R.id.btn_get_pic_form_photo_album:
                goPhotoAlbum();
                break;
            case R.id.btn_get_Permission:
                getPermission();
                break;
        }
    }
    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }

    }


    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    //激活相机操作
    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(SaoActivity.this, "com.example.mall.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        SaoActivity.this.startActivityForResult(intent, 1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //成功打开权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
    }
    //用户未同意权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String photoPath;
        if (requestCode == 1  && resultCode == RESULT_OK) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
            } else {
                photoPath = uri.getEncodedPath();
            }

            try{
                FileInputStream fis = new FileInputStream(photoPath);
                Bitmap bitmap  = BitmapFactory.decodeStream(fis);
//                iv_test_2.setImageBitmap(bitmap);

                String path = Environment.getExternalStorageDirectory() + "/Ask";

                File file = new File(path);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);

                progress_bar_sao = findViewById(R.id.progress_bar_sao);
                progress_bar_sao.setVisibility(View.VISIBLE);
                ivTest.setImageBitmap(bitmap);
                Log.e("TAG", "SaoActivity:拍照 "+file.length() );

                doUploadSao(file);
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("拍照返回图片路径:", photoPath);
            Glide.with(SaoActivity.this).load(photoPath).into(ivTest);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            progress_bar_sao.setVisibility(View.VISIBLE);
            try{
                FileInputStream fis = new FileInputStream(photoPath);
                Bitmap bitmap  = BitmapFactory.decodeStream(fis);
//                iv_test_2.setImageBitmap(bitmap);

                String path = Environment.getExternalStorageDirectory() + "/Ask";

                File file = new File(path);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                // 图像压缩 很必要 重要
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                Log.e("TAG", "SaoActivity: "+file.length() );


                doUploadSao(file);
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("拍照返回图片路径:", photoPath);
            Glide.with(SaoActivity.this).load(photoPath).into(ivTest);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume(){
        super.onResume();
        //progress_bar_sao.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onStart(){
        super.onStart();
        //progress_bar_sao.setVisibility(View.INVISIBLE);
    }

    /**
     * 模拟表单上传文件：通过MultipartBody
     */
    private void doUploadSao(File file) {
        //File file = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/iv_500x400.png");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //1.获取OkHttpClient对象
        config = new Config();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .build();


        //2.获取Request对象
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "test.jpg", fileBody)

                .build();
        Request request = new Request.Builder()
                .url(config.getPath()+"getSaoGoods")

                .post(requestBody).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Reg Img SaoActivity", "onFailure: " + e);
                //ToastUtil();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();

                JSONArray res = JSON.parseArray(result);

                final List<Goods> goods = res.toJavaList(Goods.class);
                Log.e("head pic  Sao", "onResponse: " + result);
                //runOnUiThread(() -> ToastUtil.showAtOnce(MainActivity.this, result));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("扫一扫识别", "run: "+result);
                        startNew(goods);
                    }
                });
            }
        });
    }
    public void ToastUtil(){
        Toast.makeText(SaoActivity.this,"识别失败",Toast.LENGTH_SHORT).show();
    }
    public void startNew(List<Goods> goods){
        Intent intent = new Intent(this, list_goods2Activity.class);
        intent.putExtra("goods",JSON.toJSONString(goods));
        startActivity(intent);
        finish();

    }

}
