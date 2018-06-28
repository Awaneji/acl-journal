package com.migs.learn.acl.journalapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.migs.learn.acl.journalapp.R;
import com.migs.learn.acl.journalapp.adapters.CategoryAdapter;
import com.migs.learn.acl.journalapp.database.models.JournalCategory;
import com.migs.learn.acl.journalapp.utils.IconSet;
import com.migs.learn.acl.journalapp.viewmodel.JournalCategoryViewModel;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryClickHandler {

    public static final int CATEGORY_SAVE_CODE = 145;
    private CategoryAdapter categoryAdapter;
    private RecyclerView rvCategories;
    private JournalCategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvCategories = findViewById(R.id.rv_categories);
        categoryViewModel = ViewModelProviders.of(this).get(JournalCategoryViewModel.class);
        categoryAdapter = new CategoryAdapter(this);
        categoryAdapter.setCategoryClickHandler(this);
        initRecyclerView();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, CategoryActivity.class), CATEGORY_SAVE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CATEGORY_SAVE_CODE && resultCode == RESULT_OK && data != null) {

            String category = data.getStringExtra(CategoryActivity.CATEGORY_DATA);
            JournalCategory journalCategory = new JournalCategory(category, IconSet.AB.getIconId());

            categoryViewModel.createCategory(journalCategory);
            Snackbar.make(this.rvCategories, "Journal Category saved successfully", Snackbar.LENGTH_SHORT).show();

        } else {
            Snackbar.make(this.rvCategories, "Journal Category not saved", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView() {

        rvCategories.setItemAnimator(new DefaultItemAnimator());
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        categoryViewModel.getCategories().observe(this, journalCategories -> {
            categoryAdapter.setCategoryList(journalCategories);
        });

        rvCategories.setHasFixedSize(true);
        rvCategories.setAdapter(categoryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // signing checks here and display fire-base auth ui screen
    }


    @Override
    public void onClickCategory(Integer categoryId) {
        // navigate to Journals and filter by categoryId passed
        Intent openJournals = new Intent(this, JournalsActivity.class);
        openJournals.putExtra(JournalsActivity.CATEGORY_ID, categoryId);
        startActivity(openJournals);
    }
}
