package edu.temple.webbrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.WebView;

public class BrowserActivity extends AppCompatActivity implements PageControlFragment.PageControlFragmentListener, PageViewerFragment.PageViewerFragmentListener {

    PageControlFragment pageControlFragment;
    PageViewerFragment pageViewerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        if (savedInstanceState != null) {
            pageControlFragment = (PageControlFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pageControlFragment");
            pageViewerFragment = (PageViewerFragment) getSupportFragmentManager().getFragment(savedInstanceState, "pageViewerFragment");
        } else {
            pageControlFragment = PageControlFragment.newInstance();
            pageViewerFragment = PageViewerFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.page_control, pageControlFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.page_viewer, pageViewerFragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "pageControlFragment", pageControlFragment);
        getSupportFragmentManager().putFragment(outState, "pageViewerFragment", pageViewerFragment);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        this.recreate();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void informationFromPageControlFragment(String s, String url) {
        pageViewerFragment.go(s, url);
    }

    @Override
    public void informationFromPageViewerFragment(String s, String url) {
        pageControlFragment.setURLText(url);
    }
}