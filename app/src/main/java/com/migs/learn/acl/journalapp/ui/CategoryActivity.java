package com.migs.learn.acl.journalapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.migs.learn.acl.journalapp.R;

public class CategoryActivity extends AppCompatActivity {

    public static final String CATEGORY_DATA = "category-data";
    private TextInputEditText etCategory;
    private Button btnSave;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        etCategory = findViewById(R.id.et_category);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent data = new Intent();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etCategory.getText().toString())) {
                    setResult(RESULT_CANCELED, data);
                } else {
                    String category = etCategory.getText().toString().trim();
                    data.putExtra(CATEGORY_DATA, category);
                    setResult(RESULT_OK, data);
                }

                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, data);
                finish();
            }
        });
    }
}

