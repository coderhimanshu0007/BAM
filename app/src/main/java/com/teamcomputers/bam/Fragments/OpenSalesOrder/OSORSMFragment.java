package com.teamcomputers.bam.Fragments.OpenSalesOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.OpenSalesOrder.OSORSMAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSOCustomerModel;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSOInvoiceModel;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSORSMSalesModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.OpenSalesOrderRequester;
import com.teamcomputers.bam.Requesters.SalesReceivable.OutstandingRequester;
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

public class OSORSMFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String RSM_POSITION = "RSM_POSITION";
    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String INVOICE_PROFILE = "INVOICE_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_INVOICE = "FROM_INVOICE";
    public static final String SP_POS = "SP_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";
    public static final String INVOICE_POS = "INVOICE_POS";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String toolbarTitle = "";

    String userId = "", level = "";
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
    @BindView(R.id.tviR1SOAmount)
    TextView tviR1SOAmount;
    @BindView(R.id.tviR2SOAmount)
    TextView tviR2SOAmount;
    @BindView(R.id.tviR3SOAmount)
    TextView tviR3SOAmount;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private OSORSMAdapter rsmAdapter;
    private int type = 0, pos = 0, rsmPos = 0, spPos = 0, cPos = 0, iPos = 0;
    boolean fromSP, fromCustomer, fromInvoice;

    OSORSMSalesModel spData;
    List<OSORSMSalesModel> rsmDataList = new ArrayList<>();
    OSOCustomerModel customerProfile;
    OSORSMSalesModel spProfile;
    OSOInvoiceModel invoiceProfile;

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
        fromSP = getArguments().getBoolean(FROM_SP);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);
        fromInvoice = getArguments().getBoolean(FROM_INVOICE);

        spPos = getArguments().getInt(SP_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);
        iPos = getArguments().getInt(INVOICE_POS);

        spProfile = getArguments().getParcelable(SP_PROFILE);
        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        invoiceProfile = getArguments().getParcelable(INVOICE_PROFILE);

        toolbarTitle = getString(R.string.Heading_RSM);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

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
        dashboardActivityContext.showOSOTab(level);
        dashboardActivityContext.OSORSMClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return AccountsFragment.class.getSimpleName();
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
                    case Events.GET_OPEN_SALES_ORDER_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            OSORSMSalesModel[] data = (OSORSMSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), OSORSMSalesModel[].class);
                            rsmDataList = new ArrayList<OSORSMSalesModel>(Arrays.asList(data));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initRSMData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_OPEN_SALES_ORDER_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_CLICK:
                        spData = (OSORSMSalesModel) eventObject.getObject();
                        Bundle rsmDataBundle = new Bundle();
                        rsmDataBundle.putString(OSOSalesPersonFragment.USER_ID, userId);
                        rsmDataBundle.putString(OSOSalesPersonFragment.USER_LEVEL, level);
                        rsmDataBundle.putBoolean(OSOSalesPersonFragment.FROM_RSM, true);
                        rsmDataBundle.putBoolean(OSOSalesPersonFragment.FROM_CUSTOMER, fromCustomer);
                        rsmDataBundle.putBoolean(OSOSalesPersonFragment.FROM_INVOICE, fromInvoice);

                        rsmPos = cPos + iPos + 1;
                        rsmDataBundle.putInt(OSOSalesPersonFragment.RSM_POS, rsmPos);
                        rsmDataBundle.putInt(OSOSalesPersonFragment.CUSTOMER_POS, cPos);
                        rsmDataBundle.putInt(OSOSalesPersonFragment.INVOICE_POS, iPos);

                        rsmDataBundle.putParcelable(OSOSalesPersonFragment.RSM_PROFILE, spData);
                        rsmDataBundle.putParcelable(OSOSalesPersonFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmDataBundle.putParcelable(OSOSalesPersonFragment.INVOICE_PROFILE, invoiceProfile);
                        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, rsmDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        spData = (OSORSMSalesModel) eventObject.getObject();
                        Bundle customerDataBundle = new Bundle();
                        customerDataBundle.putString(OSOCustomerFragment.USER_ID, userId);
                        customerDataBundle.putString(OSOCustomerFragment.USER_LEVEL, level);

                        rsmPos = spPos + iPos + 1;
                        customerDataBundle.putInt(OSOCustomerFragment.RSM_POS, rsmPos);
                        customerDataBundle.putInt(OSOCustomerFragment.SP_POS, spPos);
                        customerDataBundle.putInt(OSOCustomerFragment.INVOICE_POS, iPos);

                        customerDataBundle.putBoolean(OSOCustomerFragment.FROM_RSM, true);
                        customerDataBundle.putBoolean(OSOCustomerFragment.FROM_SP, fromCustomer);
                        customerDataBundle.putBoolean(OSOCustomerFragment.FROM_INVOICE, fromInvoice);
                        customerDataBundle.putParcelable(OSOCustomerFragment.RSM_PROFILE, spData);
                        customerDataBundle.putParcelable(OSOCustomerFragment.SP_PROFILE, customerProfile);
                        customerDataBundle.putParcelable(OSOCustomerFragment.INVOICE_PROFILE, invoiceProfile);
                        customerDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, customerDataBundle);
                        break;
                    case ClickEvents.PRODUCT_MENU_SELECT:
                        spData = (OSORSMSalesModel) eventObject.getObject();
                        Bundle productDataBundle = new Bundle();
                        productDataBundle.putString(OSOInvoiceFragment.USER_ID, userId);
                        productDataBundle.putString(OSOInvoiceFragment.USER_LEVEL, level);
                        productDataBundle.putBoolean(OSOInvoiceFragment.FROM_RSM, true);
                        productDataBundle.putBoolean(OSOInvoiceFragment.FROM_CUSTOMER, fromCustomer);

                        rsmPos = spPos + cPos + 1;
                        productDataBundle.putInt(OSOInvoiceFragment.RSM_POS, rsmPos);
                        productDataBundle.putInt(OSOInvoiceFragment.SP_POS, spPos);
                        productDataBundle.putInt(OSOInvoiceFragment.CUSTOMER_POS, cPos);

                        productDataBundle.putBoolean(OSOInvoiceFragment.FROM_SP, fromSP);
                        productDataBundle.putParcelable(OSOInvoiceFragment.RSM_PROFILE, spData);
                        productDataBundle.putParcelable(OSOInvoiceFragment.CUSTOMER_PROFILE, customerProfile);
                        productDataBundle.putParcelable(OSOInvoiceFragment.SP_PROFILE, invoiceProfile);
                        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_INVOICE_FRAGMENT, productDataBundle);
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

    @OnClick(R.id.iviClose)
    public void filterClose() {
        fromSP = false;
        fromCustomer = false;
        fromInvoice = false;
        spProfile = null;
        customerProfile = null;
        invoiceProfile = null;
        cviRSMHeading.setVisibility(View.GONE);
        type = 1;
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "RSM", "", "", "", "", "",""));
    }

    @OnClick(R.id.iviR1Close)
    public void r1Close() {
        if (spPos == 1) {
            fromSP = false;
            spProfile = null;
            spPos = 0;
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
            if (spPos == 2) {
                spPos = 1;
            } else if (spPos == 4) {
                spPos = 2;
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
            if (iPos == 4) {
                iPos = 2;
            }
        } else if (cPos == 2) {
            fromCustomer = false;
            customerProfile = null;
            cPos = 0;
            if (spPos == 4) {
                spPos = 2;
            }
            if (iPos == 4) {
                iPos = 2;
            }
        } else if (iPos == 2) {
            fromInvoice = false;
            invoiceProfile = null;
            iPos = 0;
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
            cPos = 0;
        } else if (iPos == 4) {
            fromInvoice = false;
            invoiceProfile = null;
            iPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromSP || fromCustomer || fromInvoice) {
            cviRSMHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = spPos + cPos + iPos;

        if (totalPosition == 7) {
            row3Display();
        } else if (totalPosition == 3) {
            row2Display();
        } else if (totalPosition == 1) {
            iviR1Close.setVisibility(View.GONE);
            row1Display();
        }

        String sales = "", customer = "", invoiceNo = "";
        if (null != spProfile)
            sales = spProfile.getTMC();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        if (null != invoiceProfile)
            invoiceNo = invoiceProfile.getInvoiceNo();

        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "RSM", "", sales, customer, "", invoiceNo, ""));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
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
            tviR1Name.setText(spProfile.getName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(spProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(spProfile.getYTD()));
            //tviAch.setText(spProfile.getYTDPercentage().intValue() + "%");
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
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            /*if (customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }*/
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
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTD()));
            //tviAch.setText(invoiceProfile.getYTDPercentage().intValue() + "%");
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
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
            tviR2Name.setText(spProfile.getName());
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(spProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(spProfile.getYTD()));
            //tviAch.setText(spProfile.getYTDPercentage().intValue() + "%");
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
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            /*if (customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }*/
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
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTD()));
            //tviAch.setText(invoiceProfile.getYTDPercentage().intValue() + "%");
        }
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
           /* if (customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }*/
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getInvoiceNo());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
    }

    private void row3Display() {
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
            tviR3Name.setText(spProfile.getName());
            tviR3SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(spProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(spProfile.getYTD()));
            //tviAch.setText(spProfile.getYTDPercentage().intValue() + "%");
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
            tviR3SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            /*if (customerProfile.getStateCodeWise().size() == 1) {
                tviR3StateName.setVisibility(View.VISIBLE);
                tviR3StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR3StateName.setVisibility(View.GONE);
            }*/
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
            tviR3SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTDTarget()));
            //tviActual.setText(BAMUtil.getRoundOffValue(invoiceProfile.getYTD()));
            //tviAch.setText(invoiceProfile.getYTDPercentage().intValue() + "%");
        }

        if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            /*if (customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }*/
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getInvoiceNo());
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            /*if (customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }*/
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getInvoiceNo());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
    }

    private void initRSMData(String type) {
        rsmAdapter = new OSORSMAdapter(dashboardActivityContext, type, level, rsmDataList, fromSP, fromCustomer, fromInvoice);
        rviRSM.setAdapter(rsmAdapter);
    }

}
