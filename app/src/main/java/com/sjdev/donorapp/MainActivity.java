package com.sjdev.donorapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    //Number of selected tab, we have 3 tabs so value must lie between 1-3. default value is 1 because first tab is selected by Default
    private int selectedTab = 1;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // swipeToRefresh();

        //Bottom Bar
        final LinearLayout homeLayout = findViewById(R.id.home_Layout);
        final LinearLayout searchLayout = findViewById(R.id.search_layout);
        final LinearLayout settingsLayout = findViewById(R.id.settings_layout);

        final ImageView homeImg = findViewById(R.id.home_img);
        final ImageView searchImg = findViewById(R.id.search_img);
        final ImageView settingsImg = findViewById(R.id.settings_img);

        final TextView homeTxt = findViewById(R.id.home_txt);
        final TextView searchTxt = findViewById(R.id.search_txt);
        final TextView settingsTxt = findViewById(R.id.settings_txt);


        //set home fragment by Default
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.FragmentContainer, HomeFragment.class, null)
                .commit();

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if home is already selected or not
                if (selectedTab != 1) {

                    //Set Home Fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.FragmentContainer, HomeFragment.class, null)
                            .commit();

                    //unselect other tabs
                    searchTxt.setVisibility(View.GONE);
                    settingsTxt.setVisibility(view.GONE);

                    searchImg.setImageResource(R.drawable.ic_search);
                    settingsImg.setImageResource(R.drawable.ic_settings);

                    searchLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    settingsLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //Home tab Select
                    homeTxt.setVisibility(View.VISIBLE);
                    homeImg.setImageResource(R.drawable.ic_home_selected);
                    homeLayout.setBackgroundResource(R.drawable.round_bottom_selected);

                    //Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);

                    //set 1st Tab as Selected Tab
                    selectedTab = 1;
                }
            }
        });

        searchLayout.setOnClickListener(view -> {
            if (selectedTab != 2) {

                //Set Search Fragment
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.FragmentContainer, SearchFragment.class, null)
                        .commit();

                //unselect other tabs
                homeTxt.setVisibility(View.GONE);
                settingsTxt.setVisibility(view.GONE);

                homeImg.setImageResource(R.drawable.ic_home);
                settingsImg.setImageResource(R.drawable.ic_settings);

                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                settingsLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                //search tab Select
                searchTxt.setVisibility(View.VISIBLE);
                searchImg.setImageResource(R.drawable.ic_search_selected);
                searchLayout.setBackgroundResource(R.drawable.round_bottom_selected);

                //Animation
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                searchLayout.startAnimation(scaleAnimation);

                //set 1st Tab as Selected Tab
                selectedTab = 2;

            }
        });

        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 3) {

                    //Set Settings Fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.FragmentContainer, SettingsFragment.class, null)
                            .commit();

                    //unselect other tabs
                    searchTxt.setVisibility(View.GONE);
                    homeTxt.setVisibility(view.GONE);

                    searchImg.setImageResource(R.drawable.ic_search);
                    homeImg.setImageResource(R.drawable.ic_home);

                    searchLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //settings tab Select
                    settingsTxt.setVisibility(View.VISIBLE);
                    settingsImg.setImageResource(R.drawable.ic_settings_selected);
                    settingsLayout.setBackgroundResource(R.drawable.round_bottom_selected);

                    //Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    settingsLayout.startAnimation(scaleAnimation);

                    //set 1st Tab as Selected Tab
                    selectedTab = 3;
                }
            }
        });
    }

//    private void swipeToRefresh() {
//
//        //Look  up for the Swipe Container
//        swipeContainer = findViewById(R.id.swipeContainer);
//
//        //Setup Refresh Listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //code  to refresh goes here
//                startActivity(getIntent());
//                finish();
//                overridePendingTransition(0, 0);
//                swipeContainer.setRefreshing(false);
//            }
//        });
//
//        //Configure
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
//    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish(); // Call finish() to close the current activity
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}



