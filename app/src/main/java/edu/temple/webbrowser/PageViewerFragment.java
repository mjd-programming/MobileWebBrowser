package edu.temple.webbrowser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageViewerFragment extends Fragment {

    PageViewerFragmentListener listener;
    WebView wv;

    WebViewClient wvc = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            listener.informationFromPageViewerFragment(null, url);
            super.doUpdateVisitedHistory(view, url, isReload);
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        wv.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    public void go(String s, String url) {
        if (s.equals("search")) {
            Pattern pattern = Pattern.compile("\\.[a-z]{1,3}");
            Matcher matcher = pattern.matcher(url);
            if (!matcher.find()) {
                url = "https://www.google.com/search?q=" + url;
            } else {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }
            }
            wv.loadUrl(url);
        } else if (s.equals("next")) {
            if (wv.canGoForward()) wv.goForward();
        } else {
            if (wv.canGoBack()) wv.goBack();
        }
    }

    public static PageViewerFragment newInstance() {
        return new PageViewerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PageControlFragment.PageControlFragmentListener) {
            listener = (PageViewerFragment.PageViewerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
    }

    public interface PageViewerFragmentListener {
        void informationFromPageViewerFragment(String s, String url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_page_viewer, container, false);
        wv = v.findViewById(R.id.web_view);
        wv.setWebViewClient(wvc);
        wv.getSettings().setJavaScriptEnabled(true);
        if (savedInstanceState != null) wv.restoreState(savedInstanceState);
        return v;
    }
}