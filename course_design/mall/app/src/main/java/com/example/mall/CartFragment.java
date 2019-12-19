package com.example.mall;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.mall.activity.LoginActivity;
import com.example.mall.activity.orderActivity;
import com.example.mall.alipay.AuthResult;
import com.example.mall.alipay.PayResult;
import com.example.mall.entity.Cart;
import com.example.mall.entity.Goods;
import com.example.mall.recyclerview.CartRecyclerAdapter;
import com.example.mall.utils.OrderInfoUtil2_0;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.example.mall.Config;

import static android.support.constraint.Constraints.TAG;


public class CartFragment extends Fragment implements CartRecyclerAdapter.OnCUpdateListener {
    public RecyclerView cart_list;
    public CartRecyclerAdapter adapter;
    public List<Goods> cart_goods;
    public TextView tv_total_price;
    public TextView tv_go_to_pay;
    //public String path = "http://192.168.1.176/";
    public List<Long> selected_goods;

    public Config config;
    public TextView tv_delete;
    //public RecyclerView cart_list;
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
                        showAlert(getContext(), getString(R.string.pay_success) + payResult);
                        startActivity(new Intent(getActivity(), LoginActivity.class));

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showAlert(getContext(), getString(R.string.pay_failed) + payResult);
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
                        showAlert(getContext(), getString(R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(getContext(), getString(R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    /**
     * 支付宝支付业务示例
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            showAlert(getContext(), getString(R.string.error_missing_appid_rsa_private));
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
        //Integer total_price = intent.getIntExtra("total_price",100);
        Integer total_price = 116;
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,total_price);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        View view = inflater.inflate(R.layout.fragment_cart,container,false);
        cart_list = view.findViewById(R.id.cart_list);
        tv_total_price = view.findViewById(R.id.tv_total_price);
        tv_go_to_pay = view.findViewById(R.id.tv_go_to_pay);
        tv_delete = view.findViewById(R.id.tv_delete);

        tv_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                delete_goods();
                doGetCart();
            }
        });

        config = new Config();
        //init_cart_data();
        doGetCart();

//        adapter = new CartRecyclerAdapter(getContext(),cart_goods);
//        // 观察者模式
//        adapter.setOnCUpdateListener(this);
//
//        cart_list.setAdapter(adapter);
//
//        cart_list.setNestedScrollingEnabled(false);
//
//        cart_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
//        // 为recyclerview设置点击事件
//        adapter.setOnItemClickListener(new CartRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(getContext(), "click " + cart_goods.get(position).getGoodsname(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        adapter.setOnItemLongClickListener(new CartRecyclerAdapter.OnItemLongClickListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(getContext(), "long click " + position, Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onActivityCreated(savedInstanceState);
        doGetCart();
        tv_go_to_pay = getActivity().findViewById(R.id.tv_go_to_pay);
        tv_go_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"支付成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), orderActivity.class);
                //goodsid_list.add(goodsid);
                String str = JSON.toJSONString(selected_goods);
                intent.putExtra("goodsid",str);
                startActivity(intent);
                //payV2();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        doGetCart();
    }

    /**
     * 从数据库获取数据 get请求
    *
     */
    // okhttp
    private void doGetCart() {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        Request request = new Request
                .Builder()
                .get()
                .url(config.getPath()+"api/getCartAll")
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
                JSONArray res = JSON.parseArray(data);

                final List<Cart> cart = res.toJavaList(Cart.class);
                Log.e("hello index =======",cart.toString());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView(cart);
                    }
                });
            }
        });
    }

    //public void init_cart_data(){
    //    cart_goods = new ArrayList<>();
    //    Random ra = new Random();
    //    for(int i =0 ;i<10;i++){
    //        Goods goods = new Goods();
    //        goods.setPrice(i*100);
    //        goods.setGoodsid(ra.nextLong());
    //        goods.setGoodsname("Hello_Cart_"+i);
    //        goods.setImage1("http://donglei.fun/upload/2019/11/2652923a686233cef2c1a3175e723177%20(2)-daf229deadcc4b4eb0e3313bee18a179.jpeg");
    //        cart_goods.add(goods);
    //    }
    //}

    /**
     * 初始化
     * @param cart_goods
     */
    void initRecyclerView(final List<Cart> cart_goods){
        adapter = new CartRecyclerAdapter(getContext(),cart_goods);
        // 观察者模式
        adapter.setOnCUpdateListener(this);

        cart_list.setAdapter(adapter);

        cart_list.setNestedScrollingEnabled(false);

        cart_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        // 为recyclerview设置点击事件
        adapter.setOnItemClickListener(new CartRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "click " + cart_goods.get(position).getGoodsname(), Toast.LENGTH_SHORT).show();
            }
        });


        adapter.setOnItemLongClickListener(new CartRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 观察者，监听数据
     * @param all_p
     * @param selected_goodsid
     */
    @Override
    public void onCUpdate(int all_p,List<Long> selected_goodsid) {
        selected_goods = selected_goodsid;
        tv_total_price.setText(all_p+"");
        tv_go_to_pay.setText("结算("+selected_goodsid.size()+")");
        if(selected_goodsid.size()!=0)
            Log.e("观察者", "onCUpdate: 观察者  " + all_p+ " == "+selected_goodsid.get(0));
        else Log.e("观察者", "onCUpdate: 观察者  " + all_p);
        Toast.makeText(getContext(),"观察者",Toast.LENGTH_LONG).show();
    }

    public void delete_goods(){
        doGetDeleteCart(selected_goods);
    }
    // okhttp
    private void doGetDeleteCart(List<Long> cart) {


        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造RequestBody
        FormBody body = new FormBody.Builder().add("cart", JSON.toJSONString(cart)).build();
        Log.e("cart delete", "doGetDeleteCart: "+ JSON.toJSONString(cart));
        Request request = new Request.Builder().url(config.getPath()+"api/deleteCart").post(body).build();
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        doGetCart();
                        setPrice();
                        ToastUtil("success");
                    }
                });
            }
        });

    }
    public void setPrice(){
        tv_total_price.setText(0+"");
    }
    public void ToastUtil(String content) {
        Toast.makeText(getContext(),content, Toast.LENGTH_SHORT).show();
    }

}