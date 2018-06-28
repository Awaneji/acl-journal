package com.migs.learn.acl.journalapp.database.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "journal", foreignKeys = @ForeignKey(entity = JournalCategory.class, parentColumns = {"categoryId"}, childColumns = {"categoryId"}),
        indices = @Index(value = {"categoryId"}, name = "categoryId"))
public class JournalEntry {

    @PrimaryKey
    private Integer journalId;

    @NonNull
    private String content;

    @NonNull
    private String title;

    @NonNull
    private Long createDate;

    @NonNull
    private Integer categoryId;

    @Ignore
    public JournalEntry(Integer journalId, @NonNull String content, @NonNull Long createDate, @NonNull Integer categoryId, @NonNull String title) {
        this.journalId = journalId;
        this.content = content;
        this.createDate = createDate;
        this.categoryId = categoryId;
        this.title = title;
    }

    public JournalEntry(@NonNull String content, @NonNull Long createDate, @NonNull Integer categoryId, @NonNull String title) {
        this.content = content;
        this.createDate = createDate;
        this.categoryId = categoryId;
        this.title = title;
    }

    public Integer getJournalId() {
        return journalId;
    }

    public void setJournalId(Integer journalId) {
        this.journalId = journalId;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    @NonNull
    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(@NonNull Long createDate) {
        this.createDate = createDate;
    }

    @NonNull
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull Integer categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }
}
