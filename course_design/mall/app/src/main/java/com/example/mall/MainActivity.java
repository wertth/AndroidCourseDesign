package com.example.mall;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import android.view.WindowManager;
import android.widget.Button;
import com.example.mall.activity.settingActivity;
import com.hjm.bottomtabbar.BottomTabBar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> images;
    List<String> title;
    private Button setting;
    BottomTabBar bottomTabBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowStatusBarColor(this,R.color.Home_top);


        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        initBottom();


    }


    public void initBottom()
    {
        bottomTabBar = findViewById(R.id.bottom_tab_bar);
        //初始化Fragment
        bottomTabBar.init(getSupportFragmentManager())
                .setImgSize(50, 50)   //图片大小
                .setFontSize(12)            //字体大小
                .setTabPadding(4, 6, 10)//选项卡的间距
                .setChangeColor(getResources().getColor(R.color.colorAccent), Color.GRAY)   //选项卡的选择颜色
                .addTabItem("首页", R.drawable.index, IndexFragment.class)
                .addTabItem("分类", R.drawable.fen_lei, FenleiFragment.class)
                .addTabItem("购物车", R.drawable.cart, CartFragment.class)
                .addTabItem("我的",R.drawable.mine, MineFragment.class)
                .isShowDivider(true)  //是否包含分割线
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name) {
                        Log.i("TGA", "位置：" + position + "   选项卡：" + name);
                    }
                });
    }
    // 设置状态栏颜色
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                //顶部状态栏
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}