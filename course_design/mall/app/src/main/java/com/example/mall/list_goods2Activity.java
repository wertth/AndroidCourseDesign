package com.example.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.mall.activity.GoodsInfoActivity;
import com.example.mall.activity.searchActivity;
import com.example.mall.entity.Goods;
import com.example.mall.recyclerview.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.mall.MainActivity.setWindowStatusBarColor;

public class list_goods2Activity extends AppCompatActivity implements View.OnClickListener{
    private MyRecyclerViewAdapter adapter;
    private RecyclerView list_goods;
    private Button change_ly;
    public Button list_goods_fanhui;
    public Config config;

    public EditText list_goods_search_input;
    public TextView list_goods_search;

    List<Goods> goods;


    public boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowStatusBarColor(this,R.color.Home_top);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list_goods2);
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
        String str = intent.getStringExtra("goods");
        JSONArray res = JSON.parseArray(str);

        goods = res.toJavaList(Goods.class);


        initRecyclerView(goods,false);
    }

    public void initRecyclerView(final List<Goods> goods,boolean flag){
        adapter = new MyRecyclerViewAdapter(this, goods,flag);
        // 为recyclerview设置点击事件
        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(list_goods2Activity.this, "click " + goods.get(position).getGoodsid(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(list_goods2Activity.this, GoodsInfoActivity.class);
                intent.putExtra("goods",goods.get(position));
                intent.putExtra("goodsid",goods.get(position).getGoodsid());
                startActivity(intent);

            }
        });


        adapter.setOnItemLongClickListener(new MyRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(list_goods2Activity.this, "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });

        list_goods.setAdapter(adapter);

        list_goods.setNestedScrollingEnabled(false);
        if(flag == false)
            list_goods.setLayoutManager(new LinearLayoutManager(list_goods2Activity.this, LinearLayoutManager.VERTICAL,false));
        else list_goods.setLayoutManager(new GridLayoutManager(list_goods2Activity.this, 2, GridLayoutManager.VERTICAL, false));
        if(flag == false) this.flag = true;
        else this.flag = false;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_change_ly:
                if(flag == false) {
                    //flag = true;
                    initRecyclerView( goods,false);
                    //list_goods.setLayoutManager(new GridLayoutManager(list_goodsActivity.this, 2, GridLayoutManager.VERTICAL, false));
                }else {
                    //flag = false;
                    initRecyclerView( goods, true);
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
