package com.example.mall.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.mall.Config;
import com.example.mall.R;
import com.example.mall.entity.User;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.db.manager.AddressDictManager;
import com.smarttop.library.utils.LogUtil;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;
import okhttp3.*;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.example.mall.utils.MD5.md5;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, OnAddressSelectedListener, AddressSelector.OnDialogCloseListener, AddressSelector.onSelectorAreaPositionListener {
    private EditText username;
    private EditText password;
    private TextView logo;
    private EditText gender;
    private EditText tel;
    private EditText address;
    private EditText postcode;
    private Button btn_submit;
    public ImageView test_img;
    public Button btn_choose_img;
    public Config config;
    // headpic
    public TextView head_pic;
    public Button btn_choose_headpic;
    public ImageView headpic;
    public int flag;

    public TextView tv_selector_area;
    private BottomDialog dialog;
    private String provinceCode;
    private String cityCode;
    private String countyCode;
    private String streetCode;
    private int provincePosition;
    private int cityPosition;
    private int countyPosition;
    private int streetPosition;
    private AddressDictManager addressDictManager;

    private TextView showAddr;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //private String path = "http://192.168.1.176/";

    public void verifyPermission(Context context){
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        initView();
        btn_submit.setOnClickListener(this);

        head_pic = findViewById(R.id.head_pic);
        headpic = findViewById(R.id.headpic);
        btn_choose_headpic = findViewById(R.id.btn_choose_headpic);
        btn_choose_headpic.setOnClickListener(this);

        tv_selector_area = (TextView) findViewById(R.id.tv_selector_area);
        //content = (LinearLayout) findViewById(R.id.content);
        tv_selector_area.setOnClickListener(this);
        AddressSelector selector = new AddressSelector(this);
        //获取地址管理数据库
        addressDictManager = selector.getAddressDictManager();

        selector.setTextSize(18);//设置字体的大小
        selector.setIndicatorBackgroundColor(android.R.color.holo_orange_light);//设置指示器的颜色
//        selector.setBackgroundColor(android.R.color.holo_red_light);//设置字体的背景

        selector.setTextSelectedColor(android.R.color.holo_orange_light);//设置字体获得焦点的颜色

        selector.setTextUnSelectedColor(android.R.color.holo_blue_light);//设置字体没有获得焦点的颜色

//        //获取数据库管理
        //AddressDictManager addressDictManager = selector.getAddressDictManager();
        //AdressBean.ChangeRecordsBean changeRecordsBean = new AdressBean.ChangeRecordsBean();
        //changeRecordsBean.parentId = 0;
        //changeRecordsBean.name = "测试省";
        //changeRecordsBean.id = 35;
        //addressDictManager.inserddress(changeRecordsBean);//对数据库里增加一个数据
        selector.setOnAddressSelectedListener(new OnAddressSelectedListener() {
            @Override
            public void onAddressSelected(Province province, City city, County county, Street street) {

            }
        });
        //View view = selector.getView();
        //content.addView(view);

        //showAddr = (TextView) findViewById(R.id.showAddr);


    }

    public void click() {
        if (dialog != null) {
            dialog.show();
        } else {
            dialog = new BottomDialog(this);
            dialog.setOnAddressSelectedListener(this);
            dialog.setDialogDismisListener(this);
            dialog.setTextSize(18);//设置字体的大小
            dialog.setIndicatorBackgroundColor(android.R.color.holo_orange_light);//设置指示器的颜色
            dialog.setTextSelectedColor(android.R.color.holo_orange_light);//设置字体获得焦点的颜色
            dialog.setTextUnSelectedColor(android.R.color.holo_blue_light);//设置字体没有获得焦点的颜色
//            dialog.setDisplaySelectorArea("31",1,"2704",1,"2711",0,"15582",1);//设置已选中的地区
            dialog.setSelectorAreaPositionListener(this);
            dialog.show();
        }
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        provinceCode = (province == null ? "" : province.code);
        cityCode = (city == null ? "" : city.code);
        countyCode = (county == null ? "" : county.code);
        streetCode = (street == null ? "" : street.code);
        LogUtil.d("数据", "省份id=" + provinceCode);
        LogUtil.d("数据", "城市id=" + cityCode);
        LogUtil.d("数据", "乡镇id=" + countyCode);
        LogUtil.d("数据", "街道id=" + streetCode);
        String s = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        tv_selector_area.setText(s);

        // showAddr 显示
        //showAddr.setText(s);

        if (dialog != null) {
            dialog.dismiss();
        }
//        getSelectedArea();
    }

    @Override
    public void dialogclose() {
        if(dialog!=null){
            dialog.dismiss();
        }
    }

    /**
     * 根据code 来显示选择过的地区
     */
    private void getSelectedArea(){
        String province = addressDictManager.getProvince(provinceCode);
        String city = addressDictManager.getCity(cityCode);
        String county = addressDictManager.getCounty(countyCode);
        String street = addressDictManager.getStreet(streetCode);
        tv_selector_area.setText(province+city+county+street);
        LogUtil.d("数据", "省份=" + province);
        LogUtil.d("数据", "城市=" + city);
        LogUtil.d("数据", "乡镇=" + county);
        LogUtil.d("数据", "街道=" + street);
    }

    @Override
    public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {
        this.provincePosition = provincePosition;
        this.cityPosition = cityPosition;
        this.countyPosition = countyPosition;
        this.streetPosition = streetPosition;
        LogUtil.d("数据", "省份位置=" + provincePosition);
        LogUtil.d("数据", "城市位置=" + cityPosition);
        LogUtil.d("数据", "乡镇位置=" + countyPosition);
        LogUtil.d("数据", "街道位置=" + streetPosition);
    }

    void initView() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        logo = findViewById(R.id.logo);
        gender = findViewById(R.id.gender);
        tel = findViewById(R.id.tel);
        address = findViewById(R.id.address);
        postcode = findViewById(R.id.postcode);
        btn_submit = findViewById(R.id.btn_submit);

        test_img = findViewById(R.id.test_img);
        btn_choose_img = findViewById(R.id.btn_choose_img);
        btn_choose_img.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                infoSubmit();
                break;
            case R.id.btn_choose_img:
                flag = 1;
                verifyPermission(this);
                selectPic();

                break;
            case R.id.tv_selector_area:
                click();
                break;
            case R.id.btn_choose_headpic:
                flag = 2;
                verifyPermission(this);
                selectPic();
                break;


        }
    }

    /**
     * 打开本地相册选择图片
     */
    private void selectPic(){
        //intent可以应用于广播和发起意图，其中属性有：ComponentName,action,data等
        Intent intent=new Intent();
        intent.setType("image/*");
        //action表示intent的类型，可以是查看、删除、发布或其他情况；我们选择ACTION_GET_CONTENT，系统可以根据Type类型来调用系统程序选择Type
        //类型的内容给你选择
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //如果第二个参数大于或等于0，那么当用户操作完成后会返回到本程序的onActivityResult方法


        startActivityForResult(intent, 1);
    }
    /**
     *把用户选择的图片显示在imageview中
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //用户操作完成，结果码返回是-1，即RESULT_OK
        if(resultCode==RESULT_OK){
            //获取选中文件的定位符
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            //使用content的接口
            ContentResolver cr = this.getContentResolver();
            try {
                //获取图片
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                if (flag == 1) {
                    BitmapToRound_Util round_Util = new BitmapToRound_Util();
                    bitmap = round_Util.toRoundBitmap(bitmap);
                }

                if(flag == 1)
                    test_img.setImageBitmap(bitmap);
                else if(2 == flag)
                    headpic.setImageBitmap(bitmap);

                String path = Environment.getExternalStorageDirectory() + "/Ask";


                File file = new File(path);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                //uploadPic(file);
                doUploadImg(file);

                bos.flush();
                bos.close();


            } catch (Exception e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }else{
            //操作错误或没有选择图片
            Log.i("MainActivtiy", "operation error");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void infoSubmit() {
        User user = new User(username.getText().toString().trim(),md5(password.getText().toString().trim()),
                logo.getText().toString().trim(),gender.getText().toString().trim(),
                tel.getText().toString().trim(), tv_selector_area.getText().toString().trim(),
                address.getText().toString().trim(),
                postcode.getText().toString().trim(), head_pic.getText().toString().trim());
        Log.e("注册信息", "infoSubmit: "+user.toString());
        //String pass = "123456";
        //String md5Password = DigestUtils.md5DigestAsHex(pass.getBytes());

        doUploadUserInfo(user);
    }



    /**
     * 模拟表单上传文件：通过MultipartBody UserInfo
     */
    public void ToastUtil(String content) {
        Toast.makeText(this,content, Toast.LENGTH_SHORT).show();
    }
    private void doUploadUserInfo(User user) {
        //1.获取OkHttpClient对象
        config = new Config();
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造RequestBody
        FormBody body = new FormBody.Builder().add("user", JSON.toJSONString(user)).build();
        Request request = new Request.Builder().url(config.getPath()+"api/reg").post(body).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: " + e);
                //ToastUtil("failure");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("TAG", "onResponse: " + result);
                //ToastUtil("failure");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil("success");
                    }
                });
            }
        });
    }

    /**
     * 将bitmap处理为圆形
     * @author Stanny
     *
     * 2015年9月28日
     */
    public class BitmapToRound_Util {
        public Bitmap toRoundBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
            if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
            } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
            }
            Bitmap output = Bitmap.createBitmap(width,
                    height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
            final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
            final RectF rectF = new RectF(dst);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output;
        }
    }



    /**
     * 模拟表单上传文件：通过MultipartBody
     */
    private void doUploadImg(File file) {
        //File file = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/iv_500x400.png");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //1.获取OkHttpClient对象
        config = new Config();
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "test.jpg", fileBody)
                .build();
        Request request = new Request.Builder()
                .url(config.getPath()+"uploadImage")
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
                Log.e("Reg Img", "onResponse: " + result);
                //runOnUiThread(() -> ToastUtil.showAtOnce(MainActivity.this, result));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(flag == 1)
                            logo.setText(result);
                        else if(flag == 2)
                            head_pic.setText(result);
                    }
                });
            }
        });
    }
}
