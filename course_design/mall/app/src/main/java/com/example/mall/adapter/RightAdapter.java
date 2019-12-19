package com.example.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.example.mall.R;
import com.example.mall.activity.searchActivity;
import com.example.mall.domain.DrugItemBean;
import com.example.mall.domain.DrugListBean;
import com.example.mall.recyclerview.list_goodsActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * User: Liumj(liumengjie@365tang.cn)
 * Date: 2016-10-11
 * Time: 15:51
 * describe:  右边适配器
 */
public class RightAdapter extends BaseQuickAdapter<DrugListBean>{

	public Context context;

	public RightAdapter(List<DrugListBean> data, Context context) {
		super(R.layout.item_main_right, data);
		this.context = context;
	}

	@Override
	protected void convert(final BaseViewHolder helper, final DrugListBean listBean) {
		helper.setText(R.id.item_main_right_title,listBean.getType());
		 TagFlowLayout  flowlayout = helper.getView(R.id.item_main_right_taglayout);
		final List<DrugItemBean> drugItemBeen = listBean.getmList();
		final DrugTagAdapter drugAdapter=new DrugTagAdapter(mContext,drugItemBeen);

		flowlayout.setAdapter(drugAdapter);
		flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			@Override
			public boolean onTagClick(View view, int position, FlowLayout parent) {
				DrugItemBean drugItemBean = drugItemBeen.get(position);
				for (DrugItemBean b:
				     drugItemBeen) {
					b.setCheck(false);
				}
				drugItemBean.setCheck(true);
				drugAdapter.notifyDataChanged();
				//Snackbar.make(helper.convertView, "点击了"+drugItemBean.getName(), Snackbar.LENGTH_SHORT).show();

				//String con = input_content.getText().toString();
				Intent intent = new Intent(context, list_goodsActivity.class);
				intent.putExtra("con",drugItemBean.getName());
//				insert_search(con);
				context.startActivity(intent);

				return false;
			}
		});



	}
}
