package com.teamcomputers.bam.Activities;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.teamcomputers.bam.CustomView.CustomViewPager;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.Logistics.LogisticsFragment;
import com.teamcomputers.bam.Fragments.ui.home.HomeFragment;
import com.teamcomputers.bam.Fragments.ui.send.SendFragment;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends BaseActivity {

    Toolbar toolbar;
    Context mContext;
    AppBarLayout appBarLayout;
    private DrawerLayout drawerLayout;

    private BaseFragment fragment;
    private FragmentManager fragmentManager;
    CollapsingToolbarLayout collapsingToolbarLayout;

    public static final String IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED = "IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED";
    public int backStackEntryCount = 0;

    String userId = "", jobTitle = "", strDate;

    /*private int[] navIcons = {
            R.drawable.ic_input,
            R.drawable.ic_sales,
            R.drawable.ic_leakage,
            R.drawable.ic_productivity,
            R.drawable.ic_collection,
            R.drawable.ic_promotion,
            R.drawable.ic_rnr,
            R.drawable.ic_promotion
    };*/
    private String[] navLabels = {
            "Dispatch",
            "In-Transit",
            "Hold Delivery",
            "Acknowledgement"
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        configureNavigationDrawer();
        configureToolbar();
        userId = "SALES";


        //  System.out.println("Date Format with dd MMMM yyyy : "+strDate);
        this.setToolBarTitle(userId, jobTitle);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_tab, null);

            TextView tab_label = tab.findViewById(R.id.nav_label);

            tab_label.setText(navLabels[i]);
            if (i == 0) {
                tab_label.setTextColor(getResources().getColor(R.color.colorTabSelected));
            } else {
                tab_label.setTextColor(getResources().getColor(R.color.colorTabNonSelected));
            }
            tabLayout.getTabAt(i).setCustomView(tab);
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final CustomViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setPagingEnabled(false);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                View tabView = tab.getCustomView();
                TextView tab_label = tabView.findViewById(R.id.nav_label);
                tab_label.setTextColor(getResources().getColor(R.color.colorTabSelected));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                TextView tab_label = tabView.findViewById(R.id.nav_label);
                tab_label.setTextColor(getResources().getColor(R.color.colorTabNonSelected));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Subscribe
    @Override
    public void onEvent(final EventObject eventObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case Events.NO_INTERNET_CONNECTION:
                        dismissProgress();
                        //showToast(ToastTexts.LOGIN_SUCCESSFULL);
                        break;
                }
            }
        });
    }

    private void init(Bundle savedInstanceState) {
        mContext = MainActivity.this;
        toolbar = findViewById(R.id.tool_bar);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tviTitle = findViewById(R.id.toolbar_title);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        strDate = formatter.format(date);
        // Log.e("Date","=="+strDate);
        tviTitle.setText("Logistics");
        tvDate.setText(strDate);
        //  Log.e("Date ","==="+strDate);
        // tvDate.setText(strDate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

//        fragmentManager = getSupportFragmentManager();
//
//        putFragmentInContainer(savedInstanceState);
//
//        replaceFragment(Fragments.HOME_FRAGMENTS, new Bundle());

    }

    public void replaceFragment(final int fragmentToBePut, final Bundle bundle) {
        switch (fragmentToBePut) {
            case Fragments.HOME_FRAGMENTS:
                fragment = new LogisticsFragment();
                break;
        }
        fragment.setArguments(bundle);
        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.dash_board_content, fragment, fragment.getFragmentName());
            backStackEntryCount = fragmentManager.getBackStackEntryCount();
            fragmentTransaction.addToBackStack(fragment.getFragmentName());
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }


    public void setToolBarTitle(String title, String subTitle) {
        //tvToolBarTitle.setText(title);
        //iviUserImage.setImageResource(R.mipmap.screen_shot_2018_06_22_at_2_35_29_pm);
    }


    public void putFragmentInContainer(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new LogisticsFragment();
        fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
        fragmentTransaction.addToBackStack(fragment.getFragmentName());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (backStackEntryCount > 0) {
                    onBackPressed();
                }
                break;
        }
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //if (fragment.getFragmentName().equals("HomeFragment")) {
        //finish();
        //}
    }

    public class TabsAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public TabsAdapter(FragmentManager fm, int NoofTabs) {
            super(fm);
            this.mNumOfTabs = NoofTabs;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    LogisticsFragment logisticsFragment = new LogisticsFragment();
                    return logisticsFragment;
                case 1:
                    LogisticsFragment salesFragments1 = new LogisticsFragment();
                    return salesFragments1;
                case 2:
                    LogisticsFragment lkgsFragments = new LogisticsFragment();
                    return lkgsFragments;
                case 3:
                    LogisticsFragment productivityFragments = new LogisticsFragment();
                    return productivityFragments;
                default:
                    return null;
            }
        }
    }

    private void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_clear_all_black_24dp);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
    }
    private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment f = null;
                int itemId = menuItem.getItemId();
                if (itemId == R.id.refresh) {
                    f = new HomeFragment();
                } else if (itemId == R.id.stop) {
                    f = new SendFragment();
                }
                if (f != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, f);
                    transaction.commit();
                    drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            // Android home
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            // manage other entries if you have it ...
        }
        return true;
    }*/
}
