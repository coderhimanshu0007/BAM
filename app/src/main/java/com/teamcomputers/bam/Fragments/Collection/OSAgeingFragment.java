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
import com.teamcomputers.bam.Adapters.OSAgeingAdapter;
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

public class OSAgeingFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private OSAgeingAdapter mAdapter;
    private ArrayList<OSAgeingModel> osAgeingModelArrayList = new ArrayList<>();

    @BindView(R.id.tviNoofSO)
    TextView tviNoofInvoice;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viUpto30Days)
    View viUpto30Days;
    @BindView(R.id.viUpto60Days)
    View viUpto60Days;
    @BindView(R.id.viUpto90Days)
    View viUpto90Days;
    @BindView(R.id.viUpto120Days)
    View viUpto120Days;
    @BindView(R.id.viMoreThan120Days)
    View viMoreThan120Days;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_os_ageing, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        setData();

        mAdapter = new OSAgeingAdapter(dashboardActivityContext, osAgeingModelArrayList);
        rviData.setAdapter(mAdapter);

        return rootView;
    }

    private void setData() {
        for (int i = 0; i < 15; i++) {
            OSAgeingModel osAgeingModel = new OSAgeingModel("LIC of India", "0.45", "15.15", "4.83", "1.92", "0.14", "0.10","22.59");
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
        return OSAgeingFragment.class.getSimpleName();
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

    @OnClick(R.id.tviUpto30Days)
    public void Upto30Days() {
        viUpto30Days.setVisibility(View.VISIBLE);
        viUpto60Days.setVisibility(View.INVISIBLE);
        viUpto90Days.setVisibility(View.INVISIBLE);
        viUpto120Days.setVisibility(View.INVISIBLE);
        viMoreThan120Days.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("2959");
        tviAmounts.setText(getString(R.string.Rs) + " 53.23");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviUpto60Days)
    public void Upto60Days() {
        viUpto30Days.setVisibility(View.INVISIBLE);
        viUpto60Days.setVisibility(View.VISIBLE);
        viUpto90Days.setVisibility(View.INVISIBLE);
        viUpto120Days.setVisibility(View.INVISIBLE);
        viMoreThan120Days.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("2896");
        tviAmounts.setText(getString(R.string.Rs) + " 21.87");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviUpto90Days)
    public void Upto90Days() {
        viUpto30Days.setVisibility(View.INVISIBLE);
        viUpto60Days.setVisibility(View.INVISIBLE);
        viUpto90Days.setVisibility(View.VISIBLE);
        viUpto120Days.setVisibility(View.INVISIBLE);
        viMoreThan120Days.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("932");
        tviAmounts.setText(getString(R.string.Rs) + " 5.99");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviUpto120Days)
    public void Upto120Days() {
        viUpto30Days.setVisibility(View.INVISIBLE);
        viUpto60Days.setVisibility(View.INVISIBLE);
        viUpto90Days.setVisibility(View.INVISIBLE);
        viUpto120Days.setVisibility(View.VISIBLE);
        viMoreThan120Days.setVisibility(View.INVISIBLE);
        tviNoofInvoice.setText("669");
        tviAmounts.setText(getString(R.string.Rs) + " 4.37");
        tviNoofInvoice.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviMoreThan120Days)
    public void MoreThan120Days() {
        viUpto30Days.setVisibility(View.INVISIBLE);
        viUpto60Days.setVisibility(View.INVISIBLE);
        viUpto90Days.setVisibility(View.INVISIBLE);
        viUpto120Days.setVisibility(View.INVISIBLE);
        viMoreThan120Days.setVisibility(View.VISIBLE);
        tviNoofInvoice.setText("1454");
        tviAmounts.setText(getString(R.string.Rs) + " 12.15");
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
