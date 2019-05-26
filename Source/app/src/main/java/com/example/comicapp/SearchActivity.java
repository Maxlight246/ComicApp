package com.example.comicapp;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicapp.Adapter.MyComicAdapter;
import com.example.comicapp.Common.Common;
import com.example.comicapp.Model.Comic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recycler_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    void init(){
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.inflateMenu(R.menu.search_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_filter:
                        showFilterDialog();
                        break;
                    case R.id.action_search:
                        showSearchDialog();
                        break;
                        default:
                            break;

                }
                return true;
            }
        });

        recycler_search = findViewById(R.id.recycler_search_comic);
        recycler_search.setHasFixedSize(true);
        recycler_search.setLayoutManager(new GridLayoutManager(this,2));
    }

    private void showSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Search");
        View search_layout = LayoutInflater.from(this).inflate(R.layout.dialog_search,null);

        final EditText edSearch = search_layout.findViewById(R.id.ed_search);

        alertDialog.setView(search_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 fetchSearchComic(edSearch.getText().toString());
            }
        });
        alertDialog.show();

    }

    private void fetchSearchComic(String query) {
             List<Comic> comic_search = new ArrayList<>();
             for (Comic comic: Common.comicList){
                 if (comic.Name.contains(query)){
                     comic_search.add(comic);
                 }
             }
             if (comic_search.size()>0){
                 recycler_search.setAdapter(new MyComicAdapter(getBaseContext(),comic_search));
             }else {
                 Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
             }

    }

    private void showFilterDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Select Category");
        View filter_layout = LayoutInflater.from(this).inflate(R.layout.dialog_option,null);


        final AutoCompleteTextView tvCategory = filter_layout.findViewById(R.id.tv_category);
        final ChipGroup chipGroup = filter_layout.findViewById(R.id.chipGroup);

        //Create Autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_item, Common.categories);
        tvCategory.setAdapter(adapter);
        tvCategory.setThreshold(1);
        tvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Clear
                tvCategory.setText("");
                //Create tag
                Chip chip = (Chip) LayoutInflater.from(SearchActivity.this).inflate(R.layout.chip_item,null);
                chip.setText(((TextView)view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroup.removeView(v);
                    }
                });
                chipGroup.addView(chip);
            }
        });
        //Setup Dialog
        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query = new StringBuilder();
                for (int j =0; j < chipGroup.getChildCount(); j++){
                    Chip chip = (Chip) chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }
                //Sap xep filter_key
                Collections.sort(filter_key);
                //Convert list to string
                for (String key: filter_key){
                    filter_query.append(key).append(",");
                }
                //Remove last ","
                filter_query.setLength(filter_query.length()-1);
                //Filter by this query
                fetchFilterCategory(filter_query.toString());
            }
        });
        alertDialog.show();
    }

    private void fetchFilterCategory(String query) {
        List<Comic> comic_filtered = new ArrayList<>();
        for (Comic comic: Common.comicList) {
            if (comic.Category!=null) {
                if (comic.Category.contains(query)) {
                    comic_filtered.add(comic);
                }
            }
        }
        if (comic_filtered.size()>0) {
            recycler_search.setAdapter(new MyComicAdapter(getBaseContext(), comic_filtered));
        }
        else {
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
        }
    }
}
