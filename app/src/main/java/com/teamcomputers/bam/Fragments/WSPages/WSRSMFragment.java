package com.teamcomputers.bam.Fragments.WSPages;

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
import com.teamcomputers.bam.Adapters.WSAdapters.SalesAdapter.KSalesRSMAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.WSModels.SalesModels.KSalesCustomerModel;
import com.teamcomputers.bam.Models.WSModels.SalesModels.KSalesProductModel;
import com.teamcomputers.bam.Models.WSModels.SalesModels.KSalesRSMModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.WSRequesters.KSalesListAprRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

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

public class WSRSMFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String FISCAL_YEAR = "FISCAL_YEAR";
    public static final String RSM_POSITION = "RSM_POSITION";
    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String STATE_CODE = "STATE_CODE";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    public static final String SP_POS = "SP_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";
    public static final String PRODUCT_POS = "PRODUCT_POS";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

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
    @BindView(R.id.pBar)
    ProgressBar pBar;
    @BindView(R.id.tviPSO)
    TextView tviPSO;
    @BindView(R.id.viYTD)
    View viYTD;
    @BindView(R.id.viQTD)
    View viQTD;
    @BindView(R.id.viMTD)
    View viMTD;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    //private NewRSMAdapter rsmAdapter;
    private KSalesRSMAdapter rsmAdapter;
    private int pos = 0, stateCode = 0, rsmPos = 0, spPos = 0, cPos = 0, pPos = 0, bar = 0;
    boolean fromSP, fromCustomer, fromProduct, search = false, selectYear = false;

    //FullSalesModel spData;
    KSalesRSMModel RSMdata;
    KSalesRSMModel.Data selectedRSMData, spProfile;
    KSalesRSMModel.Filter rsmFilterData;
    List<KSalesRSMModel.Data> rsmDataList = new ArrayList<>();
    KSalesCustomerModel.Data customerProfile;
    KSalesProductModel.Data productProfile;
    //CustomSpinnerAdapter customSpinnerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ws_rsm, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);
        fiscalYear = getArguments().getString(FISCAL_YEAR);
        fromSP = getArguments().getBoolean(FROM_SP);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);

        spPos = getArguments().getInt(SP_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);
        pPos = getArguments().getInt(PRODUCT_POS);

        spProfile = getArguments().getParcelable(SP_PROFILE);
        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);
        stateCode = getArguments().getInt(STATE_CODE);

        toolbarTitle = getString(R.string.Heading_RSM);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        tviFiscalYear.setText("FY" + fiscalYear.substring(0, 4));

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
        selectYear = false;
        dashboardActivityContext.showTab(level);
        dashboardActivityContext.rSMClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return WSRSMFragment.class.getSimpleName();
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
                    case Events.GET_RSM_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            //Previous API Response Code
                            /*JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            rsmDataList = new ArrayList<FullSalesModel>(Arrays.asList(data));*/
                            //New API Response Code
                            JSONObject jsonObject = new JSONObject(BAMUtil.replaceWSDataResponse(eventObject.getObject().toString()));
                            RSMdata = (KSalesRSMModel) BAMUtil.fromJson(String.valueOf(jsonObject), KSalesRSMModel.class);
                            rsmDataList = RSMdata.getData();
                            rsmFilterData = RSMdata.getFilter();
                            tviTarget.setText(BAMUtil.getRoundOffValue(rsmFilterData.getTargetYTD()));
                            tviActual.setText(BAMUtil.getRoundOffValue(rsmFilterData.getYtd()));
                            bar = (rsmFilterData.getYtdPercentage()).intValue();
                            pBar.setVisibility(View.VISIBLE);
                            tviAch.setText(bar + "%");
                            pBar.setProgress(bar);
                            tviPSO.setText(BAMUtil.getRoundOffValue(rsmFilterData.getSoAmount()));
                            if (bar < 50) {
                                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
                            } else if (bar >= 50 && bar < 80) {
                                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN);
                            } else if (bar >= 80 && bar < 99) {
                                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN);
                            } else if (bar >= 99) {
                                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initRSMData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_RSM_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_CLICK:
                        //spData = (FullSalesModel) eventObject.getObject();
                        selectedRSMData = (KSalesRSMModel.Data) eventObject.getObject();
                        Bundle rsmDataBundle = new Bundle();
                        rsmDataBundle.putString(WSSalesPersonFragment.USER_ID, userId);
                        rsmDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);
                        rsmDataBundle.putString(WSSalesPersonFragment.FISCAL_YEAR, fiscalYear);
                        rsmDataBundle.putBoolean(WSSalesPersonFragment.FROM_RSM, true);
                        rsmDataBundle.putBoolean(WSSalesPersonFragment.FROM_CUSTOMER, fromCustomer);
                        rsmDataBundle.putBoolean(WSSalesPersonFragment.FROM_PRODUCT, fromProduct);

                        rsmPos = cPos + pPos + 1;
                        rsmDataBundle.putInt(WSSalesPersonFragment.RSM_POS, rsmPos);
                        rsmDataBundle.putInt(WSSalesPersonFragment.CUSTOMER_POS, cPos);
                        rsmDataBundle.putInt(WSSalesPersonFragment.PRODUCT_POS, pPos);

                        rsmDataBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, selectedRSMData);
                        rsmDataBundle.putParcelable(WSSalesPersonFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmDataBundle.putParcelable(WSSalesPersonFragment.PRODUCT_PROFILE, productProfile);
                        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.WS_ACCOUNT_FRAGMENT, rsmDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        //spData = (FullSalesModel) eventObject.getObject();
                        selectedRSMData = (KSalesRSMModel.Data) eventObject.getObject();
                        Bundle customerDataBundle = new Bundle();
                        customerDataBundle.putString(WSCustomerFragment.USER_ID, userId);
                        customerDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
                        customerDataBundle.putString(WSCustomerFragment.FISCAL_YEAR, fiscalYear);

                        rsmPos = spPos + pPos + 1;
                        customerDataBundle.putInt(WSCustomerFragment.RSM_POS, rsmPos);
                        customerDataBundle.putInt(WSCustomerFragment.SP_POS, spPos);
                        customerDataBundle.putInt(WSCustomerFragment.PRODUCT_POS, pPos);

                        customerDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, true);
                        customerDataBundle.putBoolean(WSCustomerFragment.FROM_SP, fromCustomer);
                        customerDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, fromProduct);
                        customerDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, selectedRSMData);
                        customerDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, customerProfile);
                        customerDataBundle.putParcelable(WSCustomerFragment.PRODUCT_PROFILE, productProfile);
                        customerDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.WS_CUSTOMER_FRAGMENT, customerDataBundle);
                        break;
                    case ClickEvents.PRODUCT_MENU_SELECT:
                        //spData = (FullSalesModel) eventObject.getObject();
                        selectedRSMData = (KSalesRSMModel.Data) eventObject.getObject();
                        Bundle productDataBundle = new Bundle();
                        productDataBundle.putString(WSProductFragment.USER_ID, userId);
                        productDataBundle.putString(WSProductFragment.USER_LEVEL, level);
                        productDataBundle.putString(WSProductFragment.FISCAL_YEAR, fiscalYear);
                        productDataBundle.putBoolean(WSProductFragment.FROM_RSM, true);
                        productDataBundle.putBoolean(WSProductFragment.FROM_CUSTOMER, fromCustomer);

                        rsmPos = spPos + cPos + 1;
                        productDataBundle.putInt(WSProductFragment.RSM_POS, rsmPos);
                        productDataBundle.putInt(WSProductFragment.SP_POS, spPos);
                        productDataBundle.putInt(WSProductFragment.CUSTOMER_POS, cPos);

                        productDataBundle.putBoolean(WSProductFragment.FROM_SP, fromSP);
                        productDataBundle.putParcelable(WSProductFragment.RSM_PROFILE, selectedRSMData);
                        productDataBundle.putParcelable(WSProductFragment.CUSTOMER_PROFILE, customerProfile);
                        productDataBundle.putParcelable(WSProductFragment.SP_PROFILE, productProfile);
                        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.WS_PRODUCT_FRAGMENT, productDataBundle);
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
            /*txtSearch.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            txtSearch.setVisibility(View.GONE);
                        }
                    });*/
        }
    }

    @OnClick(R.id.tviYTD)
    public void tviYTD() {
        viYTD.setVisibility(View.VISIBLE);
        viQTD.setVisibility(View.INVISIBLE);
        viMTD.setVisibility(View.INVISIBLE);
        tviTarget.setText(BAMUtil.getRoundOffValue(rsmFilterData.getTargetYTD()));
        tviActual.setText(BAMUtil.getRoundOffValue(rsmFilterData.getYtd()));
        bar = (rsmFilterData.getYtdPercentage()).intValue();
        pBar.setVisibility(View.VISIBLE);
        tviAch.setText(bar + "%");
        pBar.setProgress(bar);
        if (bar < 50) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 50 && bar < 80) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 80 && bar < 99) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 99) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        }
        initRSMData("YTD");
        rsmAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tviQTD)
    public void tviQTD() {
        viYTD.setVisibility(View.INVISIBLE);
        viQTD.setVisibility(View.VISIBLE);
        viMTD.setVisibility(View.INVISIBLE);
        tviTarget.setText(BAMUtil.getRoundOffValue(rsmFilterData.getTargetQTD()));
        tviActual.setText(BAMUtil.getRoundOffValue(rsmFilterData.getQtd()));
        bar = (rsmFilterData.getYtdPercentage()).intValue();
        pBar.setVisibility(View.VISIBLE);
        tviAch.setText(bar + "%");
        pBar.setProgress(bar);
        if (bar < 50) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 50 && bar < 80) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 80 && bar < 99) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 99) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        }
        initRSMData("QTD");
        rsmAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tviMTD)
    public void tviMTD() {
        viYTD.setVisibility(View.INVISIBLE);
        viQTD.setVisibility(View.INVISIBLE);
        viMTD.setVisibility(View.VISIBLE);
        tviTarget.setText(BAMUtil.getRoundOffValue(rsmFilterData.getTargetMTD()));
        tviActual.setText(BAMUtil.getRoundOffValue(rsmFilterData.getMtd()));
        bar = (rsmFilterData.getYtdPercentage()).intValue();
        pBar.setVisibility(View.VISIBLE);
        tviAch.setText(bar + "%");
        pBar.setProgress(bar);
        if (bar < 50) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 50 && bar < 80) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 80 && bar < 99) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 99) {
            pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        }
        initRSMData("MTD");
        rsmAdapter.notifyDataSetChanged();
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
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new FiscalSalesListRequester(userId, level, "RSM", "", "", "", "", "", fiscalYear));
        BackgroundExecutor.getInstance().execute(new KSalesListAprRequester(userId, level, "RSM", "", "", "", "", "", fiscalYear));
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
            if (stateCode == 1)
                stateCode = 0;
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
            if (stateCode == 1)
                stateCode = 0;

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
            if (stateCode == 1)
                stateCode = 0;
        } else if (pPos == 4) {
            fromProduct = false;
            productProfile = null;
            pPos = 0;
        }
        rowsDisplay();
    }

    private void rowsDisplay() {
        if (fromSP || fromCustomer || fromProduct) {
            cviRSMHeading.setVisibility(View.VISIBLE);
        } else {
            cviRSMHeading.setVisibility(View.GONE);
        }

        int totalPosition = spPos + cPos + pPos;

        if (totalPosition == 7) {
            row3Display();
        } else if (totalPosition == 3) {
            row2Display();
        } else if (totalPosition == 1) {
            iviR1Close.setVisibility(View.GONE);
            row1Display();
        }

        String sales = "", customer = "", product = "", state = "";
        if (null != spProfile)
            sales = spProfile.getTmc();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        if (null != productProfile)
            product = productProfile.getCode();
        if (stateCode == 1)
            state = customerProfile.getStateCodeWise().get(0).getStateCode();

        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new FiscalSalesListRequester(userId, level, "RSM", "", sales, customer, state, product, fiscalYear));
        BackgroundExecutor.getInstance().execute(new KSalesListAprRequester(userId, level, "RSM", "", sales, customer, state, product, fiscalYear));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        if (spPos == 1) {
            tviTargetHeading.setText("Target");
            tviActualHeading.setText("Actual");
            tviAchHeading.setText("Ach%");
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
            tviTarget.setText(BAMUtil.getRoundOffValue(spProfile.getTargetYTD()));
            tviActual.setText(BAMUtil.getRoundOffValue(spProfile.getYtd()));
            bar = (spProfile.getYtdPercentage()).intValue();
            pBar.setVisibility(View.VISIBLE);
            tviAch.setText(bar + "%");
            pBar.setProgress(bar);
            if (bar < 50) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 50 && bar < 80) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 80 && bar < 99) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 99) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            }
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
                iviR1Close.setVisibility(View.VISIBLE);
                tviR1StateName.setVisibility(View.VISIBLE);
                tviR1StateName.setText(customerProfile.getStateCodeWise().get(0).getStateCode());
                tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getYtd()));
                tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getQtd()));
                tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getMtd()));
            } else {
                tviR1StateName.setVisibility(View.GONE);
                tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYtd()));
                tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQtd()));
                tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMtd()));
            }

        } else if (pPos == 1) {
            tviTargetHeading.setText("Target");
            tviActualHeading.setText("Actual");
            tviAchHeading.setText("Ach%");
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
            tviTarget.setText(BAMUtil.getRoundOffValue(productProfile.getTargetYTD()));
            tviActual.setText(BAMUtil.getRoundOffValue(productProfile.getYtd()));
            bar = (productProfile.getYtdPercentage()).intValue();
            pBar.setVisibility(View.VISIBLE);
            tviAch.setText(bar + "%");
            pBar.setProgress(bar);
            if (bar < 50) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 50 && bar < 80) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 80 && bar < 99) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 99) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            }
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
            if (fromCustomer) {
                tviTargetHeading.setText("YTD");
                tviActualHeading.setText("QTD");
                tviAchHeading.setText("MTD");
                tviTarget.setText(BAMUtil.getRoundOffValue(spProfile.getYtd()));
                tviActual.setText(BAMUtil.getRoundOffValue(spProfile.getQtd()));
                tviAch.setText(BAMUtil.getRoundOffValue(spProfile.getMtd()));
            } else if (!fromCustomer) {
                tviTargetHeading.setText("Target");
                tviActualHeading.setText("Actual");
                tviAchHeading.setText("Ach%");
                tviTarget.setText(BAMUtil.getRoundOffValue(spProfile.getTargetYTD()));
                tviActual.setText(BAMUtil.getRoundOffValue(spProfile.getYtd()));
                bar = (spProfile.getYtdPercentage()).intValue();
                pBar.setVisibility(View.VISIBLE);
                tviAch.setText(bar + "%");
                pBar.setProgress(bar);
                if (bar < 50) {
                    pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
                } else if (bar >= 50 && bar < 80) {
                    pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN);
                } else if (bar >= 80 && bar < 99) {
                    pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN);
                } else if (bar >= 99) {
                    pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
                }
            }
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
                tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getYtd()));
                tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getQtd()));
                tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getMtd()));
            } else {
                tviR2StateName.setVisibility(View.GONE);
                tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYtd()));
                tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQtd()));
                tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMtd()));
            }

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
            if (fromCustomer) {
                tviTargetHeading.setText("YTD");
                tviActualHeading.setText("QTD");
                tviAchHeading.setText("MTD");
                tviTarget.setText(BAMUtil.getRoundOffValue(productProfile.getYtd()));
                tviActual.setText(BAMUtil.getRoundOffValue(productProfile.getQtd()));
                tviAch.setText(BAMUtil.getRoundOffValue(productProfile.getMtd()));
            } else if (!fromCustomer) {
                tviTargetHeading.setText("Target");
                tviActualHeading.setText("Actual");
                tviAchHeading.setText("Ach%");
                tviTarget.setText(BAMUtil.getRoundOffValue(productProfile.getTargetYTD()));
                tviActual.setText(BAMUtil.getRoundOffValue(productProfile.getYtd()));
                bar = (productProfile.getYtdPercentage()).intValue();
                pBar.setVisibility(View.VISIBLE);
                tviAch.setText(bar + "%");
                pBar.setProgress(bar);
                if (bar < 50) {
                    pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
                } else if (bar >= 50 && bar < 80) {
                    pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN);
                } else if (bar >= 80 && bar < 99) {
                    pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN);
                } else if (bar >= 99) {
                    pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
                }
            }
        }
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
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
        if (spPos == 4) {
            tviTargetHeading.setText("YTD");
            tviActualHeading.setText("QTD");
            tviAchHeading.setText("MTD");
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
            tviTarget.setText(BAMUtil.getRoundOffValue(spProfile.getYtd()));
            tviActual.setText(BAMUtil.getRoundOffValue(spProfile.getQtd()));
            tviAch.setText(BAMUtil.getRoundOffValue(spProfile.getQtd()));
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
                tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getYtd()));
                tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getQtd()));
                tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getStateCodeWise().get(0).getMtd()));
            } else {
                tviR3StateName.setVisibility(View.GONE);
                tviTarget.setText(BAMUtil.getRoundOffValue(customerProfile.getYtd()));
                tviActual.setText(BAMUtil.getRoundOffValue(customerProfile.getQtd()));
                tviAch.setText(BAMUtil.getRoundOffValue(customerProfile.getMtd()));
            }

        } else if (pPos == 4) {
            tviTargetHeading.setText("YTD");
            tviActualHeading.setText("QTD");
            tviAchHeading.setText("MTD");
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
            tviTarget.setText(BAMUtil.getRoundOffValue(productProfile.getYtd()));
            tviActual.setText(BAMUtil.getRoundOffValue(productProfile.getQtd()));
            tviAch.setText(BAMUtil.getRoundOffValue(productProfile.getMtd()));
        }

        if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
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
        if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
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

    private void initRSMData(String type) {
        //rsmAdapter = new NewRSMAdapter(dashboardActivityContext, type, level, rsmDataList, fromSP, fromCustomer, fromProduct);
        rsmAdapter = new KSalesRSMAdapter(dashboardActivityContext, type, level, rsmDataList, fromSP, fromCustomer, fromProduct);
        rviRSM.setAdapter(rsmAdapter);
    }

}
