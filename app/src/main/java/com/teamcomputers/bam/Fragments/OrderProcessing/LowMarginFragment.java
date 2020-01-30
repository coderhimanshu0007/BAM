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
import com.teamcomputers.bam.Adapters.OrderProcessing.LowMarginAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.LowMarginModel;
import com.teamcomputers.bam.Models.SPCSModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.OrderProcessing.OrderProcessingLowMarginRequester;
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

public class LowMarginFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.tviNoofSO)
    TextView tviNoofSO;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viPending)
    View viPending;
    @BindView(R.id.viPendingHrs)
    View viPendingHrs;

    @BindView(R.id.rviData)
    RecyclerView rviData;
    EventObject eventObjects;

    private LowMarginAdapter mAdapter;
    private ArrayList<LinkedTreeMap> lowMarginArrayList0 = new ArrayList<>();
    private ArrayList<LinkedTreeMap> lowMarginArrayList1 = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_low_margin, container, false);
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
        LowMarginModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getLMData();
        if (data != null) {
            tviNoofSO.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[0].getAmount())));
            mAdapter = new LowMarginAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OrderProcessingLowMarginRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return LowMarginFragment.class.getSimpleName();
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
                    case Events.GET_ORDERPROCESING_LOWMARGIN_SUCCESSFULL:
                        dismissProgress();
                        LowMarginModel[] data = new LowMarginModel[0];
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            data = (LowMarginModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), LowMarginModel[].class);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setLMData(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tviNoofSO.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
                        tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[0].getAmount())));
                        /*mAdapter = new SPCSubmissionAdapter(dashboardActivityContext, data[0].getTable());
                        rviData.setAdapter(mAdapter);
                        eventObjects = eventObject;
                        lowMarginArrayList0.clear();
                        lowMarginArrayList1.clear();
                        tviNoofSO.setText(BAMUtil.getStringInNoFormat((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Invoices")));
                        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Amount")));
                        lowMarginArrayList0 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(0)).get("Table");
                        lowMarginArrayList1 = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) ((ArrayList) eventObjects.getObject()).get(1)).get("Table");*/
                        mAdapter = new LowMarginAdapter(dashboardActivityContext, data[0].getTable());
                        rviData.setAdapter(mAdapter);
                        break;
                    case Events.GET_ORDERPROCESING_LOWMARGIN_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tviPending)
    public void Pending() {
        viPending.setVisibility(View.VISIBLE);
        viPendingHrs.setVisibility(View.INVISIBLE);
        LowMarginModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getLMData();
        tviNoofSO.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
        tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[0].getAmount())));
        mAdapter = new LowMarginAdapter(dashboardActivityContext, data[0].getTable());
        rviData.setAdapter(mAdapter);
        tviNoofSO.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPendingHrs)
    public void PendingHrs() {
        viPending.setVisibility(View.INVISIBLE);
        viPendingHrs.setVisibility(View.VISIBLE);
        LowMarginModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getLMData();
        tviNoofSO.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[1].getInvoices())));
        tviAmounts.setText(BAMUtil.getRoundOffValue(Double.valueOf(data[1].getAmount())));
        mAdapter = new LowMarginAdapter(dashboardActivityContext, data[1].getTable());
        rviData.setAdapter(mAdapter);
        tviNoofSO.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
