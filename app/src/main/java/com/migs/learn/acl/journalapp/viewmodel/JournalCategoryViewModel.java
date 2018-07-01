package com.migs.learn.acl.journalapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.migs.learn.acl.journalapp.database.models.JournalCategory;
import com.migs.learn.acl.journalapp.repo.JournalCategoryRepo;
import com.migs.learn.acl.journalapp.utils.AbsentLiveData;
import com.migs.learn.acl.journalapp.utils.Objects;

import java.util.List;

public class JournalCategoryViewModel extends AndroidViewModel {
    private LiveData<JournalCategory> categoryLiveData;
    private MutableLiveData<Integer> categoryIdData = new MutableLiveData<>();
    private LiveData<List<JournalCategory>> categories;
    private LiveData<List<JournalCategory>> categoriesSelect;
    private JournalCategoryRepo categoryRepo;

    public JournalCategoryViewModel(@NonNull Application application) {
        super(application);

        categoryRepo = new JournalCategoryRepo(application);

        categories = categoryRepo.getCategories();

        categoryLiveData = Transformations.switchMap(categoryIdData, input -> {
            if (input == null)
                return AbsentLiveData.create();
            else
                return categoryRepo.getCategoryById(input);
        });

        categoriesSelect = Transformations.switchMap(categoryIdData, input -> {
            if (input == null)
                return AbsentLiveData.create();
            else
                return categoryRepo.getAllJournalCategoriesExcept(input);
        });

    }

    public LiveData<List<JournalCategory>> getCategoriesSelect() {
        return categoriesSelect;
    }

    public LiveData<JournalCategory> getCategory() {
        return categoryLiveData;
    }

    public LiveData<List<JournalCategory>> getCategories() {
        return categories;
    }

    public void createCategory(JournalCategory category) {
        categoryRepo.saveCategory(category);
    }

    public void deleteCategory(JournalCategory category) {
        categoryRepo.deleteCategory(category);
    }

    public void setCategoryIdData(Integer categoryId) {
        if (!Objects.equals(categoryIdData.getValue(), categoryId)) {
            categoryIdData.setValue(categoryId);
        }
    }
}
