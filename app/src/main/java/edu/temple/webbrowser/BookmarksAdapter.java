package edu.temple.webbrowser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

class BookmarksAdapter extends BaseAdapter {

    private BookmarksListener listener;
    private final Context context;
    private ArrayList<Bookmark> bookmarks;

    public BookmarksAdapter(Context context, ArrayList<Bookmark> b) {
        super();
        this.context = context;
        listener = (BookmarksListener) context;
        bookmarks = b;
    }

    @Override
    public int getCount() {
        return bookmarks.size();
    }

    @Override
    public Object getItem(int i) {
        return bookmarks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public interface BookmarksListener {
        void informationFromBookmarksListener(String u);
        void dialogHandler(int i);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Bookmark currentBookmark = bookmarks.get(i);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(R.layout.bookmark_row, viewGroup, false);
        TextView bookmarkTitle = v.findViewById(R.id.bookmark_text);
        bookmarkTitle.setText(currentBookmark.getTitle());
        bookmarkTitle.setBackgroundColor(Color.WHITE);
        bookmarkTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.informationFromBookmarksListener(currentBookmark.getUrl());
            }
        });
        ImageButton deleteButton = v.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogHandler(i);
            }
        });
        return v;
    }
}