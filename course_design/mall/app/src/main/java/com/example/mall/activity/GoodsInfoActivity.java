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
     * ???????????????????????????????????? app_id???
     */
    public static final String APPID = "******";

    /**
     * ???????????????????????????????????????????????? pid???
     */
    public static final String PID = "******";

    /**
     * ???????????????????????????????????????????????? target_id???
     */
    public static final String TARGET_ID = "******";

    /**
     *  pkcs8 ????????????????????????
     *
     * 	???????????????RSA2_PRIVATE ?????? RSA_PRIVATE ?????????????????????????????????????????????????????? Demo ?????????
     * 	?????? RSA2_PRIVATE???RSA2_PRIVATE ??????????????????????????????????????????????????????????????????????????????
     * 	RSA2_PRIVATE???
     *
     * 	?????????????????????????????????????????????????????????????????? RSA2_PRIVATE???
     * 	???????????????https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
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
                     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                     */
                    String resultInfo = payResult.getResult();// ?????????????????????????????????
                    String resultStatus = payResult.getResultStatus();
                    // ??????resultStatus ???9000?????????????????????
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // ??????????????????????????????????????????????????????????????????????????????
                        showAlert(GoodsInfoActivity.this, getString(R.string.pay_success) + payResult);
                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                        showAlert(GoodsInfoActivity.this, getString(R.string.pay_failed) + payResult);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // ??????resultStatus ??????9000??????result_code
                    // ??????200?????????????????????????????????????????????????????????????????????????????????
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // ??????alipay_open_id???????????????????????????extern_token ???value
                        // ??????????????????????????????????????????
                        showAlert(GoodsInfoActivity.this, getString(R.string.auth_success) + authResult);
                    } else {
                        // ?????????????????????????????????
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

        // ??????????????????
        if (webView.isHardwareAccelerated()) webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);

        WebSettings ws = webView.getSettings();
        ws.setUserAgentString("56renapp1234321");
        ws.setJavaScriptEnabled(false);
        ws.setAllowFileAccess(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(false);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setDefaultTextEncodingName("utf-8"); //??????????????????
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);//??????????????????</span>

        content = content.replaceAll("&amp;", "");
        content = content.replaceAll("&quot;", "\"");
        content = content.replaceAll("&lt;", "<");
        content = content.replaceAll("&gt;", ">");
        content = content.replaceAll("&nbsp;", "");
        webView.setHorizontalScrollBarEnabled(false);//???????????????
        webView.setVerticalScrollBarEnabled(false); //???????????????

        content = content.replaceAll("width=\"\\d+\"", "width=\"100%\"").replaceAll("height=\"\\d+\"", "height=\"auto\"");

        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

    }
    // okhttp
    private void doGetGoodsById(Long goodsid) {
        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????Request??????
        Request request = new Request
                .Builder()
                .get()
                .url(config.getPath()+"getGoodsById/"+goodsid)
                .build();

        //3.???Request?????????Call??????
        Call call = okHttpClient.newCall(request);
        //4.??????Call
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

        //??????banner??????
        //banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //?????????????????????
        banner.setImageLoader(new GlideImageLoader());
        //??????????????????
        banner.setImages(images);
        //??????banner????????????
        //banner.setBannerAnimation(Transformer.DepthPage);
        //????????????????????????banner???????????????title??????
        //banner.setBannerTitles(title);
        //??????????????????????????????true
        banner.isAutoPlay(true);
        //??????????????????
        banner.setDelayTime(3000);
        //???????????????????????????banner???????????????????????????
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner?????????????????????????????????????????????
        banner.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_buy:
                Toast.makeText(this,"????????????",Toast.LENGTH_SHORT).show();
                //payV2();
                // ??????????????????
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
            Toast.makeText(this,"????????????",Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(this,"????????????",Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            doGetDeleteCollect(intent.getLongExtra("goodsid",111L));
            add_to_cart_f = false;
        }
    }
    /**
     * ????????????
     * @param
     */
    private void doGetDeleteCollect(Long id) {
        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        config = new Config();
        //2.??????Request??????
        Request request = new Request.Builder().get().url(config.getPath()+"deleteCollect/"+id).build();
        //3.???Request?????????Call??????
        Call call = okHttpClient.newCall(request);
        //4.??????Call
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
                        ToastUtil("??????????????????");
                    }
                });
            }
        });
    }

    // okhttp
    // ????????????
    /**
     * ??????????????????
     * @param list
     */
    private void doGetAddCollect(Shoucang list) {

        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????RequestBody
        config = new Config();
        FormBody body = new FormBody.Builder().add("shoucang", JSON.toJSONString(list)).build();
        Request request = new Request.Builder().url(config.getPath()+"addCollect").post(body).build();
        //3.???Request?????????Call??????
        Call call = okHttpClient.newCall(request);
        //4.??????Call
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
                        ToastUtil("??????????????????");
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

        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????RequestBody
        config = new Config();

        FormBody body = new FormBody.Builder().add("cart", JSON.toJSONString(cart)).build();
        Request request = new Request.Builder().url(config.getPath()+"api/addCart").post(body).build();
        //3.???Request?????????Call??????
        Call call = okHttpClient.newCall(request);
        //4.??????Call
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
     * ???????????????????????????
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            showAlert(this, getString(R.string.error_missing_appid_rsa_private));
            return;
        }

        /*
         * ????????????????????????????????????????????????????????????????????????????????????Demo?????????????????????????????????????????????
         * ??????App??????privateKey??????????????????????????????????????????????????????????????????????????????
         * ????????????????????????????????????????????????????????????????????????????????????????????????
         *
         * orderInfo ?????????????????????????????????
         */
        // ??????total_price
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

        // ??????????????????
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //???????????????
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
            //imageView.setImageResource((Integer)path);
        }
        @Override
        public ImageView createImageView(Context context) {
            //??????fresco???????????????????????????ImageView?????????????????????????????????????????????????????????????????????ImageView
            return new ImageView(context);
        }
    }
}
