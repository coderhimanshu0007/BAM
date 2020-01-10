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
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Logistics.LogisticsDispatchRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private ArrayList<LinkedTreeMap> dispatchTableModelArrayList0 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> dispatchTableModelArrayList1 = new ArrayList<>();
    EventObject eventObjects;
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
                        eventObjects = eventObject;
                        dispatchTableModelArrayList0.clear();
                        dispatchTableModelArrayList1.clear();
                        tviNoofInvoice.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
                        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
                        dispatchTableModelArrayList0 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Table");
                        dispatchTableModelArrayList1 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Table");
                        mAdapter = new DispatchAdapter(dashboardActivityContext, dispatchTableModelArrayList0);
                        rviData.setAdapter(mAdapter);
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
        tviNoofInvoice.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
        mAdapter = new DispatchAdapter(dashboardActivityContext, dispatchTableModelArrayList0);
        rviData.setAdapter(mAdapter);
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPendingHrs)
    public void pendingHrs() {
        viPending.setVisibility(View.INVISIBLE);
        viPendingHrs.setVisibility(View.VISIBLE);
        tviNoofInvoice.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Amount")));
        mAdapter = new DispatchAdapter(dashboardActivityContext, dispatchTableModelArrayList1);
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
