package com.teamcomputers.bam.Fragments.Outstanding;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters.KTOInvoiceAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.WSModels.NRModels.Filter;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRCustomerModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRInvoiceModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRProductModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRRSMModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.MinMaxModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.WSRequesters.KAccountReceivablesJunRequester;
import com.teamcomputers.bam.Requesters.WSRequesters.KInvoiceLoadMoreRequester;
import com.teamcomputers.bam.Requesters.WSRequesters.KInvoiceSearchRequester;
import com.teamcomputers.bam.Utils.KBAMUtils;
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

public class TOSInvoiceFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";

    public static final String STATE_CODE = "STATE_CODE";

    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";

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

    String toolbarTitle = "";
    @BindView(R.id.txtSearch)
    EditText txtSearch;
    @BindView(R.id.tviTotalOutstanding)
    TextView tviTotalOutstanding;
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
    @BindView(R.id.tviAmount)
    TextView tviAmount;
    @BindView(R.id.tviDSO)
    TextView tviDSO;
    @BindView(R.id.pBar)
    ProgressBar pBar;
    @BindView(R.id.llDSO)
    LinearLayout llDSO;

    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private KTOInvoiceAdapter adapter;
    boolean fromRSM, fromSP, fromCustomer, fromProduct, search = false, isLoading = false;

    private int position = 0, bar = 0, stateCode = 0, rsmPos = 0, spPos = 0, cPos = 0, pPos = 0, iPos = 0, nextLimit = 0;

    KNRCustomerModel.Datum customerProfile;
    KNRProductModel.Datum productProfile;
    KNRRSMModel.Datum rsmProfile, spProfile;
    KNRInvoiceModel invoiceData;
    List<KNRInvoiceModel.Datum> invoiceDataList = new ArrayList<>();
    Filter invoiceFilterData;
    MinMaxModel minMaxData;
    KNRInvoiceModel.Datum selectedInvoiceData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_to_product, container, false);
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
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);
        stateCode = getArguments().getInt(STATE_CODE);

        toolbarTitle = getString(R.string.Invoice);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        isLoading = false;

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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == invoiceDataList.size() - 1) {
                        //bottom of list!
                        loadMore();
                    }
                }
            }
        });

        return rootView;
    }

    private void loadMore() {
        invoiceDataList.add(null);
        adapter.notifyItemInserted(invoiceDataList.size() - 1);
        String rsm = "", sales = "", customer = "", state = "", product = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTmc();
        if (null != spProfile)
            sales = spProfile.getTmc();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        //BackgroundExecutor.getInstance().execute(new KInvoiceLoadMoreRequester(userId, level, "Invoice", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50"));
        BackgroundExecutor.getInstance().execute(new KInvoiceLoadMoreRequester(userId, level, "Invoice", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", minAmount, maxAmount, minNOD, maxNOD));
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
        dashboardActivityContext.TOSInvoiceClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return TOSInvoiceFragment.class.getSimpleName();
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
                    case Events.GET_INVOICE_TOS_LIST_SUCCESSFULL:
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceTOSInvoiceDataResponse(eventObject.getObject().toString()));
                            invoiceData = (KNRInvoiceModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KNRInvoiceModel.class);
                            invoiceDataList = invoiceData.getData();
                            for (int i = 0; i < invoiceDataList.size(); i++) {
                                if (invoiceDataList.get(i).getDocumentNo().equals("") || invoiceDataList.get(i).getDocumentNo().equals("null")) {
                                    invoiceDataList.remove(i);
                                }
                            }
                            invoiceFilterData = invoiceData.getFilter();
                            minMaxData = invoiceData.getMinMax();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        isLoading = false;
                        //adapter.notifyDataSetChanged();
                        initData();
                        break;
                    case Events.GET_INVOICE_TOS_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_INVOICE_LOAD_MORE_SUCCESSFULL:
                        invoiceDataList.remove(invoiceDataList.size() - 1);
                        int scrollPosition = invoiceDataList.size();
                        adapter.notifyItemRemoved(scrollPosition);
                        int currentSize = scrollPosition;
                        nextLimit = currentSize + 10;
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceTOSInvoiceDataResponse(eventObject.getObject().toString()));
                            invoiceData = (KNRInvoiceModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KNRInvoiceModel.class);
                            invoiceDataList.addAll(invoiceData.getData());
                            for (int i = 0; i < invoiceDataList.size(); i++) {
                                if (invoiceDataList.get(i).getDocumentNo().equals("") || invoiceDataList.get(i).getDocumentNo().equals("null")) {
                                    invoiceDataList.remove(i);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                        dismissProgress();
                        break;
                    case Events.GET_INVOICE_LOAD_MORE_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_INVOICE_SERACH_SUCCESSFULL:
                        try {
                            JSONArray jsonArray = new JSONArray(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()));
                            //invoiceDataList.clear();
                            invoiceDataList = KBAMUtils.convertArrayToList((KNRInvoiceModel.Datum[]) KBAMUtils.fromJson(String.valueOf(jsonArray), KNRInvoiceModel.Datum[].class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        displayList();
                        showToast("Baised on top 1000 Rows");
                        //adapter.notifyDataSetChanged();
                        break;
                    case Events.GET_INVOICE_SERACH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        selectedInvoiceData = (KNRInvoiceModel.Datum) eventObject.getObject();
                        Bundle rsmMenuDataBundle = new Bundle();
                        rsmMenuDataBundle.putString(TOSRSMFragment.USER_ID, userId);
                        rsmMenuDataBundle.putString(TOSRSMFragment.USER_LEVEL, level);

                        iPos = spPos + cPos + pPos + 1;

                        rsmMenuDataBundle.putInt(TOSRSMFragment.CUSTOMER_POS, cPos);
                        rsmMenuDataBundle.putInt(TOSRSMFragment.SP_POS, spPos);
                        rsmMenuDataBundle.putInt(TOSRSMFragment.PRODUCT_POS, pPos);
                        rsmMenuDataBundle.putInt(TOSRSMFragment.INVOICE_POS, iPos);

                        rsmMenuDataBundle.putBoolean(TOSRSMFragment.FROM_SP, fromRSM);
                        rsmMenuDataBundle.putBoolean(TOSRSMFragment.FROM_CUSTOMER, fromCustomer);
                        rsmMenuDataBundle.putBoolean(TOSRSMFragment.FROM_PRODUCT, fromProduct);
                        rsmMenuDataBundle.putBoolean(TOSRSMFragment.FROM_INVOICE, true);

                        rsmMenuDataBundle.putParcelable(TOSRSMFragment.SP_PROFILE, spProfile);
                        rsmMenuDataBundle.putParcelable(TOSRSMFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmMenuDataBundle.putParcelable(TOSRSMFragment.PRODUCT_PROFILE, productProfile);
                        rsmMenuDataBundle.putParcelable(TOSRSMFragment.INVOICE_PROFILE, selectedInvoiceData);

                        rsmMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_RSM_FRAGMENT, rsmMenuDataBundle);
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        selectedInvoiceData = (KNRInvoiceModel.Datum) eventObject.getObject();
                        Bundle productDataBundle = new Bundle();
                        productDataBundle.putString(TOSSalesPersonFragment.USER_ID, userId);
                        productDataBundle.putString(TOSSalesPersonFragment.USER_LEVEL, level);

                        iPos = rsmPos + cPos + pPos + 1;

                        productDataBundle.putInt(TOSSalesPersonFragment.RSM_POS, rsmPos);
                        productDataBundle.putInt(TOSSalesPersonFragment.CUSTOMER_POS, cPos);
                        productDataBundle.putInt(TOSSalesPersonFragment.PRODUCT_POS, pPos);
                        productDataBundle.putInt(TOSSalesPersonFragment.INVOICE_POS, iPos);

                        productDataBundle.putBoolean(TOSSalesPersonFragment.FROM_RSM, fromRSM);
                        productDataBundle.putBoolean(TOSSalesPersonFragment.FROM_CUSTOMER, fromCustomer);
                        productDataBundle.putBoolean(TOSSalesPersonFragment.FROM_PRODUCT, fromProduct);
                        productDataBundle.putBoolean(TOSSalesPersonFragment.FROM_INVOICE, true);

                        productDataBundle.putParcelable(TOSSalesPersonFragment.RSM_PROFILE, rsmProfile);
                        productDataBundle.putParcelable(TOSSalesPersonFragment.CUSTOMER_PROFILE, customerProfile);
                        productDataBundle.putParcelable(TOSSalesPersonFragment.PRODUCT_PROFILE, productProfile);
                        productDataBundle.putParcelable(TOSSalesPersonFragment.INVOICE_PROFILE, selectedInvoiceData);

                        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_ACCOUNT_FRAGMENT, productDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        selectedInvoiceData = (KNRInvoiceModel.Datum) eventObject.getObject();
                        Bundle spDataBundle = new Bundle();
                        spDataBundle.putString(TOSCustomerFragment.USER_ID, userId);
                        spDataBundle.putString(TOSCustomerFragment.USER_LEVEL, level);

                        iPos = rsmPos + spPos + pPos + 1;

                        spDataBundle.putInt(TOSCustomerFragment.RSM_POS, rsmPos);
                        spDataBundle.putInt(TOSCustomerFragment.SP_POS, spPos);
                        spDataBundle.putInt(TOSCustomerFragment.PRODUCT_POS, pPos);
                        spDataBundle.putInt(TOSCustomerFragment.INVOICE_POS, iPos);

                        spDataBundle.putBoolean(TOSCustomerFragment.FROM_RSM, fromRSM);
                        spDataBundle.putBoolean(TOSCustomerFragment.FROM_SP, fromSP);
                        spDataBundle.putBoolean(TOSCustomerFragment.FROM_PRODUCT, fromProduct);
                        spDataBundle.putBoolean(TOSCustomerFragment.FROM_INVOICE, true);

                        spDataBundle.putParcelable(TOSCustomerFragment.RSM_PROFILE, rsmProfile);
                        spDataBundle.putParcelable(TOSCustomerFragment.SP_PROFILE, spProfile);
                        spDataBundle.putParcelable(TOSCustomerFragment.PRODUCT_PROFILE, productProfile);
                        spDataBundle.putParcelable(TOSCustomerFragment.INVOICE_PROFILE, selectedInvoiceData);

                        spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_CUSTOMER_FRAGMENT, spDataBundle);
                        break;
                    case ClickEvents.PRODUCT_MENU_SELECT:
                        selectedInvoiceData = (KNRInvoiceModel.Datum) eventObject.getObject();
                        Bundle customerBundle = new Bundle();
                        customerBundle.putString(TOSProductFragment.USER_ID, userId);
                        customerBundle.putString(TOSProductFragment.USER_LEVEL, level);

                        iPos = rsmPos + spPos + pPos + 1;

                        customerBundle.putInt(TOSProductFragment.RSM_POS, rsmPos);
                        customerBundle.putInt(TOSProductFragment.SP_POS, spPos);
                        customerBundle.putInt(TOSProductFragment.CUSTOMER_POS, cPos);
                        customerBundle.putInt(TOSProductFragment.INVOICE_POS, iPos);

                        customerBundle.putBoolean(TOSProductFragment.FROM_RSM, fromRSM);
                        customerBundle.putBoolean(TOSProductFragment.FROM_SP, fromSP);
                        customerBundle.putBoolean(TOSProductFragment.FROM_CUSTOMER, fromCustomer);
                        customerBundle.putBoolean(TOSProductFragment.FROM_INVOICE, true);

                        customerBundle.putParcelable(TOSProductFragment.RSM_PROFILE, rsmProfile);
                        customerBundle.putParcelable(TOSProductFragment.SP_PROFILE, spProfile);
                        customerBundle.putParcelable(TOSProductFragment.CUSTOMER_PROFILE, customerProfile);
                        customerBundle.putParcelable(TOSProductFragment.INVOICE_PROFILE, selectedInvoiceData);

                        customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_PRODUCT_FRAGMENT, customerBundle);
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

    @OnClick(R.id.iviFilter)
    public void filter() {
        showFilterDialog();
    }

    @OnTextChanged(R.id.txtSearch)
    public void search() {
        //adapter.getFilter().filter(txtSearch.getText().toString());
        if (txtSearch.getText().toString().length() > 2) {
            isLoading = true;
            showProgress(ProgressDialogTexts.LOADING);
            BackgroundExecutor.getInstance().execute(new KInvoiceSearchRequester(userId, level, txtSearch.getText().toString()));
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
            showProgress(ProgressDialogTexts.LOADING);
            //BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "Invoice", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50"));
            BackgroundExecutor.getInstance().execute(new KAccountReceivablesJunRequester(userId, level, "Invoice", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", "", "", "", ""));
        }
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
        fromRSM = false;
        fromSP = false;
        fromCustomer = false;
        rsmProfile = null;
        spProfile = null;
        customerProfile = null;
        cviProductHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Product", "", "", "", "", ""));
        //BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "Invoice", "", "", "", "", "", "", "0", "50"));
        BackgroundExecutor.getInstance().execute(new KAccountReceivablesJunRequester(userId, level, "Invoice", "", "", "", "", "", "", "0", "50", "", "", "", ""));
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
            }
            if (cPos == 2) {
                cPos = 1;
            } else if (cPos == 4) {
                cPos = 2;
            }
        } else if (spPos == 1) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
            if (rsmPos == 2) {
                rsmPos = 1;
            } else if (rsmPos == 4) {
                rsmPos = 2;
            }
            if (cPos == 2) {
                cPos = 1;
            } else if (cPos == 4) {
                cPos = 2;
            }
        } else if (cPos == 1) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
            if (stateCode == 1)
                stateCode = 0;
            if (rsmPos == 2) {
                rsmPos = 1;
            } else if (rsmPos == 4) {
                rsmPos = 2;
            }
            if (spPos == 2) {
                spPos = 1;
            } else if (spPos == 4) {
                spPos = 2;
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
            }
            if (cPos == 4) {
                cPos = 2;
            }
        } else if (spPos == 2) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
            if (rsmPos == 4) {
                rsmPos = 2;
            }
            if (cPos == 4) {
                cPos = 2;
            }
        } else if (cPos == 2) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
            if (stateCode == 1)
                stateCode = 0;
            if (rsmPos == 4) {
                rsmPos = 2;
            }
            if (spPos == 4) {
                spPos = 2;
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
        } else if (spPos == 4) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
        } else if (cPos == 4) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
            if (stateCode == 1)
                stateCode = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromRSM || fromSP || fromCustomer | fromProduct) {
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
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Product", rsm, sales, customer, state, ""));
        //BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "Invoice", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50"));
        BackgroundExecutor.getInstance().execute(new KAccountReceivablesJunRequester(userId, level, "Invoice", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", "", "", "", ""));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 1) {

            position = rsmProfile.getPosition();
            /*if (position == 0) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(rsmProfile.getName());
            tviAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 1) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = spProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(spProfile.getName());
            tviAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 1) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = customerProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (pPos == 1) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = productProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(productProfile.getProductName());
            llDSO.setVisibility(View.GONE);
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 2) {

            position = rsmProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(rsmProfile.getName());
            tviAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 2) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = spProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(spProfile.getName());
            tviAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 2) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = customerProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(customerProfile.getCustomerName());

            llDSO.setVisibility(View.GONE);
        } else if (pPos == 2) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = productProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(productProfile.getProductName());

            llDSO.setVisibility(View.GONE);
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        }
    }

    private void row3Display() {
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 4) {

            position = rsmProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(rsmProfile.getName());
            tviAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 4) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = spProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(spProfile.getName());
            tviAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 4) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = customerProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(customerProfile.getCustomerName());

            llDSO.setVisibility(View.GONE);
        } else if (pPos == 4) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = customerProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(productProfile.getProductName());

            llDSO.setVisibility(View.GONE);
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        }
    }

    private void row4Display() {
        if (rsmPos == 8) {

            position = rsmProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(rsmProfile.getName());
            tviAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 8) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = spProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(spProfile.getName());
            tviAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 8) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = customerProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(customerProfile.getCustomerName());

            llDSO.setVisibility(View.GONE);
        } else if (pPos == 8) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = customerProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(productProfile.getProductName());

            llDSO.setVisibility(View.GONE);
        }

        if (rsmPos == 4) {
            tviR3Name.setText(rsmProfile.getName());
        } else if (spPos == 4) {
            tviR3Name.setText(spProfile.getName());
        } else if (cPos == 4) {
            tviR3Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 4) {
            tviR3Name.setText(productProfile.getProductName());
        }
        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        }
    }

    private void initData() {
        tviTotalOutstanding.setText(KBAMUtils.getRoundOffValue(minMaxData.getAmount()));
        tviAmount.setText(KBAMUtils.getRoundOffValue(invoiceFilterData.getAmount()));
        if (fromCustomer || fromProduct) {
            llDSO.setVisibility(View.GONE);
        } else {
            llDSO.setVisibility(View.VISIBLE);
            bar = (invoiceFilterData.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        }
        /*if (fromCustomer) {
            tviOutstandingHeading.setText("CUSTOMER NAME");
            tviEmpty.setVisibility(View.VISIBLE);
            tviDSOHeading.setText("OUTSTANDING");
        } else {
            tviOutstandingHeading.setText("OUTSTANDING");
            tviEmpty.setVisibility(View.GONE);
            tviDSOHeading.setText("DSO");
        }*/
        displayList();
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

        rangeSeekbarOutstanding.setMinValue(minMaxData.getMinAmount());
        rangeSeekbarOutstanding.setMaxValue(minMaxData.getMaxAmount());

        EditText txtMinNOD = (EditText) dialogView.findViewById(R.id.txtMinNOD);
        EditText txtMaxNOD = (EditText) dialogView.findViewById(R.id.txtMaxNOD);

        /*if (!minAmount.equals(""))
            rangeSeekbarOutstanding.setMinStartValue(Float.parseFloat(minAmount));
        if (!maxAmount.equals(""))
            rangeSeekbarOutstanding.setMaxStartValue(Float.parseFloat(maxAmount));

        if (!minNOD.equals(""))
            rangeSeekbarNOD.setMinStartValue(Float.parseFloat(minNOD));
        if (!maxNOD.equals(""))
            rangeSeekbarNOD.setMaxStartValue(Float.parseFloat(maxNOD));*/

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
                //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Product", rsm, sales, customer, state, ""));
                BackgroundExecutor.getInstance().execute(new KAccountReceivablesJunRequester(userId, level, "Invoice", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", minAmount, maxAmount, minNOD, maxNOD));
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
                BackgroundExecutor.getInstance().execute(new KAccountReceivablesJunRequester(userId, level, "Invoice", rsm, sales, customer, state, product, "", String.valueOf(nextLimit), "50", minAmount, maxAmount, minNOD, maxNOD));
            }
        });
        iviCloseDialogType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        KBAMUtils.hideSoftKeyboard(dashboardActivityContext);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void displayList() {
        //adapter = new TOProductAdapter(dashboardActivityContext, level, type, model, fromRSM, fromSP, fromCustomer);
        adapter = new KTOInvoiceAdapter(dashboardActivityContext, level, invoiceDataList, fromRSM, fromSP, fromCustomer, fromProduct);
        rviRSM.setAdapter(adapter);
    }
}
