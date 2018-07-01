package com.migs.learn.acl.journalapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.migs.learn.acl.journalapp.database.models.JournalEntry;

import java.util.List;

@Dao
public interface JournalEntryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJournal(JournalEntry journalEntry);

    @Query("SELECT * FROM journal WHERE journalId = :journalId")
    LiveData<JournalEntry> getJournalById(int journalId);

    @Query("SELECT * FROM journal WHERE categoryId = :categoryId ORDER BY createDate DESC")
    LiveData<List<JournalEntry>> getAllJournalsByCategory(int categoryId);

    @Query("SELECT * FROM journal ORDER BY createDate DESC")
    LiveData<List<JournalEntry>> getAllJournals();

    @Update
    void updateJournal(JournalEntry journalEntry);

    @Delete
    void deleteJournal(JournalEntry journalEntry);
}
