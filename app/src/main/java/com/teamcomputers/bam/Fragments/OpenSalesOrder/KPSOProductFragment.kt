package com.teamcomputers.bam.Fragments.OpenSalesOrder

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.*
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters.KPSOCustomerAdapter
import com.teamcomputers.bam.Fragments.BaseFragment
import com.teamcomputers.bam.Fragments.SalesReceivable.CustomerFragment
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOCustomerModel
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSORSMModel
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOSOModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Requesters.WSRequesters.KSalesOpenOrderAprRequester
import com.teamcomputers.bam.Utils.BackgroundExecutor
import com.teamcomputers.bam.Utils.KBAMUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class KPSOProductFragment : BaseFragment() {
    val USER_ID = "USER_ID"
    val USER_LEVEL = "USER_LEVEL"
    val RSM_PROFILE = "RSM_PROFILE"
    val SP_PROFILE = "SP_PROFILE"
    val INVOICE_PROFILE = "INVOICE_PROFILE"
    val FROM_RSM = "FROM_RSM"
    val FROM_SP = "FROM_SP"
    val FROM_INVOICE = "FROM_INVOICE"
    val RSM_POS = "RSM_POS"
    val SP_POS = "SP_POS"
    val INVOICE_POS = "INVOICE_POS"
    private var layoutManager: LinearLayoutManager? = null
    var dashboardActivityContext: DashboardActivity = null!!
    private var adapter: KPSOCustomerAdapter? = null

    internal var fromRSM: Boolean = false
    internal var fromSP: Boolean = false
    internal var fromInvoice: Boolean = false
    internal var search = false
    internal var toolbarTitle = ""
    internal var userId: String = ""
    internal var level: String = ""

    private var position: Int = 0
    private var rsmPos: Int = 0
    private var spPos: Int = 0
    private var cPos: Int = 0
    private var iPos: Int = 0

    internal var customerData: KPSOCustomerModel? = null
    internal var selectedCustomerData: KPSOCustomerModel.Datum? = null
    internal var customerDataList: List<KPSOCustomerModel.Datum> = ArrayList()
    internal var customerFilterData: KPSOCustomerModel.Filter? = null

    internal var invoiceProfile: KPSOSOModel.Datum? = null
    internal var rsmProfile: KPSORSMModel.Datum? = null
    internal var salesProfile: KPSORSMModel.Datum? = null
    internal var sOProfile: KPSOSOModel.Datum? = null

    @BindView(R.id.txtSearch)
    internal var txtSearch: EditText? = null
    @BindView(R.id.cviSPHeading)
    internal var cviSPHeading: CardView? = null
    @BindView(R.id.llSPLayout)
    internal var llSPLayout: LinearLayout? = null
    @BindView(R.id.rlR1)
    internal var rlR1: RelativeLayout? = null
    @BindView(R.id.rlR2)
    internal var rlR2: RelativeLayout? = null
    @BindView(R.id.rlR3)
    internal var rlR3: RelativeLayout? = null
    @BindView(R.id.tviR1Name)
    internal var tviR1Name: TextView? = null
    @BindView(R.id.tviR2Name)
    internal var tviR2Name: TextView? = null
    @BindView(R.id.tviR3Name)
    internal var tviR3Name: TextView? = null
    @BindView(R.id.tviSOAmount)
    internal var tviSOAmount: TextView? = null
    @BindView(R.id.tviR1StateName)
    internal var tviR1StateName: TextView? = null
    @BindView(R.id.tviR2StateName)
    internal var tviR2StateName: TextView? = null
    @BindView(R.id.tviR3StateName)
    internal var tviR3StateName: TextView? = null
    @BindView(R.id.iviR1Close)
    internal var iviR1Close: ImageView? = null
    @BindView(R.id.rviRSM)
    internal var rviRSM: RecyclerView? = null

    override fun getFragmentName(): String {
        return KPSOProductFragment::class.java.simpleName;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_kpsoproduct, container, false)

        dashboardActivityContext = context as DashboardActivity
        EventBus.getDefault().register(this)
        unbinder = rootView?.let { ButterKnife.bind(this, it) }

        fromRSM = arguments!!.getBoolean(FROM_RSM)
        fromSP = arguments!!.getBoolean(FROM_SP)
        fromInvoice = arguments!!.getBoolean(FROM_INVOICE)

        rsmPos = arguments!!.getInt(RSM_POS)
        spPos = arguments!!.getInt(SP_POS)
        iPos = arguments!!.getInt(INVOICE_POS)

        userId = arguments!!.getString(USER_ID)
        level = arguments!!.getString(USER_LEVEL)
        rsmProfile = arguments!!.getParcelable<KPSORSMModel.Datum>(RSM_PROFILE)
        salesProfile = arguments!!.getParcelable<KPSORSMModel.Datum>(SP_PROFILE)
        sOProfile = arguments!!.getParcelable<KPSOSOModel.Datum>(INVOICE_PROFILE)

        toolbarTitle = getString(R.string.Customer)
        dashboardActivityContext?.setToolBarTitle(toolbarTitle)

        layoutManager = LinearLayoutManager(dashboardActivityContext)
        rviRSM?.setLayoutManager(layoutManager)

        rowsDisplay()

        dashboardActivityContext?.fragmentView = rootView

        return rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_screen_share)
        item.isVisible = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        var rootView: View? = null
        var unbinder: Unbinder? = null

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                KPSOProductFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    @Subscribe
    fun onEvent(eventObject: EventObject) {
        dashboardActivityContext?.runOnUiThread(Runnable {
            when (eventObject.id) {
                KBAMConstant.Events.NO_INTERNET_CONNECTION -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.NO_INTERNET_CONNECTION)
                }
                KBAMConstant.Events.NOT_FOUND -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.NO_RECORD_FOUND)
                }
                KBAMConstant.Events.GET_CUSTOMER_OSO_LIST_SUCCESSFULL -> {
                    dismissProgress()
                    try {
                        val jsonObject = JSONObject(KBAMUtils.replaceWSDataResponse(eventObject.getObject().toString()))
                        customerData = KBAMUtils.fromJson(jsonObject.toString(), KPSOCustomerModel::class.java) as KPSOCustomerModel
                        customerDataList = customerData!!.getData()!!
                        customerFilterData = customerData?.getFilter()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    initData()
                    dismissProgress()
                }
                KBAMConstant.Events.GET_CUSTOMER_OSO_LIST_UNSUCCESSFULL -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.OOPS_MESSAGE)
                }
                KBAMConstant.ClickEvents.ACCOUNT_ITEM -> {
                    val position = eventObject.getObject() as Int
                    val acctDataBundle = Bundle()
                    acctDataBundle.putParcelable(CustomerFragment.ACCT_PROFILE, customerDataList?.get(position))
                    acctDataBundle.putInt(CustomerFragment.ACCT_POSITION, position)
                    acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
                    dashboardActivityContext?.replaceFragment(KBAMConstant.Fragments.OSO_CUSTOMER_FRAGMENT, acctDataBundle)
                }
                KBAMConstant.ClickEvents.CUSTOMER_SELECT -> if (!fromInvoice) {
                    selectedCustomerData = eventObject.getObject() as KPSOCustomerModel.Datum
                    val customerBundle = Bundle()
                    customerBundle.putString(OSOInvoiceFragment.USER_ID, userId)
                    customerBundle.putString(OSOInvoiceFragment.USER_LEVEL, level)

                    cPos = rsmPos + spPos + 1
                    customerBundle.putInt(OSOInvoiceFragment.RSM_POS, rsmPos)
                    customerBundle.putInt(OSOInvoiceFragment.SP_POS, spPos)
                    customerBundle.putInt(OSOInvoiceFragment.CUSTOMER_POS, cPos)

                    customerBundle.putBoolean(OSOInvoiceFragment.FROM_RSM, fromRSM)
                    customerBundle.putBoolean(OSOInvoiceFragment.FROM_SP, fromSP)
                    customerBundle.putBoolean(OSOInvoiceFragment.FROM_CUSTOMER, true)

                    customerBundle.putParcelable(OSOInvoiceFragment.CUSTOMER_PROFILE, selectedCustomerData)
                    customerBundle.putParcelable(OSOInvoiceFragment.RSM_PROFILE, rsmProfile)
                    customerBundle.putParcelable(OSOInvoiceFragment.SP_PROFILE, salesProfile)
                    if (null != selectedCustomerData?.stateCodeWise) {
                        customerBundle.putInt(OSOInvoiceFragment.STATE_CODE, 1)
                    } else {
                        customerBundle.putInt(OSOInvoiceFragment.STATE_CODE, 0)
                    }
                    customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
                    dashboardActivityContext?.replaceFragment(KBAMConstant.Fragments.OSO_INVOICE_FRAGMENT, customerBundle)
                }
                KBAMConstant.ClickEvents.CUSTOMER_ITEM -> {
                    //if (!fromInvoice) {
                    val productStateBundle = Bundle()
                    selectedCustomerData = eventObject.getObject() as KPSOCustomerModel.Datum
                    productStateBundle.putString(OSOInvoiceFragment.USER_ID, userId)
                    productStateBundle.putString(OSOInvoiceFragment.USER_LEVEL, level)

                    cPos = rsmPos + spPos + 1
                    productStateBundle.putInt(OSOInvoiceFragment.RSM_POS, rsmPos)
                    productStateBundle.putInt(OSOInvoiceFragment.SP_POS, spPos)
                    productStateBundle.putInt(OSOInvoiceFragment.CUSTOMER_POS, cPos)

                    productStateBundle.putBoolean(OSOInvoiceFragment.FROM_RSM, fromRSM)
                    productStateBundle.putBoolean(OSOInvoiceFragment.FROM_SP, fromSP)
                    productStateBundle.putBoolean(OSOInvoiceFragment.FROM_CUSTOMER, true)

                    productStateBundle.putParcelable(OSOInvoiceFragment.CUSTOMER_PROFILE, selectedCustomerData)
                    productStateBundle.putParcelable(OSOInvoiceFragment.RSM_PROFILE, rsmProfile)
                    productStateBundle.putParcelable(OSOInvoiceFragment.SP_PROFILE, salesProfile)
                    if (null != selectedCustomerData?.stateCodeWise) {
                        productStateBundle.putInt(OSOInvoiceFragment.STATE_CODE, 1)
                    } else {
                        productStateBundle.putInt(OSOInvoiceFragment.STATE_CODE, 0)
                    }
                    productStateBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
                    dashboardActivityContext?.replaceFragment(KBAMConstant.Fragments.OSO_INVOICE_FRAGMENT, productStateBundle)
                }
                KBAMConstant.ClickEvents.RSM_MENU_SELECT -> {
                    selectedCustomerData = eventObject.getObject() as KPSOCustomerModel.Datum
                    val rsmBundle = Bundle()
                    rsmBundle.putString(OSORSMFragment.USER_ID, userId)
                    rsmBundle.putString(OSORSMFragment.USER_LEVEL, level)

                    cPos = spPos + iPos + 1
                    rsmBundle.putInt(OSORSMFragment.SP_POS, spPos)
                    rsmBundle.putInt(OSORSMFragment.INVOICE_POS, iPos)
                    rsmBundle.putInt(OSORSMFragment.CUSTOMER_POS, cPos)

                    rsmBundle.putBoolean(OSORSMFragment.FROM_INVOICE, fromInvoice)
                    rsmBundle.putBoolean(OSORSMFragment.FROM_SP, fromSP)
                    rsmBundle.putBoolean(OSORSMFragment.FROM_CUSTOMER, true)

                    rsmBundle.putParcelable(OSORSMFragment.CUSTOMER_PROFILE, selectedCustomerData)
                    rsmBundle.putParcelable(OSORSMFragment.INVOICE_PROFILE, invoiceProfile)
                    rsmBundle.putParcelable(OSORSMFragment.SP_PROFILE, salesProfile)
                    if (null != selectedCustomerData?.stateCodeWise) {
                        rsmBundle.putInt(OSORSMFragment.STATE_CODE, 1)
                    } else {
                        rsmBundle.putInt(OSORSMFragment.STATE_CODE, 0)
                    }
                    rsmBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
                    dashboardActivityContext?.replaceFragment(KBAMConstant.Fragments.OSO_RSM_FRAGMENT, rsmBundle)
                }
                KBAMConstant.ClickEvents.SP_MENU_SELECT -> {
                    selectedCustomerData = eventObject.getObject() as KPSOCustomerModel.Datum
                    val customerBundle = Bundle()
                    customerBundle.putString(OSOSalesPersonFragment.USER_ID, userId)
                    customerBundle.putString(OSOSalesPersonFragment.USER_LEVEL, level)

                    cPos = rsmPos + iPos + 1
                    customerBundle.putInt(OSOSalesPersonFragment.RSM_POS, rsmPos)
                    customerBundle.putInt(OSOSalesPersonFragment.CUSTOMER_POS, cPos)
                    customerBundle.putInt(OSOSalesPersonFragment.INVOICE_POS, iPos)

                    customerBundle.putBoolean(OSOSalesPersonFragment.FROM_RSM, fromRSM)
                    customerBundle.putBoolean(OSOSalesPersonFragment.FROM_INVOICE, fromInvoice)
                    customerBundle.putBoolean(OSOSalesPersonFragment.FROM_CUSTOMER, true)

                    customerBundle.putParcelable(OSOSalesPersonFragment.CUSTOMER_PROFILE, selectedCustomerData)
                    customerBundle.putParcelable(OSOSalesPersonFragment.RSM_PROFILE, rsmProfile)
                    customerBundle.putParcelable(OSOSalesPersonFragment.INVOICE_PROFILE, invoiceProfile)
                    if (null != selectedCustomerData?.stateCodeWise) {
                        customerBundle.putInt(OSOSalesPersonFragment.STATE_CODE, 1)
                    } else {
                        customerBundle.putInt(OSOSalesPersonFragment.STATE_CODE, 0)
                    }
                    customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
                    dashboardActivityContext?.replaceFragment(KBAMConstant.Fragments.OSO_ACCOUNT_FRAGMENT, customerBundle)
                }
            }/*acctDataBundle.putParcelable(ProductFragment.PRODUCT_PROFILE, model.get(position));
                acctDataBundle.putInt(ProductFragment.PRODUCT_POSITION, position);
                acctDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                dashboardActivityContext.replaceFragment(Fragments.PRODUCT_FRAGMENT, acctDataBundle);*///}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
        EventBus.getDefault().unregister(this)
    }

    @OnTextChanged(R.id.txtSearch)
    fun search() {
        adapter?.getFilter()?.filter(txtSearch?.getText().toString())
    }

    @OnClick(R.id.iviSearch)
    fun Search() {
        if (!search) {
            txtSearch?.setVisibility(View.VISIBLE)
            search = true
        } else if (search) {
            txtSearch?.setVisibility(View.GONE)
            search = false
        }
    }

    @OnClick(R.id.iviClose)
    fun filterClose() {
        fromRSM = false
        fromSP = false
        fromInvoice = false
        rsmProfile = null
        salesProfile = null
        invoiceProfile = null
        rsmPos = 0
        spPos = 0
        iPos = 0
        cviSPHeading?.setVisibility(View.GONE)
        showProgress(KBAMConstant.ProgressDialogTexts.LOADING)
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Customer", "", "", "", "", "", ""));
        BackgroundExecutor.getInstance().execute(KSalesOpenOrderAprRequester(userId, level, "Customer", "", "", "", "", "", ""))
    }

    @OnClick(R.id.iviR1Close)
    fun r1Close() {
        if (rsmPos == 1) {
            fromRSM = false
            rsmProfile = null
            rsmPos = 0
            if (spPos == 2) {
                spPos = 1
            } else if (spPos == 4) {
                spPos = 2
            }
            if (iPos == 2) {
                iPos = 1
            } else if (iPos == 4) {
                iPos = 2
            }
        } else if (spPos == 1) {
            fromSP = false
            salesProfile = null
            spPos = 0
            if (rsmPos == 2) {
                rsmPos = 1
            } else if (rsmPos == 4) {
                rsmPos = 2
            }
            if (iPos == 2) {
                iPos = 1
            } else if (iPos == 4) {
                iPos = 2
            }
        } else if (iPos == 1) {
            fromInvoice = false
            invoiceProfile = null
            iPos = 0
            if (rsmPos == 2) {
                rsmPos = 1
            } else if (rsmPos == 4) {
                rsmPos = 2
            }
            if (spPos == 2) {
                spPos = 1
            } else if (spPos == 4) {
                spPos = 2
            }
        }
        rowsDisplay()
    }

    @OnClick(R.id.iviR2Close)
    fun r2Close() {
        if (rsmPos == 2) {
            fromRSM = false
            rsmProfile = null
            rsmPos = 0
            if (spPos == 4) {
                spPos = 2
            }
            if (iPos == 4) {
                iPos = 2
            }
        } else if (spPos == 2) {
            fromSP = false
            salesProfile = null
            spPos = 0
            if (rsmPos == 4) {
                rsmPos = 2
            }
            if (iPos == 4) {
                iPos = 2
            }
        } else if (iPos == 2) {
            fromInvoice = false
            invoiceProfile = null
            iPos = 0
            if (rsmPos == 4) {
                rsmPos = 2
            }
            if (spPos == 4) {
                spPos = 2
            }
        }
        rowsDisplay()
    }

    @OnClick(R.id.iviR3Close)
    fun r3Close() {
        if (rsmPos == 4) {
            fromRSM = false
            rsmProfile = null
            rsmPos = 0
        } else if (spPos == 4) {
            fromSP = false
            salesProfile = null
            spPos = 0
        } else if (iPos == 4) {
            fromInvoice = false
            invoiceProfile = null
            iPos = 0
        }
        rowsDisplay()
    }

    private fun rowsDisplay() {
        if (fromRSM || fromSP || fromInvoice) {
            cviSPHeading!!.setVisibility(View.VISIBLE)
        }

        val totalPosition = rsmPos + spPos + iPos

        if (totalPosition == 7) {
            row3Display()
        } else if (totalPosition == 3) {
            row2Display()
        } else if (totalPosition == 1) {
            iviR1Close!!.setVisibility(View.GONE)
            row1Display()
        }
        var rsm: String? = ""
        var sales: String? = ""
        var invoiceNo: String? = ""
        if (null != rsmProfile)
            rsm = rsmProfile!!.tmc
        if (null != salesProfile)
            sales = salesProfile!!.tmc
        if (null != invoiceProfile)
            invoiceNo = invoiceProfile!!.soNumber
        showProgress(KBAMConstant.ProgressDialogTexts.LOADING)
        //BackgroundExecutor.getInstance().execute(new OpenSalesOrderRequester(userId, level, "Customer", rsm, sales, "", "", invoiceNo, ""));
        BackgroundExecutor.getInstance().execute(KSalesOpenOrderAprRequester(userId, level, "Customer", rsm!!, sales!!, "", "", invoiceNo!!, ""))

    }

    private fun row1Display() {
        rlR2!!.setVisibility(View.GONE)
        rlR3!!.setVisibility(View.GONE)
        if (rsmPos == 1) {
            position = rsmProfile!!.position
            if (position == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_first_item_value))
            } else if (position == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_second_item_value))
            } else if (position == 2) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_third_item_value))
            } else if (position % 2 == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_white))
            } else if (position % 2 == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.login_bg))
            }
            tviR1Name!!.setText(rsmProfile!!.name)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(rsmProfile?.soAmount!!))
        } else if (iPos == 1) {
            position = invoiceProfile?.position!!
            if (position == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_first_item_value))
            } else if (position == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_second_item_value))
            } else if (position == 2) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_third_item_value))
            } else if (position % 2 == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_white))
            } else if (position % 2 == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.login_bg))
            }
            tviR1Name!!.setText(invoiceProfile!!.soNumber)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(invoiceProfile?.soAmount!!))
        } else if (spPos == 1) {
            position = salesProfile!!.position
            if (position == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_first_item_value))
            } else if (position == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_second_item_value))
            } else if (position == 2) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_third_item_value))
            } else if (position % 2 == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_white))
            } else if (position % 2 == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.login_bg))
            }
            tviR1Name!!.setText(salesProfile!!.name)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(salesProfile?.soAmount!!))
        }
    }

    private fun row2Display() {
        rlR3!!.setVisibility(View.GONE)
        if (rsmPos == 2) {
            position = rsmProfile!!.position
            if (position == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_first_item_value))
            } else if (position == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_second_item_value))
            } else if (position == 2) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_third_item_value))
            } else if (position % 2 == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_white))
            } else if (position % 2 == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.login_bg))
            }
            tviR2Name!!.setText(rsmProfile!!.name)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(rsmProfile?.soAmount!!))
        } else if (spPos == 2) {
            position = salesProfile!!.position
            if (position == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_first_item_value))
            } else if (position == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_second_item_value))
            } else if (position == 2) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_third_item_value))
            } else if (position % 2 == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_white))
            } else if (position % 2 == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.login_bg))
            }
            tviR2Name!!.setText(salesProfile!!.name)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(salesProfile?.soAmount!!))
        } else if (iPos == 2) {
            position = invoiceProfile?.position!!
            if (position == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_first_item_value))
            } else if (position == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_second_item_value))
            } else if (position == 2) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_third_item_value))
            } else if (position % 2 == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_white))
            } else if (position % 2 == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.login_bg))
            }
            tviR2Name!!.setText(invoiceProfile!!.soNumber)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(invoiceProfile?.soAmount!!))
        }
        if (rsmPos == 1) {
            tviR1Name!!.setText(rsmProfile?.name)
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name!!.setText(salesProfile?.name)
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSOAmount()));
        } else if (iPos == 1) {
            tviR1Name!!.setText(invoiceProfile?.soNumber)
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
    }

    private fun row3Display() {
        if (rsmPos == 4) {
            position = rsmProfile!!.position
            if (position == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_first_item_value))
            } else if (position == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_second_item_value))
            } else if (position == 2) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_third_item_value))
            } else if (position % 2 == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_white))
            } else if (position % 2 == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.login_bg))
            }
            tviR3Name!!.setText(rsmProfile?.name)
        } else if (spPos == 4) {
            position = salesProfile!!.position
            if (position == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_first_item_value))
            } else if (position == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_second_item_value))
            } else if (position == 2) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_third_item_value))
            } else if (position % 2 == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_white))
            } else if (position % 2 == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.login_bg))
            }
            tviR3Name!!.setText(salesProfile!!.name)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(salesProfile?.soAmount!!))
        } else if (iPos == 4) {
            position = invoiceProfile?.position!!
            if (position == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_first_item_value))
            } else if (position == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_second_item_value))
            } else if (position == 2) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_third_item_value))
            } else if (position % 2 == 0) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.color_white))
            } else if (position % 2 == 1) {
                llSPLayout!!.setBackgroundColor(resources.getColor(R.color.login_bg))
            }
            tviR3Name!!.setText(invoiceProfile?.soNumber)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(invoiceProfile?.soAmount!!))
        }

        if (rsmPos == 2) {
            tviR2Name!!.setText(rsmProfile?.name)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(rsmProfile!!.soAmount!!))
        } else if (spPos == 2) {
            tviR2Name!!.setText(salesProfile?.name)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(salesProfile!!.soAmount!!))
        } else if (iPos == 2) {
            tviR2Name!!.setText(invoiceProfile?.soNumber)
            tviSOAmount!!.setText(KBAMUtils.getRoundOffValue(invoiceProfile!!.soAmount!!))
        }
        if (rsmPos == 1) {
            tviR1Name!!.setText(rsmProfile?.name)
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(rsmProfile.getSOAmount()));
        } else if (spPos == 1) {
            tviR1Name!!.setText(salesProfile?.name)
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(salesProfile.getSOAmount()));
        } else if (iPos == 1) {
            tviR1Name!!.setText(invoiceProfile?.soNumber)
            //tviR1SOAmount.setText(KBAMUtils.getRoundOffValue(invoiceProfile.getSOAmount()));
        }
    }

    private fun initData() {
        //adapter = new OSOCustomerAdapter(dashboardActivityContext, userId, level, model, fromRSM, fromSP, fromInvoice);
        adapter = KPSOCustomerAdapter(dashboardActivityContext, level, customerDataList, fromRSM, fromSP, fromInvoice)
        rviRSM!!.setAdapter(adapter)
    }
}
