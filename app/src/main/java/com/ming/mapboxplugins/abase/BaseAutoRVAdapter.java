package com.ming.mapboxplugins.abase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

/**
 * 创建人: sunming
 * 创建时间：2017/10/31
 * version：1.0
 * Email:sunming@radacat.com
 */

public abstract class BaseAutoRVAdapter extends RecyclerView.Adapter<BaseRVHolder> {


    public List<?> list;

    private Context context;

    public BaseAutoRVAdapter(Context context, List<?> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), parent, false);

        return new BaseRVHolder(view);
    }

    public abstract int onCreateViewLayoutID(int viewType);


    @Override
    public void onViewRecycled(@NonNull final BaseRVHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final BaseRVHolder holder, final int position) {

        onBindViewHolder(holder.getViewHolder(), position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(null, v, holder.getAdapterPosition(), holder.getItemId());
                    }
                }
            });
        }

    }

    public abstract void onBindViewHolder(BaseViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
