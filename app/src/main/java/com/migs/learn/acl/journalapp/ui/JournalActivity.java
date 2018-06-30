package com.migs.learn.acl.journalapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.migs.learn.acl.journalapp.R;
import com.migs.learn.acl.journalapp.database.models.JournalEntry;
import com.migs.learn.acl.journalapp.viewmodel.JournalEntryViewModel;

import java.util.Date;

public class JournalActivity extends AppCompatActivity {

    public static final String CATEGORY_ID = "category-id";
    public static final String JOURNAL_ID = "journal-id";
    private String content;
    private EditText tvContent;
    private EditText tvTitle;
    private int categoryId;
    private boolean isEdit;
    private int journalId;
    private JournalEntryViewModel entryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        tvContent = findViewById(R.id.tv_story);
        tvTitle = findViewById(R.id.tv_title);

        entryViewModel = ViewModelProviders.of(this).get(JournalEntryViewModel.class);

        Intent categoryData = getIntent();
        categoryId = categoryData.getIntExtra(CATEGORY_ID, 2);

        Intent journalData = getIntent();
        journalId = journalData.getIntExtra(JOURNAL_ID, 0);

        if (journalId != 0) {
            setJournalData();
        }
    }

    private void setJournalData() {
        isEdit = true;

        entryViewModel.setJournalIdData(journalId);

        entryViewModel.getJournalEntryData().observe(this, new Observer<JournalEntry>() {
            @Override
            public void onChanged(@Nullable JournalEntry journalEntry) {
                if (journalEntry != null) {
                    tvTitle.setText(journalEntry.getTitle());
                    tvContent.setText(journalEntry.getContent());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        content = tvContent.getText().toString().trim();
        if (itemId == R.id.action_save) {
            persistJournal();
            return true;
        }
        if (itemId == R.id.action_share) {
            Intent intent = initContentShare();
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void persistJournal() {
        if (TextUtils.isEmpty(tvContent.getText().toString())) {
            Toast.makeText(this, "Cannot save a blank journal", Toast.LENGTH_SHORT).show();
        } else {
            String title;

            if (TextUtils.isEmpty(tvTitle.getText().toString())) {
                //get Title from content

                String[] contentArr = content.split(" ");
                StringBuilder titleBuilder = new StringBuilder();

                title = contentArr.length <= 3 ? content : titleBuilder
                        .append(contentArr[0]).append(" ")
                        .append(contentArr[1]).append(" ")
                        .append(contentArr[2]).toString();

            } else {
                title = tvTitle.getText().toString().trim();
            }
            //call save
            JournalEntry journalEntry = new JournalEntry(content, new Date().getTime(), categoryId, title.toUpperCase());

            if (isEdit) {
                journalEntry.setJournalId(journalId);
                entryViewModel.updateJournal(journalEntry);
            } else {
                entryViewModel.saveJournal(journalEntry);
            }
        }
        finish();
    }

    private Intent initContentShare() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(content)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }
}
