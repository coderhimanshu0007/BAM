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
import com.teamcomputers.bam.Adapters.OpenSalesOrder.OSOSalesPersonAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSProductFragment;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSOCustomerModel;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSOInvoiceModel;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSORSMSalesModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.OpenSalesOrderRequester;
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

public class OSOSalesPersonFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String INVOICE_PROFILE = "INVOICE_PROFILE";
    public static final String STATE_CODE = "STATE_CODE";
    public static final String RSM_POSITION = "RSM_POSITION";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_INVOICE = "FROM_INVOICE";
    public static final String RSM_POS = "RSM_POS";
    public static final String INVOICE_POS = "INVOICE_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";

    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    OSOCustomerModel customerProfile;
    OSORSMSalesModel rsmProfile;
    OSOInvoiceModel invoiceProfile;

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
    @BindView(R.id.tviR1Name)
    TextView tviR1Name;
    @BindView(R.id.tviR2Name)
    TextView tviR2Name;
    @BindView(R.id.tviR3Name)
    TextView tviR3Name;
    @BindView(R.id.iviR1Close)
    ImageView iviR1Close;
    @BindView(R.id.tviSOAmount)
    TextView tviSOAmount;
    @BindView(R.id.tviR1StateName)
    TextView tviR1StateName;
    @BindView(R.id.tviR2StateName)
    TextView tviR2StateName;
    @BindView(R.id.tviR3StateName)
    TextView tviR3StateName;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private OSOSalesPersonAdapter adapter;
    private int type = 0, pos = 0, stateCode = 0, rsmPos = 0, spPos = 0, cPos = 0, iPos = 0;
    boolean fromRSM, fromCustomer, fromInvoice, search = false;

    List<OSORSMSalesModel> spDataList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_oso_rsm, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);

        rsmPos = getArguments().getInt(RSM_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);
        iPos = getArguments().getInt(INVOICE_POS);

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);
        fromInvoice = getArguments().getBoolean(FROM_INVOICE);

        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        invoiceProfile = getArguments().getParcelable(INVOICE_PROFILE);
        stateCode = getArguments().getInt(STATE_CODE);

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
        dashboardActivityContext.showOSOTab(level);
        dashboardActivityContext.OSOSPClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return OSOSalesPersonFragment.class.getSimpleName();
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
                    case Events.GET_SALES_OSO_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            OSORSMSalesModel[] data = (OSORSMSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), OSORSMSalesModel[].class);
                            spDataList = new ArrayList<OSORSMSalesModel>(Arrays.asList(data));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_SALES_OSO_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.SP_CLICK:
                        if (!fromCustomer) {
                            OSORSMSalesModel spData = (OSORSMSalesModel) eventObject.getObject();
                            Bundle spDataBundle = new Bundle();
                            spDataBundle.putString(OSOCustomerFragment.USER_ID, userId);
                            spDataBundle.putString(OSOCustomerFragment.USER_LEVEL, level);

                            spPos = rsmPos + iPos + 1;
                            spDataBundle.putInt(OSOCustomerFragment.RSM_POS, rsmPos);
                            spDataBundle.putInt(OSOCustomerFragment.SP_POS, spPos);
                            spDataBundle.putInt(OSOCustomerFragment.INVOICE_POS, iPos);

                            spDataBundle.putBoolean(OSOCustomerFragment.FROM_RSM, fromRSM);
                            spDataBundle.putBoolean(OSOCustomerFragment.FROM_SP, true);
                            spDataBundle.putBoolean(OSOCustomerFragment.FROM_INVOICE, fromInvoice);

                            spDataBundle.putParcelable(OSOCustomerFragment.RSM_PROFILE, rsmProfile);
                            spDataBundle.putParcelable(OSOCustomerFragment.SP_PROFILE, spData);
                            spDataBundle.putParcelable(OSOCustomerFragment.INVOICE_PROFILE, invoiceProfile);
                            spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, spDataBundle);
                        }
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        OSORSMSalesModel rsmMenuData = (OSORSMSalesModel) eventObject.getObject();
                        Bundle rsmMenuDataBundle = new Bundle();
                        rsmMenuDataBundle.putString(OSORSMFragment.USER_ID, userId);
                        rsmMenuDataBundle.putString(OSORSMFragment.USER_LEVEL, level);

                        spPos = cPos + iPos + 1;
                        rsmMenuDataBundle.putInt(OSORSMFragment.SP_POS, spPos);
                        rsmMenuDataBundle.putInt(OSORSMFragment.INVOICE_POS, iPos);
                        rsmMenuDataBundle.putInt(OSORSMFragment.CUSTOMER_POS, cPos);

                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_SP, true);
                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_CUSTOMER, fromCustomer);
                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_INVOICE, fromInvoice);

                        rsmMenuDataBundle.putParcelable(OSORSMFragment.SP_PROFILE, rsmMenuData);
                        rsmMenuDataBundle.putParcelable(OSORSMFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmMenuDataBundle.putParcelable(OSORSMFragment.INVOICE_PROFILE, invoiceProfile);
                        rsmMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_RSM_FRAGMENT, rsmMenuDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        OSORSMSalesModel spMenuData = (OSORSMSalesModel) eventObject.getObject();
                        Bundle spMenuDataBundle = new Bundle();
                        spMenuDataBundle.putString(OSOCustomerFragment.USER_ID, userId);
                        spMenuDataBundle.putString(OSOCustomerFragment.USER_LEVEL, level);

                        spPos = rsmPos + iPos + 1;
                        spMenuDataBundle.putInt(OSOCustomerFragment.RSM_POS, rsmPos);
                        spMenuDataBundle.putInt(OSOCustomerFragment.SP_POS, spPos);
                        spMenuDataBundle.putInt(OSOCustomerFragment.INVOICE_POS, iPos);

                        spMenuDataBundle.putBoolean(OSOCustomerFragment.FROM_RSM, fromRSM);
                        spMenuDataBundle.putBoolean(OSOCustomerFragment.FROM_SP, true);
                        spMenuDataBundle.putBoolean(OSOCustomerFragment.FROM_INVOICE, fromInvoice);

                        spMenuDataBundle.putParcelable(OSOCustomerFragment.RSM_PROFILE, rsmProfile);
                        spMenuDataBundle.putParcelable(OSOCustomerFragment.SP_PROFILE, spMenuData);
                        spMenuDataBundle.putParcelable(OSOCustomerFragment.INVOICE_PROFILE, invoiceProfile);
                        spMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, spMenuDataBundle);
                        break;
                    case ClickEvents.PRODUCT_MENU_SELECT:
                        OSORSMSalesModel spMenuProductData = (OSORSMSalesModel) eventObject.getObject();
                        Bundle spMenuProductDataBundle = new Bundle();
                        spMenuProductDataBundle.putString(WSProductFragment.USER_ID, userId);
                        spMenuProductDataBundle.putString(WSProductFragment.USER_LEVEL, level);

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
                        dashboardActivityContext.replaceFragment(Fragments.OSO_INVOICE_FRAGMENT, spMenuProductDataBundle);
                        break;
                    case ClickEvents.RSM_ITEM:
                        int position = (int) eventObject.getObject();
                        Bundle rsmDataBundle = new Bundle();
                        rsmDataBundle.putParcelable(AccountsFragment.RSM_PROFILE, spDataList.get(position));
                        rsmDataBundle.putInt(AccountsFragment.RSM_POSITION, position);
                        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, rsmDataBundle);
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
        fromRSM = false;
        fromCustomer = false;
        fromInvoice = false;
        customerProfile = null;
        rsmProfile = null;
        invoiceProfile = null;
        cviRSMHeading.setVisibility(View.GONE);
        type = 1;
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Sales", "", "", "", "", "", ""));
    }

    @OnClick(R.id.iviR1Close)
    public void r1Close() {
        if (rsmPos == 1) {
            fromRSM = false;
            rsmProfile = null;
            rsmPos = 0;
            if (iPos == 2) {
                iPos = 1;
            } else if (iPos == 4) {
                iPos = 2;
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
            if (iPos == 2) {
                iPos = 1;
            } else if (iPos == 4) {
                iPos = 2;
            }
        } else if (iPos == 1) {
            fromInvoice = false;
            invoiceProfile = null;
            iPos = 0;
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
            if (iPos == 4) {
                iPos = 2;
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
            if (iPos == 4) {
                iPos = 2;
            }
        } else if (iPos == 2) {
            fromInvoice = false;
            invoiceProfile = null;
            iPos = 0;
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
            if (stateCode == 1)
                stateCode = 0;
        } else if (iPos == 4) {
            fromInvoice = false;
            invoiceProfile = null;
            iPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromRSM || fromCustomer || fromInvoice) {
            cviRSMHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = rsmPos + cPos + iPos;

        if (totalPosition == 7) {
            row3Display();
        } else if (totalPosition == 3) {
            row2Display();
        } else if (totalPosition == 1) {
            iviR1Close.setVisibility(View.GONE);
            row1Display();
        }

        String rsm = "", customer = "", invoiceNo = "", state = "";

        if (null != rsmProfile)
            rsm = rsmProfile.getTMC();
        if (null != customerProfile) {
            customer = customerProfile.getCustomerName();
        }
        if (null != invoiceProfile) {
            invoiceNo = invoiceProfile.getInvoiceNo();
        }
        if (stateCode == 1)
            state = customerProfile.getStateCodeWise().get(0).getStateCode();
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Sales", rsm, "", customer, state, invoiceNo, ""));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        if (rsmPos == 1) {
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
            tviSOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTD()));
            //tviAch.setText(rsmProfile.getYTDPercentage().intValue() + "%");
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
            tviR1Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSOAmount()));
            } else {
                tviR1StateName.setVisibility(View.GONE);
                tviSOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            }
            //tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYTD()));
            //tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQTD()));
            //tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMTD()));
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
            tviR1Name.setText(invoiceProfile.getInvoiceNo());
            tviSOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
            /*if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }*/
            //tviTarget.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTD()));
            //tviAch.setText(invoiceProfile.getYTDPercentage().intValue() + "%");
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        if (rsmPos == 2) {
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
            tviSOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTD()));
            //tviAch.setText(rsmProfile.getYTDPercentage().intValue() + "%");
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
            tviR2Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSOAmount()));
            } else {
                tviR2StateName.setVisibility(View.GONE);
                tviSOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            }
            //tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYTD()));
            //tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQTD()));
            //tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMTD()));
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
            tviR2Name.setText(invoiceProfile.getInvoiceNo());
            tviSOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTD()));
            //tviAch.setText(invoiceProfile.getYTDPercentage().intValue() + "%");
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getInvoiceNo());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
    }

    private void row3Display() {
        if (rsmPos == 4) {
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
            tviSOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTD()));
            //tviAch.setText(rsmProfile.getYTDPercentage().intValue() + "%");
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
            tviR3Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR3StateName.setVisibility(View.VISIBLE);
                tviR3StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSOAmount()));
            } else {
                tviR3StateName.setVisibility(View.GONE);
                tviSOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            }
            //tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYTD()));
            //tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQTD()));
            //tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMTD()));
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
            tviR3Name.setText(invoiceProfile.getInvoiceNo());
            tviSOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTD()));
            //tviAch.setText(invoiceProfile.getYTDPercentage().intValue() + "%");
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
            //tviR2SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
            //tviR2SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getInvoiceNo());
            //tviR2SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getInvoiceNo());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
    }

    private void initData(String type) {
        adapter = new OSOSalesPersonAdapter(dashboardActivityContext, type, level, spDataList, fromRSM, fromCustomer, fromInvoice);
        rviRSM.setAdapter(adapter);
    }

}
