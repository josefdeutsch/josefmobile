package com.josef.mobile.free.ui;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.test.espresso.IdlingResource;
import androidx.viewpager.widget.ViewPager;

import com.josef.josefmobile.R;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.VIEWPAGERMAINKEY;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContentContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ContentContainerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private int which;
    ViewPager viewPager;
    private Integer mPosition;
    private ToggleButton buttonFavorite;
    View layoutInflater;
    ContentDetailFragment mHomeFragment;

    // erstes Item wird nicht initialisiert - rekursiv.

    public ContentContainerFragment() {

    }

    @Nullable
    public static ContentContainerFragment newInstance(int which) {
        ContentContainerFragment fragment = new ContentContainerFragment();
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERMAINKEY, which);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            which = getArguments().getInt(VIEWPAGERMAINKEY);
        }
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(VIEWPAGERDETAILKEY, 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layoutInflater = inflater.inflate(R.layout.fragment_content_container, container, false);
        viewPager = layoutInflater.findViewById(R.id.viewidpager);
        final ViewPagerFragmentAdapters adapters = new ViewPagerFragmentAdapters(getChildFragmentManager(), which);
        //EspressoIdlingResource.increment();
        viewPager.setAdapter(adapters);
        viewPager.setOffscreenPageLimit(3);
        //zu schnell background thread problematisch..
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                mHomeFragment = (ContentDetailFragment) adapters.getRegisteredFragment(position);

                SparseArray<Fragment> array = adapters.getRegisteredFragments();

                mHomeFragment.onPlayExecute();

                if (position - 1 != -1) {
                    ContentDetailFragment before = (ContentDetailFragment) array.get(position - 1);
                    before.onPlayBackState();
                    //     before.onPlayExecute();
                } else if (position + 1 != 49) {
                    ContentDetailFragment after = (ContentDetailFragment) array.get(position + 1);
                    after.onPlayBackState();
                    //           after.onPlayExecute();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //viewPager.setCurrentItem(1);
        //viewPager.setCurrentItem(0);
        //mHomeFragment.addItemtsToDataBase(0);

        //viewPager.setCurrentItem(mPosition);
        return layoutInflater;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(VIEWPAGERDETAILKEY, mPosition);
    }

    public class ViewPagerFragmentAdapters extends FragmentStatePagerAdapter {

        public int mIndex;
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ViewPagerFragmentAdapters(FragmentManager fm, int index) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mIndex = index;

        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        public SparseArray<Fragment> getRegisteredFragments() {
            return registeredFragments;
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
            if (registeredFragments.get(position) != null) return registeredFragments.get(position);
            return ContentDetailFragment.newInstance(mIndex, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }
    }

    @Nullable
    private IdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = EspressoIdlingResource.getIdlingResource();
        }
        return mIdlingResource;
    }
}
