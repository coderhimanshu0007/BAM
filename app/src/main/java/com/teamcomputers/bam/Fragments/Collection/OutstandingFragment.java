package com.teamcomputers.bam.Fragments.Collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.OutstandingAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.OutstandingModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OutstandingFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    RecyclerView mRecyclerViewCals;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private OutstandingAdapter mAdapter;
    private ArrayList<OutstandingModel> outstandingModelArrayList = new ArrayList<>();

    @BindView(R.id.tviNoofInvoice)
    TextView tviNoofInvoice;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viTotalOutstanding)
    View viTotalOutstanding;
    @BindView(R.id.viCollectableOutstanding)
    View viCollectableOutstanding;
    @BindView(R.id.viCollectableOutstandingCurrentMonth)
    View viCollectableOutstandingCurrentMonth;
    @BindView(R.id.viCollectableOutstandingSubsequentMonth)
    View viCollectableOutstandingSubsequentMonth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_outstanding, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        setData();

        mAdapter = new OutstandingAdapter(dashboardActivityContext, outstandingModelArrayList);
        rviData.setAdapter(mAdapter);

        return rootView;
    }

    private void setData() {
        for (int i = 0; i < 15; i++) {
            OutstandingModel outstandingModel = new OutstandingModel("GST1920DL-19114", "NATIONAL INFRA", "9.81", "Binay Kumar", "4", "15-Nov-19", "25-Nov-19","FINANCE/ACCOUNT","Cartridge pending");
            outstandingModelArrayList.add(outstandingModel);
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
        return OutstandingFragment.class.getSimpleName();
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

    @OnClick(R.id.tviTotalOutstanding)
    public void TotalOutstanding() {
        viTotalOutstanding.setVisibility(View.VISIBLE);
        viCollectableOutstanding.setVisibility(View.INVISIBLE);
        viCollectableOutstandingCurrentMonth.setVisibility(View.INVISIBLE);
        viCollectableOutstandingSubsequentMonth.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("60513");
        tviAmounts.setText(getString(R.string.Rs) + " 272.80");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviCollectableOutstanding)
    public void CollectableOutstanding() {
        viTotalOutstanding.setVisibility(View.INVISIBLE);
        viCollectableOutstanding.setVisibility(View.VISIBLE);
        viCollectableOutstandingCurrentMonth.setVisibility(View.INVISIBLE);
        viCollectableOutstandingSubsequentMonth.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("9958");
        tviAmounts.setText(getString(R.string.Rs) + " 132.40");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviCollectableOutstandingCurrentMonth)
    public void CollectableOutstandingCurrentMonth() {
        viTotalOutstanding.setVisibility(View.INVISIBLE);
        viCollectableOutstanding.setVisibility(View.INVISIBLE);
        viCollectableOutstandingCurrentMonth.setVisibility(View.VISIBLE);
        viCollectableOutstandingSubsequentMonth.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("9481");
        tviAmounts.setText(getString(R.string.Rs) + " 107.06");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviCollectableOutstandingSubsequentMonth)
    public void CollectableOutstandingSubsequentMonth() {
        viTotalOutstanding.setVisibility(View.INVISIBLE);
        viCollectableOutstanding.setVisibility(View.INVISIBLE);
        viCollectableOutstandingCurrentMonth.setVisibility(View.INVISIBLE);
        viCollectableOutstandingSubsequentMonth.setVisibility(View.VISIBLE);
        tviNoofInvoice.setText("993");
        tviAmounts.setText(getString(R.string.Rs) + " 25.33");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
