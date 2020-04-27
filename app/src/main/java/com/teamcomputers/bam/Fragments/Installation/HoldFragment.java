package com.teamcomputers.bam.Fragments.Installation;

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
import com.teamcomputers.bam.Adapters.Installation.HoldAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.HoldModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Installation.InstallationHoldRequester;
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

public class HoldFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private HoldAdapter mAdapter;
    private ArrayList<LinkedTreeMap> holdArrayList0 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> holdArrayList1 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> holdArrayList2 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> holdArrayList3 = new ArrayList<>();

    @BindView(R.id.tviNoofInvoices)
    TextView tviNoofInvoices;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viOnHold)
    View viOnHold;
    @BindView(R.id.viPaymentInProces)
    View viPaymentInProces;
    @BindView(R.id.viPaymentPending)
    View viPaymentPending;
    @BindView(R.id.viPaymentCollected)
    View viPaymentCollected;
    EventObject eventObjects;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hold, container, false);
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
        HoldModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getHoldData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[0].getAmount()));
            mAdapter = new HoldAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new InstallationHoldRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return HoldFragment.class.getSimpleName();
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
                    case Events.GET_INSTALLATION_HOLD_SUCCESSFULL:
                        dismissProgress();
                        HoldModel[] data = new HoldModel[0];
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            data = (HoldModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), HoldModel[].class);
                            if (data != null)
                                SharedPreferencesController.getInstance(dashboardActivityContext).setHoldData(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (data != null) {
                            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
                            tviAmounts.setText(BAMUtil.getRoundOffValue(data[0].getAmount()));
                            mAdapter = new HoldAdapter(dashboardActivityContext, data[0].getTable());
                            rviData.setAdapter(mAdapter);
                        }
                        break;
                    case Events.GET_INSTALLATION_HOLD_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tviOnHold)
    public void OnHold() {
        viOnHold.setVisibility(View.VISIBLE);
        viPaymentInProces.setVisibility(View.INVISIBLE);
        viPaymentPending.setVisibility(View.INVISIBLE);
        viPaymentCollected.setVisibility(View.INVISIBLE);
        HoldModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getHoldData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[0].getAmount()));
            mAdapter = new HoldAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPaymentInProces)
    public void PaymentInProces() {
        viOnHold.setVisibility(View.INVISIBLE);
        viPaymentInProces.setVisibility(View.VISIBLE);
        viPaymentPending.setVisibility(View.INVISIBLE);
        viPaymentCollected.setVisibility(View.INVISIBLE);
        HoldModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getHoldData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[1].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[1].getAmount()));
            mAdapter = new HoldAdapter(dashboardActivityContext, data[1].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviPaymentPending)
    public void PaymentPending() {
        viOnHold.setVisibility(View.INVISIBLE);
        viPaymentInProces.setVisibility(View.INVISIBLE);
        viPaymentPending.setVisibility(View.VISIBLE);
        viPaymentCollected.setVisibility(View.INVISIBLE);
        HoldModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getHoldData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[2].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[2].getAmount()));
            mAdapter = new HoldAdapter(dashboardActivityContext, data[2].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviPaymentCollected)
    public void PaymentCollected() {
        viOnHold.setVisibility(View.INVISIBLE);
        viPaymentInProces.setVisibility(View.INVISIBLE);
        viPaymentPending.setVisibility(View.INVISIBLE);
        viPaymentCollected.setVisibility(View.VISIBLE);
        HoldModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getHoldData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[3].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(data[3].getAmount()));
            mAdapter = new HoldAdapter(dashboardActivityContext, data[3].getTable());
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
