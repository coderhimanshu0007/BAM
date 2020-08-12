package com.teamcomputers.bam.Fragments.Collection;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
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
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.Collection.CollectionWIPModel;
import com.teamcomputers.bam.Models.OSAgeingModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Collection.KCollectionWIPRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.KBAMUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    CollectionWIPModel model;

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

        return rootView;
    }

    private void init(List<CollectionWIPModel.ProgressData> progress, double totalAmount) {
        pieChart.getDescription().setEnabled(false);

        pieChart.setCenterText(generateCenterText(totalAmount));
        pieChart.setCenterTextSize(10f);
        //chart.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        pieChart.setHoleRadius(75f);
        pieChart.setTransparentCircleRadius(78f);
        pieChart.setEntryLabelColor(R.color.graph_color1);

        pieChart.getDescription().setEnabled(false);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        pieChart.setData(generatePieData(progress));
    }

    @SuppressLint("ResourceAsColor")
    private SpannableString generateCenterText(double total) {
        String tot = KBAMUtils.getRoundOffValue(total);
        int len = tot.length();
        SpannableString s = new SpannableString(tot);
        s.setSpan(new RelativeSizeSpan(2.5f), 0, len, 0);
        //s.setSpan(new ForegroundColorSpan(R.color.color_progress_end), 0, len, 0);
        //s.setSpan(new RelativeSizeSpan(1f), len, s.length(), 0);
        //s.setSpan(new ForegroundColorSpan(R.color.text_color_login), len, s.length(), 0);
        return s;
    }

    protected PieData generatePieData(List<CollectionWIPModel.ProgressData> progress) {

        ArrayList<PieEntry> entries1 = new ArrayList<>();

        for (int i = 0; i < progress.size(); i++) {
            //OutstandingModel.ProgressInfo progress = model.getProgress().getCollectibleOutstanding().get(i);
            //if (!progress.getBu().equals("null")) {
            String val = KBAMUtils.getRoundOffValue(progress.get(i).getAmount());
            float x = Float.parseFloat(val);
            entries1.add(new PieEntry(x, progress.get(i).getBu()));
            //}
        }

        PieDataSet ds1 = new PieDataSet(entries1, "");

        ds1.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        ds1.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        int[] rainbow = context.getResources().getIntArray(R.array.COLLECTION_COLORS);
        ds1.setColors(rainbow);
        pieChart.animateXY(5000, 5000);
        //ds1.setSliceSpace(2f);
        ds1.setSelectionShift(30);
        ds1.setValueTextColor(Color.BLACK);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);
        //d.setValueTypeface(tf);

        return d;
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new KCollectionWIPRequester());
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
                    case Events.GET_COLLECTION_DELIVERY_INSTALLATION_SUCCESSFULL:
                        Log.e("WIP", eventObject.getObject().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceCollectionWIPDataResponse(eventObject.getObject().toString()));
                            model = (CollectionWIPModel) BAMUtil.fromJson(String.valueOf(jsonObject), CollectionWIPModel.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        if (model != null) {
                            tviWIP0Invoice.setText(model.getTable().get(0).getWiP015DaysInvoice().toString());
                            tviWIP0Amount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getWiP015DaysAmount()));

                            tviWIP16Invoice.setText(model.getTable().get(0).getWiP1630DaysInvoice().toString());
                            tviWIP16Amount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getWiP1630aysAmount()));

                            tviWIP30Invoice.setText(model.getTable().get(0).getWiP30DaysInvoice().toString());
                            tviWIP30Amount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getWiP30DaysAmount()));

                            tviPDOSLInvoice.setText(model.getTable().get(0).getWipPendingDocSubmissionLessThan2DaysInvoice().toString());
                            tviPDOSLAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getWipPendingDocSubmissionLessThan2DaysAmount()));

                            tviPDOSGInvoice.setText(model.getTable().get(0).getWipPendingDocSubmissionGreaterThan2DaysInvoice().toString());
                            tviPDOSGAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getWipPendingDocSubmissionGreaterThan2DaysAmount()));

                            init(model.getProgress().getWiP015Days(), model.getTable().get(0).getWiP015DaysAmount());
                            pieChart.invalidate();
                        }
                        break;
                    case Events.GET_COLLECTION_DELIVERY_INSTALLATION_UNSUCCESSFULL:
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

    @OnClick(R.id.cviWIP0)
    public void WIP0() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llWIP0Select.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llWIP0Select.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
        init(model.getProgress().getWiP015Days(), model.getTable().get(0).getWiP015DaysAmount());
        pieChart.invalidate();
    }

    @OnClick(R.id.cviWIP16)
    public void WIP16() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llWIP16Select.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llWIP16Select.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
        init(model.getProgress().getWiP1630Days(), model.getTable().get(0).getWiP1630aysAmount());
        pieChart.invalidate();
    }

    @OnClick(R.id.cviWIP31)
    public void WIP31() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llWIP30Select.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llWIP30Select.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
        init(model.getProgress().getWiP30Days(), model.getTable().get(0).getWiP30DaysAmount());
        pieChart.invalidate();
    }

    @OnClick(R.id.cviPDOSL)
    public void PDOSL() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llPDOSLSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llPDOSLSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
        init(model.getProgress().getWipPendingDocLessThan2Days(), model.getTable().get(0).getWipPendingDocSubmissionLessThan2DaysAmount());
        pieChart.invalidate();
    }

    @OnClick(R.id.cviPDOSG)
    public void PDOSG() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llPDOSGSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llPDOSGSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
        init(model.getProgress().getWipPendingDocGreaterThan2Days(), model.getTable().get(0).getWipPendingDocSubmissionGreaterThan2DaysAmount());
        pieChart.invalidate();
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
        dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, WIP0Bundle);
    }

    @OnClick(R.id.txtBtnWIP16Details)
    public void DeliveryInstallationPendingMore15Days() {
        Bundle WIP16Bundle = new Bundle();
        WIP16Bundle.putString(WIPDetailsFragment.FROM, "WIP16");
        WIP16Bundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, WIP16Bundle);
    }

    @OnClick(R.id.txtBtnWIP30Details)
    public void DeliveryInstallationPendingMore30Days() {
        //showToast("Work in progress...");
        Bundle WIP30Bundle = new Bundle();
        WIP30Bundle.putString(WIPDetailsFragment.FROM, "WIP30");
        WIP30Bundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, WIP30Bundle);
    }

    @OnClick(R.id.txtBtnPDOSLDetails)
    public void PendingDocSubmissionUpto2Days() {
        //showToast("Work in progress...");
        Bundle PDOSLBundle = new Bundle();
        PDOSLBundle.putString(WIPDetailsFragment.FROM, "PDOSL");
        PDOSLBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, PDOSLBundle);
    }

    @OnClick(R.id.txtBtnPDOSGDetails)
    public void PendingDocSubmissionGT2Days() {
        //showToast("Work in progress...");
        Bundle PDOSGBundle = new Bundle();
        PDOSGBundle.putString(WIPDetailsFragment.FROM, "PDOSG");
        PDOSGBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, PDOSGBundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
