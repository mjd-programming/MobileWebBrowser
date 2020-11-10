package edu.temple.webbrowser;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PageListFragment extends Fragment {

    private ArrayList<String> pages;
    private ArrayAdapter<String> adapter;
    private PageListFragmentListener listener;
    private Context context;

    public static PageListFragment newInstance() {
        return new PageListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pages = new ArrayList<>();
        if (savedInstanceState != null) {
            pages.addAll(savedInstanceState.getStringArrayList("pages"));
        } else {
            pages.add("New Window");
        }
        Log.println(Log.WARN, "onCreate() size", "" + pages.size());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("pages", pages);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof PageListFragmentListener) {
            listener = (PageListFragmentListener) context;
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
    }

    public void add(String s) {
        pages.add(s);
        update();
    }

    public void update() {
        adapter.notifyDataSetChanged();
    }

    public void updateRowAtIndex(int i, String s) {
        pages.set(i, s);
        update();
    }

    public interface PageListFragmentListener {
        void informationFromPageListFragment(int i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_page_list, container, false);
        ListView listView = v.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, pages);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.informationFromPageListFragment(i);
            }
        });
        update();
        return v;
    }
}