package com.navispin.seeknaa.samachar.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.navispin.seeknaa.samachar.NewsListFragment;

/**
 * Created by shivi on 6/25/16.
 */
public class NewsPagerAdapter  extends FragmentPagerAdapter {

    public NewsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        NewsListFragment frag = new NewsListFragment();;
        String catId = new Integer(i).toString();
        bundle.putString("CategoryId", catId);
        frag.setArguments(bundle);
        return frag;
        /*
        switch (i) {
            case 0 :
                bundle.putString("CategoryId", "0");
                frag = new NewsListFragment();
                frag.setArguments(bundle);
                return frag;
            case 1 :
                bundle.putString("CategoryId", "1");
                frag = new NewsListFragment();
                frag.setArguments(bundle);
                return frag;
            case 2 :
                bundle.putString("CategoryId", "2");
                frag = new NewsListFragment();
                frag.setArguments(bundle);
                return frag;
            case 3 :
                bundle.putString("CategoryId", "2");
                frag = new NewsListFragment();
                frag.setArguments(bundle);
                return frag;
            case 4 :
                bundle.putString("CategoryId", "2");
                frag = new NewsListFragment();
                frag.setArguments(bundle);
                return frag;
            case 5 :
                bundle.putString("CategoryId", "2");
                frag = new NewsListFragment();
                frag.setArguments(bundle);
                return frag;
            case 6 :
                bundle.putString("CategoryId", "2");
                frag = new NewsListFragment();
                frag.setArguments(bundle);
                return frag;
            default:
                return null;
        }
        */
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "Top Stories";
            case 1 :
                return "World";
            case 2 :
                return "Business";
            case 3 :
                return "Technology";
            case 4 :
                return "Health";
            case 5 :
                return "Sports";
            case 6 :
                return "Movies";
            default:
                return null;
        }
    }

}
