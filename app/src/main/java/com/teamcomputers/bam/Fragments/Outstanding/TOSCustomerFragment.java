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
import com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters.KTOCustomerAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.CustomerFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSProductFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSRSMFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSSalesPersonFragment;
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

public class TOSCustomerFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";

    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String INVOICE_PROFILE = "INVOICE_PROFILE";

    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    public static final String FROM_INVOICE = "FROM_INVOICE";

    public static final String RSM_POS = "RSM_POS";
    public static final String SP_POS = "SP_POS";
    public static final String PRODUCT_POS = "PRODUCT_POS";
    public static final String INVOICE_POS = "INVOICE_POS";

    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

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
    @BindView(R.id.iviR1Close)
    ImageView iviR1Close;
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
    private KTOCustomerAdapter adapter;
    boolean fromRSM, fromSP, fromProduct, fromInvoice, search = false;
    private int position = 0, bar = 0, rsmPos = 0, spPos = 0, cPos = 0, pPos = 0, iPos = 0;

    KNRRSMModel.Datum rsmProfile, salesProfile;
    KNRProductModel.Datum productProfile;
    KNRCustomerModel customerData;
    Filter customerFilterData;
    List<KNRCustomerModel.Datum> customerDataList = new ArrayList<>();
    KNRCustomerModel.Datum selectedCustomer;
    KNRInvoiceModel.Datum invoiceProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_to_customer, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromSP = getArguments().getBoolean(FROM_SP);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);
        fromInvoice = getArguments().getBoolean(FROM_INVOICE);

        rsmPos = getArguments().getInt(RSM_POS);
        spPos = getArguments().getInt(SP_POS);
        pPos = getArguments().getInt(PRODUCT_POS);
        iPos = getArguments().getInt(INVOICE_POS);

        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        salesProfile = getArguments().getParcelable(SP_PROFILE);
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);
        invoiceProfile = getArguments().getParcelable(INVOICE_PROFILE);

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
        dashboardActivityContext.showTOSTab(level);
        dashboardActivityContext.TOScustomerClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return TOSCustomerFragment.class.getSimpleName();
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
                    case Events.GET_CUSTOMER_TOS_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()));
                            customerData = (KNRCustomerModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KNRCustomerModel.class);
                            customerDataList = customerData.getData();
                            for (int i = 0; i < customerDataList.size(); i++) {
                                if (customerDataList.get(i).getCustomerName().equals("") || customerDataList.get(i).getCustomerName().equals("null")) {
                                    customerDataList.remove(i);
                                }
                            }
                            customerFilterData = customerData.getFilter();
                            tviAmount.setText(BAMUtil.getRoundOffValue(customerFilterData.getAmount()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
                        dismissProgress();
                        break;
                    case Events.GET_CUSTOMER_TOS_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.ACCOUNT_ITEM:
                        int position = (int) eventObject.getObject();
                        Bundle acctDataBundle = new Bundle();
                        acctDataBundle.putParcelable(CustomerFragment.ACCT_PROFILE, customerDataList.get(position));
                        acctDataBundle.putInt(CustomerFragment.ACCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_CUSTOMER_FRAGMENT, acctDataBundle);
                        /*acctDataBundle.putParcelable(ProductFragment.PRODUCT_PROFILE, model.get(position));
                        acctDataBundle.putInt(ProductFragment.PRODUCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.PRODUCT_FRAGMENT, acctDataBundle);*/
                        break;
                    case ClickEvents.CUSTOMER_SELECT:
                        if (!fromProduct) {
                            selectedCustomer = (KNRCustomerModel.Datum) eventObject.getObject();
                            Bundle customerBundle = new Bundle();
                            customerBundle.putString(TOSProductFragment.USER_ID, userId);
                            customerBundle.putString(TOSProductFragment.USER_LEVEL, level);

                            cPos = rsmPos + spPos + iPos + 1;

                            customerBundle.putInt(TOSProductFragment.RSM_POS, rsmPos);
                            customerBundle.putInt(TOSProductFragment.SP_POS, spPos);
                            customerBundle.putInt(TOSProductFragment.CUSTOMER_POS, cPos);
                            customerBundle.putInt(TOSProductFragment.INVOICE_POS, iPos);

                            customerBundle.putBoolean(TOSProductFragment.FROM_RSM, fromRSM);
                            customerBundle.putBoolean(TOSProductFragment.FROM_SP, fromSP);
                            customerBundle.putBoolean(TOSProductFragment.FROM_CUSTOMER, true);
                            customerBundle.putBoolean(TOSProductFragment.FROM_INVOICE, fromInvoice);

                            customerBundle.putParcelable(TOSProductFragment.RSM_PROFILE, rsmProfile);
                            customerBundle.putParcelable(TOSProductFragment.SP_PROFILE, salesProfile);
                            customerBundle.putParcelable(TOSProductFragment.CUSTOMER_PROFILE, selectedCustomer);
                            customerBundle.putParcelable(TOSProductFragment.INVOICE_PROFILE, invoiceProfile);

                            customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.TOS_PRODUCT_FRAGMENT, customerBundle);
                        }
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        selectedCustomer = (KNRCustomerModel.Datum) eventObject.getObject();
                        Bundle rsmBundle = new Bundle();
                        rsmBundle.putString(TOSRSMFragment.USER_ID, userId);
                        rsmBundle.putString(TOSRSMFragment.USER_LEVEL, level);

                        cPos = spPos + pPos + iPos + 1;

                        rsmBundle.putInt(TOSRSMFragment.SP_POS, spPos);
                        rsmBundle.putInt(TOSRSMFragment.PRODUCT_POS, pPos);
                        rsmBundle.putInt(TOSRSMFragment.INVOICE_POS, iPos);
                        rsmBundle.putInt(TOSRSMFragment.CUSTOMER_POS, cPos);

                        rsmBundle.putBoolean(TOSRSMFragment.FROM_SP, fromSP);
                        rsmBundle.putBoolean(TOSRSMFragment.FROM_PRODUCT, fromProduct);
                        rsmBundle.putBoolean(TOSRSMFragment.FROM_INVOICE, fromInvoice);
                        rsmBundle.putBoolean(TOSRSMFragment.FROM_CUSTOMER, true);

                        rsmBundle.putParcelable(TOSRSMFragment.SP_PROFILE, salesProfile);
                        rsmBundle.putParcelable(TOSRSMFragment.CUSTOMER_PROFILE, selectedCustomer);
                        rsmBundle.putParcelable(TOSRSMFragment.PRODUCT_PROFILE, productProfile);
                        rsmBundle.putParcelable(TOSRSMFragment.INVOICE_PROFILE, invoiceProfile);

                        rsmBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_RSM_FRAGMENT, rsmBundle);
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        selectedCustomer = (KNRCustomerModel.Datum) eventObject.getObject();
                        Bundle customerBundle = new Bundle();
                        customerBundle.putString(TOSSalesPersonFragment.USER_ID, userId);
                        customerBundle.putString(TOSSalesPersonFragment.USER_LEVEL, level);

                        cPos = rsmPos + pPos + iPos + 1;

                        customerBundle.putInt(TOSSalesPersonFragment.RSM_POS, rsmPos);
                        customerBundle.putInt(TOSSalesPersonFragment.CUSTOMER_POS, cPos);
                        customerBundle.putInt(TOSSalesPersonFragment.PRODUCT_POS, pPos);
                        customerBundle.putInt(TOSSalesPersonFragment.INVOICE_POS, iPos);

                        customerBundle.putBoolean(TOSSalesPersonFragment.FROM_RSM, fromRSM);
                        customerBundle.putBoolean(TOSSalesPersonFragment.FROM_PRODUCT, fromProduct);
                        customerBundle.putBoolean(TOSSalesPersonFragment.FROM_INVOICE, fromInvoice);
                        customerBundle.putBoolean(TOSSalesPersonFragment.FROM_CUSTOMER, true);

                        customerBundle.putParcelable(TOSSalesPersonFragment.RSM_PROFILE, rsmProfile);
                        customerBundle.putParcelable(TOSSalesPersonFragment.CUSTOMER_PROFILE, selectedCustomer);
                        customerBundle.putParcelable(TOSSalesPersonFragment.INVOICE_PROFILE, invoiceProfile);
                        customerBundle.putParcelable(TOSSalesPersonFragment.PRODUCT_PROFILE, productProfile);

                        customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_ACCOUNT_FRAGMENT, customerBundle);
                        break;
                    case ClickEvents.SO_ITEM_SELECT:
                        selectedCustomer = (KNRCustomerModel.Datum) eventObject.getObject();
                        Bundle invoiceBundle = new Bundle();
                        invoiceBundle.putString(TOSInvoiceFragment.USER_ID, userId);
                        invoiceBundle.putString(TOSInvoiceFragment.USER_LEVEL, level);

                        cPos = rsmPos + spPos + pPos + 1;
                        invoiceBundle.putInt(TOSInvoiceFragment.RSM_POS, rsmPos);
                        invoiceBundle.putInt(TOSInvoiceFragment.SP_POS, spPos);
                        invoiceBundle.putInt(TOSInvoiceFragment.PRODUCT_POS, pPos);
                        invoiceBundle.putInt(TOSInvoiceFragment.CUSTOMER_POS, cPos);

                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_RSM, fromRSM);
                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_SP, fromSP);
                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_PRODUCT, fromProduct);
                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_CUSTOMER, true);

                        invoiceBundle.putParcelable(TOSInvoiceFragment.RSM_PROFILE, rsmProfile);
                        invoiceBundle.putParcelable(TOSInvoiceFragment.SP_PROFILE, salesProfile);
                        invoiceBundle.putParcelable(TOSInvoiceFragment.PRODUCT_PROFILE, productProfile);
                        invoiceBundle.putParcelable(TOSInvoiceFragment.CUSTOMER_PROFILE, selectedCustomer);

                        invoiceBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_INVOICE_FRAGMENT, invoiceBundle);
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
        fromRSM = false;
        fromSP = false;
        fromProduct = false;
        rsmProfile = null;
        salesProfile = null;
        productProfile = null;
        cviSPHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Customer", "", "", "", "", ""));
        BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "Customer", "", "", "", "", "", "", "", ""));
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
            if (pPos == 2) {
                pPos = 1;
            } else if (pPos == 4) {
                pPos = 2;
            }
        } else if (spPos == 1) {
            fromSP = false;
            salesProfile = null;
            spPos = 0;
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
            if (pPos == 4) {
                pPos = 2;
            }
        } else if (spPos == 2) {
            fromSP = false;
            salesProfile = null;
            spPos = 0;
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
            salesProfile = null;
            spPos = 0;
        } else if (pPos == 4) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromRSM || fromSP || fromProduct || fromInvoice) {
            cviSPHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = rsmPos + spPos + pPos + iPos;

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
        String rsm = "", sales = "", product = "", invoice = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTmc();
        if (null != salesProfile)
            sales = salesProfile.getTmc();
        /*if (null != invoiceProfile)
            invoice = invoiceProfile.getDocument_No();*/
        if (null != productProfile)
            product = productProfile.getCode();
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Customer", rsm, sales, "", "", product));
        BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "Customer", rsm, sales, "", "", product, invoice, "", ""));

    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 1) {
            position = rsmProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (pPos == 1) {
            position = productProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
            //tviDSO.setText(BAMUtil.getRoundOffValue(rsmProfile.getDso()));
            //tviMTD.setText(BAMUtil.getRoundOffValue(productProfile.getMTD()));
        } else if (spPos == 1) {
            position = salesProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(salesProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (salesProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (iPos == 1) {
            position = invoiceProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 2) {
            position = rsmProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
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
            position = salesProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(salesProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (salesProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (pPos == 2) {
            position = productProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 2) {
            position = invoiceProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    private void row3Display() {
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 4) {
            position = rsmProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
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
            position = salesProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(salesProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (salesProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (pPos == 4) {
            position = productProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
            //tviDSO.setText(BAMUtil.getRoundOffValue(productProfile.getDso()));
            //tviMTD.setText(BAMUtil.getRoundOffValue(productProfile.getMTD()));
        } else if (iPos == 4) {
            position = invoiceProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (spPos == 2) {
            tviR2Name.setText(salesProfile.getName());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getDocumentNo());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    private void row4Display() {
        if (rsmPos == 8) {
            position = rsmProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
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
            position = salesProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(salesProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (salesProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (pPos == 8) {
            position = productProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 8) {
            position = invoiceProfile.getPosition();
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }

        if (rsmPos == 4) {
            tviR3Name.setText(rsmProfile.getName());
        } else if (spPos == 4) {
            tviR3Name.setText(salesProfile.getName());
        } else if (pPos == 4) {
            tviR3Name.setText(productProfile.getProductName());
        } else if (iPos == 4) {
            tviR3Name.setText(invoiceProfile.getDocumentNo());
        }
        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (spPos == 2) {
            tviR2Name.setText(salesProfile.getName());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getDocumentNo());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    private void initData() {
        tviAmount.setText(BAMUtil.getRoundOffValue(customerFilterData.getAmount()));
        llDSO.setVisibility(View.VISIBLE);
        bar = (customerFilterData.getDso()).intValue();
        tviDSO.setText(bar + " Days");
        pBar.setProgress(bar);
        if (bar < 30) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        } else {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        }
        /*if(fromCustomer){
            tviOutstandingHeading.setText("CUSTOMER NAME");
            tviEmpty.setVisibility(View.VISIBLE);
            tviDSOHeading.setText("OUTSTANDING");
        } else {
            tviOutstandingHeading.setText("OUTSTANDING");
            tviEmpty.setVisibility(View.GONE);
            tviDSOHeading.setText("DSO");
        }*/
        //adapter = new TOCustomerAdapter(dashboardActivityContext, userId, level, model, fromRSM, fromSP, fromProduct);
        adapter = new KTOCustomerAdapter(dashboardActivityContext, userId, level, customerDataList, fromRSM, fromSP, fromProduct);
        rviRSM.setAdapter(adapter);
    }
}
