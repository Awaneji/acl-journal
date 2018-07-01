package com.migs.learn.acl.journalapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.migs.learn.acl.journalapp.R;
import com.migs.learn.acl.journalapp.database.models.JournalEntry;
import com.migs.learn.acl.journalapp.utils.Utils;
import com.migs.learn.acl.journalapp.viewmodel.JournalCategoryViewModel;
import com.migs.learn.acl.journalapp.viewmodel.JournalEntryViewModel;

import java.util.Date;

public class JournalActivity extends AppCompatActivity implements CategoryPickerDialogFragment.CategoryPickerHandler {

    public static final String CATEGORY_ID = "category-id";
    public static final String JOURNAL_ID = "journal-id";
    public static final String TAG = JournalActivity.class.getSimpleName();
    public static final String FIREBASE_CATEGORIES_COLLECTION = "categories";
    public static final String FIREBASE_JOURNALS_COLLECTION = "journals";
    public static final String FIREBASE_USERS_COLLECTION = "users";
    private String content;
    private String title;
    private TextInputEditText tvContent;
    private TextInputEditText tvTitle;
    private int categoryId;
    private int selectedCategoryId;
    private boolean isEdit;
    private int journalId;
    private JournalEntryViewModel entryViewModel;
    private JournalCategoryViewModel categoryViewModel;
    private String categoryName;
    //private FirebaseFirestore firestore;
    private FirebaseUser user;
    private String username;
    private String email;
    private View mRootView;
    private CategoryPickerDialogFragment categoryPicker;
    private JournalEntry entry;
    private boolean deleteEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);


        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        tvContent = findViewById(R.id.tv_story);
        tvTitle = findViewById(R.id.tv_title);
        mRootView = findViewById(R.id.sc_journal);

        user = FirebaseAuth.getInstance().getCurrentUser();
        username = user.getDisplayName();
        email = user.getEmail();


        //firestore = FirebaseFirestore.getInstance();
        entryViewModel = ViewModelProviders.of(this).get(JournalEntryViewModel.class);
        categoryViewModel = ViewModelProviders.of(JournalActivity.this).get(JournalCategoryViewModel.class);

        Intent categoryData = getIntent();
        categoryId = categoryData.getIntExtra(CATEGORY_ID, 2);

        Intent journalData = getIntent();
        journalId = journalData.getIntExtra(JOURNAL_ID, 0);

        if (journalId != 0) {

            isEdit = true;
            setJournalData();
        }

        setCategoryName();
    }

    private void setCategoryName() {
        categoryViewModel.setCategoryIdData(categoryId);
        categoryViewModel.getCategory().observe(this, journalCategory -> {
            if (journalCategory != null) {
                categoryName = journalCategory.getCategoryName();
            }
        });
    }

    private void setJournalData() {

        entryViewModel.setJournalIdData(journalId);
        entryViewModel.getJournalEntryData().observe(this, journalEntry -> {
            if (journalEntry != null) {
                categoryId = journalEntry.getCategoryId();
                tvTitle.setText(journalEntry.getTitle());
                tvContent.setText(journalEntry.getContent());

                entry = journalEntry;
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
        title = tvTitle.getText().toString().trim();
        if (itemId == R.id.action_save) {
            persistJournal(content);
            return true;
        }
        if (itemId == R.id.action_share) {
            Intent intent = initContentShare();
            startActivity(intent);

            return true;
        }

        if (itemId == R.id.action_copy) {
            deleteEntry = false;
            showCategoryPicker();
            return true;
        }

        if (itemId == R.id.action_move) {
            deleteEntry = true;
            showCategoryPicker();

            return true;
        }

        if (itemId == R.id.action_delete) {
            if (isEdit) {
                deleteEntry = true;
                deleteFromOldCategory();

                Utils.showSnackbar(R.string.delete_journal_success, mRootView);

                new Handler().postDelayed(() -> finish(), Snackbar.LENGTH_SHORT);
            } else {
                Utils.showSnackbar(R.string.cannot_delete, mRootView);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteFromOldCategory() {
        entryViewModel.deleteJournal(entry);
    }

    private void showCategoryPicker() {
        FragmentManager fm = getSupportFragmentManager();
        categoryPicker = CategoryPickerDialogFragment.getInstance(categoryId);
        categoryPicker.setPickerHandler(this);

        categoryPicker.show(fm, "category-picker");
    }

    private void persistJournal(String content) {
        if (TextUtils.isEmpty(content)) {
            tvContent.setError(getString(R.string.blank_msg));
            Utils.showSnackbar(R.string.notify_blank_msg, mRootView);
        } else {

            buildTitle(content);

            //CollectionReference addJournal = firestore.collection(FIREBASE_USERS_COLLECTION);
            JournalEntry journalEntry = new JournalEntry(content, new Date().getTime(), categoryId, title.toUpperCase());

            if (isEdit) {
                journalEntry.setJournalId(journalId);
                entryViewModel.updateJournal(journalEntry);
                //update firebase
                /*Map<String, Object> mapJournal = new HashMap<>();
                mapJournal.put("title", journalEntry.getTitle());
                mapJournal.put("content", journalEntry.getContent());

                DocumentReference docJournal = firestore.document(email)
                        .collection(FIREBASE_CATEGORIES_COLLECTION)
                        .document(categoryName)
                        .collection(FIREBASE_JOURNALS_COLLECTION)
                        .document(journalEntry.getFirebaseRef());

                docJournal.update(mapJournal)
                        .addOnSuccessListener(aVoid -> Utils.showSnackbar(R.string.firebase_success_msg, mRootView))
                        .addOnFailureListener(e -> {
                            Utils.showSnackbar(R.string.firebase_success_failure, mRootView);
                            Log.e(JournalActivity.class.getSimpleName(), e.getMessage());
                        });*/

            } else {
                entryViewModel.saveJournal(journalEntry);
                //save to firebase
                /*JournalEntry firebaseJournal = new JournalEntry(content, title);

                addJournal.document(email)
                        .collection(FIREBASE_CATEGORIES_COLLECTION)
                        .document(categoryName)
                        .collection(FIREBASE_JOURNALS_COLLECTION)
                        .add(firebaseJournal)
                        .addOnSuccessListener(documentReference -> {
                            //update local db
                            journalEntry.setFirebaseRef(documentReference.getId());
                            journalEntry.setJournalId(journalId);
                            entryViewModel.updateJournal(journalEntry);

                            Utils.showSnackbar(R.string.firebase_success_msg, mRootView);
                        })
                        .addOnFailureListener(e -> {
                            Utils.showSnackbar(R.string.firebase_success_failure, mRootView);
                            Log.e(JournalActivity.class.getSimpleName(), e.getMessage());
                        });*/
            }

            finish();
        }

    }

    private void buildTitle(String content) {
        if (TextUtils.isEmpty(title)) {
            //get Title from content

            String[] contentArr = content.split(" ");
            StringBuilder titleBuilder = new StringBuilder();

            title = contentArr.length <= 3 ? content : titleBuilder
                    .append(contentArr[0]).append(" ")
                    .append(contentArr[1]).append(" ")
                    .append(contentArr[2]).toString();

        }
    }

    private Intent initContentShare() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(content)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    @Override
    public void pickCategory(int category) {

        if (isEdit) {
            selectedCategoryId = category;
            Log.d(TAG, "selected category: " + selectedCategoryId);
            if (deleteEntry) {
                deleteFromOldCategory();
            }
            createCopyJournal(selectedCategoryId);
        } else {
            Utils.showSnackbar(R.string.restrict_copy_or_delete, mRootView);
        }


    }

    private void createCopyJournal(int categoryId) {
        entryViewModel.saveJournal(new JournalEntry(content, new Date().getTime(), categoryId, title));
        Utils.showSnackbar(R.string.success_copy_or_move, mRootView);
        new Handler().postDelayed(() -> finish(), Snackbar.LENGTH_SHORT);
    }
}
