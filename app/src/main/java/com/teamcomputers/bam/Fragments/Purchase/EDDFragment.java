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
import com.teamcomputers.bam.Adapters.EDDAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.EDDModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EDDFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    RecyclerView mRecyclerViewCals;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.tviNoofPO)
    TextView tviNoofPO;
    @BindView(R.id.tviAmounts)
    TextView tviAmounts;

    @BindView(R.id.viBlankEDD)
    View viBlankEDD;
    @BindView(R.id.viBlankEDD7Days)
    View viBlankEDD7Days;
    @BindView(R.id.viEDDCrossed3Days)
    View viEDDCrossed3Days;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    private EDDAdapter mAdapter;
    private ArrayList<EDDModel> eddModelArrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_edd, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        setData();

        mAdapter = new EDDAdapter(dashboardActivityContext, eddModelArrayList);
        rviData.setAdapter(mAdapter);

        return rootView;
    }

    private void setData() {
        for (int i = 0; i < 15; i++) {
            EDDModel eddModel = new EDDModel("Kiran Kadam", "POGSTMH 1920-3254", "LIC of India", "REDINGTOM INDIA LIMITED", "0", "nod", "dr");
            eddModelArrayList.add(eddModel);
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
        return EDDFragment.class.getSimpleName();
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

    @OnClick(R.id.tviBlankEDD)
    public void BlankEDD() {
        viBlankEDD.setVisibility(View.VISIBLE);
        viBlankEDD7Days.setVisibility(View.INVISIBLE);
        viEDDCrossed3Days.setVisibility(View.INVISIBLE);
        tviNoofPO.setText("86");
        tviAmounts.setText(getString(R.string.Rs) + " 1195");
        tviNoofPO.setTextColor(getResources().getColor(R.color.logistics_amount));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount));
    }

    @OnClick(R.id.tviBlankEDD7Days)
    public void BlankEDD7Days() {
        viBlankEDD.setVisibility(View.INVISIBLE);
        viBlankEDD7Days.setVisibility(View.VISIBLE);
        viEDDCrossed3Days.setVisibility(View.INVISIBLE);
        tviNoofPO.setText("(Blank)");
        tviAmounts.setText("(Blank)");
        tviNoofPO.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @OnClick(R.id.tviEDDCrossed3Days)
    public void EDDCrossed3Days() {
        viBlankEDD.setVisibility(View.INVISIBLE);
        viBlankEDD7Days.setVisibility(View.INVISIBLE);
        viEDDCrossed3Days.setVisibility(View.VISIBLE);
        tviNoofPO.setText("2");
        tviAmounts.setText(getString(R.string.Rs) + " 0");
        tviNoofPO.setTextColor(getResources().getColor(R.color.logistics_amount_red));
        tviAmounts.setTextColor(getResources().getColor(R.color.logistics_amount_red));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
