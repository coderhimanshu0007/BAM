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
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters.KPSORSMAdapter;
import com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters.KPSORSMFilterAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOCustomerModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOProductModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSORSMModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOSOModel;
import com.teamcomputers.bam.Models.WSModels.PSOModels.PSOFilter;
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

public class OSORSMFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String RSM_POSITION = "RSM_POSITION";
    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String SO_PROFILE = "SO_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String STATE_CODE = "STATE_CODE";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_SO = "FROM_SO";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    public static final String SP_POS = "SP_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";
    public static final String SO_POS = "SO_POS";
    public static final String PRODUCT_POS = "PRODUCT_POS";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String toolbarTitle = "";

    String userId = "", level = "", type = "";
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

    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    //private OSORSMAdapter rsmAdapter;
    private KPSORSMAdapter rsmAdapter;
    private int pos = 0, stateCode = 0, rsmPos = 0, spPos = 0, cPos = 0, soPos = 0, pPos = 0, filterSelectedPos = 0;
    boolean fromSP, fromCustomer, fromSO, fromProduct, search = false;

    KPSORSMModel RSMdata;
    KPSORSMModel.Datum selectedRSMData;
    List<KPSORSMModel.Datum> rsmDataList = new ArrayList<>();
    List<KPSORSMModel.Datum> filterRSMList = new ArrayList<>();
    PSOFilter rsmFilterData;
    KPSOCustomerModel.Datum customerProfile;
    KPSORSMModel.Datum spProfile;
    KPSOSOModel.Datum soProfile;
    KPSOProductModel.Datum productProfile;

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
        fromSO = getArguments().getBoolean(FROM_SO);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);

        spPos = getArguments().getInt(SP_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);
        soPos = getArguments().getInt(SO_POS);
        pPos = getArguments().getInt(PRODUCT_POS);

        spProfile = getArguments().getParcelable(SP_PROFILE);
        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        soProfile = getArguments().getParcelable(SO_PROFILE);
        stateCode = getArguments().getInt(STATE_CODE);
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);

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
        dashboardActivityContext.showOSOTab(level);
        dashboardActivityContext.OSORSMClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return OSORSMFragment.class.getSimpleName();
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
                    case Events.GET_RSM_OSO_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            /*JSONArray jsonArray = new JSONArray(KBAMUtils.replaceDataResponse(eventObject.getObject().toString()));
                            OSORSMSalesModel[] data = (OSORSMSalesModel[]) KBAMUtils.fromJson(String.valueOf(jsonArray), OSORSMSalesModel[].class);
                            rsmDataList = new ArrayList<OSORSMSalesModel>(Arrays.asList(data));*/
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()));
                            RSMdata = (KPSORSMModel) KBAMUtils.fromJson(String.valueOf(jsonObject), KPSORSMModel.class);
                            rsmDataList = RSMdata.getData();
                            for (int i = 0; i < rsmDataList.size(); i++) {
                                if (rsmDataList.get(i).getName().equals("") || rsmDataList.get(i).getName().equals("null")) {
                                    rsmDataList.remove(i);
                                }
                            }
                            rsmFilterData = RSMdata.getFilter();
                            tviSOAmount.setText(KBAMUtils.getRoundOffValue(rsmFilterData.getSoAmount()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        type = "YTD";
                        initRSMData();
                        dismissProgress();
                        break;
                    case Events.GET_RSM_OSO_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_CLICK:
                        selectedRSMData = (KPSORSMModel.Datum) eventObject.getObject();
                        Bundle rsmDataBundle = new Bundle();
                        rsmDataBundle.putString(OSOSalesPersonFragment.USER_ID, userId);
                        rsmDataBundle.putString(OSOSalesPersonFragment.USER_LEVEL, level);

                        rsmPos = cPos + soPos + pPos + 1;
                        rsmDataBundle.putInt(OSOSalesPersonFragment.RSM_POS, rsmPos);
                        rsmDataBundle.putInt(OSOSalesPersonFragment.CUSTOMER_POS, cPos);
                        rsmDataBundle.putInt(OSOSalesPersonFragment.SO_POS, soPos);
                        rsmDataBundle.putInt(OSOSalesPersonFragment.PRODUCT_POS, pPos);

                        rsmDataBundle.putBoolean(OSOSalesPersonFragment.FROM_RSM, true);
                        rsmDataBundle.putBoolean(OSOSalesPersonFragment.FROM_CUSTOMER, fromCustomer);
                        rsmDataBundle.putBoolean(OSOSalesPersonFragment.FROM_SO, fromSO);
                        rsmDataBundle.putBoolean(OSOSalesPersonFragment.FROM_PRODUCT, fromProduct);

                        rsmDataBundle.putParcelable(OSOSalesPersonFragment.RSM_PROFILE, selectedRSMData);
                        rsmDataBundle.putParcelable(OSOSalesPersonFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmDataBundle.putParcelable(OSOSalesPersonFragment.SO_PROFILE, soProfile);
                        rsmDataBundle.putParcelable(OSOSalesPersonFragment.PRODUCT_PROFILE, productProfile);

                        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, rsmDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        selectedRSMData = (KPSORSMModel.Datum) eventObject.getObject();
                        Bundle customerDataBundle = new Bundle();
                        customerDataBundle.putString(OSOCustomerFragment.USER_ID, userId);
                        customerDataBundle.putString(OSOCustomerFragment.USER_LEVEL, level);

                        rsmPos = spPos + soPos + pPos + 1;
                        customerDataBundle.putInt(OSOCustomerFragment.RSM_POS, rsmPos);
                        customerDataBundle.putInt(OSOCustomerFragment.SP_POS, spPos);
                        customerDataBundle.putInt(OSOCustomerFragment.SO_POS, soPos);
                        customerDataBundle.putInt(OSOCustomerFragment.PRODUCT_POS, pPos);

                        customerDataBundle.putBoolean(OSOCustomerFragment.FROM_RSM, true);
                        customerDataBundle.putBoolean(OSOCustomerFragment.FROM_SP, fromCustomer);
                        customerDataBundle.putBoolean(OSOCustomerFragment.FROM_SO, fromSO);
                        customerDataBundle.putBoolean(OSOCustomerFragment.FROM_PRODUCT, fromProduct);

                        customerDataBundle.putParcelable(OSOCustomerFragment.RSM_PROFILE, selectedRSMData);
                        customerDataBundle.putParcelable(OSOCustomerFragment.SP_PROFILE, spProfile);
                        customerDataBundle.putParcelable(OSOCustomerFragment.SO_PROFILE, soProfile);
                        customerDataBundle.putParcelable(OSOCustomerFragment.PRODUCT_PROFILE, productProfile);

                        customerDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, customerDataBundle);
                        break;
                    case ClickEvents.PRODUCT_MENU_SELECT:
                        selectedRSMData = (KPSORSMModel.Datum) eventObject.getObject();
                        Bundle productDataBundle = new Bundle();
                        productDataBundle.putString(OSOInvoiceFragment.USER_ID, userId);
                        productDataBundle.putString(OSOInvoiceFragment.USER_LEVEL, level);

                        rsmPos = spPos + cPos + pPos + 1;

                        productDataBundle.putInt(OSOInvoiceFragment.RSM_POS, rsmPos);
                        productDataBundle.putInt(OSOInvoiceFragment.SP_POS, spPos);
                        productDataBundle.putInt(OSOInvoiceFragment.CUSTOMER_POS, cPos);
                        productDataBundle.putInt(OSOInvoiceFragment.PRODUCT_POS, pPos);

                        productDataBundle.putBoolean(OSOInvoiceFragment.FROM_RSM, true);
                        productDataBundle.putBoolean(OSOInvoiceFragment.FROM_CUSTOMER, fromCustomer);
                        productDataBundle.putBoolean(OSOInvoiceFragment.FROM_SP, fromSP);
                        productDataBundle.putBoolean(OSOInvoiceFragment.FROM_PRODUCT, fromProduct);

                        productDataBundle.putParcelable(OSOInvoiceFragment.RSM_PROFILE, selectedRSMData);
                        productDataBundle.putParcelable(OSOInvoiceFragment.CUSTOMER_PROFILE, customerProfile);
                        productDataBundle.putParcelable(OSOInvoiceFragment.SP_PROFILE, spProfile);
                        productDataBundle.putParcelable(OSOInvoiceFragment.PRODUCT_PROFILE, productProfile);

                        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_INVOICE_FRAGMENT, productDataBundle);
                        break;
                    case ClickEvents.SO_ITEM_SELECT:
                        selectedRSMData = (KPSORSMModel.Datum) eventObject.getObject();
                        Bundle productBundle = new Bundle();
                        productBundle.putString(OSOProductFragment.USER_ID, userId);
                        productBundle.putString(OSOProductFragment.USER_LEVEL, level);

                        rsmPos = cPos + spPos + soPos + 1;

                        productBundle.putInt(OSOProductFragment.RSM_POS, rsmPos);
                        productBundle.putInt(OSOProductFragment.SP_POS, spPos);
                        productBundle.putInt(OSOProductFragment.SO_POS, soPos);
                        productBundle.putInt(OSOProductFragment.CUSTOMER_POS, cPos);

                        productBundle.putBoolean(OSOProductFragment.FROM_RSM, true);
                        productBundle.putBoolean(OSOProductFragment.FROM_SP, fromSP);
                        productBundle.putBoolean(OSOProductFragment.FROM_SO, fromSO);
                        productBundle.putBoolean(OSOProductFragment.FROM_CUSTOMER, fromCustomer);

                        productBundle.putParcelable(OSOProductFragment.RSM_PROFILE, selectedRSMData);
                        productBundle.putParcelable(OSOProductFragment.SP_PROFILE, spProfile);
                        productBundle.putParcelable(OSOProductFragment.CUSTOMER_PROFILE, customerProfile);
                        productBundle.putParcelable(OSOProductFragment.SO_PROFILE, soProfile);

                        productBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OSO_PRODUCT_FRAGMENT, productBundle);
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
        soPos = 0;
        pPos = 0;
        fromSP = false;
        fromCustomer = false;
        fromSO = false;
        fromProduct = false;
        spProfile = null;
        customerProfile = null;
        soProfile = null;
        productProfile = null;
        cviRSMHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "RSM", "", "", "", "", "", ""));
        //BackgroundExecutor.getInstance().execute(new KSalesOpenOrderAprRequester(userId, level, "RSM", "", "", "", "", "", ""));
        BackgroundExecutor.getInstance().execute(new KSalesOpenOrderJunRequester(userId, level, "RSM", "", "", "", "", "", "", "", "", "", "", "", ""));
    }

    @OnClick(R.id.iviR1Close)
    public void r1Close() {
        if (spPos == 1) {
            spPos = 0;
            fromSP = false;
            spProfile = null;
        } else if (spPos == 2) {
            spPos = 1;
        } else if (spPos == 4) {
            spPos = 2;
        } else if (spPos == 8) {
            spPos = 4;
        }
        if (cPos == 1) {
            fromCustomer = false;
            customerProfile = null;
            tviR1StateName.setText("");
            cPos = 0;
        } else if (cPos == 2) {
            cPos = 1;
        } else if (cPos == 4) {
            cPos = 2;
        } else if (cPos == 8) {
            cPos = 4;
        }
        if (soPos == 1) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
        } else if (soPos == 2) {
            soPos = 1;
        } else if (soPos == 4) {
            soPos = 2;
        } else if (soPos == 8) {
            soPos = 4;
        }
        if (pPos == 1) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        } else if (pPos == 2) {
            pPos = 1;
        } else if (pPos == 4) {
            pPos = 2;
        } else if (pPos == 8) {
            pPos = 4;
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR2Close)
    public void r2Close() {
        if (spPos == 2) {
            spPos = 0;
            fromSP = false;
            spProfile = null;
        } else if (spPos == 4) {
            spPos = 2;
        } else if (spPos == 8) {
            spPos = 4;
        }
        if (cPos == 2) {
            fromCustomer = false;
            customerProfile = null;
            tviR1StateName.setText("");
            cPos = 0;
        } else if (cPos == 4) {
            cPos = 2;
        } else if (cPos == 8) {
            cPos = 4;
        }
        if (soPos == 2) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
        } else if (soPos == 4) {
            soPos = 2;
        } else if (soPos == 8) {
            soPos = 4;
        }
        if (pPos == 2) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        } else if (pPos == 4) {
            pPos = 2;
        } else if (pPos == 8) {
            pPos = 4;
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR3Close)
    public void r3Close() {
        if (spPos == 4) {
            spPos = 0;
            fromSP = false;
            spProfile = null;
        } else if (spPos == 8) {
            spPos = 4;
        }
        if (cPos == 4) {
            fromCustomer = false;
            customerProfile = null;
            tviR1StateName.setText("");
            cPos = 0;
        } else if (cPos == 8) {
            cPos = 4;
        }
        if (soPos == 4) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
        } else if (soPos == 8) {
            soPos = 4;
        }
        if (pPos == 4) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        } else if (pPos == 8) {
            pPos = 4;
        }
        rowsDisplay();
    }

    @OnClick(R.id.iviR4Close)
    public void r4Close() {
        if (spPos == 8) {
            spPos = 0;
            fromSP = false;
            spProfile = null;
        }
        if (cPos == 8) {
            fromCustomer = false;
            customerProfile = null;
            tviR1StateName.setText("");
            cPos = 0;
        }
        if (soPos == 8) {
            fromSO = false;
            soProfile = null;
            soPos = 0;
        }
        if (pPos == 8) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromSP || fromCustomer || fromSO || fromProduct) {
            cviRSMHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = spPos + cPos + soPos + pPos;

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

        String sales = "", customer = "", invoiceNo = "", state = "", product = "";
        if (null != spProfile)
            sales = spProfile.getTmc();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        if (null != soProfile)
            invoiceNo = soProfile.getSoNumber();
        if (stateCode == 1)
            state = customerProfile.getStateCodeWise().get(0).getStateCode();
        if (null != productProfile)
            product = productProfile.getCode();

        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "RSM", "", sales, customer, state, invoiceNo, ""));
        //BackgroundExecutor.getInstance().execute(new KSalesOpenOrderAprRequester(userId, level, "RSM", "", sales, customer, state, invoiceNo, product));
        BackgroundExecutor.getInstance().execute(new KSalesOpenOrderJunRequester(userId, level, "RSM", "", sales, customer, state, product, invoiceNo, "", "", "", "", "", ""));
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
            tviR1Name.setText(spProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSoAmount()));
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
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR1StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
        } else if (soPos == 1) {
            pos = soProfile.getPosition();
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
            tviR1Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
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
            tviR1Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
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
            tviR2Name.setText(spProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSoAmount()));
            //tviTarget.setText(KBAMUtils.getRoundOffValue(spProfile.getYTDTarget()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(spProfile.getYTD()));
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
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR2StateName.setVisibility(View.VISIBLE);
                tviR2StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR2StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
            //tviTarget.setText(KBAMUtils.getRoundOffValue(customerProfile.getYTD()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(customerProfile.getQTD()));
            //tviAch.setText(KBAMUtils.getRoundOffValue(customerProfile.getMTD()));
        } else if (soPos == 2) {
            pos = soProfile.getPosition();
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
            tviR2Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
            //tviTarget.setText(KBAMUtils.getRoundOffValue(soProfile.getYTDTarget()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(soProfile.getYTD()));
            //tviAch.setText(soProfile.getYTDPercentage().intValue() + "%");
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
            tviR2Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
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
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
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
            tviR3Name.setText(spProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSoAmount()));
            //tviTarget.setText(KBAMUtils.getRoundOffValue(spProfile.getYTDTarget()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(spProfile.getYTD()));
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
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR3StateName.setVisibility(View.VISIBLE);
                tviR3StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR3StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
            //tviTarget.setText(KBAMUtils.getRoundOffValue(customerProfile.getYTD()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(customerProfile.getQTD()));
            //tviAch.setText(KBAMUtils.getRoundOffValue(customerProfile.getMTD()));
        } else if (soPos == 4) {
            pos = soProfile.getPosition();
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
            tviR3Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
            //tviTarget.setText(KBAMUtils.getRoundOffValue(soProfile.getYTDTarget()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(soProfile.getYTD()));
            //tviAch.setText(soProfile.getYTDPercentage().intValue() + "%");
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
            tviR3Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }

        if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
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
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
            //tviR2SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
        }
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
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
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getProductName());
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSOAmount()));
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
            tviR4Name.setText(spProfile.getName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(spProfile.getSoAmount()));
            //tviTarget.setText(KBAMUtils.getRoundOffValue(spProfile.getYTDTarget()));
            //tviActual.setText(KBAMUtils.getRoundOffValue(spProfile.getYTD()));
            //tviAch.setText(spProfile.getYTDPercentage().intValue() + "%");
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
            tviR4Name.setText(customerProfile.getCustomerName());
            if (null != customerProfile.getStateCodeWise() && customerProfile.getStateCodeWise().size() == 1) {
                tviR4StateName.setVisibility(View.VISIBLE);
                tviR4StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getSoAmount()));
            } else {
                tviR4StateName.setVisibility(View.GONE);
                tviSOAmount.setText(KBAMUtils.getRoundOffValue(customerProfile.getSOAmount()));
            }
        } else if (soPos == 8) {
            pos = soProfile.getPosition();
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
            tviR4Name.setText(soProfile.getSoNumber());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(soProfile.getSoAmount()));
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
            tviR4Name.setText(productProfile.getProductName());
            tviSOAmount.setText(KBAMUtils.getRoundOffValue(productProfile.getSOAmount()));
        }

        if (spPos == 4) {
            tviR3Name.setText(spProfile.getName());
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
        } else if (pPos == 4) {
            tviR3Name.setText(productProfile.getProductName());
        }
        if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
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
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getProductName());
        }
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
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

        KPSORSMFilterAdapter filterAdapter = new KPSORSMFilterAdapter(dashboardActivityContext, rsmDataList);
        rviFilterList.setAdapter(filterAdapter);

        tviApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                if (filterRSMList.size() > 0) {
                    rsmAdapter = new KPSORSMAdapter(dashboardActivityContext, type, level, filterRSMList, fromSP, fromCustomer, fromSO, fromProduct);
                } else {
                    filterRSMList.clear();
                    rsmAdapter = new KPSORSMAdapter(dashboardActivityContext, type, level, rsmDataList, fromSP, fromCustomer, fromSO, fromProduct);
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
                rsmAdapter = new KPSORSMAdapter(dashboardActivityContext, type, level, rsmDataList, fromSP, fromCustomer, fromSO, fromProduct);
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
        //rsmAdapter = new OSORSMAdapter(dashboardActivityContext, type, level, rsmDataList, fromSP, fromCustomer, fromSO);
        rsmAdapter = new KPSORSMAdapter(dashboardActivityContext, type, level, rsmDataList, fromSP, fromCustomer, fromSO, fromProduct);
        rviRSM.setAdapter(rsmAdapter);
    }

}
