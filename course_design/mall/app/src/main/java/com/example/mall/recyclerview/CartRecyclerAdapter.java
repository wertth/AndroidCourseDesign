package com.example.mall.recyclerview;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.mall.R;
import com.example.mall.entity.Cart;
import com.example.mall.entity.Goods;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.*;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.MyViewHolder>{

    private final Context context;
    public ArrayList<String> datas;
    public List<Cart> data_carts;
    public int all_p = 0;
    public List<Long> selected_goodsid;
    public int num = 0;

    private Map<Integer, Boolean> checkStatus;//用来记录所有checkbox的状态 建议用 ImageView


    public CartRecyclerAdapter(Context context,List<Cart> data_carts){
        this.context = context;
        this.data_carts = data_carts;
        Collections.reverse(data_carts);
        selected_goodsid = new ArrayList<>();

        checkStatus = new HashMap<>();
        for(int i=0;i<=data_carts.size();i++){
            checkStatus.put(i,false);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context,R.layout.cart_list_item,null);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder,final int i) {
        //myViewHolder.setIsRecyclable(false);
        final Cart cart = data_carts.get(i);

        //final int p = i;
        myViewHolder.cart_tv_title.setText(cart.getGoodsname());
        myViewHolder.cart_tv_price.setText(Integer.toString(cart.getPrice()));
        Uri uri = Uri.parse(cart.getImage1());
        Log.e("cart===", "onBindViewHolder: "+cart.getImage1() );
        myViewHolder.cart_sdv.setImageURI(uri);

        myViewHolder.cart_checkbox.setOnCheckedChangeListener(null);
        myViewHolder.cart_checkbox.setChecked(checkStatus.get(i));



        myViewHolder.cart_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkStatus.put(i,isChecked);

                if(checkStatus.get(i)){

                    //myViewHolder.cart_checkbox.setBackgroundResource(R.drawable.checkbox_yes);

                    int t = cart.getPrice();

                    Log.e("Cart", "onCheckedChanged: "+cart.getGoodsid());

                    //all_p += t;
                    num ++ ;

                    setAll_p_add(t,cart.getGoodsid());
                    //set_selected_goodsid(goods.getGoodsid());
                    Toast.makeText(context,"num = "+i, Toast.LENGTH_SHORT).show();
                }else {


                    //myViewHolder.cart_checkbox.setBackgroundResource(R.drawable.checkbox_null);
                    int t = cart.getPrice();
                    Log.e("Cart", "onCheckedChanged: "+cart.getGoodsid());
//                    all_p -= t;
                    setAll_p_dec(t,cart.getGoodsid());
                    num -- ;
                    //dec_selected_goodsid(goods.getGoodsid());
                    Toast.makeText(context,"num = "+i, Toast.LENGTH_SHORT).show();
                }
            }
        });





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

    @Override
    public int getItemCount() {
        return data_carts.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView cart_sdv;
        public TextView cart_tv_title;
        public TextView cart_tv_price;
        public CheckBox cart_checkbox;
//        public Button cart_btn;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            cart_sdv = itemView.findViewById(R.id.cart_iv_img);
            cart_tv_price = itemView.findViewById(R.id.cart_iv_price);
            cart_tv_title = itemView.findViewById(R.id.cart_iv_title);
//            cart_btn = itemView.findViewById(R.id.btn_cart);
            cart_checkbox = itemView.findViewById(R.id.cart_check_box);

        }

    }

    //观察者模式
    private OnCUpdateListener mListener;
    public void setAll_p_add(int p,Long id) {
        all_p += p;
        selected_goodsid.add(id);
        mListener.onCUpdate(all_p,selected_goodsid);
    }
    public void setAll_p_dec(int p,Long id) {
        all_p -= p;
        selected_goodsid.add(id);
        mListener.onCUpdate(all_p,selected_goodsid);
    }
    //public void set_selected_goodsid(Long id){
    //    selected_goodsid.add(id);
    //    mListener.onCUpdate(all_p,selected_goodsid);
    //}
    //public void dec_selected_goodsid(Long id){
    //    selected_goodsid.add(id);
    //    mListener.onCUpdate(all_p,selected_goodsid);
    //}
    public void setOnCUpdateListener(OnCUpdateListener listener) {
        mListener = listener;
    }

    public interface OnCUpdateListener {
        public void onCUpdate(int all_p,List<Long> selected_goodsid);
    }


    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private CartRecyclerAdapter.OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(CartRecyclerAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private CartRecyclerAdapter.OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(CartRecyclerAdapter.OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
