package com.cs442project.appmonitor;

/**
 * Created by Snehal on 2/27/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cs442project.appmonitor.loader.AppEntry;
import com.cs442project.appmonitor.loader.AppListLoader;

import java.util.List;


public class mainScreen extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        //BUTTON ON CLICK LISTENER WILL START THE MAIN ACTICITY
        Button button_calc = (Button) findViewById(R.id.button);
        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mainScreen.this, MainActivity.class));
            }
        });
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            AppListFragment list = new AppListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_configure_locale) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class AppListFragment extends ListFragment implements
            LoaderManager.LoaderCallbacks<List<AppEntry>> {
        private static final String TAG = "ADP_AppListFragment";
        private static final boolean DEBUG = true;

        // We use a custom ArrayAdapter to bind application info to the ListView.
        private AppListAdapter mAdapter;

        // The Loader's id (this id is specific to the ListFragment's LoaderManager)
        private static final int LOADER_ID = 1;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setHasOptionsMenu(true);

            mAdapter = new AppListAdapter(getActivity());
            setEmptyText("No applications");
            setListAdapter(mAdapter);
            setListShown(false);

            if (DEBUG) {
                Log.i(TAG, "+++ Calling initLoader()! +++");
                if (getLoaderManager().getLoader(LOADER_ID) == null) {
                    Log.i(TAG, "+++ Initializing the new Loader... +++");
                } else {
                    Log.i(TAG, "+++ Reconnecting with existing Loader (id '1')... +++");
                }
            }

            // Initialize a Loader with id '1'. If the Loader with this id already
            // exists, then the LoaderManager will reuse the existing Loader.
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }

        /**********************/
        /** LOADER CALLBACKS **/
        /**
         * ******************
         */

        @Override
        public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
            if (DEBUG) Log.i(TAG, "+++ onCreateLoader() called! +++");
            return new AppListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
            if (DEBUG) Log.i(TAG, "+++ onLoadFinished() called! +++");
            mAdapter.setData(data);

            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<AppEntry>> loader) {
            if (DEBUG) Log.i(TAG, "+++ onLoadReset() called! +++");
            mAdapter.setData(null);
        }

        /**********************/
        /** CONFIGURE LOCALE **/
        /**
         * ******************
         */

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.activity_main, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_configure_locale:
                    configureLocale();
                    return true;
            }
            return false;
        }

        /**
         * Notifies the Loader that a configuration change has has occurred (i.e. by
         * calling {@link android.support.v4.content.Loader#onContentChanged()}).
         * <p/>
         * This feature was added so that it would be easy to see the sequence of
         * events that occurs when a content change is detected. Connect your
         * device via USB and analyze the logcat to see the sequence of methods that
         * are called as a result!
         */
        private void configureLocale() {
            Loader<AppEntry> loader = getLoaderManager().getLoader(LOADER_ID);
            if (loader != null) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }
        }
    }
}
