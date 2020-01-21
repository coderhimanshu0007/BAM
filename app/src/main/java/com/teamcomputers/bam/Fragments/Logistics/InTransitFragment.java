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
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Logistics.LogisticsInTransitRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private ArrayList<LinkedTreeMap> intransitArrayList0 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> intransitArrayList1 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> intransitArrayList2 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> intransitArrayList3 = new ArrayList<>();
    EventObject eventObjects;
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

        //setData();

        //mAdapter = new DispatchAdapter(dashboardActivityContext, dispatchModelArrayList);
        //rviData.setAdapter(mAdapter);

        return rootView;
    }

    /*private void setData() {
        for (int i = 0; i < 15; i++) {
            DispatchModel dispatchModel = new DispatchModel("LIC of India", "New Delhi", "Samba", "20 Hrs", "- - - -", "2 Lakhs", "GST1920");
            dispatchModelArrayList.add(dispatchModel);
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
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
                        eventObjects = eventObject;
                        intransitArrayList0.clear();
                        intransitArrayList1.clear();
                        intransitArrayList2.clear();
                        intransitArrayList3.clear();
                        tviNoofInvoice.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
                        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
                        intransitArrayList0 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Table");
                        intransitArrayList1 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Table");
                        intransitArrayList2 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Table");
                        intransitArrayList3 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Table");
                        mAdapter = new InTransitAdapter(dashboardActivityContext, intransitArrayList0);
                        rviData.setAdapter(mAdapter);
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
        tviNoofInvoice.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
        mAdapter = new InTransitAdapter(dashboardActivityContext, intransitArrayList0);
        rviData.setAdapter(mAdapter);
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
        tviNoofInvoice.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Amount")));
        mAdapter = new InTransitAdapter(dashboardActivityContext, intransitArrayList1);
        rviData.setAdapter(mAdapter);
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
        tviNoofInvoice.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Amount")));
        mAdapter = new InTransitAdapter(dashboardActivityContext, intransitArrayList1);
        rviData.setAdapter(mAdapter);
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
        tviNoofInvoice.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(4)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(4)).get("Amount")));
        mAdapter = new InTransitAdapter(dashboardActivityContext, intransitArrayList1);
        rviData.setAdapter(mAdapter);
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
