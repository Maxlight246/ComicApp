package com.example.comicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicapp.Adapter.MyChapterAdapter;
import com.example.comicapp.Common.Common;
import com.example.comicapp.Model.Comic;

public class ChapterActivity extends AppCompatActivity {

    RecyclerView recycler_chapter;
    TextView tvChapterName;
    Toolbar toolbar;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        init();
    }

    void init(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.comicSelected.Name);
        //set back
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });

        tvChapterName = findViewById(R.id.tv_chapter_name);

        recycler_chapter = findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        // lay comic dc chon
        fetchChapter(Common.comicSelected);
    }

    private void fetchChapter(Comic comicSelected) {
            if (comicSelected.Chapters==null){
                Toast.makeText(this, "Coming soon ...", Toast.LENGTH_SHORT).show();
            }else {
                Common.chapterList = comicSelected.Chapters;
                recycler_chapter.setAdapter(new MyChapterAdapter(this,comicSelected.Chapters));
                tvChapterName.setText(new StringBuilder("CHAP (").append(comicSelected.Chapters.size()).append(")"));
            }
    }
}
