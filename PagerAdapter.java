package com.example.ram.benellacompat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.ram.benellacompat.fragments.PrevFragment;

import java.util.ArrayList;

/**
 * Created by ram on 2/13/18.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<String> fragments = new ArrayList<>();

    public PagerAdapter(FragmentManager fragmentManager,ArrayList<String> yrt) {
        super(fragmentManager);
        this.fragments=yrt;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        PrevFragment fragment=new PrevFragment();
        Bundle bundle=new Bundle();
        bundle.putString("URL",fragments.get(position));
        fragment.setArguments(bundle);
        return  fragment;
    }

}
