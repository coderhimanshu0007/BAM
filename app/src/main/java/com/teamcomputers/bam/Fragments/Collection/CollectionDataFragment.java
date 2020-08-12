package com.teamcomputers.bam.Fragments.Collection;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.Collection.CollectionCollectionModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Collection.KCollectionCollectionRequester;
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

public class CollectionDataFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;

    CollectionCollectionModel model;

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

        return rootView;
    }

    private void init(List<CollectionCollectionModel.CollectedData> progress, double totalAmount) {
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

    protected PieData generatePieData(List<CollectionCollectionModel.CollectedData> progress) {

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
        BackgroundExecutor.getInstance().execute(new KCollectionCollectionRequester());
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
                        break;
                    case Events.GET_COLLECTION_COLLECTION_SUCCESSFULL:
                        Log.e("Collection", eventObject.getObject().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceCollectionWIPDataResponse(eventObject.getObject().toString()));
                            model = (CollectionCollectionModel) BAMUtil.fromJson(String.valueOf(jsonObject), CollectionCollectionModel.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        if (model != null) {
                            tviECWInvoice.setText(model.getTable().get(0).getExpectedCollectionThisWeekInvoice().toString());
                            tviECWAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getExpectedCollectionThisWeekAmount()));

                            tviECMInvoice.setText(model.getTable().get(0).getExpectedCollectionThisMonthInvoice().toString());
                            tviECMAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getExpectedCollectionThisMonthAmount()));

                            tviPCWInvoice.setText(model.getTable().get(0).getPaymentCollectedThisWeekhInvoice().toString());
                            tviPCWAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getPaymentCollectedThisWeekAmount()));

                            tviPCMInvoice.setText(model.getTable().get(0).getPaymentCollectedThisMonthInvoice().toString());
                            tviPCMAmount.setText(KBAMUtils.getRoundOffValue(model.getTable().get(0).getPaymentCollectedThisMonthAmount()));

                            init(model.getProgress().getExpectedCollectionThisWeek(), model.getTable().get(0).getExpectedCollectionThisWeekAmount());
                            pieChart.invalidate();
                        }
                        break;
                    case Events.GET_COLLECTION_COLLECTION_UNSUCCESSFULL:
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

    @OnClick(R.id.cviECW)
    public void ECW() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llECWSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llECWSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
        init(model.getProgress().getExpectedCollectionThisWeek(), model.getTable().get(0).getExpectedCollectionThisWeekAmount());
        pieChart.invalidate();
    }

    @OnClick(R.id.cviECM)
    public void ECM() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llECMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llECMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
        init(model.getProgress().getExpectedCollectionThisMonth(), model.getTable().get(0).getExpectedCollectionThisMonthAmount());
        pieChart.invalidate();
    }

    @OnClick(R.id.cviPCW)
    public void PCW() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llPCWSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llPCWSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
        init(model.getProgress().getPaymentCollectedThisWeek(), model.getTable().get(0).getPaymentCollectedThisWeekAmount());
        pieChart.invalidate();
    }

    @OnClick(R.id.cviPCM)
    public void PCM() {
        selectItem();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            llPCMSelect.setBackgroundDrawable(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        } else {
            llPCMSelect.setBackground(ContextCompat.getDrawable(dashboardActivityContext, R.drawable.ic_path_5546));
        }
        init(model.getProgress().getPaymentCollectedThisMonth(), model.getTable().get(0).getPaymentCollectedThisMonthAmount());
        pieChart.invalidate();
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
        ECWBundle.putParcelable(CollectionDetailsFragment.COLLECTIONDATA,  model.getTable().get(0));
        ECWBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, ECWBundle);
    }

    @OnClick(R.id.txtBtnECMDetails)
    public void ExpectedCollectionthisMonth() {
        Bundle ECMBundle = new Bundle();
        ECMBundle.putString(CollectionDetailsFragment.FROM, "ECM");
        ECMBundle.putParcelable(CollectionDetailsFragment.COLLECTIONDATA,  model.getTable().get(0));
        ECMBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, ECMBundle);
    }

    @OnClick(R.id.txtBtnPCWDetails)
    public void PaymentCollectionthisWeek() {
        Bundle PCWBundle = new Bundle();
        PCWBundle.putString(CollectionDetailsFragment.FROM, "PCW");
        PCWBundle.putParcelable(CollectionDetailsFragment.COLLECTIONDATA,  model.getTable().get(0));
        PCWBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, PCWBundle);
    }

    @OnClick(R.id.txtBtnPCMDetails)
    public void PaymentCollectionthisMonth() {
        Bundle PCMBundle = new Bundle();
        PCMBundle.putString(CollectionDetailsFragment.FROM, "PCM");
        PCMBundle.putParcelable(CollectionDetailsFragment.COLLECTIONDATA,  model.getTable().get(0));
        PCMBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, PCMBundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
