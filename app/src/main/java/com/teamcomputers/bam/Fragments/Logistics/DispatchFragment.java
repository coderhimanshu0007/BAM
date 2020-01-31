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
import com.teamcomputers.bam.Adapters.Logistics.DispatchAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.DispatchModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Logistics.LogisticsDispatchRequester;
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

public class DispatchFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private DispatchAdapter mAdapter;
    @BindView(R.id.tviPending)
    TextView tviPending;
    @BindView(R.id.tviPendingHrs)
    TextView tviPendingHrs;

    @BindView(R.id.tviNoofSO)
    TextView tviNoofInvoice;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viPending)
    View viPending;
    @BindView(R.id.viPendingHrs)
    View viPendingHrs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dispatchs, container, false);
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
        DispatchModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getDispatchData();
        if (data != null) {
            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[0].getAmount()));
            mAdapter = new DispatchAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new LogisticsDispatchRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return DispatchFragment.class.getSimpleName();
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
                    case Events.GET_LOGISTICS_DISPATCH_SUCCESSFULL:
                        dismissProgress();
                        DispatchModel[] model = new DispatchModel[0];
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            model = (DispatchModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), DispatchModel[].class);
                            if (model != null)
                                SharedPreferencesController.getInstance(dashboardActivityContext).setDispatchData(model);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (model != null) {
                            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(model[0].getInvoices())));
                            tviAmounts.setText(BAMUtil.getRoundOffValue((Double) model[0].getAmount()));
                            mAdapter = new DispatchAdapter(dashboardActivityContext, model[0].getTable());
                            rviData.setAdapter(mAdapter);
                        }
                        break;
                    case Events.GET_LOGISTICS_DISPATCH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tviPending)
    public void pending() {
        viPending.setVisibility(View.VISIBLE);
        viPendingHrs.setVisibility(View.INVISIBLE);
        DispatchModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getDispatchData();
        if (data != null) {
            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[0].getAmount()));
            mAdapter = new DispatchAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPendingHrs)
    public void pendingHrs() {
        viPending.setVisibility(View.INVISIBLE);
        viPendingHrs.setVisibility(View.VISIBLE);
        DispatchModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getDispatchData();
        if (data != null) {
            tviNoofInvoice.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[1].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[1].getAmount()));
            mAdapter = new DispatchAdapter(dashboardActivityContext, data[1].getTable());
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
