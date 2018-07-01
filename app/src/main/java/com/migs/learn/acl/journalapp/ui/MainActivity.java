package com.migs.learn.acl.journalapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.migs.learn.acl.journalapp.R;
import com.migs.learn.acl.journalapp.adapters.CategoryAdapter;
import com.migs.learn.acl.journalapp.database.models.JournalCategory;
import com.migs.learn.acl.journalapp.utils.GlideApp;
import com.migs.learn.acl.journalapp.utils.IconSet;
import com.migs.learn.acl.journalapp.utils.Utils;
import com.migs.learn.acl.journalapp.viewmodel.JournalCategoryViewModel;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryClickHandler, AppBarLayout.OnOffsetChangedListener {

    public static final int CATEGORY_SAVE_CODE = 145;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private CategoryAdapter categoryAdapter;
    private RecyclerView rvCategories;
    private JournalCategoryViewModel categoryViewModel;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private int mMaxScrollSize;
    private TextView tvEmail;
    private TextView tvUsername;
    //private FirebaseFirestore firestore;
    private FirebaseUser user;
    private String username;
    private String email;
    private View mRootView;

    public static Intent createIntent(Context context, IdpResponse idpResponse) {
        return new Intent().setClass(context, MainActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, idpResponse);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(SignUpActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //firestore = FirebaseFirestore.getInstance();

        intViews();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivityForResult(new Intent(MainActivity.this, CategoryActivity.class), CATEGORY_SAVE_CODE));
    }

    private void intViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mRootView = findViewById(R.id.rootView);
        tvEmail = findViewById(R.id.tv_email);
        tvUsername = findViewById(R.id.tv_username);
        mProfileImage = findViewById(R.id.materialup_profile_image);
        rvCategories = findViewById(R.id.rv_categories);
        AppBarLayout appbarLayout = findViewById(R.id.materialup_appbar);

        appbarLayout.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);


        username = user.getDisplayName();
        email = user.getEmail();

        tvUsername.setText(username);
        tvEmail.setText(email);
        if (user.getPhotoUrl() != null) {
            GlideApp.with(this)
                    .load(user.getPhotoUrl())
                    .fitCenter()
                    .into(mProfileImage);
        }

        //createFireUser();
        initRecyclerView();
    }

    /*private void createFireUser() {
        final User appUser = new User(email, username);
        CollectionReference addUser = firestore.collection("users");
        addUser.document(email).set(user)
                .addOnSuccessListener(aVoid -> Log.i(MainActivity.class.getSimpleName(), appUser.getEmail()))
                .addOnFailureListener(e -> Log.d(MainActivity.class.getSimpleName(), e.getMessage()));
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CATEGORY_SAVE_CODE && resultCode == RESULT_OK && data != null) {

            String category = data.getStringExtra(CategoryActivity.CATEGORY_DATA);
            JournalCategory journalCategory = new JournalCategory(category, IconSet.AB.getIconId());

            categoryViewModel.createCategory(journalCategory);
            Utils.showSnackbar(R.string.category_saved, mRootView);
        } else {
            Utils.showSnackbar(R.string.category_failed_save, mRootView);
        }
    }

    private void initRecyclerView() {

        categoryViewModel = ViewModelProviders.of(this).get(JournalCategoryViewModel.class);
        categoryAdapter = new CategoryAdapter(this);
        categoryAdapter.setCategoryClickHandler(this);

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

        if (id == R.id.action_sign_out) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(SignUpActivity.createIntent(MainActivity.this));
                        finish();
                    } else {
                        Log.w(TAG, "signOut:failure", task.getException());
                        Utils.showSnackbar(R.string.sign_out_failed, mRootView);
                    }
                });
    }

    @Override
    public void onClickCategory(Integer categoryId) {
        // navigate to Journals and filter by categoryId passed
        Intent openJournals = new Intent(this, JournalsActivity.class);
        openJournals.putExtra(JournalsActivity.CATEGORY_ID, categoryId);
        startActivity(openJournals);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }

    }
}
