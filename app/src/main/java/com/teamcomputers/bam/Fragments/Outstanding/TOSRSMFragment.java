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
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters.KTORSMAdapter;
import com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters.KTORSMFilterAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSCustomerFragment;
import com.teamcomputers.bam.Models.WSModels.NRModels.Filter;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRCustomerModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRInvoiceModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRProductModel;
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRRSMModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.WSRequesters.KAccountReceivablesJunRequester;
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

public class TOSRSMFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String RSM_POSITION = "RSM_POSITION";

    public static final String STATE_CODE = "STATE_CODE";

    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String INVOICE_PROFILE = "INVOICE_PROFILE";

    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    public static final String FROM_INVOICE = "FROM_INVOICE";

    public static final String SP_POS = "SP_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";
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
    @BindView(R.id.tviR4StateName)
    TextView tviR4StateName;
    @BindView(R.id.tviAmount)
    TextView tviAmount;
    @BindView(R.id.tviDSO)
    TextView tviDSO;

    @BindView(R.id.tviOutstandingHeading)
    TextView tviOutstandingHeading;
    @BindView(R.id.tviEmpty)
    TextView tviEmpty;
    @BindView(R.id.tviDSOHeading)
    TextView tviDSOHeading;

    @BindView(R.id.pBar)
    ProgressBar pBar;
    @BindView(R.id.llDSO)
    LinearLayout llDSO;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private KTORSMAdapter rsmAdapter;
    private int type = 0, pos = 0, stateCode = 0, bar = 0, rsmPos = 0, spPos = 0, cPos = 0, pPos = 0, iPos = 0, filterSelectedPos = 0;
    boolean fromSP, fromCustomer, fromProduct, fromInvoice, search = false;

    KNRRSMModel.Datum spProfile, selectedRSMData;
    KNRRSMModel rsmData;
    List<KNRRSMModel.Datum> rsmDataList = new ArrayList<>();
    List<KNRRSMModel.Datum> filterRSMList = new ArrayList<>();
    Filter rsmFilterData;
    KNRCustomerModel.Datum customerProfile;
    KNRProductModel.Datum productProfile;
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

        fromSP = getArguments().getBoolean(FROM_SP);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);
        fromInvoice = getArguments().getBoolean(FROM_INVOICE);

        spPos = getArguments().getInt(SP_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);
        pPos = getArguments().getInt(PRODUCT_POS);
        iPos = getArguments().getInt(INVOICE_POS);

        spProfile = getArguments().getParcelable(SP_PROFILE);
        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);
        invoiceProfile = getArguments().getParcelable(INVOICE_PROFILE);
        //stateCode = getArguments().getInt(STATE_CODE);

        toolbarTitle = getString(R.string.Heading_RSM);
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
        dashboardActivityContext.TOSRSMClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return TOSRSMFragment.class.getSimpleName();
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
                    case Events.GET_RSM_TOS_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()));
                            rsmData = (KNRRSMModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KNRRSMModel.class);
                            rsmDataList = rsmData.getData();
                            for (int i = 0; i < rsmDataList.size(); i++) {
                                if (rsmDataList.get(i).getName().equals("") || rsmDataList.get(i).getName().equals("null")) {
                                    rsmDataList.remove(i);
                                }
                            }
                            rsmFilterData = rsmData.getFilter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initRSMData();
                        dismissProgress();
                        break;
                    case Events.GET_RSM_TOS_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_CLICK:
                        selectedRSMData = (KNRRSMModel.Datum) eventObject.getObject();
                        Bundle rsmDataBundle = new Bundle();
                        rsmDataBundle.putString(TOSSalesPersonFragment.USER_ID, userId);
                        rsmDataBundle.putString(TOSSalesPersonFragment.USER_LEVEL, level);

                        rsmPos = cPos + pPos + iPos + 1;

                        rsmDataBundle.putInt(TOSSalesPersonFragment.RSM_POS, rsmPos);
                        rsmDataBundle.putInt(TOSSalesPersonFragment.CUSTOMER_POS, cPos);
                        rsmDataBundle.putInt(TOSSalesPersonFragment.PRODUCT_POS, pPos);
                        rsmDataBundle.putInt(TOSSalesPersonFragment.INVOICE_POS, iPos);

                        rsmDataBundle.putBoolean(TOSSalesPersonFragment.FROM_RSM, true);
                        rsmDataBundle.putBoolean(TOSSalesPersonFragment.FROM_CUSTOMER, fromCustomer);
                        rsmDataBundle.putBoolean(TOSSalesPersonFragment.FROM_PRODUCT, fromProduct);
                        rsmDataBundle.putBoolean(TOSSalesPersonFragment.FROM_INVOICE, fromInvoice);

                        rsmDataBundle.putParcelable(TOSSalesPersonFragment.RSM_PROFILE, selectedRSMData);
                        rsmDataBundle.putParcelable(TOSSalesPersonFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmDataBundle.putParcelable(TOSSalesPersonFragment.PRODUCT_PROFILE, productProfile);
                        rsmDataBundle.putParcelable(TOSSalesPersonFragment.INVOICE_PROFILE, invoiceProfile);

                        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_ACCOUNT_FRAGMENT, rsmDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        selectedRSMData = (KNRRSMModel.Datum) eventObject.getObject();
                        Bundle customerDataBundle = new Bundle();
                        customerDataBundle.putString(WSCustomerFragment.USER_ID, userId);
                        customerDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);

                        rsmPos = spPos + pPos + iPos + 1;

                        customerDataBundle.putInt(TOSCustomerFragment.RSM_POS, rsmPos);
                        customerDataBundle.putInt(TOSCustomerFragment.SP_POS, spPos);
                        customerDataBundle.putInt(TOSCustomerFragment.PRODUCT_POS, pPos);
                        customerDataBundle.putInt(TOSCustomerFragment.INVOICE_POS, iPos);

                        customerDataBundle.putBoolean(TOSCustomerFragment.FROM_RSM, true);
                        customerDataBundle.putBoolean(TOSCustomerFragment.FROM_SP, fromCustomer);
                        customerDataBundle.putBoolean(TOSCustomerFragment.FROM_PRODUCT, fromProduct);
                        customerDataBundle.putBoolean(TOSCustomerFragment.FROM_INVOICE, fromInvoice);

                        customerDataBundle.putParcelable(TOSCustomerFragment.RSM_PROFILE, selectedRSMData);
                        customerDataBundle.putParcelable(TOSCustomerFragment.SP_PROFILE, customerProfile);
                        customerDataBundle.putParcelable(TOSCustomerFragment.PRODUCT_PROFILE, productProfile);
                        customerDataBundle.putParcelable(TOSCustomerFragment.INVOICE_PROFILE, invoiceProfile);

                        customerDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_CUSTOMER_FRAGMENT, customerDataBundle);
                        break;
                    case ClickEvents.PRODUCT_MENU_SELECT:
                        selectedRSMData = (KNRRSMModel.Datum) eventObject.getObject();
                        Bundle productDataBundle = new Bundle();
                        productDataBundle.putString(TOSProductFragment.USER_ID, userId);
                        productDataBundle.putString(TOSProductFragment.USER_LEVEL, level);

                        rsmPos = spPos + cPos + iPos + 1;

                        productDataBundle.putInt(TOSProductFragment.RSM_POS, rsmPos);
                        productDataBundle.putInt(TOSProductFragment.SP_POS, spPos);
                        productDataBundle.putInt(TOSProductFragment.CUSTOMER_POS, cPos);
                        productDataBundle.putInt(TOSProductFragment.INVOICE_POS, iPos);

                        productDataBundle.putBoolean(TOSProductFragment.FROM_RSM, true);
                        productDataBundle.putBoolean(TOSProductFragment.FROM_CUSTOMER, fromCustomer);
                        productDataBundle.putBoolean(TOSProductFragment.FROM_SP, fromSP);
                        productDataBundle.putBoolean(TOSProductFragment.FROM_INVOICE, fromInvoice);

                        productDataBundle.putParcelable(TOSProductFragment.RSM_PROFILE, selectedRSMData);
                        productDataBundle.putParcelable(TOSProductFragment.CUSTOMER_PROFILE, customerProfile);
                        productDataBundle.putParcelable(TOSProductFragment.SP_PROFILE, productProfile);
                        productDataBundle.putParcelable(TOSProductFragment.INVOICE_PROFILE, invoiceProfile);

                        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_PRODUCT_FRAGMENT, productDataBundle);
                        break;
                    case ClickEvents.SO_ITEM_SELECT:
                        selectedRSMData = (KNRRSMModel.Datum) eventObject.getObject();
                        Bundle invoiceBundle = new Bundle();
                        invoiceBundle.putString(TOSInvoiceFragment.USER_ID, userId);
                        invoiceBundle.putString(TOSInvoiceFragment.USER_LEVEL, level);

                        rsmPos = spPos + cPos + pPos + 1;

                        invoiceBundle.putInt(TOSInvoiceFragment.RSM_POS, rsmPos);
                        invoiceBundle.putInt(TOSInvoiceFragment.SP_POS, spPos);
                        invoiceBundle.putInt(TOSInvoiceFragment.PRODUCT_POS, pPos);
                        invoiceBundle.putInt(TOSInvoiceFragment.CUSTOMER_POS, cPos);

                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_RSM, true);
                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_SP, fromSP);
                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_PRODUCT, fromProduct);
                        invoiceBundle.putBoolean(TOSInvoiceFragment.FROM_CUSTOMER, fromCustomer);

                        invoiceBundle.putParcelable(TOSInvoiceFragment.RSM_PROFILE, selectedRSMData);
                        invoiceBundle.putParcelable(TOSInvoiceFragment.SP_PROFILE, spProfile);
                        invoiceBundle.putParcelable(TOSInvoiceFragment.PRODUCT_PROFILE, productProfile);
                        invoiceBundle.putParcelable(TOSInvoiceFragment.CUSTOMER_PROFILE, customerProfile);

                        invoiceBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_INVOICE_FRAGMENT, invoiceBundle);
                        break;
                    case Events.ITEM_SELECTED:
                        filterSelectedPos = (int) eventObject.getObject();
                        rsmDataList.get(filterSelectedPos).setSelected(true);
                        filterRSMList.add(rsmDataList.get(filterSelectedPos));
                        break;
                    case Events.ITEM_UNSELECTED:
                        filterSelectedPos = (int) eventObject.getObject();
                        rsmDataList.get(filterSelectedPos).setSelected(false);
                        filterRSMList.remove(rsmDataList.get(filterSelectedPos));
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
        rsmAdapter.getFilter().filter(txtSearch.getText().toString());
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
        spPos = 0;
        cPos = 0;
        pPos = 0;
        fromSP = false;
        fromCustomer = false;
        fromProduct = false;
        spProfile = null;
        customerProfile = null;
        productProfile = null;
        cviRSMHeading.setVisibility(View.GONE);
        type = 1;
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "RSM", "", "", "", "", ""));
        //BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "RSM", "", "", "", "", "", "", "", ""));
        BackgroundExecutor.getInstance().execute(new KAccountReceivablesJunRequester(userId, level, "RSM", "", "", "", "", "", "", "", "", "", "", "", ""));

    }

    @OnClick(R.id.iviR1Close)
    public void r1Close() {
        if (spPos == 1) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
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
            tviR1StateName.setText("");
            cPos = 0;
            /*if (stateCode == 1)
                stateCode = 0;*/
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
        } else if (pPos == 1) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
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
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR2Close)
    public void r2Close() {
        if (spPos == 2) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
            if (cPos == 4) {
                cPos = 2;
            }
            if (pPos == 4) {
                pPos = 2;
            }
        } else if (cPos == 2) {
            fromCustomer = false;
            customerProfile = null;
            tviR2StateName.setText("");
            cPos = 0;
            /*if (stateCode == 1)
                stateCode = 0;*/
            if (spPos == 4) {
                spPos = 2;
            }
            if (pPos == 4) {
                pPos = 2;
            }
        } else if (pPos == 2) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
            if (spPos == 4) {
                spPos = 2;
            }
            if (cPos == 4) {
                cPos = 2;
            }
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR3Close)
    public void r3Close() {
        if (spPos == 4) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
        } else if (cPos == 4) {
            fromCustomer = false;
            customerProfile = null;
            tviR3StateName.setText("");
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
        if (fromSP || fromCustomer || fromProduct || fromInvoice) {
            cviRSMHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = spPos + cPos + pPos + iPos;

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

        String sales = "", customer = "", product = "", state = "", invoice = "";
        if (null != spProfile)
            sales = spProfile.getTmc();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        if (null != productProfile)
            product = productProfile.getCode();
        if (null != invoiceProfile)
            invoice = invoiceProfile.getDocumentNo();
        /*if (stateCode == 1)
            state = customerProfile.getDocumentNo().get(0).getDocumentNo();*/

        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "RSM", "", sales, customer, state, product));
        //BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "RSM", "", sales, customer, state, product, invoice, "", ""));
        BackgroundExecutor.getInstance().execute(new KAccountReceivablesJunRequester(userId, level, "RSM", "", sales, customer, state, product, invoice, "", "", "", "", "", ""));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (spPos == 1) {
            pos = spProfile.getPosition();
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(spProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(spProfile.getAmount()));
            if (fromCustomer || fromInvoice) {
                llDSO.setVisibility(View.GONE);
            } else {
                llDSO.setVisibility(View.VISIBLE);
            }
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 1) {
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (pPos == 1) {
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 1) {
            pos = invoiceProfile.getPosition();
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (spPos == 2) {
            pos = spProfile.getPosition();
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(spProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(spProfile.getAmount()));
            if (fromCustomer || fromInvoice) {
                llDSO.setVisibility(View.GONE);
            } else {
                llDSO.setVisibility(View.VISIBLE);
            }
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 2) {
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (pPos == 2) {
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 2) {
            pos = invoiceProfile.getPosition();
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
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
        if (spPos == 4) {
            pos = spProfile.getPosition();
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(spProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(spProfile.getAmount()));
            if (fromCustomer || fromInvoice) {
                llDSO.setVisibility(View.GONE);
            } else {
                llDSO.setVisibility(View.VISIBLE);
            }
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 4) {
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (pPos == 4) {
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 4) {
            pos = invoiceProfile.getPosition();
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }

        if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getDocumentNo());
        }
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    private void row4Display() {
        if (spPos == 8) {
            pos = spProfile.getPosition();
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(spProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(spProfile.getAmount()));
            if (fromCustomer || fromInvoice) {
                llDSO.setVisibility(View.GONE);
            } else {
                llDSO.setVisibility(View.VISIBLE);
            }
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 8) {
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (pPos == 8) {
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(productProfile.getProductName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 8) {
            pos = invoiceProfile.getPosition();
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
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(invoiceProfile.getDocumentNo());
            tviAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
        }

        if (spPos == 4) {
            tviR3Name.setText(spProfile.getName());
        } else if (cPos == 4) {
            tviR3Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 4) {
            tviR3Name.setText(productProfile.getProductName());
        } else if (iPos == 4) {
            tviR3Name.setText(invoiceProfile.getDocumentNo());
        }
        if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getDocumentNo());
        }
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    AlertDialog alertDialog;

    public void showFilterDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(dashboardActivityContext);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        TextView tviDialogType = (TextView) dialogView.findViewById(R.id.tviDialogType);
        ImageView iviCloseDialogType = (ImageView) dialogView.findViewById(R.id.iviCloseDialogType);

        TextView tviApply = (TextView) dialogView.findViewById(R.id.tviApply);
        TextView tviClear = (TextView) dialogView.findViewById(R.id.tviClear);

        tviDialogType.setText("Apply Filter");

        RecyclerView rviFilterList = (RecyclerView) dialogView.findViewById(R.id.rviFilterList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviFilterList.setLayoutManager(layoutManager);

        KTORSMFilterAdapter filterAdapter = new KTORSMFilterAdapter(dashboardActivityContext, rsmDataList);
        rviFilterList.setAdapter(filterAdapter);

        tviApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                if (filterRSMList.size() > 0) {
                    rsmAdapter = new KTORSMAdapter(dashboardActivityContext, level, filterRSMList, fromSP, fromCustomer, fromInvoice, fromProduct);
                } else {
                    filterRSMList.clear();
                    rsmAdapter = new KTORSMAdapter(dashboardActivityContext, level, rsmDataList, fromSP, fromCustomer, fromInvoice, fromProduct);
                }
                rviRSM.setAdapter(rsmAdapter);
            }
        });
        tviClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                filterRSMList.clear();
                for (int i = 0; i < rsmDataList.size(); i++) {
                    rsmDataList.get(i).setSelected(false);
                }
                rsmAdapter = new KTORSMAdapter(dashboardActivityContext, level, rsmDataList, fromSP, fromCustomer, fromInvoice, fromProduct);
                rviRSM.setAdapter(rsmAdapter);
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

    private void initRSMData() {
        tviAmount.setText(BAMUtil.getRoundOffValue(rsmFilterData.getAmount()));
        if (fromCustomer || fromInvoice) {
            llDSO.setVisibility(View.GONE);
        } else {
            llDSO.setVisibility(View.VISIBLE);
        }
        bar = (rsmFilterData.getDso()).intValue();
        tviDSO.setText(bar + " Days");
        pBar.setProgress(bar);
        if (bar < 30) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        } else {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        }
        if (fromCustomer || fromInvoice || fromProduct) {
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
        //rsmAdapter = new TORSMAdapter(dashboardActivityContext, type, level, rsmDataList, fromSP, fromCustomer, fromProduct);
        rsmAdapter = new KTORSMAdapter(dashboardActivityContext, level, rsmDataList, fromSP, fromCustomer, fromInvoice, fromProduct);
        rviRSM.setAdapter(rsmAdapter);
    }

}
