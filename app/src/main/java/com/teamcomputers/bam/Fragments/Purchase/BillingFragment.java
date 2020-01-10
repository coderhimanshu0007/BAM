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
import com.teamcomputers.bam.Adapters.BillingAdapter;
import com.teamcomputers.bam.Adapters.SalesOrderAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.BillingModel;
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

public class BillingFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    RecyclerView mRecyclerViewCals;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.tviNoofSOs)
    TextView tviNoofSOs;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viPurchaseEBCW)
    View viPurchaseEBCW;
    @BindView(R.id.viPurchaseEBNW)
    View viPurchaseEBNW;
    @BindView(R.id.viPurchaseEBUCM)
    View viPurchaseEBUCM;
    @BindView(R.id.viPurchaseEBNM)
    View viPurchaseEBNM;
    @BindView(R.id.viPurchaseEBSM)
    View viPurchaseEBSM;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private BillingAdapter mAdapter;
    private ArrayList<BillingModel> billingModelArrayList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_billing, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        setData();

        mAdapter = new BillingAdapter(dashboardActivityContext, billingModelArrayList);
        rviData.setAdapter(mAdapter);
        return rootView;
    }

    private void setData() {
        for (int i = 0; i < 15; i++) {
            BillingModel billingModel = new BillingModel("Atul Shinde", "SOGST1920MH-11164", "THE BOSTON CONSULTING GROUP", "1", "Rs. 3.06", "11/19/2019", "Aftab Alam");
            billingModelArrayList.add(billingModel);
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
        return BillingFragment.class.getSimpleName();
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

    @OnClick(R.id.tviPurchaseEBCW)
    public void PurchaseEBCW() {
        viPurchaseEBCW.setVisibility(View.VISIBLE);
        viPurchaseEBNW.setVisibility(View.INVISIBLE);
        viPurchaseEBUCM.setVisibility(View.INVISIBLE);
        viPurchaseEBNM.setVisibility(View.INVISIBLE);
        viPurchaseEBSM.setVisibility(View.INVISIBLE);
        tviNoofSOs.setText("4358");
        tviAmounts.setText(getString(R.string.Rs) + " 13,261");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseEBNW)
    public void PurchaseEBNW() {
        viPurchaseEBCW.setVisibility(View.INVISIBLE);
        viPurchaseEBNW.setVisibility(View.VISIBLE);
        viPurchaseEBUCM.setVisibility(View.INVISIBLE);
        viPurchaseEBNM.setVisibility(View.INVISIBLE);
        viPurchaseEBSM.setVisibility(View.INVISIBLE);
        tviNoofSOs.setText("2142");
        tviAmounts.setText(getString(R.string.Rs) + " 5,850");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseEBUCM)
    public void PurchaseEBUCM() {
        viPurchaseEBCW.setVisibility(View.INVISIBLE);
        viPurchaseEBNW.setVisibility(View.INVISIBLE);
        viPurchaseEBUCM.setVisibility(View.VISIBLE);
        viPurchaseEBNM.setVisibility(View.INVISIBLE);
        viPurchaseEBSM.setVisibility(View.INVISIBLE);
        tviNoofSOs.setText("1532");
        tviAmounts.setText(getString(R.string.Rs) + " 5,288");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviPurchaseEBNM)
    public void PurchaseEBNM() {
        viPurchaseEBCW.setVisibility(View.INVISIBLE);
        viPurchaseEBNW.setVisibility(View.INVISIBLE);
        viPurchaseEBUCM.setVisibility(View.INVISIBLE);
        viPurchaseEBNM.setVisibility(View.VISIBLE);
        viPurchaseEBSM.setVisibility(View.INVISIBLE);
        tviNoofSOs.setText("291");
        tviAmounts.setText(getString(R.string.Rs) + " 673");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviPurchaseEBSM)
    public void PurchaseEBSM() {
        viPurchaseEBCW.setVisibility(View.INVISIBLE);
        viPurchaseEBNW.setVisibility(View.INVISIBLE);
        viPurchaseEBUCM.setVisibility(View.INVISIBLE);
        viPurchaseEBNM.setVisibility(View.INVISIBLE);
        viPurchaseEBSM.setVisibility(View.VISIBLE);
        tviNoofSOs.setText("245");
        tviAmounts.setText(getString(R.string.Rs) + " 479");
        tviNoofSOs.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
