package com.josef.mobile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import com.josef.josefmobile.R;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeContainer#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomeContainer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private int which;
    ViewPager viewPager;
    private int mPosition;
    private ToggleButton buttonFavorite;
    View layoutInflater;
    public HomeContainer() {
        // Required empty public constructor
    }

    @Nullable
    public static HomeContainer newInstance(int which) {
        HomeContainer fragment = new HomeContainer();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, which);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            which = getArguments().getInt(ARG_PARAM1);
        }
        if(savedInstanceState!=null)mPosition = savedInstanceState.getInt(VIEWPAGERDETAILKEY,0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.fragment_home_container, container, false);
        viewPager= layoutInflater.findViewById(R.id.viewidpager);
        viewPager.setId(View.generateViewId());
        final ViewPagerFragmentAdapters adapters = new ViewPagerFragmentAdapters(getChildFragmentManager(), which);
        //EspressoIdlingResource.increment();
        viewPager.setAdapter(adapters);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                HomeFragment fragment = (HomeFragment)adapters.getRegisteredFragment(position);
                fragment.shareMetaData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mPosition);
        return layoutInflater;
    }

   @Override
   public void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);
        outState.putInt(VIEWPAGERDETAILKEY,mPosition);
   }

    public class ViewPagerFragmentAdapters extends FragmentStatePagerAdapter {

        //smartfragmentstatepager....
        public int mIndex;
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ViewPagerFragmentAdapters(FragmentManager fm, int index) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mIndex = index;

        }
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public Fragment getItem(int position) {
            return HomeFragment.newInstance(mIndex, position);
        }
    }

}
