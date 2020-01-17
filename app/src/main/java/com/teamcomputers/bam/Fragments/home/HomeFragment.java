package com.teamcomputers.bam.Fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    LoginModel userProfile;
    private DashboardActivity dashboardActivityContext;
    private HomeViewModel homeViewModel;
    String toolbarTitle = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        dashboardActivityContext = (DashboardActivity) context;
        toolbarTitle = getString(R.string.Heading_Home);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);
        final TextView text_hello = rootView.findViewById(R.id.text_hello);
        final TextView text_name = rootView.findViewById(R.id.text_name);
        userProfile = SharedPreferencesController.getInstance(BAMApplication.getInstance()).getUserProfile();
        /*homeViewModel.getText().observe(this, new Observer<HomeViewModel.User>() {
            @Override
            public void onChanged(@Nullable HomeViewModel.User s) {
                text_hello.setText(s.helloText);
                text_name.setText(userProfile.getFirstName());
            }
        });*/
        homeViewModel.getText().observe(this, new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {
                text_hello.setText("Hello,");
                String name = null;
                if (loginModel.getMemberName().split(" ").length > 2) {
                    name = loginModel.getMemberName().split(" ")[0]
                            + " " + loginModel.getMemberName().split(" ")[loginModel.getMemberName().split(" ").length - 1];
                    ;
                } else if (loginModel.getMemberName().split(" ").length <= 2) {
                    name = loginModel.getMemberName();
                }
                text_name.setText(name);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_screen_share);
        item.setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //getFragmentName();
    }

    @Override
    public String getFragmentName() {
        return HomeFragment.class.getSimpleName();
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

    @OnClick(R.id.rlaWS)
    public void WS() {
        EventBus.getDefault().post(new EventObject(Events.WS, null));
    }

    @OnClick(R.id.rlaOthers)
    public void Others() {
        EventBus.getDefault().post(new EventObject(Events.OTHERS, null));
    }
}