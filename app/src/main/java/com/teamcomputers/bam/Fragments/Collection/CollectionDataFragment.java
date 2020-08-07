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
import com.teamcomputers.bam.Adapters.ExpectedCollectionAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.ExpectedCollectionModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CollectionDataFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    private ExpectedCollectionAdapter mAdapter;
    private ArrayList<ExpectedCollectionModel> collectionModelArrayList = new ArrayList<>();
    private ArrayList<ExpectedCollectionModel> expectedCollectionModelArrayList = new ArrayList<>();
    private ArrayList<ExpectedCollectionModel> paymentCollectionModelArrayList = new ArrayList<>();

    @BindView(R.id.pieChart)
    PieChart pieChart;

    @BindView(R.id.tviECWInvoice)
    TextView tviECWInvoice;
    @BindView(R.id.tviECWAmount)
    TextView tviECWAmount;

    @BindView(R.id.tviECMInvoice)
    TextView tviECMInvoice;
    @BindView(R.id.tviECMAmount)
    TextView tviECMAmount;

    @BindView(R.id.tviPCWInvoice)
    TextView tviPCWInvoice;
    @BindView(R.id.tviPCWAmount)
    TextView tviPCWAmount;

    @BindView(R.id.tviPCMInvoice)
    TextView tviPCMInvoice;
    @BindView(R.id.tviPCMAmount)
    TextView tviPCMAmount;

    @BindView(R.id.llECWSelect)
    LinearLayout llECWSelect;

    @BindView(R.id.llECMSelect)
    LinearLayout llECMSelect;

    @BindView(R.id.llPCWSelect)
    LinearLayout llPCWSelect;

    @BindView(R.id.llPCMSelect)
    LinearLayout llPCMSelect;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_collection_data, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        setData();

        return rootView;
    }

    private void setData() {
        pieChart.getDescription().setEnabled(false);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

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
        pieChart.setHoleRadius(75f);
        pieChart.setTransparentCircleRadius(78f);
        pieChart.setEntryLabelColor(R.color.graph_color1);

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
        return CollectionDataFragment.class.getSimpleName();
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

    @OnClick(R.id.cviECW)
    public void ECW() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llECWSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llECWSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviECM)
    public void ECM() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llECMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llECMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviPCW)
    public void PCW() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llPCWSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llPCWSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    @OnClick(R.id.cviPCM)
    public void PCM() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llPCMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llPCMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
    }

    private void selectItem() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llECWSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llECMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llPCWSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llPCMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
        } else {
            llECWSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llECMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llPCWSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
            llPCMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5547));
        }
    }

    @OnClick(R.id.txtBtnECWDetails)
    public void ExpectedCollectionthisWeek() {
        Bundle ECWBundle = new Bundle();
        ECWBundle.putString(CollectionDetailsFragment.FROM, "ECW");
        ECWBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, ECWBundle);
    }

    @OnClick(R.id.txtBtnECMDetails)
    public void ExpectedCollectionthisMonth() {
        Bundle ECMBundle = new Bundle();
        ECMBundle.putString(CollectionDetailsFragment.FROM, "ECM");
        ECMBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, ECMBundle);
    }

    @OnClick(R.id.txtBtnPCWDetails)
    public void PaymentCollectionthisWeek() {
        Bundle PCWBundle = new Bundle();
        PCWBundle.putString(CollectionDetailsFragment.FROM, "PCW");
        PCWBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, PCWBundle);
    }

    @OnClick(R.id.txtBtnPCMDetails)
    public void PaymentCollectionthisMonth() {
        Bundle PCMBundle = new Bundle();
        PCMBundle.putString(CollectionDetailsFragment.FROM, "PCM");
        PCMBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, PCMBundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
