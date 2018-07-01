package com.migs.learn.acl.journalapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import com.migs.learn.acl.journalapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testRecyclerClickWhenHasRows() {
        if (getRVCount() > 0) {
            onView(withId(R.id.rv_categories)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }
    }

    private int getRVCount() {
        RecyclerView recyclerView = mActivityRule.getActivity().findViewById(R.id.rv_categories);
        return recyclerView.getAdapter().getItemCount();
    }
}
