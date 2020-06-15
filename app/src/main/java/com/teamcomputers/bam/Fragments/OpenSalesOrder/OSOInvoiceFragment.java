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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters.KPSOSOAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.WSModels.NRModels.MinMaxModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOCustomerModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOProductModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSORSMModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOSOModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.PSOFilter;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.WSRequesters.KAccountReceivablesJunRequester;
import com.teamcomputers.bam.Requesters.WSRequesters.KInvoiceLoadMoreRequester;
import com.teamcomputers.bam.Requesters.WSRequesters.KSOLoadMoreRequester;
import com.teamcomputers.bam.Requesters.WSRequesters.KSalesOpenOrderJunRequester;
import com.teamcomputers.bam.Requesters.WSRequesters.KSalesOpenOrderSearchRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.KBAMUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class OSOInvoiceFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String STATE_CODE = "STATE_CODE";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    public static final String RSM_POS = "RSM_POS";
    public static final String SP_POS = "SP_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";
    public static final String PRODUCT_POS = "PRODUCT_POS";

    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String userId = "", level, minAmount = "", maxAmount = "", minNOD = "", maxNOD = "";
    boolean fromRSM, fromSP, fromCustomer, fromProduct, search = false, isLoading = false;
    String toolbarTitle = "";
    @BindView(R.id.txtSearch)
    EditText txtSearch;
    @BindView(R.id.cviProductHeading)
    CardView cviProductHeading;
    @BindView(R.id.llProductLayout)
    LinearLayout llProductLayout;
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
    @BindView(R.id.iviR1Close)
    ImageView iviR1Close;
    @BindView(R.id.tviR1StateName)
    TextView tviR1StateName;
    @BindView(R.id.tviR2StateName)
    TextView tviR2StateName;
    @BindView(R.id.tviR3StateName)
    TextView tviR3StateName;
    @BindView(R.id.tviR4StateName)
    TextView tviR4StateName;
    @BindView(R.id.tviSOAmount)
    TextView tviSOAmount;
    /*@BindView(R.id.tviYtdHeading)
    TextView tviYtdHeading;
    @BindView(R.id.tviQtdHeading)
    TextView tviQtdHeading;
    @BindView(R.id.tviMtdHeading)
    TextView tviMtdHeading;*/
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private KPSOSOAdapter adapter;
    private int position = 0, stateCode = 0, rsmPos = 0, spPos = 0, cPos = 0, iPos = 0, pPos = 0, nextLimit = 0;

    KPSOSOModel sOData;
    KPSOSOModel.Datum selectedSOData;
    PSOFilter sOFilterData;
    MinMaxModel minMaxData;
    List<KPSOSOModel.Datum> sODataList = new ArrayList<>();
    KPSOCustomerModel.Datum customerProfile;
    KPSORSMModel.Datum rsmProfile, spProfile;
    KPSOProductModel.Datum productProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_oso_invoice, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);

        rsmPos = getArguments().getInt(RSM_POS);
        spPos = getArguments().getInt(SP_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);
        pPos = getArguments().getInt(PRODUCT_POS);

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromSP = getArguments().getBoolean(FROM_SP);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);

        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        spProfile = getArguments().getParcelable(SP_PROFILE);
        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        stateCode = getArguments().getInt(STATE_CODE);
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);

        toolbarTitle = getString(R.string.SO);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        rowsDisplay();

        dashboardActivityContext.fragmentView = rootView;

        rviRSM.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == sODataList.size() - 1) {
                        //bottom of list!
                        loadMore();
                    }
                }
            }
        });

        return rootView;
    }

    private void loadMore() {
        sODataList.add(null);
        adapter.notifyItemInserted(sODataList.size() - 1);
        String rsm = "", sales = "", customer = "", state = "", product = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTmc();
        if (null != spProfile)
            sales = spProfile.getTmc();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        if (stateCode == 1)
            state = customerProfile.getStateCodeWise().get(0).getStateCode();
        if (null != productProfile)
            product = productProfile.getCode();
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new KSOLoadMoreRequester(userId, level, "SONumber", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", minAmount, maxAmount, minNOD, maxNOD));
        isLoading = true;
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
        dashboardActivityContext.OSOSOClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return OSOInvoiceFragment.class.getSimpleName();
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
                    case Events.GET_INVOICE_OSO_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            /*JSONArray jsonArray = new JSONArray(KBAMUtils.replaceDataResponse(eventObject.getObject().toString()));
                            OSOInvoiceModel[] data = (OSOInvoiceModel[]) KBAMUtils.fromJson(String.valueOf(jsonArray), OSOInvoiceModel[].class);
                            model = new ArrayList<OSOInvoiceModel>(Arrays.asList(data));*/
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceTOSInvoiceDataResponse(eventObject.getObject().toString()));
                            sOData = (KPSOSOModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KPSOSOModel.class);
                            sODataList = sOData.getData();
                            for (int i = 0; i < sODataList.size(); i++) {
                                if (sODataList.get(i).getSoNumber().equals("") || sODataList.get(i).getSoNumber().equals("null")) {
                                    sODataList.remove(i);
                                }
                            }
                            sOFilterData = sOData.getFilter();
                            minMaxData = sOData.getMinMax();
                            tviSOAmount.setText(KBAMUtils.getRoundOffValue(sOFilterData.getSoAmount()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
                        dismissProgress();
                        break;
                    case Events.GET_INVOICE_OSO_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SO_LOAD_MORE_SUCCESSFULL:
                        sODataList.remove(sODataList.size() - 1);
                        int scrollPosition = sODataList.size();
                        adapter.notifyItemRemoved(scrollPosition);
                        int currentSize = scrollPosition;
                        nextLimit = currentSize + 10;
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceTOSInvoiceDataResponse(eventObject.getObject().toString()));
                            sOData = (KPSOSOModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KPSOSOModel.class);
                            sODataList.addAll(sOData.getData());
                            for (int i = 0; i < sODataList.size(); i++) {
                                if (sODataList.get(i).getSoNumber().equals("") || sODataList.get(i).getSoNumber().equals("null")) {
                                    sODataList.remove(i);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                        dismissProgress();
                        break;
                    case Events.GET_SO_LOAD_MORE_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SO_SERACH_SUCCESSFULL:
                        try {
                            JSONArray jsonArray = new JSONArray(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()));
                            //invoiceDataList.clear();
                            sODataList = KBAMUtils.convertArrayToList((KPSOSOModel.Datum[]) KBAMUtils.fromJson(String.valueOf(jsonArray), KPSOSOModel.Datum[].class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        initData();
                        showToast("Baised on top 1000 Rows");
                        //adapter.notifyDataSetChanged();
                        break;
                    case Events.GET_SO_SERACH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.SO_ITEM_SELECT:
                        selectedSOData = (KPSOSOModel.Datum) eventObject.getObject();
                        Bundle soDataBundle = new Bundle();
                        soDataBundle.putString(OSOProductFragment.USER_ID, userId);
                        soDataBundle.putString(OSOProductFragment.USER_LEVEL, level);

                        iPos = rsmPos + spPos + cPos + 1;
                        soDataBundle.putInt(OSOProductFragment.RSM_POS, rsmPos);
                        soDataBundle.putInt(OSOProductFragment.SP_POS, spPos);
                        soDataBundle.putInt(OSOProductFragment.CUSTOMER_POS, cPos);
                        soDataBundle.putInt(OSOProductFragment.SO_POS, iPos);

                        soDataBundle.putBoolean(OSOProductFragment.FROM_RSM, fromRSM);
                        soDataBundle.putBoolean(OSOProductFragment.FROM_SP, fromSP);
                        soDataBundle.putBoolean(OSOProductFragment.FROM_CUSTOMER, fromCustomer);
                        soDataBundle.putBoolean(OSOProductFragment.FROM_SO, true);

                        soDataBundle.putParcelable(OSOProductFragment.RSM_PROFILE, rsmProfile);
                        soDataBundle.putParcelable(OSOProductFragment.SP_PROFILE, spProfile);
                        soDataBundle.putParcelable(OSOProductFragment.CUSTOMER_PROFILE, customerProfile);
                        soDataBundle.putParcelable(OSOProductFragment.SO_PROFILE, selectedSOData);
                        soDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_PRODUCT_FRAGMENT, soDataBundle);
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        selectedSOData = (KPSOSOModel.Datum) eventObject.getObject();
                        Bundle rsmMenuDataBundle = new Bundle();
                        rsmMenuDataBundle.putString(OSORSMFragment.USER_ID, userId);
                        rsmMenuDataBundle.putString(OSORSMFragment.USER_LEVEL, level);

                        iPos = spPos + cPos + pPos + 1;

                        rsmMenuDataBundle.putInt(OSORSMFragment.CUSTOMER_POS, cPos);
                        rsmMenuDataBundle.putInt(OSORSMFragment.SP_POS, spPos);
                        rsmMenuDataBundle.putInt(OSORSMFragment.SO_POS, iPos);
                        rsmMenuDataBundle.putInt(OSORSMFragment.PRODUCT_POS, pPos);

                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_SP, fromRSM);
                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_CUSTOMER, fromCustomer);
                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_SO, true);
                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_PRODUCT, fromProduct);

                        rsmMenuDataBundle.putParcelable(OSORSMFragment.SP_PROFILE, spProfile);
                        rsmMenuDataBundle.putParcelable(OSORSMFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmMenuDataBundle.putParcelable(OSORSMFragment.SO_PROFILE, selectedSOData);
                        rsmMenuDataBundle.putParcelable(OSORSMFragment.PRODUCT_PROFILE, productProfile);

                        rsmMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_RSM_FRAGMENT, rsmMenuDataBundle);
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        selectedSOData = (KPSOSOModel.Datum) eventObject.getObject();
                        Bundle productDataBundle = new Bundle();
                        productDataBundle.putString(OSOSalesPersonFragment.USER_ID, userId);
                        productDataBundle.putString(OSOSalesPersonFragment.USER_LEVEL, level);

                        iPos = rsmPos + cPos + pPos + 1;

                        productDataBundle.putInt(OSOSalesPersonFragment.RSM_POS, rsmPos);
                        productDataBundle.putInt(OSOSalesPersonFragment.CUSTOMER_POS, cPos);
                        productDataBundle.putInt(OSOSalesPersonFragment.SO_POS, iPos);
                        productDataBundle.putInt(OSOSalesPersonFragment.PRODUCT_POS, pPos);

                        productDataBundle.putBoolean(OSOSalesPersonFragment.FROM_RSM, fromRSM);
                        productDataBundle.putBoolean(OSOSalesPersonFragment.FROM_CUSTOMER, fromCustomer);
                        productDataBundle.putBoolean(OSOSalesPersonFragment.FROM_SO, true);
                        productDataBundle.putBoolean(OSOSalesPersonFragment.FROM_PRODUCT, fromProduct);

                        productDataBundle.putParcelable(OSOSalesPersonFragment.RSM_PROFILE, rsmProfile);
                        productDataBundle.putParcelable(OSOSalesPersonFragment.CUSTOMER_PROFILE, customerProfile);
                        productDataBundle.putParcelable(OSOSalesPersonFragment.SO_PROFILE, selectedSOData);
                        productDataBundle.putParcelable(OSOSalesPersonFragment.PRODUCT_PROFILE, productProfile);

                        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, productDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        selectedSOData = (KPSOSOModel.Datum) eventObject.getObject();
                        Bundle spDataBundle = new Bundle();
                        spDataBundle.putString(OSOCustomerFragment.USER_ID, userId);
                        spDataBundle.putString(OSOCustomerFragment.USER_LEVEL, level);

                        iPos = rsmPos + spPos + pPos + 1;
                        spDataBundle.putInt(OSOCustomerFragment.RSM_POS, rsmPos);
                        spDataBundle.putInt(OSOCustomerFragment.SP_POS, spPos);
                        spDataBundle.putInt(OSOCustomerFragment.SO_POS, iPos);
                        spDataBundle.putInt(OSOCustomerFragment.PRODUCT_POS, pPos);

                        spDataBundle.putBoolean(OSOCustomerFragment.FROM_RSM, fromRSM);
                        spDataBundle.putBoolean(OSOCustomerFragment.FROM_SP, fromSP);
                        spDataBundle.putBoolean(OSOCustomerFragment.FROM_SO, true);
                        spDataBundle.putBoolean(OSOCustomerFragment.FROM_PRODUCT, fromProduct);

                        spDataBundle.putParcelable(OSOCustomerFragment.RSM_PROFILE, rsmProfile);
                        spDataBundle.putParcelable(OSOCustomerFragment.SP_PROFILE, spProfile);
                        spDataBundle.putParcelable(OSOCustomerFragment.SO_PROFILE, selectedSOData);
                        spDataBundle.putParcelable(OSOCustomerFragment.PRODUCT_PROFILE, productProfile);

                        spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, spDataBundle);
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
        //adapter.getFilter().filter(txtSearch.getText().toString());
        if (txtSearch.getText().toString().length() > 2) {
            isLoading = true;
            showProgress(ProgressDialogTexts.LOADING);
            BackgroundExecutor.getInstance().execute(new KSalesOpenOrderSearchRequester(userId, level, txtSearch.getText().toString()));
        } else if (txtSearch.getText().toString().length() == 0) {
            isLoading = false;
            KBAMUtils.hideSoftKeyboard(dashboardActivityContext);
            String rsm = "", sales = "", customer = "", state = "", product = "";
            if (null != rsmProfile)
                rsm = rsmProfile.getTmc();
            if (null != spProfile)
                sales = spProfile.getTmc();
            if (null != customerProfile)
                customer = customerProfile.getCustomerName();
            if (stateCode == 1)
                state = customerProfile.getStateCodeWise().get(0).getStateCode();
            if (null != productProfile)
                product = productProfile.getCode();
            showProgress(ProgressDialogTexts.LOADING);
            BackgroundExecutor.getInstance().execute(new KSalesOpenOrderJunRequester(userId, level, "SONumber", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", "", "", "", ""));
        }
    }

    @OnClick(R.id.iviFilter)
    public void filter() {
        showFilterDialog();
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
        cPos = 0;
        fromRSM = false;
        fromSP = false;
        fromCustomer = false;
        rsmProfile = null;
        spProfile = null;
        customerProfile = null;
        cviProductHeading.setVisibility(View.GONE);
        /*tviYtdHeading.setText("TARGET");
        tviQtdHeading.setText("ACTUAL");
        tviMtdHeading.setText("ACH%");*/
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Invoice", "", "", "", "", "", ""));
        //BackgroundExecutor.getInstance().execute(new KSalesOpenOrderAprRequester(userId, level, "SONumber", "", "", "", "", "", ""));
        BackgroundExecutor.getInstance().execute(new KSalesOpenOrderJunRequester(userId, level, "SONumber", "", "", "", "", "", "", String.valueOf(nextLimit), "50", "", "", "", ""));
    }

    @OnClick(R.id.iviR1Close)
    public void r1Close() {
        if (rsmPos == 1) {
            fromRSM = false;
            rsmProfile = null;
            rsmPos = 0;
            if (spPos == 2) {
                spPos = 1;
            } else if (spPos == 4) {
                spPos = 2;
            } else if (spPos == 8) {
                spPos = 4;
            }
            if (cPos == 2) {
                cPos = 1;
            } else if (cPos == 4) {
                cPos = 2;
            } else if (cPos == 8) {
                cPos = 4;
            }
            if (pPos == 2) {
                pPos = 1;
            } else if (pPos == 4) {
                pPos = 2;
            } else if (pPos == 8) {
                pPos = 4;
            }
        } else if (spPos == 1) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
            if (rsmPos == 2) {
                rsmPos = 1;
            } else if (rsmPos == 4) {
                rsmPos = 2;
            } else if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (cPos == 2) {
                cPos = 1;
            } else if (cPos == 4) {
                cPos = 2;
            } else if (cPos == 8) {
                cPos = 4;
            }
            if (pPos == 2) {
                pPos = 1;
            } else if (pPos == 4) {
                pPos = 2;
            } else if (pPos == 8) {
                pPos = 4;
            }
        } else if (cPos == 1) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
            if (rsmPos == 2) {
                rsmPos = 1;
            } else if (rsmPos == 4) {
                rsmPos = 2;
            } else if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (spPos == 2) {
                spPos = 1;
            } else if (spPos == 4) {
                spPos = 2;
            } else if (spPos == 8) {
                spPos = 4;
            }
            if (pPos == 2) {
                pPos = 1;
            } else if (pPos == 4) {
                pPos = 2;
            } else if (pPos == 8) {
                pPos = 4;
            }
        } else if (pPos == 1) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
            if (rsmPos == 2) {
                rsmPos = 1;
            } else if (rsmPos == 4) {
                rsmPos = 2;
            } else if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (spPos == 2) {
                spPos = 1;
            } else if (spPos == 4) {
                spPos = 2;
            } else if (spPos == 8) {
                spPos = 4;
            }
            if (cPos == 2) {
                cPos = 1;
            } else if (cPos == 4) {
                cPos = 2;
            } else if (cPos == 8) {
                cPos = 4;
            }
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR2Close)
    public void r2Close() {
        if (rsmPos == 2) {
            fromRSM = false;
            rsmProfile = null;
            rsmPos = 0;
            if (spPos == 4) {
                spPos = 2;
            } else if (spPos == 8) {
                spPos = 4;
            }
            if (cPos == 4) {
                cPos = 2;
            } else if (cPos == 8) {
                cPos = 4;
            }
            if (pPos == 4) {
                pPos = 2;
            } else if (pPos == 8) {
                pPos = 4;
            }
        } else if (spPos == 2) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
            if (rsmPos == 4) {
                rsmPos = 2;
            } else if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (cPos == 4) {
                cPos = 2;
            } else if (cPos == 8) {
                cPos = 4;
            }
            if (pPos == 4) {
                pPos = 2;
            } else if (pPos == 8) {
                pPos = 4;
            }
        } else if (cPos == 2) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
            if (rsmPos == 4) {
                rsmPos = 2;
            } else if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (spPos == 4) {
                spPos = 2;
            } else if (spPos == 8) {
                spPos = 4;
            }
            if (pPos == 4) {
                pPos = 2;
            } else if (pPos == 8) {
                pPos = 4;
            }
        } else if (pPos == 2) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
            if (rsmPos == 4) {
                rsmPos = 2;
            } else if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (spPos == 4) {
                spPos = 2;
            } else if (spPos == 8) {
                spPos = 4;
            }
            if (cPos == 4) {
                cPos = 2;
            } else if (cPos == 8) {
                cPos = 4;
            }
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR3Close)
    public void r3Close() {
        if (rsmPos == 4) {
            fromRSM = false;
            rsmProfile = null;
            rsmPos = 0;
            if (spPos == 8) {
                spPos = 4;
            }
            if (cPos == 8) {
                cPos = 4;
            }
            if (pPos == 8) {
                pPos = 4;
            }
        } else if (spPos == 4) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
            if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (cPos == 8) {
                cPos = 4;
            }
            if (pPos == 8) {
                pPos = 4;
            }
        } else if (cPos == 4) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
            if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (spPos == 8) {
                spPos = 4;
            }
            if (pPos == 8) {
                pPos = 4;
            }
        } else if (pPos == 4) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
            if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (spPos == 8) {
                spPos = 4;
            }
            if (cPos == 8) {
                cPos = 4;
            }
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR4Close)
    public void r4Close() {
        if (rsmPos == 8) {
            fromRSM = false;
            rsmProfile = null;
            rsmPos = 0;
        } else if (spPos == 8) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
        } else if (cPos == 8) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
        } else if (pPos == 8) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        }
        rowsDisplay();
    }


    private void rowsDisplay() {
        if (fromRSM || fromSP || fromCustomer || fromProduct) {
            cviProductHeading.setVisibility(View.VISIBLE);
        } else {
            cviProductHeading.setVisibility(View.GONE);
        }
        int totalPosition = rsmPos + spPos + cPos + pPos;

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

        String rsm = "", sales = "", customer = "", state = "", product = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTmc();
        if (null != spProfile)
            sales = spProfile.getTmc();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        if (stateCode == 1)
            state = customerProfile.getStateCodeWise().get(0).getStateCode();
        if (null != productProfile)
            product = productProfile.getCode();
        //tviR3StateName.setText(state);
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Invoice", rsm, sales, customer, state, "", ""));
        //BackgroundExecutor.getInstance().execute(new KSalesOpenOrderAprRequester(userId, level, "SONumber", rsm, sales, customer, state, "", product));
        BackgroundExecutor.getInstance().execute(new KSalesOpenOrderJunRequester(userId, level, "SONumber", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", "", "", "", ""));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 1) {
            position = rsmProfile.getPosition();
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
            tviR1Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 1) {
            position = spProfile.getPosition();
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
            tviR1Name.setText(spProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSoAmount()));
        } else if (cPos == 1) {
            position = customerProfile.getPosition();
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
            tviR1Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR1StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
        } else if (pPos == 1) {
            position = productProfile.getPosition();
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
            tviR1Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 2) {
            position = rsmProfile.getPosition();
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
            tviR2Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 2) {
            position = spProfile.getPosition();
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
            tviR2Name.setText(spProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSoAmount()));
        } else if (cPos == 4) {
            position = customerProfile.getPosition();
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
            tviR2Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR2StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
        } else if (pPos == 2) {
            position = productProfile.getPosition();
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
            tviR2Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSOAmount()));
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }
    }

    private void row3Display() {
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 4) {
            position = rsmProfile.getPosition();
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
            tviR3Name.setText(rsmProfile.getName());
        } else if (spPos == 4) {
            position = spProfile.getPosition();
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
            tviR3Name.setText(spProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSoAmount()));
        } else if (cPos == 4) {
            position = customerProfile.getPosition();
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
            tviR3Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR3StateName.setVisibility(View.VISIBLE);
                tviR3StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR3StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
        } else if (pPos == 4) {
            position = productProfile.getPosition();
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
            tviR3Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSoAmount()));
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSOAmount()));
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }
    }

    private void row4Display() {
        if (rsmPos == 8) {
            position = rsmProfile.getPosition();
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
            tviR4Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 8) {
            position = spProfile.getPosition();
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
            tviR4Name.setText(spProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSoAmount()));
        } else if (cPos == 8) {
            position = customerProfile.getPosition();
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
            tviR4Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR4StateName.setVisibility(View.VISIBLE);
                tviR4StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR4StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
            //tviTarget.setText(KBAMUtils.getRoundOffValue(customerProfile.getYTD()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(customerProfile.getQTD()));
            //tviAch.setText(KBAMUtils.getRoundOffValue(customerProfile.getMTD()));
        } else if (pPos == 8) {
            position = productProfile.getPosition();
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
            tviR4Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }

        if (rsmPos == 4) {
            tviR3Name.setText(rsmProfile.getName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSOAmount()));
        } else if (spPos == 4) {
            tviR3Name.setText(spProfile.getName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 4) {
            tviR3Name.setText(customerProfile.getCustomerName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR3StateName.setVisibility(View.VISIBLE);
                tviR3StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR3StateName.setVisibility(View.GONE);
            }
        } else if (pPos == 4) {
            tviR3Name.setText(productProfile.getProductName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSOAmount()));
        } else if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }
    }

    AlertDialog alertDialog;

    public void showFilterDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(dashboardActivityContext);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.so_filter_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        TextView tviDialogType = (TextView) dialogView.findViewById(R.id.tviDialogType);
        ImageView iviCloseDialogType = (ImageView) dialogView.findViewById(R.id.iviCloseDialogType);
        CrystalRangeSeekbar rangeSeekbarOutstanding = (CrystalRangeSeekbar) dialogView.findViewById(R.id.rangeSeekbarOutstanding);
        CrystalRangeSeekbar rangeSeekbarNOD = (CrystalRangeSeekbar) dialogView.findViewById(R.id.rangeSeekbarNOD);

        EditText txtMinOutstanding = (EditText) dialogView.findViewById(R.id.txtMinOutstanding);
        EditText txtMaxOutstanding = (EditText) dialogView.findViewById(R.id.txtMaxOutstanding);

        EditText txtMinNOD = (EditText) dialogView.findViewById(R.id.txtMinNOD);
        EditText txtMaxNOD = (EditText) dialogView.findViewById(R.id.txtMaxNOD);

        rangeSeekbarOutstanding.setMinValue(minMaxData.getMinAmount());
        rangeSeekbarOutstanding.setMaxValue(minMaxData.getMaxAmount());

        TextView tviApply = (TextView) dialogView.findViewById(R.id.tviApply);
        TextView tviClear = (TextView) dialogView.findViewById(R.id.tviClear);

        tviDialogType.setText("Apply Filter");

        // set listener
        rangeSeekbarOutstanding.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                txtMinOutstanding.setText(String.valueOf(minValue));
                txtMaxOutstanding.setText(String.valueOf(maxValue));
            }
        });

        // set listener
        rangeSeekbarNOD.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                txtMinNOD.setText(String.valueOf(minValue));
                txtMaxNOD.setText(String.valueOf(maxValue));
            }
        });

        tviApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                String rsm = "", sales = "", customer = "", state = "", product = "";
                if (null != rsmProfile)
                    rsm = rsmProfile.getTmc();
                if (null != spProfile)
                    sales = spProfile.getTmc();
                if (null != customerProfile)
                    customer = customerProfile.getCustomerName();
                if (null != txtMinOutstanding.getText().toString())
                    minAmount = txtMinOutstanding.getText().toString();
                if (null != txtMaxOutstanding.getText().toString())
                    maxAmount = txtMaxOutstanding.getText().toString();
                if (null != txtMinNOD.getText().toString())
                    minNOD = txtMinNOD.getText().toString();
                if (null != txtMaxNOD.getText().toString())
                    maxNOD = txtMaxNOD.getText().toString();
                showProgress(ProgressDialogTexts.LOADING);
                BackgroundExecutor.getInstance().execute(new KSalesOpenOrderJunRequester(userId, level, "SONumber", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", minAmount, maxAmount, minNOD, maxNOD));
            }
        });
        tviClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                String rsm = "", sales = "", customer = "", state = "", product = "";
                if (null != rsmProfile)
                    rsm = rsmProfile.getTmc();
                if (null != spProfile)
                    sales = spProfile.getTmc();
                if (null != customerProfile)
                    customer = customerProfile.getCustomerName();
                minAmount = "";
                maxAmount = "";
                minNOD = "";
                maxNOD = "";
                showProgress(ProgressDialogTexts.LOADING);
                BackgroundExecutor.getInstance().execute(new KSalesOpenOrderJunRequester(userId, level, "SONumber", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", minAmount, maxAmount, minNOD, maxNOD));
            }
        });

        iviCloseDialogType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void initData() {
        //adapter = new OSOInvoiceAdapter(dashboardActivityContext, level, type, model, fromRSM, fromSP, fromCustomer);
        adapter = new KPSOSOAdapter(dashboardActivityContext, level, sODataList, fromRSM, fromSP, fromCustomer, fromProduct);
        rviRSM.setAdapter(adapter);
    }
}
