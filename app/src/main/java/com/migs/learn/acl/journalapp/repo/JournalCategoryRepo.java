package com.migs.learn.acl.journalapp.repo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.migs.learn.acl.journalapp.database.JournalDb;
import com.migs.learn.acl.journalapp.database.dao.JournalCategoryDAO;
import com.migs.learn.acl.journalapp.database.models.JournalCategory;

import java.util.List;

public class JournalCategoryRepo {
    private JournalCategoryDAO journalCategoryDAO;

    public JournalCategoryRepo(Application context) {
        this.journalCategoryDAO = JournalDb.getInstance(context).journalCategoryDAO();
    }

    public LiveData<List<JournalCategory>> getCategories() {
        return journalCategoryDAO.getAllJournalCategories();
    }

    public LiveData<JournalCategory> getCategoryById(int categoryId) {
        return journalCategoryDAO.getJournalCategoryById(categoryId);
    }

    public void saveCategory(JournalCategory category) {
        new InsertJournalCategoryAsync(journalCategoryDAO).execute(category);

    }

    @SuppressLint("StaticFieldLeak")
    private class InsertJournalCategoryAsync extends AsyncTask<JournalCategory, Void, Void> {

        private JournalCategoryDAO categoryDAO;

        public InsertJournalCategoryAsync(JournalCategoryDAO journalCategoryDAO) {
            this.categoryDAO = journalCategoryDAO;
        }

        @Override
        protected Void doInBackground(JournalCategory... journalCategories) {
            categoryDAO.insertJournalCategory(journalCategories[0]);
            return null;
        }
    }
}
