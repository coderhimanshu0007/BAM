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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.OSAgeingModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CollectionWIPFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    private ArrayList<OSAgeingModel> osAgeingModelArrayList = new ArrayList<>();

    @BindView(R.id.pieChart)
    PieChart pieChart;

    @BindView(R.id.tviWIP0Invoice)
    TextView tviWIP0Invoice;
    @BindView(R.id.tviWIP0Amount)
    TextView tviWIP0Amount;

    @BindView(R.id.tviWIP16Invoice)
    TextView tviWIP16Invoice;
    @BindView(R.id.tviWIP16Amount)
    TextView tviWIP16Amount;

    @BindView(R.id.tviWIP30Invoice)
    TextView tviWIP30Invoice;
    @BindView(R.id.tviWIP30Amount)
    TextView tviWIP30Amount;

    @BindView(R.id.tviPDOSLInvoice)
    TextView tviPDOSLInvoice;
    @BindView(R.id.tviPDOSLAmount)
    TextView tviPDOSLAmount;

    @BindView(R.id.tviPDOSGInvoice)
    TextView tviPDOSGInvoice;
    @BindView(R.id.tviPDOSGAmount)
    TextView tviPDOSGAmount;

    @BindView(R.id.llWIP0Select)
    LinearLayout llWIP0Select;

    @BindView(R.id.llWIP16Select)
    LinearLayout llWIP16Select;

    @BindView(R.id.llWIP30Select)
    LinearLayout llWIP30Select;

    @BindView(R.id.llPDOSLSelect)
    LinearLayout llPDOSLSelect;

    @BindView(R.id.llPDOSGSelect)
    LinearLayout llPDOSGSelect;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_delivery_installation, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        //layoutManager = new LinearLayoutManager(dashboardActivityContext);
        //rviData.setLayoutManager(layoutManager);
        pieChart.getDescription().setEnabled(false);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        setData();

        //mAdapter = new OSAgeingAdapter(dashboardActivityContext, osAgeingModelArrayList);
        //rviData.setAdapter(mAdapter);

        return rootView;
    }

    private void setData() {
        ArrayList<PieEntry> NoOfEmp = new ArrayList<>();

        NoOfEmp.add(new PieEntry(1133f, "2010"));
        NoOfEmp.add(new PieEntry(1240f, "2011"));
        NoOfEmp.add(new PieEntry(1369f, "2012"));
        NoOfEmp.add(new PieEntry(1487f, "2013"));
        NoOfEmp.add(new PieEntry(1501f, "2014"));
        NoOfEmp.add(new PieEntry(1645f, "2015"));
        NoOfEmp.add(new PieEntry(1578f, "2016"));
        NoOfEmp.add(new PieEntry(1695f, "2017"));

        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        pieChart.setHoleRadius(80f);
        pieChart.setData(data);
        int[] rainbow = context.getResources().getIntArray(R.array.COLORFUL_COLORS);
        dataSet.setColors(rainbow);
        dataSet.setSelectionShift(30);
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
        return CollectionWIPFragment.class.getSimpleName();
    }

    @Subscribe
    public void onEvent(final EventObject eventObject) {
        dashboardActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case Events.NO_INTERNET_CONNECTION:
                        dismissProgress();
                        //showToast(ToastTexts.LOGIN_SUCCESSFULL);
                        //((DashbordActivity) getActivity()).replaceFragment(Fragments.ASSIGN_CALLS_MAP_FRAGMENTS, assignedCallsBundle);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.cviTotalOustanding)
    public void totalOustanding() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llWIP0Select.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llWIP0Select.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviWIP16)
    public void WIP16() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llWIP16Select.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llWIP16Select.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviWIP31)
    public void WIP31() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llWIP30Select.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llWIP30Select.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviPDOSL)
    public void PDOSL() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llPDOSLSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llPDOSLSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviPDOSG)
    public void PDOSG() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llPDOSGSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llPDOSGSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    private void selectItem() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llWIP0Select.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llWIP16Select.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llWIP30Select.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llPDOSLSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llPDOSGSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
        } else {
            llWIP0Select.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llWIP16Select.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llWIP30Select.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llPDOSLSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llPDOSGSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
        }
    }

    @OnClick(R.id.txtBtnWIP0Details)
    public void DeliveryInstallationPendingUpto15Days() {
        Bundle WIP0Bundle = new Bundle();
        WIP0Bundle.putString(WIPDetailsFragment.FROM, "WIP0");
        WIP0Bundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, WIP0Bundle);
    }

    @OnClick(R.id.txtBtnWIP16Details)
    public void DeliveryInstallationPendingMore15Days() {
        Bundle WIP16Bundle = new Bundle();
        WIP16Bundle.putString(WIPDetailsFragment.FROM, "WIP16");
        WIP16Bundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, WIP16Bundle);
    }

    @OnClick(R.id.txtBtnWIP30Details)
    public void DeliveryInstallationPendingMore30Days() {
        Bundle WIP30Bundle = new Bundle();
        WIP30Bundle.putString(WIPDetailsFragment.FROM, "WIP30");
        WIP30Bundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, WIP30Bundle);
    }

    @OnClick(R.id.txtBtnPDOSLDetails)
    public void PendingDocSubmissionUpto2Days() {
        Bundle PDOSLBundle = new Bundle();
        PDOSLBundle.putString(WIPDetailsFragment.FROM, "PDOSL");
        PDOSLBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, PDOSLBundle);
    }

    @OnClick(R.id.txtBtnPDOSGDetails)
    public void PendingDocSubmissionGT2Days() {
        Bundle PDOSGBundle = new Bundle();
        PDOSGBundle.putString(WIPDetailsFragment.FROM, "PDOSG");
        PDOSGBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.TOTAL_OUTSTANDING_FRAGMENT, PDOSGBundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
