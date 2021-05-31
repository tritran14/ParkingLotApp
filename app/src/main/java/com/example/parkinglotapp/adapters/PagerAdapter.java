package com.example.parkinglotapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.parkinglotapp.fragments.HistoryFragment;
import com.example.parkinglotapp.fragments.InfoFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment now=null;
        switch (position){
            case 0:
                now=new HistoryFragment();
                break;
            case 1:
                now=new InfoFragment();
                break;
        }
        return now;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
