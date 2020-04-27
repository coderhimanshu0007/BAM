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
import com.teamcomputers.bam.Adapters.Installation.WIPAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.WIPModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Installation.InstallationWIPRequester;
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

public class WIPFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private WIPAdapter mAdapter;

    @BindView(R.id.tviNoofInvoices)
    TextView tviNoofInvoices;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viTotalWIP)
    View viTotalWIP;
    @BindView(R.id.viWIPUpto5Days)
    View viWIPUpto5Days;
    @BindView(R.id.viWIPUpto10Days)
    View viWIPUpto10Days;
    @BindView(R.id.viWIPMoreThanDays)
    View viWIPMoreThanDays;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wip, container, false);
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
        WIPModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getWIPData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[0].getAmount())));
            mAdapter = new WIPAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new InstallationWIPRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return WIPFragment.class.getSimpleName();
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
                    case Events.GET_INSTALLATION_WIP_SUCCESSFULL:
                        dismissProgress();
                        WIPModel[] data = new WIPModel[0];
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            data = (WIPModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), WIPModel[].class);
                            if (data != null)
                                SharedPreferencesController.getInstance(dashboardActivityContext).setWIPData(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (data != null) {
                            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
                            tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[0].getAmount())));
                            mAdapter = new WIPAdapter(dashboardActivityContext, data[0].getTable());
                            rviData.setAdapter(mAdapter);
                        }
                        break;
                    case Events.GET_INSTALLATION_WIP_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tviTotalWIP)
    public void TotalWIP() {
        viTotalWIP.setVisibility(View.VISIBLE);
        viWIPUpto5Days.setVisibility(View.INVISIBLE);
        viWIPUpto10Days.setVisibility(View.INVISIBLE);
        viWIPMoreThanDays.setVisibility(View.INVISIBLE);
        WIPModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getWIPData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[0].getAmount())));
            mAdapter = new WIPAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviWIPUpto5Days)
    public void WIPUpto5Days() {
        viTotalWIP.setVisibility(View.INVISIBLE);
        viWIPUpto5Days.setVisibility(View.VISIBLE);
        viWIPUpto10Days.setVisibility(View.INVISIBLE);
        viWIPMoreThanDays.setVisibility(View.INVISIBLE);
        WIPModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getWIPData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[1].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[1].getAmount())));
            mAdapter = new WIPAdapter(dashboardActivityContext, data[1].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviWIPUpto10Days)
    public void WIPUpto10Days() {
        viTotalWIP.setVisibility(View.INVISIBLE);
        viWIPUpto5Days.setVisibility(View.INVISIBLE);
        viWIPUpto10Days.setVisibility(View.VISIBLE);
        viWIPMoreThanDays.setVisibility(View.INVISIBLE);
        WIPModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getWIPData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[2].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[2].getAmount())));
            mAdapter = new WIPAdapter(dashboardActivityContext, data[2].getTable());
            rviData.setAdapter(mAdapter);
        }
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviWIPMoreThanDays)
    public void WIPMoreThanDays() {
        viTotalWIP.setVisibility(View.INVISIBLE);
        viWIPUpto5Days.setVisibility(View.INVISIBLE);
        viWIPUpto10Days.setVisibility(View.INVISIBLE);
        viWIPMoreThanDays.setVisibility(View.VISIBLE);
        WIPModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getWIPData();
        if (data != null) {
            tviNoofInvoices.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[3].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[3].getAmount())));
            mAdapter = new WIPAdapter(dashboardActivityContext, data[3].getTable());
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
