package com.ming.mapboxplugins.abase;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 创建人: sunming
 * 创建时间：2017/10/31
 * version：1.0
 * Email:sunming@radacat.com
 */

public class BaseRVHolder extends RecyclerView.ViewHolder {


    private BaseViewHolder viewHolder;

    public BaseRVHolder(View itemView) {
        super(itemView);
        viewHolder= BaseViewHolder.getViewHolder(itemView);
    }


    public BaseViewHolder getViewHolder() {
        return viewHolder;
    }

}