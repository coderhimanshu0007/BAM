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
import com.teamcomputers.bam.Adapters.OpenSalesOrder.OSOInvoiceAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
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
import butterknife.Unbinder;

public class OSOInvoiceFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String STATE_CODE = "STATE_CODE";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String RSM_POS = "RSM_POS";
    public static final String SP_POS = "SP_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";

    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String userId = "", level;
    boolean fromRSM, fromSP, fromCustomer;
    String toolbarTitle = "";
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
    @BindView(R.id.tviR1SOAmount)
    TextView tviR1SOAmount;
    @BindView(R.id.tviR2SOAmount)
    TextView tviR2SOAmount;
    @BindView(R.id.tviR3SOAmount)
    TextView tviR3SOAmount;
    /*@BindView(R.id.tviYtdHeading)
    TextView tviYtdHeading;
    @BindView(R.id.tviQtdHeading)
    TextView tviQtdHeading;
    @BindView(R.id.tviMtdHeading)
    TextView tviMtdHeading;*/
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private OSOInvoiceAdapter adapter;
    private int position = 0, stateCode = 0, rsmPos = 0, spPos = 0, cPos = 0, iPos = 0;
    OSOCustomerModel customerProfile;
    OSORSMSalesModel rsmProfile, spProfile;
    List<OSOInvoiceModel> model = new ArrayList<>();

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

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromSP = getArguments().getBoolean(FROM_SP);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);

        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        spProfile = getArguments().getParcelable(SP_PROFILE);
        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        stateCode = getArguments().getInt(STATE_CODE);

        toolbarTitle = getString(R.string.Invoice);
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
        dashboardActivityContext.OSOproductClick(level);
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
                    case Events.GET_INVOICE_OSO_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            OSOInvoiceModel[] data = (OSOInvoiceModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), OSOInvoiceModel[].class);
                            model = new ArrayList<OSOInvoiceModel>(Arrays.asList(data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_INVOICE_OSO_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        OSOInvoiceModel rsmMenuData = (OSOInvoiceModel) eventObject.getObject();
                        Bundle rsmMenuDataBundle = new Bundle();
                        rsmMenuDataBundle.putString(OSORSMFragment.USER_ID, userId);
                        rsmMenuDataBundle.putString(OSORSMFragment.USER_LEVEL, level);

                        iPos = spPos + cPos + 1;
                        rsmMenuDataBundle.putInt(OSORSMFragment.CUSTOMER_POS, cPos);
                        rsmMenuDataBundle.putInt(OSORSMFragment.SP_POS, spPos);
                        rsmMenuDataBundle.putInt(OSORSMFragment.INVOICE_POS, iPos);

                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_SP, fromRSM);
                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_CUSTOMER, fromCustomer);
                        rsmMenuDataBundle.putBoolean(OSORSMFragment.FROM_INVOICE, true);

                        rsmMenuDataBundle.putParcelable(OSORSMFragment.SP_PROFILE, spProfile);
                        rsmMenuDataBundle.putParcelable(OSORSMFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmMenuDataBundle.putParcelable(OSORSMFragment.INVOICE_PROFILE, rsmMenuData);
                        rsmMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_RSM_FRAGMENT, rsmMenuDataBundle);
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        OSOInvoiceModel invoiceData = (OSOInvoiceModel) eventObject.getObject();
                        Bundle productDataBundle = new Bundle();
                        productDataBundle.putString(OSOSalesPersonFragment.USER_ID, userId);
                        productDataBundle.putString(OSOSalesPersonFragment.USER_LEVEL, level);

                        iPos = rsmPos + cPos + 1;
                        productDataBundle.putInt(OSOSalesPersonFragment.RSM_POS, rsmPos);
                        productDataBundle.putInt(OSOSalesPersonFragment.CUSTOMER_POS, cPos);
                        productDataBundle.putInt(OSOSalesPersonFragment.INVOICE_POS, iPos);

                        productDataBundle.putBoolean(OSOSalesPersonFragment.FROM_RSM, fromRSM);
                        productDataBundle.putBoolean(OSOSalesPersonFragment.FROM_CUSTOMER, fromCustomer);
                        productDataBundle.putBoolean(OSOSalesPersonFragment.FROM_INVOICE, true);
                        productDataBundle.putParcelable(OSOSalesPersonFragment.RSM_PROFILE, rsmProfile);
                        productDataBundle.putParcelable(OSOSalesPersonFragment.CUSTOMER_PROFILE, customerProfile);
                        productDataBundle.putParcelable(OSOSalesPersonFragment.INVOICE_PROFILE, invoiceData);
                        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, productDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        OSOInvoiceModel invoiceModel = (OSOInvoiceModel) eventObject.getObject();
                        Bundle spDataBundle = new Bundle();
                        spDataBundle.putString(OSOCustomerFragment.USER_ID, userId);
                        spDataBundle.putString(OSOCustomerFragment.USER_LEVEL, level);

                        iPos = rsmPos + spPos + 1;
                        spDataBundle.putInt(OSOCustomerFragment.RSM_POS, rsmPos);
                        spDataBundle.putInt(OSOCustomerFragment.SP_POS, spPos);
                        spDataBundle.putInt(OSOCustomerFragment.INVOICE_POS, iPos);

                        spDataBundle.putBoolean(OSOCustomerFragment.FROM_RSM, fromRSM);
                        spDataBundle.putBoolean(OSOCustomerFragment.FROM_SP, fromSP);
                        spDataBundle.putBoolean(OSOCustomerFragment.FROM_INVOICE, true);

                        spDataBundle.putParcelable(OSOCustomerFragment.RSM_PROFILE, rsmProfile);
                        spDataBundle.putParcelable(OSOCustomerFragment.SP_PROFILE, spProfile);
                        spDataBundle.putParcelable(OSOCustomerFragment.INVOICE_PROFILE, invoiceModel);
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

    @OnClick(R.id.iviClose)
    public void filterClose() {
        fromRSM = false;
        fromSP = false;
        fromCustomer = false;
        rsmProfile = null;
        spProfile = null;
        customerProfile = null;
        cviProductHeading.setVisibility(View.GONE);
        /*tviYtdHeading.setText("TARGET");
        tviQtdHeading.setText("ACTUAL");
        tviMtdHeading.setText("ACHIEVEMENT");*/
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Invoice", "", "", "", "", "", ""));
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
        if (fromRSM || fromSP || fromCustomer) {
            cviProductHeading.setVisibility(View.VISIBLE);
        } else {
            cviProductHeading.setVisibility(View.GONE);
        }
        int totalPosition = rsmPos + spPos + cPos;

        if (totalPosition == 7) {
            row3Display();
        } else if (totalPosition == 3) {
            row2Display();
        } else if (totalPosition == 1) {
            iviR1Close.setVisibility(View.GONE);
            row1Display();
        }

        String rsm = "", sales = "", customer = "", state = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTMC();
        if (null != spProfile)
            sales = spProfile.getTMC();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        if (stateCode == 1)
            state = customerProfile.getStateCodeWise().get(0).getStateCode();
        //tviR3StateName.setText(state);
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Invoice", rsm, sales, customer, state, "", ""));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
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
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            cviProductHeading.setVisibility(View.VISIBLE);

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
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 1) {
            cviProductHeading.setVisibility(View.VISIBLE);

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
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            if (customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        if (rsmPos == 2) {
            /*tviYtdHeading.setText("YTD");
            tviQtdHeading.setText("QTD");
            tviMtdHeading.setText("MTD");*/

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
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
            //tviTarget.setText(BAMUtil.getRoundOffValue(rsmProfile.getYTD()));
            //tviActual.setText(BAMUtil.getRoundOffValue(rsmProfile.getQTD()));
            //tviAch.setText(BAMUtil.getRoundOffValue(rsmProfile.getMTD()));
        } else if (spPos == 2) {
            cviProductHeading.setVisibility(View.VISIBLE);
            /*tviYtdHeading.setText("YTD");
            tviQtdHeading.setText("QTD");
            tviMtdHeading.setText("MTD");*/

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
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 2) {
            cviProductHeading.setVisibility(View.VISIBLE);

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
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            if (customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            if (customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR1StateName.setVisibility(View.GONE);
            }
        }
    }

    private void row3Display() {
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
            tviR3SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 4) {
            cviProductHeading.setVisibility(View.VISIBLE);

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
            tviR3SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 4) {
            cviProductHeading.setVisibility(View.VISIBLE);

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
            tviR3SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            if (customerProfile.getStateCodeWise().size() == 1) {
                iviR1Close.setVisibility(View.VISIBLE);
                tviR3StateName.setVisibility(View.VISIBLE);
                tviR3StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR3StateName.setVisibility(View.GONE);
            }
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
            tviR2SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
            if (customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
            } else {
                tviR2StateName.setVisibility(View.GONE);
            }
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(spProfile.getSOAmount()));
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
            tviR1SOAmount.setText(BAMUtil.getRoundOffValue(customerProfile.getSOAmount()));
        }
    }

    private void initData(String type) {
        adapter = new OSOInvoiceAdapter(dashboardActivityContext, level, type, model, fromRSM, fromSP, fromCustomer);
        rviRSM.setAdapter(adapter);
    }
}
