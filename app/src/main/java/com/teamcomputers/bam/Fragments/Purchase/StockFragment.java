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
import com.teamcomputers.bam.Adapters.StockAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.SalesOrderModel;
import com.teamcomputers.bam.Models.StockModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StockFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    RecyclerView mRecyclerViewCals;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viPurchaseTotalStock)
    View viPurchaseTotalStock;
    @BindView(R.id.viPurchaseStock0to15Days)
    View viPurchaseStock0to15Days;
    @BindView(R.id.viPurchaseStock16to30Days)
    View viPurchaseStock16to30Days;
    @BindView(R.id.viPurchaseStock31to60Days)
    View viPurchaseStock31to60Days;
    @BindView(R.id.viPurchaseStock61to90Days)
    View viPurchaseStock61to90Days;
    @BindView(R.id.viPurchaseStockgreater90Days)
    View viPurchaseStockgreater90Days;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private StockAdapter mAdapter;
    private ArrayList<StockModel> stockModelArrayList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stock, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        tviAmounts.setText(getString(R.string.Rs) + " 516");

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        setData();

        mAdapter = new StockAdapter(dashboardActivityContext, stockModelArrayList);
        rviData.setAdapter(mAdapter);
        return rootView;
    }

    private void setData() {
        for (int i = 0; i < 15; i++) {
            StockModel stockModel = new StockModel("LIC OF INDIA", "IT007109", "Transfer - MR0121920-4900", "Atul Shinde", getString(R.string.Rs) + " 39", "29");
            stockModelArrayList.add(stockModel);
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
        return StockFragment.class.getSimpleName();
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

    @OnClick(R.id.tviPurchaseTotalStock)
    public void PurchaseTotalStock() {
        viPurchaseTotalStock.setVisibility(View.VISIBLE);
        viPurchaseStock0to15Days.setVisibility(View.INVISIBLE);
        viPurchaseStock16to30Days.setVisibility(View.INVISIBLE);
        viPurchaseStock31to60Days.setVisibility(View.INVISIBLE);
        viPurchaseStock61to90Days.setVisibility(View.INVISIBLE);
        viPurchaseStockgreater90Days.setVisibility(View.INVISIBLE);
        tviAmounts.setText(getString(R.string.Rs) + " 516");
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseStock0to15Days)
    public void PurchaseStock0to15Days() {
        viPurchaseTotalStock.setVisibility(View.INVISIBLE);
        viPurchaseStock0to15Days.setVisibility(View.VISIBLE);
        viPurchaseStock16to30Days.setVisibility(View.INVISIBLE);
        viPurchaseStock31to60Days.setVisibility(View.INVISIBLE);
        viPurchaseStock61to90Days.setVisibility(View.INVISIBLE);
        viPurchaseStockgreater90Days.setVisibility(View.INVISIBLE);
        tviAmounts.setText(getString(R.string.Rs) + " 318");
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseStock16to30Days)
    public void PurchaseStock16to30Days() {
        viPurchaseTotalStock.setVisibility(View.INVISIBLE);
        viPurchaseStock0to15Days.setVisibility(View.INVISIBLE);
        viPurchaseStock16to30Days.setVisibility(View.VISIBLE);
        viPurchaseStock31to60Days.setVisibility(View.INVISIBLE);
        viPurchaseStock61to90Days.setVisibility(View.INVISIBLE);
        viPurchaseStockgreater90Days.setVisibility(View.INVISIBLE);
        tviAmounts.setText(getString(R.string.Rs) + " 76");
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseStock31to60Days)
    public void PurchaseStock31to60Days() {
        viPurchaseTotalStock.setVisibility(View.INVISIBLE);
        viPurchaseStock0to15Days.setVisibility(View.INVISIBLE);
        viPurchaseStock16to30Days.setVisibility(View.INVISIBLE);
        viPurchaseStock31to60Days.setVisibility(View.VISIBLE);
        viPurchaseStock61to90Days.setVisibility(View.INVISIBLE);
        viPurchaseStockgreater90Days.setVisibility(View.INVISIBLE);
        tviAmounts.setText(getString(R.string.Rs) + " 61");
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseStock61to90Days)
    public void PurchaseStock61to90Days() {
        viPurchaseTotalStock.setVisibility(View.INVISIBLE);
        viPurchaseStock0to15Days.setVisibility(View.INVISIBLE);
        viPurchaseStock16to30Days.setVisibility(View.INVISIBLE);
        viPurchaseStock31to60Days.setVisibility(View.INVISIBLE);
        viPurchaseStock61to90Days.setVisibility(View.VISIBLE);
        viPurchaseStockgreater90Days.setVisibility(View.INVISIBLE);
        tviAmounts.setText(getString(R.string.Rs) + " 13");
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseStockgreater90Days)
    public void PurchaseStockgreater90Days() {
        viPurchaseTotalStock.setVisibility(View.INVISIBLE);
        viPurchaseStock0to15Days.setVisibility(View.INVISIBLE);
        viPurchaseStock16to30Days.setVisibility(View.INVISIBLE);
        viPurchaseStock31to60Days.setVisibility(View.INVISIBLE);
        viPurchaseStock61to90Days.setVisibility(View.INVISIBLE);
        viPurchaseStockgreater90Days.setVisibility(View.VISIBLE);
        tviAmounts.setText(getString(R.string.Rs) + " 43");
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
