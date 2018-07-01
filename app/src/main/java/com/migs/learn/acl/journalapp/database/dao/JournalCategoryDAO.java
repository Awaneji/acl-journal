package com.migs.learn.acl.journalapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.migs.learn.acl.journalapp.database.models.JournalCategory;

import java.util.List;

@Dao
public interface JournalCategoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertJournalCategory(JournalCategory journalCategory);

    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    LiveData<JournalCategory> getJournalCategoryById(int categoryId);

    @Query("SELECT * FROM category")
    LiveData<List<JournalCategory>> getAllJournalCategories();

    @Query("SELECT * FROM category WHERE categoryId <> :categoryId ")
    LiveData<List<JournalCategory>> getAllJournalCategoriesExcept(int categoryId);

    @Delete
    void deleteCategory(JournalCategory journalCategory);
}
