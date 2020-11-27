package edu.temple.webbrowser;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class BrowserControlFragment extends Fragment {


    BrowserControlFragmentListener listener;

    public static BrowserControlFragment newInstance() {
        return new BrowserControlFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BrowserControlFragment.BrowserControlFragmentListener) {
            listener = (BrowserControlFragment.BrowserControlFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
    }

    public interface BrowserControlFragmentListener {
        void informationFromBrowserControlFragment(String s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_browser_control, container, false);
        ImageButton new_page_button = v.findViewById(R.id.new_page_button);
        ImageButton new_bookmark_button = v.findViewById(R.id.new_bookmark_button);
        ImageButton bookmarks_button= v.findViewById(R.id.bookmarks_button);
        new_page_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listener.informationFromBrowserControlFragment("page");
            }
        });
        new_bookmark_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listener.informationFromBrowserControlFragment("bookmark");
            }
        });
        bookmarks_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listener.informationFromBrowserControlFragment("bookmarks");
            }
        });
        return v;
    }
}