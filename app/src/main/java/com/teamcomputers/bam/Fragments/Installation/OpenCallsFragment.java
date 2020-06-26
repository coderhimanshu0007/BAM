package com.teamcomputers.bam.Fragments.Installation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.Installation.KOpenCallsAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.OpenCallsModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Installation.KIOpenCallsRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OpenCallsFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private KOpenCallsAdapter mAdapter;

    @BindView(R.id.tviNoofInvoices)
    TextView tviNoofInvoices;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viTotalOpenCalls)
    View viTotalOpenCalls;
    @BindView(R.id.viCorpCall7Days)
    View viCorpCall7Days;
    @BindView(R.id.viGovtPsu10Days)
    View viGovtPsu10Days;
    @BindView(R.id.viUnassignedCalls)
    View viUnassignedCalls;
    @BindView(R.id.viUnassignedCalls10Days)
    View viUnassignedCalls10Days;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_open_calls, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        dashboardActivityContext.fragmentView = rootView;

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCallsModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOpenCallsData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[0].getAmount()));
            mAdapter = new KOpenCallsAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new InstallationOpenCallsRequester());
        BackgroundExecutor.getInstance().execute(new KIOpenCallsRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return OpenCallsFragment.class.getSimpleName();
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
                    case Events.GET_INSTALLATION_OPEN_CALLS_SUCCESSFULL:
                        dismissProgress();
                        OpenCallsModel[] model = new OpenCallsModel[0];
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            model = (OpenCallsModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), OpenCallsModel[].class);
                            if (model != null)
                                SharedPreferencesController.getInstance(dashboardActivityContext).setOpenCallsData(model);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (model != null) {
                            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(model[0].getInvoices())));
                            tviAmounts.setText(BAMUtil.getRoundOffValue((Double) model[0].getAmount()));
                            mAdapter = new KOpenCallsAdapter(dashboardActivityContext, model[0].getTable());
                            rviData.setAdapter(mAdapter);
                        }
                        break;
                    case Events.GET_INSTALLATION_OPEN_CALLS_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tviTotalOpenCalls)
    public void TotalOpenCalls() {
        viTotalOpenCalls.setVisibility(View.VISIBLE);
        viCorpCall7Days.setVisibility(View.INVISIBLE);
        viGovtPsu10Days.setVisibility(View.INVISIBLE);
        viUnassignedCalls.setVisibility(View.INVISIBLE);
        viUnassignedCalls10Days.setVisibility(View.INVISIBLE);
        OpenCallsModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOpenCallsData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[0].getAmount()));
            mAdapter = new KOpenCallsAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviCorpCall7Days)
    public void CorpCall7Days() {
        viTotalOpenCalls.setVisibility(View.INVISIBLE);
        viCorpCall7Days.setVisibility(View.VISIBLE);
        viGovtPsu10Days.setVisibility(View.INVISIBLE);
        viUnassignedCalls.setVisibility(View.INVISIBLE);
        viUnassignedCalls10Days.setVisibility(View.INVISIBLE);
        OpenCallsModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOpenCallsData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[1].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[1].getAmount()));
            mAdapter = new KOpenCallsAdapter(dashboardActivityContext, data[1].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviGovtPsu10Days)
    public void GovtPsu10Days() {
        viTotalOpenCalls.setVisibility(View.INVISIBLE);
        viCorpCall7Days.setVisibility(View.INVISIBLE);
        viGovtPsu10Days.setVisibility(View.VISIBLE);
        viUnassignedCalls.setVisibility(View.INVISIBLE);
        viUnassignedCalls10Days.setVisibility(View.INVISIBLE);
        OpenCallsModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOpenCallsData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[2].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[2].getAmount()));
            mAdapter = new KOpenCallsAdapter(dashboardActivityContext, data[2].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviUnassignedCalls)
    public void UnassignedCalls() {
        viTotalOpenCalls.setVisibility(View.INVISIBLE);
        viCorpCall7Days.setVisibility(View.INVISIBLE);
        viGovtPsu10Days.setVisibility(View.INVISIBLE);
        viUnassignedCalls.setVisibility(View.VISIBLE);
        viUnassignedCalls10Days.setVisibility(View.INVISIBLE);
        OpenCallsModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOpenCallsData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[3].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[3].getAmount()));
            mAdapter = new KOpenCallsAdapter(dashboardActivityContext, data[3].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviUnassignedCalls10Days)
    public void UnassignedCalls10Days() {
        viTotalOpenCalls.setVisibility(View.INVISIBLE);
        viCorpCall7Days.setVisibility(View.INVISIBLE);
        viGovtPsu10Days.setVisibility(View.INVISIBLE);
        viUnassignedCalls.setVisibility(View.INVISIBLE);
        viUnassignedCalls10Days.setVisibility(View.VISIBLE);
        OpenCallsModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOpenCallsData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[4].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[4].getAmount()));
            mAdapter = new KOpenCallsAdapter(dashboardActivityContext, data[4].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
