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
import com.teamcomputers.bam.Adapters.Installation.DOAIRAdapter;
import com.teamcomputers.bam.Adapters.Installation.HoldAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Installation.InstallationHoldRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showToast(ToastTexts.WORK_PROGRESS);
        //showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new InstallationHoldRequester());
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
                        eventObjects = eventObject;
                        holdArrayList0.clear();
                        holdArrayList1.clear();
                        holdArrayList2.clear();
                        holdArrayList3.clear();
                        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
                        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
                        holdArrayList0 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Table");
                        holdArrayList1 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Table");
                        holdArrayList2 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Table");
                        holdArrayList3 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Table");
                        mAdapter = new HoldAdapter(dashboardActivityContext, holdArrayList0);
                        rviData.setAdapter(mAdapter);
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
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
        mAdapter = new HoldAdapter(dashboardActivityContext, holdArrayList0);
        rviData.setAdapter(mAdapter);
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPaymentInProces)
    public void PaymentInProces() {
        viOnHold.setVisibility(View.INVISIBLE);
        viPaymentInProces.setVisibility(View.VISIBLE);
        viPaymentPending.setVisibility(View.INVISIBLE);
        viPaymentCollected.setVisibility(View.INVISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Amount")));
        mAdapter = new HoldAdapter(dashboardActivityContext, holdArrayList1);
        rviData.setAdapter(mAdapter);
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviPaymentPending)
    public void PaymentPending() {
        viOnHold.setVisibility(View.INVISIBLE);
        viPaymentInProces.setVisibility(View.INVISIBLE);
        viPaymentPending.setVisibility(View.VISIBLE);
        viPaymentCollected.setVisibility(View.INVISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Amount")));
        mAdapter = new HoldAdapter(dashboardActivityContext, holdArrayList2);
        rviData.setAdapter(mAdapter);
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviPaymentCollected)
    public void PaymentCollected() {
        viOnHold.setVisibility(View.INVISIBLE);
        viPaymentInProces.setVisibility(View.INVISIBLE);
        viPaymentPending.setVisibility(View.INVISIBLE);
        viPaymentCollected.setVisibility(View.VISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Amount")));
        mAdapter = new HoldAdapter(dashboardActivityContext, holdArrayList3);
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
