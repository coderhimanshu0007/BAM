package com.teamcomputers.bam.Fragments.OpenSalesOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters.KPSOCustomerAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.CustomerFragment;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOCustomerModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOProductModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSORSMModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOSOModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.PSOFilter;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.WSRequesters.KSalesOpenOrderAprRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.KBAMUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class OSOCustomerFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String SO_PROFILE = "SO_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_SO = "FROM_SO";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    public static final String RSM_POS = "RSM_POS";
    public static final String SP_POS = "SP_POS";
    public static final String SO_POS = "SO_POS";
    public static final String PRODUCT_POS = "PRODUCT_POS";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    boolean fromRSM, fromSP, fromSO, fromProduct, search = false;
    String toolbarTitle = "";
    String userId = "", level = "";
    @BindView(R.id.txtSearch)
    EditText txtSearch;
    @BindView(R.id.cviSPHeading)
    CardView cviSPHeading;
    @BindView(R.id.llSPLayout)
    LinearLayout llSPLayout;
    @BindView(R.id.rlR1)
    RelativeLayout rlR1;
    @BindView(R.id.rlR2)
    RelativeLayout rlR2;
    @BindView(R.id.rlR3)
    RelativeLayout rlR3;
    @BindView(R.id.rlR4)
    RelativeLayout rlR4;
    @BindView(R.id.tviR1Name)
    TextView tviR1Name;
    @BindView(R.id.tviR2Name)
    TextView tviR2Name;
    @BindView(R.id.tviR3Name)
    TextView tviR3Name;
    @BindView(R.id.tviR4Name)
    TextView tviR4Name;
    @BindView(R.id.tviSOAmount)
    TextView tviSOAmount;
    @BindView(R.id.tviR1StateName)
    TextView tviR1StateName;
    @BindView(R.id.tviR2StateName)
    TextView tviR2StateName;
    @BindView(R.id.tviR3StateName)
    TextView tviR3StateName;
    @BindView(R.id.tviR4StateName)
    TextView tviR4StateName;
    @BindView(R.id.iviR1Close)
    ImageView iviR1Close;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private KPSOCustomerAdapter adapter;
    private int pos = 0, rsmPos = 0, spPos = 0, cPos = 0, soPos = 0, pPos = 0;

    KPSOCustomerModel customerData;
    KPSOCustomerModel.Datum selectedCustomerData;
    List<KPSOCustomerModel.Datum> customerDataList = new ArrayList<>();
    PSOFilter customerFilterData;

    KPSORSMModel.Datum rsmProfile, salesProfile;
    KPSOSOModel.Datum soProfile;
    KPSOProductModel.Datum productProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_oso_customer, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromSP = getArguments().getBoolean(FROM_SP);
        fromSO = getArguments().getBoolean(FROM_SO);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);

        rsmPos = getArguments().getInt(RSM_POS);
        spPos = getArguments().getInt(SP_POS);
        soPos = getArguments().getInt(SO_POS);
        pPos = getArguments().getInt(PRODUCT_POS);

        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        salesProfile = getArguments().getParcelable(SP_PROFILE);
        soProfile = getArguments().getParcelable(SO_PROFILE);
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);

        toolbarTitle = getString(R.string.Customer);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        rowsDisplay();

        dashboardActivityContext.fragmentView = rootView;

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
        dashboardActivityContext.showOSOTab(level);
        dashboardActivityContext.OSOcustomerClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return OSOCustomerFragment.class.getSimpleName();
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
                    case Events.GET_CUSTOMER_OSO_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            /*JSONArray jsonArray = new JSONArray(KBAMUtils.replaceDataResponse(eventObject.getObject().toString()));
                            OSOCustomerModel[] data = (OSOCustomerModel[]) KBAMUtils.fromJson(String.valueOf(jsonArray), OSOCustomerModel[].class);
                            model = new ArrayList<OSOCustomerModel>(Arrays.asList(data));*/
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()));
                            customerData = (KPSOCustomerModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KPSOCustomerModel.class);
                            customerDataList = customerData.getData();
                            for (int i = 0; i < customerDataList.size(); i++) {
                                if (customerDataList.get(i).getCustomerName().equals("") || customerDataList.get(i).getCustomerName().equals("null")) {
                                    customerDataList.remove(i);
                                }
                            }
                            customerFilterData = customerData.getFilter();
                            tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerFilterData.getSoAmount()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
                        dismissProgress();
                        break;
                    case Events.GET_CUSTOMER_OSO_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.ACCOUNT_ITEM:
                        int position = (int) eventObject.getObject();
                        Bundle acctDataBundle = new Bundle();
                        acctDataBundle.putParcelable(CustomerFragment.ACCT_PROFILE, customerDataList.get(position));
                        acctDataBundle.putInt(CustomerFragment.ACCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, acctDataBundle);
                        /*acctDataBundle.putParcelable(ProductFragment.PRODUCT_PROFILE, model.get(position));
                        acctDataBundle.putInt(ProductFragment.PRODUCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.PRODUCT_FRAGMENT, acctDataBundle);*/
                        break;
                    case ClickEvents.CUSTOMER_SELECT:
                        if (!fromSO) {
                            selectedCustomerData = (KPSOCustomerModel.Datum) eventObject.getObject();
                            Bundle customerBundle = new Bundle();
                            customerBundle.putString(OSOInvoiceFragment.USER_ID, userId);
                            customerBundle.putString(OSOInvoiceFragment.USER_LEVEL, level);

                            cPos = rsmPos + spPos + pPos + 1;

                            customerBundle.putInt(OSOInvoiceFragment.RSM_POS, rsmPos);
                            customerBundle.putInt(OSOInvoiceFragment.SP_POS, spPos);
                            customerBundle.putInt(OSOInvoiceFragment.CUSTOMER_POS, cPos);
                            customerBundle.putInt(OSOInvoiceFragment.PRODUCT_POS, pPos);

                            customerBundle.putBoolean(OSOInvoiceFragment.FROM_RSM, fromRSM);
                            customerBundle.putBoolean(OSOInvoiceFragment.FROM_SP, fromSP);
                            customerBundle.putBoolean(OSOInvoiceFragment.FROM_CUSTOMER, true);
                            customerBundle.putBoolean(OSOInvoiceFragment.FROM_PRODUCT, fromProduct);

                            customerBundle.putParcelable(OSOInvoiceFragment.CUSTOMER_PROFILE, selectedCustomerData);
                            customerBundle.putParcelable(OSOInvoiceFragment.RSM_PROFILE, rsmProfile);
                            customerBundle.putParcelable(OSOInvoiceFragment.SP_PROFILE, salesProfile);
                            customerBundle.putParcelable(OSOInvoiceFragment.PRODUCT_PROFILE, productProfile);

                            if (null != selectedCustomerData.getStateCodeWise()) {
                                customerBundle.putInt(OSOInvoiceFragment.STATE_CODE, 1);
                            } else {
                                customerBundle.putInt(OSOInvoiceFragment.STATE_CODE, 0);
                            }
                            customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.OSO_INVOICE_FRAGMENT, customerBundle);
                        }
                        break;
                    case ClickEvents.SO_ITEM_SELECT:
                        selectedCustomerData = (KPSOCustomerModel.Datum) eventObject.getObject();
                        Bundle productBundle = new Bundle();
                        productBundle.putString(OSOProductFragment.USER_ID, userId);
                        productBundle.putString(OSOProductFragment.USER_LEVEL, level);

                        cPos = rsmPos + spPos + soPos + 1;

                        productBundle.putInt(OSOProductFragment.RSM_POS, rsmPos);
                        productBundle.putInt(OSOProductFragment.SP_POS, spPos);
                        productBundle.putInt(OSOProductFragment.SO_POS, soPos);
                        productBundle.putInt(OSOProductFragment.CUSTOMER_POS, cPos);

                        productBundle.putBoolean(OSOProductFragment.FROM_RSM, fromRSM);
                        productBundle.putBoolean(OSOProductFragment.FROM_SP, fromSP);
                        productBundle.putBoolean(OSOProductFragment.FROM_SO, fromSO);
                        productBundle.putBoolean(OSOProductFragment.FROM_CUSTOMER, true);

                        productBundle.putParcelable(OSOProductFragment.RSM_PROFILE, rsmProfile);
                        productBundle.putParcelable(OSOProductFragment.SP_PROFILE, salesProfile);
                        productBundle.putParcelable(OSOProductFragment.CUSTOMER_PROFILE, selectedCustomerData);
                        productBundle.putParcelable(OSOProductFragment.SO_PROFILE, soProfile);
                        if (null != selectedCustomerData.getStateCodeWise()) {
                            productBundle.putInt(OSOProductFragment.STATE_CODE, 1);
                        } else {
                            productBundle.putInt(OSOProductFragment.STATE_CODE, 0);
                        }
                        productBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_PRODUCT_FRAGMENT, productBundle);
                        break;
                    case ClickEvents.CUSTOMER_ITEM:
                        //if (!fromSO) {
                        Bundle productStateBundle = new Bundle();
                        selectedCustomerData = (KPSOCustomerModel.Datum) eventObject.getObject();
                        productStateBundle.putString(OSOInvoiceFragment.USER_ID, userId);
                        productStateBundle.putString(OSOInvoiceFragment.USER_LEVEL, level);

                        cPos = rsmPos + spPos + pPos + 1;
                        productStateBundle.putInt(OSOInvoiceFragment.RSM_POS, rsmPos);
                        productStateBundle.putInt(OSOInvoiceFragment.SP_POS, spPos);
                        productStateBundle.putInt(OSOInvoiceFragment.CUSTOMER_POS, cPos);
                        productStateBundle.putInt(OSOInvoiceFragment.PRODUCT_POS, pPos);

                        productStateBundle.putBoolean(OSOInvoiceFragment.FROM_RSM, fromRSM);
                        productStateBundle.putBoolean(OSOInvoiceFragment.FROM_SP, fromSP);
                        productStateBundle.putBoolean(OSOInvoiceFragment.FROM_CUSTOMER, true);
                        productStateBundle.putBoolean(OSOInvoiceFragment.FROM_PRODUCT, fromProduct);

                        productStateBundle.putParcelable(OSOInvoiceFragment.CUSTOMER_PROFILE, selectedCustomerData);
                        productStateBundle.putParcelable(OSOInvoiceFragment.RSM_PROFILE, rsmProfile);
                        productStateBundle.putParcelable(OSOInvoiceFragment.SP_PROFILE, salesProfile);
                        productStateBundle.putParcelable(OSOInvoiceFragment.PRODUCT_PROFILE, productProfile);

                        if (null != selectedCustomerData.getStateCodeWise()) {
                            productStateBundle.putInt(OSOInvoiceFragment.STATE_CODE, 1);
                        } else {
                            productStateBundle.putInt(OSOInvoiceFragment.STATE_CODE, 0);
                        }
                        productStateBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_INVOICE_FRAGMENT, productStateBundle);
                        //}
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        selectedCustomerData = (KPSOCustomerModel.Datum) eventObject.getObject();
                        Bundle rsmBundle = new Bundle();
                        rsmBundle.putString(OSORSMFragment.USER_ID, userId);
                        rsmBundle.putString(OSORSMFragment.USER_LEVEL, level);

                        cPos = spPos + soPos + pPos + 1;
                        rsmBundle.putInt(OSORSMFragment.SP_POS, spPos);
                        rsmBundle.putInt(OSORSMFragment.SO_POS, soPos);
                        rsmBundle.putInt(OSORSMFragment.CUSTOMER_POS, cPos);
                        rsmBundle.putInt(OSORSMFragment.PRODUCT_POS, pPos);

                        rsmBundle.putBoolean(OSORSMFragment.FROM_SO, fromSO);
                        rsmBundle.putBoolean(OSORSMFragment.FROM_SP, fromSP);
                        rsmBundle.putBoolean(OSORSMFragment.FROM_CUSTOMER, true);
                        rsmBundle.putBoolean(OSORSMFragment.FROM_PRODUCT, fromProduct);

                        rsmBundle.putParcelable(OSORSMFragment.CUSTOMER_PROFILE, selectedCustomerData);
                        rsmBundle.putParcelable(OSORSMFragment.SO_PROFILE, soProfile);
                        rsmBundle.putParcelable(OSORSMFragment.SP_PROFILE, salesProfile);
                        rsmBundle.putParcelable(OSORSMFragment.PRODUCT_PROFILE, productProfile);
                        if (null != selectedCustomerData.getStateCodeWise()) {
                            rsmBundle.putInt(OSORSMFragment.STATE_CODE, 1);
                        } else {
                            rsmBundle.putInt(OSORSMFragment.STATE_CODE, 0);
                        }
                        rsmBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_RSM_FRAGMENT, rsmBundle);
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        selectedCustomerData = (KPSOCustomerModel.Datum) eventObject.getObject();
                        Bundle customerBundle = new Bundle();
                        customerBundle.putString(OSOSalesPersonFragment.USER_ID, userId);
                        customerBundle.putString(OSOSalesPersonFragment.USER_LEVEL, level);

                        cPos = rsmPos + soPos + pPos + 1;
                        customerBundle.putInt(OSOSalesPersonFragment.RSM_POS, rsmPos);
                        customerBundle.putInt(OSOSalesPersonFragment.CUSTOMER_POS, cPos);
                        customerBundle.putInt(OSOSalesPersonFragment.SO_POS, soPos);
                        customerBundle.putInt(OSOSalesPersonFragment.PRODUCT_POS, pPos);

                        customerBundle.putBoolean(OSOSalesPersonFragment.FROM_RSM, fromRSM);
                        customerBundle.putBoolean(OSOSalesPersonFragment.FROM_SO, fromSO);
                        customerBundle.putBoolean(OSOSalesPersonFragment.FROM_CUSTOMER, true);
                        customerBundle.putBoolean(OSOSalesPersonFragment.FROM_PRODUCT, fromProduct);

                        customerBundle.putParcelable(OSOSalesPersonFragment.CUSTOMER_PROFILE, selectedCustomerData);
                        customerBundle.putParcelable(OSOSalesPersonFragment.RSM_PROFILE, rsmProfile);
                        customerBundle.putParcelable(OSOSalesPersonFragment.SO_PROFILE, soProfile);
                        customerBundle.putParcelable(OSOSalesPersonFragment.PRODUCT_PROFILE, productProfile);
                        if (null != selectedCustomerData.getStateCodeWise()) {
                            customerBundle.putInt(OSOSalesPersonFragment.STATE_CODE, 1);
                        } else {
                            customerBundle.putInt(OSOSalesPersonFragment.STATE_CODE, 0);
                        }
                        customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, customerBundle);
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

    @OnTextChanged(R.id.txtSearch)
    public void search() {
        adapter.getFilter().filter(txtSearch.getText().toString());
    }

    @OnClick(R.id.iviSearch)
    public void Search() {
        if (!search) {
            txtSearch.setVisibility(View.VISIBLE);
            search = true;
        } else if (search) {
            txtSearch.setVisibility(View.GONE);
            search = false;
        }
    }

    @OnClick(R.id.iviClose)
    public void filterClose() {
        rsmPos = 0;
        spPos = 0;
        pPos = 0;
        soPos = 0;
        fromRSM = false;
        fromSP = false;
        fromProduct = false;
        fromSO = false;
        rsmProfile = null;
        salesProfile = null;
        soProfile = null;
        productProfile = null;
        cviSPHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Customer", "", "", "", "", "", ""));
        BackgroundExecutor.getInstance().execute(new KSalesOpenOrderAprRequester(userId, level, "Customer", "", "", "", "", "", ""));
    }

    @OnClick(R.id.iviR1Close)
    public void r1Close() {
        if (rsmPos == 1) {
            rsmPos = 0;
            fromRSM = false;
            rsmProfile = null;
        } else if (rsmPos == 2) {
            rsmPos = 1;
        } else if (rsmPos == 4) {
            rsmPos = 2;
        } else if (rsmPos == 8) {
            rsmPos = 4;
        }
        if (spPos == 1) {
            fromSP = false;
            salesProfile = null;
            tviR1StateName.setText("");
            spPos = 0;
        } else if (spPos == 2) {
            spPos = 1;
        } else if (spPos == 4) {
            spPos = 2;
        } else if (spPos == 8) {
            spPos = 4;
        }
        if (soPos == 1) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
        } else if (soPos == 2) {
            soPos = 1;
        } else if (soPos == 4) {
            soPos = 2;
        } else if (soPos == 8) {
            soPos = 4;
        }
        if (pPos == 1) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        } else if (pPos == 2) {
            pPos = 1;
        } else if (pPos == 4) {
            pPos = 2;
        } else if (pPos == 8) {
            pPos = 4;
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR2Close)
    public void r2Close() {
        if (rsmPos == 2) {
            fromRSM = false;
            rsmProfile = null;
            tviR1StateName.setText("");
            rsmPos = 0;
        } else if (rsmPos == 4) {
            rsmPos = 2;
        } else if (rsmPos == 8) {
            rsmPos = 4;
        }
        if (spPos == 2) {
            spPos = 0;
            fromSP = false;
            salesProfile = null;
        } else if (spPos == 4) {
            spPos = 2;
        } else if (spPos == 8) {
            spPos = 4;
        }
        if (soPos == 2) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
        } else if (soPos == 4) {
            soPos = 2;
        } else if (soPos == 8) {
            soPos = 4;
        }
        if (pPos == 2) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        } else if (pPos == 4) {
            pPos = 2;
        } else if (pPos == 8) {
            pPos = 4;
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR3Close)
    public void r3Close() {
        if (rsmPos == 4) {
            fromRSM = false;
            rsmProfile = null;
            tviR1StateName.setText("");
            rsmPos = 0;
        } else if (rsmPos == 8) {
            rsmPos = 4;
        }
        if (spPos == 4) {
            spPos = 0;
            fromSP = false;
            salesProfile = null;
        } else if (spPos == 8) {
            spPos = 4;
        }
        if (soPos == 4) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
        } else if (soPos == 8) {
            soPos = 4;
        }
        if (pPos == 4) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        } else if (pPos == 8) {
            pPos = 4;
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR4Close)
    public void r4Close() {
        if (rsmPos == 8) {
            fromRSM = false;
            rsmProfile = null;
            tviR1StateName.setText("");
            rsmPos = 0;
        }
        if (spPos == 8) {
            spPos = 0;
            fromSP = false;
            salesProfile = null;
        }
        if (soPos == 8) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
        }
        if (pPos == 8) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromRSM || fromSP || fromSO || fromProduct) {
            cviSPHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = rsmPos + spPos + soPos + pPos;

        if (totalPosition == 15) {
            row4Display();
        } else if (totalPosition == 7) {
            row3Display();
        } else if (totalPosition == 3) {
            row2Display();
        } else if (totalPosition == 1) {
            iviR1Close.setVisibility(View.GONE);
            row1Display();
        }
        String rsm = "", sales = "", invoiceNo = "", product = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTmc();
        if (null != salesProfile)
            sales = salesProfile.getTmc();
        if (null != soProfile)
            invoiceNo = soProfile.getSoNumber();
        if (null != productProfile)
            product = productProfile.getCode();
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Customer", rsm, sales, "", "", invoiceNo, ""));
        BackgroundExecutor.getInstance().execute(new KSalesOpenOrderAprRequester(userId, level, "Customer", rsm, sales, "", "", invoiceNo, product));

    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);

        if (rsmPos == 1) {
            pos = rsmProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR1Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));

        } else if (spPos == 1) {
            pos = salesProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR1Name.setText(salesProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSoAmount()));
        } else if (soPos == 1) {
            pos = soProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR1Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
        } else if (pPos == 1) {
            pos = productProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR1Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);

        if (rsmPos == 2) {
            pos = rsmProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR2Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 2) {
            pos = salesProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR2Name.setText(salesProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSoAmount()));
            //tviTarget.setText(KBAMUtils.getRoundOffValue(salesProfile.getYTDTarget()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(salesProfile.getYTD()));
            //tviAch.setText(salesProfile.getYTDPercentage().intValue() + "%");
        } else if (soPos == 2) {
            pos = soProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR2Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
            //tviTarget.setText(KBAMUtils.getRoundOffValue(soProfile.getYTDTarget()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(soProfile.getYTD()));
            //tviAch.setText(soProfile.getYTDPercentage().intValue() + "%");
        } else if (pPos == 2) {
            pos = productProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR2Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
        } else if (soPos == 1) {
            tviR1Name.setText(soProfile.getSoNumber());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        }
    }

    private void row3Display() {
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 4) {
            pos = rsmProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR3Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 4) {
            pos = salesProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR3Name.setText(salesProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSoAmount()));
        } else if (soPos == 4) {
            pos = soProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR3Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
        } else if (pPos == 4) {
            pos = productProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR3Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (spPos == 2) {
            tviR2Name.setText(salesProfile.getName());
        } else if (soPos == 2) {
            tviR2Name.setText(soProfile.getSoNumber());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
        } else if (soPos == 1) {
            tviR1Name.setText(soProfile.getSoNumber());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        }
    }

    private void row4Display() {
        if (rsmPos == 8) {
            pos = rsmProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR4Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 8) {
            pos = salesProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR4Name.setText(salesProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSoAmount()));
        } else if (soPos == 8) {
            pos = soProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR4Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
        } else if (pPos == 8) {
            pos = productProfile.getPosition();
            if (pos == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }
            tviR4Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }

        if (rsmPos == 4) {
            tviR3Name.setText(rsmProfile.getName());
        } else if (spPos == 4) {
            tviR3Name.setText(salesProfile.getName());
        } else if (soPos == 4) {
            tviR3Name.setText(soProfile.getSoNumber());
        } else if (pPos == 4) {
            tviR3Name.setText(productProfile.getProductName());
        }
        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (spPos == 2) {
            tviR2Name.setText(salesProfile.getName());
        } else if (soPos == 2) {
            tviR2Name.setText(soProfile.getSoNumber());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        }

        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
        } else if (soPos == 1) {
            tviR1Name.setText(soProfile.getSoNumber());
        }
    }

    private void initData() {
        //adapter = new OSOCustomerAdapter(dashboardActivityContext, userId, level, model, fromRSM, fromSP, fromSO);
        adapter = new KPSOCustomerAdapter(dashboardActivityContext, level, customerDataList, fromRSM, fromSP, fromSO, fromProduct);
        rviRSM.setAdapter(adapter);
    }
}
