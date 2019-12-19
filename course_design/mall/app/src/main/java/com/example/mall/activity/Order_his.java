package com.example.mall.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.mall.Config;
import com.example.mall.R;
import com.example.mall.entity.Goods;
import com.example.mall.entity.Order;
import com.example.mall.recyclerview.MyRecyclerViewAdapter;
import com.example.mall.recyclerview.list_goodsActivity;
import okhttp3.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class Order_his extends AppCompatActivity {

    public Config config;
    public RecyclerView order_list;
    public MyRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_his);

        order_list = findViewById(R.id.order_list);


        config = new Config();
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        String name = sp.getString("username","");
        doGetSearchGoods(name);

    }



    // okhttp
    //
    private void doGetSearchGoods(String goods_name) {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        Request request = new Request
                .Builder()
                .get()
                .url(config.getPath() + "api/getOrder/" + goods_name)
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
                final String data = response.body().string();
                JSONArray res = JSON.parseArray(data);

                final List<Goods> orders = res.toJavaList(Goods.class);
                Log.e("hello index =======", orders.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.reverse(orders);
                        initRecyclerView(orders, false);
                        //data_goods = goods;
                    }
                });
            }
        });
    }
    public void initRecyclerView(final List<Goods> goods,boolean flag){
        adapter = new MyRecyclerViewAdapter(Order_his.this, goods,flag);
        // 为recyclerview设置点击事件
//        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(Order_his.this, "click " + goods.get(position).getGoodsid(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Order_his.this, GoodsInfoActivity.class);
//                intent.putExtra("goods",goods.get(position));
//                intent.putExtra("goodsid",goods.get(position).getGoodsid());
//                startActivity(intent);
//            }
//        });
//
//
//        adapter.setOnItemLongClickListener(new MyRecyclerViewAdapter.OnItemLongClickListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(Order_his.this, "long click " + position, Toast.LENGTH_SHORT).show();
//            }
//        });

        order_list.setAdapter(adapter);

        order_list.setNestedScrollingEnabled(false);
        //if(flag == false)
        order_list.setLayoutManager(new LinearLayoutManager(Order_his.this, LinearLayoutManager.VERTICAL,false));
        //else list_goods.setLayoutManager(new GridLayoutManager(list_goodsActivity.this, 2, GridLayoutManager.VERTICAL, false));
        //if(flag == false) this.flag = true;
        //else this.flag = false;

    }
}
