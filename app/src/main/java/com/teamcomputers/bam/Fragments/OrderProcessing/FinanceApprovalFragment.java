package com.teamcomputers.bam.Fragments.OrderProcessing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.OrderProcessing.FAAdapter;
import com.teamcomputers.bam.Adapters.OrderProcessing.KFAAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.FAModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.OrderProcessing.KOPFinanceApprovalRequester;
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

public class FinanceApprovalFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.tviNoofProjects)
    TextView tviNoofProjects;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viPending)
    View viPending;
    @BindView(R.id.viPendingHrs)
    View viPendingHrs;
    @BindView(R.id.viAAToday)
    View viAAToday;
    @BindView(R.id.viRejected)
    View viRejected;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private FAAdapter mAdapter;
    //private KFAAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_finance_approval, container, false);
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
        FAModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOPFAData();
        if (data != null) {
            tviNoofProjects.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
            tviAmounts.setText(BAMUtil.getRoundOffValue((Double) data[0].getAmount()));
            mAdapter = new FAAdapter(dashboardActivityContext, data[0].getTable());
            //mAdapter = new KFAAdapter(dashboardActivityContext, data[0].getTable());
            rviData.setAdapter(mAdapter);
        }
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OrderProcessingFinanceApprovalRequester());
        BackgroundExecutor.getInstance().execute(new KOPFinanceApprovalRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return FinanceApprovalFragment.class.getSimpleName();
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
                    case Events.GET_ORDERPROCESING_FINANCE_APPROVAL_SUCCESSFULL:
                        dismissProgress();
                        FAModel[] model = new FAModel[0];
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            model = (FAModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FAModel[].class);
                            if (model != null)
                                SharedPreferencesController.getInstance(dashboardActivityContext).setOPFAData(model);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (model != null) {
                            tviNoofProjects.setText(BAMUtil.getStringInNoFormat(Double.valueOf(model[0].getInvoices())));
                            tviAmounts.setText(BAMUtil.getRoundOffValue((Double) model[0].getAmount()));
                            mAdapter = new FAAdapter(dashboardActivityContext, model[0].getTable());
                            //mAdapter = new KFAAdapter(dashboardActivityContext, model[0].getTable());
                            rviData.setAdapter(mAdapter);
                        }
                        break;
                    case Events.GET_ORDERPROCESING_FINANCE_APPROVAL_UNSUCCESSFULL:
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
        viAAToday.setVisibility(View.INVISIBLE);
        viRejected.setVisibility(View.INVISIBLE);
        FAModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOPFAData();
        tviNoofProjects.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[0].getInvoices())));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) data[0].getAmount()));
        mAdapter = new FAAdapter(dashboardActivityContext, data[0].getTable());
        //mAdapter = new KFAAdapter(dashboardActivityContext, data[0].getTable());
        rviData.setAdapter(mAdapter);
        tviNoofProjects.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPendingHrs)
    public void PendingHrs() {
        viPending.setVisibility(View.INVISIBLE);
        viPendingHrs.setVisibility(View.VISIBLE);
        viAAToday.setVisibility(View.INVISIBLE);
        viRejected.setVisibility(View.INVISIBLE);
        FAModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOPFAData();
        tviNoofProjects.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[1].getInvoices())));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) data[1].getAmount()));
        mAdapter = new FAAdapter(dashboardActivityContext, data[1].getTable());
        //mAdapter = new KFAAdapter(dashboardActivityContext, data[1].getTable());
        rviData.setAdapter(mAdapter);
        tviNoofProjects.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviAAToday)
    public void IntraState() {
        viPending.setVisibility(View.INVISIBLE);
        viPendingHrs.setVisibility(View.INVISIBLE);
        viAAToday.setVisibility(View.VISIBLE);
        viRejected.setVisibility(View.INVISIBLE);
        FAModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOPFAData();
        tviNoofProjects.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[2].getInvoices())));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) data[2].getAmount()));
        mAdapter = new FAAdapter(dashboardActivityContext, data[2].getTable());
        //mAdapter = new KFAAdapter(dashboardActivityContext, data[2].getTable());
        rviData.setAdapter(mAdapter);
        tviNoofProjects.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviRejected)
    public void Rejected() {
        viPending.setVisibility(View.INVISIBLE);
        viPendingHrs.setVisibility(View.INVISIBLE);
        viAAToday.setVisibility(View.INVISIBLE);
        viRejected.setVisibility(View.VISIBLE);
        FAModel[] data = SharedPreferencesController.getInstance(dashboardActivityContext).getOPFAData();
        tviNoofProjects.setText(BAMUtil.getStringInNoFormat(Double.valueOf(data[3].getInvoices())));
        tviAmounts.setText(BAMUtil.getRoundOffValue((Double) data[3].getAmount()));
        mAdapter = new FAAdapter(dashboardActivityContext, data[3].getTable());
        //mAdapter = new KFAAdapter(dashboardActivityContext, data[3].getTable());
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
