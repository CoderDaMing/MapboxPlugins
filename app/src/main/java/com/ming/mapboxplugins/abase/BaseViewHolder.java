package com.ming.mapboxplugins.abase;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 创建人: sunming
 * 创建时间：2017/10/31
 * version：1.0
 * Email:sunming@radacat.com
 */

public class BaseViewHolder {
    private SparseArray<View> viewHolder;
    private View view;

    public static BaseViewHolder getViewHolder(View view){
        BaseViewHolder viewHolder = (BaseViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new BaseViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }
    private BaseViewHolder(View view) {
        this.view = view;
        viewHolder = new SparseArray<View>();
        view.setTag(viewHolder);
    }
    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return view;
    }

    public TextView getTextView(int id) {

        return get(id);
    }
    public Button getButton(int id) {

        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int  id,CharSequence charSequence){
        getTextView(id).setText(charSequence);
    }

}