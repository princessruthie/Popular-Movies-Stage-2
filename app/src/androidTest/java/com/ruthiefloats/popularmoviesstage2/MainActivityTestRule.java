package com.ruthiefloats.popularmoviesstage2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

/**
 * TODO: add a class header comment.
 */

public class MainActivityTestRule<A extends Activity> extends ActivityTestRule<A> {

    private static final String LOG_TAG = "MainActyTestRule";

    /**
     * Launches the Activity under test.
     * <p>
     * Don't call this method directly, unless you explicitly requested not to launch the Activity
     * manually using the launchActivity flag in
     * {@link ActivityTestRule#ActivityTestRule(Class, boolean, boolean)}.
     * <p>
     * Usage:
     * <pre>
     *    &#064;Test
     *    public void customIntentToStartActivity() {
     *        Intent intent = new Intent(Intent.ACTION_PICK);
     *        mActivity = mActivityRule.launchActivity(intent);
     *    }
     * </pre>
     *
     * @param startIntent The Intent that will be used to start the Activity under test. If
     *                    {@code startIntent} is null, the Intent returned by
     *                    {@link ActivityTestRule#getActivityIntent()} is used.
     * @return the Activity launched by this rule.
     * @see ActivityTestRule#getActivityIntent()
     */
    @Override
    public A launchActivity(@Nullable Intent startIntent) {
        return super.launchActivity(startIntent);
    }

    /**
     * Override this method to set up Intent as if supplied to
     * {@link Context#startActivity}.
     * <p>
     * The default Intent (if this method returns null or is not overwritten) is:
     * action = {@link Intent#ACTION_MAIN}
     * flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
     * All other intent fields are null or empty.
     *
     * @return The Intent as if supplied to {@link Context#startActivity}.
     */
    @Override
    protected Intent getActivityIntent() {
        Log.i(LOG_TAG, "get Acty Intent");
        return super.getActivityIntent();
    }

    /**
     * Override this method to execute any code that should run before your {@link Activity} is
     * created and launched.
     * This method is called before each test method, including any method annotated with
     * <a href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before</code></a>.
     */
    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        Log.i(LOG_TAG, "before acty launched");
    }

    /**
     * Override this method to execute any code that should run after your {@link Activity} is
     * launched, but before any test code is run including any method annotated with
     * <a href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before</code></a>.
     * <p>
     * Prefer
     * <a href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before</code></a>
     * over this method. This method should usually not be overwritten directly in tests and only be
     * used by subclasses of ActivityTestRule to get notified when the activity is created and
     * visible but test runs.
     */
    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();
        Log.i(LOG_TAG, "after acty launched");
    }

    /**
     * Override this method to execute any code that should run after your {@link Activity} is
     * finished.
     * This method is called after each test method, including any method annotated with
     * <a href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>After</code></a>.
     */
    // TODO: 9/21/16 do you call the super before or after on the onFinished?
    @Override
    protected void afterActivityFinished() {
        Log.i(LOG_TAG, "after acty finished pre super");
        super.afterActivityFinished();
        Log.i(LOG_TAG, "after act finished post super");
    }

    /**
     * @return The activity under test.
     */
    @Override
    public A getActivity() {
        Log.i(LOG_TAG, "get acty");
        return super.getActivity();
    }

    public MainActivityTestRule(Class<A> activityClass) {
        super(activityClass);
        Log.i(LOG_TAG, "constructor");
    }
}
