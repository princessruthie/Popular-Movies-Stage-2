package com.ruthiefloats.popularmoviesstage2;

import android.app.Activity;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Learning to write tests.  Just a few to start.
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

    /*had a bug where if the user changed orientation before the movie list had been set,the
    resume methods would pass a null list to the recyclerviewadapter.  never again.
     */
    @Test
    public void testUiDevice() throws RemoteException {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        if (uiDevice.isScreenOn()){
            uiDevice.setOrientationLeft();
            uiDevice.setOrientationNatural();
        }
    }
}