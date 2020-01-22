package com.teamcomputers.bam.Fragments.SalesReceivable;

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
import com.teamcomputers.bam.Requesters.SalesReceivable.ReceivableRefreshRequester;
import com.teamcomputers.bam.Requesters.SalesReceivable.SalesRefreshRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SalesReceivableFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private String[] navLabels = {
            "Sales",
            "Outstanding"
    };
    String toolbarTitle = "";

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sr, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        dashboardActivityContext = (DashboardActivity) context;
        toolbarTitle = getString(R.string.Heading_Sales_Receivable);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
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
                if(tab.getPosition()==0){
                    showProgress(ProgressDialogTexts.LOADING);
                    BackgroundExecutor.getInstance().execute(new SalesRefreshRequester());
                } else if(tab.getPosition()==1){
                    showProgress(ProgressDialogTexts.LOADING);
                    BackgroundExecutor.getInstance().execute(new ReceivableRefreshRequester());
                }
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

        int position = SharedPreferencesController.getInstance(dashboardActivityContext).getSalesReceivablePageNo();
        tabLayout.getTabAt(position).select();
        SharedPreferencesController.getInstance(dashboardActivityContext).setSalesReceivablePageNo(0);
        if(position==0){
            showProgress(ProgressDialogTexts.LOADING);
            BackgroundExecutor.getInstance().execute(new SalesRefreshRequester());
        } else if(position==1){
            showProgress(ProgressDialogTexts.LOADING);
            BackgroundExecutor.getInstance().execute(new ReceivableRefreshRequester());
        }

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
        return SalesReceivableFragment.class.getSimpleName();
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
                    case Events.GET_SALES_REFRESH_SUCCESSFULL:
                        dashboardActivityContext.updateDate(eventObject.getObject().toString());
                        dismissProgress();
                        break;
                    case Events.GET_RECEIVABLE_REFRESH_SUCCESSFULL:
                        dashboardActivityContext.updateDate(eventObject.getObject().toString());
                        dismissProgress();
                        break;
                    case Events.GET_RECEIVABLE_REFRESH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SALES_REFRESH_UNSUCCESSFULL:
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
                    SalesFragment salesFragment = new SalesFragment();
                    return salesFragment;
                case 1:
                    OutstandingFragment outstandingFragment = new OutstandingFragment();
                    return outstandingFragment;
                default:
                    return null;
            }
        }
    }

}