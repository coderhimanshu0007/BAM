package com.teamcomputers.bam.Fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.ActiveEmployeeAccessModel;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
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
    ActiveEmployeeAccessModel activeEmployeeAccessModel;
    @BindView(R.id.llaOrderProcssing)
    LinearLayout llaOrderProcessing;
    @BindView(R.id.llaPurchase)
    LinearLayout llaPurchase;
    @BindView(R.id.llaLogistics)
    LinearLayout llaLogistics;
    @BindView(R.id.llaInstallation)
    LinearLayout llaInstallation;
    @BindView(R.id.llaCollection)
    LinearLayout llaCollection;
    @BindView(R.id.llaWS)
    LinearLayout llaWS;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        EventBus.getDefault().register(this);
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
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {
                //text_hello.setText("Hello,");
                String name = null;
                if (loginModel.getMemberName().split(" ").length > 2) {
                    name = loginModel.getMemberName().split(" ")[0]
                            + " " + loginModel.getMemberName().split(" ")[loginModel.getMemberName().split(" ").length - 1];
                    ;
                } else if (loginModel.getMemberName().split(" ").length <= 2) {
                    name = loginModel.getMemberName();
                }
                text_hello.setText("Hello " + name);
            }
        });

        llaOrderProcessing.setVisibility(View.VISIBLE);
        llaPurchase.setVisibility(View.VISIBLE);
        llaLogistics.setVisibility(View.VISIBLE);
        llaInstallation.setVisibility(View.VISIBLE);
        llaCollection.setVisibility(View.VISIBLE);
        llaWS.setVisibility(View.VISIBLE);

        activeEmployeeAccessModel = SharedPreferencesController.getInstance(dashboardActivityContext).getActiveEmployeeAccess();
        if (activeEmployeeAccessModel.getData().getOrderProcessingModule() == 1) {
            llaOrderProcessing.setVisibility(View.GONE);
        }
        if (activeEmployeeAccessModel.getData().getPurchaseModule() == 1) {
            llaPurchase.setVisibility(View.GONE);
        }
        if (activeEmployeeAccessModel.getData().getLogisticsModule() == 1) {
            llaLogistics.setVisibility(View.GONE);
        }
        if (activeEmployeeAccessModel.getData().getInstallationModule() == 1) {
            llaInstallation.setVisibility(View.GONE);
        }
        if (activeEmployeeAccessModel.getData().getCollectionModule() == 1) {
            llaCollection.setVisibility(View.GONE);
        }
        if (activeEmployeeAccessModel.getData().getSalesModule() == 1) {
            llaWS.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_screen_share);
        item.setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivityContext.hideTab();
        dashboardActivityContext.hideTOSTab();
        dashboardActivityContext.hideOSOTab();
        //getFragmentName();
    }

    @Override
    public String getFragmentName() {
        return HomeFragment.class.getSimpleName();
    }

    @Subscribe
    public void onEvent(final EventObject eventObject) {
        dashboardActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    /*case Events.NO_INTERNET_CONNECTION:
                        dismissProgress();
                        showToast(ToastTexts.NO_INTERNET_CONNECTION);
                        break;
                    case Events.GET_ACTIVE_EMPLOYEE_ACCESS_SUCCESSFUL:
                        dismissProgress();
                        showToast("RECORD_FOUND");
                        break;
                    case Events.GET_ACTIVE_EMPLOYEE_ACCESS_UNSUCCESSFUL:
                        dismissProgress();
                        showToast(ToastTexts.NO_RECORD_FOUND);
                        break;*/
                }
            }
        });
    }

    @OnClick(R.id.rlaOrderProcessing)
    public void OrderProcessing() {
        if (activeEmployeeAccessModel.getData().getOrderProcessingModule() == 1) {
            EventBus.getDefault().post(new EventObject(Events.ORDER_PROCESSING, null));
        }
    }

    @OnClick(R.id.rlaPurchase)
    public void Purchase() {
        if (activeEmployeeAccessModel.getData().getPurchaseModule() == 1) {
            EventBus.getDefault().post(new EventObject(Events.PURCHASE, null));
        }
    }

    @OnClick(R.id.rlaLogistics)
    public void Logistics() {
        if (activeEmployeeAccessModel.getData().getLogisticsModule() == 1) {
            EventBus.getDefault().post(new EventObject(Events.LOGISTICS, null));
        }
    }

    @OnClick(R.id.rlaInstallation)
    public void Installation() {
        if (activeEmployeeAccessModel.getData().getInstallationModule() == 1) {
            EventBus.getDefault().post(new EventObject(Events.INSTALLATION, null));
        }
    }

    @OnClick(R.id.rlaCollection)
    public void Collection() {
        if (activeEmployeeAccessModel.getData().getCollectionModule() == 1) {
            EventBus.getDefault().post(new EventObject(Events.COLLECTION, null));
        }
    }

    @OnClick(R.id.rlaWS)
    public void WS() {
        if (activeEmployeeAccessModel.getData().getSalesModule() == 1) {
            EventBus.getDefault().post(new EventObject(Events.WS, null));
        }
    }

    @OnClick(R.id.rlaOthers)
    public void Others() {
        EventBus.getDefault().post(new EventObject(Events.OTHERS, null));
    }
}