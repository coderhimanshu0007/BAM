package com.teamcomputers.bam.Fragments.NewSalesReceivable;

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
import com.teamcomputers.bam.Adapters.SalesOutstanding.NewProductAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.CustomerFragment;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.NewSalesReceivable.FullProductListRequester;
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

public class NewProductFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String RSM_POSITION = "RSM_POSITION";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    FullSalesModel fullSalesModel;
    String userId = "";
    String toolbarTitle = "";
    @BindView(R.id.llTabHeading)
    LinearLayout llTabHeading;
    @BindView(R.id.cviProductHeading)
    CardView cviProductHeading;
    @BindView(R.id.llProductLayout)
    LinearLayout llProductLayout;
    @BindView(R.id.tviName)
    TextView tviName;
    @BindView(R.id.tviStateName)
    TextView tviStateName;
    @BindView(R.id.tviTarget)
    TextView tviTarget;
    @BindView(R.id.tviActual)
    TextView tviActual;
    @BindView(R.id.tviAch)
    TextView tviAch;
    @BindView(R.id.tviYtdHeading)
    TextView tviYtdHeading;
    @BindView(R.id.tviQtdHeading)
    TextView tviQtdHeading;
    @BindView(R.id.tviMtdHeading)
    TextView tviMtdHeading;
    @BindView(R.id.viYTD)
    View viYTD;
    @BindView(R.id.viQTD)
    View viQTD;
    @BindView(R.id.viMTD)
    View viMTD;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private NewProductAdapter adapter;
    private int position = 0;

    List<FullSalesModel> model = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_product, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        userId = getArguments().getString(USER_ID);
        //fullSalesModel = getArguments().getParcelable(RSM_PROFILE);
        /*position = getArguments().getInt(RSM_POSITION);

        if (position == 0) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
        } else if (position == 1) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
        } else if (position == 2) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
        } else if (position % 2 == 0) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
        } else if (position % 2 == 1) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
        }
        tviName.setText(fullSalesModel.getName());
        tviYtd.setText(BAMUtil.getRoundOffValue(fullSalesModel.getYTD()));
        tviQtd.setText(BAMUtil.getRoundOffValue(fullSalesModel.getQTD()));
        tviMtd.setText(BAMUtil.getRoundOffValue(fullSalesModel.getMTD()));
*/
        //toolbarTitle = getString(R.string.Sales);
        //dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FullProductListRequester(userId, "R1", "Product", "", ""));

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
        return NewProductFragment.class.getSimpleName();
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
                    case Events.GET_FULL_PRODUCT_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            model = new ArrayList<FullSalesModel>(Arrays.asList(data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_FULL_PRODUCT_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SELECTED_PRODUCT_LIST_SUCCESSFULL:
                        dismissProgress();
                        llTabHeading.setVisibility(View.GONE);
                        cviProductHeading.setVisibility(View.VISIBLE);
                        tviYtdHeading.setText("YTD");
                        tviQtdHeading.setText("QTD");
                        tviMtdHeading.setText("MTD");
                        //FullSalesModel dataList = ((NewRSMTabFragment) getParentFragment()).dataList;
                        SalesCustomerModel salesCustomerModel = ((NewRSMTabFragment) getParentFragment()).customerList;
                        position = salesCustomerModel.getPosition();
                        if (position == 0) {
                            llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
                        } else if (position == 1) {
                            llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
                        } else if (position == 2) {
                            llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
                        } else if (position % 2 == 0) {
                            llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
                        } else if (position % 2 == 1) {
                            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
                        }
                        tviName.setText(salesCustomerModel.getCustomerName());
                        if (salesCustomerModel.getStateCodeWise().size() == 1) {
                            tviStateName.setVisibility(View.VISIBLE);
                            tviStateName.setText(salesCustomerModel.getStateCodeWise().get(0).getStateCode());
                        } else {
                            tviStateName.setVisibility(View.GONE);
                        }
                        tviTarget.setText(BAMUtil.getRoundOffValue(salesCustomerModel.getYTD()));
                        tviActual.setText(BAMUtil.getRoundOffValue(salesCustomerModel.getQTD()));
                        tviAch.setText(BAMUtil.getRoundOffValue(salesCustomerModel.getMTD()));
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            model = new ArrayList<FullSalesModel>(Arrays.asList(data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData("0");
                        dismissProgress();
                        break;
                    case Events.GET_SELECTED_PRODUCT_LIST_UNSUCCESSFULL:
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

    @OnClick(R.id.tviYTD)
    public void tviYTD() {
        viYTD.setVisibility(View.VISIBLE);
        viQTD.setVisibility(View.INVISIBLE);
        viMTD.setVisibility(View.INVISIBLE);
        initData("YTD");
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tviQTD)
    public void tviQTD() {
        viYTD.setVisibility(View.INVISIBLE);
        viQTD.setVisibility(View.VISIBLE);
        viMTD.setVisibility(View.INVISIBLE);
        initData("QTD");
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tviMTD)
    public void tviMTD() {
        viYTD.setVisibility(View.INVISIBLE);
        viQTD.setVisibility(View.INVISIBLE);
        viMTD.setVisibility(View.VISIBLE);
        initData("MTD");
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.iviR1Close)
    public void productClose() {
        llTabHeading.setVisibility(View.VISIBLE);
        cviProductHeading.setVisibility(View.GONE);
        tviYtdHeading.setText("TARGET");
        tviQtdHeading.setText("ACTUAL");
        tviMtdHeading.setText("ACH%");
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FullProductListRequester(userId, "R1", "Product", "", ""));
    }

    private void initData(String type) {
        adapter = new NewProductAdapter(dashboardActivityContext, "",type, model, false, false,false);
        rviRSM.setAdapter(adapter);
    }
}
