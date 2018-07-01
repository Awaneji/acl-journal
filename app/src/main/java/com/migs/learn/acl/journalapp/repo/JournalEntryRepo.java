package com.migs.learn.acl.journalapp.repo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.migs.learn.acl.journalapp.database.JournalDb;
import com.migs.learn.acl.journalapp.database.dao.JournalEntryDAO;
import com.migs.learn.acl.journalapp.database.models.JournalEntry;

import java.util.List;

public class JournalEntryRepo {
    private JournalEntryDAO journalEntryDAO;

    public JournalEntryRepo(Application context) {
        this.journalEntryDAO = JournalDb.getInstance(context).journalEntryDAO();
    }

    public void saveJournal(JournalEntry entry) {
        new InsertJournalAsync(journalEntryDAO).execute(entry);
    }

    public void updateJournal(JournalEntry entry) {
        new UpdateJournalAsync(journalEntryDAO).execute(entry);
    }

    public void deleteJournal(JournalEntry entry) {
        new DeleteJournalAsync(journalEntryDAO).execute(entry);
    }

    public LiveData<List<JournalEntry>> getAllJournalEntriesByCategory(int categoryId) {
        return journalEntryDAO.getAllJournalsByCategory(categoryId);
    }

    public LiveData<List<JournalEntry>> getAllJournals() {
        return journalEntryDAO.getAllJournals();
    }

    public LiveData<JournalEntry> getJournalEntryById(int journalId) {
        return journalEntryDAO.getJournalById(journalId);
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertJournalAsync extends AsyncTask<JournalEntry, Void, Void> {
        private JournalEntryDAO journalDAO;

        public InsertJournalAsync(JournalEntryDAO journalDAO) {
            this.journalDAO = journalDAO;
        }

        @Override
        protected Void doInBackground(JournalEntry... journalEntries) {
            journalDAO.insertJournal(journalEntries[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteJournalAsync extends AsyncTask<JournalEntry, Void, Void> {
        private JournalEntryDAO journalDAO;

        public DeleteJournalAsync(JournalEntryDAO journalDAO) {
            this.journalDAO = journalDAO;
        }

        @Override
        protected Void doInBackground(JournalEntry... journalEntries) {
            journalDAO.deleteJournal(journalEntries[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateJournalAsync extends AsyncTask<JournalEntry, Void, Void> {
        private JournalEntryDAO journalDAO;

        public UpdateJournalAsync(JournalEntryDAO journalDAO) {
            this.journalDAO = journalDAO;
        }

        @Override
        protected Void doInBackground(JournalEntry... journalEntries) {
            journalDAO.updateJournal(journalEntries[0]);
            return null;
        }
    }
}
