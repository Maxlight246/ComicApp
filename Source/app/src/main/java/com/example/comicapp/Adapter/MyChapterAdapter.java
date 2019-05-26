package com.example.comicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.comicapp.Common.Common;
import com.example.comicapp.Interface.IRecyclerClickListener;
import com.example.comicapp.Model.Chapter;
import com.example.comicapp.R;
import com.example.comicapp.ViewComicActivity;

import java.util.List;

public class MyChapterAdapter extends RecyclerView.Adapter<MyChapterAdapter.MyViewHolder> {

    Context context;
    List<Chapter> chapterList;

    public MyChapterAdapter(Context context, List<Chapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
         myViewHolder.tvChapterNumb.setText(chapterList.get(i).Name);
         myViewHolder.setRecyclerClickListener(new IRecyclerClickListener() {
             @Override
             public void onClick(View view, int positon) {
                 Common.chapterSelected = chapterList.get(positon);
                 Common.chapterIndex = positon;
                 context.startActivity(new Intent(context,ViewComicActivity.class));
             }
         });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvChapterNumb;
        IRecyclerClickListener recyclerClickListener;

        public void setRecyclerClickListener(IRecyclerClickListener recyclerClickListener) {
            this.recyclerClickListener = recyclerClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterNumb = itemView.findViewById(R.id.tv_chapter_numb);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerClickListener.onClick(v,getAdapterPosition());
        }
    }
}
