/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArraySet;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

/**
 * This class represents a delegate which you can use to extend AppCompat's support to any
 * {@link android.app.Activity}.
 *
 * <p>When using an {@link AppCompatDelegate}, you should call the following methods instead of the
 * {@link android.app.Activity} method of the same name:</p>
 * <ul>
 *     <li>{@link #addContentView(android.view.View, android.view.ViewGroup.LayoutParams)}</li>
 *     <li>{@link #setContentView(int)}</li>
 *     <li>{@link #setContentView(android.view.View)}</li>
 *     <li>{@link #setContentView(android.view.View, android.view.ViewGroup.LayoutParams)}</li>
 *     <li>{@link #requestWindowFeature(int)}</li>
 *     <li>{@link #hasWindowFeature(int)}</li>
 *     <li>{@link #invalidateOptionsMenu()}</li>
 *     <li>{@link #startSupportActionMode(androidx.appcompat.view.ActionMode.Callback)}</li>
 *     <li>{@link #setSupportActionBar(androidx.appcompat.widget.Toolbar)}</li>
 *     <li>{@link #getSupportActionBar()}</li>
 *     <li>{@link #getMenuInflater()}</li>
 *     <li>{@link #findViewById(int)}</li>
 * </ul>
 *
 * <p>The following methods should be called from the {@link android.app.Activity} method of the
 * same name:</p>
 * <ul>
 *     <li>{@link #onCreate(android.os.Bundle)}</li>
 *     <li>{@link #onPostCreate(android.os.Bundle)}</li>
 *     <li>{@link #onConfigurationChanged(android.content.res.Configuration)}</li>
 *     <li>{@link #onStart()}</li>
 *     <li>{@link #onStop()}</li>
 *     <li>{@link #onPostResume()}</li>
 *     <li>{@link #onSaveInstanceState(Bundle)}</li>
 *     <li>{@link #setTitle(CharSequence)}</li>
 *     <li>{@link #onStop()}</li>
 *     <li>{@link #onDestroy()}</li>
 * </ul>
 *
 * <p>An {@link Activity} can only be linked with one {@link AppCompatDelegate} instance,
 * therefore the instance returned from {@link #create(Activity, AppCompatCallback)} should be
 * retained until the Activity is destroyed.</p>
 */
public abstract class AppCompatDelegate {
    /**
     * Mode which uses the system's night mode setting to determine if it is night or not.
     *
     * @see #setLocalNightMode(int)
     */
    public static final int MODE_NIGHT_FOLLOW_SYSTEM = -1;
    /**
     * Night mode which switches between dark and light mode depending on the time of day
     * (dark at night, light in the day).
     *
     * <p>The calculation used to determine whether it is night or not makes use of the location
     * APIs (if this app has the necessary permissions). This allows us to generate accurate
     * sunrise and sunset times. If this app does not have permission to access the location APIs
     * then we use hardcoded times which will be less accurate.</p>
     *
     * @deprecated Automatic switching of dark/light based on the current time is deprecated.
     * Considering using an explicit setting, or {@link #MODE_NIGHT_AUTO_BATTERY}.
     */
    @Deprecated
    public static final int MODE_NIGHT_AUTO_TIME = 0;
    /**
     * @deprecated Use {@link AppCompatDelegate#MODE_NIGHT_AUTO_TIME} instead
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static final int MODE_NIGHT_AUTO = MODE_NIGHT_AUTO_TIME;
    /**
     * Night mode which uses always uses a light mode, enabling {@code notnight} qualified
     * resources regardless of the time.
     *
     * @see #setLocalNightMode(int)
     */
    public static final int MODE_NIGHT_NO = 1;
    /**
     * Night mode which uses always uses a dark mode, enabling {@code night} qualified
     * resources regardless of the time.
     *
     * @see #setLocalNightMode(int)
     */
    public static final int MODE_NIGHT_YES = 2;
    /**
     * Night mode which uses a dark mode when the system's 'Battery Saver' feature is enabled,
     * otherwise it uses a 'light mode'. This mode can help the device to decrease power usage,
     * depending on the display technology in the device.
     *
     * <em>Please note: this mode should only be used when running on devices which do not
     * provide a similar device-wide setting.</em>
     *
     * @see #setLocalNightMode(int)
     */
    public static final int MODE_NIGHT_AUTO_BATTERY = 3;
    /**
     * An unspecified mode for night mode. This is primarily used with
     * {@link #setLocalNightMode(int)}, to allow the default night mode to be used.
     * If both the default and local night modes are set to this value, then the default value of
     * {@link #MODE_NIGHT_FOLLOW_SYSTEM} is applied.
     *
     * @see AppCompatDelegate#setDefaultNightMode(int)
     */
    public static final int MODE_NIGHT_UNSPECIFIED = -100;
    /**
     * Flag for enabling the support Action Bar.
     *
     * <p>This is enabled by default for some devices. The Action Bar replaces the title bar and
     * provides an alternate location for an on-screen menu button on some devices.
     */
    public static final int FEATURE_SUPPORT_ACTION_BAR = 100 + WindowCompat.FEATURE_ACTION_BAR;
    /**
     * Flag for requesting an support Action Bar that overlays window content.
     * Normally an Action Bar will sit in the space above window content, but if this
     * feature is requested along with {@link #FEATURE_SUPPORT_ACTION_BAR} it will be layered over
     * the window content itself. This is useful if you would like your app to have more control
     * over how the Action Bar is displayed, such as letting application content scroll beneath
     * an Action Bar with a transparent background or otherwise displaying a transparent/translucent
     * Action Bar over application content.
     *
     * <p>This mode is especially useful with {@code View.SYSTEM_UI_FLAG_FULLSCREEN}, which allows
     * you to seamlessly hide the action bar in conjunction with other screen decorations.
     * When an ActionBar is in this mode it will adjust the insets provided to
     * {@link View#fitSystemWindows(android.graphics.Rect) View.fitSystemWindows(Rect)}
     * to include the content covered by the action bar, so you can do layout within
     * that space.
     */
    public static final int FEATURE_SUPPORT_ACTION_BAR_OVERLAY =
            100 + WindowCompat.FEATURE_ACTION_BAR_OVERLAY;
    /**
     * Flag for specifying the behavior of action modes when an Action Bar is not present.
     * If overlay is enabled, the action mode UI will be allowed to cover existing window content.
     */
    public static final int FEATURE_ACTION_MODE_OVERLAY = WindowCompat.FEATURE_ACTION_MODE_OVERLAY;
    static final boolean DEBUG = true;
    static final String TAG = "AppCompatDelegate";
    private static final ArraySet<WeakReference<AppCompatDelegate>> sActiveDelegates =
            new ArraySet<>();
    private static final Object sActiveDelegatesLock = new Object();
    @NightMode
    private static int sDefaultNightMode = MODE_NIGHT_UNSPECIFIED;

    /**
     * Private constructor
     */
    AppCompatDelegate() {
    }

    /**
     * Create an {@link androidx.appcompat.app.AppCompatDelegate} to use with {@code activity}.
     *
     * @param callback An optional callback for AppCompat specific events
     */
    @NonNull
    public static AppCompatDelegate create(@NonNull Activity activity,
                                           @Nullable AppCompatCallback callback) {
        return new AppCompatDelegateImpl(activity, callback);
    }

    /**
     * Create an {@link androidx.appcompat.app.AppCompatDelegate} to use with {@code dialog}.
     *
     * @param callback An optional callback for AppCompat specific events
     */
    @NonNull
    public static AppCompatDelegate create(@NonNull Dialog dialog,
                                           @Nullable AppCompatCallback callback) {
        return new AppCompatDelegateImpl(dialog, callback);
    }

    /**
     * Create an {@link androidx.appcompat.app.AppCompatDelegate} to use with a {@code context}
     * and a {@code window}.
     *
     * @param callback An optional callback for AppCompat specific events
     */
    @NonNull
    public static AppCompatDelegate create(@NonNull Context context, @NonNull Window window,
                                           @Nullable AppCompatCallback callback) {
        return new AppCompatDelegateImpl(context, window, callback);
    }

    /**
     * Create an {@link androidx.appcompat.app.AppCompatDelegate} to use with a {@code context}
     * and hosted by an {@code Activity}.
     *
     * @param callback An optional callback for AppCompat specific events
     */
    @NonNull
    public static AppCompatDelegate create(@NonNull Context context, @NonNull Activity activity,
                                           @Nullable AppCompatCallback callback) {
        return new AppCompatDelegateImpl(context, activity, callback);
    }

    /**
     * Support library version of {@link Activity#getActionBar}.
     *
     * @return AppCompat's action bar, or null if it does not have one.
     */
    @Nullable
    public abstract ActionBar getSupportActionBar();

    /**
     * Set a {@link Toolbar} to act as the {@link ActionBar} for this delegate.
     *
     * <p>When set to a non-null value the {@link #getSupportActionBar()} ()} method will return
     * an {@link ActionBar} object that can be used to control the given toolbar as if it were
     * a traditional window decor action bar. The toolbar's menu will be populated with the
     * Activity's options menu and the navigation button will be wired through the standard
     * {@link android.R.id#home home} menu select action.</p>
     *
     * <p>In order to use a Toolbar within the Activity's window content the application
     * must not request the window feature
     * {@link AppCompatDelegate#FEATURE_SUPPORT_ACTION_BAR FEATURE_SUPPORT_ACTION_BAR}.</p>
     *
     * @param toolbar Toolbar to set as the Activity's action bar, or {@code null} to clear it
     */
    public abstract void setSupportActionBar(@Nullable Toolbar toolbar);

    /**
     * Return the value of this call from your {@link Activity#getMenuInflater()}
     */
    public abstract MenuInflater getMenuInflater();

    /**
     * Should be called from {@link Activity#onCreate Activity.onCreate()}.
     *
     * <p>This should be called before {@code super.onCreate()} as so:</p>
     * <pre class="prettyprint">
     * protected void onCreate(Bundle savedInstanceState) {
     *     getDelegate().onCreate(savedInstanceState);
     *     super.onCreate(savedInstanceState);
     *     // ...
     * }
     * </pre>
     */
    public abstract void onCreate(Bundle savedInstanceState);

    /**
     * Should be called from {@link Activity#onPostCreate(android.os.Bundle)}
     */
    public abstract void onPostCreate(Bundle savedInstanceState);

    /**
     * Should be called from
     * {@link Activity#onConfigurationChanged}
     */
    public abstract void onConfigurationChanged(Configuration newConfig);

    /**
     * Should be called from {@link Activity#onStart()} Activity.onStart()}
     */
    public abstract void onStart();

    /**
     * Should be called from {@link Activity#onStop Activity.onStop()}
     */
    public abstract void onStop();

    /**
     * Should be called from {@link Activity#onPostResume()}
     */
    public abstract void onPostResume();

    /**
     * This should be called from {@link Activity#setTheme(int)} to notify AppCompat of what
     * the current theme resource id is.
     */
    public void setTheme(@StyleRes int themeResId) {
    }

    /**
     * Finds a view that was identified by the id attribute from the XML that
     * was processed in {@link #onCreate}.
     *
     * @return The view if found or null otherwise.
     */
    @SuppressWarnings("TypeParameterUnusedInFormals")
    @Nullable
    public abstract <T extends View> T findViewById(@IdRes int id);

    /**
     * Should be called instead of {@link Activity#setContentView(android.view.View)}}
     */
    public abstract void setContentView(View v);

    /**
     * Should be called instead of {@link Activity#setContentView(int)}}
     */
    public abstract void setContentView(@LayoutRes int resId);

    /**
     * Should be called instead of
     * {@link Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)}}
     */
    public abstract void setContentView(View v, ViewGroup.LayoutParams lp);

    /**
     * Should be called instead of
     * {@link Activity#addContentView(android.view.View, android.view.ViewGroup.LayoutParams)}}
     */
    public abstract void addContentView(View v, ViewGroup.LayoutParams lp);

    /**
     * @deprecated use {@link #attachBaseContext2(Context)} instead.
     */
    @Deprecated
    public void attachBaseContext(Context context) {
    }

    /**
     * Should be called from {@link Activity#attachBaseContext(Context)}.
     */
    @NonNull
    @CallSuper
    public Context attachBaseContext2(@NonNull Context context) {
        attachBaseContext(context);
        return context;
    }

    /**
     * Should be called from {@link Activity#onTitleChanged(CharSequence, int)}}
     */
    public abstract void setTitle(@Nullable CharSequence title);

    /**
     * Should be called from {@link Activity#invalidateOptionsMenu()}} or
     * {@link FragmentActivity#supportInvalidateOptionsMenu()}.
     */
    public abstract void invalidateOptionsMenu();

    /**
     * Should be called from {@link Activity#onDestroy()}
     */
    public abstract void onDestroy();

    /**
     * Returns an {@link ActionBarDrawerToggle.Delegate} which can be returned from your Activity
     * if it implements {@link ActionBarDrawerToggle.DelegateProvider}.
     */
    @Nullable
    public abstract ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();

    /**
     * @hide
     */
    @SuppressWarnings("deprecation")
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    @IntDef({MODE_NIGHT_NO, MODE_NIGHT_YES, MODE_NIGHT_AUTO_TIME, MODE_NIGHT_FOLLOW_SYSTEM,
            MODE_NIGHT_UNSPECIFIED, MODE_NIGHT_AUTO_BATTERY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NightMode {
    }

    @IntDef({MODE_NIGHT_NO, MODE_NIGHT_YES, MODE_NIGHT_FOLLOW_SYSTEM})
    @Retention(RetentionPolicy.SOURCE)
    @interface ApplyableNightMode {
    }

/**
 * Enable extended window features.  This should be called instead of
 * {@link android.app.Activity#requestWindowFeature(int)} or
 * {@link android.view.Window#requestFeature getWindow().requestFeature()}.
 *
 * @param featureId The desired feature as defi
n