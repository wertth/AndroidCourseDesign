package com.example.mall;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.mall.entity.User;
import okhttp3.*;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TakePhoto extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private ImageView ivTest;
//    public ImageView iv_test_2;

    public TextView input_username;


    private File cameraSavePath;//拍照照片路径
    private Uri uri;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public Config config;
    public ProgressBar progress_bar_take_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        // TakePhoto 权限
        ActivityCompat.requestPermissions(TakePhoto.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        config = new Config();

        progress_bar_take_photo = findViewById(R.id.progress_bar_take_photo);
        progress_bar_take_photo.setVisibility(View.INVISIBLE);

        Button btnGetPicFromCamera = findViewById(R.id.btn_get_pic_from_camera);
        Button btnGetPicFromPhotoAlbum = findViewById(R.id.btn_get_pic_form_photo_album);
        Button btnGetPermission = findViewById(R.id.btn_get_Permission);
        ivTest = findViewById(R.id.iv_test);
//        iv_test_2 = findViewById(R.id.iv_test_2);
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
            uri = FileProvider.getUriForFile(TakePhoto.this, "com.example.mall.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        TakePhoto.this.startActivityForResult(intent, 1);
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

            try {
                FileInputStream fis = new FileInputStream(photoPath);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
//                iv_test_2.setImageBitmap(bitmap);

                String path = Environment.getExternalStorageDirectory() + "/Ask";

                File file = new File(path);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 15, bos);
                //代码中控制显隐藏
                progress_bar_take_photo = (ProgressBar) findViewById(R.id.progress_bar_take_photo);
                progress_bar_take_photo.setVisibility(View.VISIBLE);
                Log.e("TAG", "onActivityResult: "+file.length() );
                doUploadHeadPic(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("拍照返回图片路径:", photoPath);
            Glide.with(TakePhoto.this).load(photoPath).into(ivTest);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Glide.with(TakePhoto.this).load(photoPath).into(ivTest);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 模拟表单上传文件：通过MultipartBody
     */
    private void doUploadHeadPic(File file) {
        //File file = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/iv_500x400.png");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //1.获取OkHttpClient对象
        config = new Config();
        //OkHttpClient okHttpClient = new OkHttpClient();
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
                .url(config.getPath() + "uploadImage")
                .post(requestBody).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Reg Img", "onFailure: " + e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.e("head pic  Img", "onResponse: " + result);
                //runOnUiThread(() -> ToastUtil.showAtOnce(MainActivity.this, result));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //if(flag == 1)
                        //    logo.setText(result);
                        //else if(flag == 2)
                        //    head_pic.setText(result);
                        String username = input_username.getText().toString().trim();
                        doPostHead(result, username);
                    }
                });
            }
        });
    }

    public void ToastUtil(){
        Toast.makeText(TakePhoto.this,"刷脸失败，请重新尝试",Toast.LENGTH_SHORT).show();
    }

    private void doPostHead(String headpic, String username) {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造RequestBody
        FormBody body = new FormBody.Builder().add("username", username).add("headpic", headpic).build();
        Request request = new Request.Builder().url(config.getPath() + "isOne").post(body).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.e("TAG", "onResponse: " + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("false".equals(result)) {
                            Log.e("login with face ", "run: 登录失败");
                            LoginFailure();
                        } else {
                            Log.e("login with face ", "run: 登录成功");
                            LoginSuccess(result);
                        }

                    }
                });
            }
        });

    }

    public void LoginFailure() {
        Toast.makeText(TakePhoto.this, "人脸认证失败，请重新认证", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void LoginSuccess(String result) {
        User user = JSON.parseObject(result, User.class);
        setLogin(user);
        //startActivity(new Intent(this, MineFragment.class));
        finish();
    }

    public void setLogin(User user) {
        String username = user.getUsername().trim();
        String password = user.getPassword();
        String logo = user.getLogo();
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("isLogin", "true");
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("logo", logo);
        editor.putString("address", user.getAddress());
        editor.putString("addr", user.getAddr());
        editor.putString("tel", user.getTel());
        editor.commit();
    }


}
