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
import com.teamcomputers.bam.Adapters.Installation.OpenCallsAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Installation.InstallationOpenCallsRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

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

    private OpenCallsAdapter mAdapter;
    private ArrayList<LinkedTreeMap> openCallsArrayList0 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> openCallsArrayList1 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> openCallsArrayList2 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> openCallsArrayList3 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> openCallsArrayList4 = new ArrayList<>();

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

    EventObject eventObjects;

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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new InstallationOpenCallsRequester());
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
                        eventObjects = eventObject;
                        openCallsArrayList0.clear();
                        openCallsArrayList1.clear();
                        openCallsArrayList2.clear();
                        openCallsArrayList3.clear();
                        openCallsArrayList4.clear();
                        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
                        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
                        openCallsArrayList0 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Table");
                        openCallsArrayList1 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Table");
                        openCallsArrayList2 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Table");
                        openCallsArrayList3 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Table");
                        openCallsArrayList4 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(4)).get("Table");
                        mAdapter = new OpenCallsAdapter(dashboardActivityContext, openCallsArrayList0);
                        rviData.setAdapter(mAdapter);
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
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
        mAdapter = new OpenCallsAdapter(dashboardActivityContext, openCallsArrayList0);
        rviData.setAdapter(mAdapter);
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
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Amount")));
        mAdapter = new OpenCallsAdapter(dashboardActivityContext, openCallsArrayList1);
        rviData.setAdapter(mAdapter);
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
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Amount")));
        mAdapter = new OpenCallsAdapter(dashboardActivityContext, openCallsArrayList2);
        rviData.setAdapter(mAdapter);
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
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Amount")));
        mAdapter = new OpenCallsAdapter(dashboardActivityContext, openCallsArrayList3);
        rviData.setAdapter(mAdapter);
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
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(4)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(4)).get("Amount")));
        mAdapter = new OpenCallsAdapter(dashboardActivityContext, openCallsArrayList4);
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
