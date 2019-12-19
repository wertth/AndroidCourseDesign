package com.example.mall.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mall.R;
import com.example.mall.entity.Goods;
import com.facebook.drawee.view.SimpleDraweeView;


import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    public ArrayList<String> datas;
    public List<Goods> data_goods;
    public boolean flag = false;

    public MyRecyclerViewAdapter(Context context, List<Goods> data_goods,boolean flag) {
        this.context = context;
        this.data_goods = data_goods;
        this.flag = flag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview;
        if(flag==false){
            itemview =  View.inflate(context,R.layout.list_goods_item, null);
        }else{
            itemview = View.inflate(context,R.layout.list_goods_item_two,null);
        }
        //View itemview = View.inflate(context, R.layout.list_goods_item,null);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if(flag == false){
        Goods data = data_goods.get(i);
        //myViewHolder.textView.setText(data.getGoodsname());
        myViewHolder.textView.setText(data.getGoodsname());
        myViewHolder.iv_price.setText(Integer.toString(data.getPrice()));
        //myViewHolder.iv_img.setImageBitmap(getImage(data.getImage1()));
        //SimpleDraweeView sdv = (SimpleDraweeView) view.findViewById(R.id.simp);
        Uri uri = Uri.parse(data.getImage1());
        myViewHolder.sdv.setImageURI(uri);


        final int position = i;
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(position);
            }
        }
        });

        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(position);
                }
                return true;
            }
        });
        }else{
            Goods data = data_goods.get(i);
            Log.e("第二种布局", "onBindViewHolder: "+data.toString() );
            //myViewHolder.textView.setText(data.getGoodsname());
            myViewHolder.list_goods_two_title.setText(data.getGoodsname());
            //myViewHolder.iv_price.setText(Integer.toString(data.getPrice()));
            myViewHolder.list_goods_two_price.setText(Integer.toString(data.getPrice()));
            //myViewHolder.iv_img.setImageBitmap(getImage(data.getImage1()));
            //SimpleDraweeView sdv = (SimpleDraweeView) view.findViewById(R.id.simp);
            Uri uri = Uri.parse(data.getImage1());
            myViewHolder.list_goods_two_img.setImageURI(uri);


            final int position = i;
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
            });

            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        longClickListener.onClick(position);
                    }
                    return true;
                }
            });
        }
    }






    @Override
    public int getItemCount() {
        return data_goods.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public TextView iv_price;

        public TextView list_goods_two_title;
        public SimpleDraweeView list_goods_two_img;
        public TextView list_goods_two_price;

        SimpleDraweeView sdv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_img);
            textView = itemView.findViewById(R.id.iv_title);
            iv_price = itemView.findViewById(R.id.iv_price);
            sdv = itemView.findViewById(R.id.iv_img);
            list_goods_two_title = itemView.findViewById(R.id.list_goods_two_title);
            list_goods_two_img = itemView.findViewById(R.id.list_goods_two_img);
            list_goods_two_price = itemView.findViewById(R.id.list_goods_two_price);


        }
    }

    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }




    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

}
