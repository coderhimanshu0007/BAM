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
import com.teamcomputers.bam.Adapters.OpenSalesOrder.OSOCustomerAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.CustomerFragment;
import com.teamcomputers.bam.Models.TotalOutstanding.TOCustomerModel;
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

public class OSOCustomerFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String INVOICE_PROFILE = "INVOICE_PROFILE";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_INVOICE = "FROM_INVOICE";
    public static final String RSM_POS = "RSM_POS";
    public static final String SP_POS = "SP_POS";
    public static final String INVOICE_POS = "INVOICE_POS";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    OSORSMSalesModel rsmProfile, salesProfile;
    OSOInvoiceModel invoiceProfile;

    boolean fromRSM, fromSP, fromInvoice, search = false;
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
    @BindView(R.id.tviR1Name)
    TextView tviR1Name;
    @BindView(R.id.tviR2Name)
    TextView tviR2Name;
    @BindView(R.id.tviR3Name)
    TextView tviR3Name;
    @BindView(R.id.tviSOAmount)
    TextView tviSOAmount;
    @BindView(R.id.tviR1StateName)
    TextView tviR1StateName;
    @BindView(R.id.tviR2StateName)
    TextView tviR2StateName;
    @BindView(R.id.tviR3StateName)
    TextView tviR3StateName;
    @BindView(R.id.iviR1Close)
    ImageView iviR1Close;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private OSOCustomerAdapter adapter;
    private int position = 0, rsmPos = 0, spPos = 0, cPos = 0, iPos = 0;

    List<OSOCustomerModel> model = new ArrayList<>();

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

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromSP = getArguments().getBoolean(FROM_SP);
        fromInvoice = getArguments().getBoolean(FROM_INVOICE);

        rsmPos = getArguments().getInt(RSM_POS);
        spPos = getArguments().getInt(SP_POS);
        iPos = getArguments().getInt(INVOICE_POS);

        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);
        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        salesProfile = getArguments().getParcelable(SP_PROFILE);
        invoiceProfile = getArguments().getParcelable(INVOICE_PROFILE);

        toolbarTitle = getString(R.string.Customer);
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
        dashboardActivityContext.OSOcustomerClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return OSOCustomerFragment.class.getSimpleName();
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
                    case Events.GET_CUSTOMER_OSO_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            OSOCustomerModel[] data = (OSOCustomerModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), OSOCustomerModel[].class);
                            model = new ArrayList<OSOCustomerModel>(Arrays.asList(data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
                        dismissProgress();
                        break;
                    case Events.GET_CUSTOMER_OSO_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.ACCOUNT_ITEM:
                        int position = (int) eventObject.getObject();
                        Bundle acctDataBundle = new Bundle();
                        acctDataBundle.putParcelable(CustomerFragment.ACCT_PROFILE, model.get(position));
                        acctDataBundle.putInt(CustomerFragment.ACCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, acctDataBundle);
                        /*acctDataBundle.putParcelable(ProductFragment.PRODUCT_PROFILE, model.get(position));
                        acctDataBundle.putInt(ProductFragment.PRODUCT_POSITION, position);
                        acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.PRODUCT_FRAGMENT, acctDataBundle);*/
                        break;
                    case ClickEvents.CUSTOMER_SELECT:
                        if (!fromInvoice) {
                            OSOCustomerModel customerList = (OSOCustomerModel) eventObject.getObject();
                            Bundle customerBundle = new Bundle();
                            customerBundle.putString(OSOInvoiceFragment.USER_ID, userId);
                            customerBundle.putString(OSOInvoiceFragment.USER_LEVEL, level);

                            cPos = rsmPos + spPos + 1;
                            customerBundle.putInt(OSOInvoiceFragment.RSM_POS, rsmPos);
                            customerBundle.putInt(OSOInvoiceFragment.SP_POS, spPos);
                            customerBundle.putInt(OSOInvoiceFragment.CUSTOMER_POS, cPos);

                            customerBundle.putBoolean(OSOInvoiceFragment.FROM_RSM, fromRSM);
                            customerBundle.putBoolean(OSOInvoiceFragment.FROM_SP, fromSP);
                            customerBundle.putBoolean(OSOInvoiceFragment.FROM_CUSTOMER, true);

                            customerBundle.putParcelable(OSOInvoiceFragment.CUSTOMER_PROFILE, customerList);
                            customerBundle.putParcelable(OSOInvoiceFragment.RSM_PROFILE, rsmProfile);
                            customerBundle.putParcelable(OSOInvoiceFragment.SP_PROFILE, salesProfile);
                            customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.OSO_INVOICE_FRAGMENT, customerBundle);
                        }
                        break;
                    case ClickEvents.STATE_ITEM:
                        if (!fromInvoice) {
                            Bundle productStateBundle = new Bundle();
                            OSOCustomerModel salesCustomerModel = (OSOCustomerModel) eventObject.getObject();
                            productStateBundle.putString(OSOInvoiceFragment.USER_ID, userId);
                            productStateBundle.putString(OSOInvoiceFragment.USER_LEVEL, level);

                            cPos = rsmPos + spPos + 1;
                            productStateBundle.putInt(OSOInvoiceFragment.RSM_POS, rsmPos);
                            productStateBundle.putInt(OSOInvoiceFragment.SP_POS, spPos);
                            productStateBundle.putInt(OSOInvoiceFragment.CUSTOMER_POS, cPos);

                            productStateBundle.putBoolean(OSOInvoiceFragment.FROM_RSM, fromRSM);
                            productStateBundle.putBoolean(OSOInvoiceFragment.FROM_SP, fromSP);
                            productStateBundle.putBoolean(OSOInvoiceFragment.FROM_CUSTOMER, true);

                            productStateBundle.putParcelable(OSOInvoiceFragment.CUSTOMER_PROFILE, salesCustomerModel);
                            productStateBundle.putParcelable(OSOInvoiceFragment.RSM_PROFILE, rsmProfile);
                            productStateBundle.putParcelable(OSOInvoiceFragment.SP_PROFILE, salesProfile);
                            productStateBundle.putInt(OSOInvoiceFragment.STATE_CODE, 1);
                            productStateBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.OSO_INVOICE_FRAGMENT, productStateBundle);
                        }
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        OSOCustomerModel selectedCustomerList = (OSOCustomerModel) eventObject.getObject();
                        Bundle rsmBundle = new Bundle();
                        rsmBundle.putString(OSORSMFragment.USER_ID, userId);
                        rsmBundle.putString(OSORSMFragment.USER_LEVEL, level);

                        cPos = spPos + iPos + 1;
                        rsmBundle.putInt(OSORSMFragment.SP_POS, spPos);
                        rsmBundle.putInt(OSORSMFragment.INVOICE_POS, iPos);
                        rsmBundle.putInt(OSORSMFragment.CUSTOMER_POS, cPos);

                        rsmBundle.putBoolean(OSORSMFragment.FROM_INVOICE, fromInvoice);
                        rsmBundle.putBoolean(OSORSMFragment.FROM_SP, fromSP);
                        rsmBundle.putBoolean(OSORSMFragment.FROM_CUSTOMER, true);

                        rsmBundle.putParcelable(OSORSMFragment.CUSTOMER_PROFILE, selectedCustomerList);
                        rsmBundle.putParcelable(OSORSMFragment.INVOICE_PROFILE, invoiceProfile);
                        rsmBundle.putParcelable(OSORSMFragment.SP_PROFILE, salesProfile);
                        rsmBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_RSM_FRAGMENT, rsmBundle);
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        OSOCustomerModel customerList = (OSOCustomerModel) eventObject.getObject();
                        Bundle customerBundle = new Bundle();
                        customerBundle.putString(OSOSalesPersonFragment.USER_ID, userId);
                        customerBundle.putString(OSOSalesPersonFragment.USER_LEVEL, level);

                        cPos = rsmPos + iPos + 1;
                        customerBundle.putInt(OSOSalesPersonFragment.RSM_POS, rsmPos);
                        customerBundle.putInt(OSOSalesPersonFragment.CUSTOMER_POS, cPos);
                        customerBundle.putInt(OSOSalesPersonFragment.INVOICE_POS, iPos);

                        customerBundle.putBoolean(OSOSalesPersonFragment.FROM_RSM, fromRSM);
                        customerBundle.putBoolean(OSOSalesPersonFragment.FROM_INVOICE, fromInvoice);
                        customerBundle.putBoolean(OSOSalesPersonFragment.FROM_CUSTOMER, true);

                        customerBundle.putParcelable(OSOSalesPersonFragment.CUSTOMER_PROFILE, customerList);
                        customerBundle.putParcelable(OSOSalesPersonFragment.RSM_PROFILE, rsmProfile);
                        customerBundle.putParcelable(OSOSalesPersonFragment.INVOICE_PROFILE, invoiceProfile);
                        customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, customerBundle);
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
    public void Search(){
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
        fromSP = false;
        fromInvoice = false;
        rsmProfile = null;
        salesProfile = null;
        invoiceProfile = null;
        rsmPos = 0;
        spPos = 0;
        iPos = 0;
        cviSPHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Customer", "", "", "", "", "", ""));
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
            if (iPos == 2) {
                iPos = 1;
            } else if (iPos == 4) {
                iPos = 2;
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
            if (iPos == 4) {
                iPos = 2;
            }
        } else if (spPos == 2) {
            fromSP = false;
            salesProfile = null;
            spPos = 0;
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
        } else if (iPos == 4) {
            fromInvoice = false;
            invoiceProfile = null;
            iPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromRSM || fromSP || fromInvoice) {
            cviSPHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = rsmPos + spPos + iPos;

        if (totalPosition == 7) {
            row3Display();
        } else if (totalPosition == 3) {
            row2Display();
        } else if (totalPosition == 1) {
            iviR1Close.setVisibility(View.GONE);
            row1Display();
        }
        String rsm = "", sales = "", invoiceNo = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTMC();
        if (null != salesProfile)
            sales = salesProfile.getTMC();
        if (null != invoiceProfile)
            invoiceNo = invoiceProfile.getInvoiceNo();
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Customer", rsm, sales, "", "", invoiceNo, ""));

    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
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
            tviSOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (iPos == 1) {
            position = invoiceProfile.getPosition();
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
            tviR1Name.setText(invoiceProfile.getInvoiceNo());
            tviSOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
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
            tviSOAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getSOAmount()));
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
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
            tviSOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
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
            tviSOAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getSOAmount()));
        } else if (iPos == 2) {
            position = invoiceProfile.getPosition();
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
            tviR2Name.setText(invoiceProfile.getInvoiceNo());
            tviSOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getSOAmount()));
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getInvoiceNo());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
    }

    private void row3Display() {
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
            tviSOAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getSOAmount()));
        } else if (iPos == 4) {
            position = invoiceProfile.getPosition();
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
            tviR3Name.setText(invoiceProfile.getInvoiceNo());
            tviSOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
            tviSOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 2) {
            tviR2Name.setText(salesProfile.getName());
            tviSOAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getSOAmount()));
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getInvoiceNo());
            tviSOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getSOAmount()));
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getInvoiceNo());
            //tviR1SOAmount.setText(BAMUtil.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
    }

    private void initData() {
        adapter = new OSOCustomerAdapter(dashboardActivityContext, userId, level, model, fromRSM, fromSP, fromInvoice);
        rviRSM.setAdapter(adapter);
    }
}
