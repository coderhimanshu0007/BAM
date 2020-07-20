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
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.OSAgeingModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DeliveryInstallationFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    //private OSAgeingAdapter mAdapter;
    private ArrayList<OSAgeingModel> osAgeingModelArrayList = new ArrayList<>();

    @BindView(R.id.tviNoofInvoices)
    TextView tviNoofInvoice;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viDeliveryInstallationPendingUpto15Days)
    View viDeliveryInstallationPendingUpto15Days;
    @BindView(R.id.viDeliveryInstallationPendingMore15Days)
    View viDeliveryInstallationPendingMore15Days;
    @BindView(R.id.viDeliveryInstallationPendingMore30Days)
    View viDeliveryInstallationPendingMore30Days;
    @BindView(R.id.viPendingDocSubmissionUpto2Days)
    View viPendingDocSubmissionUpto2Days;
    @BindView(R.id.viPendingDocSubmissionGT2Days)
    View viPendingDocSubmissionGT2Days;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_delivery_installation, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        setData();

        //mAdapter = new OSAgeingAdapter(dashboardActivityContext, osAgeingModelArrayList);
        //rviData.setAdapter(mAdapter);

        return rootView;
    }

    private void setData() {
        for (int i = 0; i < 15; i++) {
            OSAgeingModel osAgeingModel = new OSAgeingModel("GST1920KR1", "INTUIT IN...", "1.10", "11-Oct-19", "14-Oct-19", "36", "0.10","22.59");
            osAgeingModelArrayList.add(osAgeingModel);
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
        return DeliveryInstallationFragment.class.getSimpleName();
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

    @OnClick(R.id.tviDeliveryInstallationPendingUpto15Days)
    public void DeliveryInstallationPendingUpto15Days() {
        viDeliveryInstallationPendingUpto15Days.setVisibility(View.VISIBLE);
        viDeliveryInstallationPendingMore15Days.setVisibility(View.INVISIBLE);
        viDeliveryInstallationPendingMore30Days.setVisibility(View.INVISIBLE);
        viPendingDocSubmissionUpto2Days.setVisibility(View.INVISIBLE);
        viPendingDocSubmissionGT2Days.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("9966");
        tviAmounts.setText(getString(R.string.Rs) + " 115.09");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviDeliveryInstallationPendingMore15Days)
    public void DeliveryInstallationPendingMore15Days() {
        viDeliveryInstallationPendingUpto15Days.setVisibility(View.INVISIBLE);
        viDeliveryInstallationPendingMore15Days.setVisibility(View.VISIBLE);
        viDeliveryInstallationPendingMore30Days.setVisibility(View.INVISIBLE);
        viPendingDocSubmissionUpto2Days.setVisibility(View.INVISIBLE);
        viPendingDocSubmissionGT2Days.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("2550");
        tviAmounts.setText(getString(R.string.Rs) + " 25.41");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviDeliveryInstallationPendingMore30Days)
    public void DeliveryInstallationPendingMore30Days() {
        viDeliveryInstallationPendingUpto15Days.setVisibility(View.INVISIBLE);
        viDeliveryInstallationPendingMore15Days.setVisibility(View.INVISIBLE);
        viDeliveryInstallationPendingMore30Days.setVisibility(View.VISIBLE);
        viPendingDocSubmissionUpto2Days.setVisibility(View.INVISIBLE);
        viPendingDocSubmissionGT2Days.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("4200");
        tviAmounts.setText(getString(R.string.Rs) + " 5072");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviPendingDocSubmissionUpto2Days)
    public void PendingDocSubmissionUpto2Days() {
        viDeliveryInstallationPendingUpto15Days.setVisibility(View.INVISIBLE);
        viDeliveryInstallationPendingMore15Days.setVisibility(View.INVISIBLE);
        viDeliveryInstallationPendingMore30Days.setVisibility(View.INVISIBLE);
        viPendingDocSubmissionUpto2Days.setVisibility(View.VISIBLE);
        viPendingDocSubmissionGT2Days.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("619");
        tviAmounts.setText(getString(R.string.Rs) + " 3.32");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviPendingDocSubmissionGT2Days)
    public void PendingDocSubmissionGT2Days() {
        viDeliveryInstallationPendingUpto15Days.setVisibility(View.INVISIBLE);
        viDeliveryInstallationPendingMore15Days.setVisibility(View.INVISIBLE);
        viDeliveryInstallationPendingMore30Days.setVisibility(View.INVISIBLE);
        viPendingDocSubmissionUpto2Days.setVisibility(View.INVISIBLE);
        viPendingDocSubmissionGT2Days.setVisibility(View.VISIBLE);
        tviNoofInvoice.setText("5711");
        tviAmounts.setText(getString(R.string.Rs) + " 39.46");
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
