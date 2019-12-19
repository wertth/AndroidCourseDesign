package com.example.mall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.mall.activity.*;
import com.example.mall.entity.Goods;
import com.example.mall.recyclerview.MyRecyclerViewAdapter;
import com.example.mall.recyclerview.list_goodsActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.support.constraint.Constraints.TAG;

public class MineFragment extends Fragment {
    private ImageView head_pic;
    private TextView name;
    private Button setting;
    public Button show_order;

    public SimpleDraweeView sdv;
    public TextView shoucang;
    public TextView guanzhu;
    public TextView xihuan;
    public TextView zuji;
    public Button take_photo;
    public Button pro;

    public ProgressBar mProgressBar;

    public List<Goods> data_goods;
    private List<String> datas;
    public MyRecyclerViewAdapter adapter;
    public RecyclerView mine_list;
    public Config config;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        // Inflate the layout for this fragment
        //Log.e("当前类：=======",getActivity().getClass().getSimpleName());

        if (!isLogin()) getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));

        //head_pic = getActivity().findViewById(R.id.head_pic);
        sdv = view.findViewById(R.id.head_pic);
        mine_list = view.findViewById(R.id.mine_list);
        config = new Config();
        initData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Mine =======", "onStart: ");
        if (!isLogin()) {
            name = getActivity().findViewById(R.id.name);
            name.setText("未登录");
            Fresco.initialize(getContext());//初始化框架
            Uri uri = Uri.parse("https://cdn.acwing.com/media/user/profile/photo/11975_sm_19a4d6340f.jpeg");
            //Log.e("TAG", "onActivityCreated: head Fresco");
            sdv.setImageURI(uri);
        }
    }

    //    @Override
//    public void onPause() {
//        super.onPause();
//        Log.e("Mine ==========", "onPause: ");
//    }
    @Override
    public void onResume() {
        super.onResume();
        if (isLogin()) {
            name = getActivity().findViewById(R.id.name);
            name.setText(getUsername());
            Fresco.initialize(getContext());//初始化框架
            Uri uri = Uri.parse(getLogo());
            Log.e("TAG", "onActivityCreated: head Fresco");
            sdv.setImageURI(uri);
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Fresco.initialize(getContext());//初始化框架
        //Uri uri = Uri.parse(getLogo());
        //Log.e("TAG", "onActivityCreated: head Fresco");
        //sdv.setImageURI(uri);


        name = getActivity().findViewById(R.id.name);
        //name.setText("hello");
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().equals("未登录")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
        if (!isLogin()) {
            name = getActivity().findViewById(R.id.name);
            name.setText("未登录");
        }
        setting = getActivity().findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), settingActivity.class));
            }
        });
        show_order = getActivity().findViewById(R.id.show_order);
        show_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Order_his.class));
            }
        });
        take_photo = getActivity().findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TakePhoto.class));
            }
        });
        shoucang = getActivity().findViewById(R.id.shoucang);
        shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CollectActivity.class));
            }
        });
        pro = getActivity().findViewById(R.id.pro);
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //代码中控制显隐藏
                mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progress_bar_main);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        guanzhu = getActivity().findViewById(R.id.guanzhu);
        guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CollectActivity.class));
            }
        });
        xihuan = getActivity().findViewById(R.id.xihuan);
        xihuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CollectActivity.class));
            }
        });
        zuji = getActivity().findViewById(R.id.zuji);
        zuji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Order_his.class));
            }
        });
    }

    public boolean isLogin() {
        SharedPreferences sp = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        String data = sp.getString("isLogin", "");
        Log.e("isLogin() ===  ", data);
        if (data.equals("true")) return true;
        else return false;
    }

    public String getUsername() {
        SharedPreferences sp = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        return sp.getString("username", "");
    }

    public String getLogo() {
        SharedPreferences sp = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        return sp.getString("logo", "");
    }

    // okhttp
    private void doGetIndexGoods() {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
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
    public void initRecylerView(final List<Goods> data_goods){



        adapter = new MyRecyclerViewAdapter(getActivity(), data_goods,true);
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

        mine_list.setAdapter(adapter);

        mine_list.setNestedScrollingEnabled(false);

        //mine_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        mine_list.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
    }


}
