package com.teamcomputers.bam.Fragments.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Activities.MainActivity;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private HomeViewModel homeViewModel;
    String userId = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        dashboardActivityContext = (DashboardActivity) context;
        userId = "KoCKPIT";
        dashboardActivityContext.setToolBarTitle(userId);
        final TextView text_hello = rootView.findViewById(R.id.text_hello);
        final TextView text_name = rootView.findViewById(R.id.text_name);
        homeViewModel.getText().observe(this, new Observer<HomeViewModel.User>() {
            @Override
            public void onChanged(@Nullable HomeViewModel.User s) {
                text_hello.setText(s.helloText);
                text_name.setText(s.nameText);
            }
        });

        return rootView;
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @OnClick(R.id.rlaOrderProcessing)
    public void OrderProcessing() {
        EventBus.getDefault().post(new EventObject(Events.ORDER_PROCESSING, null));
    }
    @OnClick(R.id.rlaPurchase)
    public void Purchase() {
        EventBus.getDefault().post(new EventObject(Events.PURCHASE, null));
    }
    @OnClick(R.id.rlaLogistics)
    public void Logistics() {
        EventBus.getDefault().post(new EventObject(Events.LOGISTICS, null));
    }
    @OnClick(R.id.rlaInstallation)
    public void Installation() {
        EventBus.getDefault().post(new EventObject(Events.INSTALLATION, null));
    }
    @OnClick(R.id.rlaCollection)
    public void Collection() {
        EventBus.getDefault().post(new EventObject(Events.COLLECTION, null));
    }
}