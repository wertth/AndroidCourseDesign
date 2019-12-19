package com.example.mall.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.example.mall.Config;
import com.example.mall.IndexFragment;
import com.example.mall.R;
import com.example.mall.alipay.AuthResult;
import com.example.mall.alipay.PayDemoActivity;
import com.example.mall.alipay.PayResult;
import com.example.mall.entity.*;
import com.example.mall.utils.OrderInfoUtil2_0;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;
import static com.example.mall.MainActivity.setWindowStatusBarColor;

public class GoodsInfoActivity extends AppCompatActivity implements View.OnClickListener{

    public List<String> images;
    public WebView webView;
    public Button btn_buy;
    public TextView info_title;
    public TextView info_price;
    public Button btn_shoucang;
    public Button add_to_cart;
    public Config config;

    public ScrollView scroll_test;
    public TextView address_info;
    public TextView sell_num;

//    public FloatingActionButton floating_btn_main;

    //String path = "http://192.168.1.176/";


    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "******";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "******";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "******";

    /**
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "******";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private static void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }
    private static void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton(R.string.confirm, null)
                .setOnDismissListener(onDismiss)
                .show();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showAlert(GoodsInfoActivity.this, getString(R.string.pay_success) + payResult);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showAlert(GoodsInfoActivity.this, getString(R.string.pay_failed) + payResult);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        showAlert(GoodsInfoActivity.this, getString(R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(GoodsInfoActivity.this, getString(R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        //setWindowStatusBarColor(this, R.color.goods_info);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //config = new Config();

        Intent intent = getIntent();
        Long goodsid = intent.getLongExtra("goodsid",111L);
        Log.e("GoodsInfo", "onCreate: goodsid = "+goodsid );
        Goods goods = (Goods)intent.getSerializableExtra("goods");
        initBanner(goods);
        initWebview(goods.getDescription());
        btn_buy = findViewById(R.id.btn_buy);
        btn_buy.setOnClickListener(this);

        btn_shoucang = findViewById(R.id.btn_shoucang);
        btn_shoucang.setOnClickListener(this);

        info_price = findViewById(R.id.info_price);
        info_title = findViewById(R.id.info_title);
        add_to_cart = findViewById(R.id.add_to_cart);
//        floating_btn_main = findViewById(R.id.floating_btn_main);
//        floating_btn_main.setOnClickListener(this);
        scroll_test = findViewById(R.id.scroll_test);
        address_info = findViewById(R.id.address_info);
        address_info.setText(goods.getAddress());
        sell_num = findViewById(R.id.sell_num);
        sell_num.setText(Integer.toString(goods.getSellnums()));

        info_title.setText(goods.getGoodsname());
        info_price.setText(Integer.toString(goods.getPrice()));
        add_to_cart.setOnClickListener(this);

        //doGetGoodsById(goodsid);
        //initWebview(goo);
    }
    public void initWebview(String content){
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        //String content = "<img width=\"100%\" src=\"http://img.alicdn.com/imgextra/i4/263726286/O1CN01T3aunI1wJ2FNO21k3_!!263726286.jpg_2200x2200Q90s50.jpg_.webp\" alt=\"\" /><img width=\"100%\" src=\"http://img.alicdn.com/imgextra/i2/263726286/O1CN01FbmEvh1wJ2FMMuxJs_!!263726286.jpg_2200x2200Q90s50.jpg_.webp\" alt=\"\" /><img width=\"100%\" src=\"http://img.alicdn.com/imgextra/i4/263726286/O1CN01ZyuQSU1wJ2FHGEjE2_!!263726286.jpg_2200x2200Q90s50.jpg_.webp\" alt=\"\" />";

        // 开启硬件加速
        if (webView.isHardwareAccelerated()) webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);

        WebSettings ws = webView.getSettings();
        ws.setUserAgentString("56renapp1234321");
        ws.setJavaScriptEnabled(false);
        ws.setAllowFileAccess(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(false);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setDefaultTextEncodingName("utf-8"); //设置文本编码
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式</span>

        content = content.replaceAll("&amp;", "");
        content = content.replaceAll("&quot;", "\"");
        content = content.replaceAll("&lt;", "<");
        content = content.replaceAll("&gt;", ">");
        content = content.replaceAll("&nbsp;", "");
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示

        content = content.replaceAll("width=\"\\d+\"", "width=\"100%\"").replaceAll("height=\"\\d+\"", "height=\"auto\"");

        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

    }
    // okhttp
    private void doGetGoodsById(Long goodsid) {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        Request request = new Request
                .Builder()
                .get()
                .url(config.getPath()+"getGoodsById/"+goodsid)
                .build();

        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.e(TAG, "onResponse: " + response.body().string());
                //Looper.prepare();
                String data = response.body().string();


                final Goods goods = JSON.parseObject(data,Goods.class);
                Log.e("hello index =======",goods.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //initRecylerView(goods);
                        initBanner(goods);
                    }
                });
            }
        });
    }
    public void initBanner(Goods goods){
        images = new ArrayList<>();
        images.add(goods.getImage1());
        if(!TextUtils.isEmpty(goods.getImage2()))
            images.add(goods.getImage2());
        if(!TextUtils.isEmpty(goods.getImage3()))
            images.add(goods.getImage3());
        if(!TextUtils.isEmpty(goods.getImage4()))
            images.add(goods.getImage4());
        if(!TextUtils.isEmpty(goods.getImage5()))
            images.add(goods.getImage5());

        Banner banner = findViewById(R.id.banner_info);

        //设置banner样式
        //banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        //banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(title);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_buy:
                Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
                //payV2();
                // 添加历史订单
                Intent intent = new Intent(this, orderActivity.class);
                List<Long> goodsid_list = new ArrayList<>();
                Intent intent1 = getIntent();
                Long goodsid = intent1.getLongExtra("goodsid",111L);
                goodsid_list.add(goodsid);
                String str = JSON.toJSONString(goodsid_list);
                intent.putExtra("goodsid",str);
                startActivity(intent);
                break;
            case R.id.btn_shoucang:
                setAdd_to_cart();
                break;
            case R.id.add_to_cart:
                add_to_cart();
                break;
//            case R.id.floating_btn_main:
//                scroll_test.fullScroll(View.FOCUS_UP);
//                break;
        }
    }
    boolean add_to_cart_f = false;
    public void setAdd_to_cart(){
        if(!add_to_cart_f){
            btn_shoucang.setBackgroundResource(R.drawable.shoucang_red);
            Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
            add_to_cart_f = true;
            Intent intent = getIntent();
            Goods goods = (Goods)intent.getSerializableExtra("goods");
            Shoucang shoucang = new Shoucang();
            shoucang.setGoodsid(goods.getGoodsid());
            shoucang.setUsername(getSharedPreferences("data",MODE_PRIVATE).getString("username",""));
            shoucang.setGoodsimage(goods.getImage1());
            shoucang.setGoodsprice(goods.getPrice());
            shoucang.setGoodsname(goods.getGoodsname());
            Log.e(TAG, "setAdd_to_cart: "+shoucang );

            doGetAddCollect(shoucang);

        }else{
            btn_shoucang.setBackgroundResource(R.drawable.shoucang);
//            Toast.makeText(this,"取消收藏",Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            doGetDeleteCollect(intent.getLongExtra("goodsid",111L));
            add_to_cart_f = false;
        }
    }
    /**
     * 取消收藏
     * @param
     */
    private void doGetDeleteCollect(Long id) {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        config = new Config();
        //2.获取Request对象
        Request request = new Request.Builder().get().url(config.getPath()+"deleteCollect/"+id).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil("取消收藏成功");
                    }
                });
            }
        });
    }

    // okhttp
    // 添加收藏
    /**
     * 插入历史订单
     * @param list
     */
    private void doGetAddCollect(Shoucang list) {

        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造RequestBody
        config = new Config();
        FormBody body = new FormBody.Builder().add("shoucang", JSON.toJSONString(list)).build();
        Request request = new Request.Builder().url(config.getPath()+"addCollect").post(body).build();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil("添加收藏成功");
                    }
                });
            }
        });

    }


    public void add_to_cart(){

        Intent intent = getIntent();
        Goods goods = (Goods)intent.getSerializableExtra("goods");
        Cart cart = new Cart();

        cart.setId(Calendar.getInstance().getTimeInMillis());
        cart.setGoodsid(goods.getGoodsid());
        cart.setGoodsname(goods.getGoodsname());
        cart.setImage1(goods.getImage1());
        cart.setPrice(goods.getPrice());
        Log.e("add_to_cart: ", "add_to_cart: "+cart.toString());

        doUploadCart(cart);


    }
    private void doUploadCart(Cart cart) {

        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造RequestBody
        config = new Config();

        FormBody body = new FormBody.Builder().add("cart", JSON.toJSONString(cart)).build();
        Request request = new Request.Builder().url(config.getPath()+"api/addCart").post(body).build();
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

    public void ToastUtil(String content) {
        Toast.makeText(this,content, Toast.LENGTH_SHORT).show();
    }
    /**
     * 支付宝支付业务示例
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            showAlert(this, getString(R.string.error_missing_appid_rsa_private));
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
        // 构造total_price
        Intent intent = getIntent();
        Goods goods = (Goods)intent.getSerializableExtra("goods");

        Integer total_price = goods.getPrice();
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,total_price);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(GoodsInfoActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //图片加载器
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
            //imageView.setImageResource((Integer)path);
        }
        @Override
        public ImageView createImageView(Context context) {
            //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
            return new ImageView(context);
        }
    }
}
