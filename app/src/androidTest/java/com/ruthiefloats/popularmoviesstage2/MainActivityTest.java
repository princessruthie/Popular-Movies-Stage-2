package com.ruthiefloats.popularmoviesstage2;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TODO: add a class header comment.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public MainActivityTestRule<MainActivity> mMainActivityActivityTestRule = (MainActivityTestRule<MainActivity>) new MainActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testUi() {
        Activity activity = mMainActivityActivityTestRule.getActivity();
        assertNotNull(activity.findViewById(R.id.master_container));
        assertEquals(2, 2);
    }

    @Test
    public void testUi_2() {
        Activity activity = mMainActivityActivityTestRule.getActivity();
        assertNotNull(activity.findViewById(R.id.master_container));
        assertEquals(2, 2);
    }
}