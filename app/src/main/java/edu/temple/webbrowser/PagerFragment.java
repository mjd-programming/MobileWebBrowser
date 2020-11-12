package edu.temple.webbrowser;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebBackForwardList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class PagerFragment extends Fragment {

    ViewPager vp;
    ArrayList<PageViewerFragment> fragments;
    PagerFragmentListener listener;
    FragmentStatePagerAdapter fragmentStatePagerAdapter;

    public static PagerFragment newInstance() {
        return new PagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = new ArrayList<>();
        if (savedInstanceState != null) {
            fragments = (ArrayList<PageViewerFragment>) savedInstanceState.getSerializable("fragments");
        } else {
            add();
        }
        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                if (fragments.contains(object))
                    return fragments.indexOf(object);
                else
                    return POSITION_NONE;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fragments", fragments);
    }

    public interface PagerFragmentListener {
        void informationFromPagerFragment(int i);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PagerFragmentListener) {
            listener = (PagerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
    }

    public void add() {
        PageViewerFragment pageViewerFragment = PageViewerFragment.newInstance();
        fragments.add(pageViewerFragment);
    }

    public void notifyDataSetChanged() {
        fragmentStatePagerAdapter.notifyDataSetChanged();
    }

    public PageViewerFragment getCurrentPage() {
        return (PageViewerFragment) fragmentStatePagerAdapter.getItem(vp.getCurrentItem());
    }

    public ArrayList<PageViewerFragment> getAllPages() {
        return fragments;
    }

    public void setPages(ArrayList<PageViewerFragment> p) {
        fragments = new ArrayList<>();
        fragments.addAll(p);
    }

    public int getCurrentIndex() {
        return vp.getCurrentItem();
    }

    public int getSize() {
        return vp.getChildCount();
    }

    public void moveToPage(int i) {
        vp.setCurrentItem(i, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager, container, false);
        vp = v.findViewById(R.id.view_pager);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                listener.informationFromPagerFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setAdapter(fragmentStatePagerAdapter);
        return v;
    }
}