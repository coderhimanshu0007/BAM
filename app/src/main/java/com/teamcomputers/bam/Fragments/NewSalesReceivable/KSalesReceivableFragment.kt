package com.teamcomputers.bam.Fragments.NewSalesReceivable

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnItemSelected
import butterknife.Unbinder
import com.teamcomputers.bam.Activities.KDashboardActivity
import com.teamcomputers.bam.Adapters.SalesOutstanding.CustomSpinnerAdapter
import com.teamcomputers.bam.Adapters.SalesOutstanding.KQMDialogAdapter
import com.teamcomputers.bam.BAMApplication
import com.teamcomputers.bam.Fragments.KBaseFragment
import com.teamcomputers.bam.Fragments.WSPages.WSCustomerFragment
import com.teamcomputers.bam.Fragments.WSPages.WSRSMFragment
import com.teamcomputers.bam.Fragments.WSPages.WSSalesPersonFragment
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.FiscalYearModel
import com.teamcomputers.bam.Models.LoginModel
import com.teamcomputers.bam.Models.NewYTDQTDModel
import com.teamcomputers.bam.Models.SalesReceivableModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Requesters.SalesReceivable.SalesReceivableRequester
import com.teamcomputers.bam.Requesters.WSRequesters.KFiscalYearRequester
import com.teamcomputers.bam.Requesters.WSRequesters.KSalesReceivablesFiscalRequester
import com.teamcomputers.bam.Requesters.WSRequesters.KYTDQTDFiscalRequester
import com.teamcomputers.bam.Utils.BAMUtil
import com.teamcomputers.bam.Utils.BackgroundExecutor
import com.teamcomputers.bam.controllers.SharedPreferencesController
import kotlinx.android.synthetic.main.fragment_new_sales_receivable.*
import kotlinx.android.synthetic.main.fragment_new_sales_receivable.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class KSalesReceivableFragment : KBaseFragment() {
    private var rootView: View? = null
    private var unbinder: Unbinder? = null
    private var mContext: KDashboardActivity? = null
    internal var toolbarTitle = ""
    internal var fiscalYear = ""

    /*@BindView(R.id.spinnYear)
    internal var spinnYear: Spinner? = null
    @BindView(R.id.tviTargetYTD)
    internal var tviTargetYTD: TextView? = null
    @BindView(R.id.tviTargetQTD)
    internal var tviTargetQTD: TextView? = null
    @BindView(R.id.tviTargetMTD)
    internal var tviTargetMTD: TextView? = null
    @BindView(R.id.tviActualYTD)
    internal var tviActualYTD: TextView? = null
    @BindView(R.id.tviActualQTD)
    internal var tviActualQTD: TextView? = null
    @BindView(R.id.tviActualMTD)
    internal var tviActualMTD: TextView? = null
    @BindView(R.id.tviOpenSalesOrder)
    internal var tviOpenSalesOrder: TextView? = null
    @BindView(R.id.tviOutstanding)
    internal var tviOutstanding: TextView? = null
    @BindView(R.id.tviDays)
    internal var tviDays: TextView? = null
    @BindView(R.id.seek_bar)
    internal var seek_bar: CircularSeekBar? = null*/
    internal var loginModel: LoginModel? = null

    /*@BindView(R.id.progressBarYTD)
    internal var progressBarYTD: TextProgressBar? = null
    @BindView(R.id.progressBarMTD)
    internal var progressBarMTD: TextProgressBar? = null
    @BindView(R.id.progressBarQTD)
    internal var progressBarQTD: TextProgressBar? = null*/
    internal var fiscalYearModel = FiscalYearModel()
    internal var customSpinnerAdapter: CustomSpinnerAdapter? = null

    var days = 260
    var type: String = null.toString()
    var userId: String = ""
    var level: String = ""
    var dataType: String? = null

    override fun getFragmentName(): String {
        return KSalesReceivableFragment::class.java.simpleName
    }

    @Subscribe
    fun onEvent(eventObject: EventObject) {
        mContext?.runOnUiThread(Runnable {
            when (eventObject.id) {
                KBAMConstant.Events.NO_INTERNET_CONNECTION -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.NO_INTERNET_CONNECTION)
                }
                KBAMConstant.Events.GET_SALES_REFRESH_SUCCESSFULL -> {
                    mContext?.updateDate(eventObject.getObject().toString())
                    BackgroundExecutor.getInstance().execute(SalesReceivableRequester(loginModel?.getUserID()))
                }
                KBAMConstant.Events.GET_SALES_REFRESH_UNSUCCESSFULL -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.OOPS_MESSAGE)
                }
                KBAMConstant.Events.GET_FISCAL_YEAR_LIST_SUCCESSFULL -> {
                    //JSONObject jsonObject = new JSONObject(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                    //fiscalYearModel = (FiscalYearModel) eventObject.getObject();
                    //dashboardActivityContext.fiscalYearModel = fiscalYearModel;
                    //mContext?.selectedFiscalYear = fiscalYearModel.getFascialYear().get(0).getYear();
                    //mContext?.selectedPosition = 0;
                    fiscalYearModel = eventObject.getObject() as FiscalYearModel
                    mContext?.updateDate(fiscalYearModel.getLastTimeRefreshed())
                    //Creating the ArrayAdapter instance having the country list
                    customSpinnerAdapter = CustomSpinnerAdapter(mContext, fiscalYearModel)
                    //Setting the ArrayAdapter data on the Spinner
                    spinnYear.setAdapter(customSpinnerAdapter)
                    //BackgroundExecutor.getInstance().execute(new SalesReceivablesFiscalRequester(loginModel.getUserID(), fiscalYear));
                    BackgroundExecutor.getInstance().execute(KSalesReceivablesFiscalRequester(loginModel?.getUserID()!!, fiscalYear))
                }
                KBAMConstant.Events.GET_FISCAL_YEAR_LIST_UNSUCCESSFULL -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.OOPS_MESSAGE)
                }
                KBAMConstant.Events.GET_RECEIVABLE_REFRESH_SUCCESSFULL -> {
                    mContext?.updateDate(eventObject.getObject().toString())
                    dismissProgress()
                }
                KBAMConstant.Events.GET_RECEIVABLE_REFRESH_UNSUCCESSFULL -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.OOPS_MESSAGE)
                }
                KBAMConstant.Events.GET_SALES_RECEIVABLE_FISCAL_SUCCESSFULL -> {
                    dismissProgress()
                    var model = SalesReceivableModel()
                    try {
                        val jsonObject = JSONObject(BAMUtil.replaceDataResponse(eventObject.getObject().toString()))
                        model = BAMUtil.fromJson(jsonObject.toString(), SalesReceivableModel::class.java) as SalesReceivableModel
                        //if (model != null)
                        //    SharedPreferencesController.getInstance(dashboardActivityContext).setAcknowledgementData(model);

                        userId = model.userId
                        level = model.level
                        mContext?.userId = userId
                        mContext?.level = level
                        if (level != "null") {
                            dataType = "RSM"
                            tviTargetYTD?.setText(BAMUtil.getRoundOffValue(model.ytdTarget!!))
                            tviTargetQTD?.setText(BAMUtil.getRoundOffValue(model.qtdTarget!!))
                            tviTargetMTD?.setText(BAMUtil.getRoundOffValue(model.mtdTarget!!))

                            tviActualYTD?.setText(BAMUtil.getRoundOffValue(model.ytd!!))
                            tviActualQTD?.setText(BAMUtil.getRoundOffValue(model.qtd!!))
                            tviActualMTD?.setText(BAMUtil.getRoundOffValue(model.mtd!!))

                            val progressYtd = model.ytdPercentage.toInt()
                            progressBarYTD?.setProgress(progressYtd)
                            progressBarYTD?.setText("$progressYtd%")
                            /*if (progressYtd < 35) {
                            progressBarYTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
                        } else if (progressYtd >= 35 && progressYtd < 70) {
                            progressBarYTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
                        } else if (progressYtd >= 70) {
                            progressBarYTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
                        }*/
                            if (progressYtd < 49) {
                                progressBarYTD?.getProgressDrawable()?.setColorFilter(mContext?.getResources()?.getColor(R.color.color_progress_start)!!, PorterDuff.Mode.SRC_IN)
                            } else if (progressYtd >= 49 && progressYtd < 79) {
                                progressBarYTD?.getProgressDrawable()?.setColorFilter(mContext?.getResources()?.getColor(R.color.color_orange)!!, PorterDuff.Mode.SRC_IN)
                            } else if (progressYtd >= 79 && progressYtd < 99) {
                                progressBarYTD?.getProgressDrawable()?.setColorFilter(mContext?.getResources()?.getColor(R.color.color_amber)!!, PorterDuff.Mode.SRC_IN)
                            } else if (progressYtd >= 99) {
                                progressBarYTD?.getProgressDrawable()?.setColorFilter(mContext?.getResources()?.getColor(R.color.color_progress_end)!!, PorterDuff.Mode.SRC_IN)
                            }

                            val progressQtd = model.qtdPercentage.toInt()
                            progressBarQTD?.setProgress(progressQtd)
                            progressBarQTD?.setText("$progressQtd%")
                            /*if (progressQtd < 35) {
                            progressBarQTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
                        } else if (progressQtd >= 35 && progressQtd < 70) {
                            progressBarQTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
                        } else if (progressQtd >= 70) {
                            progressBarQTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
                        }*/
                            if (progressQtd < 49) {
                                progressBarQTD?.getProgressDrawable()?.setColorFilter(mContext?.getResources()?.getColor(R.color.color_progress_start)!!, PorterDuff.Mode.SRC_IN)
                            } else if (progressQtd >= 49 && progressQtd < 79) {
                                progressBarQTD?.getProgressDrawable()?.setColorFilter(mContext?.getResources()?.getColor(R.color.color_orange)!!, PorterDuff.Mode.SRC_IN)
                            } else if (progressQtd >= 79 && progressQtd < 99) {
                                progressBarQTD?.getProgressDrawable()?.setColorFilter(mContext?.getResources()?.getColor(R.color.color_amber)!!, PorterDuff.Mode.SRC_IN)
                            } else if (progressQtd >= 99) {
                                progressBarQTD?.getProgressDrawable()?.setColorFilter(mContext?.getResources()?.getColor(R.color.color_progress_end)!!, PorterDuff.Mode.SRC_IN)
                            }
                            //progressBarQTD.setProgressDrawable();

                            val progressMtd = model.mtdPercentage.toInt()
                            progressBarMTD.setProgress(progressMtd)
                            progressBarMTD.setText("$progressMtd%")
                            /*if (progressMtd < 35) {
                            progressBarMTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
                        } else if (progressMtd >= 35 && progressMtd < 70) {
                            progressBarMTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
                        } else if (progressMtd >= 70) {
                            progressBarMTD.getProgressDrawable().setColorFilter(dashboardActivityContext.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
                        }*/
                            if (progressMtd < 49) {
                                progressBarMTD.getProgressDrawable().setColorFilter(mContext?.getResources()?.getColor(R.color.color_progress_start)!!, PorterDuff.Mode.SRC_IN)
                            } else if (progressMtd >= 49 && progressMtd < 79) {
                                progressBarMTD.getProgressDrawable().setColorFilter(mContext?.getResources()?.getColor(R.color.color_orange)!!, PorterDuff.Mode.SRC_IN)
                            } else if (progressMtd >= 79 && progressMtd < 99) {
                                progressBarMTD.getProgressDrawable().setColorFilter(mContext?.getResources()?.getColor(R.color.color_amber)!!, PorterDuff.Mode.SRC_IN)
                            } else if (progressMtd >= 99) {
                                progressBarMTD.getProgressDrawable().setColorFilter(mContext?.getResources()?.getColor(R.color.color_progress_end)!!, PorterDuff.Mode.SRC_IN)
                            }

                            tviOpenSalesOrder.setText(BAMUtil.getRoundOffValue(model.openSalesOrder!!))
                            tviOutstanding.setText(BAMUtil.getRoundOffValue(model.outStanding!!))
                            tviDays.setText(BAMUtil.getRoundOffValue(model.dso!!))

                            seek_bar?.setMax(365f)
                            val dso = model.dso!!.toInt()
                            seek_bar.setProgress(dso.toFloat())
                            //seek_bar.setProgress(250);
                            tviDays.setText(dso.toString())
                            if (dso > 30)
                                seek_bar.setCircleProgressColorRed()
                            seek_bar.setEnabled(false)
                        } else if (level == "null") {
                            mContext?.onBackPressed()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
                KBAMConstant.Events.GET_YTDQTD_SUCCESSFULL -> {
                    val ytdqtdModel = BAMUtil.fromJson(eventObject.getObject().toString(), NewYTDQTDModel::class.java) as NewYTDQTDModel
                    dismissProgress()
                    showDialog(type, ytdqtdModel)
                }
                KBAMConstant.Events.GET_YTDQTD_UNSUCCESSFULL -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.OOPS_MESSAGE)
                }
                KBAMConstant.Events.GET_SALES_RECEIVABLE_FISCAL_UNSUCCESSFULL -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.OOPS_MESSAGE)
                }
            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_new_sales_receivable, container, false)
        unbinder = ButterKnife.bind(this, rootView!!)
        EventBus.getDefault().register(this)
        mContext = context as KDashboardActivity
        toolbarTitle = getString(R.string.Heading_Sales_Receivable)
        mContext?.setToolBarTitle(toolbarTitle)

        loginModel = SharedPreferencesController.getInstance(BAMApplication.getInstance()).userProfile
        mContext?.fragmentView = rootView

        rootView?.spinnYear?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itemSelected(position)
            }
        }

        rootView?.llMonthly?.setOnClickListener(View.OnClickListener { MonthlyClick() })
        rootView?.llQuarterly?.setOnClickListener(View.OnClickListener { QuarterlyClick() })
        rootView?.txtSalesAnalysis?.setOnClickListener(View.OnClickListener { salesAnalysis() })
        rootView?.txtBtnOpenSellsOrderAnalysis?.setOnClickListener(View.OnClickListener { btnOpenSellsOrderAnalysis() })
        rootView?.txtBtnOutstandingAnalysis?.setOnClickListener(View.OnClickListener { btnOutstandingAnalysis() })

        return rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_screen_share)
        item.isVisible = true
    }

    override fun onResume() {
        super.onResume()
        seek_bar.progress = 0f
        //seek_bar.setProgress(250);
        tviDays.text = 0.toString()
        seek_bar.setCircleProgressColorGreen()
        mContext?.hideTab()
        mContext?.hideOSOTab()
        mContext?.hideTOSTab()
        showProgress(KBAMConstant.ProgressDialogTexts.LOADING)
        //BackgroundExecutor.getInstance().execute(new SalesRefreshRequester());
        //BackgroundExecutor.getInstance().execute(new FiscalYearRequester());
        BackgroundExecutor.getInstance().execute(KFiscalYearRequester())
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
        EventBus.getDefault().unregister(this)
    }

    internal var alertDialog: AlertDialog? = null

    //@OnItemSelected(R.id.spinnYear)
    fun itemSelected(position: Int) {
        fiscalYear = fiscalYearModel.fascialYear[position].year
        mContext?.selectedFiscalYear = fiscalYearModel.fascialYear[position].year
        mContext?.selectedPosition = position
        showProgress(KBAMConstant.ProgressDialogTexts.LOADING)
        //BackgroundExecutor.getInstance().execute(new SalesReceivablesFiscalRequester(loginModel.getUserID(), fiscalYear));
        BackgroundExecutor.getInstance().execute(KSalesReceivablesFiscalRequester(loginModel?.getUserID()!!, fiscalYear))
    }

    //@OnClick(R.id.llMonthly)
    fun MonthlyClick() {
        type = "MONTHLY"
        showProgress(KBAMConstant.ProgressDialogTexts.LOADING)
        //BackgroundExecutor.getInstance().execute(new YTDQTDFiscalRequester(userId, fiscalYear));
        BackgroundExecutor.getInstance().execute(KYTDQTDFiscalRequester(userId, fiscalYear))
    }

    //@OnClick(R.id.llQuarterly)
    fun QuarterlyClick() {
        type = "QUARTERLY"
        showProgress(KBAMConstant.ProgressDialogTexts.LOADING)
        //BackgroundExecutor.getInstance().execute(new YTDQTDFiscalRequester(userId, fiscalYear));
        BackgroundExecutor.getInstance().execute(KYTDQTDFiscalRequester(userId, fiscalYear))
    }

    //@OnClick(R.id.txtSalesAnalysis)
    fun salesAnalysis() {
        if (level == "R1") {
            val rsmDataBundle = Bundle()
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId)
            rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level)
            rsmDataBundle.putString(WSRSMFragment.FISCAL_YEAR, fiscalYear)
            rsmDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
            //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
            mContext?.replaceFragment(KBAMConstant.Fragments.WS_RSM_FRAGMENT, rsmDataBundle)
        } else if (level == "R2" || level == "R3") {
            /*Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId);
            rsmDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.SALES_ANALYSIS_FRAGMENT, rsmDataBundle);*/
            val rsmDataBundle = Bundle()
            rsmDataBundle.putString(WSSalesPersonFragment.USER_ID, userId)
            rsmDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level)
            rsmDataBundle.putString(WSSalesPersonFragment.FISCAL_YEAR, fiscalYear)
            rsmDataBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, null)
            rsmDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
            mContext?.replaceFragment(KBAMConstant.Fragments.WS_ACCOUNT_FRAGMENT, rsmDataBundle)
        } else if (level == "R4") {
            /*Bundle rsmDataBundle = new Bundle();
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId);
            rsmDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
            dashboardActivityContext.replaceFragment(Fragments.CUSTOMER_ANALYSIS_FRAGMENT, rsmDataBundle);*/
            val spDataBundle = Bundle()
            spDataBundle.putString(WSCustomerFragment.USER_ID, userId)
            spDataBundle.putString(WSCustomerFragment.USER_LEVEL, level)
            spDataBundle.putString(WSCustomerFragment.FISCAL_YEAR, fiscalYear)
            spDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, false)
            spDataBundle.putBoolean(WSCustomerFragment.FROM_SP, false)
            spDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, false)
            spDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, null)
            spDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, null)
            spDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
            mContext?.replaceFragment(KBAMConstant.Fragments.WS_CUSTOMER_FRAGMENT, spDataBundle)
        }
        //EventBus.getDefault().post(new EventObject(Events.SALESANALYSIS, null));
    }

    //@OnClick(R.id.txtBtnOpenSellsOrderAnalysis)
    fun btnOpenSellsOrderAnalysis() {
        if (level == "R1") {
            val rsmDataBundle = Bundle()
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId)
            rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level)
            rsmDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
            //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
            mContext?.replaceFragment(KBAMConstant.Fragments.OSO_RSM_FRAGMENT, rsmDataBundle)
        } else if (level == "R2" || level == "R3") {
            val rsmDataBundle = Bundle()
            rsmDataBundle.putString(WSSalesPersonFragment.USER_ID, userId)
            rsmDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level)
            rsmDataBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, null)
            rsmDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
            mContext?.replaceFragment(KBAMConstant.Fragments.OSO_ACCOUNT_FRAGMENT, rsmDataBundle)
        } else if (level == "R4") {
            val spDataBundle = Bundle()
            spDataBundle.putString(WSCustomerFragment.USER_ID, userId)
            spDataBundle.putString(WSCustomerFragment.USER_LEVEL, level)
            spDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, false)
            spDataBundle.putBoolean(WSCustomerFragment.FROM_SP, false)
            spDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, false)
            spDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, null)
            spDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, null)
            spDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
            mContext?.replaceFragment(KBAMConstant.Fragments.OSO_CUSTOMER_FRAGMENT, spDataBundle)
        }
    }


    //@OnClick(R.id.txtBtnOutstandingAnalysis)
    fun btnOutstandingAnalysis() {
        if (level == "R1") {
            val rsmDataBundle = Bundle()
            rsmDataBundle.putString(NewRSMTabFragment.USER_ID, userId)
            rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level)
            rsmDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
            //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
            mContext?.replaceFragment(KBAMConstant.Fragments.TOS_RSM_FRAGMENT, rsmDataBundle)
        } else if (level == "R2" || level == "R3") {
            val rsmDataBundle = Bundle()
            rsmDataBundle.putString(WSSalesPersonFragment.USER_ID, userId)
            rsmDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level)
            rsmDataBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, null)
            rsmDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
            mContext?.replaceFragment(KBAMConstant.Fragments.TOS_ACCOUNT_FRAGMENT, rsmDataBundle)
        } else if (level == "R4") {
            val spDataBundle = Bundle()
            spDataBundle.putString(WSCustomerFragment.USER_ID, userId)
            spDataBundle.putString(WSCustomerFragment.USER_LEVEL, level)
            spDataBundle.putBoolean(WSCustomerFragment.FROM_RSM, false)
            spDataBundle.putBoolean(WSCustomerFragment.FROM_SP, false)
            spDataBundle.putBoolean(WSCustomerFragment.FROM_PRODUCT, false)
            spDataBundle.putParcelable(WSCustomerFragment.RSM_PROFILE, null)
            spDataBundle.putParcelable(WSCustomerFragment.SP_PROFILE, null)
            spDataBundle.putBoolean(KDashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
            mContext?.replaceFragment(KBAMConstant.Fragments.TOS_CUSTOMER_FRAGMENT, spDataBundle)
        }
    }


    @OnClick(R.id.txtBtnDSOAnalysis)
    fun btnDSOAnalysis() {

    }


    fun showDialog(type: String, ytdqtdModel: NewYTDQTDModel) {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        // ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.qm_dialog, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        val layoutManager: LinearLayoutManager

        val tviDialogType = dialogView.findViewById<View>(R.id.tviDialogType) as TextView
        val iviCloseDialogType = dialogView.findViewById<View>(R.id.iviCloseDialogType) as ImageView
        val tviQTDHeading = dialogView.findViewById<View>(R.id.tviQTDHeading) as TextView

        val rviData = dialogView.findViewById<View>(R.id.rviData) as RecyclerView

        layoutManager = LinearLayoutManager(mContext)
        rviData.layoutManager = layoutManager

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

        tviDialogType.text = type

        if (type == "QUARTERLY") {
            tviQTDHeading.text = "QUARTERS"
            //llQTD.setVisibility(View.GONE);
            //llYTD.setVisibility(View.VISIBLE);
            val qtd = ytdqtdModel.qtd
            /*tviYTDHeading1.setText(ytd.get(0).getName());
            tviYTDHeading2.setText(ytd.get(1).getName());
            tviYTDHeading3.setText(ytd.get(2).getName());
            tviYTDHeading4.setText(ytd.get(3).getName());
            tviYTDValue1.setText(BAMUtil.getRoundOffValue(ytd.get(0).getValue()));
            tviYTDValue2.setText(BAMUtil.getRoundOffValue(ytd.get(1).getValue()));
            tviYTDValue3.setText(BAMUtil.getRoundOffValue(ytd.get(2).getValue()));
            tviYTDValue4.setText(BAMUtil.getRoundOffValue(ytd.get(3).getValue()));*/
            //
            // Sarvesh 16-05-20
            //
            val qmDialogAdapter = KQMDialogAdapter(activity!!, qtd)
            rviData.adapter = qmDialogAdapter
        } else if (type == "MONTHLY") {
            tviQTDHeading.text = "MONTH"
            //llQTD.setVisibility(View.VISIBLE);
            //llYTD.setVisibility(View.GONE);
            val qtd = ArrayList<NewYTDQTDModel.QTD>()

            for (i in 0 until ytdqtdModel.mtd.size) {
                val ytd = NewYTDQTDModel.QTD()
                ytd.name = ytdqtdModel.mtd[i].name
                ytd.target = ytdqtdModel.mtd[i].target
                ytd.actual = ytdqtdModel.mtd[i].actual
                ytd.percentage = ytdqtdModel.mtd[i].percentage

                qtd.add(ytd)
            }
            //List<NewYTDQTDModel.YTD> qtd = ytdqtdModel.getQTD();
            /*tviQTDHeading1.setText(qtd.get(0).getName());
            tviQTDHeading2.setText(qtd.get(1).getName());
            tviQTDHeading3.setText(qtd.get(2).getName());
            tviQTDValue1.setText(BAMUtil.getRoundOffValue(qtd.get(0).getValue()));
            tviQTDValue2.setText(BAMUtil.getRoundOffValue(qtd.get(1).getValue()));
            tviQTDValue3.setText(BAMUtil.getRoundOffValue(qtd.get(2).getValue()));*/
            //
            // Sarvesh 16-05-20
            //
            val qmDialogAdapter = KQMDialogAdapter(activity!!, qtd)
            rviData.adapter = qmDialogAdapter
        }

        iviCloseDialogType.setOnClickListener { alertDialog?.cancel() }

        alertDialog = dialogBuilder.create()
        alertDialog?.show()
    }

}