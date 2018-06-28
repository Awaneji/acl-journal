package com.migs.learn.acl.journalapp.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "category", indices = @Index(unique = true, name = "description", value = {"description"}))
public class JournalCategory {
    @PrimaryKey
    private Integer categoryId;

    @NonNull
    @ColumnInfo(name = "description")
    private String categoryName;

    @NonNull
    @ColumnInfo(name = "icon")
    private Integer iconId;

    @Ignore
    public JournalCategory(Integer categoryId, @NonNull String categoryName, @NonNull Integer iconId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.iconId = iconId;
    }

    public JournalCategory(@NonNull String categoryName, @NonNull Integer iconId) {
        this.categoryName = categoryName;
        this.iconId = iconId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(@NonNull String categoryName) {
        this.categoryName = categoryName;
    }

    @NonNull
    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(@NonNull Integer iconId) {
        this.iconId = iconId;
    }
}
