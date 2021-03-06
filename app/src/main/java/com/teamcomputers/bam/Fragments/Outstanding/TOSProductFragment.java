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
import com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters.KTOProductAdapter;
import com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters.KTOProductFilterAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSCustomerFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSRSMFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSSalesPersonFragment;
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

public class TOSProductFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";

    public static final String STATE_CODE = "STATE_CODE";

    public static final String CUSTOMER_PROFILE = "CUSTOMER_PROFILE";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String SP_PROFILE = "SP_PROFILE";
    public static final String INVOICE_PROFILE = "INVOICE_PROFILE";

    public static final String FROM_RSM = "FROM_RSM";
    public static final String FROM_SP = "FROM_SP";
    public static final String FROM_CUSTOMER = "FROM_CUSTOMER";
    public static final String FROM_INVOICE = "FROM_INVOICE";

    public static final String RSM_POS = "RSM_POS";
    public static final String SP_POS = "SP_POS";
    public static final String CUSTOMER_POS = "CUSTOMER_POS";
    public static final String INVOICE_POS = "INVOICE_POS";

    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String userId = "", level;

    String toolbarTitle = "";
    @BindView(R.id.txtSearch)
    EditText txtSearch;
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
    private KTOProductAdapter adapter;
    boolean fromRSM, fromSP, fromCustomer, fromInvoice, search = false;
    private int position = 0, bar = 0, stateCode = 0, rsmPos = 0, spPos = 0, cPos = 0, pPos = 0, iPos = 0, filterSelectedPos = 0;

    KNRCustomerModel.Datum customerProfile;
    KNRRSMModel.Datum rsmProfile, spProfile;
    KNRProductModel productData;
    List<KNRProductModel.Datum> productDataList = new ArrayList<>();
    List<KNRProductModel.Datum> filterProductList = new ArrayList<>();
    Filter prductFilterData;
    KNRProductModel.Datum selectedProductData;
    KNRInvoiceModel.Datum invoiceProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_to_product_progress, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);

        rsmPos = getArguments().getInt(RSM_POS);
        spPos = getArguments().getInt(SP_POS);
        cPos = getArguments().getInt(CUSTOMER_POS);
        iPos = getArguments().getInt(INVOICE_POS);

        fromRSM = getArguments().getBoolean(FROM_RSM);
        fromSP = getArguments().getBoolean(FROM_SP);
        fromCustomer = getArguments().getBoolean(FROM_CUSTOMER);
        fromInvoice = getArguments().getBoolean(FROM_INVOICE);

        rsmProfile = getArguments().getParcelable(RSM_PROFILE);
        spProfile = getArguments().getParcelable(SP_PROFILE);
        customerProfile = getArguments().getParcelable(CUSTOMER_PROFILE);
        invoiceProfile = getArguments().getParcelable(INVOICE_PROFILE);

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
        dashboardActivityContext.TOSproductClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return TOSProductFragment.class.getSimpleName();
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
                    case Events.GET_PRODUCT_TOS_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()));
                            productData = (KNRProductModel) BAMUtil.fromJson(String.valueOf(jsonObject), KNRProductModel.class);
                            productDataList = productData.getData();
                            for (int i = 0; i < productDataList.size(); i++) {
                                if (productDataList.get(i).getProductName().equals("") || productDataList.get(i).getProductName().equals("null")) {
                                    productDataList.remove(i);
                                }
                            }
                            prductFilterData = productData.getFilter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
                        dismissProgress();
                        break;
                    case Events.GET_PRODUCT_TOS_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_MENU_SELECT:
                        selectedProductData = (KNRProductModel.Datum) eventObject.getObject();
                        Bundle rsmMenuDataBundle = new Bundle();
                        rsmMenuDataBundle.putString(WSRSMFragment.USER_ID, userId);
                        rsmMenuDataBundle.putString(WSRSMFragment.USER_LEVEL, level);

                        pPos = spPos + cPos + 1;
                        rsmMenuDataBundle.putInt(WSRSMFragment.CUSTOMER_POS, cPos);
                        rsmMenuDataBundle.putInt(WSRSMFragment.SP_POS, spPos);
                        rsmMenuDataBundle.putInt(WSRSMFragment.PRODUCT_POS, pPos);

                        rsmMenuDataBundle.putBoolean(WSRSMFragment.FROM_SP, fromRSM);
                        rsmMenuDataBundle.putBoolean(WSRSMFragment.FROM_CUSTOMER, fromCustomer);
                        rsmMenuDataBundle.putBoolean(WSRSMFragment.FROM_PRODUCT, true);

                        rsmMenuDataBundle.putParcelable(WSRSMFragment.SP_PROFILE, spProfile);
                        rsmMenuDataBundle.putParcelable(WSRSMFragment.CUSTOMER_PROFILE, customerProfile);
                        rsmMenuDataBundle.putParcelable(WSRSMFragment.PRODUCT_PROFILE, selectedProductData);
                        rsmMenuDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_RSM_FRAGMENT, rsmMenuDataBundle);
                        break;
                    case ClickEvents.SP_MENU_SELECT:
                        selectedProductData = (KNRProductModel.Datum) eventObject.getObject();
                        Bundle productDataBundle = new Bundle();
                        productDataBundle.putString(WSSalesPersonFragment.USER_ID, userId);
                        productDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);

                        pPos = rsmPos + cPos + 1;
                        productDataBundle.putInt(WSSalesPersonFragment.RSM_POS, rsmPos);
                        productDataBundle.putInt(WSSalesPersonFragment.CUSTOMER_POS, cPos);
                        productDataBundle.putInt(WSSalesPersonFragment.PRODUCT_POS, pPos);

                        productDataBundle.putBoolean(WSSalesPersonFragment.FROM_RSM, fromRSM);
                        productDataBundle.putBoolean(WSSalesPersonFragment.FROM_CUSTOMER, fromCustomer);
                        productDataBundle.putBoolean(WSSalesPersonFragment.FROM_PRODUCT, true);
                        productDataBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, rsmProfile);
                        productDataBundle.putParcelable(WSSalesPersonFragment.CUSTOMER_PROFILE, customerProfile);
                        productDataBundle.putParcelable(WSSalesPersonFragment.PRODUCT_PROFILE, selectedProductData);
                        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_ACCOUNT_FRAGMENT, productDataBundle);
                        break;
                    case ClickEvents.CUSTOMER_MENU_SELECT:
                        selectedProductData = (KNRProductModel.Datum) eventObject.getObject();
                        Bundle spDataBundle = new Bundle();
                        spDataBundle.putString(WSCustomerFragment.USER_ID, userId);
                        spDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);

                        pPos = rsmPos + spPos + 1;
                        spDataBundle.putInt(WSCustomerFragment.RSM_POS, rsmPos);
                        spDataBundle.putInt(WSCustomerFragment.SP_POS, spPos);
                        spDataBundle.putInt(WSCustomerFragment.PRODUCT_POS, pPos);

                        spDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, fromRSM);
                        spDataBundle.putBoolean(WSCustomerFragment.FROM_SP, fromSP);
                        spDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, true);

                        spDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, rsmProfile);
                        spDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, spProfile);
                        spDataBundle.putParcelable(WSCustomerFragment.PRODUCT_PROFILE, selectedProductData);
                        spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.TOS_CUSTOMER_FRAGMENT, spDataBundle);
                        break;
                    case Events.ITEM_SELECTED:
                        filterSelectedPos = (int) eventObject.getObject();
                        productDataList.get(filterSelectedPos).setSelected(true);
                        filterProductList.add(productDataList.get(filterSelectedPos));
                        break;
                    case Events.ITEM_UNSELECTED:
                        filterSelectedPos = (int) eventObject.getObject();
                        productDataList.get(filterSelectedPos).setSelected(false);
                        filterProductList.remove(productDataList.get(filterSelectedPos));
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
        pPos = 0;
        fromRSM = false;
        fromSP = false;
        fromCustomer = false;
        rsmProfile = null;
        spProfile = null;
        customerProfile = null;
        cviProductHeading.setVisibility(View.GONE);
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Product", "", "", "", "", ""));
        //BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "Product", "", "", "", "", "", "", "", ""));
        BackgroundExecutor.getInstance().execute(new KAccountReceivablesJunRequester(userId, level, "Product", "", "", "", "", "", "", "", "", "", "", "", ""));
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
        if (fromRSM || fromSP || fromCustomer || fromInvoice) {
            cviProductHeading.setVisibility(View.VISIBLE);
        } else {
            cviProductHeading.setVisibility(View.GONE);
        }
        int totalPosition = rsmPos + spPos + cPos + iPos;

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

        String rsm = "", sales = "", customer = "", state = "", invoice = "";
        if (null != rsmProfile)
            rsm = rsmProfile.getTmc();
        if (null != spProfile)
            sales = spProfile.getTmc();
        if (null != customerProfile)
            customer = customerProfile.getCustomerName();
        /*if (null != invoiceProfile)
            invoice = invoiceProfile.getDocument_No();*/
        /*if (stateCode == 1)
            state = customerProfile.getDocumentNo().get(0).getDocumentNo();*/
        //tviR3StateName.setText(state);
        //showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new OutstandingRequester(userId, level, "Product", rsm, sales, customer, state, ""));
        //BackgroundExecutor.getInstance().execute(new KAccountReceivablesAprRequester(userId, level, "Product", rsm, sales, customer, state, "", invoice, "",""));
    }

    private void row1Display() {
        rlR2.setVisibility(View.GONE);
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 1) {

            position = rsmProfile.getPosition();
            /*if (position == 0) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 1) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = spProfile.getPosition();
            /*if (position == 0) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(spProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(spProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 1) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = customerProfile.getPosition();
            /*if (position == 0) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
            } else if (position == 1) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
            } else if (position == 2) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
            } else if (position % 2 == 0) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
            } else if (position % 2 == 1) {
                llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            }*/
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(customerProfile.getCustomerName());
            llDSO.setVisibility(View.GONE);
        } else if (iPos == 1) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = invoiceProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR1Name.setText(invoiceProfile.getDocumentNo());
            llDSO.setVisibility(View.GONE);
        }
    }

    private void row2Display() {
        rlR3.setVisibility(View.GONE);
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 2) {
            position = rsmProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 2) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = spProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(spProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(spProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 2) {
            cviProductHeading.setVisibility(View.VISIBLE);
            position = customerProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(customerProfile.getCustomerName());

            llDSO.setVisibility(View.GONE);
        } else if (iPos == 1) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = invoiceProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR2Name.setText(invoiceProfile.getDocumentNo());
            llDSO.setVisibility(View.GONE);
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    private void row3Display() {
        rlR4.setVisibility(View.GONE);
        if (rsmPos == 4) {
            position = rsmProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 4) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = spProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(spProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(spProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 4) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = customerProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(customerProfile.getCustomerName());

            llDSO.setVisibility(View.GONE);
        } else if (iPos == 4) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = invoiceProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR3Name.setText(invoiceProfile.getDocumentNo());

            llDSO.setVisibility(View.GONE);
        }

        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getDocumentNo());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
        } else if (iPos == 1) {
            tviR1Name.setText(invoiceProfile.getDocumentNo());
        }
    }

    private void row4Display() {
        if (rsmPos == 8) {
            position = rsmProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(rsmProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(rsmProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (rsmProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (spPos == 8) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = spProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(spProfile.getName());
            tviAmount.setText(BAMUtil.getRoundOffValue(spProfile.getAmount()));
            llDSO.setVisibility(View.VISIBLE);
            bar = (spProfile.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        } else if (cPos == 8) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = customerProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(customerProfile.getCustomerName());

            llDSO.setVisibility(View.GONE);
        } else if (iPos == 8) {
            cviProductHeading.setVisibility(View.VISIBLE);

            position = invoiceProfile.getPosition();
            llProductLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
            tviR4Name.setText(invoiceProfile.getDocumentNo());

            llDSO.setVisibility(View.GONE);
        }

        if (rsmPos == 4) {
            tviR3Name.setText(rsmProfile.getName());
        } else if (spPos == 4) {
            tviR3Name.setText(spProfile.getName());
        } else if (cPos == 4) {
            tviR3Name.setText(customerProfile.getCustomerName());
        } else if (iPos == 4) {
            tviR3Name.setText(invoiceProfile.getDocumentNo());
        }
        if (rsmPos == 2) {
            tviR2Name.setText(rsmProfile.getName());
        } else if (spPos == 2) {
            tviR2Name.setText(spProfile.getName());
        } else if (cPos == 2) {
            tviR2Name.setText(customerProfile.getCustomerName());
        } else if (iPos == 2) {
            tviR2Name.setText(invoiceProfile.getDocumentNo());
        }
        if (rsmPos == 1) {
            tviR1Name.setText(rsmProfile.getName());
        } else if (spPos == 1) {
            tviR1Name.setText(spProfile.getName());
        } else if (cPos == 1) {
            tviR1Name.setText(customerProfile.getCustomerName());
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

        KTOProductFilterAdapter filterAdapter = new KTOProductFilterAdapter(dashboardActivityContext, productDataList);
        rviFilterList.setAdapter(filterAdapter);

        tviApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                if (filterProductList.size() > 0) {
                    adapter = new KTOProductAdapter(dashboardActivityContext, level, filterProductList, fromRSM, fromSP, fromCustomer, fromInvoice);
                } else {
                    filterProductList.clear();
                    adapter = new KTOProductAdapter(dashboardActivityContext, level, productDataList, fromRSM, fromSP, fromCustomer, fromInvoice);
                }
                rviRSM.setAdapter(adapter);
            }
        });
        tviClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                filterProductList.clear();
                for (int i = 0; i < productDataList.size(); i++) {
                    productDataList.get(i).setSelected(false);
                }
                adapter = new KTOProductAdapter(dashboardActivityContext, level, productDataList, fromRSM, fromSP, fromCustomer, fromInvoice);
                rviRSM.setAdapter(adapter);
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

    private void initData() {
        tviAmount.setText(BAMUtil.getRoundOffValue(prductFilterData.getAmount()));
        if (fromInvoice || fromCustomer) {
            llDSO.setVisibility(View.GONE);
        } else {
            llDSO.setVisibility(View.VISIBLE);
            bar = (prductFilterData.getDso()).intValue();
            tviDSO.setText(bar + " Days");
            pBar.setProgress(bar);
            if (bar < 30) {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            } else {
                pBar.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            }
        }
        /*if (fromCustomer) {
            tviOutstandingHeading.setText("CUSTOMER NAME");
            tviEmpty.setVisibility(View.VISIBLE);
            tviDSOHeading.setText("OUTSTANDING");
        } else {
            tviOutstandingHeading.setText("OUTSTANDING");
            tviEmpty.setVisibility(View.GONE);
            tviDSOHeading.setText("DSO");
        }*/
        //adapter = new TOProductAdapter(dashboardActivityContext, level, type, model, fromRSM, fromSP, fromCustomer);
        adapter = new KTOProductAdapter(dashboardActivityContext, level, productDataList, fromRSM, fromSP, fromCustomer, fromInvoice);
        rviRSM.setAdapter(adapter);
    }
}
