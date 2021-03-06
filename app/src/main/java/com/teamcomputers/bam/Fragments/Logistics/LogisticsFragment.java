package com.teamcomputers.bam.Fragments.Logistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.CustomView.CustomViewPager;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Logistics.KLRefreshRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LogisticsFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;

    private String[] navLabels = {
            "Dispatch",
            "In-Transit",
            "Hold Delivery",
            "Acknowledgement"
    };

    String toolbarTitle = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_logistics, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        toolbarTitle = getString(R.string.Heading_Logistics);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(dashboardActivityContext).inflate(R.layout.nav_tab, null);

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
        final CustomViewPager viewPager = (CustomViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setPagingEnabled(false);
        TabsAdapter tabsAdapter = new TabsAdapter(getChildFragmentManager(), tabLayout.getTabCount());
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
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new LogisticsRefreshRequester());
        BackgroundExecutor.getInstance().execute(new KLRefreshRequester());
        int position = SharedPreferencesController.getInstance(dashboardActivityContext).getLogisticPageNo();
        tabLayout.getTabAt(position).select();
        SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(0);

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_screen_share);
        item.setVisible(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return LogisticsFragment.class.getSimpleName();
    }

    @Subscribe
    public void onEvent(final EventObject eventObject) {
        dashboardActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case Events.NO_INTERNET_CONNECTION:
                        dismissProgress();
                        showToast(ToastTexts.NO_INTERNET_CONNECTION);
                        break;
                    case Events.GET_LOGISTICS_REFRESH_SUCCESSFULL:
                        dashboardActivityContext.updateDate(eventObject.getObject().toString());
                        dismissProgress();
                        break;
                    case Events.GET_LOGISTICS_REFRESH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void toogleProfile(Boolean toogle) {
        super.toogleProfile(toogle);
        if (toogle) {
            //llProfile.setVisibility(View.VISIBLE);
            //Toast.makeText(dashboardActivityContext, "Toogle true", Toast.LENGTH_SHORT).show();
        } else {
            //llProfile.setVisibility(View.GONE);
            //Toast.makeText(dashboardActivityContext, "Toogle false", Toast.LENGTH_SHORT).show();
        }
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
                    DispatchFragment dispatchFragment = new DispatchFragment();
                    return dispatchFragment;
                case 1:
                    InTransitFragment inTransitFragment = new InTransitFragment();
                    return inTransitFragment;
                case 2:
                    HoldDeliveryFragment holdDeliveryFragment = new HoldDeliveryFragment();
                    return holdDeliveryFragment;
                case 3:
                    AcknowledgementFragment acknowledgementFragment = new AcknowledgementFragment();
                    return acknowledgementFragment;
                default:
                    return null;
            }
        }
    }

}
