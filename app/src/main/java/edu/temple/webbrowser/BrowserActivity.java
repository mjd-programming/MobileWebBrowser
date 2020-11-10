package edu.temple.webbrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

public class BrowserActivity extends AppCompatActivity implements PageControlFragment.PageControlFragmentListener, BrowserControlFragment.BrowserControlFragmentListener, PagerFragment.PagerFragmentListener, PageViewerFragment.PageViewerFragmentListener {

    BrowserControlFragment browserControlFragment;
    PageControlFragment pageControlFragment;
    PagerFragment pagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        if (savedInstanceState != null) {
            browserControlFragment = (BrowserControlFragment) getSupportFragmentManager().getFragment(savedInstanceState, "browserControlFragment");
            pageControlFragment = (PageControlFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pageControlFragment");
            pagerFragment = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pagerFragment");
        } else {
            pageControlFragment = PageControlFragment.newInstance();
            browserControlFragment = BrowserControlFragment.newInstance();
            pagerFragment = PagerFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.browser_control, browserControlFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.page_control, pageControlFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.page_viewer, pagerFragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "pageControlFragment", pageControlFragment);
        getSupportFragmentManager().putFragment(outState, "browserControlFragment", browserControlFragment);
        getSupportFragmentManager().putFragment(outState, "pagerFragment", pagerFragment);
    }

    @Override
    public void informationFromPageControlFragment(String s, String url) {
        pagerFragment.getCurrentPage().go(s, url);
    }

    @Override
    public void informationFromBrowserControlFragment() {
        pagerFragment.addFragment();
        pagerFragment.notifyChange();
        pagerFragment.setCurrentItem(pagerFragment.getLastItemIndex());
    }

    @Override
    public void informationFromPagerFragment(String s, String url, int pos) {
        pageControlFragment.setURLText(url);
    }

    @Override
    public void informationFromPageViewerFragment(String s, String url) {

    }
}