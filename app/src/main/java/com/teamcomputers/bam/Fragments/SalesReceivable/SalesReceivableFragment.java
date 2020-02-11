package com.teamcomputers.bam.Fragments.SalesReceivable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.CustomView.CircularSeekBar;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.SalesReceivableModel;
import com.teamcomputers.bam.Models.YTDQTDModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.SalesReceivableRequester;
import com.teamcomputers.bam.Requesters.SalesReceivable.SalesRefreshRequester;
import com.teamcomputers.bam.Requesters.SalesReceivable.YTDQTDRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.tviYtd)
    TextView tviYtd;
    @BindView(R.id.tviQtd)
    TextView tviQtd;
    @BindView(R.id.tviMtd)
    TextView tviMtd;
    @BindView(R.id.tviOpenSalesOrder)
    TextView tviOpenSalesOrder;
    @BindView(R.id.tviOutstanding)
    TextView tviOutstanding;
    @BindView(R.id.tviDays)
    TextView tviDays;
    @BindView(R.id.seek_bar)
    CircularSeekBar seek_bar;

    int days = 260;
    String type = null;
    String userId = null;
    String level = null;
    String dataType = null;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sales_receivable, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        dashboardActivityContext = (DashboardActivity) context;
        toolbarTitle = getString(R.string.Heading_Sales_Receivable);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        /*TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
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
*/
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new SalesRefreshRequester());
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
        seek_bar.setProgress(0);
        //seek_bar.setProgress(250);
        tviDays.setText(String.valueOf(0));
        seek_bar.setCircleProgressColorGreen();
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
                        BackgroundExecutor.getInstance().execute(new SalesReceivableRequester("1464"));
                        break;
                    case Events.GET_SALES_REFRESH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_RECEIVABLE_REFRESH_SUCCESSFULL:
                        dashboardActivityContext.updateDate(eventObject.getObject().toString());
                        dismissProgress();
                        break;
                    case Events.GET_RECEIVABLE_REFRESH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SALES_RECEIVABLE_SUCCESSFULL:
                        dismissProgress();
                        SalesReceivableModel model = new SalesReceivableModel();
                        try {
                            JSONObject jsonObject = new JSONObject(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            model = (SalesReceivableModel) BAMUtil.fromJson(String.valueOf(jsonObject), SalesReceivableModel.class);
                            //if (model != null)
                            //    SharedPreferencesController.getInstance(dashboardActivityContext).setAcknowledgementData(model);

                            userId = model.getUserId();
                            level = model.getLevel();
                            dataType = "RSM";
                            tviYtd.setText(BAMUtil.getRoundOffValue(model.getYTD()));
                            tviQtd.setText(BAMUtil.getRoundOffValue(model.getQTD()));
                            tviMtd.setText(BAMUtil.getRoundOffValue(model.getMTD()));

                            tviOpenSalesOrder.setText(BAMUtil.getRoundOffValue(model.getOpenSalesOrder()));
                            tviOutstanding.setText(BAMUtil.getRoundOffValue(model.getOutStanding()));
                            tviDays.setText(BAMUtil.getRoundOffValue(model.getDSO()));

                            seek_bar.setMax(365);
                            Integer dso = model.getDSO().intValue();
                            seek_bar.setProgress(dso);
                            //seek_bar.setProgress(250);
                            tviDays.setText(String.valueOf(dso));
                            if (dso > 30)
                                seek_bar.setCircleProgressColorRed();
                            seek_bar.setEnabled(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /*if (model != null) {
                            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(model[0].getInvoices())));
                            tviAmounts.setText(BAMUtil.getRoundOffValue((Double) model[0].getAmount()));
                        }*/
                        break;
                    case Events.GET_YTDQTD_SUCCESSFULL:
                        YTDQTDModel ytdqtdModel = (YTDQTDModel) BAMUtil.fromJson(String.valueOf(eventObject.getObject()), YTDQTDModel.class);
                        dismissProgress();
                        showDialog(type, ytdqtdModel);
                        break;
                    case Events.GET_YTDQTD_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SALES_RECEIVABLE_UNSUCCESSFULL:
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

    AlertDialog alertDialog;

    @OnClick(R.id.llYTD)
    public void YTDCllick() {
        //showDialog("YTD");
        type = "YTD";
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new YTDQTDRequester("1464"));
    }

    @OnClick(R.id.llQTD)
    public void QTDCllick() {
        //showDialog("QTD");
        type = "QTD";
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new YTDQTDRequester("1464"));
    }

    @OnClick(R.id.txtSalesAnalysis)
    public void salesAnalysis() {
        if (level.equals("R1")) {
            Bundle rsmDataBundle = new Bundle();
            //rsmDataBundle.putParcelable(RSMFragment.USER_PROFILE, (RSMDataModel) eventObject.getObject());
            rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.SALES_ANALYSIS_FRAGMENT, rsmDataBundle);
        }
        //EventBus.getDefault().post(new EventObject(Events.SALESANALYSIS, null));
    }

    @OnClick(R.id.txtBtnOpenSellsOrderAnalysis)
    public void btnOpenSellsOrderAnalysis() {

    }


    @OnClick(R.id.txtBtnOutstandingAnalysis)
    public void btnOutstandingAnalysis() {

    }


    @OnClick(R.id.txtBtnDSOAnalysis)
    public void btnDSOAnalysis() {

    }


    public void showDialog(String type, YTDQTDModel ytdqtdModel) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(dashboardActivityContext);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sa_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        TextView tviDialogType = (TextView) dialogView.findViewById(R.id.tviDialogType);
        ImageView iviCloseDialogType = (ImageView) dialogView.findViewById(R.id.iviCloseDialogType);

        LinearLayout llQTD = (LinearLayout) dialogView.findViewById(R.id.llQTD);
        LinearLayout llYTD = (LinearLayout) dialogView.findViewById(R.id.llYTD);

        TextView tviQTDHeading1 = (TextView) dialogView.findViewById(R.id.tviQTDHeading1);
        TextView tviQTDHeading2 = (TextView) dialogView.findViewById(R.id.tviQTDHeading2);
        TextView tviQTDHeading3 = (TextView) dialogView.findViewById(R.id.tviQTDHeading3);

        TextView tviQTDValue1 = (TextView) dialogView.findViewById(R.id.tviQTDValue1);
        TextView tviQTDValue2 = (TextView) dialogView.findViewById(R.id.tviQTDValue2);
        TextView tviQTDValue3 = (TextView) dialogView.findViewById(R.id.tviQTDValue3);

        TextView tviYTDHeading1 = (TextView) dialogView.findViewById(R.id.tviYTDHeading1);
        TextView tviYTDHeading2 = (TextView) dialogView.findViewById(R.id.tviYTDHeading2);
        TextView tviYTDHeading3 = (TextView) dialogView.findViewById(R.id.tviYTDHeading3);
        TextView tviYTDHeading4 = (TextView) dialogView.findViewById(R.id.tviYTDHeading4);

        TextView tviYTDValue1 = (TextView) dialogView.findViewById(R.id.tviYTDValue1);
        TextView tviYTDValue2 = (TextView) dialogView.findViewById(R.id.tviYTDValue2);
        TextView tviYTDValue3 = (TextView) dialogView.findViewById(R.id.tviYTDValue3);
        TextView tviYTDValue4 = (TextView) dialogView.findViewById(R.id.tviYTDValue4);

        tviDialogType.setText(type);

        if (type.equals("YTD")) {
            llQTD.setVisibility(View.GONE);
            llYTD.setVisibility(View.VISIBLE);
            List<YTDQTDModel.YTD> ytd = ytdqtdModel.getYTD();
            tviYTDHeading1.setText(ytd.get(0).getName());
            tviYTDHeading2.setText(ytd.get(1).getName());
            tviYTDHeading3.setText(ytd.get(2).getName());
            tviYTDHeading4.setText(ytd.get(3).getName());
            tviYTDValue1.setText(BAMUtil.getRoundOffValue(ytd.get(0).getValue()));
            tviYTDValue2.setText(BAMUtil.getRoundOffValue(ytd.get(1).getValue()));
            tviYTDValue3.setText(BAMUtil.getRoundOffValue(ytd.get(2).getValue()));
            tviYTDValue4.setText(BAMUtil.getRoundOffValue(ytd.get(3).getValue()));
        } else if (type.equals("QTD")) {
            llQTD.setVisibility(View.VISIBLE);
            llYTD.setVisibility(View.GONE);
            List<YTDQTDModel.QTD> qtd = ytdqtdModel.getQTD();
            tviQTDHeading1.setText(qtd.get(0).getName());
            tviQTDHeading2.setText(qtd.get(1).getName());
            tviQTDHeading3.setText(qtd.get(2).getName());
            tviQTDValue1.setText(BAMUtil.getRoundOffValue(qtd.get(0).getValue()));
            tviQTDValue2.setText(BAMUtil.getRoundOffValue(qtd.get(1).getValue()));
            tviQTDValue3.setText(BAMUtil.getRoundOffValue(qtd.get(2).getValue()));
        }

        iviCloseDialogType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

}