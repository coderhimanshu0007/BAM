package com.teamcomputers.bam.Fragments.OrderProcessing;

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
import com.teamcomputers.bam.Adapters.OrderProcessing.SPCSubmissionAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.OrderProcessing.OrderProcessingSPCSubmissionRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SPCSubmissionFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    RecyclerView mRecyclerViewCals;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.tviNoofProjects)
    TextView tviNoofProjects;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viOpenProjects)
    View viOpenProjects;
    @BindView(R.id.viPending)
    View viPending;
    @BindView(R.id.viPendingSPC)
    View viPendingSPC;
    @BindView(R.id.viSPCRejected)
    View viSPCRejected;

    @BindView(R.id.rviData)
    RecyclerView rviData;
    EventObject eventObjects;
    private SPCSubmissionAdapter mAdapter;
    private ArrayList<LinkedTreeMap> spcSubmissionArrayList0 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> spcSubmissionArrayList1 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> spcSubmissionArrayList2 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> spcSubmissionArrayList3 = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_spc_submission, container, false);
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
        BackgroundExecutor.getInstance().execute(new OrderProcessingSPCSubmissionRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return SPCSubmissionFragment.class.getSimpleName();
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
                    case Events.GET_ORDERPROCESING_SPCSUBMISSION_SUCCESSFULL:
                        dismissProgress();
                        eventObjects = eventObject;
                        spcSubmissionArrayList0.clear();
                        spcSubmissionArrayList1.clear();
                        spcSubmissionArrayList2.clear();
                        spcSubmissionArrayList3.clear();
                        tviNoofProjects.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
                        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
                        spcSubmissionArrayList0 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Table");
                        spcSubmissionArrayList1 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Table");
                        spcSubmissionArrayList2 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Table");
                        spcSubmissionArrayList3 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Table");
                        mAdapter = new SPCSubmissionAdapter(dashboardActivityContext, spcSubmissionArrayList0);
                        rviData.setAdapter(mAdapter);
                        break;
                    case Events.GET_ORDERPROCESING_SPCSUBMISSION_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tviOpenProjects)
    public void OpenProjects() {
        viOpenProjects.setVisibility(View.VISIBLE);
        viPending.setVisibility(View.INVISIBLE);
        viPendingSPC.setVisibility(View.INVISIBLE);
        viSPCRejected.setVisibility(View.INVISIBLE);
        tviNoofProjects.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
        mAdapter = new SPCSubmissionAdapter(dashboardActivityContext, spcSubmissionArrayList0);
        rviData.setAdapter(mAdapter);
        tviNoofProjects.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPending)
    public void Pending() {
        viOpenProjects.setVisibility(View.INVISIBLE);
        viPending.setVisibility(View.VISIBLE);
        viPendingSPC.setVisibility(View.INVISIBLE);
        viSPCRejected.setVisibility(View.INVISIBLE);
        tviNoofProjects.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Amount")));
        mAdapter = new SPCSubmissionAdapter(dashboardActivityContext, spcSubmissionArrayList1);
        rviData.setAdapter(mAdapter);
        tviNoofProjects.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPendingSPC)
    public void PendingSPC() {
        viOpenProjects.setVisibility(View.INVISIBLE);
        viPending.setVisibility(View.INVISIBLE);
        viPendingSPC.setVisibility(View.VISIBLE);
        viSPCRejected.setVisibility(View.INVISIBLE);
        tviNoofProjects.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(2)).get("Amount")));
        mAdapter = new SPCSubmissionAdapter(dashboardActivityContext, spcSubmissionArrayList2);
        rviData.setAdapter(mAdapter);
        tviNoofProjects.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviSPCRejected)
    public void SPCRejected() {
        viOpenProjects.setVisibility(View.INVISIBLE);
        viPending.setVisibility(View.INVISIBLE);
        viPendingSPC.setVisibility(View.INVISIBLE);
        viSPCRejected.setVisibility(View.VISIBLE);
        tviNoofProjects.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Invoices")));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(3)).get("Amount")));
        mAdapter = new SPCSubmissionAdapter(dashboardActivityContext, spcSubmissionArrayList3);
        rviData.setAdapter(mAdapter);
        tviNoofProjects.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
