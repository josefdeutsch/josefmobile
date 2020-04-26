package com.josef.mobile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.josef.josefmobile.R;


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
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    // TODO: Rename and change types of parameters
    private int which;
    private Integer mainkey;
    private Integer detailvalue;

    public HomeContainer() {
        // Required empty public constructor
    }
    @Nullable
    public static HomeContainer newInstance(int which, @Nullable Integer mainkey,@Nullable Integer detailvalue) {
        HomeContainer fragment = new HomeContainer();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1,which);

        // APPPREFERENCE BETTER ??!?!?!?!?! TEST !?!?!?!
        if(mainkey!=null){ args.putInt(ARG_PARAM2,mainkey);}
        if(detailvalue!=null){args.putInt(ARG_PARAM3,detailvalue);}
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            which = getArguments().getInt(ARG_PARAM1);
            mainkey = getArguments().getInt(ARG_PARAM2);
            detailvalue = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.fragment_home_container, container, false);

       // textView = layoutInflater.findViewById(R.id.number);
        Toast.makeText(getActivity(),String.valueOf(which),
                Toast.LENGTH_SHORT).show();
        ViewPager viewPager = layoutInflater.findViewById(R.id.viewidpager);
        viewPager.setId(View.generateViewId());
        ViewPagerFragmentAdapters adapters = new ViewPagerFragmentAdapters(getChildFragmentManager(),which);
        //EspressoIdlingResource.increment();
        viewPager.setAdapter(adapters);
        viewPager.setOffscreenPageLimit(3);

        if(which==mainkey){viewPager.setCurrentItem(detailvalue);}
        return layoutInflater;
    }

    public class ViewPagerFragmentAdapters extends FragmentStatePagerAdapter {

        public int mIndex;

        public ViewPagerFragmentAdapters(FragmentManager fm,int index) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mIndex = index;

        }

        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public Fragment getItem(int position) {
            return HomeFragment.newInstance(mIndex,position);
        }
    }

}
