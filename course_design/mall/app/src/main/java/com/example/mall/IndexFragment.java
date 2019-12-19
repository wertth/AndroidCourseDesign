package com.example.mall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.example.mall.activity.GoodsInfoActivity;
import com.example.mall.activity.SaoActivity;
import com.example.mall.activity.searchActivity;
import com.example.mall.entity.Goods;
import com.example.mall.recyclerview.MyRecyclerViewAdapter;
import com.example.mall.recyclerview.list_goodsActivity;
import com.hjm.bottomtabbar.BottomTabBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.support.constraint.Constraints.TAG;

public class IndexFragment extends Fragment {

    private List<String> images;
    List<String> title;
    private Button saoyisao;
    private Button msg;
    public EditText input;

    private MyRecyclerViewAdapter adapter;
    public ArrayList<String> datas;
    private RecyclerView list_goods;

    public List<Goods> data_goods;
    public List<Goods> goods_test;
    public Config config;

    //String path = "10.153.17.211";


    BottomTabBar bottomTabBar;
    private EditText editText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index,container,false);
        // Inflate the layout for this fragment

        images = new ArrayList<>();
        images.add("http://m.360buyimg.com/mobilecms/s700x280_jfs/t1/91904/4/5610/85676/5dee1f04Ed3d26fa1/0254ed837575417b.jpg!cr_1125x445_0_171!q70.jpg");
        images.add("http://gw.alicdn.com/imgextra/i2/139/O1CN01zvj9wQ1Cthd6QPM1R_!!139-0-lubanu.jpg");
        images.add("http://gw.alicdn.com/imgextra/i2/150/O1CN01aBsloh1CyjzDtMmjR_!!150-0-lubanu.jpg");

        //editText = getActivity().findViewById(R.id.input);


        config = new Config();

        Banner banner = view.findViewById(R.id.banner_home);

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
//        banner.setOnBannerListener()
        banner.start();

        list_goods = view.findViewById(R.id.index_list);
        initData();
//        list_goods = view.findViewById(R.id.index_list);
//
//
//        adapter = new MyRecyclerViewAdapter(getActivity(), data_goods);
//        // 为recyclerview设置点击事件
//        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(getContext(), "click " + data_goods.get(position).getGoodsid(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        adapter.setOnItemLongClickListener(new MyRecyclerViewAdapter.OnItemLongClickListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(getContext(), "long click " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        list_goods.setAdapter(adapter);
//
//        list_goods.setNestedScrollingEnabled(false);
//
//        list_goods.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));



        msg = view.findViewById(R.id.msg);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), searchActivity.class));
                //doGet();
            }
        });

        input = view.findViewById(R.id.input);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), searchActivity.class));
            }
        });


        return view;

    }
    // okhttp
    private void doGet() {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        Request request = new Request
                .Builder()
                .get()
                .url(config.getPath()+"test")
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
//                Log.e(TAG, "onResponse: " + response.body().string());
                Looper.prepare();

                Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }



    // recyclerview 初始化数据
    public void initData() {
        datas = new ArrayList<>();
        data_goods = new ArrayList<>();
        Random ra =new Random();
        //for(int i=0;i<10;i++) {
        //    Goods goods = new Goods();
        //    goods.setGoodsid(ra.nextLong());
        //    goods.setGoodsname("iphone 11 promax");
        //    goods.setPrice(9999);
        //    data_goods.add(goods);
        //}
        //initRecylerView(data_goods);
        doGetIndexGoods();
        Log.e("goods === ",data_goods.toString());
        for(int i =0 ;i < 20; i++) {
            datas.add("index_content_"+ra.nextInt());
        }
    }

    // okhttp
    private void doGetIndexGoods() {
        //1.获取OkHttpClient对象
//        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .build();
        //2.获取Request对象
        Request request = new Request
                .Builder()
                .get()
                .url(config.getPath()+"getAll")
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

                final List<Goods> goods = res.toJavaList(Goods.class);
                Log.e("hello index =======",goods.toString());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecylerView(goods);
                    }
                });
            }
        });
    }
    public void initRecylerView(final List<Goods> data_goods){



        adapter = new MyRecyclerViewAdapter(getActivity(), data_goods,false);
        // 为recyclerview设置点击事件
        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "click " + data_goods.get(position).getGoodsid(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), GoodsInfoActivity.class);
                intent.putExtra("goods",data_goods.get(position));
                intent.putExtra("goodsid",data_goods.get(position).getGoodsid());
                startActivity(intent);

            }
        });


        adapter.setOnItemLongClickListener(new MyRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });

        list_goods.setAdapter(adapter);

        list_goods.setNestedScrollingEnabled(false);

        list_goods.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

    }
    @Override
    public void onResume(){
        super.onResume();
        initData();
    }
    @Override
    public void onPause(){
        super.onPause();
        initData();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        saoyisao = getActivity().findViewById(R.id.saoyisao);
        saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), list_goodsActivity.class));
                startActivity(new Intent(getActivity(), SaoActivity.class));
            }
        });

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