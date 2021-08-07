package com.academiamoviles.distrack.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maacs on 21/09/2017.
 */

public class PagerTareasAdapter extends FragmentStatePagerAdapter {

    private final List<String> titles = new ArrayList<>();
    private final List<Fragment> fragments = new ArrayList<>();

    public PagerTareasAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

}
