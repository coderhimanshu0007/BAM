package com.teamcomputers.bam.Fragments.SalesReceivable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.SalesOutstanding.AccountAdapter;
import com.teamcomputers.bam.Adapters.SalesOutstanding.CustomerAdapter;
import com.teamcomputers.bam.Adapters.SalesOutstanding.ProductAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.RSMDataModel;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.FullSalesListRequester;
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

public class ProductFragment extends BaseFragment {
    public static final String CUSTOMER = "CUSTOMER";
    public static final String STATE_CODE = "STATE_CODE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String PRODUCT_POSITION = "PRODUCT_POSITION";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;
    SalesCustomerModel fullSalesModel;
    String toolbarTitle = "";

    @BindView(R.id.llRSMLayout)
    LinearLayout llRSMLayout;
    @BindView(R.id.tviName)
    TextView tviName;
    @BindView(R.id.tviYtd)
    TextView tviYtd;
    @BindView(R.id.tviQtd)
    TextView tviQtd;
    @BindView(R.id.tviMtd)
    TextView tviMtd;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private ProductAdapter adapter;
    private int position = 0;
    private String customer, stateCode;

    List<FullSalesModel> model = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        customer = getArguments().getString(CUSTOMER);
        stateCode = getArguments().getString(STATE_CODE);
        fullSalesModel = getArguments().getParcelable(PRODUCT_PROFILE);
        position = getArguments().getInt(PRODUCT_POSITION);

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
        tviName.setText(fullSalesModel.getCustomerName());
        tviYtd.setText(BAMUtil.getRoundOffValue(fullSalesModel.getYTD()));
        tviQtd.setText(BAMUtil.getRoundOffValue(fullSalesModel.getQTD()));
        tviMtd.setText(BAMUtil.getRoundOffValue(fullSalesModel.getMTD()));

        toolbarTitle = getString(R.string.Product);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FullSalesListRequester(customer, "R4", "Product", fullSalesModel.getCustomerName(), ""));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*SalesDataModel[] model = SharedPreferencesController.getInstance(dashboardActivityContext).getSalesData();
        if (null != model) {
            initTreeData(model);
        }*/
        /*showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new SalesReceivableSalesRequester());*/
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return ProductFragment.class.getSimpleName();
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
                    case Events.GET_FULL_SALES_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            model = new ArrayList<FullSalesModel>(Arrays.asList(data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
                        dismissProgress();
                        break;
                    case Events.GET_FULL_SALES_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
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

    private void initData() {
        adapter = new ProductAdapter(dashboardActivityContext, model);
        rviRSM.setAdapter(adapter);
    }
}
