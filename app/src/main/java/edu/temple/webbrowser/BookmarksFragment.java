package edu.temple.webbrowser;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class BookmarksFragment extends Fragment {

    BookmarksFragmentListener listener;
    Context context;
    ListView bookmarksList;
    ArrayList<Bookmark> bookmarks = new ArrayList<>();
    BookmarksAdapter adapter;

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
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
    public void onAttach(@NonNull Context c) {
        super.onAttach(c);
        context = c;
        listener = (BookmarksFragmentListener) context;
        adapter = new BookmarksAdapter(context, bookmarks);
    }

    public void add(String t, String u) {
        bookmarks.add(new Bookmark(t, u));
    }

    public void remove(int i) {
        bookmarks.remove(i);
        adapter.notifyDataSetChanged();
    }

    public interface BookmarksFragmentListener {
        void informationFromBookmarksFragment();
    }

    public ArrayList<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarksFromTinyDB(ArrayList<Bookmark> b) {
        bookmarks.clear();
        bookmarks.addAll(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        bookmarksList = v.findViewById(R.id.bookmarks_list);
        bookmarksList.setAdapter(adapter);
        ImageButton backButton = v.findViewById(R.id.bookmarks_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.informationFromBookmarksFragment();
            }
        });
        return v;
    }
}