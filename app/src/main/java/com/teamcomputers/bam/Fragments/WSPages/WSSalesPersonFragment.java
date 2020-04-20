package com.teamcomputers.bam.Fragments.WSPages;

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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.SalesOutstanding.CustomSpinnerAdapter;
import com.teamcomputers.bam.Adapters.SalesOutstanding.NewSalesPersonAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.FiscalSalesListRequester;
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
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class WSSalesPersonFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String FISCAL_YEAR = "FISCAL_YEAR";
    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String RSM_POSITION = "RSM_POSITION";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    public static final String RSM_POS = "RSM_POS";
    public static final String PRODUCT_POS = "PRODUCT_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";

    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    SalesCustomerModel customerProfile;
    FullSalesModel rsmProfile, productProfile;

    String toolbarTitle = "";
    String userId = "", level = "", fiscalYear = "";
    @BindView(R.id.txtSearch)
    EditText txtSearch;
    @BindView(R.id.tviFiscalYear)
    TextView tviFiscalYear;
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
    @BindView(R.id.tviR1Name)
    TextView tviR1Name;
    @BindView(R.id.tviR2Name)
    TextView tviR2Name;
    @BindView(R.id.tviR3Name)
    TextView tviR3Name;
    @BindView(R.id.iviR1Close)
    ImageView iviR1Close;
    @BindView(R.id.tviR1StateName)
    TextView tviR1StateName;
    @BindView(R.id.tviR2StateName)
    TextView tviR2StateName;
    @BindView(R.id.tviR3StateName)
    TextView tviR3StateName;
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
    private int type = 0, pos = 0, rsmPos = 0, spPos = 0, cPos = 0, pPos = 0;
    boolean fromRSM, fromCustomer, fromProduct, search = false, selectYear = false;

    List<FullSalesModel> spDataList = new ArrayList<>();
    CustomSpinnerAdapter customSpinnerAdapter;

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
        fiscalYear = getArguments().getString(FISCAL_YEAR);

        rsmPos = getArguments().getInt(RSM_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);
        pPos = getArguments().getInt(PRODUCT_POS);

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);

        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);

        toolbarTitle = getString(R.string.Sales);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        tviFiscalYear.setText("FY" + fiscalYear.substring(0, 4));

        /*//Creating the ArrayAdapter instance having the country list
        customSpinnerAdapter = new CustomSpinnerAdapter(dashboardActivityContext, dashboardActivityContext.fiscalYearModel);
        //Setting the ArrayAdapter data on the Spinner
        spinnYear.setAdapter(customSpinnerAdapter);

        tviFiscalYear.setText(dashboardActivityContext.selectedFiscalYear.substring(0, 4));
        *//*tviFiscalYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnYear.performClick();
            }
        });*//*
        //spinnYear.setSelection(dashboardActivityContext.selectedPosition, false);

        spinnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!selectYear) {
                    selectYear = true;
                } else {
                    dashboardActivityContext.selectedFiscalYear = dashboardActivityContext.fiscalYearModel.getFascialYear().get(position).getYear();
                    dashboardActivityContext.selectedPosition = position;
                    rowsDisplay();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        rowsDisplay();

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
        selectYear = false;
        dashboardActivityContext.showTab(level);
        dashboardActivityContext.sPClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return WSSalesPersonFragment.class.getSimpleName();
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
                    case Events.GET_SALES_LIST_SUCCESSFULL:
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
                    case Events.GET_SALES_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.SP_CLICK:
                        if (!fromCustomer) {
                            FullSalesModel spData = (FullSalesModel) eventObject.getObject();
                            Bundle spDataBundle = new Bundle();
                            spDataBundle.putString(WSCustomerFragment.USER_ID, userId);
                            spDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
                            spDataBundle.putString(WSCustomerFragment.FISCAL_YEAR, fiscalYear);

                            spPos = rsmPos + pPos + 1;
                            spDataBundle.putInt(WSCustomerFragment.RSM_POS, rsmPos);
                            spDataBundle.putInt(WSCustomerFragment.SP_POS, spPos);
                            spDataBundle.putInt(WSCustomerFragment.PRODUCT_POS, pPos);

                            spDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, fromRSM);
                            spDataBundle.putBoolean(WSCustomerFragment.FROM_SP, true);
                            spDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, fromProduct);

                            spDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, rsmProfile);
                            spDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, spData);
                            spDataBundle.putParcelable(WSCustomerFragment.PRODUCT_PROFILE, productProfile);
                            spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.WS_CUSTOMER_FRAGMENT, spDataBundle);
                        }
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        FullSalesModel rsmMenuData = (FullSalesModel) eventObject.getObject();
                        Bundle rsmMenuDataBundle = new Bundle();
                        rsmMenuDataBundle.putString(WSRSMFragment.USER_ID, userId);
                        rsmMenuDataBundle.putString(WSRSMFragment.USER_LEVEL, level);
                        rsmMenuDataBundle.putString(WSRSMFragment.FISCAL_YEAR, fiscalYear);

                        spPos = rsmPos + pPos + 1;
                        rsmMenuDataBundle.putInt(WSRSMFragment.SP_POS, spPos);
                        rsmMenuDataBundle.putInt(WSRSMFragment.PRODUCT_POS, pPos);
                        rsmMenuDataBundle.putInt(WSRSMFragment.CUSTOMER_POS, cPos);

                        rsmMenuDataBundle.putBoolean(WSRSMFragment.FROM_SP, true);
                        rsmMenuDataBundle.putBoolean(WSRSMFragment.FROM_CUSTOMER, fromCustomer);
                        rsmMenuDataBundle.putBoolean(WSRSMFragment.FROM_PRODUCT, fromProduct);

                        rsmMenuDataBundle.putParcelable(WSRSMFragment.SP_PROFILE, rsmMenuData);
                        rsmMenuDataBundle.putParcelable(WSRSMFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmMenuDataBundle.putParcelable(WSRSMFragment.PRODUCT_PROFILE, productProfile);
                        rsmMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.WS_RSM_FRAGMENT, rsmMenuDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        FullSalesModel spMenuData = (FullSalesModel) eventObject.getObject();
                        Bundle spMenuDataBundle = new Bundle();
                        spMenuDataBundle.putString(WSCustomerFragment.USER_ID, userId);
                        spMenuDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
                        spMenuDataBundle.putString(WSCustomerFragment.FISCAL_YEAR, fiscalYear);

                        spPos = rsmPos + pPos + 1;
                        spMenuDataBundle.putInt(WSCustomerFragment.RSM_POS, rsmPos);
                        spMenuDataBundle.putInt(WSCustomerFragment.SP_POS, spPos);
                        spMenuDataBundle.putInt(WSCustomerFragment.PRODUCT_POS, pPos);

                        spMenuDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, fromRSM);
                        spMenuDataBundle.putBoolean(WSCustomerFragment.FROM_SP, true);
                        spMenuDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, fromProduct);

                        spMenuDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, rsmProfile);
                        spMenuDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, spMenuData);
                        spMenuDataBundle.putParcelable(WSCustomerFragment.PRODUCT_PROFILE, productProfile);
                        spMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.WS_CUSTOMER_FRAGMENT, spMenuDataBundle);
                        break;
                    case ClickEvents.PRODUCT_MENU_SELECT:
                        FullSalesModel spMenuProductData = (FullSalesModel) eventObject.getObject();
                        Bundle spMenuProductDataBundle = new Bundle();
                        spMenuProductDataBundle.putString(WSProductFragment.USER_ID, userId);
                        spMenuProductDataBundle.putString(WSProductFragment.USER_LEVEL, level);
                        spMenuProductDataBundle.putString(WSProductFragment.FISCAL_YEAR, fiscalYear);

                        spPos = rsmPos + cPos + 1;
                        spMenuProductDataBundle.putInt(WSProductFragment.RSM_POS, rsmPos);
                        spMenuProductDataBundle.putInt(WSProductFragment.SP_POS, spPos);
                        spMenuProductDataBundle.putInt(WSProductFragment.CUSTOMER_POS, cPos);

                        spMenuProductDataBundle.putBoolean(WSProductFragment.FROM_RSM, fromRSM);
                        spMenuProductDataBundle.putBoolean(WSProductFragment.FROM_SP, true);
                        spMenuProductDataBundle.putBoolean(WSProductFragment.FROM_CUSTOMER, fromCustomer);

                        spMenuProductDataBundle.putParcelable(WSProductFragment.RSM_PROFILE, rsmProfile);
                        spMenuProductDataBundle.putParcelable(WSProductFragment.SP_PROFILE, spMenuProductData);
                        spMenuProductDataBundle.putParcelable(WSProductFragment.CUSTOMER_PROFILE, customerProfile);
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
        fiscalYear = dashboardActivityContext.selectedFiscalYear;
        cviRSMHeading.setVisibility(View.GONE);
        type = 1;
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FiscalSalesListRequester(userId, level, "Sales", "", "", "", "", "", fiscalYear));
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
            tviR1StateName.setText("");
            cPos = 0;
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
            tviR2StateName.setText("");
            cPos = 0;
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
            tviR3StateName.setText("");
            cPos = 0;
        } else if (pPos == 4) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromRSM || fromCustomer || fromProduct) {
            cviRSMHeading.setVisibility(View.VISIBLE);
        } else {
            cviRSMHeading.setVisibility(View.GONE);
        }

        int totalPosition = rsmPos + cPos + pPos;

        if (totalPosition == 7) {
            row3Display();
        } else if (totalPosition == 3) {
            row2Display();
        } else if (totalPosition == 1) {
            iviR1Close.setVisibility(View.GONE);
            row1Display();
        }

        String rsm = "", customer = "", product = "";

        if (null != rsmProfile)
            rsm = rsmProfile.getTMC();
        if (null != customerProfile) {
            customer = customerProfile.getCustomerName();
        }
        if (null != productProfile) {
            product = productProfile.getCode();
        }

        fiscalYear = dashboardActivityContext.selectedFiscalYear;

        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FiscalSalesListRequester(userId, level, "Sales", rsm, "", customer, "", product, fiscalYear));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        if (rsmPos == 1) {
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
            tviR1Name.setText(rsmProfile.getName());
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTD()));
            tviAch.setText(rsmProfile.getYTDPercentage().intValue() + "%");
        } else if (cPos == 1) {
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
            tviR1Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
            tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYTD()));
            tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQTD()));
            tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMTD()));
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
            tviR1Name.setText(productProfile.getName());
            tviTarget.setText(BAMUtil.getRoundOffValue(productProfile.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(productProfile.getYTD()));
            tviAch.setText(productProfile.getYTDPercentage().intValue() + "%");
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        if (rsmPos == 2) {
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
            tviR2Name.setText(rsmProfile.getName());
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTD()));
            tviAch.setText(rsmProfile.getYTDPercentage().intValue() + "%");
        } else if (cPos == 2) {
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
            tviR2Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }
            tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYTD()));
            tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQTD()));
            tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMTD()));
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
            tviR2Name.setText(productProfile.getName());
            tviTarget.setText(BAMUtil.getRoundOffValue(productProfile.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(productProfile.getYTD()));
            tviAch.setText(productProfile.getYTDPercentage().intValue() + "%");
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getName());
        }
    }

    private void row3Display() {
        if (rsmPos == 4) {
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
            tviR3Name.setText(rsmProfile.getName());
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTD()));
            tviAch.setText(rsmProfile.getYTDPercentage().intValue() + "%");
        } else if (cPos == 4) {
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
            tviR3Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR3StateName.setVisibility(View.VISIBLE);
                tviR3StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR3StateName.setVisibility(View.GONE);
            }
            tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYTD()));
            tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQTD()));
            tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMTD()));
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
            tviR3Name.setText(productProfile.getName());
            tviTarget.setText(BAMUtil.getRoundOffValue(productProfile.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(productProfile.getYTD()));
            tviAch.setText(productProfile.getYTDPercentage().intValue() + "%");
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getName());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getName());
        }
    }

    private void initData(String type) {
        adapter = new NewSalesPersonAdapter(dashboardActivityContext, type, level, spDataList, fromRSM, fromCustomer, fromProduct);
        rviRSM.setAdapter(adapter);
    }

}
