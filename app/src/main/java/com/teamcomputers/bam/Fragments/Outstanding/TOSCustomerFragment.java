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
import com.teamcomputers.bam.Adapters.TotalOutstanding.TOCustomerAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.CustomerFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSProductFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSRSMFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSSalesPersonFragment;
import com.teamcomputers.bam.Models.TotalOutstanding.TOCustomerModel;
import com.teamcomputers.bam.Models.TotalOutstanding.TOProductModel;
import com.teamcomputers.bam.Models.TotalOutstanding.TORSMSalesModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
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
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class TOSCustomerFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String PRODUCT_PROFILE = "PRODUCT_PROFILE";
    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_PRODUCT = "FROM_PRODUCT";
    public static final String RSM_POS = "RSM_POS";
    public static final String SP_POS = "SP_POS";
    public static final String PRODUCT_POS = "PRODUCT_POS";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    boolean fromRSM, fromSP, fromProduct, search = false;
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
    private TOCustomerAdapter adapter;
    private int position = 0, bar = 0, rsmPos = 0, spPos = 0, cPos = 0, pPos = 0;

    TORSMSalesModel rsmProfile, salesProfile;
    TOProductModel productProfile;
    List<TOCustomerModel> model = new ArrayList<>();

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

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromSP = getArguments().getBoolean(FROM_SP);
        fromProduct = getArguments().getBoolean(FROM_PRODUCT);

        rsmPos = getArguments().getInt(RSM_POS);
        spPos = getArguments().getInt(SP_POS);
        pPos = getArguments().getInt(PRODUCT_POS);

        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);
        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        salesProfile = getArguments().getParcelable(SP_PROFILE);
        productProfile = getArguments().getParcelable(PRODUCT_PROFILE);

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
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            TOCustomerModel[] data = (TOCustomerModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), TOCustomerModel[].class);
                            model = new ArrayList<TOCustomerModel>(Arrays.asList(data));
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
                        acctDataBundle.putParcelable(CustomerFragment.ACCT_PROFILE, model.get(position));
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
                            TOCustomerModel customerList = (TOCustomerModel) eventObject.getObject();
                            Bundle customerBundle = new Bundle();
                            customerBundle.putString(WSProductFragment.USER_ID, userId);
                            customerBundle.putString(WSProductFragment.USER_LEVEL, level);

                            cPos = rsmPos + spPos + 1;
                            customerBundle.putInt(WSProductFragment.RSM_POS, rsmPos);
                            customerBundle.putInt(WSProductFragment.SP_POS, spPos);
                            customerBundle.putInt(WSProductFragment.CUSTOMER_POS, cPos);

                            customerBundle.putBoolean(WSProductFragment.FROM_RSM, fromRSM);
                            customerBundle.putBoolean(WSProductFragment.FROM_SP, fromSP);
                            customerBundle.putBoolean(WSProductFragment.FROM_CUSTOMER, true);

                            customerBundle.putParcelable(WSProductFragment.CUSTOMER_PROFILE, customerList);
                            customerBundle.putParcelable(WSProductFragment.RSM_PROFILE, rsmProfile);
                            customerBundle.putParcelable(WSProductFragment.SP_PROFILE, salesProfile);
                            customerBundle.putInt(WSProductFragment.STATE_CODE, 1);
                            if(null!=customerList.getStateCodeWise()) {
                                customerBundle.putInt(WSProductFragment.STATE_CODE, 1);
                            } else {
                                customerBundle.putInt(WSProductFragment.STATE_CODE, 0);
                            }
                            customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.TOS_PRODUCT_FRAGMENT, customerBundle);
                        }
                        break;
                    case ClickEvents.STATE_ITEM:
                        if (!fromProduct) {
                            Bundle productStateBundle = new Bundle();
                            TOCustomerModel salesCustomerModel = (TOCustomerModel) eventObject.getObject();
                            productStateBundle.putString(WSProductFragment.USER_ID, userId);
                            productStateBundle.putString(WSProductFragment.USER_LEVEL, level);

                            cPos = rsmPos + spPos + 1;
                            productStateBundle.putInt(WSProductFragment.RSM_POS, rsmPos);
                            productStateBundle.putInt(WSProductFragment.SP_POS, spPos);
                            productStateBundle.putInt(WSProductFragment.CUSTOMER_POS, cPos);

                            productStateBundle.putBoolean(WSProductFragment.FROM_RSM, fromRSM);
                            productStateBundle.putBoolean(WSProductFragment.FROM_SP, fromSP);
                            productStateBundle.putBoolean(WSProductFragment.FROM_CUSTOMER, true);

                            productStateBundle.putParcelable(WSProductFragment.CUSTOMER_PROFILE, salesCustomerModel);
                            productStateBundle.putParcelable(WSProductFragment.RSM_PROFILE, rsmProfile);
                            productStateBundle.putParcelable(WSProductFragment.SP_PROFILE, salesProfile);
                            if(null!=salesCustomerModel.getStateCodeWise()) {
                                productStateBundle.putInt(WSProductFragment.STATE_CODE, 1);
                            } else {
                                productStateBundle.putInt(WSProductFragment.STATE_CODE, 0);
                            }
                            productStateBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                            dashboardActivityContext.replaceFragment(Fragments.TOS_PRODUCT_FRAGMENT, productStateBundle);
                        }
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        TOCustomerModel selectedCustomerList = (TOCustomerModel) eventObject.getObject();
                        Bundle rsmBundle = new Bundle();
                        rsmBundle.putString(WSRSMFragment.USER_ID, userId);
                        rsmBundle.putString(WSRSMFragment.USER_LEVEL, level);

                        cPos = spPos + pPos + 1;
                        rsmBundle.putInt(WSRSMFragment.SP_POS, spPos);
                        rsmBundle.putInt(WSRSMFragment.PRODUCT_POS, pPos);
                        rsmBundle.putInt(WSRSMFragment.CUSTOMER_POS, cPos);

                        rsmBundle.putBoolean(WSRSMFragment.FROM_PRODUCT, fromProduct);
                        rsmBundle.putBoolean(WSRSMFragment.FROM_SP, fromSP);
                        rsmBundle.putBoolean(WSRSMFragment.FROM_CUSTOMER, true);

                        rsmBundle.putParcelable(WSRSMFragment.CUSTOMER_PROFILE, selectedCustomerList);
                        rsmBundle.putParcelable(WSRSMFragment.PRODUCT_PROFILE, productProfile);
                        rsmBundle.putParcelable(WSRSMFragment.SP_PROFILE, salesProfile);
                        if(null!=selectedCustomerList.getStateCodeWise()) {
                            rsmBundle.putInt(WSProductFragment.STATE_CODE, 1);
                        } else {
                            rsmBundle.putInt(WSProductFragment.STATE_CODE, 0);
                        }
                        rsmBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_RSM_FRAGMENT, rsmBundle);
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        TOCustomerModel customerList = (TOCustomerModel) eventObject.getObject();
                        Bundle customerBundle = new Bundle();
                        customerBundle.putString(WSSalesPersonFragment.USER_ID, userId);
                        customerBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);

                        cPos = rsmPos + pPos + 1;
                        customerBundle.putInt(WSSalesPersonFragment.RSM_POS, rsmPos);
                        customerBundle.putInt(WSSalesPersonFragment.CUSTOMER_POS, cPos);
                        customerBundle.putInt(WSSalesPersonFragment.PRODUCT_POS, pPos);

                        customerBundle.putBoolean(WSSalesPersonFragment.FROM_RSM, fromRSM);
                        customerBundle.putBoolean(WSSalesPersonFragment.FROM_PRODUCT, fromProduct);
                        customerBundle.putBoolean(WSSalesPersonFragment.FROM_CUSTOMER, true);

                        customerBundle.putParcelable(WSSalesPersonFragment.CUSTOMER_PROFILE, customerList);
                        customerBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, rsmProfile);
                        customerBundle.putParcelable(WSSalesPersonFragment.PRODUCT_PROFILE, productProfile);
                        if(null!=customerList.getStateCodeWise()) {
                            customerBundle.putInt(WSSalesPersonFragment.STATE_CODE, 1);
                        } else {
                            customerBundle.putInt(WSSalesPersonFragment.STATE_CODE, 0);
                        }
                        customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_ACCOUNT_FRAGMENT, customerBundle);
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
        fromProduct = false;
        rsmProfile = null;
        salesProfile = null;
        productProfile = null;
        rsmPos = 0;
        spPos = 0;
        pPos = 0;
        cviSPHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Customer", "", "", "", "", ""));
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
        if (fromRSM || fromSP || fromProduct) {
            cviSPHeading.setVisibility(View.VISIBLE);
        }

        int totalPosition = rsmPos + spPos + pPos;

        if (totalPosition == 7) {
            row3Display();
        } else if (totalPosition == 3) {
            row2Display();
        } else if (totalPosition == 1) {
            iviR1Close.setVisibility(View.GONE);
            row1Display();
        }
        String rsm = "", sales = "", product = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTMC();
        if (null != salesProfile)
            sales = salesProfile.getTMC();
        if (null != productProfile)
            product = productProfile.getCode();
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Customer", rsm, sales, "", "", product));

    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        if (rsmPos == 1) {
            position = rsmProfile.getPosition();
            /*if (position == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDSO()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (pPos == 1) {
            position = productProfile.getPosition();
            /*if (position == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(productProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
            //tviDSO.setText(BAMUtil.getRoundOffValue(rsmProfile.getDSO()));
            //tviMTD.setText(BAMUtil.getRoundOffValue(productProfile.getMTD()));
        } else if (spPos == 1) {
            position = salesProfile.getPosition();
            /*if (position == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(salesProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (salesProfile.getDSO()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        if (rsmPos == 2) {
            position = rsmProfile.getPosition();
            /*if (position == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDSO()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 2) {
            position = salesProfile.getPosition();
            /*if (position == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(salesProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (salesProfile.getDSO()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (pPos == 2) {
            position = productProfile.getPosition();
            /*if (position == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(productProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
            //tviDSO.setText(BAMUtil.getRoundOffValue(productProfile.getDSO()));
            //tviMTD.setText(BAMUtil.getRoundOffValue(productProfile.getMTD()));
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getName());
        }
    }

    private void row3Display() {
        if (rsmPos == 4) {
            position = rsmProfile.getPosition();
            /*if (position == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDSO()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 4) {
            position = salesProfile.getPosition();
            /*if (position == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(salesProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(salesProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (salesProfile.getDSO()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (pPos == 4) {
            position = productProfile.getPosition();
            /*if (position == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llSPLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(productProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(productProfile.getAmount()));
            llDSO.setVisibility(View.GONE);
            //tviDSO.setText(BAMUtil.getRoundOffValue(productProfile.getDSO()));
            //tviMTD.setText(BAMUtil.getRoundOffValue(productProfile.getMTD()));
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (spPos == 2) {
            tviR2Name.setText(salesProfile.getName());
        } else if (pPos == 2) {
            tviR2Name.setText(productProfile.getName());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(salesProfile.getName());
        } else if (pPos == 1) {
            tviR1Name.setText(productProfile.getName());
        }
    }

    private void initData() {
        adapter = new TOCustomerAdapter(dashboardActivityContext, userId, level, model, fromRSM, fromSP, fromProduct);
        rviRSM.setAdapter(adapter);
    }
}
