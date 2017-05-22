package com.nikola.appprotect;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.nikola.appprotect.adapter.AppAdapter;
import com.nikola.appprotect.database.AppsContentProvider;
import com.nikola.appprotect.database.AppsDb;
import com.nikola.appprotect.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ConstantConditions")
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AppAdapter.OnLockClick, DataPass {
    @BindView(R.id.list_view)
    ListView listView;
    private AppAdapter appAdapter;
    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.main_fragment_layout, container, false);
        ButterKnife.bind(this, rootView);
        appAdapter = new AppAdapter(getActivity(), null, 0, this);
        listView.setAdapter(appAdapter);
        getLoaderManager().initLoader(0, null, MainFragment.this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                AppsDb.COLUMN_ID,
                AppsDb.COLUMN_LABEL,
                AppsDb.COLUMN_PACKAGE_NAME,
                AppsDb.COLUMN_LOCKED_STATUS};
        return new CursorLoader(getActivity(), AppsContentProvider.BASE_CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        appAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        appAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        final ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                new refreshListTask().execute();
                return true;
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new refreshListTask().execute();
                editText = (EditText) searchView.findViewById(R.id.search_src_text);
                editText.setText("");
                searchView.setQuery("", false);
                searchView.onActionViewCollapsed();
                searchMenuItem.collapseActionView();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                PermissionUtil.showProtectAppDialogTutorial(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick() {
        new refreshListTask().execute();
    }

    @Override
    public void input(String searchWord) {
        String[] projection = {
                AppsDb.COLUMN_ID,
                AppsDb.COLUMN_LABEL,
                AppsDb.COLUMN_PACKAGE_NAME,
                AppsDb.COLUMN_LOCKED_STATUS};
        String mSelectionClause = AppsDb.COLUMN_LABEL + " LIKE ?";
        String[] mSelectionArgs = {"%" + searchWord + "%"};
        Cursor cursor = getActivity().getContentResolver().query(AppsContentProvider.BASE_CONTENT_URI, projection, mSelectionClause, mSelectionArgs, null, null);
        appAdapter.swapCursor(cursor);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        restartLoader();
    }

    private class refreshListTask extends AsyncTask<Object, Object, Cursor> {

        @Override
        protected Cursor doInBackground(Object... params) {
            String[] projection = {
                    AppsDb.COLUMN_ID,
                    AppsDb.COLUMN_LABEL,
                    AppsDb.COLUMN_PACKAGE_NAME,
                    AppsDb.COLUMN_LOCKED_STATUS};
            return getActivity().getContentResolver().query(AppsContentProvider.BASE_CONTENT_URI, projection, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            appAdapter.swapCursor(cursor);
        }
    }
}
