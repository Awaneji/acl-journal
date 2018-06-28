package com.migs.learn.acl.journalapp.database;

import android.annotation.SuppressLint;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.migs.learn.acl.journalapp.database.dao.JournalCategoryDAO;
import com.migs.learn.acl.journalapp.database.dao.JournalEntryDAO;
import com.migs.learn.acl.journalapp.database.models.JournalCategory;
import com.migs.learn.acl.journalapp.database.models.JournalEntry;
import com.migs.learn.acl.journalapp.utils.DateTypeConverter;
import com.migs.learn.acl.journalapp.utils.IconSet;

@Database(exportSchema = false, version = 1, entities = {JournalCategory.class, JournalEntry.class})
@TypeConverters({DateTypeConverter.class})
public abstract class JournalDb extends RoomDatabase {

    private static final String DATABASE_NAME = "journal_db";
    private static final String DEFAULT_CATEGORY = "Happy Thoughts";
    private static volatile JournalDb databaseInstance;

    // calllback to insert default Category data

    private static Callback insertDefaultCategory = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            //async call for default category inserts
            new DefaultCategoryInsertAsync(databaseInstance).execute();
        }
    };

    public static JournalDb getInstance(Context context) {
        if (databaseInstance == null) {
            synchronized (JournalDb.class) {
                if (databaseInstance == null) {
                    databaseInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            JournalDb.class,
                            DATABASE_NAME)
                            .addCallback(insertDefaultCategory)
                            .build();
                }
            }
        }
        return databaseInstance;
    }

    public abstract JournalCategoryDAO journalCategoryDAO();

    public abstract JournalEntryDAO journalEntryDAO();

    @SuppressLint("StaticFieldLeak")
    private static class DefaultCategoryInsertAsync extends AsyncTask<Void, Void, Void> {

        private JournalCategoryDAO categoryDAO;

        private DefaultCategoryInsertAsync(JournalDb db) {
            this.categoryDAO = db.journalCategoryDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JournalCategory category = new JournalCategory(DEFAULT_CATEGORY, IconSet.AB.getIconId());
            categoryDAO.insertJournalCategory(category);

            JournalCategory journalCategory = new JournalCategory("Another one",IconSet.AB.getIconId());
            categoryDAO.insertJournalCategory(journalCategory);
            categoryDAO.insertJournalCategory(new JournalCategory("My quotes",IconSet.AB.getIconId()));
            categoryDAO.insertJournalCategory(new JournalCategory("Wild thoughts", IconSet.AB.getIconId()));

            return null;
        }
    }


}
