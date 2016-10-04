package com.ruthiefloats.popularmoviesstage2;

import android.app.Activity;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.widget.LinearLayout;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        if (uiDevice.isScreenOn()) {
            uiDevice.setOrientationLeft();
            uiDevice.unfreezeRotation();
        }
    }

    /*Test that menu works */
    @Test
    public void testUiAutomatorApi() throws RemoteException, UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        if (device.isScreenOn()) {
            assertTrue(device.pressMenu());

        }

    }

    /*Test that menu works */
    @Test
    public void testUiAutomatorApi_2() throws RemoteException, UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        if (device.isScreenOn()) {
            assertTrue(device.pressMenu());
            UiSelector selector = new UiSelector();
            selector.className("android.widget.LinearLayout");
            UiObject hopefully = device.findObject(selector);

            String name = "Sort By Popularity";
            UiScrollable listView = new UiScrollable(new UiSelector());
            listView.setMaxSearchSwipes(100);
            listView.scrollTextIntoView(name);
            listView.waitForExists(5000);
            UiObject listViewItem = listView.getChildByText(new UiSelector()
                    .className(android.widget.TextView.class.getName()), ""+name+"");
            listViewItem.click();
            System.out.println("\""+name+"\" ListView item was clicked.");
        }
    }
}