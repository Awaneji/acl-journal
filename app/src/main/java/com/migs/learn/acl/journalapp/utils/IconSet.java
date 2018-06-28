package com.migs.learn.acl.journalapp.utils;

import com.migs.learn.acl.journalapp.R;

public enum IconSet {
    AB(R.drawable.ic_launcher_background);

    private int iconId;

    IconSet(int iconId) {
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }
}
