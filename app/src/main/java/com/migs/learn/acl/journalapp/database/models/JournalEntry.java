package com.migs.learn.acl.journalapp.database.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

//@IgnoreExtraProperties
@Entity(tableName = "journal",
        foreignKeys = @ForeignKey(entity = JournalCategory.class,
                parentColumns = {"categoryId"},
                childColumns = {"categoryId"},
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = {"categoryId"}, name = "categoryId"))
public class JournalEntry {

    @PrimaryKey
    private Integer journalId;

    //@PropertyName("content")
    private String content;

    //@PropertyName("title")
    private String title;

    private Long createDate;

    private Integer categoryId;

    private String firebaseRef;

    @Ignore
    //@ServerTimestamp
    private Date timeStamp;

    @Ignore
    public JournalEntry() {
    }

    @Ignore
    public JournalEntry(Integer journalId, String content, Long createDate, Integer categoryId, String title) {
        this.journalId = journalId;
        this.content = content;
        this.createDate = createDate;
        this.categoryId = categoryId;
        this.title = title;
    }

    @Ignore
    public JournalEntry(String content, String title) {
        this.content = content;
        this.title = title;
    }

    public JournalEntry(String content, Long createDate, Integer categoryId, String title) {
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

    public String getFirebaseRef() {
        return firebaseRef;
    }

    public void setFirebaseRef(String firebaseRef) {
        this.firebaseRef = firebaseRef;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
