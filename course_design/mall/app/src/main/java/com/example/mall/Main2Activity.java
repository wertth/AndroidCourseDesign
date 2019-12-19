package com.example.mall;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView mTextMessage;
    private BottomNavigationView navigationView;
    private Fragment headFragment;
    private Fragment orderFragment;
    private Fragment userFragment;
    private Fragment[] fragmentlist;
    //用于标识上一个fragment
    private int lastFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//                case R.id.navigation_me:
//                    mTextMessage.setText("me");
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bnv_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initFragment();
    }

    private void initFragment() {
        navigationView = (BottomNavigationView) findViewById(R.id.bnv_main);
        //配置菜单按钮显示图标
        navigationView.setItemIconTintList(null);
        //将三个fragment先放在数组里
        headFragment = new IndexFragment();
        orderFragment = new CartFragment();
        userFragment = new FenleiFragment();
        fragmentlist = new Fragment[]{headFragment, orderFragment, userFragment};
        //此时标识标识首页
        //0表示首页，1表示orderFragment，2表示userFragment
        lastFragment = 0;
        //设置默认页面为headFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, headFragment)
                .show(headFragment).commit();
        navigationView.setSelectedItemId(R.id.navigation_home);
        //给BottomNavigation添加按钮的点击事件
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                resetToDefaultIcon();
                switch (Item.getItemId()) {
                    case R.id.navigation_home:
                        //判断要跳转的页面是否是当前页面，若是则不做动作
                        if (lastFragment != 0) {
                            switchFragment(lastFragment, 0);
                            lastFragment = 0;
                        }
                        Item.setIcon(R.drawable.ic_home_black_24dp);
                        return true;
                    case R.id.navigation_dashboard:
                        if (lastFragment != 1) {
                            switchFragment(lastFragment, 1);
                            lastFragment = 1;
                        }
                        Item.setIcon(R.drawable.ic_dashboard_black_24dp);
                        return true;
                    case R.id.navigation_notifications:
                        if (lastFragment != 2) {
                            switchFragment(lastFragment, 2);
                            lastFragment = 2;
                        }
                        Item.setIcon(R.drawable.ic_notifications_black_24dp);
                        return true;
                }
                return false;
            }
        });
    }

    private void switchFragment(int lastFragment, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个fragment
        transaction.hide(fragmentlist[lastFragment]);

        if (fragmentlist[i].isAdded() == false) {
            transaction.add(R.id.fl_main, fragmentlist[i]);
        }
        transaction.show(fragmentlist[i]).commitAllowingStateLoss();
    }
    //重新配置每个按钮的状态
    private void resetToDefaultIcon() {
        navigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.ic_home_black_24dp);
        navigationView.getMenu().findItem(R.id.navigation_dashboard).setIcon(R.drawable.ic_dashboard_black_24dp);
        navigationView.getMenu().findItem(R.id.navigation_notifications).setIcon(R.drawable.ic_notifications_black_24dp);
    }


}
