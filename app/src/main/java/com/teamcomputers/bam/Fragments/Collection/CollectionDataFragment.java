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
import com.teamcomputers.bam.Adapters.ExpectedCollectionAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.ExpectedCollectionModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CollectionDataFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    private ExpectedCollectionAdapter mAdapter;
    private ArrayList<ExpectedCollectionModel> collectionModelArrayList = new ArrayList<>();
    private ArrayList<ExpectedCollectionModel> expectedCollectionModelArrayList = new ArrayList<>();
    private ArrayList<ExpectedCollectionModel> paymentCollectionModelArrayList = new ArrayList<>();

    @BindView(R.id.pieChart)
    PieChart pieChart;

    @BindView(R.id.tviECWInvoice)
    TextView tviECWInvoice;
    @BindView(R.id.tviECWAmount)
    TextView tviECWAmount;

    @BindView(R.id.tviECMInvoice)
    TextView tviECMInvoice;
    @BindView(R.id.tviECMAmount)
    TextView tviECMAmount;

    @BindView(R.id.tviPCWInvoice)
    TextView tviPCWInvoice;
    @BindView(R.id.tviPCWAmount)
    TextView tviPCWAmount;

    @BindView(R.id.tviPCMInvoice)
    TextView tviPCMInvoice;
    @BindView(R.id.tviPCMAmount)
    TextView tviPCMAmount;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_collection_data, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        //setData();
        //setPaymentData();

        return rootView;
    }

    private void setData() {
        for (int i = 0; i < 15; i++) {
            ExpectedCollectionModel expectedCollectionModel = new ExpectedCollectionModel("GST1819KR-3232", "LIC OF INDIA", "0.53", "22-Nov-19");
            expectedCollectionModelArrayList.add(expectedCollectionModel);
        }
    }

    private void setPaymentData() {
        for (int i = 0; i < 15; i++) {
            ExpectedCollectionModel expectedCollectionModel = new ExpectedCollectionModel("CITCCR1920H....", "THE BOSTONC...", "0.53", "18-Nov-19");
            paymentCollectionModelArrayList.add(expectedCollectionModel);
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
        return CollectionDataFragment.class.getSimpleName();
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

    @OnClick(R.id.txtBtnECWDetails)
    public void ExpectedCollectionthisWeek() {
        Bundle ECWBundle = new Bundle();
        ECWBundle.putString(CollectionDetailsFragment.FROM, "ECW");
        ECWBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, ECWBundle);
    }

    @OnClick(R.id.txtBtnECMDetails)
    public void ExpectedCollectionthisMonth() {
        Bundle ECMBundle = new Bundle();
        ECMBundle.putString(CollectionDetailsFragment.FROM, "ECM");
        ECMBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, ECMBundle);
    }

    @OnClick(R.id.txtBtnPCWDetails)
    public void PaymentCollectionthisWeek() {
        Bundle PCWBundle = new Bundle();
        PCWBundle.putString(CollectionDetailsFragment.FROM, "PCW");
        PCWBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, PCWBundle);
    }

    @OnClick(R.id.txtBtnPCMDetails)
    public void PaymentCollectionthisMonth() {
        Bundle PCMBundle = new Bundle();
        PCMBundle.putString(CollectionDetailsFragment.FROM, "PCM");
        PCMBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        dashboardActivityContext.replaceFragment(Fragments.COLLECTION_FRAGMENT, PCMBundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
