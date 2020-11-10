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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class PagerFragment extends Fragment implements PageViewerFragment.PageViewerFragmentListener {
    ViewPager vp;
    ArrayList<PageViewerFragment> fragments;
    PagerFragmentListener listener;
    FragmentStatePagerAdapter fragmentStatePagerAdapter;
    Parcelable savedState;
    public static PagerFragment newInstance() {
        return new PagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

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
        fragments = new ArrayList<>();
        if (savedInstanceState != null) {
            for (int i = 0; i < savedInstanceState.getInt("total"); i++) {
                addFragment();
            }
            notifyChange();
            fragmentStatePagerAdapter.restoreState(savedState, getClass().getClassLoader());
        } else {
            addFragment();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        savedState = fragmentStatePagerAdapter.saveState();
        outState.putInt("total", vp.getChildCount());
    }

    public void addFragment() {
        PageViewerFragment pvf = PageViewerFragment.newInstance();
        fragments.add(pvf);
        if (vp != null) {
            notifyChange();
            vp.setOffscreenPageLimit(vp.getOffscreenPageLimit() + 1);
        }
    }

    public void notifyChange() {
        if (vp != null) vp.getAdapter().notifyDataSetChanged();
    }

    public int getCurrentItem() {
        return vp.getCurrentItem();
    }

    public void setCurrentItem(int i) {
        vp.setCurrentItem(i);
        notifyChange();
    }

    public int getLastItemIndex() {
        return vp.getChildCount();
    }

    public PageViewerFragment getCurrentPage() {
        return fragments.get(getCurrentItem());
    }

    @Override
    public void informationFromPageViewerFragment(String s, String url) {

    }

    public interface PagerFragmentListener {
        void informationFromPagerFragment(String s, String url, int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PagerFragmentListener) {
            listener = (PagerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pager, container, false);
        vp = v.findViewById(R.id.view_pager);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                listener.informationFromPagerFragment("selected", getCurrentPage().getUrl(), position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setAdapter(fragmentStatePagerAdapter);
        if (savedInstanceState != null) {
            notifyChange();
            vp.setOffscreenPageLimit(fragments.size());
        }
        return v;
    }
}