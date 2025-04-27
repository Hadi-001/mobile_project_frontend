package com.example.mobile_project_frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyPropertiesAdapter
        extends RecyclerView.Adapter<MyPropertiesAdapter.Holder> {

    public interface OnDeleteClickListener { void onDelete(PropertyItem p); }
    public interface OnEditClickListener   { void onEdit  (PropertyItem p); }

    private final List<PropertyItem> list;
    private final Context ctx;
    private final OnDeleteClickListener deleteCb;
    private final OnEditClickListener   editCb;

    public MyPropertiesAdapter(List<PropertyItem> list,
                               Context ctx,
                               OnDeleteClickListener d,
                               OnEditClickListener   e) {
        this.list     = list;
        this.ctx      = ctx;
        this.deleteCb = d;
        this.editCb   = e;
    }

    @NonNull
    @Override public Holder onCreateViewHolder(@NonNull ViewGroup p,int v) {
        View view = LayoutInflater.from(p.getContext())
                .inflate(R.layout.estates_vertical, p,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h,int pos){
        PropertyItem item = list.get(pos);

        h.title.setText(item.getType());
        Glide.with(ctx)
                .load("http://10.0.2.2/" + item.getImageLink())
                .into(h.thumb);

        h.bin.setImageResource(R.drawable.ic_delete);          // 🗑️ icon
        h.bin.setOnClickListener(v -> deleteCb.onDelete(item));

        h.itemView.setOnClickListener(v -> editCb.onEdit(item));
    }

    @Override public int getItemCount(){ return list.size(); }

    static class Holder extends RecyclerView.ViewHolder{
        ImageView  thumb;
        TextView   title;
        ImageButton bin;
        Holder(@NonNull View v){
            super(v);
            thumb = v.findViewById(R.id.itemImage);
            title = v.findViewById(R.id.titleTextView);
            bin   = v.findViewById(R.id.favoriteButton);  // reused id
        }
    }
}