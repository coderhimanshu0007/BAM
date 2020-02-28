package com.teamcomputers.bam.Fragments.NewSalesReceivable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.CustomView.CustomViewPager;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.NewSalesReceivable.SalesPersonListRequester;
import com.teamcomputers.bam.Requesters.NewSalesReceivable.SelectedCustomerListRequester;
import com.teamcomputers.bam.Requesters.NewSalesReceivable.SelectedProductListRequester;
import com.teamcomputers.bam.Requesters.OrderProcessing.OrderProcessingRefreshRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewRSMTabFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    private View rootView;
    private Unbinder unbinder;
    String userId = "";
    public String id = "";
    private DashboardActivity dashboardActivityContext;
    public FullSalesModel dataList, salesPersonDataList;
    public SalesCustomerModel customerList;

    private String[] navLabels = {
            "RSM",
            "Sales Person",
            "Customer",
            "Product"
    };

    private int[] navIcon = {
            R.drawable.ic_sales_person,
            R.drawable.ic_sales_person,
            R.drawable.ic_customer,
            R.drawable.ic_product
    };

    String title = "";
    String toolbarTitle = "";
    TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_rsm, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        title = getString(R.string.Heading_RSM);
        toolbarTitle = title;
        userId = getArguments().getString(USER_ID);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(dashboardActivityContext).inflate(R.layout.new_nav_tab, null);

            TextView tab_label = tab.findViewById(R.id.nav_label);
            ImageView iviNav_icon = tab.findViewById(R.id.iviNav_icon);

            iviNav_icon.setImageResource(navIcon[i]);
            tab_label.setText(navLabels[i]);
            if (i == 0) {
                tab_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
                iviNav_icon.setColorFilter(ContextCompat.getColor(context, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                tab_label.setTextColor(getResources().getColor(R.color.color_value_54));
                iviNav_icon.setColorFilter(ContextCompat.getColor(context, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
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
                if (tab.getPosition() == 0) {
                    toolbarTitle = getString(R.string.Heading_RSM);
                    dashboardActivityContext.setToolBarTitle(toolbarTitle);
                } else if (tab.getPosition() == 1) {
                    toolbarTitle = getString(R.string.Sales);
                    dashboardActivityContext.setToolBarTitle(toolbarTitle);
                } else if (tab.getPosition() == 2) {
                    toolbarTitle = getString(R.string.Customer);
                    dashboardActivityContext.setToolBarTitle(toolbarTitle);
                } else if (tab.getPosition() == 3) {
                    toolbarTitle = getString(R.string.Product);
                    dashboardActivityContext.setToolBarTitle(toolbarTitle);
                }
                viewPager.setCurrentItem(tab.getPosition());
                View tabView = tab.getCustomView();
                ImageView iviNav_icon = tabView.findViewById(R.id.iviNav_icon);
                TextView tab_label = tabView.findViewById(R.id.nav_label);
                tab_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
                iviNav_icon.setColorFilter(ContextCompat.getColor(context, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                ImageView iviNav_icon = tabView.findViewById(R.id.iviNav_icon);
                TextView tab_label = tabView.findViewById(R.id.nav_label);
                tab_label.setTextColor(getResources().getColor(R.color.color_value_54));
                iviNav_icon.setColorFilter(ContextCompat.getColor(context, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OrderProcessingRefreshRequester());
        int position = SharedPreferencesController.getInstance(dashboardActivityContext).getOPPageNo();
        tabLayout.getTabAt(position).select();
        SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(0);

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
        return NewRSMTabFragment.class.getSimpleName();
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
                    case Events.GET_ORDERPROCESING_REFRESH_SUCCESSFULL:
                        dashboardActivityContext.updateDate(eventObject.getObject().toString());
                        dismissProgress();
                        //eventObjects = eventObject;
                        break;
                    case Events.GET_ORDERPROCESING_REFRESH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_CLICK:
                        salesPersonDataList = (FullSalesModel) eventObject.getObject();
                        showProgress(ProgressDialogTexts.LOADING);
                        BackgroundExecutor.getInstance().execute(new SalesPersonListRequester(salesPersonDataList.getTMC(), "R2", "Sales", "", ""));
                        tabLayout.getTabAt(1).select();
                        break;
                    case ClickEvents.SP_CLICK:
                        dataList = (FullSalesModel) eventObject.getObject();
                        id = dataList.getTMC();
                        showProgress(ProgressDialogTexts.LOADING);
                        BackgroundExecutor.getInstance().execute(new SelectedCustomerListRequester(dataList.getTMC(), "R4", "Customer", "", ""));
                        tabLayout.getTabAt(2).select();
                        break;
                    case ClickEvents.CUSTOMER_SELECT:
                        customerList = (SalesCustomerModel) eventObject.getObject();
                        showProgress(ProgressDialogTexts.LOADING);
                        if (id.equals(userId)) {
                            BackgroundExecutor.getInstance().execute(new SelectedProductListRequester(id, "R1", "Product", customerList.getCustomerName(), ""));
                        } else if (id.equals("")) {
                            BackgroundExecutor.getInstance().execute(new SelectedProductListRequester(userId, "R1", "Product", customerList.getCustomerName(), ""));
                        } else {
                            BackgroundExecutor.getInstance().execute(new SelectedProductListRequester(id, "R4", "Product", customerList.getCustomerName(), ""));
                        }
                        tabLayout.getTabAt(3).select();
                        break;
                    case ClickEvents.STATE_SELECT:
                        customerList = (SalesCustomerModel) eventObject.getObject();
                        showProgress(ProgressDialogTexts.LOADING);
                        if (id.equals(userId)) {
                            BackgroundExecutor.getInstance().execute(new SelectedProductListRequester(id, "R1", "Product", customerList.getCustomerName(), customerList.getStateCodeWise().get(0).getStateCode()));
                        } else if (id.equals("")) {
                            BackgroundExecutor.getInstance().execute(new SelectedProductListRequester(userId, "R1", "Product", customerList.getCustomerName(), customerList.getStateCodeWise().get(0).getStateCode()));
                        } else {
                            BackgroundExecutor.getInstance().execute(new SelectedProductListRequester(id, "R4", "Product", customerList.getCustomerName(), customerList.getStateCodeWise().get(0).getStateCode()));
                        }
                        tabLayout.getTabAt(3).select();
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
            Bundle bundle = new Bundle();
            bundle.putString(SalesPersonFragment.USER_ID, userId);
            switch (position) {
                case 0:
                    NewRSMFragment newRSMTabFragment = new NewRSMFragment();
                    newRSMTabFragment.setArguments(bundle);
                    return newRSMTabFragment;
                case 1:
                    SalesPersonFragment salesPersonFragment = new SalesPersonFragment();
                    salesPersonFragment.setArguments(bundle);
                    return salesPersonFragment;
                case 2:
                    NewCustomerFragment newCustomerFragment = new NewCustomerFragment();
                    newCustomerFragment.setArguments(bundle);
                    return newCustomerFragment;
                case 3:
                    NewProductFragment newProductFragment = new NewProductFragment();
                    newProductFragment.setArguments(bundle);
                    return newProductFragment;
                default:
                    return null;
            }
        }
    }
}
