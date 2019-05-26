package com.example.comicapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicapp.Adapter.MyComicAdapter;
import com.example.comicapp.Adapter.MySlideAdapter;
import com.example.comicapp.Common.Common;
import com.example.comicapp.Interface.IBannerLoadDone;
import com.example.comicapp.Interface.IComicLoadDone;
import com.example.comicapp.Model.Comic;
import com.example.comicapp.Service.PicassoLoadingService;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity implements IBannerLoadDone, IComicLoadDone {

    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_comic;
    TextView tv_comic;
    ImageView bt_search;

    //Database
    DatabaseReference banners,comics;

    //Listener
    IBannerLoadDone bannerListener;
    IComicLoadDone comicListtener;

    //Dialog
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init(){
        //Database
        banners = FirebaseDatabase.getInstance().getReference("Banners");
        comics = FirebaseDatabase.getInstance().getReference("Comic");

        //Listener
        bannerListener = this;
        comicListtener = this;

        slider = findViewById(R.id.slider);
        slider.init(new PicassoLoadingService());

        swipeRefreshLayout = findViewById(R.id.swipe_to_refesh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBanner();
                loadComic();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
                loadComic();
            }
        });

        tv_comic = findViewById(R.id.tv_comic);

        recycler_comic = findViewById(R.id.recycler_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(this,2));

        bt_search = findViewById(R.id.bt_show_filter_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });
    }

    private void loadBanner(){
        // get list from database
        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bannerList = new ArrayList<>();
                for (DataSnapshot bannerSnapshot:dataSnapshot.getChildren()){
                    String image = bannerSnapshot.getValue(String.class);
                    bannerList.add(image);
                }
                //Call Listener
                bannerListener.onBannerLoadDoneListener(bannerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComic(){
        //show dialog
        alertDialog = new SpotsDialog.Builder().setContext(this)
                               .setCancelable(false)
                               .setMessage("Please wait ...")
                               .build();
         alertDialog.show();
         // get list from database
         comics.addListenerForSingleValueEvent(new ValueEventListener() {

             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 List<Comic> comic_load = new ArrayList<>();
                   for (DataSnapshot comicSnapshot:dataSnapshot.getChildren()){
                       Comic comic = comicSnapshot.getValue(Comic.class);
                       comic_load.add(comic);
                   }
                   // call Listener
                   comicListtener.onComicLoadDoneListener(comic_load);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });
    }

    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
         slider.setAdapter(new MySlideAdapter(banners));
         swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onComicLoadDoneListener(List<Comic> comicList) {
        Common.comicList = comicList;
        recycler_comic.setAdapter(new MyComicAdapter(getBaseContext(),comicList));

        tv_comic.setText(new StringBuilder("NEW COMIC (").append(comicList.size()).append(")"));

        alertDialog.dismiss();

    }
}
