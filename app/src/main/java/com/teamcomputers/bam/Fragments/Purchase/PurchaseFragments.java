package com.teamcomputers.bam.Fragments.Purchase;

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
import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.CustomView.CustomViewPager;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.ActiveEmployeeAccessModel;
import com.teamcomputers.bam.Models.SessionDataModel;
import com.teamcomputers.bam.Models.SessionDetailsModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Purchase.PurchaseRefreshRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.KBAMUtils;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PurchaseFragments extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;

    ActiveEmployeeAccessModel activeEmployeeAccessModel;
    Date currentDate;
    String logInTime, logOutTime;

    private String[] navLabels = {
            "Sales Order",
            "Billing",
            "Stock",
            "EDD"
    };

    String toolbarTitle = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_processing, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        toolbarTitle = getString(R.string.Heading_OrderProcessing);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        activeEmployeeAccessModel = SharedPreferencesController.getInstance(BAMApplication.getInstance()).getActiveEmployeeAccess();
        currentDate = Calendar.getInstance().getTime();
        logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);

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
        BackgroundExecutor.getInstance().execute(new PurchaseRefreshRequester());
        int position = SharedPreferencesController.getInstance(dashboardActivityContext).getPurchasePageNo();
        tabLayout.getTabAt(position).select();
        SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(0);

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
    public void onDestroy() {
        super.onDestroy();
        logOutTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);

        SessionDetailsModel sessionDetailsModel = new SessionDetailsModel();
        sessionDetailsModel.setSessionId(activeEmployeeAccessModel.getData().getSessionId());
        List<SessionDataModel> sessionDataModelList = new ArrayList<>();
        if (null != SharedPreferencesController.getInstance(dashboardActivityContext).getSessionDetail()) {
            sessionDataModelList = SharedPreferencesController.getInstance(dashboardActivityContext).getSessionDetail().getData();
        }
        SessionDataModel sessionDataModel = new SessionDataModel();
        sessionDataModel.setModule("Purchase");
        sessionDataModel.setPage("");
        sessionDataModel.setLogInTimeStamp(logInTime);
        sessionDataModel.setLogOutTimeStamp(logOutTime);
        sessionDataModel.setOs("Android");
        sessionDataModel.setSharing(1);
        sessionDataModelList.add(sessionDataModel);
        sessionDetailsModel.setData(sessionDataModelList);
        SharedPreferencesController.getInstance(dashboardActivityContext).setSessionDetail(sessionDetailsModel);

    }

    @Override
    public String getFragmentName() {
        return PurchaseFragments.class.getSimpleName();
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
                    case Events.GET_PURCHASE_REFRESH_SUCCESSFULL:
                        dashboardActivityContext.updateDate(eventObject.getObject().toString());
                        dismissProgress();
                        break;
                    case Events.GET_PURCHASE_REFRESH_UNSUCCESSFULL:
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
                    SalesOrderFragment salesOrderFragment = new SalesOrderFragment();
                    return salesOrderFragment;
                case 1:
                    BillingFragment billingFragment = new BillingFragment();
                    return billingFragment;
                case 2:
                    StockFragment stockFragment = new StockFragment();
                    return stockFragment;
                case 3:
                    EDDFragment eddFragment = new EDDFragment();
                    return eddFragment;
                default:
                    return null;
            }
        }
    }
}
