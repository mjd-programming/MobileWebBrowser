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
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;

public class PageControlFragment extends Fragment {

    private EditText text;
    private PageControlFragmentListener listener;
    private Context context;

    public static PageControlFragment newInstance() {
        return new PageControlFragment();
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
        outState.putString("url", text.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof PageControlFragmentListener) {
            listener = (PageControlFragmentListener) context;
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
    }

    public void setURLText(String s) {
        text.setText(s);
    }

    public interface PageControlFragmentListener {
        void informationFromPageControlFragment(String s, String url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_page_control, container, false);
        text = v.findViewById(R.id.url);
        ImageButton go_button = v.findViewById(R.id.button_go);
        ImageButton next_button = v.findViewById(R.id.button_next);
        ImageButton back_button = v.findViewById(R.id.button_back);
        go_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String m = text.getText().toString();
                listener.informationFromPageControlFragment("search", m);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        });
        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listener.informationFromPageControlFragment("next", null);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listener.informationFromPageControlFragment("back", null);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        });
        if (savedInstanceState != null) {
            text.setText(savedInstanceState.getString("url"));
        }
        return v;
    }
}