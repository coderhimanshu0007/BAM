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
import com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters.KPSOProductAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.CustomerFragment;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOCustomerModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOProductModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSORSMModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOSOModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.WSRequesters.KSalesOpenOrderJunRequester;
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

public class OSOProductFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String SO_PROFILE = "SO_PROFILE";
    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_SO = "FROM_SO";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String STATE_CODE = "STATE_CODE";
    public static final String RSM_POS = "RSM_POS";
    public static final String SP_POS = "SP_POS";
    public static final String SO_POS = "SO_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    boolean fromRSM, fromSP, fromSO, fromCustomer, search = false;
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
    private KPSOProductAdapter adapter;
    private int position = 0, rsmPos = 0, spPos = 0, cPos = 0, stateCode = 0, soPos = 0, pPos = 0;

    KPSOProductModel productData;
    KPSOProductModel.Datum selectedProductData;
    List<KPSOProductModel.Datum> productDataList = new ArrayList<>();
    KPSOProductModel.Filter productFilterData;

    KPSORSMModel.Datum rsmProfile, salesProfile;
    KPSOSOModel.Datum soProfile;
    KPSOCustomerModel.Datum customerProfile;

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
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);

        rsmPos = getArguments().getInt(RSM_POS);
        spPos = getArguments().getInt(SP_POS);
        soPos = getArguments().getInt(SO_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);

        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        salesProfile = getArguments().getParcelable(SP_PROFILE);
        soProfile = getArguments().getParcelable(SO_PROFILE);
        stateCode = getArguments().getInt(STATE_CODE);
        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);

        toolbarTitle = getString(R.string.Product);
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
        dashboardActivityContext.OSOProductClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return OSOProductFragment.class.getSimpleName();
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
                    case Events.GET_PRODUCT_OSO_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            /*JSONArray jsonArray = new JSONArray(KBAMUtils.replaceDataResponse(eventObject.getObject().toString()));
                            OSOCustomerModel[] data = (OSOCustomerModel[]) KBAMUtils.fromJson(String.valueOf(jsonArray), OSOCustomerModel[].class);
                            model = new ArrayList<OSOCustomerModel>(Arrays.asList(data));*/
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()));
                            productData = (KPSOProductModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KPSOProductModel.class);
                            productDataList = productData.getData();
                            for (int i = 0; i < productDataList.size(); i++) {
                                if (productDataList.get(i).getProductName().equals("") || productDataList.get(i).getProductName().equals("null")) {
                                    productDataList.remove(i);
                                }
                            }
                            productFilterData = productData.getFilter();
                            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productFilterData.getSoAmount()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
                        dismissProgress();
                        break;
                    case Events.GET_PRODUCT_OSO_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.ACCOUNT_ITEM:
                        int position = (int) eventObject.getObject();
                        Bundle acctDataBundle = new Bundle();
                        acctDataBundle.putParcelable(CustomerFragment.ACCT_PROFILE, productDataList.get(position));
                        acctDataBundle.putInt(CustomerFragment.ACCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, acctDataBundle);
                        /*acctDataBundle.putParcelable(ProductFragment.PRODUCT_PROFILE, model.get(position));
                        acctDataBundle.putInt(ProductFragment.PRODUCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.PRODUCT_FRAGMENT, acctDataBundle);*/
                        break;
                    case ClickEvents.SO_ITEM_SELECT:
                        selectedProductData = (KPSOProductModel.Datum) eventObject.getObject();
                        Bundle soBundle = new Bundle();
                        soBundle.putString(OSOInvoiceFragment.USER_ID, userId);
                        soBundle.putString(OSOInvoiceFragment.USER_LEVEL, level);

                        pPos = rsmPos + spPos + cPos + 1;

                        soBundle.putInt(OSOInvoiceFragment.RSM_POS, rsmPos);
                        soBundle.putInt(OSOInvoiceFragment.SP_POS, spPos);
                        soBundle.putInt(OSOInvoiceFragment.CUSTOMER_POS, cPos);
                        soBundle.putInt(OSOInvoiceFragment.PRODUCT_POS, pPos);

                        soBundle.putBoolean(OSOInvoiceFragment.FROM_RSM, fromRSM);
                        soBundle.putBoolean(OSOInvoiceFragment.FROM_SP, fromSP);
                        soBundle.putBoolean(OSOInvoiceFragment.FROM_CUSTOMER, fromCustomer);
                        soBundle.putBoolean(OSOInvoiceFragment.FROM_PRODUCT, true);

                        soBundle.putParcelable(OSOInvoiceFragment.CUSTOMER_PROFILE, customerProfile);
                        soBundle.putParcelable(OSOInvoiceFragment.RSM_PROFILE, rsmProfile);
                        soBundle.putParcelable(OSOInvoiceFragment.SP_PROFILE, salesProfile);
                        soBundle.putParcelable(OSOInvoiceFragment.PRODUCT_PROFILE, selectedProductData);

                        soBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_INVOICE_FRAGMENT, soBundle);
                        break;
                    case ClickEvents.CUSTOMER_ITEM:
                        selectedProductData = (KPSOProductModel.Datum) eventObject.getObject();
                        Bundle customerBundle = new Bundle();
                        customerBundle.putString(OSOCustomerFragment.USER_ID, userId);
                        customerBundle.putString(OSOCustomerFragment.USER_LEVEL, level);

                        pPos = rsmPos + spPos + soPos + 1;

                        customerBundle.putInt(OSOCustomerFragment.RSM_POS, rsmPos);
                        customerBundle.putInt(OSOCustomerFragment.SP_POS, spPos);
                        customerBundle.putInt(OSOCustomerFragment.SO_POS, soPos);
                        customerBundle.putInt(OSOCustomerFragment.PRODUCT_POS, pPos);

                        customerBundle.putBoolean(OSOCustomerFragment.FROM_RSM, fromRSM);
                        customerBundle.putBoolean(OSOCustomerFragment.FROM_SP, fromSP);
                        customerBundle.putBoolean(OSOCustomerFragment.FROM_SO, fromSO);
                        customerBundle.putBoolean(OSOCustomerFragment.FROM_PRODUCT, true);

                        customerBundle.putParcelable(OSOCustomerFragment.SO_PROFILE, soProfile);
                        customerBundle.putParcelable(OSOCustomerFragment.RSM_PROFILE, rsmProfile);
                        customerBundle.putParcelable(OSOCustomerFragment.SP_PROFILE, salesProfile);
                        customerBundle.putParcelable(OSOCustomerFragment.PRODUCT_PROFILE, selectedProductData);

                        customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, customerBundle);
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        selectedProductData = (KPSOProductModel.Datum) eventObject.getObject();
                        Bundle rsmBundle = new Bundle();
                        rsmBundle.putString(OSORSMFragment.USER_ID, userId);
                        rsmBundle.putString(OSORSMFragment.USER_LEVEL, level);

                        pPos = spPos + cPos + soPos + 1;

                        rsmBundle.putInt(OSORSMFragment.SP_POS, spPos);
                        rsmBundle.putInt(OSORSMFragment.CUSTOMER_POS, cPos);
                        rsmBundle.putInt(OSORSMFragment.SO_POS, soPos);
                        rsmBundle.putInt(OSORSMFragment.PRODUCT_POS, pPos);

                        rsmBundle.putBoolean(OSORSMFragment.FROM_SP, fromSP);
                        rsmBundle.putBoolean(OSORSMFragment.FROM_CUSTOMER, fromCustomer);
                        rsmBundle.putBoolean(OSORSMFragment.FROM_SO, fromSO);
                        rsmBundle.putBoolean(OSORSMFragment.FROM_PRODUCT, true);

                        rsmBundle.putParcelable(OSORSMFragment.SP_PROFILE, salesProfile);
                        rsmBundle.putParcelable(OSORSMFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmBundle.putParcelable(OSORSMFragment.SO_PROFILE, soProfile);
                        rsmBundle.putParcelable(OSORSMFragment.PRODUCT_PROFILE, selectedProductData);

                        rsmBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_RSM_FRAGMENT, rsmBundle);
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        selectedProductData = (KPSOProductModel.Datum) eventObject.getObject();
                        Bundle salesBundle = new Bundle();
                        salesBundle.putString(OSOSalesPersonFragment.USER_ID, userId);
                        salesBundle.putString(OSOSalesPersonFragment.USER_LEVEL, level);

                        pPos = rsmPos + cPos + soPos + 1;

                        salesBundle.putInt(OSOSalesPersonFragment.RSM_POS, rsmPos);
                        salesBundle.putInt(OSOSalesPersonFragment.CUSTOMER_POS, cPos);
                        salesBundle.putInt(OSOSalesPersonFragment.SO_POS, soPos);
                        salesBundle.putInt(OSOSalesPersonFragment.PRODUCT_POS, pPos);

                        salesBundle.putBoolean(OSOSalesPersonFragment.FROM_RSM, fromRSM);
                        salesBundle.putBoolean(OSOSalesPersonFragment.FROM_CUSTOMER, fromCustomer);
                        salesBundle.putBoolean(OSOSalesPersonFragment.FROM_SO, fromSO);
                        salesBundle.putBoolean(OSOSalesPersonFragment.FROM_PRODUCT, true);

                        salesBundle.putParcelable(OSOSalesPersonFragment.RSM_PROFILE, rsmProfile);
                        salesBundle.putParcelable(OSOSalesPersonFragment.CUSTOMER_PROFILE, customerProfile);
                        salesBundle.putParcelable(OSOSalesPersonFragment.SO_PROFILE, soProfile);
                        salesBundle.putParcelable(OSOSalesPersonFragment.PRODUCT_PROFILE, selectedProductData);

                        salesBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, salesBundle);
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
        cPos = 0;
        soPos = 0;
        fromRSM = false;
        fromSP = false;
        fromCustomer = false;
        fromSO = false;
        rsmProfile = null;
        salesProfile = null;
        customerProfile = null;
        soProfile = null;
        cviSPHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Customer", "", "", "", "", "", ""));
        //BackgroundExecutor.getInstance().execute(new KSalesOpenOrderAprRequester(userId, level, "Product", "", "", "", "", "", ""));
        BackgroundExecutor.getInstance().execute(new KSalesOpenOrderJunRequester(userId, level, "Product", "", "", "", "", "", "", "", "", "", "", "", ""));
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
            if (soPos == 2) {
                soPos = 1;
            } else if (soPos == 4) {
                soPos = 2;
            } else if (soPos == 8) {
                soPos = 4;
            }
        } else if (spPos == 1) {
            fromSP = false;
            salesProfile = null;
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
            if (soPos == 2) {
                soPos = 1;
            } else if (soPos == 4) {
                soPos = 2;
            } else if (soPos == 8) {
                soPos = 4;
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
            if (soPos == 2) {
                soPos = 1;
            } else if (soPos == 4) {
                soPos = 2;
            } else if (soPos == 8) {
                soPos = 4;
            }
        } else if (soPos == 1) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
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
            if (soPos == 4) {
                soPos = 2;
            } else if (soPos == 8) {
                soPos = 4;
            }
        } else if (spPos == 2) {
            fromSP = false;
            salesProfile = null;
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
            if (soPos == 4) {
                soPos = 2;
            } else if (soPos == 8) {
                soPos = 4;
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
            if (soPos == 4) {
                soPos = 2;
            } else if (soPos == 8) {
                soPos = 4;
            }
        } else if (soPos == 2) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
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
            if (soPos == 8) {
                soPos = 4;
            }
        } else if (spPos == 4) {
            fromSP = false;
            salesProfile = null;
            spPos = 0;
            if (rsmPos == 8) {
                rsmPos = 4;
            }
            if (cPos == 8) {
                cPos = 4;
            }
            if (soPos == 8) {
                soPos = 4;
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
            if (soPos == 8) {
                soPos = 4;
            }
        } else if (soPos == 4) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
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
            salesProfile = null;
            spPos = 0;
        } else if (cPos == 8) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
        } else if (soPos == 8) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromRSM || fromSP || fromCustomer || fromSO) {
            cviSPHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = rsmPos + spPos + cPos + soPos;

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
        String rsm = "", sales = "", customer = "", state = "", invoiceNo = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTmc();
        if (null != salesProfile)
            sales = salesProfile.getTmc();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        if (stateCode == 1)
            state = customerProfile.getStateCodeWise().get(0).getStateCode();
        if (null != soProfile)
            invoiceNo = soProfile.getSoNumber();
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Customer", rsm, sales, "", "", invoiceNo, ""));
        //BackgroundExecutor.getInstance().execute(new KSalesOpenOrderAprRequester(userId, level, "Product", rsm, sales, customer, state, invoiceNo, ""));
        BackgroundExecutor.getInstance().execute(new KSalesOpenOrderJunRequester(userId, level, "Product", rsm, sales, customer, state, "", invoiceNo, "", "", "", "", "", ""));

    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 1) {
            position = rsmProfile.getPosition();
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
            tviR1Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 1) {
            position = salesProfile.getPosition();
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
            tviR1Name.setText(salesProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSoAmount()));
        } else if (cPos == 1) {
            position = customerProfile.getPosition();
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
            tviR1Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR1StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
        } else if (soPos == 1) {
            position = soProfile.getPosition();
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
            tviR1Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 2) {
            position = rsmProfile.getPosition();
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
            tviR2Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 2) {
            position = salesProfile.getPosition();
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
            tviR2Name.setText(salesProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSoAmount()));
        } else if (cPos == 4) {
            position = customerProfile.getPosition();
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
            tviR2Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR2StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
        } else if (soPos == 2) {
            position = soProfile.getPosition();
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
            tviR2Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
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
        } else if (soPos == 1) {
            tviR1Name.setText(soProfile.getSoNumber());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }
    }

    private void row3Display() {
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 4) {
            position = rsmProfile.getPosition();
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
            tviR3Name.setText(rsmProfile.getName());
        } else if (spPos == 4) {
            position = salesProfile.getPosition();
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
            tviR3Name.setText(salesProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSoAmount()));
        } else if (cPos == 4) {
            position = customerProfile.getPosition();
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
            tviR3Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR3StateName.setVisibility(View.VISIBLE);
                tviR3StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR3StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
        } else if (soPos == 4) {
            position = soProfile.getPosition();
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
            tviR3Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 2) {
            tviR2Name.setText(salesProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSoAmount()));
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }
        } else if (soPos == 2) {
            tviR2Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
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
        } else if (soPos == 1) {
            tviR1Name.setText(soProfile.getSoNumber());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }
    }

    private void row4Display() {
        if (rsmPos == 8) {
            position = rsmProfile.getPosition();
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
            tviR4Name.setText(rsmProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSoAmount()));
        } else if (spPos == 8) {
            position = salesProfile.getPosition();
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
            tviR4Name.setText(salesProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSoAmount()));
        } else if (cPos == 8) {
            position = customerProfile.getPosition();
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
        } else if (soPos == 8) {
            position = soProfile.getPosition();
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
            tviR4Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
        }

        if (rsmPos == 4) {
            tviR3Name.setText(rsmProfile.getName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSOAmount()));
        } else if (spPos == 4) {
            tviR3Name.setText(salesProfile.getName());
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
        } else if (soPos == 4) {
            tviR3Name.setText(soProfile.getSoNumber());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSOAmount()));
        } else if (spPos == 2) {
            tviR2Name.setText(salesProfile.getName());
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
        } else if (soPos == 2) {
            tviR2Name.setText(soProfile.getSoNumber());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
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
        } else if (soPos == 1) {
            tviR1Name.setText(soProfile.getSoNumber());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }
    }

    private void initData() {
        //adapter = new OSOCustomerAdapter(dashboardActivityContext, userId, level, model, fromRSM, fromSP, fromSO);
        adapter = new KPSOProductAdapter(dashboardActivityContext, level, productDataList, fromRSM, fromSP, fromCustomer, fromSO);
        rviRSM.setAdapter(adapter);
    }
}
