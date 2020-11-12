package edu.temple.webbrowser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageViewerFragment extends Fragment implements Serializable {

    PageViewerFragmentListener listener;
    WebView wv;

    WebViewClient wvc = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (listener != null) listener.informationFromPageViewerFragment();
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) wv.restoreState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        wv.saveState(outState);
        outState.putString("url", wv.getUrl());
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

    public String getUrl() {
        return wv.getUrl();
    }

    public String getTitle() {
        String t = wv.getTitle();
        if (t.length() > 25) return t.substring(0, 22) + "...";
        else return t;
    }

    public static PageViewerFragment newInstance() {
        return new PageViewerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PageViewerFragmentListener) {
            listener = (PageViewerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
    }

    public interface PageViewerFragmentListener {
        void informationFromPageViewerFragment();
    }



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_page_viewer, container, false);
        wv = v.findViewById(R.id.web_view);
        wv.setWebViewClient(wvc);
        wv.getSettings().setJavaScriptEnabled(true);
        return v;
    }
}