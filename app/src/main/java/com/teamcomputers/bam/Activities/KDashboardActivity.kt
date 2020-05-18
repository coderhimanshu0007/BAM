package com.teamcomputers.bam.Activities

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.google.android.material.appbar.AppBarLayout
import com.teamcomputers.bam.ExpandableRecyclerview.expandables.NavigationExpandable
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItem
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItemParentModel
import com.teamcomputers.bam.Fragments.KBaseFragment
import com.teamcomputers.bam.Fragments.NewSalesReceivable.KSalesReceivableFragment
import com.teamcomputers.bam.Fragments.home.KHomeFragment
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.KBAMApplication
import com.teamcomputers.bam.Models.LoginModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.WrapContentLinearLayoutManager
import com.teamcomputers.bam.controllers.SharedPreferencesController
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class KDashboardActivity : KBaseActivity() {
    companion object {
        val IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED = "IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED"
    }

    var userId: String? = null
    var level: String? = null
    var selectedFiscalYear = ""
    var selectedPosition = 0
    var fragmentView: View? = null


    /*@BindView(R.id.llWSTabs)
        internal var llWSTabs: LinearLayout? = null
        @BindView(R.id.llOSOTabs)
        internal var llOSOTabs: LinearLayout? = null
        @BindView(R.id.llTOSTabs)
        internal var llTOSTabs: LinearLayout? = null*/
    /*@BindView(R.id.claMain)
    CoordinatorLayout claMain;*/
    /*@BindView(R.id.iviToolbarLogo)
    internal var iviToolbarLogo: ImageView? = null
    @BindView(R.id.toolbar_title)
    internal var tvToolBarTitle: TextView? = null
    @BindView(R.id.tv_date)
    internal var tv_date: TextView? = null
    @BindView(R.id.iv_employee_profile)
    internal var ivEmployee: CircularImageView? = null
    @BindView(R.id.tv_employee_name)
    internal var tvEmployeeName: TextViewCustom? = null*/
    private var toolbar: Toolbar? = null
    @BindView(R.id.drawer_layout)
    internal var drawerLayout: DrawerLayout? = null
    @BindView(R.id.recycler_view_nav_item)
    internal var expandableNavigationRecyclerView: RecyclerView? = null
    @BindView(R.id.appBar)
    internal var appBarLayout: AppBarLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var navigationExpandable: NavigationExpandable? = null
    internal var navigationItemParentModels: MutableList<NavigationItemParentModel> = ArrayList()
    //private var this: DashboardActivity? = null
    private var fragment: KBaseFragment? = null
    private var userProfile: LoginModel? = null
    private var fragmentManager: FragmentManager? = null
    private val drawerHandler = Handler()
    private val mAppBarConfiguration: AppBarConfiguration? = null


    override fun getLayout(): Int {
        return R.layout.activity_dashboard
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        runOnUiThread {
            when (eventObject.id) {
                KBAMConstant.Events.ORDER_PROCESSING -> replaceFragment(KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS, Bundle())
                KBAMConstant.Events.PURCHASE -> replaceFragment(KBAMConstant.Fragments.PURCHASE_FRAGMENTS, Bundle())
                KBAMConstant.Events.LOGISTICS -> replaceFragment(KBAMConstant.Fragments.LOGISTICS_FRAGMENTS, Bundle())
                KBAMConstant.Events.INSTALLATION -> replaceFragment(KBAMConstant.Fragments.INSTALLATION_FRAGMENTS, Bundle())
                KBAMConstant.Events.COLLECTION -> replaceFragment(KBAMConstant.Fragments.COLLECTION_FRAGMENTS, Bundle())
                KBAMConstant.Events.WS -> {
                    val srBundle = Bundle()
                    srBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
                    replaceFragment(KBAMConstant.Fragments.SR_FRAGMENTS, srBundle)
                }
                KBAMConstant.Events.OTHERS -> replaceFragment(KBAMConstant.Fragments.OTHERS_FRAGMENTS, Bundle())
            }/*case Events.SALESANALYSIS:
                        replaceFragment(Fragments.SALES_ANALYSIS_FRAGMENT, new Bundle());
                        break;*/
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this = this
        init()
        if (SharedPreferencesController.getInstance(this).isUserLoggedIn) {
            toolbar = findViewById<View>(R.id.tool_bar) as Toolbar
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            //NavigationView navigationView = findViewById(R.id.nav_view);

            drawerToggle = object : ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                override fun onDrawerClosed(drawerView: View) {
                    // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                    super.onDrawerClosed(drawerView)
                    //val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    //inputMethodManager.hideSoftInputFromWindow(this?.getWindow()?.getDecorView()?.getRootView().getWindowToken(), 0)
                }

                override fun onDrawerOpened(drawerView: View) {
                    // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                    super.onDrawerOpened(drawerView)
                    //val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    //inputMethodManager.hideSoftInputFromWindow(this.getWindow()?.getDecorView()?.getRootView().getWindowToken(), 0)
                }
            }
            drawer.setDrawerListener(drawerToggle)
            drawerToggle?.syncState()
            setNavigationDrawerData()
            //putFragmentInContainer(savedInstanceState!!)
            home()
            reloadNavigationDrawer()
            userProfile = SharedPreferencesController.getInstance(KBAMApplication.ctx).userProfile
            var name: String? = null
            if (userProfile?.getMemberName()?.split(" ".toRegex())?.dropLastWhile({ it.isEmpty() })?.toTypedArray()?.size!! > 2) {
                name = (userProfile?.getMemberName()?.split(" ".toRegex())?.dropLastWhile({ it.isEmpty() })!!.toTypedArray()[0]
                        + " " + userProfile?.getMemberName()?.split(" ".toRegex())?.dropLastWhile({ it.isEmpty() })?.toTypedArray()!![userProfile!!.getMemberName().split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray().size - 1])
            } else if (userProfile?.getMemberName()?.split(" ".toRegex())?.dropLastWhile({ it.isEmpty() })!!.toTypedArray().size <= 2) {
                name = userProfile?.getMemberName()
            }
            //tvEmployeeName.setText(userProfile?.getMemberName() == null ? "No Name" : userProfile?.getMemberName());
            tv_employee_name.setText(name ?: "No Name")
        } else {
            logoutAction()
        }
    }

    private fun init() {
        ll_home.setOnClickListener(View.OnClickListener { home() })
        ll_SR.setOnClickListener(View.OnClickListener { SR() })
        ll_logout.setOnClickListener(View.OnClickListener { logoutAction() })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)
        val action_screen_share = menu.findItem(R.id.action_screen_share)
        action_screen_share.setOnMenuItemClickListener {
            getScreen()
            false
        }
        return true
    }

    private fun reloadNavigationDrawer() {
        navigationItemParentModels.clear()
        setNavigationDrawerData()
        recycler_view_nav_item.setLayoutManager(WrapContentLinearLayoutManager(this))
        navigationExpandable = NavigationExpandable(navigationItemParentModels, this)
        recycler_view_nav_item.setItemAnimator(DefaultItemAnimator())
        recycler_view_nav_item.setAdapter(navigationExpandable)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return mAppBarConfiguration?.let { NavigationUI.navigateUp(navController, it) }!! || super.onSupportNavigateUp()
    }

    fun setToolBarTitle(title: String) {
        toolbar_title.setText(title)
        if (title == getString(R.string.Heading_Home)) {
            iviToolbarLogo.setVisibility(View.VISIBLE)
            toolbar_title.setVisibility(View.GONE)
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                claMain.setBackground(getResources().getDrawable(R.drawable.gradient_drawable));
            }*/
        } else {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                claMain.setBackground(getResources().getDrawable(R.drawable.gradient_login_bg));
            }*/
            iviToolbarLogo.setVisibility(View.GONE)
            toolbar_title.setVisibility(View.VISIBLE)
        }
        if (title == getString(R.string.Heading_Home) || title == getString(R.string.Heading_Feedback)) {
            tv_date.setVisibility(View.GONE)
        } else {
            tv_date.setVisibility(View.VISIBLE)
        }
    }

    fun updateDate(date: String) {
        val newDate = Date(date)
        val sdf = SimpleDateFormat("dd/MM/yyyy\nhh:mm:ss aa", Locale.getDefault())
        val refreshDate = sdf.format(newDate)
        tv_date.setText("$refreshDate\nValues In Lacs")
    }

    private fun setNavigationDrawerData() {
        val navigationOrderProcessing = ArrayList<NavigationItem>()
        navigationOrderProcessing.add(NavigationItem(SpannableStringBuilder(getString(R.string.OP1)), KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS1))
        navigationOrderProcessing.add(NavigationItem(SpannableStringBuilder(getString(R.string.OP2)), KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS2))
        navigationOrderProcessing.add(NavigationItem(SpannableStringBuilder(getString(R.string.OP3)), KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS3))
        navigationOrderProcessing.add(NavigationItem(SpannableStringBuilder(getString(R.string.OP4)), KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS4))

        val navigationPurchase = ArrayList<NavigationItem>()
        navigationPurchase.add(NavigationItem(SpannableStringBuilder(getString(R.string.Purchanse1)), KBAMConstant.Fragments.PURCHASE_FRAGMENTS1))
        navigationPurchase.add(NavigationItem(SpannableStringBuilder(getString(R.string.Purchanse2)), KBAMConstant.Fragments.PURCHASE_FRAGMENTS2))
        navigationPurchase.add(NavigationItem(SpannableStringBuilder(getString(R.string.Purchanse3)), KBAMConstant.Fragments.PURCHASE_FRAGMENTS3))
        navigationPurchase.add(NavigationItem(SpannableStringBuilder(getString(R.string.Purchanse4)), KBAMConstant.Fragments.PURCHASE_FRAGMENTS4))

        val navigationLogistics = ArrayList<NavigationItem>()
        navigationLogistics.add(NavigationItem(SpannableStringBuilder(getString(R.string.Logistics1)), KBAMConstant.Fragments.LOGISTICS_FRAGMENTS1))
        navigationLogistics.add(NavigationItem(SpannableStringBuilder(getString(R.string.Logistics2)), KBAMConstant.Fragments.LOGISTICS_FRAGMENTS2))
        navigationLogistics.add(NavigationItem(SpannableStringBuilder(getString(R.string.Logistics3)), KBAMConstant.Fragments.LOGISTICS_FRAGMENTS3))
        navigationLogistics.add(NavigationItem(SpannableStringBuilder(getString(R.string.Logistics4)), KBAMConstant.Fragments.LOGISTICS_FRAGMENTS4))

        val navigationInstallation = ArrayList<NavigationItem>()
        navigationInstallation.add(NavigationItem(SpannableStringBuilder(getString(R.string.Installation1)), KBAMConstant.Fragments.INSTALLATION_FRAGMENTS1))
        navigationInstallation.add(NavigationItem(SpannableStringBuilder(getString(R.string.Installation2)), KBAMConstant.Fragments.INSTALLATION_FRAGMENTS2))
        navigationInstallation.add(NavigationItem(SpannableStringBuilder(getString(R.string.Installation3)), KBAMConstant.Fragments.INSTALLATION_FRAGMENTS3))
        navigationInstallation.add(NavigationItem(SpannableStringBuilder(getString(R.string.Installation4)), KBAMConstant.Fragments.INSTALLATION_FRAGMENTS4))

        val navigationCollection = ArrayList<NavigationItem>()
        navigationCollection.add(NavigationItem(SpannableStringBuilder(getString(R.string.Collection1)), KBAMConstant.Fragments.COLLECTION_FRAGMENTS1))
        navigationCollection.add(NavigationItem(SpannableStringBuilder(getString(R.string.Collection2)), KBAMConstant.Fragments.COLLECTION_FRAGMENTS2))
        navigationCollection.add(NavigationItem(SpannableStringBuilder(getString(R.string.Collection3)), KBAMConstant.Fragments.COLLECTION_FRAGMENTS3))
        navigationCollection.add(NavigationItem(SpannableStringBuilder(getString(R.string.Collection4)), KBAMConstant.Fragments.COLLECTION_FRAGMENTS4))

        val navigationOthers = ArrayList<NavigationItem>()
        navigationOthers.add(NavigationItem(SpannableStringBuilder("-" + getString(R.string.Others)), KBAMConstant.Fragments.OTHERS_FRAGMENTS))
        //navigationOthers.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Others2)), Fragments.OTHERS_FRAGMENTS));
        //navigationOthers.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Others3)), Fragments.OTHERS_FRAGMENTS));
        //navigationOthers.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Others4)), Fragments.OTHERS_FRAGMENTS));

        val navigationSR = ArrayList<NavigationItem>()
        navigationSR.add(NavigationItem(SpannableStringBuilder(getString(R.string.SalsReceivable1)), KBAMConstant.Fragments.SR_FRAGMENTS1))
        navigationSR.add(NavigationItem(SpannableStringBuilder(getString(R.string.SalsReceivable2)), KBAMConstant.Fragments.SR_FRAGMENTS2))

        val navigationOrderProcessingParent = NavigationItemParentModel()
        navigationOrderProcessingParent.navImageParent = R.drawable.ic_group_menu_order_processing
        navigationOrderProcessingParent.navTitleParent = getString(R.string.OrderProcessing)
        navigationOrderProcessingParent.navigationItems = navigationOrderProcessing

        val navigationPurchaseParent = NavigationItemParentModel()
        navigationPurchaseParent.navImageParent = R.drawable.ic_group_menu_purchase
        navigationPurchaseParent.navTitleParent = getString(R.string.Purchanse)
        navigationPurchaseParent.navigationItems = navigationPurchase

        val navigationLogisticsParent = NavigationItemParentModel()
        navigationLogisticsParent.navImageParent = R.drawable.ic_group_menu_logistics
        navigationLogisticsParent.navTitleParent = getString(R.string.Logistics)
        navigationLogisticsParent.navigationItems = navigationLogistics

        val navigationInstallationParent = NavigationItemParentModel()
        navigationInstallationParent.navImageParent = R.drawable.ic_group_menu_installation
        navigationInstallationParent.navTitleParent = getString(R.string.Installation)
        navigationInstallationParent.navigationItems = navigationInstallation

        val navigationCollectionParent = NavigationItemParentModel()
        navigationCollectionParent.navImageParent = R.drawable.ic_group_menu_collection
        navigationCollectionParent.navTitleParent = getString(R.string.Collection)
        navigationCollectionParent.navigationItems = navigationCollection

        val navigationOthersParent = NavigationItemParentModel()
        navigationOthersParent.navImageParent = R.drawable.ic_menu_others
        navigationOthersParent.navTitleParent = getString(R.string.Others)
        navigationOthersParent.navigationItems = navigationOthers

        val navigationWSParent = NavigationItemParentModel()
        navigationWSParent.navImageParent = R.drawable.ic_menu_others
        navigationWSParent.navTitleParent = getString(R.string.SalesReceivable)
        navigationWSParent.navigationItems = navigationSR

        /*if (SharedPreferencesController.getInstance(TeamWorksApplication.getInstance()).getUserProfile().getIsHead().equals("1")) {
            navigationItemParentModels.add(navigationItemParentMyTeam);
        }*/
        navigationItemParentModels.add(navigationOrderProcessingParent)
        navigationItemParentModels.add(navigationPurchaseParent)
        navigationItemParentModels.add(navigationLogisticsParent)
        navigationItemParentModels.add(navigationInstallationParent)
        navigationItemParentModels.add(navigationCollectionParent)
        //navigationItemParentModels.add(navigationWSParent);
    }

    fun logoutAction() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        SharedPreferencesController.getInstance(KBAMApplication.ctx).clear()
        ActivityCompat.finishAffinity(this@KDashboardActivity)
        val intent = Intent(this@KDashboardActivity, KSplashActivity::class.java)
        intent.putExtra(KBAMConstant.Constants.FINISH, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun putFragmentInContainer(savedInstanceState: Bundle) {
        fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        /*if (savedInstanceState == null && notificationBundle != null) {
            if (notificationBundle.getParcelableArrayList(NotificationTopics.BIRTHDAYS) != null) {
                fragment = new BirthdayFragment();
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.commit();
            } else if (notificationBundle.getParcelableArrayList(NotificationTopics.HR_ANNOUNCEMENT) != null) {
                fragment = new HRNewsFragment();
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.commit();
            } else if (notificationBundle.getParcelableArrayList(NotificationTypes.APPLY_LEAVE) != null) {
                fragment = new TeamLeaveFragment();
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.commit();
            } else if (notificationBundle.getParcelableArrayList(NotificationTypes.APPROVE_LEAVE) != null) {
                fragment = new MyLeaveFragment();
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.commit();
            } else if (notificationBundle.getParcelableArrayList(NotificationTypes.REJECT_LEAVE) != null) {
                fragment = new MyLeaveFragment();
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.commit();
            } else if (notificationBundle.getParcelable(NotificationTypes.NEW_TRAVEL_REQUEST) != null) {
                fragment = new TeamTravelReportFragment();
                notificationBundle.putBoolean(IS_REPORT_FILTERING_NEEDED, true);
                fragment.setArguments(notificationBundle);
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.commit();
            } else if (notificationBundle.getParcelable(NotificationTypes.TRAVEL_REQUEST_STATUS) != null) {
                fragment = new MyTravelReportFragment();
                fragment.setArguments(notificationBundle);
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.commit();
            } else if (notificationBundle.getParcelable(NotificationTypes.TICKET_UPLOAD_NOTIFICATION) != null) {
                fragment = new MyTravelReportFragment();
                fragment.setArguments(notificationBundle);
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.commit();
            } else if (notificationBundle.getParcelable(NotificationTypes.TRAVEL_FEED_BACK) == null) {
                fragment = new AttendanceFragment();
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(fragment.getFragmentName());
                fragmentTransaction.commit();
            } else if (notificationBundle.getParcelable(NotificationTypes.GUEST_HOUSE) == null) {
                fragment = new TravelFeedBackFragment();
                fragment.setArguments(notificationBundle);
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.commit();
            } else {
                fragment = new AttendanceFragment();
                fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(fragment.getFragmentName());
                fragmentTransaction.commit();
            }
        } else {*/
        fragment = KHomeFragment()
        fragmentTransaction.add(R.id.dash_board_content, fragment as KHomeFragment, fragment!!.getFragmentName())
        fragmentTransaction.addToBackStack(fragment!!.getFragmentName())
        fragmentTransaction.commit()
        //}
    }

    fun replaceFragment(fragmentToBePut: Int, bundle: Bundle) {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        fragment?.onDetach()
        drawerHandler.postDelayed({
            fragmentManager = supportFragmentManager
            toggleToolBarVisibility(true)
            //                    dashBoardActivityContext.toggleToolBarVisibility(true);
            when (fragmentToBePut) {
                //appBarLayout.setBackgroundResource(R.drawable.gradient_login_bg);
                KBAMConstant.Fragments.EDIT_PROFILE_FRAGMENTS ->
                    //fragment = new EditProfileFragment();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                KBAMConstant.Fragments.HOME_FRAGMENTS ->
                    fragment = KHomeFragment()
                //
                // 15-05-20 Sarvesh
                //
                /*KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS -> if (userProfile?.getSBU() != "WS") {
                    fragment = OrderProcessingFragment()
                }
                KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS1 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setOPPageNo(0)
                    fragment = OrderProcessingFragment()
                }
                KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS2 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setOPPageNo(1)
                    fragment = OrderProcessingFragment()
                }
                KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS3 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setOPPageNo(2)
                    fragment = OrderProcessingFragment()
                }
                KBAMConstant.Fragments.ORDERPROCESSING_FRAGMENTS4 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setOPPageNo(3)
                    fragment = OrderProcessingFragment()
                }*/
                KBAMConstant.Fragments.PURCHASE_FRAGMENTS -> if (userProfile?.getSBU() != "WS") {
                    //fragment = new PurchaseFragments();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                KBAMConstant.Fragments.PURCHASE_FRAGMENTS1 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setPurchasePageNo(0)
                    //fragment = new PurchaseFragments();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                KBAMConstant.Fragments.PURCHASE_FRAGMENTS2 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setPurchasePageNo(1)
                    //fragment = new PurchaseFragments();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                KBAMConstant.Fragments.PURCHASE_FRAGMENTS3 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setPurchasePageNo(2)
                    //fragment = new PurchaseFragments();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                KBAMConstant.Fragments.PURCHASE_FRAGMENTS4 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setPurchasePageNo(3)
                    //fragment = new PurchaseFragments();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                //
                // 15-05-20 Sarvesh
                //
                /*KBAMConstant.Fragments.LOGISTICS_FRAGMENTS -> if (userProfile?.getSBU() != "WS") {
                    fragment = LogisticsFragment()
                }
                KBAMConstant.Fragments.LOGISTICS_FRAGMENTS1 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setLogisticPageNo(0)
                    fragment = LogisticsFragment()
                }
                KBAMConstant.Fragments.LOGISTICS_FRAGMENTS2 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setLogisticPageNo(1)
                    fragment = LogisticsFragment()
                }
                KBAMConstant.Fragments.LOGISTICS_FRAGMENTS3 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setLogisticPageNo(2)
                    fragment = LogisticsFragment()
                }
                KBAMConstant.Fragments.LOGISTICS_FRAGMENTS4 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setLogisticPageNo(3)
                    fragment = LogisticsFragment()
                }
                KBAMConstant.Fragments.INSTALLATION_FRAGMENTS -> if (userProfile?.getSBU() != "WS") {
                    fragment = InstallationFragment()
                }
                KBAMConstant.Fragments.INSTALLATION_FRAGMENTS1 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setInstallationPageNo(0)
                    fragment = InstallationFragment()
                }
                KBAMConstant.Fragments.INSTALLATION_FRAGMENTS2 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setInstallationPageNo(1)
                    fragment = InstallationFragment()
                }
                KBAMConstant.Fragments.INSTALLATION_FRAGMENTS3 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setInstallationPageNo(2)
                    fragment = InstallationFragment()
                }
                KBAMConstant.Fragments.INSTALLATION_FRAGMENTS4 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setInstallationPageNo(3)
                    fragment = InstallationFragment()
                }*/
                KBAMConstant.Fragments.COLLECTION_FRAGMENTS -> if (userProfile?.getSBU() != "WS") {
                    //fragment = new CollectionFragment();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                KBAMConstant.Fragments.COLLECTION_FRAGMENTS1 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setCollectionPageNo(0)
                    //fragment = new CollectionFragment();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                KBAMConstant.Fragments.COLLECTION_FRAGMENTS2 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setCollectionPageNo(1)
                    //fragment = new CollectionFragment();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                KBAMConstant.Fragments.COLLECTION_FRAGMENTS3 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setCollectionPageNo(2)
                    //fragment = new CollectionFragment();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                KBAMConstant.Fragments.COLLECTION_FRAGMENTS4 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setCollectionPageNo(3)
                    //fragment = new CollectionFragment();
                    showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                }
                KBAMConstant.Fragments.OTHERS_FRAGMENTS -> showToast(KBAMConstant.ToastTexts.WORK_PROGRESS)
                //
                // 15-05-20 Sarvesh
                //
                KBAMConstant.Fragments.SR_FRAGMENTS ->
                    //fragment = new SalesReceivableFragment();
                    fragment = KSalesReceivableFragment()
                KBAMConstant.Fragments.SR_FRAGMENTS1 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setSalesReceivablePageNo(0)
                    fragment = KSalesReceivableFragment()
                }
                KBAMConstant.Fragments.SR_FRAGMENTS2 -> if (userProfile?.getSBU() != "WS") {
                    SharedPreferencesController.getInstance(this).setSalesReceivablePageNo(1)
                    fragment = KSalesReceivableFragment()
                }
                /*KBAMConstant.Fragments.FEEDBACK_FRAGMENTS -> fragment = FeedbackFragment()
                KBAMConstant.Fragments.RSM_ANALYSIS_FRAGMENT -> fragment = NewRSMTabFragment()
                KBAMConstant.Fragments.SALES_ANALYSIS_FRAGMENT -> fragment = NewSalesPersonTabFragment()
                KBAMConstant.Fragments.CUSTOMER_ANALYSIS_FRAGMENT -> fragment = NewCustomerTabFragment()
                KBAMConstant.Fragments.ACCOUNT_FRAGMENT -> fragment = AccountsFragment()
                KBAMConstant.Fragments.CUSTOMER_FRAGMENT -> fragment = CustomerFragment()
                KBAMConstant.Fragments.PRODUCT_FRAGMENT -> fragment = ProductFragment()
                KBAMConstant.Fragments.WS_RSM_FRAGMENT -> fragment = WSRSMFragment()
                KBAMConstant.Fragments.WS_ACCOUNT_FRAGMENT -> fragment = WSSalesPersonFragment()
                KBAMConstant.Fragments.WS_CUSTOMER_FRAGMENT -> fragment = WSCustomerFragment()
                KBAMConstant.Fragments.WS_PRODUCT_FRAGMENT -> fragment = WSProductFragment()
                KBAMConstant.Fragments.OSO_RSM_FRAGMENT -> fragment = OSORSMFragment()
                KBAMConstant.Fragments.OSO_ACCOUNT_FRAGMENT -> fragment = OSOSalesPersonFragment()
                KBAMConstant.Fragments.OSO_CUSTOMER_FRAGMENT -> fragment = OSOCustomerFragment()
                KBAMConstant.Fragments.OSO_INVOICE_FRAGMENT -> fragment = OSOInvoiceFragment()
                KBAMConstant.Fragments.TOS_RSM_FRAGMENT -> fragment = TOSRSMFragment()
                KBAMConstant.Fragments.TOS_ACCOUNT_FRAGMENT -> fragment = TOSSalesPersonFragment()
                KBAMConstant.Fragments.TOS_CUSTOMER_FRAGMENT -> fragment = TOSCustomerFragment()
                KBAMConstant.Fragments.TOS_PRODUCT_FRAGMENT -> fragment = TOSProductFragment()*/
                else -> fragment = KHomeFragment()
            }

            try {
                fragment?.setArguments(bundle)
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left)
                fragmentTransaction?.replace(R.id.dash_board_content, fragment as KBaseFragment, fragment?.getFragmentName())
                val backStackEntryCount = fragmentManager?.getBackStackEntryCount()
                if (bundle.getBoolean(IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED)) {
                    fragmentTransaction?.addToBackStack(fragment?.getFragmentName())
                    /*if (backStackEntryCount < 6) {
                    fragmentTransaction.addToBackStack(fragment.getFragmentName());
                }
            } else {
                if (backStackEntryCount < 2) {
                    fragmentTransaction.addToBackStack(fragment.getFragmentName());
                }*/
                }
                fragmentTransaction?.commitAllowingStateLoss()
            } catch (ignored: Exception) {
                //Crashlytics.logException(ignored);
            }
        }, 400)
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        if (visibility) {
            toolbar?.setVisibility(View.VISIBLE)
        } else {
            toolbar?.setVisibility(View.GONE)
        }
    }

    private fun getScreen() {
        val now = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd_hh:mm:ss", Locale.getDefault())
        val refreshDate = sdf.format(now)
        //View v = fragment.getView();
        //View v = this.getWindow().getCurrentFocus();
        //View v = findViewById(android.R.id.content).getRootView();
        if (null != fragmentView) {
            fragmentView!!.setDrawingCacheEnabled(true)
            val b = fragmentView!!.getDrawingCache()
            val mPath = Environment.getExternalStorageDirectory().toString() + "/" + refreshDate + ".jpeg"
            var myPath: File? = null
            myPath = File(mPath)
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(myPath)
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
                MediaStore.Images.Media.insertImage(contentResolver, b, "Screen", "screen")
                shareIt(myPath)
                //openScreenshot(myPath);
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun shareIt(imagePath: File) {
        val fileUri = FileProvider.getUriForFile(this, "com.teamcomputers.bam.provider",
                imagePath)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, fileUri)
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    fun home() {
        replaceFragment(KBAMConstant.Fragments.HOME_FRAGMENTS, Bundle())
    }

    fun SR() {
        val srBundle = Bundle()
        srBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true)
        replaceFragment(KBAMConstant.Fragments.SR_FRAGMENTS, srBundle)
    }

    fun showTab(level: String) {
        if (level == "R1" || level == "R2" || level == "R3" || level == "R4")
            llWSTabs.setVisibility(View.VISIBLE)
    }

    fun hideTab() {
        llWSTabs.setVisibility(View.GONE)
    }

    fun showOSOTab(level: String) {
        if (level == "R1" || level == "R2" || level == "R3" || level == "R4")
            llOSOTabs.setVisibility(View.VISIBLE)
    }

    fun hideOSOTab() {
        llOSOTabs.setVisibility(View.GONE)
    }

    fun showTOSTab(level: String) {
        if (level == "R1" || level == "R2" || level == "R3" || level == "R4")
            llTOSTabs.setVisibility(View.VISIBLE)
    }

    fun hideTOSTab() {
        llTOSTabs.setVisibility(View.GONE)
    }

}