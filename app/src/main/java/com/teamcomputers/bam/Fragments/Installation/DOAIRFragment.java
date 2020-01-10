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
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Installation.InstallationDOAIRRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DOAIRFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private DOAIRAdapter mAdapter;
    private ArrayList<LinkedTreeMap> doairArrayList0 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> doairArrayList1 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> doairArrayList2 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> doairArrayList3 = new ArrayList<>();

    @BindView(R.id.tviNoofInvoices)
    TextView tviNoofInvoices;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viDOA)
    View viDOA;
    @BindView(R.id.viDOAApproved)
    View viDOAApproved;
    @BindView(R.id.viDOARejected)
    View viDOARejected;
    @BindView(R.id.viIRAwaited)
    View viIRAwaited;

    EventObject eventObjects;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dao_ir, container, false);
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
        BackgroundExecutor.getInstance().execute(new InstallationDOAIRRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return DOAIRFragment.class.getSimpleName();
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
                    case Events.GET_INSTALLATION_DOA_IR_SUCCESSFULL:
                        dismissProgress();
                        eventObjects = eventObject;
                        doairArrayList0.clear();
                        doairArrayList1.clear();
                        doairArrayList2.clear();
                        doairArrayList3.clear();
                        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
                        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
                        doairArrayList0 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Table");
                        doairArrayList1 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Table");
                        doairArrayList2 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Table");
                        doairArrayList3 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Table");
                        mAdapter = new DOAIRAdapter(dashboardActivityContext, doairArrayList0);
                        rviData.setAdapter(mAdapter);
                        break;
                    case Events.GET_INSTALLATION_DOA_IR_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tviDOA)
    public void DOA() {
        viDOA.setVisibility(View.VISIBLE);
        viDOAApproved.setVisibility(View.INVISIBLE);
        viDOARejected.setVisibility(View.INVISIBLE);
        viIRAwaited.setVisibility(View.INVISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
        mAdapter = new DOAIRAdapter(dashboardActivityContext, doairArrayList0);
        rviData.setAdapter(mAdapter);
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviDOAApproved)
    public void DOAApproved() {
        viDOA.setVisibility(View.INVISIBLE);
        viDOAApproved.setVisibility(View.VISIBLE);
        viDOARejected.setVisibility(View.INVISIBLE);
        viIRAwaited.setVisibility(View.INVISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Amount")));
        mAdapter = new DOAIRAdapter(dashboardActivityContext, doairArrayList1);
        rviData.setAdapter(mAdapter);
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviDOARejected)
    public void DOARejected() {
        viDOA.setVisibility(View.INVISIBLE);
        viDOAApproved.setVisibility(View.INVISIBLE);
        viDOARejected.setVisibility(View.VISIBLE);
        viIRAwaited.setVisibility(View.INVISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Amount")));
        mAdapter = new DOAIRAdapter(dashboardActivityContext, doairArrayList2);
        rviData.setAdapter(mAdapter);
        tviNoofInvoices.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviIRAwaited)
    public void IRAwaited() {
        viDOA.setVisibility(View.INVISIBLE);
        viDOAApproved.setVisibility(View.INVISIBLE);
        viDOARejected.setVisibility(View.INVISIBLE);
        viIRAwaited.setVisibility(View.VISIBLE);
        tviNoofInvoices.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Amount")));
        mAdapter = new DOAIRAdapter(dashboardActivityContext, doairArrayList3);
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
