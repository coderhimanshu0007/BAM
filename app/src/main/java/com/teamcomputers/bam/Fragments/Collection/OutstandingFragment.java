package com.teamcomputers.bam.Fragments.Collection;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.Collection.OutstandingModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Collection.KCollectionOutstandingRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.KBAMUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OutstandingFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;

    @BindView(R.id.pieChart)
    PieChart pieChart;

    @BindView(R.id.tviTOInvoice)
    TextView tviTOInvoice;
    @BindView(R.id.tviTOAmount)
    TextView tviTOAmount;

    @BindView(R.id.tviCOInvoice)
    TextView tviCOInvoice;
    @BindView(R.id.tviCOAmount)
    TextView tviCOAmount;

    @BindView(R.id.tviCOCMInvoice)
    TextView tviCOCMInvoice;
    @BindView(R.id.tviCOCMAmount)
    TextView tviCOCMAmount;

    @BindView(R.id.tviCOSMInvoice)
    TextView tviCOSMInvoice;
    @BindView(R.id.tviCOSMAmount)
    TextView tviCOSMAmount;

    @BindView(R.id.llTOSelect)
    LinearLayout llTOSelect;

    @BindView(R.id.llCOSelect)
    LinearLayout llCOSelect;

    @BindView(R.id.llCOCMSelect)
    LinearLayout llCOCMSelect;

    @BindView(R.id.llCOSMSelect)
    LinearLayout llCOSMSelect;

    OutstandingModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_outstanding, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        //layoutManager = new LinearLayoutManager(dashboardActivityContext);
        //gviData.setLayoutManager(layoutManager);

        setData();

        //mAdapter = new KOutstandingAdapter(dashboardActivityContext, outstandingModelArrayList);
        //gviData.setAdapter(mAdapter);

        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new KCollectionOutstandingRequester());

        return rootView;
    }

    private void setData() {
        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new PieEntry(945f, 2008));
        NoOfEmp.add(new PieEntry(1040f, 2009));
        NoOfEmp.add(new PieEntry(1133f, 2010));
        NoOfEmp.add(new PieEntry(1240f, 2011));
        NoOfEmp.add(new PieEntry(1369f, 2012));
        NoOfEmp.add(new PieEntry(1487f, 2013));
        NoOfEmp.add(new PieEntry(1501f, 2014));
        NoOfEmp.add(new PieEntry(1645f, 2015));
        NoOfEmp.add(new PieEntry(1578f, 2016));
        NoOfEmp.add(new PieEntry(1695f, 2017));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Employees");

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        ArrayList year = new ArrayList();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");
        PieData data = new PieData(dataSet);
        pieChart.setHoleRadius(80f);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);
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
        return OutstandingFragment.class.getSimpleName();
    }

    @Subscribe
    public void onEvent(final EventObject eventObject) {
        dashboardActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case Events.NO_INTERNET_CONNECTION:
                        dismissProgress();
                        break;
                    case Events.GET_COLLECTION_OUTSTANDING_SUCCESSFULL:
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceCollectionDataResponse(eventObject.getObject().toString()));
                            model = (OutstandingModel) BAMUtil.fromJson(String.valueOf(jsonObject), OutstandingModel.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        tviTOInvoice.setText(model.getTable().get(0).getTotalOutStandingInvoice().toString());
                        tviTOAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getTotalOutStandingAmount()));
                        tviCOInvoice.setText(model.getTable().get(0).getCollectibleOutStandingInvoice().toString());
                        tviCOAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getCollectibleOutStandingAmount()));
                        tviCOCMInvoice.setText(model.getTable().get(0).getCollectibleOutStandingCurrentMonthInvoice().toString());
                        tviCOCMAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getCollectibleOutStandingCurrentMonthAmount()));
                        tviCOSMInvoice.setText(model.getTable().get(0).getCollectibleOutStandingSubsequentMonthInvoice().toString());
                        tviCOSMAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getCollectibleOutStandingSubsequentMonthAmount()));

                        //init();
                        //chart.setData(generatePieData());
                        //chart.notifyDataSetChanged();
                        //chart.invalidate();
                        break;
                    case Events.GET_COLLECTION_OUTSTANDING_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.OOPS_MESSAGE:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.INTERNAL_SERVER_ERROR:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.cviTO)
    public void TO() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llTOSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llTOSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviCO)
    public void CO() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llCOSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llCOSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviCOCM)
    public void COCM() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llCOCMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llCOCMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviCOSM)
    public void COSM() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llCOSMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llCOSMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    private void selectItem() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llTOSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llCOSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llCOCMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llCOSMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
        } else {
            llTOSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llCOSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llCOCMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llCOSMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
        }
    }

    @OnClick(R.id.txtBtnTODetails)
    public void TotalOutstandingDetails() {
        showToast("Work in progress...");
        /*Bundle outstandingBundle = new Bundle();
        outstandingBundle.putString(TotalOutstandingFragment.FROM, "TOTALOUTSTANDING");
        outstandingBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.TOTAL_OUTSTANDING_FRAGMENT, outstandingBundle);*/
    }

    @OnClick(R.id.txtBtnCODetails)
    public void CollectibleOutstandingDetails() {
        Bundle outstandingBundle = new Bundle();
        outstandingBundle.putString(TotalOutstandingFragment.FROM, "COLLECTIBLEOUTSTANDING");
        outstandingBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.TOTAL_OUTSTANDING_FRAGMENT, outstandingBundle);
    }

    @OnClick(R.id.txtBtnCOCMDetails)
    public void CollectibleOutstandingCurrentMonthDetails() {
        Bundle outstandingBundle = new Bundle();
        outstandingBundle.putString(TotalOutstandingFragment.FROM, "COCM");
        outstandingBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.TOTAL_OUTSTANDING_FRAGMENT, outstandingBundle);
    }

    @OnClick(R.id.txtBtnCOSMDetails)
    public void CollectibleOutstandingSubsequentDetails() {
        Bundle outstandingBundle = new Bundle();
        outstandingBundle.putString(TotalOutstandingFragment.FROM, "COSM");
        outstandingBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.TOTAL_OUTSTANDING_FRAGMENT, outstandingBundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
