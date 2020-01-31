package com.teamcomputers.bam.Fragments.Logistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.Logistics.InTransitAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.InTransitModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Logistics.LogisticsInTransitRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InTransitFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private InTransitAdapter mAdapter;
    @BindView(R.id.tviNoofSO)
    TextView tviNoofInvoice;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viInTransit)
    View viInTransit;
    @BindView(R.id.viInterState)
    View viInterState;
    @BindView(R.id.viIntraState)
    View viIntraState;
    @BindView(R.id.viODA)
    View viODA;
    @BindView(R.id.viState)
    View viState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_intransit, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        InTransitModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getInTransitData();
        if (data != null) {
            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[0].getAmount()));
            mAdapter = new InTransitAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new LogisticsInTransitRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return InTransitFragment.class.getSimpleName();
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
                    case Events.GET_LOGISTICS_INTRANSIT_SUCCESSFULL:
                        dismissProgress();
                        InTransitModel[] model = new InTransitModel[0];
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            model = (InTransitModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), InTransitModel[].class);
                            if (model != null)
                                SharedPreferencesController.getInstance(dashboardActivityContext).setInTransitData(model);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (model != null) {
                            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(model[0].getInvoices())));
                            tviAmounts.setText(BAMUtil.getRoundOffValue((Double) model[0].getAmount()));
                            mAdapter = new InTransitAdapter(dashboardActivityContext, model[0].getTable());
                            rviData.setAdapter(mAdapter);
                        }
                        break;
                    case Events.GET_LOGISTICS_INTRANSIT_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tviOpenProject)
    public void InTransit() {
        viInTransit.setVisibility(View.VISIBLE);
        viInterState.setVisibility(View.INVISIBLE);
        viIntraState.setVisibility(View.INVISIBLE);
        viODA.setVisibility(View.INVISIBLE);
        viState.setVisibility(View.INVISIBLE);
        InTransitModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getInTransitData();
        if (data != null) {
            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[0].getAmount()));
            mAdapter = new InTransitAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviInterState)
    public void InterState() {
        viInTransit.setVisibility(View.INVISIBLE);
        viInterState.setVisibility(View.VISIBLE);
        viIntraState.setVisibility(View.INVISIBLE);
        viODA.setVisibility(View.INVISIBLE);
        viState.setVisibility(View.VISIBLE);
        InTransitModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getInTransitData();
        if (data != null) {
            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[1].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[1].getAmount()));
            mAdapter = new InTransitAdapter(dashboardActivityContext, data[1].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviIntraState)
    public void IntraState() {
        viInTransit.setVisibility(View.INVISIBLE);
        viInterState.setVisibility(View.INVISIBLE);
        viIntraState.setVisibility(View.VISIBLE);
        viODA.setVisibility(View.INVISIBLE);
        viState.setVisibility(View.VISIBLE);
        InTransitModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getInTransitData();
        if (data != null) {
            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[2].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[2].getAmount()));
            mAdapter = new InTransitAdapter(dashboardActivityContext, data[2].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviODA)
    public void ODA() {
        viInTransit.setVisibility(View.INVISIBLE);
        viInterState.setVisibility(View.INVISIBLE);
        viIntraState.setVisibility(View.INVISIBLE);
        viODA.setVisibility(View.VISIBLE);
        viState.setVisibility(View.VISIBLE);
        InTransitModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getInTransitData();
        if (data != null) {
            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[3].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[3].getAmount()));
            mAdapter = new InTransitAdapter(dashboardActivityContext, data[3].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
