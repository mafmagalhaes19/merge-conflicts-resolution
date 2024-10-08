/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.android.browser;

import com.google.android.providers.GoogleSettings.Partner;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemProperties;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.CacheManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.preference.PreferenceManager;
import android.provider.Browser;

import java.util.HashMap;
import java.util.Observable;

/*
 * Package level class for storing various WebView and Browser settings. To use
 * this class:
 * BrowserSettings s = BrowserSettings.getInstance();
 * s.addObserver(webView.getSettings());
 * s.loadFromDb(context); // Only needed on app startup
 * s.javaScriptEnabled = true;
 * ... // set any other settings
 * s.update(); // this will update all the observers
 *
 * To remove an observer:
 * s.deleteObserver(webView.getSettings());
 */
class BrowserSettings extends Observable {

    private static final String DEFAULT_HOME_URL =
            "http://www.google.com/m?client=ms-";
    // Private variables for settings
    // NOTE: these defaults need to be kept in sync with the XML
    // until the performance of PreferenceManager.setDefaultValues()
    // is improved.
    private boolean loadsImagesAutomatically = true;
    private boolean javaScriptEnabled = true;
    private boolean pluginsEnabled = true;
    private String pluginsPath;  // default value set in loadFromDb().
    private boolean javaScriptCanOpenWindowsAutomatically = false;
    private boolean showSecurityWarnings = true;
    private boolean rememberPasswords = true;
    private boolean saveFormData = true;
    private boolean openInBackground = false;
    private String defaultTextEncodingName;
    private String homeUrl;
    private boolean loginInitialized = false;
    private boolean autoFitPage = true;
    private boolean landscapeOnly = false;
    private boolean showDebugSettings = false;
    private String databasePath; // default value set in loadFromDb()
    private boolean databaseEnabled = true;
    private long webStorageDefaultQuota = 5 * 1024 * 1024;
    // The Browser always enables Application Caches.
    private boolean appCacheEnabled = true;
    private String appCachePath;  // default value set in loadFromDb().

    private final static String TAG = "BrowserSettings";

    // Development settings
    public WebSettings.LayoutAlgorithm layoutAlgorithm =
        WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
    private boolean useWideViewPort = true;
    private int userAgent = 0;
    private boolean tracing = false;
    private boolean lightTouch = false;
    private boolean navDump = false;
    // Browser only settings
    private boolean doFlick = false;

    // Private preconfigured values
    private static int minimumFontSize = 8;
    private static int minimumLogicalFontSize = 8;
    private static int defaultFontSize = 16;
    private static int defaultFixedFontSize = 13;
    private static WebSettings.TextSize textSize =
        WebSettings.TextSize.NORMAL;

    // Preference keys that are used outside this class
    public final static String PREF_CLEAR_CACHE = "privacy_clear_cache";
    public final static String PREF_CLEAR_COOKIES = "privacy_clear_cookies";
    public final static String PREF_CLEAR_HISTORY = "privacy_clear_history";
    public final static String PREF_HOMEPAGE = "homepage";
    public final static String PREF_CLEAR_FORM_DATA =
            "privacy_clear_form_data";
    public final static String PREF_CLEAR_PASSWORDS =
            "privacy_clear_passwords";
    public final static String PREF_CLEAR_DATABASES =
            "webstorage_clear_databases";
    public final static String PREF_CLEAR_ALL_DATA =
            "webstorage_clear_all_data";
    public final static String PREF_MANAGE_QUOTA =
            "webstorage_manage_quota";
    public final static String PREF_DEFAULT_QUOTA =
            "webstorage_default_quota";
    public final static String PREF_EXTRAS_RESET_DEFAULTS =
            "reset_default_preferences";
    public final static String PREF_DEBUG_SETTINGS = "debug_menu";
    public final static String PREF_GEARS_SETTINGS = "gears_settings";
    public final static String PREF_WEBSTORAGE_SETTINGS = "webstorage_manage_databases";
    public final static String PREF_WEBSTORAGE_CLEAR_ALL = "webstorage_clear_databases";
    public final static String PREF_TEXT_SIZE = "text_size";
    public final static String PREF_DEFAULT_TEXT_ENCODING =
            "default_text_encoding";

    private static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Macintosh; " +
            "U; Intel Mac OS X 10_5_5; en-us) AppleWebKit/525.18 (KHTML, " +
            "like Gecko) Version/3.1.2 Safari/525.20.1";

    private static final String IPHONE_USERAGENT = "Mozilla/5.0 (iPhone; U; " +
            "CPU iPhone OS 2_2 like Mac OS X; en-us) AppleWebKit/525.18.1 " +
            "(KHTML, like Gecko) Version/3.1.1 Mobile/5G77 Safari/525.20";

    // Value to truncate strings when adding them to a TextView within
    // a ListView
    public final static int MAX_TEXTVIEW_LEN = 80;

    private TabControl mTabControl;

    // Single instance of the BrowserSettings for use in the Browser app.
    private static BrowserSettings sSingleton;

    // Private map of WebSettings to Observer objects used when deleting an
    // observer.
    private HashMap<WebSettings,Observer> mWebSettingsToObservers =
        new HashMap<WebSettings,Observer>();

    /*
     * An observer wrapper for updating a WebSettings object with the new
     * settings after a call to BrowserSettings.update().
     */
    static class Observer implements java.util.Observer {
        // Private WebSettings object that will be updated.
        private WebSettings mSettings;

        Observer(WebSettings w) {
            mSettings = w;
        }

        public void update(Observable o, Object arg) {
            BrowserSettings b = (BrowserSettings)o;
            WebSettings s = mSettings;

            s.setLayoutAlgorithm(b.layoutAlgorithm);
            if (b.userAgent == 0) {
                // use the default ua string
                s.setUserAgentString(null);
            } else if (b.userAgent == 1) {
                s.setUserAgentString(DESKTOP_USERAGENT);
            } else if (b.userAgent == 2) {
                s.setUserAgentString(IPHONE_USERAGENT);
            }
            s.setUseWideViewPort(b.useWideViewPort);
            s.setLoadsImagesAutomatically(b.loadsImagesAutomatically);
            s.setJavaScriptEnabled(b.javaScriptEnabled);
            s.setPluginsEnabled(b.pluginsEnabled);
            s.setJavaScriptCanOpenWindowsAutomatically(
                    b.javaScriptCanOpenWindowsAutomatically);
            s.setDefaultTextEncodingName(b.defaultTextEncodingName);
            s.setMinimumFontSize(b.minimumFontSize);
            s.setMinimumLogicalFontSize(b.minimumLogicalFontSize);
            s.setDefaultFontSize(b.defaultFontSize);
            s.setDefaultFixedFontSize(b.defaultFixedFontSize);
            s.setNavDump(b.navDump);
            s.setTextSize(b.textSize);
            s.setLightTouchEnabled(b.lightTouch);
            s.setSaveFormData(b.saveFormData);
            s.setSavePassword(b.rememberPasswords);

            // WebView inside Browser doesn't want initial focus to be set.
            s.setNeedInitialFocus(false);
            // Browser supports multiple windows
            s.setSupportMultipleWindows(true);
            // Turn off file access
            s.setAllowFileAccess(false);

            s.setDatabasePath(b.databasePath);
            s.setDatabaseEnabled(b.databaseEnabled);
            s.setWebStorageDefaultQuota(b.webStorageDefaultQuota);

            // Turn on Application Caches.
            s.setAppCachePath(b.appCachePath);
            s.setAppCacheEnabled(b.appCacheEnabled);
        }
    }

    /**
     * Load settings from the browser app's database.
     * NOTE: Strings used for the preferences must match those specified
     * in the browser_preferences.xml
     * @param ctx A Context object used to query the browser's settings
     *            database. If the database exists, the saved settings will be
     *            stored in this BrowserSettings object. This will update all
     *            observers of this object.
     */
    public void loadFromDb(Context ctx) {
        SharedPreferences p =
                PreferenceManager.getDefaultSharedPreferences(ctx);

        // Set the default value for the plugins path to the application's
        // local directory.
        pluginsPath = ctx.getDir("plugins", 0).getPath();
        // Set the default value for the Application Caches path.
        appCachePath = ctx.getDir("appcache", 0).getPath();
        // Set the default value for the Database path.
        databasePath = ctx.getDir("databases", 0).getPath();

        homeUrl = DEFAULT_HOME_URL +
                Partner.getString(ctx.getContentResolver(), Partner.CLIENT_ID);

        // Load the defaults from the xml
        // This call is TOO SLOW, need to manually keep the defaults
        // in sync
        //PreferenceManager.setDefaultValues(ctx, R.xml.browser_preferences);
        syncSharedPreferences(p);
    }

    /* package */ void syncSharedPreferences(SharedPreferences p) {

        homeUrl =
            p.getString(PREF_HOMEPAGE, homeUrl);

        loadsImagesAutomatically = p.getBoolean("load_images",
                loadsImagesAutomatically);
        javaScriptEnabled = p.getBoolean("enable_javascript",
                javaScriptEnabled);
        pluginsEnabled = p.getBoolean("enable_plugins",
                pluginsEnabled);
        pluginsPath = p.getString("plugins_path", pluginsPath);
        databasePath = p.getString("database_path", databasePath);
        databaseEnabled = p.getBoolean("enable_database", databaseEnabled);
        webStorageDefaultQuota = Long.parseLong(p.getString(PREF_DEFAULT_QUOTA,
                String.valueOf(webStorageDefaultQuota)));
        appCacheEnabled = p.getBoolean("enable_appcache",
            appCacheEnabled);
        appCachePath = p.getString("appcache_path", appCachePath);
        javaScriptCanOpenWindowsAutomatically = !p.getBoolean(
            "block_popup_windows",
            !javaScriptCanOpenWindowsAutomatically);
        showSecurityWarnings = p.getBoolean("show_security_warnings",
                showSecurityWarnings);
        rememberPasswords = p.getBoolean("remember_passwords",
                rememberPasswords);
        saveFormData = p.getBoolean("save_formdata",
                saveFormData);
        boolean accept_cookies = p.getBoolean("accept_cookies",
                CookieManager.getInstance().acceptCookie());
        CookieManager.getInstance().setAcceptCookie(accept_cookies);
        openInBackground = p.getBoolean("open_in_background", openInBackground);
        loginInitialized = p.getBoolean("login_initialized", loginInitialized);
        textSize = WebSettings.TextSize.valueOf(
                p.getString(PREF_TEXT_SIZE, textSize.name()));
        autoFitPage = p.getBoolean("autofit_pages", autoFitPage);
        boolean landscapeOnlyTemp =
                p.getBoolean("landscape_only", landscapeOnly);
        if (landscapeOnlyTemp != landscapeOnly) {
            landscapeOnly = landscapeOnlyTemp;
            mTabControl.getBrowserActivity().setRequestedOrientation(
                    landscapeOnly ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    : ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        useWideViewPort = true; // use wide view port for either setting
        if (autoFitPage) {
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
        } else {
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL;
        }
        defaultTextEncodingName =
                p.getString(PREF_DEFAULT_TEXT_ENCODING,
                        defaultTextEncodingName);

        showDebugSettings =
                p.getBoolean(PREF_DEBUG_SETTINGS, showDebugSettings);
        // Debug menu items have precidence if the menu is visible
        if (showDebugSettings) {
            boolean small_screen = p.getBoolean("small_screen",
                    layoutAlgorithm ==
                    WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            if (small_screen) {
                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
            } else {
                boolean normal_layout = p.getBoolean("normal_layout",
                        layoutAlgorithm == WebSettings.LayoutAlgorithm.NORMAL);
                if (normal_layout) {
                    layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL;
                } else {
                    layoutAlgorithm =
                            WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
                }
            }
            useWideViewPort = p.getBoolean("wide_viewport", useWideViewPort);
            tracing = p.getBoolean("enable_tracing", tracing);
            lightTouch = p.getBoolean("enable_light_touch", lightTouch);
            navDump = p.getBoolean("enable_nav_dump", navDump);
            doFlick = p.getBoolean("enable_flick", doFlick);
            userAgent = Integer.parseInt(p.getString("user_agent", "0"));
            mTabControl.getBrowserActivity().setBaseSearchUrl(
                    p.getString("search_url", ""));
        }
        update();
    }

    public String getPluginsPath() {
        return pluginsPath;
    }

    public String getHomePage() {
        return homeUrl;
    }

    public void setHomePage(Context context, String url) {
        Editor ed = PreferenceManager.
                getDefaultSharedPreferences(context).edit();
        ed.putString(PREF_HOMEPAGE, url);
        ed.commit();
        homeUrl = url;
    }

    public boolean isLoginInitialized() {
        return loginInitialized;
    }

    public void setLoginInitialized(Context context) {
        loginInitialized = true;
        Editor ed = PreferenceManager.
                getDefaultSharedPreferences(context).edit();
        ed.putBoolean("login_initialized", loginInitialized);
        ed.commit();
    }

    public WebSettings.TextSize getTextSize() {
        return textSize;
    }

    public boolean openInBackground() {
        return openInBackground;
    }

    public boolean showSecurityWarnings() {
        return showSecurityWarnings;
    }

    public boolean isTracing() {
        return tracing;
    }

    public boolean isLightTouch() {
        return lightTouch;
    }

    public boolean isNavDump() {
        return navDump;
    }

    public boolean doFlick() {
        return doFlick;
    }

    public boolean showDebugSettings() {
        return showDebugSettings;
    }

    public void toggleDebugSettings() {
        showDebugSettings = !showDebugSettings;
        navDump = showDebugSettings;
        update();
    }

    /**
     * Add a WebSettings object to the list of observers that will be updated
     * when update() is called.
     *
     * @param s A WebSettings object that is strictly tied to the life of a
     *            WebView.
     */
    public Observer addObserver(WebSettings s) {
        Observer old = mWebSettingsToObservers.get(s);
        if (old != null) {
            super.deleteObserver(old);
        }
        Observer o = new Observer(s);
        mWebSettingsToObservers.put(s, o);
        super.addObserver(o);
        return o;
    }

    /**
     * Delete the given WebSettings observer from the list of observers.
     * @param s The WebSettings object to be deleted.
     */
    public void deleteObserver(WebSettings s) {
        Observer o = mWebSettingsToObservers.get(s);
        if (o != null) {
            mWebSettingsToObservers.remove(s);
            super.deleteObserver(o);
        }
    }

    /*
     * Package level method for obtaining a single app instance of the
     * BrowserSettings.
     */
    /*package*/ static BrowserSettings getInstance() {
        if (sSingleton == null ) {
            sSingleton = new BrowserSettings();
        }
        return sSingleton;
    }

    /*
     * Package level method for associating the BrowserSettings with TabControl
     */
    /* package */void setTabControl(TabControl tabControl) {
        mTabControl = tabControl;
    }

    /*
     * Update all the observers of the object.
     */
    /*package*/ void update() {
        setChanged();
        notifyObservers();
    }

    /*package*/ void clearCache(Context context) {
        WebIconDatabase.getInstance().removeAllIcons();
        if (mTabControl != null) {
            WebView current = mTabControl.getCurrentWebView();
            if (current != null) {
                current.clearCache(true);
            }
        }
    }

    /*package*/ void clearCookies(Context context) {
        CookieManager.getInstance().removeAllCookie();
    }

    /* package */void clearHistory(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Browser.clearHistory(resolver);
        Browser.clearSearches(resolver);
    }

    /* package */ void clearFormData(Context context) {
        WebViewDatabase.getInstance(context).clearFormData();
        if (mTabControl != null) {
            mTabControl.getCurrentTopWebView().clearFormData();
        }
    }

    /*package*/ void clearPasswords(Context context) {
        WebViewDatabase db = WebViewDatabase.getInstance(context);
        db.clearUsernamePassword();
        db.clearHttpAuthUsernamePassword();
    }

    /*package*/ void clearDatabases(Context context) {
        WebStorage.getInstance().deleteAllDatabases();
        // Remove all listed databases from the preferences
        PreferenceActivity activity = (PreferenceActivity) context;
        PreferenceScreen screen = (PreferenceScreen)
            activity.findPreference(BrowserSettings.PREF_WEBSTORAGE_SETTINGS);
        screen.removeAll();
        screen.setEnabled(false);
    }

    /*package*/ void resetDefaultPreferences(Context ctx) {
        SharedPreferences p =
            PreferenceManager.getDefaultSharedPreferences(ctx);
        p.edit().clear().commit();
        PreferenceManager.setDefaultValues(ctx, R.xml.browser_preferences,
                true);
        setHomePage(ctx, DEFAULT_HOME_URL +
                Partner.getString(ctx.getContentResolver(), Partner.CLIENT_ID));
    }

    // Private constructor that does nothing.
    private BrowserSettings() {
    }
}
