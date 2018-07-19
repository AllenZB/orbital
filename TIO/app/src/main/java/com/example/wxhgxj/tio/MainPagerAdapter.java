package com.example.wxhgxj.tio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TodoFragment todoFragment = new TodoFragment();
                return todoFragment;
            case 1:
                DeadlineFragment deadlineFragment = new DeadlineFragment();
                return deadlineFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
