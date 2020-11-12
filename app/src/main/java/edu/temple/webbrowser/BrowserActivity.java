package edu.temple.webbrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

public class BrowserActivity extends AppCompatActivity implements
        PageControlFragment.PageControlFragmentListener,
        BrowserControlFragment.BrowserControlFragmentListener,
        PagerFragment.PagerFragmentListener,
        PageViewerFragment.PageViewerFragmentListener,
        PageListFragment.PageListFragmentListener {

    BrowserControlFragment browserControlFragment;
    PageControlFragment pageControlFragment;
    PagerFragment pagerFragment;
    PageListFragment pageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        if (savedInstanceState != null) {
            browserControlFragment = (BrowserControlFragment) getSupportFragmentManager().getFragment(savedInstanceState, "browserControlFragment");
            pageControlFragment = (PageControlFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pageControlFragment");
            pagerFragment = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pagerFragment");
            pageListFragment = (PageListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pageListFragment");
        } else {
            pageControlFragment = PageControlFragment.newInstance();
            browserControlFragment = BrowserControlFragment.newInstance();
            pagerFragment = PagerFragment.newInstance();
            pageListFragment = PageListFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.browser_control, browserControlFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.page_control, pageControlFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.page_viewer, pagerFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.page_list, pageListFragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "pageControlFragment", pageControlFragment);
        getSupportFragmentManager().putFragment(outState, "browserControlFragment", browserControlFragment);
        getSupportFragmentManager().putFragment(outState, "pagerFragment", pagerFragment);
        getSupportFragmentManager().putFragment(outState, "pageListFragment", pageListFragment);
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
    public void informationFromBrowserControlFragment() {
        pagerFragment.add();
        pagerFragment.notifyDataSetChanged();
        pageControlFragment.setText("");
        pageListFragment.add("");
        pagerFragment.moveToPage(pagerFragment.getSize() - 1);
        getSupportActionBar().setTitle("");
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
}