package com.migs.learn.acl.journalapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.migs.learn.acl.journalapp.database.models.JournalEntry;
import com.migs.learn.acl.journalapp.repo.JournalEntryRepo;
import com.migs.learn.acl.journalapp.utils.AbsentLiveData;
import com.migs.learn.acl.journalapp.utils.Objects;

import java.util.List;

public class JournalEntryViewModel extends AndroidViewModel {
    private final JournalEntryRepo entryRepo;
    private LiveData<JournalEntry> journalEntryData;
    private LiveData<List<JournalEntry>> allJournalEntries;
    private LiveData<List<JournalEntry>> categoryJournalEntries;


    private MutableLiveData<Integer> journalIdData = new MutableLiveData<>();
    private MutableLiveData<Integer> categoryIdData = new MutableLiveData<>();

    public JournalEntryViewModel(@NonNull Application application) {
        super(application);
        entryRepo = new JournalEntryRepo(application);

        journalEntryData = Transformations.switchMap(journalIdData, input -> {
            if (input == null)
                return AbsentLiveData.create();
            else
                return entryRepo.getJournalEntryById(input);
        });

        allJournalEntries = entryRepo.getAllJournals();

        categoryJournalEntries = Transformations.switchMap(categoryIdData, input -> {
            if (input == null)
                return AbsentLiveData.create();
            else
                return entryRepo.getAllJournalEntriesByCategory(input);
        });
    }

    public LiveData<JournalEntry> getJournalEntryData() {
        return journalEntryData;
    }

    public LiveData<List<JournalEntry>> getAllJournalEntries() {
        return allJournalEntries;
    }

    public LiveData<List<JournalEntry>> getCategoryJournalEntries() {
        return categoryJournalEntries;
    }

    public void saveJournal(JournalEntry journalEntry) {
        entryRepo.saveJournal(journalEntry);
    }

    public void updateJournal(JournalEntry journalEntry) {
        entryRepo.updateJournal(journalEntry);
    }

    public void deleteJournal(JournalEntry journalEntry) {
        entryRepo.deleteJournal(journalEntry);
    }

    public void setJournalIdData(Integer journalId) {
        if (!Objects.equals(journalIdData.getValue(), journalId)) {
            journalIdData.setValue(journalId);
        }
    }

    public void setCategoryIdData(Integer categoryId) {
        if (!Objects.equals(categoryIdData.getValue(), categoryId)) {
            categoryIdData.setValue(categoryId);
        }
    }
}
