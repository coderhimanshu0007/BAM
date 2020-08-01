package com.teamcomputers.bam.Fragments.Collection;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.Collection.KCollectionAgeingAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.Collection.AgeingModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Collection.KCollectionAgeingRequester;
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
import butterknife.Unbinder;

public class OSAgeingFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.pieChart)
    PieChart chart;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private KCollectionAgeingAdapter mAdapter;
    AgeingModel model = new AgeingModel();

    //Typeface tf;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_os_ageing, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        //tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");

        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new KCollectionAgeingRequester());

        //init();

        //setData();

        return rootView;
    }

    private void init() {
        chart.getDescription().setEnabled(false);

        //chart.setCenterTextTypeface(tf);
        chart.setCenterText(generateCenterText());
        chart.setCenterTextSize(10f);
        //chart.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        chart.setHoleRadius(80f);
        //chart.setTransparentCircleRadius(90f);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        chart.setData(generatePieData());
    }

    @SuppressLint("ResourceAsColor")
    private SpannableString generateCenterText() {
        float total = 0;
        for (int i = 0; i < model.getProgress().size(); i++) {
            AgeingModel.Progress progress = model.getProgress().get(i);
            String val = KBAMUtils.getRoundOffValue(progress.getAmount());
            float x = (float) Double.parseDouble(val);
            total = total + x;
        }
        String tot = String.valueOf(total);
        int len = tot.length() + 1;
        SpannableString s = new SpannableString(tot + "\nCOLLECTIBLE\nOUTSTANDING");
        s.setSpan(new RelativeSizeSpan(2.5f), 0, len, 0);
        //s.setSpan(new ForegroundColorSpan(R.color.color_progress_end), 0, len, 0);
        s.setSpan(new RelativeSizeSpan(1f), len, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(R.color.text_color_login), len, s.length(), 0);
        return s;
    }

    protected PieData generatePieData() {

        ArrayList<PieEntry> entries1 = new ArrayList<>();

        for (int i = 0; i < model.getProgress().size(); i++) {
            AgeingModel.Progress progress = model.getProgress().get(i);
            //entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Quarter " + (i + 1)));
            String val = KBAMUtils.getRoundOffValue(progress.getAmount());
            float x = (float) Double.parseDouble(val);
            entries1.add(new PieEntry(x, progress.getInterval()));
        }

        PieDataSet ds1 = new PieDataSet(entries1, "");

        ds1.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        ds1.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        int[] rainbow = context.getResources().getIntArray(R.array.COLORFUL_COLORS);
        ds1.setColors(rainbow);
        //ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.BLACK);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);
        //d.setValueTypeface(tf);

        return d;
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
        return OSAgeingFragment.class.getSimpleName();
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
                    case Events.GET_COLLECTION_OS_AGEING_SUCCESSFULL:
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceCollectionDataResponse(eventObject.getObject().toString()));
                            model = (AgeingModel) BAMUtil.fromJson(String.valueOf(jsonObject), AgeingModel.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        mAdapter = new KCollectionAgeingAdapter(dashboardActivityContext, model.getTable());
                        rviData.setAdapter(mAdapter);
                        init();
                        //chart.setData(generatePieData());
                        //chart.notifyDataSetChanged();
                        chart.invalidate();
                        break;
                    case Events.GET_COLLECTION_OS_AGEING_UNSUCCESSFULL:
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
