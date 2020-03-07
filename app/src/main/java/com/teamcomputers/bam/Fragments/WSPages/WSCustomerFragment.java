package com.teamcomputers.bam.Fragments.WSPages;

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
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.FilterSalesListRequester;
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

public class WSCustomerFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    FullSalesModel rsmModel, fullSalesModel;

    boolean fromRSM, fromSP, fromProduct;
    String toolbarTitle = "";
    String userId = "", level = "";
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

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromSP = getArguments().getBoolean(FROM_SP);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);

        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);
        rsmModel = getArguments().getParcelable(RSM_PROFILE);
        fullSalesModel = getArguments().getParcelable(SP_PROFILE);

        toolbarTitle = getString(R.string.Customer);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);
        if (null != fullSalesModel) {
            cviSPHeading.setVisibility(View.VISIBLE);
            position = fullSalesModel.getPosition();
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
            tviName.setText(fullSalesModel.getName());
            tviYTD.setText(BAMUtil.getRoundOffValue(fullSalesModel.getYTD()));
            tviQTD.setText(BAMUtil.getRoundOffValue(fullSalesModel.getQTD()));
            tviMTD.setText(BAMUtil.getRoundOffValue(fullSalesModel.getMTD()));
        }
        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);
        String rsm = "";
        if (null != rsmModel)
            rsm = rsmModel.getTMC();
        String sales = "";
        if (null != fullSalesModel)
            sales = fullSalesModel.getTMC();
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FilterSalesListRequester(userId, level, "Customer", rsm, sales, "", "", ""));

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
        dashboardActivityContext.showTab(level);
        dashboardActivityContext.customerClick(level);
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
                    case Events.GET_FILTER_SALES_LIST_SUCCESSFULL:
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
                    case Events.GET_FILTER_SALES_LIST_UNSUCCESSFULL:
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
                    case ClickEvents.CUSTOMER_SELECT:
                        if (!fromProduct) {
                            SalesCustomerModel customerList = (SalesCustomerModel) eventObject.getObject();
                            Bundle customerBundle = new Bundle();
                            customerBundle.putString(WSProductFragment.USER_ID, userId);
                            customerBundle.putString(WSProductFragment.USER_LEVEL, level);
                            customerBundle.putBoolean(WSProductFragment.FROM_RSM, fromRSM);
                            customerBundle.putBoolean(WSProductFragment.FROM_SP, fromSP);
                            customerBundle.putBoolean(WSProductFragment.FROM_CUSTOMER, true);
                            customerBundle.putParcelable(WSProductFragment.CUSTOMER_PROFILE, customerList);
                            customerBundle.putParcelable(WSProductFragment.RSM_PROFILE, rsmModel);
                            customerBundle.putParcelable(WSProductFragment.SP_PROFILE, fullSalesModel);
                            customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.WS_PRODUCT_FRAGMENT, customerBundle);
                        }
                        break;
                    case ClickEvents.STATE_ITEM:
                        if (!fromProduct) {
                            Bundle productStateBundle = new Bundle();
                            SalesCustomerModel salesCustomerModel = (SalesCustomerModel) eventObject.getObject();
                            productStateBundle.putString(WSProductFragment.USER_ID, userId);
                            productStateBundle.putString(WSProductFragment.USER_LEVEL, level);
                            productStateBundle.putBoolean(WSProductFragment.FROM_RSM, fromRSM);
                            productStateBundle.putBoolean(WSProductFragment.FROM_SP, fromSP);
                            productStateBundle.putBoolean(WSProductFragment.FROM_CUSTOMER, true);
                            productStateBundle.putParcelable(WSProductFragment.CUSTOMER_PROFILE, salesCustomerModel);
                            productStateBundle.putParcelable(WSProductFragment.RSM_PROFILE, rsmModel);
                            productStateBundle.putParcelable(WSProductFragment.SP_PROFILE, fullSalesModel);
                            productStateBundle.putInt(WSProductFragment.STATE_CODE, 1);
                            productStateBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.WS_PRODUCT_FRAGMENT, productStateBundle);
                        }
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        SalesCustomerModel customerList = (SalesCustomerModel) eventObject.getObject();
                        Bundle customerBundle = new Bundle();
                        customerBundle.putString(WSProductFragment.USER_ID, userId);
                        customerBundle.putString(WSProductFragment.USER_LEVEL, level);
                        customerBundle.putBoolean(WSProductFragment.FROM_RSM, fromRSM);
                        customerBundle.putBoolean(WSProductFragment.FROM_SP, fromSP);
                        customerBundle.putBoolean(WSProductFragment.FROM_CUSTOMER, true);
                        customerBundle.putParcelable(WSProductFragment.CUSTOMER_PROFILE, customerList);
                        customerBundle.putParcelable(WSProductFragment.RSM_PROFILE, rsmModel);
                        customerBundle.putParcelable(WSProductFragment.SP_PROFILE, fullSalesModel);
                        customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.WS_ACCOUNT_FRAGMENT, customerBundle);
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

    @OnClick(R.id.iviSPClose)
    public void SPClose() {
        fromRSM = false;
        fromSP = false;
        fromProduct = false;
        cviSPHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FilterSalesListRequester(userId, level, "Customer", "", "", "", "", ""));
    }

    private void initData() {
        adapter = new NewCustomerAdapter(dashboardActivityContext, userId, level, model, fromRSM, fromSP, fromProduct);
        rviRSM.setAdapter(adapter);
    }
}
