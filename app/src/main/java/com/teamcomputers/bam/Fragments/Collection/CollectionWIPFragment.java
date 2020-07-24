package com.teamcomputers.bam.Fragments.Collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.charts.PieChart;
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

public class CollectionWIPFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    private ArrayList<OSAgeingModel> osAgeingModelArrayList = new ArrayList<>();

    @BindView(R.id.pieChart)
    PieChart pieChart;

    @BindView(R.id.tviWIP0Invoice)
    TextView tviWIP0Invoice;
    @BindView(R.id.tviWIP0Amount)
    TextView tviWIP0Amount;

    @BindView(R.id.tviWIP16Invoice)
    TextView tviWIP16Invoice;
    @BindView(R.id.tviWIP16Amount)
    TextView tviWIP16Amount;

    @BindView(R.id.tviWIP30Invoice)
    TextView tviWIP30Invoice;
    @BindView(R.id.tviWIP30Amount)
    TextView tviWIP30Amount;

    @BindView(R.id.tviPDOSLInvoice)
    TextView tviPDOSLInvoice;
    @BindView(R.id.tviPDOSLAmount)
    TextView tviPDOSLAmount;

    @BindView(R.id.tviPDOSGInvoice)
    TextView tviPDOSGInvoice;
    @BindView(R.id.tviPDOSGAmount)
    TextView tviPDOSGAmount;

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

        //layoutManager = new LinearLayoutManager(dashboardActivityContext);
        //rviData.setLayoutManager(layoutManager);

        //setData();

        //mAdapter = new OSAgeingAdapter(dashboardActivityContext, osAgeingModelArrayList);
        //rviData.setAdapter(mAdapter);

        return rootView;
    }

    private void setData() {
        for (int i = 0; i < 15; i++) {
            OSAgeingModel osAgeingModel = new OSAgeingModel("GST1920KR1", "INTUIT IN...", "1.10", "11-Oct-19", "14-Oct-19", "36", "0.10", "22.59");
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
        return CollectionWIPFragment.class.getSimpleName();
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

    @OnClick(R.id.txtBtnWIP0Details)
    public void DeliveryInstallationPendingUpto15Days() {
        Bundle WIP0Bundle = new Bundle();
        WIP0Bundle.putString(WIPDetailsFragment.FROM, "WIP0");
        WIP0Bundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, WIP0Bundle);
    }

    @OnClick(R.id.txtBtnWIP16Details)
    public void DeliveryInstallationPendingMore15Days() {
        Bundle WIP16Bundle = new Bundle();
        WIP16Bundle.putString(WIPDetailsFragment.FROM, "WIP16");
        WIP16Bundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, WIP16Bundle);
    }

    @OnClick(R.id.txtBtnWIP30Details)
    public void DeliveryInstallationPendingMore30Days() {
        Bundle WIP30Bundle = new Bundle();
        WIP30Bundle.putString(WIPDetailsFragment.FROM, "WIP30");
        WIP30Bundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, WIP30Bundle);
    }

    @OnClick(R.id.txtBtnPDOSLDetails)
    public void PendingDocSubmissionUpto2Days() {
        Bundle PDOSLBundle = new Bundle();
        PDOSLBundle.putString(WIPDetailsFragment.FROM, "PDOSL");
        PDOSLBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.WIP_FRAGMENT, PDOSLBundle);
    }

    @OnClick(R.id.txtBtnPDOSGDetails)
    public void PendingDocSubmissionGT2Days() {
        Bundle PDOSGBundle = new Bundle();
        PDOSGBundle.putString(WIPDetailsFragment.FROM, "PDOSG");
        PDOSGBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.TOTAL_OUTSTANDING_FRAGMENT, PDOSGBundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
