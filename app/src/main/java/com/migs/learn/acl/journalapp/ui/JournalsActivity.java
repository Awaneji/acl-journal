package com.migs.learn.acl.journalapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.migs.learn.acl.journalapp.R;
import com.migs.learn.acl.journalapp.adapters.JournalEntryAdapter;
import com.migs.learn.acl.journalapp.viewmodel.JournalCategoryViewModel;
import com.migs.learn.acl.journalapp.viewmodel.JournalEntryViewModel;

public class JournalsActivity extends AppCompatActivity implements JournalEntryAdapter.JournalClickHandler {

    public static final String CATEGORY_ID = "category-id";
    private JournalEntryAdapter adapter;
    private TextView tvCategory;
    private RecyclerView journalsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journals);
        Toolbar toolbar = findViewById(R.id.toolbar);
        tvCategory = findViewById(R.id.tv_category);
        journalsRecyclerView = findViewById(R.id.rv_journals);

        setSupportActionBar(toolbar);

        // get intent data
        Intent categoryData = getIntent();
        int categoryId = categoryData.getIntExtra(CATEGORY_ID, -1);

        adapter = new JournalEntryAdapter(this);
        adapter.setClickHandler(this);
        setUpDetails(categoryId);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent journalIntent = new Intent(JournalsActivity.this, JournalActivity.class);
                journalIntent.putExtra(JournalActivity.CATEGORY_ID, categoryId);
                startActivity(journalIntent);
            }
        });
    }

    private void setUpDetails(int categoryId) {
        JournalCategoryViewModel journalCategoryViewModel = ViewModelProviders.of(this).get(JournalCategoryViewModel.class);
        JournalEntryViewModel journalEntryViewModel = ViewModelProviders.of(this).get(JournalEntryViewModel.class);
        // display category details
        journalCategoryViewModel.setCategoryIdData(categoryId);
        journalCategoryViewModel.getCategory().observe(this, journalCategory -> {
            if (journalCategory != null) {
                //display
                tvCategory.setText(journalCategory.getCategoryName());
            } else {
                //show snackbar
                finish();
            }
        });

        journalsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        journalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set adapter here
        journalEntryViewModel.setCategoryIdData(categoryId);
        journalEntryViewModel.getCategoryJournalEntries().observe(this, journalEntries -> adapter.setEntryList(journalEntries));

        // populate recyclerView with entries
        journalsRecyclerView.setHasFixedSize(true);
        journalsRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onClickJournal(Integer journalId) {
        Intent intentJournal = new Intent(JournalsActivity.this, JournalActivity.class);
        intentJournal.putExtra(JournalActivity.JOURNAL_ID, journalId);
        startActivity(intentJournal);
    }
}
