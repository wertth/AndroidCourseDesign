package com.example.mall.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alipay.sdk.app.PayTask;
import com.example.mall.Config;
import com.example.mall.MainActivity;
import com.example.mall.R;
import com.example.mall.alipay.AuthResult;
import com.example.mall.alipay.PayResult;
import com.example.mall.entity.Goods;
import com.example.mall.entity.Order;
import com.example.mall.recyclerview.MyRecyclerViewAdapter;
import com.example.mall.utils.OrderInfoUtil2_0;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class orderActivity extends AppCompatActivity {

    public RecyclerView order_list;
    public MyRecyclerViewAdapter adapter;
    public List<Goods> data_goods;
    public Config config;
    public TextView allPrice;
    public Button btn_buy_order;


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
                        //showAlert(orderActivity.this, getString(R.string.pay_success) + payResult);
                        Toast.makeText(orderActivity.this,"交易成功，跳回主页",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(orderActivity.this, sellSuccessActivity.class));
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showAlert(orderActivity.this, getString(R.string.pay_failed) + payResult);
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
                        showAlert(orderActivity.this, getString(R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(orderActivity.this, getString(R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    public List<Order> order_his_list;
    public String getUsername(){
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        return sp.getString("username","");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_order);
        order_list = findViewById(R.id.order_list);
        allPrice = findViewById(R.id.allPrice);
        btn_buy_order = findViewById(R.id.btn_buy_order);
        btn_buy_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(orderActivity.this,"付款成功",Toast.LENGTH_SHORT).show();

                // 支付宝支付
                payV2();
                insertOrderHis(); // 将订单插入到历史订单
            }
        });

        config = new Config();
        Intent intent = getIntent();
        String str = intent.getStringExtra("goodsid");
        List<Long> goodsid_list  = JSON.parseArray(str,Long.class);
        Log.e("order", "onCreate: "+str );
        Log.e("order2","hello " + goodsid_list);
        initData(goodsid_list);

    }
    public void insertOrderHis(){
        List<Order> orders = new ArrayList<>();
        for(Goods g: data_goods){
            Order order = new Order(getUsername(),g.getGoodsid(),g.getImage1(),g.getGoodsname(),g.getPrice());
            orders.add(order);
        }

        doGetInsertOrder(orders);
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
        //Intent intent = getIntent();
        //Goods goods = (Goods)intent.getSerializableExtra("goods");

        //Integer total_price = goods.getPrice();
        int all_p = 0;
        for(Goods g:data_goods){
            all_p += g.getPrice();
        }
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,all_p);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(orderActivity.this);
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


    void initData(List<Long> list){

        doGetGoodsById(list);
        //initRecyclerView();
    }
    // okhttp

    /**
     * 插入历史订单
     * @param list
     */
    private void doGetInsertOrder(List<Order> list) {

        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造RequestBody
        FormBody body = new FormBody.Builder().add("order", JSON.toJSONString(list)).build();
        Log.e("cart delete", "doGetDeleteCart: "+ JSON.toJSONString(list));
        Request request = new Request.Builder().url(config.getPath()+"api/insertOrder").post(body).build();
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

//                String data = response.body().string();
//                Log.e("Order response data", "onResponse: "+data );
//                JSONArray res = JSON.parseArray(data);
//                final List<Goods> goods = res.toJavaList(Goods.class);
//                Log.e(TAG, "onResponse: "+goods );
//                //ToastUtil("failure");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initRecyclerView(goods);
//                        data_goods = goods;
//                        initPrice(goods);
//                    }
//                });
            }
        });

    }
    // okhttp
    // 获取order list
    private void doGetGoodsById(List<Long> list) {

        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造RequestBody
        FormBody body = new FormBody.Builder().add("list", JSON.toJSONString(list)).build();
        Log.e("cart delete", "doGetDeleteCart: "+ JSON.toJSONString(list));
        Request request = new Request.Builder().url(config.getPath()+"api/getGoodsOrder").post(body).build();
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

                String data = response.body().string();
                Log.e("Order response data", "onResponse: "+data );
                JSONArray res = JSON.parseArray(data);
                final List<Goods> goods = res.toJavaList(Goods.class);
                Log.e(TAG, "onResponse: "+goods );
                //ToastUtil("failure");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView(goods);
                        data_goods = goods;
                        initPrice(goods);
                    }
                });
            }
        });

    }
    void initPrice(List<Goods> goods){
        int all_p = 0;
        for(Goods g:goods){
            all_p += g.getPrice();
        }
        allPrice.setText(Integer.toString(all_p));
    }
    // okhttp
//    private void doGetGoodsById(Long goodsid) {
//        //1.获取OkHttpClient对象
//        OkHttpClient okHttpClient = new OkHttpClient();
//        //2.获取Request对象
//        Request request = new Request
//                .Builder()
//                .get()
//                .url(config.getPath()+"getGoodsById/"+goodsid)
//                .build();
//
//        //3.将Request封装为Call对象
//        Call call = okHttpClient.newCall(request);
//        //4.执行Call
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, "onFailure: " + e);
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                //Log.e(TAG, "onResponse: " + response.body().string());
//                //Looper.prepare();
//                String data = response.body().string();
//
//
//                final Goods goods = JSON.parseObject(data,Goods.class);
//                Log.e("hello index =======",goods.toString());
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //initRecylerView(goods);
//                        //initBanner(goods);
//                        data_goods.add(goods);
//                    }
//                });
//            }
//        });
//    }

    public void initRecyclerView(final List<Goods> goods){
        adapter = new MyRecyclerViewAdapter(this, goods,false);
        // 为recyclerview设置点击事件
        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(orderActivity.this, "click " + goods.get(position).getGoodsid(), Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(this, GoodsInfoActivity.class);
                //intent.putExtra("goods",data_goods.get(position));
                //intent.putExtra("goodsid",data_goods.get(position).getGoodsid());
                //startActivity(intent);

            }
        });


        adapter.setOnItemLongClickListener(new MyRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(orderActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });

        order_list.setAdapter(adapter);

        order_list.setNestedScrollingEnabled(false);

        order_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));


    }
}
