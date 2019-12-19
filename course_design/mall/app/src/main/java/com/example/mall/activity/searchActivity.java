package com.example.mall.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.example.mall.R;
import com.example.mall.SQLiteDatabase.search_history_Helper;
import com.example.mall.entity.Goods;
import com.example.mall.recyclerview.MyRecyclerViewAdapter;
import com.example.mall.recyclerview.SearchHisAdapter;
import com.example.mall.recyclerview.list_goodsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.mall.MainActivity.setWindowStatusBarColor;

public class searchActivity extends AppCompatActivity {

    public Button fanhui;
    public TextView search;
    public EditText input_content;
    public SearchHisAdapter adapter;
    public List<String> datas;
    public RecyclerView search_his;
    public Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowStatusBarColor(this,R.color.Home_top);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        search_his = findViewById(R.id.search_his);
        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_search();
                initData();
                if(datas.size() == 0){
                    btn_delete.setVisibility(View.INVISIBLE);
                }else{
                    btn_delete.setVisibility(View.VISIBLE);
                }
                initRecyclerView(datas);

            }
        });

        initData();
        if(datas.size() == 0){
            btn_delete.setVisibility(View.INVISIBLE);
        }else{
            btn_delete.setVisibility(View.VISIBLE);
        }
        initRecyclerView(datas);

        fanhui = findViewById(R.id.fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        input_content = findViewById(R.id.search_input);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String con = input_content.getText().toString();
                Intent intent = new Intent(searchActivity.this, list_goodsActivity.class);
                intent.putExtra("con",con);
                insert_search(con);
                startActivity(intent);
            }
        });
        // 回车搜索
        input_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //执行搜索方法

                }
                return false;
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        initData();
        if(datas.size() == 0){
            btn_delete.setVisibility(View.INVISIBLE);
        }else{
            btn_delete.setVisibility(View.VISIBLE);
        }
        initRecyclerView(datas);
        //Log.i(TAG, "Main----onResume");

    }
    public void initRecyclerView(final List<String> datas){
        Collections.reverse(datas);
        adapter = new SearchHisAdapter(this, datas);
        // 为recyclerview设置点击事件
        adapter.setOnItemClickListener(new SearchHisAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(searchActivity.this, "click " + datas.get(position), Toast.LENGTH_SHORT).show();
                //String con = input_content.getText().toString();
                String con = datas.get(position);
                Intent intent = new Intent(searchActivity.this, list_goodsActivity.class);
                intent.putExtra("con",con);
                insert_search(con);
                startActivity(intent);
                //Intent intent = new Intent(list_goodsActivity.this, GoodsInfoActivity.class);
                //intent.putExtra("goods",goods.get(position));
                //startActivity(intent);

            }
        });


        adapter.setOnItemLongClickListener(new SearchHisAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                //Toast.makeText(this, "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });

        search_his.setAdapter(adapter);

        search_his.setNestedScrollingEnabled(false);

        search_his.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

    }

    public void initData() {
        datas = new ArrayList<>();
        getSearchHis();
        //Collections.reverse(datas);
    }
    public void getSearchHis(){
        SQLiteOpenHelper helper = new search_history_Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from search_history",new String[]{});
        while(cursor.moveToNext()){
            String desc = cursor.getString(1);
            datas.add(desc);
        }
        //return datas;
    }

    public void insert_search(String desc){
        SQLiteOpenHelper helper = new search_history_Helper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("ch",ch);
        values.put("desc",desc);
        db.insert("search_history",null,values);
        db.close();
    }
    public void delete_search(){
        SQLiteOpenHelper helper = new search_history_Helper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("search_history",null,null);
        db.close();
    }
}
