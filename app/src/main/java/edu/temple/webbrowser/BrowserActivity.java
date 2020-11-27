package edu.temple.webbrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BrowserActivity extends AppCompatActivity implements
        PageControlFragment.PageControlFragmentListener,
        BrowserControlFragment.BrowserControlFragmentListener,
        PagerFragment.PagerFragmentListener,
        PageViewerFragment.PageViewerFragmentListener,
        PageListFragment.PageListFragmentListener,
        BookmarksAdapter.BookmarksListener,
        BookmarksFragment.BookmarksFragmentListener,
        BookmarkDialog.BookmarkDialogListener {

    BrowserControlFragment browserControlFragment;
    PageControlFragment pageControlFragment;
    PagerFragment pagerFragment;
    PageListFragment pageListFragment;
    BookmarksFragment bookmarksFragment;

    int bookmarkPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        bookmarkPosition = -1;
        if (savedInstanceState != null) {
            browserControlFragment = (BrowserControlFragment) getSupportFragmentManager().getFragment(savedInstanceState, "browserControlFragment");
            pageControlFragment = (PageControlFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pageControlFragment");
            pagerFragment = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pagerFragment");
            pageListFragment = (PageListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pageListFragment");
            if (savedInstanceState.getInt("bookmarks") == 1) bookmarksFragment = (BookmarksFragment) getSupportFragmentManager().getFragment(savedInstanceState, "bookmarksFragment");
            else bookmarksFragment = BookmarksFragment.newInstance();
        } else {
            pageControlFragment = PageControlFragment.newInstance();
            browserControlFragment = BrowserControlFragment.newInstance();
            pagerFragment = PagerFragment.newInstance();
            pageListFragment = PageListFragment.newInstance();
            bookmarksFragment = BookmarksFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.browser_control, browserControlFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.page_control, pageControlFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.page_viewer, pagerFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.page_list, pageListFragment).commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        TinyDB tinyDB = new TinyDB(this);
        ArrayList<Object> bookmarkObjects = new ArrayList<>();
        for (Bookmark b : bookmarksFragment.getBookmarks()) {
            bookmarkObjects.add((Object)b);
        }
        tinyDB.putListObject("bookmarks", bookmarkObjects);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TinyDB tinyDB = new TinyDB(this);
        ArrayList<Bookmark> b = new ArrayList<>();
        ArrayList<Object> objectList = tinyDB.getListObject("bookmarks", Bookmark.class);
        for (Object obj : objectList) {
            b.add((Bookmark) obj);
        }
        bookmarksFragment.setBookmarksFromTinyDB(b);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "pageControlFragment", pageControlFragment);
        getSupportFragmentManager().putFragment(outState, "browserControlFragment", browserControlFragment);
        getSupportFragmentManager().putFragment(outState, "pagerFragment", pagerFragment);
        getSupportFragmentManager().putFragment(outState, "pageListFragment", pageListFragment);
        if (bookmarksFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "bookmarksFragment", bookmarksFragment);
            outState.putInt("bookmarks", 1);
        } else {
            outState.putInt("bookmarks", 0);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void informationFromPageControlFragment(String s, String url) {
        hideKeyboard();
        pagerFragment.getCurrentPage().go(s, url);
        pageListFragment.updateRowAtIndex(pagerFragment.getCurrentIndex(), pagerFragment.getCurrentPage().getTitle());
    }

    @Override
    public void informationFromBrowserControlFragment(String s) {
        if (s.equals("page")) {
            pagerFragment.add();
            pagerFragment.notifyDataSetChanged();
            pageControlFragment.setText("");
            pageListFragment.add("");
            pagerFragment.moveToPage(pagerFragment.getSize() - 1);
            getSupportActionBar().setTitle("");
        } else if (s.equals("bookmarks")) {
            getSupportFragmentManager().beginTransaction().add(R.id.bookmarks_holder, bookmarksFragment).commit();
            getSupportActionBar().setTitle("Bookmarks");
        } else {
            bookmarksFragment.add(pagerFragment.getCurrentPage().getTitle(), pagerFragment.getCurrentPage().getUrl());
        }
    }

    @Override
    public void informationFromPagerFragment(int i) {
        pagerFragment.moveToPage(i);
        pageControlFragment.setText(pagerFragment.getCurrentPage().getUrl());
        pageListFragment.updateRowAtIndex(i, pagerFragment.getCurrentPage().getTitle());
        getSupportActionBar().setTitle(pagerFragment.getCurrentPage().getTitle());
    }

    @Override
    public void informationFromPageViewerFragment() {
        pageControlFragment.setText(pagerFragment.getCurrentPage().getUrl());
        pageListFragment.updateRowAtIndex(pagerFragment.getCurrentIndex(), pagerFragment.getCurrentPage().getTitle());
        getSupportActionBar().setTitle(pagerFragment.getCurrentPage().getTitle());
    }

    @Override
    public void informationFromPageListFragment(int i) {
        pagerFragment.moveToPage(i);
        pageControlFragment.setText(pagerFragment.getCurrentPage().getUrl());
        getSupportActionBar().setTitle(pagerFragment.getCurrentPage().getTitle());
    }

    @Override
    public void informationFromBookmarksListener(String u) {
        getSupportFragmentManager().beginTransaction().remove(bookmarksFragment).commit();
        pagerFragment.getCurrentPage().go("search", u);
        pageListFragment.updateRowAtIndex(pagerFragment.getCurrentIndex(), pagerFragment.getCurrentPage().getTitle());
    }

    @Override
    public void dialogHandler(int i) {
        bookmarkPosition = i;
        BookmarkDialog bookmarkDialog = new BookmarkDialog();
        bookmarkDialog.show(getSupportFragmentManager(), "bookmarkDialog");
    }

    @Override
    public void informationFromBookmarksFragment() {
        getSupportFragmentManager().beginTransaction().remove(bookmarksFragment).commit();
        getSupportActionBar().setTitle(pagerFragment.getCurrentPage().getTitle());
    }

    @Override
    public void informationFromDialog(boolean pos) {
        if (pos) {
            bookmarksFragment.remove(bookmarkPosition);
        }
    }
}