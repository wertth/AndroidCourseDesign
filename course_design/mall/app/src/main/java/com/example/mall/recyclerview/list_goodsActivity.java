package com.example.mall.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.mall.Config;
import com.example.mall.R;
import com.example.mall.activity.GoodsInfoActivity;
import com.example.mall.activity.searchActivity;
import com.example.mall.entity.Goods;
import okhttp3.*;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.support.constraint.Constraints.TAG;
import static com.example.mall.MainActivity.setWindowStatusBarColor;

public class list_goodsActivity extends AppCompatActivity implements View.OnClickListener{
    private MyRecyclerViewAdapter adapter;
    public ArrayList<Goods> datas;
    private RecyclerView list_goods;
    private Button change_ly;
    public Button list_goods_fanhui;
    public Config config;
    public List<Goods> data_goods;

    public EditText list_goods_search_input;
    public TextView list_goods_search;


    public boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowStatusBarColor(this,R.color.Home_top);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list_goods);
        //initData();
        list_goods = findViewById(R.id.list_goods);
        change_ly = findViewById(R.id.btn_change_ly);
        list_goods_fanhui = findViewById(R.id.list_goods_fanhui);
        list_goods_fanhui.setOnClickListener(this);

        list_goods_search_input = findViewById(R.id.list_goods_search_input);
        list_goods_search = findViewById(R.id.list_goods_search);
        list_goods_search_input.setOnClickListener(this);
        list_goods_search.setOnClickListener(this);


        config = new Config();



        change_ly.setOnClickListener(this);

        Intent intent  = getIntent();
        String goods_name = intent.getStringExtra("con");
        list_goods_search_input.setText(goods_name);
        doGetSearchGoods(goods_name);
    }
    // okhttp
    // 搜索 模糊查询
    private void doGetSearchGoods(String goods_name) {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        Request request = new Request
                .Builder()
                .get()
                .url(config.getPath()+"getGoodsByName/"+goods_name)
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

                final List<Goods> goods = res.toJavaList(Goods.class);
                Log.e("hello index =======",goods.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView(goods,false);
                        data_goods = goods;
                    }
                });
            }
        });
    }
    public void initRecyclerView(final List<Goods> goods,boolean flag){
        adapter = new MyRecyclerViewAdapter(this, goods,flag);
        // 为recyclerview设置点击事件
        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(list_goodsActivity.this, "click " + goods.get(position).getGoodsid(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(list_goodsActivity.this, GoodsInfoActivity.class);
                intent.putExtra("goods",goods.get(position));
                intent.putExtra("goodsid",goods.get(position).getGoodsid());
                startActivity(intent);

            }
        });


        adapter.setOnItemLongClickListener(new MyRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(list_goodsActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });

        list_goods.setAdapter(adapter);

        list_goods.setNestedScrollingEnabled(false);
        if(flag == false)
            list_goods.setLayoutManager(new LinearLayoutManager(list_goodsActivity.this, LinearLayoutManager.VERTICAL,false));
        else list_goods.setLayoutManager(new GridLayoutManager(list_goodsActivity.this, 2, GridLayoutManager.VERTICAL, false));
        if(flag == false) this.flag = true;
        else this.flag = false;

    }
    public void initData() {
        Random ra = new Random();
        datas = new ArrayList<>();
        for(int i =0 ;i < 100; i++) {
            Goods goods = new Goods();
            goods.setGoodsid(ra.nextLong());
            goods.setGoodsname("content_goods_"+i);
            goods.setImage1("http://donglei.fun/upload/2019/11/2652923a686233cef2c1a3175e723177%20(2)-daf229deadcc4b4eb0e3313bee18a179.jpeg");
            goods.setPrice(9999);
            datas.add(goods);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_change_ly:
                if(flag == false) {
                    //flag = true;
                    initRecyclerView(data_goods,false);
                    //list_goods.setLayoutManager(new GridLayoutManager(list_goodsActivity.this, 2, GridLayoutManager.VERTICAL, false));
                }else {
                    //flag = false;
                    initRecyclerView(data_goods, true);
                    //list_goods.setLayoutManager(new LinearLayoutManager(list_goodsActivity.this, LinearLayoutManager.VERTICAL,false));
                }
                break;
            case R.id.list_goods_fanhui:
                finish();
                break;
            case R.id.list_goods_search_input:
                startActivity(new Intent(this, searchActivity.class));
                finish();
                break;
            case R.id.list_goods_search:
                startActivity(new Intent(this, searchActivity.class));
                finish();
                break;
        }
    }
}
