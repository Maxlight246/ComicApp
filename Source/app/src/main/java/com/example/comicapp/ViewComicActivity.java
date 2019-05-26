package com.example.comicapp;

import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicapp.Adapter.MyViewPagerAdapter;
import com.example.comicapp.Common.Common;
import com.example.comicapp.Model.Chapter;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

public class ViewComicActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView tvChapterName;
    View back,next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comic);
        init();
    }

    void init(){
        viewPager = findViewById(R.id.view_pagger);

        tvChapterName = findViewById(R.id.tv_chapter_name);
        tvChapterName.setText(Common.chapterSelected.Name);

        back = findViewById(R.id.chapter_back);
        next = findViewById(R.id.chapter_next);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.chapterIndex==0){
                    Toast.makeText(ViewComicActivity.this, "You are reading first chapter", Toast.LENGTH_SHORT).show();
                }else {
                   Common.chapterIndex--;
                   fetchLinks(Common.chapterList.get(Common.chapterIndex));
                   tvChapterName.setText(Common.chapterList.get(Common.chapterIndex).Name);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.chapterIndex==Common.chapterList.size()-1){
                    Toast.makeText(ViewComicActivity.this, "You are reading last chapter", Toast.LENGTH_SHORT).show();
                }else {
                    Common.chapterIndex++;
                    fetchLinks(Common.chapterList.get(Common.chapterIndex));
                    tvChapterName.setText(Common.chapterList.get(Common.chapterIndex).Name);
                }
            }
        });

        fetchLinks(Common.chapterSelected);
    }

    private void fetchLinks(Chapter chapter) {

        if (chapter.Links!= null){

            if (chapter.Links.size()>0){
                MyViewPagerAdapter adapter = new MyViewPagerAdapter(getBaseContext(),chapter.Links);
                viewPager.setAdapter(adapter);

                //Animation
                BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
                bookFlipPageTransformer.setScaleAmountPercent(10f);
                viewPager.setPageTransformer(true, bookFlipPageTransformer);
            }
            else {
                Toast.makeText(this, "No image here", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            View view = findViewById(R.id.view_comic);
            String message = "This chapter is translating ...";
            int duration = Snackbar.LENGTH_SHORT;
            Snackbar.make(view,message,duration).show();
        }

    }
}
