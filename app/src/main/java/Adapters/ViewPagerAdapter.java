package Adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import Fragments.Friends;
import Fragments.IngrumBump;
import Fragments.Message;
import Fragments.UserProfile;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        if(position == 0) {
            fragment = new UserProfile();
        }else if(position == 1){
            fragment = new IngrumBump();
        }else if (position == 2){
            fragment = new Friends();
        }
        position = position+1;
        Bundle bundle = new Bundle();
        bundle.putString("message","Fragment: "+position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        position = position+1;
        return "Fragment: "+position;
    }
}
