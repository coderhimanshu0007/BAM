package com.teamcomputers.bam.Fragments.Purchase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.SalesOrderAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.SalesOrderModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SalesOrderFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    RecyclerView mRecyclerViewCals;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.tviNoofSOs)
    TextView tviNoofSOs;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viPurchaseTotalSO)
    View viPurchaseTotalSO;
    @BindView(R.id.viPurchaseServiceSO)
    View viPurchaseServiceSO;
    @BindView(R.id.viPurchaseLinkedSO)
    View viPurchaseLinkedSO;
    @BindView(R.id.viPurchaseCompletedSO)
    View viPurchaseCompletedSO;
    @BindView(R.id.viPurchaseIncompletedSO)
    View viPurchaseIncompletedSO;
    @BindView(R.id.viPurchaseNotDueSalesSO)
    View viPurchaseNotDueSalesSO;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private SalesOrderAdapter mAdapter;
    private ArrayList<SalesOrderModel> salesOrderModelArrayList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sales_order, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        setData();

        mAdapter = new SalesOrderAdapter(dashboardActivityContext, salesOrderModelArrayList);
        rviData.setAdapter(mAdapter);
        return rootView;
    }

    private void setData() {
        for (int i = 0; i < 15; i++) {
            SalesOrderModel salesOrderModel = new SalesOrderModel("Atul Shinde", "SOGST1920MH-11164", "STATE BANK OF INDIA", "1", "Rs. 3.06", "39", "Mayank Mathawadia", "Bid Delays","--");
            salesOrderModelArrayList.add(salesOrderModel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return SalesOrderFragment.class.getSimpleName();
    }

    @Subscribe
    public void onEvent(final EventObject eventObject) {
        dashboardActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case Events.NO_INTERNET_CONNECTION:
                        dismissProgress();
                        //showToast(ToastTexts.LOGIN_SUCCESSFULL);
                        //((DashbordActivity) getActivity()).replaceFragment(Fragments.ASSIGN_CALLS_MAP_FRAGMENTS, assignedCallsBundle);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tviPurchaseTotalSO)
    public void TotalSO() {
        viPurchaseTotalSO.setVisibility(View.VISIBLE);
        viPurchaseServiceSO.setVisibility(View.INVISIBLE);
        viPurchaseLinkedSO.setVisibility(View.INVISIBLE);
        viPurchaseCompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseIncompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseNotDueSalesSO.setVisibility(View.INVISIBLE);
        tviNoofSOs.setText("4358");
        tviAmounts.setText("Rs. 13,261");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseServiceSO)
    public void ServiceSO() {
        viPurchaseTotalSO.setVisibility(View.INVISIBLE);
        viPurchaseServiceSO.setVisibility(View.VISIBLE);
        viPurchaseLinkedSO.setVisibility(View.INVISIBLE);
        viPurchaseCompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseIncompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseNotDueSalesSO.setVisibility(View.INVISIBLE);
        tviNoofSOs.setText("2142");
        tviAmounts.setText("Rs. 5,850");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseLinkedSO)
    public void LinkedSO() {
        viPurchaseTotalSO.setVisibility(View.INVISIBLE);
        viPurchaseServiceSO.setVisibility(View.INVISIBLE);
        viPurchaseLinkedSO.setVisibility(View.VISIBLE);
        viPurchaseCompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseIncompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseNotDueSalesSO.setVisibility(View.INVISIBLE);
        tviNoofSOs.setText("1532");
        tviAmounts.setText("Rs. 5,288");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseCompletedSO)
    public void CompletedSO() {
        viPurchaseTotalSO.setVisibility(View.INVISIBLE);
        viPurchaseServiceSO.setVisibility(View.INVISIBLE);
        viPurchaseLinkedSO.setVisibility(View.INVISIBLE);
        viPurchaseCompletedSO.setVisibility(View.VISIBLE);
        viPurchaseIncompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseNotDueSalesSO.setVisibility(View.INVISIBLE);
        tviNoofSOs.setText("291");
        tviAmounts.setText("Rs. 673");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviPurchaseIncompletedSO)
    public void IncompletedSO() {
        viPurchaseTotalSO.setVisibility(View.INVISIBLE);
        viPurchaseServiceSO.setVisibility(View.INVISIBLE);
        viPurchaseLinkedSO.setVisibility(View.INVISIBLE);
        viPurchaseCompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseIncompletedSO.setVisibility(View.VISIBLE);
        viPurchaseNotDueSalesSO.setVisibility(View.INVISIBLE);
        tviNoofSOs.setText("245");
        tviAmounts.setText("Rs. 479");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviPurchaseNotDueSalesSO)
    public void NotDueSalesSO() {
        viPurchaseTotalSO.setVisibility(View.INVISIBLE);
        viPurchaseServiceSO.setVisibility(View.INVISIBLE);
        viPurchaseLinkedSO.setVisibility(View.INVISIBLE);
        viPurchaseCompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseIncompletedSO.setVisibility(View.INVISIBLE);
        viPurchaseNotDueSalesSO.setVisibility(View.VISIBLE);
        tviNoofSOs.setText("213");
        tviAmounts.setText("Rs. 779");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
