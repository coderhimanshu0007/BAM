package com.teamcomputers.bam.Fragments.SalesReceivableR4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.SalesOutstanding.NewCustomerAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.CustomerFragment;
import com.teamcomputers.bam.Fragments.SalesReceivableR2.NewSalesPersonTabFragment;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.NewSalesReceivable.FullCustomerListRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.teamcomputers.bam.Fragments.NewSalesReceivable.SalesPersonFragment.USER_ID;

public class NewCustomerR4Fragment extends BaseFragment {
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String RSM_POSITION = "RSM_POSITION";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;


    String toolbarTitle = "";
    String userId = "";
    @BindView(R.id.cviSPHeading)
    CardView cviSPHeading;
    @BindView(R.id.llSPLayout)
    LinearLayout llSPLayout;
    @BindView(R.id.tviName)
    TextView tviName;
    @BindView(R.id.tviYTD)
    TextView tviYTD;
    @BindView(R.id.tviQTD)
    TextView tviQTD;
    @BindView(R.id.tviMTD)
    TextView tviMTD;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private NewCustomerAdapter adapter;
    private int position = 0;

    List<SalesCustomerModel> model = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_customer, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        userId = getArguments().getString(USER_ID);
        //fullSalesModel = getArguments().getParcelable(RSM_PROFILE);

        toolbarTitle = getString(R.string.Customer);
        //dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FullCustomerListRequester(userId, "R4", "Customer", "", ""));

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_screen_share);
        item.setVisible(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
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
        return AccountsFragment.class.getSimpleName();
    }

    @Subscribe
    public void onEvent(final EventObject eventObject) {
        dashboardActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case Events.NO_INTERNET_CONNECTION:
                        dismissProgress();
                        showToast(ToastTexts.NO_INTERNET_CONNECTION);
                        break;
                    case Events.NOT_FOUND:
                        dismissProgress();
                        showToast(ToastTexts.NO_RECORD_FOUND);
                        break;
                    case Events.GET_FULL_CUSTOMER_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            SalesCustomerModel[] data = (SalesCustomerModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), SalesCustomerModel[].class);
                            model = new ArrayList<SalesCustomerModel>(Arrays.asList(data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
                        dismissProgress();
                        break;
                    case Events.GET_FULL_CUSTOMER_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SELECTED_CUSTOMER_LIST_SUCCESSFULL:
                        dismissProgress();
                        cviSPHeading.setVisibility(View.VISIBLE);
                        FullSalesModel dataList = ((NewSalesPersonTabFragment) getParentFragment()).dataList;
                        position = dataList.getPosition();
                        if (position == 0) {
                            llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
                        } else if (position == 1) {
                            llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
                        } else if (position == 2) {
                            llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
                        } else if (position % 2 == 0) {
                            llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
                        } else if (position % 2 == 1) {
                            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
                        }
                        tviName.setText(dataList.getName());
                        tviYTD.setText(BAMUtil.getRoundOffValue(dataList.getYTD()));
                        tviQTD.setText(BAMUtil.getRoundOffValue(dataList.getQTD()));
                        tviMTD.setText(BAMUtil.getRoundOffValue(dataList.getMTD()));
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            SalesCustomerModel[] data = (SalesCustomerModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), SalesCustomerModel[].class);
                            model = new ArrayList<SalesCustomerModel>(Arrays.asList(data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
                        dismissProgress();
                        break;
                    case Events.GET_SELECTED_CUSTOMER_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.ACCOUNT_ITEM:
                        int position = (int) eventObject.getObject();
                        Bundle acctDataBundle = new Bundle();
                        acctDataBundle.putParcelable(CustomerFragment.ACCT_PROFILE, model.get(position));
                        acctDataBundle.putInt(CustomerFragment.ACCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.CUSTOMER_FRAGMENT, acctDataBundle);
                        /*acctDataBundle.putParcelable(ProductFragment.PRODUCT_PROFILE, model.get(position));
                        acctDataBundle.putInt(ProductFragment.PRODUCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.PRODUCT_FRAGMENT, acctDataBundle);*/
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iviR1Close)
    public void SPClose() {
        ((NewSalesPersonTabFragment) getParentFragment()).id = userId;
        cviSPHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FullCustomerListRequester(userId, "R4", "Customer", "", ""));
    }

    private void initData() {
        adapter = new NewCustomerAdapter(dashboardActivityContext, userId, "", model, false,false, false);
        rviRSM.setAdapter(adapter);
    }
}
