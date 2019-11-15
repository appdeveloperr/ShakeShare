package com.project.usmansh.ingrumidreal;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.Toolbar;

import Adapters.ViewPagerAdapter;

public class TabHomeScreenAct extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager viewpager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    public static String username= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home_screen);


            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            viewpager = (ViewPager)findViewById(R.id.pager);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewpager.setAdapter(viewPagerAdapter);

            tabLayout = (TabLayout)findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewpager, true);
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        username = prefs.getString("username", "No name defined");//"No name defined" is t

//            username = getIntent().getStringExtra("username");
        Log.d("UNAMeLogin: ",username);

    }


}
