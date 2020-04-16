package com.teamcomputers.bam.Fragments.NewSalesReceivable;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.SalesOutstanding.CustomSpinnerAdapter;
import com.teamcomputers.bam.Adapters.SalesOutstanding.QMDialogAdapter;
import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.CustomView.CircularSeekBar;
import com.teamcomputers.bam.CustomView.TextProgressBar;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSCustomerFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSRSMFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSSalesPersonFragment;
import com.teamcomputers.bam.Models.FiscalYearModel;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.NewYTDQTDModel;
import com.teamcomputers.bam.Models.SalesReceivableModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.FiscalYearRequester;
import com.teamcomputers.bam.Requesters.SalesReceivable.SalesReceivableRequester;
import com.teamcomputers.bam.Requesters.SalesReceivable.SalesReceivablesFiscalRequester;
import com.teamcomputers.bam.Requesters.SalesReceivable.YTDQTDRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;

public class NewSalesReceivableFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    String toolbarTitle = "", fiscalYear = "";

    @BindView(R.id.spinnYear)
    Spinner spinnYear;
    @BindView(R.id.tviTargetYTD)
    TextView tviTargetYTD;
    @BindView(R.id.tviTargetQTD)
    TextView tviTargetQTD;
    @BindView(R.id.tviTargetMTD)
    TextView tviTargetMTD;
    @BindView(R.id.tviActualYTD)
    TextView tviActualYTD;
    @BindView(R.id.tviActualQTD)
    TextView tviActualQTD;
    @BindView(R.id.tviActualMTD)
    TextView tviActualMTD;
    @BindView(R.id.tviOpenSalesOrder)
    TextView tviOpenSalesOrder;
    @BindView(R.id.tviOutstanding)
    TextView tviOutstanding;
    @BindView(R.id.tviDays)
    TextView tviDays;
    @BindView(R.id.seek_bar)
    CircularSeekBar seek_bar;
    LoginModel loginModel;

    @BindView(R.id.progressBarYTD)
    TextProgressBar progressBarYTD;
    @BindView(R.id.progressBarMTD)
    TextProgressBar progressBarMTD;
    @BindView(R.id.progressBarQTD)
    TextProgressBar progressBarQTD;
    FiscalYearModel fiscalYearModel = new FiscalYearModel();
    CustomSpinnerAdapter customSpinnerAdapter;

    int days = 260;
    String type = null;
    String userId = null;
    String level = null;
    String dataType = null;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_sales_receivable, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        dashboardActivityContext = (DashboardActivity) context;
        toolbarTitle = getString(R.string.Heading_Sales_Receivable);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        loginModel = SharedPreferencesController.getInstance(BAMApplication.getInstance()).getUserProfile();

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_screen_share);
        item.setVisible(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        seek_bar.setProgress(0);
        //seek_bar.setProgress(250);
        tviDays.setText(String.valueOf(0));
        seek_bar.setCircleProgressColorGreen();
        dashboardActivityContext.hideTab();
        dashboardActivityContext.hideOSOTab();
        dashboardActivityContext.hideTOSTab();
        showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new SalesRefreshRequester());
        BackgroundExecutor.getInstance().execute(new FiscalYearRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return NewSalesReceivableFragment.class.getSimpleName();
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
                    case Events.GET_SALES_REFRESH_SUCCESSFULL:
                        dashboardActivityContext.updateDate(eventObject.getObject().toString());
                        BackgroundExecutor.getInstance().execute(new SalesReceivableRequester(loginModel.getUserID()));
                        break;
                    case Events.GET_SALES_REFRESH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_FISCAL_YEAR_LIST_SUCCESSFULL:
                        //JSONObject jsonObject = new JSONObject(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                        fiscalYearModel = (FiscalYearModel) eventObject.getObject();
                        //dashboardActivityContext.fiscalYearModel = fiscalYearModel;
                        //dashboardActivityContext.selectedFiscalYear = fiscalYearModel.getFascialYear().get(0).getYear();
                        //dashboardActivityContext.selectedPosition = 0;
                        //dashboardActivityContext.updateDate(fiscalYearModel.getLastTimeRefreshed());
                        //Creating the ArrayAdapter instance having the country list
                        customSpinnerAdapter = new CustomSpinnerAdapter(dashboardActivityContext, fiscalYearModel);
                        //Setting the ArrayAdapter data on the Spinner
                        spinnYear.setAdapter(customSpinnerAdapter);
                        BackgroundExecutor.getInstance().execute(new SalesReceivablesFiscalRequester(loginModel.getUserID(), fiscalYear));
                        break;
                    case Events.GET_FISCAL_YEAR_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_RECEIVABLE_REFRESH_SUCCESSFULL:
                        dashboardActivityContext.updateDate(eventObject.getObject().toString());
                        dismissProgress();
                        break;
                    case Events.GET_RECEIVABLE_REFRESH_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SALES_RECEIVABLE_FISCAL_SUCCESSFULL:
                        dismissProgress();
                        SalesReceivableModel model = new SalesReceivableModel();
                        try {
                            JSONObject jsonObject = new JSONObject(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            model = (SalesReceivableModel) BAMUtil.fromJson(String.valueOf(jsonObject), SalesReceivableModel.class);
                            //if (model != null)
                            //    SharedPreferencesController.getInstance(dashboardActivityContext).setAcknowledgementData(model);

                            userId = model.getUserId();
                            level = model.getLevel();
                            dashboardActivityContext.userId = userId;
                            dashboardActivityContext.level = level;
                            if (!level.equals("null")) {
                                dataType = "RSM";
                                tviTargetYTD.setText(BAMUtil.getRoundOffValue(model.getYTDTarget()));
                                tviTargetQTD.setText(BAMUtil.getRoundOffValue(model.getQTDTarget()));
                                tviTargetMTD.setText(BAMUtil.getRoundOffValue(model.getMTDTarget()));

                                tviActualYTD.setText(BAMUtil.getRoundOffValue(model.getYTD()));
                                tviActualQTD.setText(BAMUtil.getRoundOffValue(model.getQTD()));
                                tviActualMTD.setText(BAMUtil.getRoundOffValue(model.getMTD()));

                                int progressYtd = (model.getYTDPercentage()).intValue();
                                progressBarYTD.setProgress(progressYtd);
                                progressBarYTD.setText(progressYtd + "%");
                                if (progressYtd < 35) {
                                    progressBarYTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
                                } else if (progressYtd >= 35 && progressYtd < 70) {
                                    progressBarYTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
                                } else if (progressYtd >= 70) {
                                    progressBarYTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
                                }

                                int progressQtd = (model.getQTDPercentage()).intValue();
                                progressBarQTD.setProgress(progressQtd);
                                progressBarQTD.setText(progressQtd + "%");
                                if (progressQtd < 35) {
                                    progressBarQTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
                                } else if (progressQtd >= 35 && progressQtd < 70) {
                                    progressBarQTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
                                } else if (progressQtd >= 70) {
                                    progressBarQTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
                                }
                                //progressBarQTD.setProgressDrawable();

                                int progressMtd = (model.getMTDPercentage()).intValue();
                                progressBarMTD.setProgress(progressMtd);
                                progressBarMTD.setText(progressMtd + "%");
                                if (progressMtd < 35) {
                                    progressBarMTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
                                } else if (progressMtd >= 35 && progressMtd < 70) {
                                    progressBarMTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
                                } else if (progressMtd >= 70) {
                                    progressBarMTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
                                }

                                tviOpenSalesOrder.setText(BAMUtil.getRoundOffValue(model.getOpenSalesOrder()));
                                tviOutstanding.setText(BAMUtil.getRoundOffValue(model.getOutStanding()));
                                tviDays.setText(BAMUtil.getRoundOffValue(model.getDSO()));

                                seek_bar.setMax(365);
                                Integer dso = model.getDSO().intValue();
                                seek_bar.setProgress(dso);
                                //seek_bar.setProgress(250);
                                tviDays.setText(String.valueOf(dso));
                                if (dso > 30)
                                    seek_bar.setCircleProgressColorRed();
                                seek_bar.setEnabled(false);
                            } else if (level.equals("null")) {
                                dashboardActivityContext.onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Events.GET_YTDQTD_SUCCESSFULL:
                        NewYTDQTDModel ytdqtdModel = (NewYTDQTDModel) BAMUtil.fromJson(String.valueOf(eventObject.getObject()), NewYTDQTDModel.class);
                        dismissProgress();
                        showDialog(type, ytdqtdModel);
                        break;
                    case Events.GET_YTDQTD_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_SALES_RECEIVABLE_FISCAL_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void toogleProfile(Boolean toogle) {
        super.toogleProfile(toogle);
        if (toogle) {
            //llProfile.setVisibility(View.VISIBLE);
            //Toast.makeText(dashboardActivityContext, "Toogle true", Toast.LENGTH_SHORT).show();
        } else {
            //llProfile.setVisibility(View.GONE);
            //Toast.makeText(dashboardActivityContext, "Toogle false", Toast.LENGTH_SHORT).show();
        }
    }

    AlertDialog alertDialog;

    @OnItemSelected(R.id.spinnYear)
    public void itemSelected(Spinner spinner, int position) {
        fiscalYear = fiscalYearModel.getFascialYear().get(position).getYear();
        dashboardActivityContext.selectedFiscalYear = fiscalYearModel.getFascialYear().get(position).getYear();
        dashboardActivityContext.selectedPosition = position;
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new SalesReceivablesFiscalRequester(loginModel.getUserID(), fiscalYear));
    }

    @OnClick(R.id.llMonthly)
    public void MonthlyClick() {
        type = "MONTHLY";
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new YTDQTDRequester(userId));
    }

    @OnClick(R.id.llQuarterly)
    public void QuarterlyClick() {
        type = "QUARTERLY";
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new YTDQTDRequester(userId));
    }

    @OnClick(R.id.txtSalesAnalysis)
    public void salesAnalysis() {
        if (level.equals("R1")) {
            Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId);
            rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level);
            rsmDataBundle.putString(WSRSMFragment.FISCAL_YEAR, fiscalYear);
            rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
            dashboardActivityContext.replaceFragment(Fragments.WS_RSM_FRAGMENT, rsmDataBundle);
        } else if (level.equals("R2") || level.equals("R3")) {
            /*Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId);
            rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.SALES_ANALYSIS_FRAGMENT, rsmDataBundle);*/
            Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(WSSalesPersonFragment.USER_ID, userId);
            rsmDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);
            rsmDataBundle.putString(WSSalesPersonFragment.FISCAL_YEAR, fiscalYear);
            rsmDataBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, null);
            rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.WS_ACCOUNT_FRAGMENT, rsmDataBundle);
        } else if (level.equals("R4")) {
            /*Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId);
            rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.CUSTOMER_ANALYSIS_FRAGMENT, rsmDataBundle);*/
            Bundle spDataBundle = new Bundle();
            spDataBundle.putString(WSCustomerFragment.USER_ID, userId);
            spDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
            spDataBundle.putString(WSCustomerFragment.FISCAL_YEAR, fiscalYear);
            spDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, false);
            spDataBundle.putBoolean(WSCustomerFragment.FROM_SP, false);
            spDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, false);
            spDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, null);
            spDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, null);
            spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.WS_CUSTOMER_FRAGMENT, spDataBundle);
        }
        //EventBus.getDefault().post(new EventObject(Events.SALESANALYSIS, null));
    }

    @OnClick(R.id.txtBtnOpenSellsOrderAnalysis)
    public void btnOpenSellsOrderAnalysis() {
        if (level.equals("R1")) {
            Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId);
            rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level);
            rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
            dashboardActivityContext.replaceFragment(Fragments.OSO_RSM_FRAGMENT, rsmDataBundle);
        } else if (level.equals("R2") || level.equals("R3")) {
            Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(WSSalesPersonFragment.USER_ID, userId);
            rsmDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);
            rsmDataBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, null);
            rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, rsmDataBundle);
        } else if (level.equals("R4")) {
            Bundle spDataBundle = new Bundle();
            spDataBundle.putString(WSCustomerFragment.USER_ID, userId);
            spDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
            spDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, false);
            spDataBundle.putBoolean(WSCustomerFragment.FROM_SP, false);
            spDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, false);
            spDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, null);
            spDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, null);
            spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, spDataBundle);
        }
    }


    @OnClick(R.id.txtBtnOutstandingAnalysis)
    public void btnOutstandingAnalysis() {
        if (level.equals("R1")) {
            Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId);
            rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level);
            rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
            dashboardActivityContext.replaceFragment(Fragments.TOS_RSM_FRAGMENT, rsmDataBundle);
        } else if (level.equals("R2") || level.equals("R3")) {
            Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(WSSalesPersonFragment.USER_ID, userId);
            rsmDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);
            rsmDataBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, null);
            rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.TOS_ACCOUNT_FRAGMENT, rsmDataBundle);
        } else if (level.equals("R4")) {
            Bundle spDataBundle = new Bundle();
            spDataBundle.putString(WSCustomerFragment.USER_ID, userId);
            spDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
            spDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, false);
            spDataBundle.putBoolean(WSCustomerFragment.FROM_SP, false);
            spDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, false);
            spDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, null);
            spDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, null);
            spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.TOS_CUSTOMER_FRAGMENT, spDataBundle);
        }
    }


    @OnClick(R.id.txtBtnDSOAnalysis)
    public void btnDSOAnalysis() {

    }


    public void showDialog(String type, NewYTDQTDModel ytdqtdModel) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(dashboardActivityContext);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.qm_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        LinearLayoutManager layoutManager;

        TextView tviDialogType = (TextView) dialogView.findViewById(R.id.tviDialogType);
        ImageView iviCloseDialogType = (ImageView) dialogView.findViewById(R.id.iviCloseDialogType);
        TextView tviQTDHeading = (TextView) dialogView.findViewById(R.id.tviQTDHeading);

        RecyclerView rviData = (RecyclerView) dialogView.findViewById(R.id.rviData);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        /*LinearLayout llQTD = (LinearLayout) dialogView.findViewById(R.id.llQTD);
        LinearLayout llYTD = (LinearLayout) dialogView.findViewById(R.id.llYTD);

        TextView tviQTDHeading1 = (TextView) dialogView.findViewById(R.id.tviQTDHeading1);
        TextView tviQTDHeading2 = (TextView) dialogView.findViewById(R.id.tviQTDHeading2);
        TextView tviQTDHeading3 = (TextView) dialogView.findViewById(R.id.tviQTDHeading3);

        TextView tviQTDValue1 = (TextView) dialogView.findViewById(R.id.tviQTDValue1);
        TextView tviQTDValue2 = (TextView) dialogView.findViewById(R.id.tviQTDValue2);
        TextView tviQTDValue3 = (TextView) dialogView.findViewById(R.id.tviQTDValue3);

        TextView tviYTDHeading1 = (TextView) dialogView.findViewById(R.id.tviYTDHeading1);
        TextView tviYTDHeading2 = (TextView) dialogView.findViewById(R.id.tviYTDHeading2);
        TextView tviYTDHeading3 = (TextView) dialogView.findViewById(R.id.tviYTDHeading3);
        TextView tviYTDHeading4 = (TextView) dialogView.findViewById(R.id.tviYTDHeading4);

        TextView tviYTDValue1 = (TextView) dialogView.findViewById(R.id.tviYTDValue1);
        TextView tviYTDValue2 = (TextView) dialogView.findViewById(R.id.tviYTDValue2);
        TextView tviYTDValue3 = (TextView) dialogView.findViewById(R.id.tviYTDValue3);
        TextView tviYTDValue4 = (TextView) dialogView.findViewById(R.id.tviYTDValue4);*/

        tviDialogType.setText(type);

        if (type.equals("QUARTERLY")) {
            tviQTDHeading.setText("QUARTERLY");
            //llQTD.setVisibility(View.GONE);
            //llYTD.setVisibility(View.VISIBLE);
            List<NewYTDQTDModel.QTD> qtd = ytdqtdModel.getQTD();
            /*tviYTDHeading1.setText(ytd.get(0).getName());
            tviYTDHeading2.setText(ytd.get(1).getName());
            tviYTDHeading3.setText(ytd.get(2).getName());
            tviYTDHeading4.setText(ytd.get(3).getName());
            tviYTDValue1.setText(BAMUtil.getRoundOffValue(ytd.get(0).getValue()));
            tviYTDValue2.setText(BAMUtil.getRoundOffValue(ytd.get(1).getValue()));
            tviYTDValue3.setText(BAMUtil.getRoundOffValue(ytd.get(2).getValue()));
            tviYTDValue4.setText(BAMUtil.getRoundOffValue(ytd.get(3).getValue()));*/
            QMDialogAdapter qmDialogAdapter = new QMDialogAdapter(dashboardActivityContext, qtd);
            rviData.setAdapter(qmDialogAdapter);
        } else if (type.equals("MONTHLY")) {
            tviQTDHeading.setText("MONTH");
            //llQTD.setVisibility(View.VISIBLE);
            //llYTD.setVisibility(View.GONE);
            List<NewYTDQTDModel.QTD> qtd = new ArrayList<>();

            for (int i = 0; i < ytdqtdModel.getMTD().size(); i++) {
                NewYTDQTDModel.QTD ytd = new NewYTDQTDModel.QTD();
                ytd.setName(ytdqtdModel.getMTD().get(i).getName());
                ytd.setTarget(ytdqtdModel.getMTD().get(i).getTarget());
                ytd.setActual(ytdqtdModel.getMTD().get(i).getActual());
                ytd.setPercentage(ytdqtdModel.getMTD().get(i).getPercentage());

                qtd.add(ytd);
            }
            //List<NewYTDQTDModel.YTD> qtd = ytdqtdModel.getQTD();
            /*tviQTDHeading1.setText(qtd.get(0).getName());
            tviQTDHeading2.setText(qtd.get(1).getName());
            tviQTDHeading3.setText(qtd.get(2).getName());
            tviQTDValue1.setText(BAMUtil.getRoundOffValue(qtd.get(0).getValue()));
            tviQTDValue2.setText(BAMUtil.getRoundOffValue(qtd.get(1).getValue()));
            tviQTDValue3.setText(BAMUtil.getRoundOffValue(qtd.get(2).getValue()));*/
            QMDialogAdapter qmDialogAdapter = new QMDialogAdapter(dashboardActivityContext, qtd);
            rviData.setAdapter(qmDialogAdapter);
        }

        iviCloseDialogType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

}