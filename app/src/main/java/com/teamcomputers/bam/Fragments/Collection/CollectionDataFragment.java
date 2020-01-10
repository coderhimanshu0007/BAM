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

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private ExpectedCollectionAdapter mAdapter;
    private ArrayList<ExpectedCollectionModel> collectionModelArrayList = new ArrayList<>();
    private ArrayList<ExpectedCollectionModel> expectedCollectionModelArrayList = new ArrayList<>();
    private ArrayList<ExpectedCollectionModel> paymentCollectionModelArrayList = new ArrayList<>();

    @BindView(R.id.tviNoofInvoice)
    TextView tviNoofInvoice;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viExpectedCollectionthisWeek)
    View viExpectedCollectionthisWeek;
    @BindView(R.id.viExpectedCollectionthisMonth)
    View viExpectedCollectionthisMonth;
    @BindView(R.id.viPaymentCollectionthisWeek)
    View viPaymentCollectionthisWeek;
    @BindView(R.id.viPaymentCollectionthisMonth)
    View viPaymentCollectionthisMonth;

    @BindView(R.id.tviInvoiceNoHeading)
    TextView tviInvoiceNoHeading;
    @BindView(R.id.tviCustomerHeading)
    TextView tviCustomerHeading;
    @BindView(R.id.tviAmountHeading)
    TextView tviAmountHeading;
    @BindView(R.id.tviExpectedDateHeading)
    TextView tviExpectedDateHeading;

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

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        setData();
        setPaymentData();
        collectionModelArrayList.addAll(expectedCollectionModelArrayList);
        mAdapter = new ExpectedCollectionAdapter(dashboardActivityContext, collectionModelArrayList);
        rviData.setAdapter(mAdapter);

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

    @OnClick(R.id.tviExpectedCollectionthisWeek)
    public void ExpectedCollectionthisWeek() {
        collectionModelArrayList.clear();
        collectionModelArrayList.addAll(expectedCollectionModelArrayList);
        mAdapter.notifyDataSetChanged();
        viExpectedCollectionthisWeek.setVisibility(View.VISIBLE);
        viExpectedCollectionthisMonth.setVisibility(View.INVISIBLE);
        viPaymentCollectionthisWeek.setVisibility(View.INVISIBLE);
        viPaymentCollectionthisMonth.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("576");
        tviAmounts.setText(getString(R.string.Rs) + " 6.58");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviInvoiceNoHeading.setText("Invoice No");
        tviCustomerHeading.setText("Customer");
        tviAmountHeading.setText("Amount");
        tviExpectedDateHeading.setText("Expected Date");
    }

    @OnClick(R.id.tviExpectedCollectionthisMonth)
    public void ExpectedCollectionthisMonth() {
        collectionModelArrayList.clear();
        collectionModelArrayList.addAll(expectedCollectionModelArrayList);
        mAdapter.notifyDataSetChanged();
        viExpectedCollectionthisWeek.setVisibility(View.INVISIBLE);
        viExpectedCollectionthisMonth.setVisibility(View.VISIBLE);
        viPaymentCollectionthisWeek.setVisibility(View.INVISIBLE);
        viPaymentCollectionthisMonth.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("1643");
        tviAmounts.setText(getString(R.string.Rs) + " 41.08");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviInvoiceNoHeading.setText("Invoice No");
        tviCustomerHeading.setText("Customer");
        tviAmountHeading.setText("Amount");
        tviExpectedDateHeading.setText("Expected Date");
    }

    @OnClick(R.id.tviPaymentCollectionthisWeek)
    public void PaymentCollectionthisWeek() {
        collectionModelArrayList.clear();
        collectionModelArrayList.addAll(paymentCollectionModelArrayList);
        mAdapter.notifyDataSetChanged();
        viExpectedCollectionthisWeek.setVisibility(View.INVISIBLE);
        viExpectedCollectionthisMonth.setVisibility(View.INVISIBLE);
        viPaymentCollectionthisWeek.setVisibility(View.VISIBLE);
        viPaymentCollectionthisMonth.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("44");
        tviAmounts.setText(getString(R.string.Rs) + " 1.25");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviInvoiceNoHeading.setText("Document No");
        tviCustomerHeading.setText("Name");
        tviAmountHeading.setText("Amount");
        tviExpectedDateHeading.setText("Posting Date");
    }

    @OnClick(R.id.tviPaymentCollectionthisMonth)
    public void PaymentCollectionthisMonth() {
        collectionModelArrayList.clear();
        collectionModelArrayList.addAll(paymentCollectionModelArrayList);
        mAdapter.notifyDataSetChanged();
        viExpectedCollectionthisWeek.setVisibility(View.INVISIBLE);
        viExpectedCollectionthisMonth.setVisibility(View.INVISIBLE);
        viPaymentCollectionthisWeek.setVisibility(View.INVISIBLE);
        viPaymentCollectionthisMonth.setVisibility(View.VISIBLE);
        tviNoofInvoice.setText("1161");
        tviAmounts.setText(getString(R.string.Rs) + " 60.50");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviInvoiceNoHeading.setText("Document No");
        tviCustomerHeading.setText("Name");
        tviAmountHeading.setText("Amount");
        tviExpectedDateHeading.setText("Posting Date");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
