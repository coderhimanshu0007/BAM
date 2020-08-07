package com.teamcomputers.bam.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.CustomView.TextViewCustom;
import com.teamcomputers.bam.ExpandableRecyclerview.expandables.NavigationExpandable;
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItem;
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItemParentModel;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.Collection.CollectionFragment;
import com.teamcomputers.bam.Fragments.Collection.TotalOutstandingFragment;
import com.teamcomputers.bam.Fragments.Collection.WIPDetailsFragment;
import com.teamcomputers.bam.Fragments.FeedbackFragment;
import com.teamcomputers.bam.Fragments.HelpFragment;
import com.teamcomputers.bam.Fragments.Installation.InstallationFragment;
import com.teamcomputers.bam.Fragments.Logistics.LogisticsFragment;
import com.teamcomputers.bam.Fragments.NewSalesReceivable.NewRSMTabFragment;
import com.teamcomputers.bam.Fragments.NewSalesReceivable.NewSalesReceivableFragment;
import com.teamcomputers.bam.Fragments.OpenSalesOrder.OSOCustomerFragment;
import com.teamcomputers.bam.Fragments.OpenSalesOrder.OSOInvoiceFragment;
import com.teamcomputers.bam.Fragments.OpenSalesOrder.OSOProductFragment;
import com.teamcomputers.bam.Fragments.OpenSalesOrder.OSORSMFragment;
import com.teamcomputers.bam.Fragments.OpenSalesOrder.OSOSalesPersonFragment;
import com.teamcomputers.bam.Fragments.OrderProcessing.OrderProcessingFragment;
import com.teamcomputers.bam.Fragments.Outstanding.TOSCustomerFragment;
import com.teamcomputers.bam.Fragments.Outstanding.TOSInvoiceFragment;
import com.teamcomputers.bam.Fragments.Outstanding.TOSProductFragment;
import com.teamcomputers.bam.Fragments.Outstanding.TOSRSMFragment;
import com.teamcomputers.bam.Fragments.Outstanding.TOSSalesPersonFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.CustomerFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.ProductFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.SalesReceivableFragment;
import com.teamcomputers.bam.Fragments.SalesReceivableR2.NewSalesPersonTabFragment;
import com.teamcomputers.bam.Fragments.SalesReceivableR4.NewCustomerTabFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSCustomerFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSProductFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSRSMFragment;
import com.teamcomputers.bam.Fragments.WSPages.WSSalesPersonFragment;
import com.teamcomputers.bam.Fragments.home.HomeFragment;
import com.teamcomputers.bam.Models.ActiveEmployeeAccessModel;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.SessionDataModel;
import com.teamcomputers.bam.Models.SessionDetailsModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.KSaveSessionDetailRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.CircularImageView;
import com.teamcomputers.bam.Utils.KBAMUtils;
import com.teamcomputers.bam.Utils.WrapContentLinearLayoutManager;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class DashboardActivity extends BaseActivity {
    public static final String IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED = "IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED";
    @BindView(R.id.llWSTabs)
    LinearLayout llWSTabs;
    @BindView(R.id.llOSOTabs)
    LinearLayout llOSOTabs;
    @BindView(R.id.llTOSTabs)
    LinearLayout llTOSTabs;
    /*@BindView(R.id.claMain)
    CoordinatorLayout claMain;*/
    @BindView(R.id.iviToolbarLogo)
    ImageView iviToolbarLogo;
    @BindView(R.id.toolbar_title)
    TextView tvToolBarTitle;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.iv_employee_profile)
    CircularImageView ivEmployee;
    @BindView(R.id.tv_employee_name)
    TextViewCustom tvEmployeeName;
    private Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.recycler_view_nav_item)
    RecyclerView expandableNavigationRecyclerView;
    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationExpandable navigationExpandable;
    List<NavigationItemParentModel> navigationItemParentModels = new ArrayList<>();
    private DashboardActivity dashboardActivityContext;
    private BaseFragment fragment;
    private LoginModel userProfile;
    private FragmentManager fragmentManager;
    private Handler drawerHandler = new Handler();
    private AppBarConfiguration mAppBarConfiguration;

    public View fragmentView;

    public String userId = "", level = "", selectedFiscalYear = "";
    public int selectedPosition = 0, sharing = 0, levelSharing = 0;

    ActiveEmployeeAccessModel activeEmployeeAccessModel;
    SessionDataModel level1sessionData = new SessionDataModel();
    SessionDataModel sessionDataModel = new SessionDataModel();
    Date currentDate;
    String logInTime, logOutTime;

    @Override
    protected int getLayout() {
        return R.layout.activity_dashboard;
    }

    @Subscribe
    @Override
    public void onEvent(EventObject eventObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case Events.ORDER_PROCESSING:
                        Bundle opBundle = new Bundle();
                        opBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        replaceFragment(Fragments.ORDERPROCESSING_FRAGMENTS, opBundle);
                        break;
                    case Events.PURCHASE:
                        Bundle pBundle = new Bundle();
                        pBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        replaceFragment(Fragments.PURCHASE_FRAGMENTS, pBundle);
                        break;
                    case Events.LOGISTICS:
                        Bundle lBundle = new Bundle();
                        lBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        replaceFragment(Fragments.LOGISTICS_FRAGMENTS, lBundle);
                        break;
                    case Events.INSTALLATION:
                        Bundle iBundle = new Bundle();
                        iBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        replaceFragment(Fragments.INSTALLATION_FRAGMENTS, iBundle);
                        break;
                    case Events.COLLECTION:
                        Bundle cBundle = new Bundle();
                        cBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        replaceFragment(Fragments.COLLECTION_FRAGMENTS, cBundle);
                        break;
                    case Events.WS:
                        Bundle srBundle = new Bundle();
                        srBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        replaceFragment(Fragments.SR_FRAGMENTS, srBundle);
                        break;
                    case Events.OTHERS:
                        replaceFragment(Fragments.OTHERS_FRAGMENTS, new Bundle());
                        break;
                    /*case Events.SALESANALYSIS:
                        replaceFragment(Fragments.SALES_ANALYSIS_FRAGMENT, new Bundle());
                        break;*/
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardActivityContext = this;
        if (SharedPreferencesController.getInstance(dashboardActivityContext).isUserLoggedIn()) {
            toolbar = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            //NavigationView navigationView = findViewById(R.id.nav_view);

            activeEmployeeAccessModel = SharedPreferencesController.getInstance(BAMApplication.getInstance()).getActiveEmployeeAccess();

            drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                    super.onDrawerClosed(drawerView);
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(dashboardActivityContext.getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                    super.onDrawerOpened(drawerView);
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(dashboardActivityContext.getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            };
            initTimer();
            drawer.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
            setNavigationDrawerData();
            putFragmentInContainer(savedInstanceState);
            reloadNavigationDrawer();
            userProfile = SharedPreferencesController.getInstance(BAMApplication.getInstance()).getUserProfile();
            String name = null;
            if (userProfile.getMemberName().split(" ").length > 2) {
                name = userProfile.getMemberName().split(" ")[0]
                        + " " + userProfile.getMemberName().split(" ")[userProfile.getMemberName().split(" ").length - 1];
            } else if (userProfile.getMemberName().split(" ").length <= 2) {
                name = userProfile.getMemberName();
            }
            //tvEmployeeName.setText(userProfile.getMemberName() == null ? "No Name" : userProfile.getMemberName());
            tvEmployeeName.setText(name == null ? "No Name" : name);
        } else {
            logoutAction();
        }
        //tvEmployeeEmailId.setText(userProfile.getCompaneyEmail());
        /*if (!userProfile.getUserImage().contains("No Image")) {
            ivEmployee.setImageBitmap(BAMUtil.getImageBitMap(userProfile.getUserImage()));
        }*/
        /*expandableNavigationRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        navigationExpandable = new NavigationExpandable(navigationItemParentModels, this);
        expandableNavigationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        expandableNavigationRecyclerView.setAdapter(navigationExpandable);*/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /* mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        MenuItem action_screen_share = menu.findItem(R.id.action_screen_share);
        action_screen_share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getScreen();
                return false;
            }
        });
        /*MenuItem action_search = menu.findItem(R.id.action_search);
        action_search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //getScreen();
                if (fragment.getFragmentName().equals("WSRSMFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.WS_RSM_SEARCH, null));
                } else if (fragment.getFragmentName().equals("WSSalesPersonFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.WS_SP_SEARCH, null));
                } else if (fragment.getFragmentName().equals("WSCustomerFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.WS_CUSTOMER_SEARCH, null));
                } else if (fragment.getFragmentName().equals("WSProductFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.WS_PRODUCT_SEARCH, null));
                } else if (fragment.getFragmentName().equals("OSORSMFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.OSO_RSM_SEARCH, null));
                } else if (fragment.getFragmentName().equals("OSOSalesPersonFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.OSO_SP_SEARCH, null));
                } else if (fragment.getFragmentName().equals("OSOCustomerFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.OSO_CUSTOMER_SEARCH, null));
                } else if (fragment.getFragmentName().equals("OSOProductFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.OSO_PRODUCT_SEARCH, null));
                } else if (fragment.getFragmentName().equals("TOSRSMFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.TOS_RSM_SEARCH, null));
                } else if (fragment.getFragmentName().equals("TOSSalesPersonFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.TOS_SP_SEARCH, null));
                } else if (fragment.getFragmentName().equals("TOSCustomerFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.TOS_CUSTOMER_SEARCH, null));
                } else if (fragment.getFragmentName().equals("TOSProductFragment")) {
                    EventBus.getDefault().post(new EventObject(ClickEvents.TOS_PRODUCT_SEARCH, null));
                }
                return false;
            }
        });*/
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cTimer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cTimer.start();
    }

    CountDownTimer cTimer;

    private void initTimer() {
        cTimer = new CountDownTimer(1 * 60 * 1000, 30000) {
            public void onTick(long millisUntilFinished) {
                Log.v(Constants.TAG, "Service Started");
            }

            public void onFinish() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Log.v(Constants.TAG, "Service Finish");
                //ActivityCompat.finishAffinity(DashboardActivity.this);
                currentDate = Calendar.getInstance().getTime();
                logOutTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);

                SessionDetailsModel sessionDetailsModel = new SessionDetailsModel();
                sessionDetailsModel.setSessionId(activeEmployeeAccessModel.getData().getSessionId());
                List<SessionDataModel> sessionDataModelList = new ArrayList<>();
                if (null != SharedPreferencesController.getInstance(dashboardActivityContext).getSessionDetail()) {
                    sessionDataModelList = SharedPreferencesController.getInstance(dashboardActivityContext).getSessionDetail().getData();
                }
                if (fragmentManager.getBackStackEntryCount() == 2) {
                    level1sessionData.setLogOutTimeStamp(logOutTime);
                    level1sessionData.setOs("Android");
                    level1sessionData.setSharing(levelSharing);
                    sessionDataModelList.add(level1sessionData);
                } else if (fragmentManager.getBackStackEntryCount() > 2) {
                    //getSessionData();
                    sessionDataModel.setLogOutTimeStamp(logOutTime);
                    sessionDataModel.setOs("Android");
                    sessionDataModel.setSharing(sharing);
                    sessionDataModelList.add(sessionDataModel);
                    // WS Dashboard Data
                    level1sessionData.setLogOutTimeStamp(logOutTime);
                    level1sessionData.setOs("Android");
                    level1sessionData.setSharing(levelSharing);
                    sessionDataModelList.add(level1sessionData);
                }
                sessionDetailsModel.setData(sessionDataModelList);
                SharedPreferencesController.getInstance(dashboardActivityContext).setSessionDetail(sessionDetailsModel);
                uploadData();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uploadData();
    }

    private void uploadData() {
        SessionDetailsModel sessionDetailsModel = SharedPreferencesController.getInstance(dashboardActivityContext).getSessionDetail();
        if (null != sessionDetailsModel) {
            String session = "{\n\t\"SessionId\":" + sessionDetailsModel.getSessionId() + ",\n\t\"Data\":[\n\t\t";
            for (int i = 0; i < sessionDetailsModel.getData().size(); i++) {
                String data = "{\n\t\t\t\"Module\":\"" + sessionDetailsModel.getData().get(i).getModule() + "\"," +
                        "\n\t\t\t\"Page\":\"" + sessionDetailsModel.getData().get(i).getPage() + "\"," +
                        "\n\t\t\t\"LogInTimeStamp\":\"" + sessionDetailsModel.getData().get(i).getLogInTimeStamp() + "\"," +
                        "\n\t\t\t\"LogOutTimeStamp\":\"" + sessionDetailsModel.getData().get(i).getLogOutTimeStamp() + "\"," +
                        "\n\t\t\t\"OS\":\"" + sessionDetailsModel.getData().get(i).getOs() + "\"," +
                        "\n\t\t\t\"Sharing\":" + sessionDetailsModel.getData().get(i).getSharing() + "\n\t\t}";
                if (i > 0) {
                    session = session + "," + data;
                } else {
                    session = session + data;
                }
            }
            session = session + "\n\t]\n}";
            BackgroundExecutor.getInstance().execute(new KSaveSessionDetailRequester(session));
        }
    }

    @OnClick(R.id.ll_home)
    public void home() {
        replaceFragment(Fragments.HOME_FRAGMENTS, new Bundle());
    }

    @OnClick(R.id.ll_SR)
    public void SR() {
        Bundle srBundle = new Bundle();
        srBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        replaceFragment(Fragments.SR_FRAGMENTS, srBundle);
    }

    @OnClick(R.id.ll_help)
    public void favourits() {
        replaceFragment(Fragments.HELP_FRAGMENTS, new Bundle());
    }

    @OnClick(R.id.ll_feedback)
    public void feedback() {
        replaceFragment(Fragments.FEEDBACK_FRAGMENTS, new Bundle());
    }

    private void reloadNavigationDrawer() {
        navigationItemParentModels.clear();
        setNavigationDrawerData();
        expandableNavigationRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(dashboardActivityContext));
        navigationExpandable = new NavigationExpandable(navigationItemParentModels, dashboardActivityContext);
        expandableNavigationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        expandableNavigationRecyclerView.setAdapter(navigationExpandable);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setToolBarTitle(String title) {
        tvToolBarTitle.setText(title);
        if (title.equals(getString(R.string.Heading_Home))) {
            iviToolbarLogo.setVisibility(View.VISIBLE);
            tvToolBarTitle.setVisibility(View.GONE);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                claMain.setBackground(getResources().getDrawable(R.drawable.gradient_drawable));
            }*/
        } else {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                claMain.setBackground(getResources().getDrawable(R.drawable.gradient_login_bg));
            }*/
            iviToolbarLogo.setVisibility(View.GONE);
            tvToolBarTitle.setVisibility(View.VISIBLE);
        }
        if (title.equals(getString(R.string.Heading_Home)) || title.equals(getString(R.string.Heading_Feedback))) {
            tv_date.setVisibility(View.GONE);
        } else {
            tv_date.setVisibility(View.VISIBLE);
        }
    }

    public void updateDate(String date) {
        Date newDate = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy\nhh:mm:ss aa", Locale.getDefault());
        String refreshDate = sdf.format(newDate);
        tv_date.setText(String.valueOf(refreshDate) + "\nValues In Lacs");
    }

    private void setNavigationDrawerData() {
        List<NavigationItem> navigationOrderProcessing = new ArrayList<>();
        if (activeEmployeeAccessModel.getData().getOrderProcessingModule() == 1) {
            navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP1)), Fragments.ORDERPROCESSING_FRAGMENTS1));
            navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP2)), Fragments.ORDERPROCESSING_FRAGMENTS2));
            navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP3)), Fragments.ORDERPROCESSING_FRAGMENTS3));
            navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP4)), Fragments.ORDERPROCESSING_FRAGMENTS4));
        } else if (activeEmployeeAccessModel.getData().getOrderProcessingModule() == 0) {
            navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP1)), 0));
            navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP2)), 0));
            navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP3)), 0));
            navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP4)), 0));
        }

        List<NavigationItem> navigationPurchase = new ArrayList<>();
        navigationPurchase.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Purchanse1)), Fragments.PURCHASE_FRAGMENTS1));
        navigationPurchase.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Purchanse2)), Fragments.PURCHASE_FRAGMENTS2));
        navigationPurchase.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Purchanse3)), Fragments.PURCHASE_FRAGMENTS3));
        navigationPurchase.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Purchanse4)), Fragments.PURCHASE_FRAGMENTS4));

        List<NavigationItem> navigationLogistics = new ArrayList<>();
        navigationLogistics.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Logistics1)), Fragments.LOGISTICS_FRAGMENTS1));
        navigationLogistics.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Logistics2)), Fragments.LOGISTICS_FRAGMENTS2));
        navigationLogistics.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Logistics3)), Fragments.LOGISTICS_FRAGMENTS3));
        navigationLogistics.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Logistics4)), Fragments.LOGISTICS_FRAGMENTS4));

        List<NavigationItem> navigationInstallation = new ArrayList<>();
        navigationInstallation.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Installation1)), Fragments.INSTALLATION_FRAGMENTS1));
        navigationInstallation.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Installation2)), Fragments.INSTALLATION_FRAGMENTS2));
        navigationInstallation.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Installation3)), Fragments.INSTALLATION_FRAGMENTS3));
        navigationInstallation.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Installation4)), Fragments.INSTALLATION_FRAGMENTS4));

        List<NavigationItem> navigationCollection = new ArrayList<>();
        navigationCollection.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Collection1)), Fragments.COLLECTION_FRAGMENTS1));
        navigationCollection.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Collection2)), Fragments.COLLECTION_FRAGMENTS2));
        navigationCollection.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Collection3)), Fragments.COLLECTION_FRAGMENTS3));
        navigationCollection.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Collection4)), Fragments.COLLECTION_FRAGMENTS4));

        List<NavigationItem> navigationOthers = new ArrayList<>();
        navigationOthers.add(new NavigationItem(new SpannableStringBuilder("-" + getString(R.string.Others)), Fragments.OTHERS_FRAGMENTS));
        //navigationOthers.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Others2)), Fragments.OTHERS_FRAGMENTS));
        //navigationOthers.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Others3)), Fragments.OTHERS_FRAGMENTS));
        //navigationOthers.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Others4)), Fragments.OTHERS_FRAGMENTS));

        List<NavigationItem> navigationSR = new ArrayList<>();
        navigationSR.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.SalsReceivable1)), Fragments.SR_FRAGMENTS1));
        navigationSR.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.SalsReceivable2)), Fragments.SR_FRAGMENTS2));

        NavigationItemParentModel navigationOrderProcessingParent = new NavigationItemParentModel();
        navigationOrderProcessingParent.setNavImageParent(R.drawable.ic_group_menu_order_processing);
        navigationOrderProcessingParent.setNavTitleParent(getString(R.string.OrderProcessing));
        navigationOrderProcessingParent.setNavigationItems(navigationOrderProcessing);

        NavigationItemParentModel navigationPurchaseParent = new NavigationItemParentModel();
        navigationPurchaseParent.setNavImageParent(R.drawable.ic_group_menu_purchase);
        navigationPurchaseParent.setNavTitleParent(getString(R.string.Purchanse));
        navigationPurchaseParent.setNavigationItems(navigationPurchase);

        NavigationItemParentModel navigationLogisticsParent = new NavigationItemParentModel();
        navigationLogisticsParent.setNavImageParent(R.drawable.ic_group_menu_logistics);
        navigationLogisticsParent.setNavTitleParent(getString(R.string.Logistics));
        navigationLogisticsParent.setNavigationItems(navigationLogistics);

        NavigationItemParentModel navigationInstallationParent = new NavigationItemParentModel();
        navigationInstallationParent.setNavImageParent(R.drawable.ic_group_menu_installation);
        navigationInstallationParent.setNavTitleParent(getString(R.string.Installation));
        navigationInstallationParent.setNavigationItems(navigationInstallation);

        NavigationItemParentModel navigationCollectionParent = new NavigationItemParentModel();
        navigationCollectionParent.setNavImageParent(R.drawable.ic_group_menu_collection);
        navigationCollectionParent.setNavTitleParent(getString(R.string.Collection));
        navigationCollectionParent.setNavigationItems(navigationCollection);

        NavigationItemParentModel navigationOthersParent = new NavigationItemParentModel();
        navigationOthersParent.setNavImageParent(R.drawable.ic_menu_others);
        navigationOthersParent.setNavTitleParent(getString(R.string.Others));
        navigationOthersParent.setNavigationItems(navigationOthers);

        NavigationItemParentModel navigationWSParent = new NavigationItemParentModel();
        navigationWSParent.setNavImageParent(R.drawable.ic_menu_others);
        navigationWSParent.setNavTitleParent(getString(R.string.SalesReceivable));
        navigationWSParent.setNavigationItems(navigationSR);

        /*if (SharedPreferencesController.getInstance(TeamWorksApplication.getInstance()).getUserProfile().getIsHead().equals("1")) {
            navigationItemParentModels.add(navigationItemParentMyTeam);
        }*/
        navigationItemParentModels.add(navigationOrderProcessingParent);
        navigationItemParentModels.add(navigationPurchaseParent);
        navigationItemParentModels.add(navigationLogisticsParent);
        navigationItemParentModels.add(navigationInstallationParent);
        navigationItemParentModels.add(navigationCollectionParent);
        //navigationItemParentModels.add(navigationWSParent);
    }

    @OnClick(R.id.iviEditProfile)
    public void editProfile() {
        Bundle userProfileBundle = new Bundle();
        //userProfileBundle.putParcelable(UserProfileFragment.USER_PROFILE, userProfile);
        //userProfileBundle.putString(UserProfileFragment.PROFILE_MODE, UserProfileFragment.MODE_MY_PROFILE);
        replaceFragment(Fragments.EDIT_PROFILE_FRAGMENTS, userProfileBundle);
    }

    @OnClick(R.id.ll_logout)
    public void logout() {
        logoutAction();
    }

    public void logoutAction() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        SharedPreferencesController.getInstance(BAMApplication.getInstance()).clear();
        ActivityCompat.finishAffinity(DashboardActivity.this);
        Intent intent = new Intent(DashboardActivity.this, KSplashActivity.class);
        intent.putExtra(Constants.FINISH, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void putFragmentInContainer(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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
        fragment = new HomeFragment();
        fragmentTransaction.add(R.id.dash_board_content, fragment, fragment.getFragmentName());
        fragmentTransaction.addToBackStack(fragment.getFragmentName());
        fragmentTransaction.commit();
        //}
    }

    public void replaceFragment(final int fragmentToBePut, final Bundle bundle) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        fragment.onDetach();
        drawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager = getSupportFragmentManager();
                toggleToolBarVisibility(true);
//                    dashBoardActivityContext.toggleToolBarVisibility(true);
                switch (fragmentToBePut) {
                    //appBarLayout.setBackgroundResource(R.drawable.gradient_login_bg);
                    case Fragments.EDIT_PROFILE_FRAGMENTS:
                        //fragment = new EditProfileFragment();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.HOME_FRAGMENTS:
                        fragment = new HomeFragment();
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS:
                        if (activeEmployeeAccessModel.getData().getOrderProcessingModule() == 1) {
                            levelSharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            level1sessionData.setModule("Order Processing");
                            level1sessionData.setPage("");
                            level1sessionData.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(level1sessionData);
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS1:
                        if (activeEmployeeAccessModel.getData().getOrderProcessingModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(0);
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS2:
                        if (activeEmployeeAccessModel.getData().getOrderProcessingModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(1);
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS3:
                        if (activeEmployeeAccessModel.getData().getOrderProcessingModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(2);
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS4:
                        if (activeEmployeeAccessModel.getData().getOrderProcessingModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(3);
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS:
                        if (activeEmployeeAccessModel.getData().getPurchaseModule() == 1) {
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS1:
                        if (activeEmployeeAccessModel.getData().getPurchaseModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(0);
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS2:
                        if (activeEmployeeAccessModel.getData().getPurchaseModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(1);
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS3:
                        if (activeEmployeeAccessModel.getData().getPurchaseModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(2);
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS4:
                        if (activeEmployeeAccessModel.getData().getPurchaseModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(3);
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS:
                        if (activeEmployeeAccessModel.getData().getLogisticsModule() == 1) {
                            levelSharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            level1sessionData.setModule("Logistics");
                            level1sessionData.setPage("");
                            level1sessionData.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(level1sessionData);
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS1:
                        if (activeEmployeeAccessModel.getData().getLogisticsModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(0);
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS2:
                        if (activeEmployeeAccessModel.getData().getLogisticsModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(1);
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS3:
                        if (activeEmployeeAccessModel.getData().getLogisticsModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(2);
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS4:
                        if (activeEmployeeAccessModel.getData().getLogisticsModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(3);
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS:
                        if (activeEmployeeAccessModel.getData().getInstallationModule() == 1) {
                            levelSharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            level1sessionData.setModule("Installation");
                            level1sessionData.setPage("");
                            level1sessionData.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(level1sessionData);
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS1:
                        if (activeEmployeeAccessModel.getData().getInstallationModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(0);
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS2:
                        if (activeEmployeeAccessModel.getData().getInstallationModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(1);
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS3:
                        if (activeEmployeeAccessModel.getData().getInstallationModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(2);
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS4:
                        if (activeEmployeeAccessModel.getData().getInstallationModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(3);
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS:
                        if (activeEmployeeAccessModel.getData().getCollectionModule() == 1) {
                            fragment = new CollectionFragment();
                        //    showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS1:
                        if (activeEmployeeAccessModel.getData().getCollectionModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(0);
                            //fragment = new CollectionFragment();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS2:
                        if (activeEmployeeAccessModel.getData().getCollectionModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(1);
                            //fragment = new CollectionFragment();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS3:
                        if (activeEmployeeAccessModel.getData().getCollectionModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(2);
                            //fragment = new CollectionFragment();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS4:
                        if (activeEmployeeAccessModel.getData().getCollectionModule() == 1) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(3);
                            //fragment = new CollectionFragment();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.OTHERS_FRAGMENTS:
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.SR_FRAGMENTS:
                        if (activeEmployeeAccessModel.getData().getSalesModule() == 1) {
                            levelSharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            level1sessionData.setModule("WSDashboard");
                            level1sessionData.setPage("");
                            level1sessionData.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(level1sessionData);
                            fragment = new NewSalesReceivableFragment();
                        }
                        break;
                    case Fragments.SR_FRAGMENTS1:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSalesReceivablePageNo(0);
                            fragment = new SalesReceivableFragment();
                        }
                        break;
                    case Fragments.SR_FRAGMENTS2:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSalesReceivablePageNo(1);
                            fragment = new SalesReceivableFragment();
                        }
                        break;
                    case Fragments.HELP_FRAGMENTS:
                        fragment = new HelpFragment();
                        break;
                    case Fragments.FEEDBACK_FRAGMENTS:
                        fragment = new FeedbackFragment();
                        break;
                    case Fragments.RSM_ANALYSIS_FRAGMENT:
                        fragment = new NewRSMTabFragment();
                        break;
                    case Fragments.SALES_ANALYSIS_FRAGMENT:
                        fragment = new NewSalesPersonTabFragment();
                        break;
                    case Fragments.CUSTOMER_ANALYSIS_FRAGMENT:
                        fragment = new NewCustomerTabFragment();
                        break;
                    case Fragments.ACCOUNT_FRAGMENT:
                        fragment = new AccountsFragment();
                        break;
                    case Fragments.CUSTOMER_FRAGMENT:
                        fragment = new CustomerFragment();
                        break;
                    case Fragments.PRODUCT_FRAGMENT:
                        fragment = new ProductFragment();
                        break;
                    case Fragments.TOTAL_OUTSTANDING_FRAGMENT:
                        fragment = new TotalOutstandingFragment();
                        break;
                    case Fragments.WIP_FRAGMENT:
                        fragment = new WIPDetailsFragment();
                        break;
                    case Fragments.WS_RSM_FRAGMENT:
                        if (bundle.get("USER_LEVEL").equals("R0") || bundle.get("USER_LEVEL").equals("R1")) {
                            sharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            sessionDataModel.setModule("WSDashboard");
                            sessionDataModel.setPage("Sales");
                            sessionDataModel.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(sessionDataModel);
                        }
                        fragment = new WSRSMFragment();
                        break;
                    case Fragments.WS_ACCOUNT_FRAGMENT:
                        if (bundle.get("USER_LEVEL").equals("R2") || bundle.get("USER_LEVEL").equals("R3")) {
                            sharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            sessionDataModel.setModule("WSDashboard");
                            sessionDataModel.setPage("Sales");
                            sessionDataModel.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(sessionDataModel);
                        }
                        fragment = new WSSalesPersonFragment();
                        break;
                    case Fragments.WS_CUSTOMER_FRAGMENT:
                        if (bundle.get("USER_LEVEL").equals("R4")) {
                            sharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            sessionDataModel.setModule("WSDashboard");
                            sessionDataModel.setPage("Sales");
                            sessionDataModel.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(sessionDataModel);
                        }
                        fragment = new WSCustomerFragment();
                        break;
                    case Fragments.WS_PRODUCT_FRAGMENT:
                        fragment = new WSProductFragment();
                        break;
                    case Fragments.OSO_RSM_FRAGMENT:
                        if (bundle.get("USER_LEVEL").equals("R0") || bundle.get("USER_LEVEL").equals("R1")) {
                            sharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            sessionDataModel.setModule("WSDashboard");
                            sessionDataModel.setPage("OSO");
                            sessionDataModel.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(sessionDataModel);
                        }
                        fragment = new OSORSMFragment();
                        break;
                    case Fragments.OSO_ACCOUNT_FRAGMENT:
                        if (bundle.get("USER_LEVEL").equals("R2") || bundle.get("USER_LEVEL").equals("R3")) {
                            sharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            sessionDataModel.setModule("WSDashboard");
                            sessionDataModel.setPage("OSO");
                            sessionDataModel.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(sessionDataModel);
                        }
                        fragment = new OSOSalesPersonFragment();
                        break;
                    case Fragments.OSO_CUSTOMER_FRAGMENT:
                        if (bundle.get("USER_LEVEL").equals("R4")) {
                            sharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            sessionDataModel.setModule("WSDashboard");
                            sessionDataModel.setPage("OSO");
                            sessionDataModel.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(sessionDataModel);
                        }
                        fragment = new OSOCustomerFragment();
                        break;
                    case Fragments.OSO_INVOICE_FRAGMENT:
                        fragment = new OSOInvoiceFragment();
                        break;
                    case Fragments.OSO_PRODUCT_FRAGMENT:
                        fragment = new OSOProductFragment();
                        break;
                    case Fragments.TOS_RSM_FRAGMENT:
                        if (bundle.get("USER_LEVEL").equals("R0") || bundle.get("USER_LEVEL").equals("R1")) {
                            sharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            sessionDataModel.setModule("WSDashboard");
                            sessionDataModel.setPage("Net Receivable");
                            sessionDataModel.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(sessionDataModel);
                        }
                        fragment = new TOSRSMFragment();
                        break;
                    case Fragments.TOS_ACCOUNT_FRAGMENT:
                        if (bundle.get("USER_LEVEL").equals("R2") || bundle.get("USER_LEVEL").equals("R3")) {
                            sharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            sessionDataModel.setModule("WSDashboard");
                            sessionDataModel.setPage("Net Receivable");
                            sessionDataModel.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(sessionDataModel);
                        }
                        fragment = new TOSSalesPersonFragment();
                        break;
                    case Fragments.TOS_CUSTOMER_FRAGMENT:
                        if (bundle.get("USER_LEVEL").equals("R4")) {
                            sharing = 0;
                            currentDate = Calendar.getInstance().getTime();
                            logInTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);
                            sessionDataModel.setModule("WSDashboard");
                            sessionDataModel.setPage("Net Receivable");
                            sessionDataModel.setLogInTimeStamp(logInTime);
                            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionData(sessionDataModel);
                        }
                        fragment = new TOSCustomerFragment();
                        break;
                    case Fragments.TOS_PRODUCT_FRAGMENT:
                        fragment = new TOSProductFragment();
                        break;
                    case Fragments.TOS_INVOICE_FRAGMENT:
                        fragment = new TOSInvoiceFragment();
                        break;
                    /*case 0:
                        fragment = null;
                        break;*/
                    default:
                        fragment = new HomeFragment();
                        break;
                }

                try {
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                        fragmentTransaction.replace(R.id.dash_board_content, fragment, fragment.getFragmentName());
                        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
                        if (bundle.getBoolean(IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED)) {
                            fragmentTransaction.addToBackStack(fragment.getFragmentName());
                        /*if (backStackEntryCount < 6) {
                            fragmentTransaction.addToBackStack(fragment.getFragmentName());
                        }
                    } else {
                        if (backStackEntryCount < 2) {
                            fragmentTransaction.addToBackStack(fragment.getFragmentName());
                        }*/
                        }
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                } catch (Exception ignored) {
                    //Crashlytics.logException(ignored);
                }
            }
        }, 400);
    }

    public void toggleToolBarVisibility(boolean visibility) {
        if (visibility) {
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            //this.finish();
            ActivityCompat.finishAffinity(DashboardActivity.this);
        }
        if (fragmentManager.getBackStackEntryCount() == 1) {
            currentDate = Calendar.getInstance().getTime();
            logOutTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);

            SessionDetailsModel sessionDetailsModel = new SessionDetailsModel();
            sessionDetailsModel.setSessionId(activeEmployeeAccessModel.getData().getSessionId());
            List<SessionDataModel> sessionDataModelList = new ArrayList<>();
            if (null != SharedPreferencesController.getInstance(dashboardActivityContext).getSessionDetail()) {
                sessionDataModelList = SharedPreferencesController.getInstance(dashboardActivityContext).getSessionDetail().getData();
            }
            level1sessionData.setLogOutTimeStamp(logOutTime);
            level1sessionData.setOs("Android");
            level1sessionData.setSharing(levelSharing);
            sessionDataModelList.add(level1sessionData);
            sessionDetailsModel.setData(sessionDataModelList);
            SharedPreferencesController.getInstance(dashboardActivityContext).setSessionDetail(sessionDetailsModel);
            uploadData();
        }
        if (fragmentManager.getBackStackEntryCount() == 2) {
            getSessionData();
        }

    }

    private void getSessionData() {
        currentDate = Calendar.getInstance().getTime();
        logOutTime = KBAMUtils.getFormattedDate(DateFormat.SESSION_DATE_FORMAT, currentDate);

        SessionDetailsModel sessionDetailsModel = new SessionDetailsModel();
        sessionDetailsModel.setSessionId(activeEmployeeAccessModel.getData().getSessionId());
        List<SessionDataModel> sessionDataModelList = new ArrayList<>();
        if (null != SharedPreferencesController.getInstance(dashboardActivityContext).getSessionDetail()) {
            sessionDataModelList = SharedPreferencesController.getInstance(dashboardActivityContext).getSessionDetail().getData();
        }
        sessionDataModel.setLogOutTimeStamp(logOutTime);
        sessionDataModel.setOs("Android");
        sessionDataModel.setSharing(sharing);
        sessionDataModelList.add(sessionDataModel);
        sessionDetailsModel.setData(sessionDataModelList);
        SharedPreferencesController.getInstance(dashboardActivityContext).setSessionDetail(sessionDetailsModel);
        uploadData();
    }

    private void getScreen() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss", Locale.getDefault());
        String refreshDate = sdf.format(now);
        //View v = fragment.getView();
        //View v = dashboardActivityContext.getWindow().getCurrentFocus();
        //View v = findViewById(android.R.id.content).getRootView();
        if (null != fragmentView) {
            fragmentView.setDrawingCacheEnabled(true);
            Bitmap b = fragmentView.getDrawingCache();
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + refreshDate + ".jpeg";
            File myPath = null;
            myPath = new File(mPath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(myPath);
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
                shareIt(myPath);
                //openScreenshot(myPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void shareIt(File imagePath) {
        if (fragmentManager.getBackStackEntryCount() > 2) {
            sharing++;
            SharedPreferencesController.getInstance(dashboardActivityContext).setSharing(sharing);
        } else if (fragmentManager.getBackStackEntryCount() == 2) {
            levelSharing++;
            SharedPreferencesController.getInstance(dashboardActivityContext).setSharing(levelSharing);
        }
        Uri fileUri = FileProvider.getUriForFile(dashboardActivityContext, "com.teamcomputers.bam.provider",
                imagePath);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    @BindView(R.id.llRSM_Nav_tab)
    LinearLayout llRSM_Nav_tab;
    @BindView(R.id.llSP_Nav_tab)
    LinearLayout llSP_Nav_tab;

    @BindView(R.id.llOSORSM_Nav_tab)
    LinearLayout llOSORSM_Nav_tab;
    @BindView(R.id.llOSOSP_Nav_tab)
    LinearLayout llOSOSP_Nav_tab;

    @BindView(R.id.llTOSRSM_Nav_tab)
    LinearLayout llTOSRSM_Nav_tab;
    @BindView(R.id.llTOSSP_Nav_tab)
    LinearLayout llTOSSP_Nav_tab;

    @BindView(R.id.iviHomeNav_icon)
    ImageView iviHomeNav_icon;
    @BindView(R.id.tviHome_Nav_label)
    TextView tviHome_Nav_label;
    @BindView(R.id.iviRSMNav_icon)
    ImageView iviRSMNav_icon;
    @BindView(R.id.tviRSM_Nav_label)
    TextView tviRSM_Nav_label;
    @BindView(R.id.iviSP_Nav_icon)
    ImageView iviSP_Nav_icon;
    @BindView(R.id.tviSP_Nav_label)
    TextView tviSP_Nav_label;
    @BindView(R.id.iviCustomer_Nav_icon)
    ImageView iviCustomer_Nav_icon;
    @BindView(R.id.tviCustomer_Nav_label)
    TextView tviCustomer_Nav_label;
    @BindView(R.id.iviProduct_Nav_icon)
    ImageView iviProduct_Nav_icon;
    @BindView(R.id.tviProduct_Nav_label)
    TextView tviProduct_Nav_label;

    @BindView(R.id.iviOSOHomeNav_icon)
    ImageView iviOSOHomeNav_icon;
    @BindView(R.id.tviOSOHome_Nav_label)
    TextView tviOSOHome_Nav_label;
    @BindView(R.id.iviOSORSMNav_icon)
    ImageView iviOSORSMNav_icon;
    @BindView(R.id.tviOSORSM_Nav_label)
    TextView tviOSORSM_Nav_label;
    @BindView(R.id.iviOSOSP_Nav_icon)
    ImageView iviOSOSP_Nav_icon;
    @BindView(R.id.tviOSOSP_Nav_label)
    TextView tviOSOSP_Nav_label;
    @BindView(R.id.iviOSOCustomer_Nav_icon)
    ImageView iviOSOCustomer_Nav_icon;
    @BindView(R.id.tviOSOCustomer_Nav_label)
    TextView tviOSOCustomer_Nav_label;
    @BindView(R.id.iviOSOInvoice_Nav_icon)
    ImageView iviOSOInvoice_Nav_icon;
    @BindView(R.id.tviOSOInvoice_Nav_label)
    TextView tviOSOInvoice_Nav_label;
    @BindView(R.id.iviOSOProduct_Nav_icon)
    ImageView iviOSOProduct_Nav_icon;
    @BindView(R.id.tviOSOProduct_Nav_label)
    TextView tviOSOProduct_Nav_label;

    @BindView(R.id.iviTOSHomeNav_icon)
    ImageView iviTOSHomeNav_icon;
    @BindView(R.id.tviTOSHome_Nav_label)
    TextView tviTOSHome_Nav_label;
    @BindView(R.id.iviTOSRSMNav_icon)
    ImageView iviTOSRSMNav_icon;
    @BindView(R.id.tviTOSRSM_Nav_label)
    TextView tviTOSRSM_Nav_label;
    @BindView(R.id.iviTOSSP_Nav_icon)
    ImageView iviTOSSP_Nav_icon;
    @BindView(R.id.tviTOSSP_Nav_label)
    TextView tviTOSSP_Nav_label;
    @BindView(R.id.iviTOSCustomer_Nav_icon)
    ImageView iviTOSCustomer_Nav_icon;
    @BindView(R.id.tviTOSCustomer_Nav_label)
    TextView tviTOSCustomer_Nav_label;
    @BindView(R.id.iviTOSProduct_Nav_icon)
    ImageView iviTOSProduct_Nav_icon;
    @BindView(R.id.tviTOSProduct_Nav_label)
    TextView tviTOSProduct_Nav_label;
    @BindView(R.id.iviTOSInvoice_Nav_icon)
    ImageView iviTOSInvoice_Nav_icon;
    @BindView(R.id.tviTOSInvoice_Nav_label)
    TextView tviTOSInvoice_Nav_label;


    public void showTab(String level) {
        if (level.equals("R0") || level.equals("R1") || level.equals("R2") || level.equals("R3") || level.equals("R4"))
            llWSTabs.setVisibility(View.VISIBLE);
    }

    public void hideTab() {
        llWSTabs.setVisibility(View.GONE);
    }

    public void showOSOTab(String level) {
        if (level.equals("R0") || level.equals("R1") || level.equals("R2") || level.equals("R3") || level.equals("R4"))
            llOSOTabs.setVisibility(View.VISIBLE);
    }

    public void hideOSOTab() {
        llOSOTabs.setVisibility(View.GONE);
    }

    public void showTOSTab(String level) {
        if (level.equals("R1") || level.equals("R2") || level.equals("R3") || level.equals("R4"))
            llTOSTabs.setVisibility(View.VISIBLE);
    }

    public void hideTOSTab() {
        llTOSTabs.setVisibility(View.GONE);
    }

    @OnClick(R.id.llHome_Nav_tab)
    public void Home_Nav_tab() {
        HomeClick();
        clearStack();
    }

    @OnClick(R.id.llRSM_Nav_tab)
    public void RSMNavClick() {
        //rSMClick();
        Bundle rsmDataBundle = new Bundle();
        rsmDataBundle.putString(WSRSMFragment.USER_ID, userId);
        rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level);
        rsmDataBundle.putString(WSRSMFragment.FISCAL_YEAR, selectedFiscalYear);
        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.WS_RSM_FRAGMENT, rsmDataBundle);
    }

    @OnClick(R.id.llSP_Nav_tab)
    public void SPNavClick() {
        //sPClick();
        Bundle spDataBundle = new Bundle();
        spDataBundle.putString(WSSalesPersonFragment.USER_ID, userId);
        spDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);
        spDataBundle.putString(WSSalesPersonFragment.FISCAL_YEAR, selectedFiscalYear);
        spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.WS_ACCOUNT_FRAGMENT, spDataBundle);
    }

    @OnClick(R.id.llCustomer_Nav_tab)
    public void CustomerNavClick() {
        //customerClick();
        Bundle customerDataBundle = new Bundle();
        customerDataBundle.putString(WSCustomerFragment.USER_ID, userId);
        customerDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
        customerDataBundle.putString(WSCustomerFragment.FISCAL_YEAR, selectedFiscalYear);
        customerDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.WS_CUSTOMER_FRAGMENT, customerDataBundle);
    }

    @OnClick(R.id.llProduct_Nav_tab)
    public void ProductNavClick() {
        //productClick();
        Bundle productDataBundle = new Bundle();
        productDataBundle.putString(WSProductFragment.USER_ID, userId);
        productDataBundle.putString(WSProductFragment.USER_LEVEL, level);
        productDataBundle.putString(WSProductFragment.FISCAL_YEAR, selectedFiscalYear);
        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.WS_PRODUCT_FRAGMENT, productDataBundle);
    }

    private void HomeClick() {
        tviHome_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void rSMClick(String level) {
        tviHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviRSM_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void sPClick(String level) {
        if (level.equals("R2") || level.equals("R3")) {
            llRSM_Nav_tab.setVisibility(View.GONE);
        }
        tviHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviSP_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void customerClick(String level) {
        if (level.equals("R4")) {
            llRSM_Nav_tab.setVisibility(View.GONE);
            llSP_Nav_tab.setVisibility(View.GONE);
        }
        tviHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviCustomer_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void productClick(String level) {
        tviHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviProduct_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @OnClick(R.id.llOSOHome_Nav_tab)
    public void OSOHome_Nav_tab() {
        OSOHomeClick();
        clearStack();
    }

    @OnClick(R.id.llOSORSM_Nav_tab)
    public void OSORSMNavClick() {
        //rSMClick();
        Bundle rsmDataBundle = new Bundle();
        rsmDataBundle.putString(WSRSMFragment.USER_ID, userId);
        rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level);
        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.OSO_RSM_FRAGMENT, rsmDataBundle);
    }

    @OnClick(R.id.llOSOSP_Nav_tab)
    public void OSOSPNavClick() {
        //sPClick();
        Bundle spDataBundle = new Bundle();
        spDataBundle.putString(WSSalesPersonFragment.USER_ID, userId);
        spDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);
        spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.OSO_ACCOUNT_FRAGMENT, spDataBundle);
    }

    @OnClick(R.id.llOSOCustomer_Nav_tab)
    public void OSOCustomerNavClick() {
        //customerClick();
        Bundle customerDataBundle = new Bundle();
        customerDataBundle.putString(WSCustomerFragment.USER_ID, userId);
        customerDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
        customerDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.OSO_CUSTOMER_FRAGMENT, customerDataBundle);
    }

    @OnClick(R.id.llOSOInvoice_Nav_tab)
    public void OSOInvoiceNavClick() {
        //productClick();
        Bundle productDataBundle = new Bundle();
        productDataBundle.putString(WSProductFragment.USER_ID, userId);
        productDataBundle.putString(WSProductFragment.USER_LEVEL, level);
        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.OSO_INVOICE_FRAGMENT, productDataBundle);
    }

    @OnClick(R.id.llOSOProduct_Nav_tab)
    public void OSOProductNavClick() {
        //productClick();
        Bundle productDataBundle = new Bundle();
        productDataBundle.putString(WSProductFragment.USER_ID, userId);
        productDataBundle.putString(WSProductFragment.USER_LEVEL, level);
        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.OSO_PRODUCT_FRAGMENT, productDataBundle);
    }

    private void OSOHomeClick() {
        tviOSOHome_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviOSOHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSORSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSORSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void OSORSMClick(String level) {
        tviOSOHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSORSM_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviOSORSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void OSOSPClick(String level) {
        if (level.equals("R2") || level.equals("R3")) {
            llOSORSM_Nav_tab.setVisibility(View.GONE);
        }
        tviOSOHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSORSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSORSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOSP_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviOSOSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void OSOcustomerClick(String level) {
        if (level.equals("R4")) {
            llOSORSM_Nav_tab.setVisibility(View.GONE);
            llOSOSP_Nav_tab.setVisibility(View.GONE);
        }
        tviOSOHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSORSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSORSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOCustomer_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviOSOCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void OSOSOClick(String level) {
        tviOSOHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSORSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSORSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOInvoice_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviOSOInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void OSOProductClick(String level) {
        tviOSOHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSORSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSORSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviOSOInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviOSOProduct_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviOSOProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @OnClick(R.id.llTOSHome_Nav_tab)
    public void TOSHome_Nav_tab() {
        TOSHomeClick();
        clearStack();
    }

    @OnClick(R.id.llTOSRSM_Nav_tab)
    public void TOSRSMNavClick() {
        //rSMClick();
        Bundle rsmDataBundle = new Bundle();
        rsmDataBundle.putString(WSRSMFragment.USER_ID, userId);
        rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level);
        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.TOS_RSM_FRAGMENT, rsmDataBundle);
    }

    @OnClick(R.id.llTOSSP_Nav_tab)
    public void TOSSPNavClick() {
        //sPClick();
        Bundle spDataBundle = new Bundle();
        spDataBundle.putString(WSSalesPersonFragment.USER_ID, userId);
        spDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);
        spDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.TOS_ACCOUNT_FRAGMENT, spDataBundle);
    }

    @OnClick(R.id.llTOSCustomer_Nav_tab)
    public void TOSCustomerNavClick() {
        //customerClick();
        Bundle customerDataBundle = new Bundle();
        customerDataBundle.putString(WSCustomerFragment.USER_ID, userId);
        customerDataBundle.putString(WSCustomerFragment.USER_LEVEL, level);
        customerDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.TOS_CUSTOMER_FRAGMENT, customerDataBundle);
    }

    @OnClick(R.id.llTOSProduct_Nav_tab)
    public void TOSProductNavClick() {
        //productClick();
        Bundle productDataBundle = new Bundle();
        productDataBundle.putString(WSProductFragment.USER_ID, userId);
        productDataBundle.putString(WSProductFragment.USER_LEVEL, level);
        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.TOS_PRODUCT_FRAGMENT, productDataBundle);
    }

    @OnClick(R.id.llTOSInvoice_Nav_tab)
    public void TOSInvoiceNavClick() {
        //productClick();
        Bundle productDataBundle = new Bundle();
        productDataBundle.putString(WSProductFragment.USER_ID, userId);
        productDataBundle.putString(WSProductFragment.USER_LEVEL, level);
        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.TOS_INVOICE_FRAGMENT, productDataBundle);
    }

    private void TOSHomeClick() {
        tviTOSHome_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviTOSHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void TOSRSMClick(String level) {
        tviTOSHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSRSM_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviTOSRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void TOSSPClick(String level) {
        if (level.equals("R2") || level.equals("R3")) {
            llTOSRSM_Nav_tab.setVisibility(View.GONE);
        }
        tviTOSHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSSP_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviTOSSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void TOScustomerClick(String level) {
        if (level.equals("R4")) {
            llTOSRSM_Nav_tab.setVisibility(View.GONE);
            llTOSSP_Nav_tab.setVisibility(View.GONE);
        }
        tviTOSHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSCustomer_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviTOSCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void TOSproductClick(String level) {
        tviTOSHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSProduct_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviTOSProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSInvoice_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void TOSInvoiceClick(String level) {
        tviTOSHome_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSHomeNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSProduct_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviTOSProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviTOSInvoice_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviTOSInvoice_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void clearStack() {
        getSessionData();
        int count = fragmentManager.getBackStackEntryCount();
        while (count > 2) {
            fragmentManager.popBackStack();
            count--;
        }
    }

}
