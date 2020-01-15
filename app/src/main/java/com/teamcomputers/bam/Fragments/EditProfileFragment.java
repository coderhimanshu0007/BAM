package com.teamcomputers.bam.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Fragments.home.HomeViewModel;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditProfileFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private HomeViewModel homeViewModel;

    @BindView(R.id.txtUserName)
    EditText txtUserName;
    @BindView(R.id.txtPhoneNumber)
    EditText txtPhoneNumber;

    String toolbarTitle = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        dashboardActivityContext = (DashboardActivity) context;
        toolbarTitle = getString(R.string.Heading_Edit_Profile);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);
        //final TextView text_hello = rootView.findViewById(R.id.text_hello);
        //final TextView text_name = rootView.findViewById(R.id.text_name);
        homeViewModel.getText().observe(this, new Observer<LoginModel>() {
            @Override
            public void onChanged(@Nullable LoginModel s) {
                txtUserName.setText(s.getMemberName());
                txtPhoneNumber.setText(s.getMobilePhoneNo());
            }
        });

        return rootView;
    }

    @Override
    public String getFragmentName() {
        return EditProfileFragment.class.getSimpleName();
    }

    @OnClick(R.id.llSave)
    public void OrderProcessing() {
        showToast(ToastTexts.WORK_PROGRESS);
        //EventBus.getDefault().post(new EventObject(Events.ORDER_PROCESSING, null));
    }
}