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
import com.teamcomputers.bam.Adapters.SalesOutstanding.NewSalesPersonAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.NewSalesReceivable.SalesPersonListRequester;
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

public class WSSalesPersonFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String RSM_POSITION = "RSM_POSITION";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    SalesCustomerModel customerProfile;
    FullSalesModel rsmProfile, productProfile;

    String toolbarTitle = "";
    String userId = "", level = "";
    @BindView(R.id.llRSMLayout)
    LinearLayout llRSMLayout;
    @BindView(R.id.cviRSMHeading)
    CardView cviRSMHeading;
    @BindView(R.id.tviName)
    TextView tviName;
    @BindView(R.id.tviTargetHeading)
    TextView tviTargetHeading;
    @BindView(R.id.tviActualHeading)
    TextView tviActualHeading;
    @BindView(R.id.tviAchHeading)
    TextView tviAchHeading;
    @BindView(R.id.tviTarget)
    TextView tviTarget;
    @BindView(R.id.tviActual)
    TextView tviActual;
    @BindView(R.id.tviAch)
    TextView tviAch;
    @BindView(R.id.viYTD)
    View viYTD;
    @BindView(R.id.viQTD)
    View viQTD;
    @BindView(R.id.viMTD)
    View viMTD;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private NewSalesPersonAdapter adapter;
    private int type = 0, pos = 0;
    boolean fromRSM, fromCustomer, fromProduct;

    List<FullSalesModel> spDataList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sales_person, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);
        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);
        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        productProfile = getArguments().getParcelable(SP_PROFILE);
        if (null != rsmProfile) {
            cviRSMHeading.setVisibility(View.VISIBLE);
            tviTargetHeading.setText("Target");
            tviActualHeading.setText("Actual");
            tviAchHeading.setText("Ach");
            pos = rsmProfile.getPosition();
            if (pos == 0) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviName.setText(rsmProfile.getName());
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTD()));
            tviAch.setText(rsmProfile.getYTDPercentage().intValue() + "%");
        } else if (null != customerProfile) {
            cviRSMHeading.setVisibility(View.VISIBLE);
            tviTargetHeading.setText("YTD");
            tviActualHeading.setText("QTD");
            tviAchHeading.setText("MTD");
            pos = customerProfile.getPosition();
            if (pos == 0) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviName.setText(customerProfile.getCustomerName());
            tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYTD()));
            tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQTD()));
            tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMTD()));
        } else if (null != productProfile) {
            cviRSMHeading.setVisibility(View.VISIBLE);
            /*tviTargetHeading.setText("YTD");
            tviActualHeading.setText("QTD");
            tviAchHeading.setText("MTD");*/
            pos = productProfile.getPosition();
            if (pos == 0) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviName.setText(productProfile.getName());
            tviTarget.setText(BAMUtil.getRoundOffValue(productProfile.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(productProfile.getYTD()));
            tviAch.setText(productProfile.getYTDPercentage().intValue() + "%");
        }

        toolbarTitle = getString(R.string.Sales);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        showProgress(ProgressDialogTexts.LOADING);
        String rsm = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTMC();
        BackgroundExecutor.getInstance().execute(new FilterSalesListRequester(userId, level, "Sales", rsm, "", "", "", ""));
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
        dashboardActivityContext.sPClick(level);
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
                    /*case Events.GET_FULL_SALES_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            spDataList = new ArrayList<FullSalesModel>(Arrays.asList(data));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //initTreeData(model);
                        initData("YTD");
                        break;
                    case Events.GET_FULL_SALES_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SALES_PERSON_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            spDataList = new ArrayList<FullSalesModel>(Arrays.asList(data));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_SALES_PERSON_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;*/
                    case Events.GET_FILTER_SALES_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            spDataList = new ArrayList<FullSalesModel>(Arrays.asList(data));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_FILTER_SALES_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.SP_CLICK:
                        if (!fromCustomer) {
                            FullSalesModel spData = (FullSalesModel) eventObject.getObject();
                            Bundle spDataBundle = new Bundle();
                            spDataBundle.putString(WSCustomerFragment.USER_ID, userId);
                            spDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
                            spDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, fromRSM);
                            spDataBundle.putBoolean(WSCustomerFragment.FROM_SP, false);
                            spDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, fromProduct);
                            spDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, rsmProfile);
                            spDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, spData);
                            spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.WS_CUSTOMER_FRAGMENT, spDataBundle);
                        }
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        FullSalesModel spMenuData = (FullSalesModel) eventObject.getObject();
                        Bundle spMenuDataBundle = new Bundle();
                        spMenuDataBundle.putString(WSCustomerFragment.USER_ID, userId);
                        spMenuDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
                        spMenuDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, fromRSM);
                        spMenuDataBundle.putBoolean(WSCustomerFragment.FROM_SP, true);
                        spMenuDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, fromProduct);
                        spMenuDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, rsmProfile);
                        spMenuDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, spMenuData);
                        spMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.WS_CUSTOMER_FRAGMENT, spMenuDataBundle);
                        break;
                    case ClickEvents.PRODUCT_MENU_SELECT:
                        FullSalesModel spMenuProductData = (FullSalesModel) eventObject.getObject();
                        Bundle spMenuProductDataBundle = new Bundle();
                        spMenuProductDataBundle.putString(WSCustomerFragment.USER_ID, userId);
                        spMenuProductDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
                        spMenuProductDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, fromRSM);
                        spMenuProductDataBundle.putBoolean(WSCustomerFragment.FROM_SP, true);
                        spMenuProductDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, fromProduct);
                        spMenuProductDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, rsmProfile);
                        spMenuProductDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, spMenuProductData);
                        spMenuProductDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.WS_PRODUCT_FRAGMENT, spMenuProductDataBundle);
                        break;
                    case ClickEvents.RSM_ITEM:
                        int position = (int) eventObject.getObject();
                        Bundle rsmDataBundle = new Bundle();
                        rsmDataBundle.putParcelable(AccountsFragment.RSM_PROFILE, spDataList.get(position));
                        rsmDataBundle.putInt(AccountsFragment.RSM_POSITION, position);
                        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.ACCOUNT_FRAGMENT, rsmDataBundle);
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

    @OnClick(R.id.tviYTD)
    public void tviYTD() {
        viYTD.setVisibility(View.VISIBLE);
        viQTD.setVisibility(View.INVISIBLE);
        viMTD.setVisibility(View.INVISIBLE);
        if (null != rsmProfile) {
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTD()));
            tviAch.setText(rsmProfile.getYTDPercentage().intValue() + "%");
        }
        initData("YTD");
        adapter.notifyDataSetChanged();
        /*if (type == 0) {
            initRSMData("YTD");
            rsmAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getYTD()));
            tviAch.setText(rsmDataList.get(pos).getYTDPercentage().intValue() + "%");
            initData("YTD");
            adapter.notifyDataSetChanged();
        }*/
    }

    @OnClick(R.id.tviQTD)
    public void tviQTD() {
        viYTD.setVisibility(View.INVISIBLE);
        viQTD.setVisibility(View.VISIBLE);
        viMTD.setVisibility(View.INVISIBLE);
        if (null != rsmProfile) {
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getQTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getQTD()));
            tviAch.setText(rsmProfile.getQTDPercentage().intValue() + "%");
        }
        initData("QTD");
        adapter.notifyDataSetChanged();
        /*if (type == 0) {
            initRSMData("QTD");
            rsmAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getQTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getQTD()));
            tviAch.setText(rsmDataList.get(pos).getQTDPercentage().intValue() + "%");
            initData("QTD");
            adapter.notifyDataSetChanged();
        }*/
    }

    @OnClick(R.id.tviMTD)
    public void tviMTD() {
        viYTD.setVisibility(View.INVISIBLE);
        viQTD.setVisibility(View.INVISIBLE);
        viMTD.setVisibility(View.VISIBLE);
        if (null != rsmProfile) {
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getMTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getMTD()));
            tviAch.setText(rsmProfile.getMTDPercentage().intValue() + "%");
        }
        initData("MTD");
        adapter.notifyDataSetChanged();
        /*if (type == 0) {
            initRSMData("MTD");
            rsmAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getMTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getMTD()));
            tviAch.setText(rsmDataList.get(pos).getMTDPercentage().intValue() + "%");
            initData("MTD");
            adapter.notifyDataSetChanged();
        }*/
    }

    @OnClick(R.id.iviRSMClose)
    public void RSMClose() {
        fromRSM = false;
        fromCustomer = false;
        fromProduct = false;
        cviRSMHeading.setVisibility(View.GONE);
        type = 1;
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FilterSalesListRequester(userId, level, "Sales", "", "", "", "", ""));
        //BackgroundExecutor.getInstance().execute(new FullSalesListRequester(userId, "R1", "Sales", "", ""));
    }

    private void initData(String type) {
        adapter = new NewSalesPersonAdapter(dashboardActivityContext, type, level, spDataList, fromRSM, fromCustomer, fromProduct);
        rviRSM.setAdapter(adapter);
    }

}
