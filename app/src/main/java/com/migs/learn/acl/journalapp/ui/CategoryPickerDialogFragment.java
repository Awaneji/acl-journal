package com.migs.learn.acl.journalapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.migs.learn.acl.journalapp.R;
import com.migs.learn.acl.journalapp.adapters.CategoryAdapter;
import com.migs.learn.acl.journalapp.viewmodel.JournalCategoryViewModel;

public class CategoryPickerDialogFragment extends DialogFragment implements CategoryAdapter.CategoryClickHandler {

    public static final String CATEGORY_ID = "categoryId";
    public static final String TAG = CategoryPickerDialogFragment.class.getSimpleName();
    private CategoryPickerHandler pickerHandler;
    private RecyclerView categoryRecycler;
    private JournalCategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;

    public CategoryPickerDialogFragment() {
    }

    public static CategoryPickerDialogFragment getInstance(int filterCategory) {
        CategoryPickerDialogFragment fragment = new CategoryPickerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CATEGORY_ID, filterCategory);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setPickerHandler(CategoryPickerHandler pickerHandler) {
        this.pickerHandler = pickerHandler;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_dialog, container);

        categoryViewModel = ViewModelProviders.of(this).get(JournalCategoryViewModel.class);
        categoryAdapter = new CategoryAdapter(this.getActivity());
        categoryAdapter.setCategoryClickHandler(this);

        categoryRecycler = rootView.findViewById(R.id.rv_categories);

        if (getArguments() != null)
            initRecyclerView(getArguments().getInt(CATEGORY_ID));

        this.getDialog().setTitle("Pick Category");

        return rootView;
    }


    private void initRecyclerView(int filterCategory) {

        categoryRecycler.setItemAnimator(new DefaultItemAnimator());
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        categoryViewModel.setCategoryIdData(filterCategory);
        categoryViewModel.getCategoriesSelect().observe(this, categoryAdapter::setCategoryList);

        categoryRecycler.setHasFixedSize(true);
        categoryRecycler.setAdapter(categoryAdapter);
    }

    @Override
    public void onClickCategory(Integer categoryId) {
        pickerHandler.pickCategory(categoryId);
        dismiss();
    }

    public interface CategoryPickerHandler {
        void pickCategory(int category);
    }
}
