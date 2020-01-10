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
import com.teamcomputers.bam.Adapters.Installation.WIPAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Installation.InstallationWIPRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

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
    private ArrayList<LinkedTreeMap> wipArrayList0 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> wipArrayList1 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> wipArrayList2 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> wipArrayList3 = new ArrayList<>();

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
    EventObject eventObjects;

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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showToast(ToastTexts.WORK_PROGRESS);
        //showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new InstallationWIPRequester());
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
                        eventObjects = eventObject;
                        wipArrayList0.clear();
                        wipArrayList1.clear();
                        wipArrayList2.clear();
                        wipArrayList3.clear();
                        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
                        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
                        wipArrayList0 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Table");
                        wipArrayList1 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Table");
                        wipArrayList2 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Table");
                        wipArrayList3 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Table");
                        mAdapter = new WIPAdapter(dashboardActivityContext, wipArrayList0);
                        rviData.setAdapter(mAdapter);
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
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
        mAdapter = new WIPAdapter(dashboardActivityContext, wipArrayList0);
        rviData.setAdapter(mAdapter);
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviWIPUpto5Days)
    public void WIPUpto5Days() {
        viTotalWIP.setVisibility(View.INVISIBLE);
        viWIPUpto5Days.setVisibility(View.VISIBLE);
        viWIPUpto10Days.setVisibility(View.INVISIBLE);
        viWIPMoreThanDays.setVisibility(View.INVISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Amount")));
        mAdapter = new WIPAdapter(dashboardActivityContext, wipArrayList1);
        rviData.setAdapter(mAdapter);
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviWIPUpto10Days)
    public void WIPUpto10Days() {
        viTotalWIP.setVisibility(View.INVISIBLE);
        viWIPUpto5Days.setVisibility(View.INVISIBLE);
        viWIPUpto10Days.setVisibility(View.VISIBLE);
        viWIPMoreThanDays.setVisibility(View.INVISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Amount")));
        mAdapter = new WIPAdapter(dashboardActivityContext, wipArrayList2);
        rviData.setAdapter(mAdapter);
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviWIPMoreThanDays)
    public void WIPMoreThanDays() {
        viTotalWIP.setVisibility(View.INVISIBLE);
        viWIPUpto5Days.setVisibility(View.INVISIBLE);
        viWIPUpto10Days.setVisibility(View.INVISIBLE);
        viWIPMoreThanDays.setVisibility(View.VISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Amount")));
        mAdapter = new WIPAdapter(dashboardActivityContext, wipArrayList3);
        rviData.setAdapter(mAdapter);
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
