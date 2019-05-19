package com.ming.mapboxplugins.abase;

import android.content.Context;

import com.ming.mapboxplugins.R;

import java.util.List;

public class RvItemListAdapter extends BaseAutoRVAdapter {
    private List<ItemBean> itemBeans;

    public RvItemListAdapter(Context context, List<?> list) {
        super(context, list);
        this.itemBeans = (List<ItemBean>) list;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_list;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (itemBeans != null && itemBeans.size() > 0){
            ItemBean itemBean = itemBeans.get(position);
            holder.setTextView(R.id.tv_item_name,itemBean.getItemName());
        }
    }
}
