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

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters.KTOSPAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSCustomerFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSProductFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSRSMFragment;
import com.teamcomputers.bam.Models.WSModels.NRModels.Filter;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRCustomerModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRInvoiceModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRProductModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRRSMModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.WSRequesters.KAccountReceivablesAprRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
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

public class TOSSalesPersonFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";

    public static final String STATE_CODE = "STATE_CODE";

    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String INVOICE_PROFILE = "INVOICE_PROFILE";

    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    public static final String FROM_INVOICE = "FROM_INVOICE";

    public static final String RSM_POS = "RSM_POS";
    public static final String PRODUCT_POS = "PRODUCT_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";
    public static final String INVOICE_POS = "INVOICE_POS";

    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String toolbarTitle = "";
    String userId = "", level = "";
    @BindView(R.id.txtSearch)
    EditText txtSearch;
    @BindView(R.id.llRSMLayout)
    LinearLayout llRSMLayout;
    @BindView(R.id.cviRSMHeading)
    CardView cviRSMHeading;
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

    @BindView(R.id.tviOutstandingHeading)
    TextView tviOutstandingHeading;
    @BindView(R.id.tviEmpty)
    TextView tviEmpty;
    @BindView(R.id.tviDSOHeading)
    TextView tviDSOHeading;

    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private KTOSPAdapter adapter;
    private int type = 0, pos = 0, bar = 0, rsmPos = 0, spPos = 0, cPos = 0, pPos = 0, iPos = 0;
    boolean fromRSM, fromCustomer, fromProduct, fromInvoice, search = false;

    KNRCustomerModel.Datum customerProfile;
    KNRRSMModel.Datum rsmProfile, selectedSPData;
    KNRProductModel.Datum productProfile;
    KNRRSMModel spData;
    List<KNRRSMModel.Datum> spDataList = new ArrayList<>();
    Filter spFilterData;
    KNRInvoiceModel.Datum invoiceProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_to_rsm, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);

        rsmPos = getArguments().getInt(RSM_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);
        pPos = getArguments().getInt(PRODUCT_POS);
        iPos = getArguments().getInt(INVOICE_POS);

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);
        fromInvoice = getArguments().getBoolean(FROM_INVOICE);

        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);
        invoiceProfile = getArguments().getParcelable(INVOICE_PROFILE);

        toolbarTitle = getString(R.string.Sales);
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
        dashboardActivityContext.showTOSTab(level);
        dashboardActivityContext.TOSSPClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return TOSSalesPersonFragment.class.getSimpleName();
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
                    case Events.GET_SALES_TOS_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()));
                            spData = (KNRRSMModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KNRRSMModel.class);
                            spDataList = spData.getData();
                            for (int i = 0; i < spDataList.size(); i++) {
                                if (spDataList.get(i).getName().equals("") || spDataList.get(i).getName().equals("null")) {
                                    spDataList.remove(i);
                                }
                            }
                            spFilterData = spData.getFilter();
                            tviAmount.setText(BAMUtil.getRoundOffValue(spFilterData.getAmount()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_SALES_TOS_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.SP_CLICK:
                        if (!fromCustomer) {
                            selectedSPData = (KNRRSMModel.Datum) eventObject.getObject();
                            Bundle spDataBundle = new Bundle();
                            spDataBundle.putString(TOSCustomerFragment.USER_ID, userId);
                            spDataBundle.putString(TOSCustomerFragment.USER_LEVEL, level);

                            spPos = rsmPos + pPos + iPos + 1;

                            spDataBundle.putInt(TOSCustomerFragment.RSM_POS, rsmPos);
                            spDataBundle.putInt(TOSCustomerFragment.SP_POS, spPos);
                            spDataBundle.putInt(TOSCustomerFragment.PRODUCT_POS, pPos);
                            spDataBundle.putInt(TOSCustomerFragment.INVOICE_POS, iPos);

                            spDataBundle.putBoolean(TOSCustomerFragment.FROM_RSM, fromRSM);
                            spDataBundle.putBoolean(TOSCustomerFragment.FROM_SP, true);
                            spDataBundle.putBoolean(TOSCustomerFragment.FROM_PRODUCT, fromProduct);
                            spDataBundle.putBoolean(TOSCustomerFragment.FROM_INVOICE, fromInvoice);

                            spDataBundle.putParcelable(TOSCustomerFragment.RSM_PROFILE, rsmProfile);
                            spDataBundle.putParcelable(TOSCustomerFragment.SP_PROFILE, selectedSPData);
                            spDataBundle.putParcelable(TOSCustomerFragment.PRODUCT_PROFILE, productProfile);
                            spDataBundle.putParcelable(TOSCustomerFragment.INVOICE_PROFILE, invoiceProfile);

                            spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.TOS_CUSTOMER_FRAGMENT, spDataBundle);
                        }
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        selectedSPData = (KNRRSMModel.Datum) eventObject.getObject();
                        Bundle rsmMenuDataBundle = new Bundle();
                        rsmMenuDataBundle.putString(TOSRSMFragment.USER_ID, userId);
                        rsmMenuDataBundle.putString(TOSRSMFragment.USER_LEVEL, level);

                        spPos = cPos + pPos + iPos + 1;

                        rsmMenuDataBundle.putInt(TOSRSMFragment.SP_POS, spPos);
                        rsmMenuDataBundle.putInt(TOSRSMFragment.PRODUCT_POS, pPos);
                        rsmMenuDataBundle.putInt(TOSRSMFragment.CUSTOMER_POS, cPos);
                        rsmMenuDataBundle.putInt(TOSRSMFragment.INVOICE_POS, iPos);

                        rsmMenuDataBundle.putBoolean(TOSRSMFragment.FROM_SP, true);
                        rsmMenuDataBundle.putBoolean(TOSRSMFragment.FROM_CUSTOMER, fromCustomer);
                        rsmMenuDataBundle.putBoolean(TOSRSMFragment.FROM_PRODUCT, fromProduct);
                        rsmMenuDataBundle.putBoolean(TOSRSMFragment.FROM_INVOICE, fromInvoice);

                        rsmMenuDataBundle.putParcelable(TOSRSMFragment.SP_PROFILE, selectedSPData);
                        rsmMenuDataBundle.putParcelable(TOSRSMFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmMenuDataBundle.putParcelable(TOSRSMFragment.PRODUCT_PROFILE, productProfile);
                        rsmMenuDataBundle.putParcelable(TOSRSMFragment.INVOICE_PROFILE, invoiceProfile);

                        rsmMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_RSM_FRAGMENT, rsmMenuDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        selectedSPData = (KNRRSMModel.Datum) eventObject.getObject();
                        Bundle spMenuDataBundle = new Bundle();
                        spMenuDataBundle.putString(TOSCustomerFragment.USER_ID, userId);
                        spMenuDataBundle.putString(TOSCustomerFragment.USER_LEVEL, level);

                        spPos = rsmPos + pPos + iPos + 1;

                        spMenuDataBundle.putInt(TOSCustomerFragment.RSM_POS, rsmPos);
                        spMenuDataBundle.putInt(TOSCustomerFragment.SP_POS, spPos);
                        spMenuDataBundle.putInt(TOSCustomerFragment.PRODUCT_POS, pPos);
                        spMenuDataBundle.putInt(TOSCustomerFragment.INVOICE_POS, iPos);

                        spMenuDataBundle.putBoolean(TOSCustomerFragment.FROM_RSM, fromRSM);
                        spMenuDataBundle.putBoolean(TOSCustomerFragment.FROM_SP, true);
                        spMenuDataBundle.putBoolean(TOSCustomerFragment.FROM_PRODUCT, fromProduct);
                        spMenuDataBundle.putBoolean(TOSCustomerFragment.FROM_INVOICE, fromInvoice);

                        spMenuDataBundle.putParcelable(TOSCustomerFragment.RSM_PROFILE, rsmProfile);
                        spMenuDataBundle.putParcelable(TOSCustomerFragment.SP_PROFILE, selectedSPData);
                        spMenuDataBundle.putParcelable(TOSCustomerFragment.PRODUCT_PROFILE, productProfile);
                        spMenuDataBundle.putParcelable(TOSCustomerFragment.INVOICE_PROFILE, invoiceProfile);

                        spMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_CUSTOMER_FRAGMENT, spMenuDataBundle);
                        break;
                    case ClickEvents.PRODUCT_MENU_SELECT:
                        selectedSPData = (KNRRSMModel.Datum) eventObject.getObject();
                        Bundle spMenuProductDataBundle = new Bundle();
                        spMenuProductDataBundle.putString(TOSProductFragment.USER_ID, userId);
                        spMenuProductDataBundle.putString(TOSProductFragment.USER_LEVEL, level);

                        spPos = rsmPos + cPos + iPos + 1;

                        spMenuProductDataBundle.putInt(TOSProductFragment.RSM_POS, rsmPos);
                        spMenuProductDataBundle.putInt(TOSProductFragment.SP_POS, spPos);
                        spMenuProductDataBundle.putInt(TOSProductFragment.CUSTOMER_POS, cPos);
                        spMenuProductDataBundle.putInt(TOSProductFragment.INVOICE_PROFILE, iPos);

                        spMenuProductDataBundle.putBoolean(TOSProductFragment.FROM_RSM, fromRSM);
                        spMenuProductDataBundle.putBoolean(TOSProductFragment.FROM_SP, true);
                        spMenuProductDataBundle.putBoolean(TOSProductFragment.FROM_CUSTOMER, fromCustomer);
                        spMenuProductDataBundle.putBoolean(TOSProductFragment.FROM_INVOICE, fromInvoice);

                        spMenuProductDataBundle.putParcelable(TOSProductFragment.RSM_PROFILE, rsmProfile);
                        spMenuProductDataBundle.putParcelable(TOSProductFragment.SP_PROFILE, selectedSPData);
                        spMenuProductDataBundle.putParcelable(TOSProductFragment.CUSTOMER_PROFILE, customerProfile);
                        spMenuProductDataBundle.putParcelable(TOSProductFragment.INVOICE_PROFILE, invoiceProfile);

                        spMenuProductDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_PRODUCT_FRAGMENT, spMenuProductDataBundle);
                        break;
                    case ClickEvents.SO_ITEM_SELECT:
                        selectedSPData = (KNRRSMModel.Datum) eventObject.getObject();
                        Bundle invoiceBundle = new Bundle();
                        invoiceBundle.putString(TOSInvoiceFragment.USER_ID, userId);
                        invoiceBundle.putString(TOSInvoiceFragment.USER_LEVEL, level);

                        spPos = rsmPos + cPos + pPos + 1;

                        invoiceBundle.putInt(TOSInvoiceFragment.RSM_POS, rsmPos);
                        invoiceBundle.putInt(TOSInvoiceFragment.SP_POS, spPos);
                        invoiceBundle.putInt(TOSInvoiceFragment.PRODUCT_POS, pPos);
                        invoiceBundle.putInt(TOSInvoiceFragment.CUSTOMER_POS, cPos);

                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_RSM, fromRSM);
                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_SP, true);
                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_PRODUCT, fromProduct);
                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_CUSTOMER, fromCustomer);

                        invoiceBundle.putParcelable(TOSInvoiceFragment.RSM_PROFILE, rsmProfile);
                        invoiceBundle.putParcelable(TOSInvoiceFragment.SP_PROFILE, selectedSPData);
                        invoiceBundle.putParcelable(TOSInvoiceFragment.PRODUCT_PROFILE, productProfile);
                        invoiceBundle.putParcelable(TOSInvoiceFragment.CUSTOMER_PROFILE, customerProfile);

                        invoiceBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_INVOICE_FRAGMENT, invoiceBundle);
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
        cPos = 0;
        pPos = 0;
        fromRSM = false;
        fromCustomer = false;
        fromProduct = false;
        customerProfile = null;
        rsmProfile = null;
        productProfile = null;
        cviRSMHeading.setVisibility(View.GONE);
        type = 1;
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Sales", "", "", "", "", ""));
        BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "Sales", "", "", "", "", "", "", "", ""));
    }

    @OnClick(R.id.iviR1Close)
    public void r1Close() {
        if (rsmPos == 1) {
            fromRSM = false;
            rsmProfile = null;
            rsmPos = 0;
            if (pPos == 2) {
                pPos = 1;
            } else if (pPos == 4) {
                pPos = 2;
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
           /* if (stateCode == 1)
                stateCode = 0;*/

            if (rsmPos == 2) {
                rsmPos = 1;
            } else if (rsmPos == 4) {
                rsmPos = 2;
            }
            if (pPos == 2) {
                pPos = 1;
            } else if (pPos == 4) {
                pPos = 2;
            }
        } else if (pPos == 1) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
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
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR2Close)
    public void r2Close() {
        if (rsmPos == 2) {
            fromRSM = false;
            rsmProfile = null;
            rsmPos = 0;
            if (pPos == 4) {
                pPos = 2;
            }
            if (cPos == 4) {
                cPos = 2;
            }
        } else if (cPos == 2) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
            /*if (stateCode == 1)
                stateCode = 0;*/
            if (rsmPos == 4) {
                rsmPos = 2;
            }
            if (pPos == 4) {
                pPos = 2;
            }
        } else if (pPos == 2) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
            if (rsmPos == 4) {
                rsmPos = 2;
            }
            if (cPos == 4) {
                cPos = 2;
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
        } else if (cPos == 4) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
            /*if (stateCode == 1)
                stateCode = 0;*/
        } else if (pPos == 4) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromRSM || fromCustomer || fromProduct || fromInvoice) {
            cviRSMHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = rsmPos + cPos + pPos + iPos;

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

        String rsm = "", customer = "", product = "", state = "", invoice = "";

        if (null != rsmProfile)
            rsm = rsmProfile.getTmc();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        if (null != productProfile)
            product = productProfile.getCode();
        /*if (null != invoiceProfile)
            invoice = invoiceProfile.getDocument_No();*/
        /*if (stateCode == 1)
            state = customerProfile.getDocumentNo().get(0).getDocumentNo();*/
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Sales", rsm, "", customer, state, product));
        BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "Sales", rsm, "", customer, state, product, invoice, "", ""));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 1) {
            pos = rsmProfile.getPosition();
            /*if (pos == 0) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (pos == 1) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (pos == 2) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (pos % 2 == 0) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (pos % 2 == 1) {
                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            if (fromCustomer) {
                llDSO.setVisibility(View.GONE);
            } else {
                llDSO.setVisibility(View.VISIBLE);
            }
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 1) {
            pos = customerProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (pPos == 1) {
            pos = productProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 1) {
            pos = invoiceProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 2) {
            pos = rsmProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            if (fromCustomer) {
                llDSO.setVisibility(View.GONE);
            } else {
                llDSO.setVisibility(View.VISIBLE);
            }
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 2) {
            pos = customerProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (pPos == 2) {
            pos = productProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 4) {
            pos = invoiceProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    private void row3Display() {
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 4) {
            pos = rsmProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            if (fromCustomer) {
                llDSO.setVisibility(View.GONE);
            } else {
                llDSO.setVisibility(View.VISIBLE);
            }
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 4) {
            pos = customerProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (pPos == 4) {
            pos = productProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 4) {
            pos = invoiceProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getDocumentNo());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    private void row4Display() {
        if (rsmPos == 8) {
            pos = rsmProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            if (fromCustomer) {
                llDSO.setVisibility(View.GONE);
            } else {
                llDSO.setVisibility(View.VISIBLE);
            }
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 8) {
            pos = customerProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (pPos == 8) {
            pos = productProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 8) {
            pos = invoiceProfile.getPosition();
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }

        if (rsmPos == 4) {
            tviR3Name.setText(rsmProfile.getName());
        } else if (cPos == 4) {
            tviR3Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 4) {
            tviR3Name.setText(productProfile.getProductName());
        } else if (iPos == 4) {
            tviR3Name.setText(invoiceProfile.getDocumentNo());
        }
        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getDocumentNo());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        } else if (pPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    private void initData(String type) {
        tviAmount.setText(BAMUtil.getRoundOffValue(spFilterData.getAmount()));
        if (fromCustomer) {
            llDSO.setVisibility(View.GONE);
        } else {
            llDSO.setVisibility(View.VISIBLE);
        }
        bar = (spFilterData.getDso()).intValue();
        tviDSO.setText(bar + " Days");
        pBar.setProgress(bar);
        if (bar < 30) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        } else {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        }
        if (fromCustomer || fromProduct) {
            llDSO.setVisibility(View.GONE);
            tviOutstandingHeading.setText("CUSTOMER NAME");
            tviEmpty.setVisibility(View.VISIBLE);
            tviDSOHeading.setText("OUTSTANDING");
        } else {
            llDSO.setVisibility(View.VISIBLE);
            tviOutstandingHeading.setText("OUTSTANDING");
            tviEmpty.setVisibility(View.GONE);
            tviDSOHeading.setText("DSO");
        }
        //adapter = new TOSalesPersonAdapter(dashboardActivityContext, type, level, spDataList, fromRSM, fromCustomer, fromProduct);
        adapter = new KTOSPAdapter(dashboardActivityContext, type, level, spDataList, fromRSM, fromCustomer, fromProduct);
        rviRSM.setAdapter(adapter);
    }

}
