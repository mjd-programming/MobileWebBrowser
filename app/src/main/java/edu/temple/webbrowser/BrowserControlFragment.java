package edu.temple.webbrowser;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

public class BrowserControlFragment extends Fragment {


    private BrowserControlFragment.BrowserControlFragmentListener listener;

    public static BrowserControlFragment newInstance() {
        return new BrowserControlFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BrowserControlFragment.BrowserControlFragmentListener) {
            listener = (BrowserControlFragment.BrowserControlFragmentListener) context;
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
    }

    public interface BrowserControlFragmentListener {
        void informationFromBrowserControlFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_browser_control, container, false);
        ImageButton new_page_button = v.findViewById(R.id.new_page_button);
        new_page_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listener.informationFromBrowserControlFragment();
            }
        });
        return v;
    }
}