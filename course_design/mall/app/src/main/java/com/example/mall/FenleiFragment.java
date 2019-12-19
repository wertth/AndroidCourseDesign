package com.example.mall;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.mall.adapter.LeftAdapter;
import com.example.mall.adapter.RightAdapter;
import com.example.mall.domain.DrugBean;
import com.example.mall.domain.DrugItemBean;
import com.example.mall.domain.DrugListBean;

import java.util.ArrayList;
import java.util.List;

public class FenleiFragment extends Fragment {
    private RecyclerView mLeftRvRecyclerView;
    private RecyclerView mRightRvRecyclerView;

    private List<DrugBean> drugBeanList;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    private List<DrugListBean> listBeanList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fen_lei,container,false);

        mLeftRvRecyclerView = (RecyclerView) view.findViewById(R.id.main_left_rv);
        mRightRvRecyclerView = (RecyclerView) view.findViewById(R.id.main_right_rv);

        initData();
        leftAdapter=new LeftAdapter(drugBeanList);
        rightAdapter=new RightAdapter(listBeanList, getContext());



        mLeftRvRecyclerView.setAdapter(leftAdapter);
        mRightRvRecyclerView.setAdapter(rightAdapter);

        mLeftRvRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightRvRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mLeftRvRecyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                DrugBean drugBean = drugBeanList.get(i);
                listBeanList.clear();
                listBeanList.addAll(drugBean.getmList());
                leftAdapter.setSelectPos(i);
                leftAdapter.notifyDataSetChanged();
                rightAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });

        return view;
    }
    /**
     * 初始化数据
     */
    private void initData() {
        drugBeanList=new ArrayList<>();
        listBeanList=new ArrayList<>();

        DrugBean d1=new DrugBean();
        d1.setTitle("手机数码");//糖尿病
        DrugBean d2=new DrugBean();
        d2.setTitle("食品生鲜");
        DrugBean d3=new DrugBean();
        d3.setTitle("家用电器");
        DrugBean d4 = new DrugBean();
        d4.setTitle("电脑办公");

        DrugListBean l1=new DrugListBean();

        // 手机数码
        l1.setType("热门品牌"); // 口服药


        DrugListBean l2=new DrugListBean();

        l2.setType("手机通讯");
//热门品牌：小米、苹果、华为、荣耀、vivo、oppo、三星
//	手机通讯：老人机、手机、全面屏手机
//	智能手环：华为、小米、Apple
//	数码相机：索尼、佳能、柯达、松下、尼康

        DrugItemBean b1=new DrugItemBean();
        b1.setName("小米");
        DrugItemBean b2=new DrugItemBean();
        b2.setName("iphone");
        DrugItemBean b3=new DrugItemBean();
        b3.setName("华为");
        DrugItemBean b4=new DrugItemBean();
        b4.setName("荣耀");
        DrugItemBean b5=new DrugItemBean();
        b5.setName("vivo");
        DrugItemBean b6=new DrugItemBean();
        b6.setName("三星");
        DrugItemBean b7=new DrugItemBean();
        b7.setName("oppo");

        List<DrugItemBean> list1=new ArrayList<>();
        List<DrugItemBean> list2=new ArrayList<>();
        list1.add(b1);
        list1.add(b2);
        list1.add(b3);
        list1.add(b4);
        list1.add(b5);
        list1.add(b6);
        list1.add(b7);
//	手机通讯：老人机、手机、全面屏手机

        DrugItemBean b8=new DrugItemBean();
        b8.setName("老人机");
        DrugItemBean b9=new DrugItemBean();
        b9.setName("手机");
        DrugItemBean b10=new DrugItemBean();
        b10.setName("全面屏手机");
        list2.add(b8);
        list2.add(b9);
        list2.add(b10);
        //	智能手环：华为、小米、Apple
        DrugListBean l3=new DrugListBean();
        l3.setType("智能手环");
        List<DrugItemBean> list3=new ArrayList<>();
        DrugItemBean b11=new DrugItemBean();
        b11.setName("华为手环");
        DrugItemBean b12=new DrugItemBean();
        b12.setName("小米手环");
        DrugItemBean b13=new DrugItemBean();
        b13.setName("Apple");
        list3.add(b11);
        list3.add(b12);
        list3.add(b13);
        //	数码相机：索尼、佳能、柯达、松下、尼康
        DrugListBean l4=new DrugListBean();
        l4.setType("数码相机");
        List<DrugItemBean> list4=new ArrayList<>();
        DrugItemBean b14=new DrugItemBean();
        b14.setName("索尼");
        DrugItemBean b15=new DrugItemBean();
        b15.setName("柯达");
        DrugItemBean b16=new DrugItemBean();
        b16.setName("松下");
        DrugItemBean b17=new DrugItemBean();
        b17.setName("尼康");
        list4.add(b14);
        list4.add(b15);
        list4.add(b16);
        list4.add(b17);

        //食品生鲜：
        //新鲜水果：苹果、橙子、百香果、榴莲、火龙果、香蕉、车厘子、迷糊桃
        //油粮调味：米、食用油、方便食品、面、杂粮、花生油

        DrugListBean l5=new DrugListBean();
        l5.setType("新鲜水果");
        List<DrugItemBean> list5=new ArrayList<>();
        DrugItemBean b18=new DrugItemBean();
        b18.setName("苹果");
        DrugItemBean b19=new DrugItemBean();
        b19.setName("橙子");
        DrugItemBean b20=new DrugItemBean();
        b20.setName("百香果");
        DrugItemBean b21=new DrugItemBean();
        b21.setName("榴莲");
        DrugItemBean b22=new DrugItemBean();
        b22.setName("火龙果");
        DrugItemBean b23=new DrugItemBean();
        b23.setName("香蕉");
        DrugItemBean b24=new DrugItemBean();
        b24.setName("车厘子");
        DrugItemBean b25=new DrugItemBean();
        b25.setName("迷糊桃");
        list5.add(b18);
        list5.add(b19);
        list5.add(b20);
        list5.add(b21);
        list5.add(b22);
        list5.add(b23);
        list5.add(b24);
        list5.add(b25);
        //油粮调味：米、食用油、方便食品、面、杂粮、花生油
        DrugListBean l6=new DrugListBean();
        l6.setType("油粮调味");
        List<DrugItemBean> list6=new ArrayList<>();
        DrugItemBean b26=new DrugItemBean();
        b26.setName("米");
        DrugItemBean b27=new DrugItemBean();
        b27.setName("食用油");
        DrugItemBean b28=new DrugItemBean();
        b28.setName("方便食品");
        DrugItemBean b29=new DrugItemBean();
        b29.setName("面");
        DrugItemBean b30=new DrugItemBean();
        b30.setName("杂粮");
        DrugItemBean b31=new DrugItemBean();
        b31.setName("花生油");
        list6.add(b26);
        list6.add(b27);
        list6.add(b28);
        list6.add(b29);
        list6.add(b30);
        list6.add(b31);

//家用电器：
//	厨房小电：电饭煲、电压力锅、热水壶、电磁炉、豆浆机、榨汁机
//	个护健康：电吹风、剃须刀、理发器、
        DrugListBean l7=new DrugListBean();
        l7.setType("厨房小电");
        List<DrugItemBean> list7=new ArrayList<>();
        DrugItemBean b32=new DrugItemBean();
        b32.setName("电饭煲");
        DrugItemBean b33=new DrugItemBean();
        b33.setName("电压力锅");
        DrugItemBean b34=new DrugItemBean();
        b34.setName("热水壶");
        DrugItemBean b35=new DrugItemBean();
        b35.setName("电磁炉");
        DrugItemBean b36=new DrugItemBean();
        b36.setName("豆浆机");
        DrugItemBean b37=new DrugItemBean();
        b37.setName("榨汁机");
        list7.add(b32);
        list7.add(b33);
        list7.add(b34);
        list7.add(b35);
        list7.add(b36);
        list7.add(b37);

        //	个护健康：电吹风、剃须刀、理发器、
        DrugListBean l8=new DrugListBean();
        l8.setType("个护健康");
        List<DrugItemBean> list8=new ArrayList<>();
        DrugItemBean b38=new DrugItemBean();
        b38.setName("电吹风");
        DrugItemBean b39=new DrugItemBean();
        b39.setName("剃须刀");
        DrugItemBean b40=new DrugItemBean();
        b40.setName("理发器");

        list8.add(b38);
        list8.add(b39);
        list8.add(b40);








        //	手机配件: "数据线,耳机,蓝牙耳机,充电器",
        DrugListBean l9=new DrugListBean();
        l9.setType("手机配件");
        List<DrugItemBean> list9=new ArrayList<>();
        DrugItemBean b41=new DrugItemBean();
        b41.setName("数据线");
        DrugItemBean b42=new DrugItemBean();
        b42.setName("耳机");
        DrugItemBean b43=new DrugItemBean();
        b43.setName("蓝牙耳机");
        DrugItemBean b44=new DrugItemBean();
        b44.setName("充电器");
        list9.add(b41);
        list9.add(b42);
        list9.add(b43);
        list9.add(b44);

        //电脑办公：
        //热卖分类：轻薄本、游戏本、机械键盘、显示器

        DrugListBean l10 = new DrugListBean();
        l10.setType("热卖分类");
        List<DrugItemBean> list10=new ArrayList<>();
        DrugItemBean b45=new DrugItemBean();
        b45.setName("轻薄本");
        DrugItemBean b46=new DrugItemBean();
        b46.setName("游戏本");
        DrugItemBean b47=new DrugItemBean();
        b47.setName("机械键盘");
        DrugItemBean b48=new DrugItemBean();
        b48.setName("显示器");
        list10.add(b45);
        list10.add(b46);
        list10.add(b47);
        list10.add(b48);
//电脑整机：笔记本、平板电脑、一体机
        DrugListBean l11 = new DrugListBean();
        l11.setType("电脑整机");
        List<DrugItemBean> list11=new ArrayList<>();
        DrugItemBean b49=new DrugItemBean();
        b49.setName("笔记本");
        DrugItemBean b50=new DrugItemBean();
        b50.setName("平板电脑");
        DrugItemBean b51=new DrugItemBean();
        b51.setName("一体机");

        list11.add(b49);
        list11.add(b50);
        list11.add(b51);




        l1.setmList(list1);
        l2.setmList(list2);
        l3.setmList(list3);
        l4.setmList(list4);
        l5.setmList(list5);
        l6.setmList(list6);
        l7.setmList(list7);
        l8.setmList(list8);
        l9.setmList(list9);
        l10.setmList(list10);
        l11.setmList(list11);

        //

        List<DrugListBean> li1=new ArrayList<>();
        List<DrugListBean> li2=new ArrayList<>();
        List<DrugListBean> li3=new ArrayList<>();
        List<DrugListBean> li4=new ArrayList<>();

        li1.add(l1);
        li1.add(l2);
        li1.add(l3);
        li1.add(l4);
        li1.add(l9);

        li2.add(l5);
        li2.add(l6);

        li3.add(l7);
        li3.add(l8);

        li4.add(l10);
        li4.add(l11);


        d1.setmList(li1);
        d2.setmList(li2);
        d3.setmList(li3);
        d4.setmList(li4);


        drugBeanList.add(d1);
        drugBeanList.add(d2);
        drugBeanList.add(d3);
        drugBeanList.add(d4);


        listBeanList.addAll(drugBeanList.get(0).getmList());


    }

}