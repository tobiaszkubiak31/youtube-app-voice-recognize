package app.com.youtubeapiv3.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.com.youtubeapiv3.fragments.SearchFragment;
import app.com.youtubeapiv3.fragments.LiveFragment;
import app.com.youtubeapiv3.fragments.PopularFragment;

/**
 * Created by mdmunirhossain on 12/18/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    SearchFragment searchFragment;
    PopularFragment popularFragment;
    LiveFragment liveFragment;

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                searchFragment = new SearchFragment();
                return searchFragment;
            case 1:
                popularFragment = new PopularFragment();
                return popularFragment;
            case 2:
                liveFragment = new LiveFragment();
                return liveFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public int getmNumOfTabs() {
        return mNumOfTabs;
    }

    public SearchFragment getSearchFragment() {
        return searchFragment;
    }

    public PopularFragment getPopularFragment() {
        return popularFragment;
    }

    public LiveFragment getLiveFragment() {
        return liveFragment;
    }
}
