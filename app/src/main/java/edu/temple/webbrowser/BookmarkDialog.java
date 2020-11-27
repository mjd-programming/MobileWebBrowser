package edu.temple.webbrowser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class BookmarkDialog extends AppCompatDialogFragment {

    BookmarkDialogListener bookmarkDialogListener;
    int listPosition;

    public interface BookmarkDialogListener {
        void informationFromDialog(boolean pos);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        bookmarkDialogListener = (BookmarkDialogListener) context;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int i) {
        listPosition = i;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure?")
                .setMessage("Are you sure you want to delete this bookmark?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bookmarkDialogListener.informationFromDialog(true);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bookmarkDialogListener.informationFromDialog(false);
            }
        });
        return builder.create();
    }
}
