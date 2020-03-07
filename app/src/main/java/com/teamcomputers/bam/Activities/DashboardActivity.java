package com.teamcomputers.bam.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import com.teamcomputers.bam.Fragments.FeedbackFragment;
import com.teamcomputers.bam.Fragments.Installation.InstallationFragment;
import com.teamcomputers.bam.Fragments.Logistics.LogisticsFragment;
import com.teamcomputers.bam.Fragments.NewSalesReceivable.NewRSMTabFragment;
import com.teamcomputers.bam.Fragments.NewSalesReceivable.NewSalesReceivableFragment;
import com.teamcomputers.bam.Fragments.OrderProcessing.OrderProcessingFragment;
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
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.CircularImageView;
import com.teamcomputers.bam.Utils.WrapContentLinearLayoutManager;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class DashboardActivity extends BaseActivity {
    public static final String IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED = "IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED";
    @BindView(R.id.llWSTabs)
    LinearLayout llWSTabs;
    @BindView(R.id.claMain)
    CoordinatorLayout claMain;
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

    public String userId = "", level = "";

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
                        replaceFragment(Fragments.ORDERPROCESSING_FRAGMENTS, new Bundle());
                        break;
                    case Events.PURCHASE:
                        replaceFragment(Fragments.PURCHASE_FRAGMENTS, new Bundle());
                        break;
                    case Events.LOGISTICS:
                        replaceFragment(Fragments.LOGISTICS_FRAGMENTS, new Bundle());
                        break;
                    case Events.INSTALLATION:
                        replaceFragment(Fragments.INSTALLATION_FRAGMENTS, new Bundle());
                        break;
                    case Events.COLLECTION:
                        replaceFragment(Fragments.COLLECTION_FRAGMENTS, new Bundle());
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
        if (SharedPreferencesController.getInstance(dashboardActivityContext).isUserLoggedIn()) {
            dashboardActivityContext = this;
            toolbar = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            //NavigationView navigationView = findViewById(R.id.nav_view);

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
                ;
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
        return true;
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

    @OnClick(R.id.ll_favourits)
    public void favourits() {
        replaceFragment(Fragments.SETTINGS_FRAGMENTS, new Bundle());
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                claMain.setBackground(getResources().getDrawable(R.drawable.gradient_drawable));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                claMain.setBackground(getResources().getDrawable(R.drawable.gradient_login_bg));
            }
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
        tv_date.setText(String.valueOf(refreshDate));
    }

    private void setNavigationDrawerData() {
        List<NavigationItem> navigationOrderProcessing = new ArrayList<>();
        navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP1)), Fragments.ORDERPROCESSING_FRAGMENTS1));
        navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP2)), Fragments.ORDERPROCESSING_FRAGMENTS2));
        navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP3)), Fragments.ORDERPROCESSING_FRAGMENTS3));
        navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP4)), Fragments.ORDERPROCESSING_FRAGMENTS4));

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
        navigationOrderProcessingParent.setNavImageParent(R.drawable.ic_menu_order_processing);
        navigationOrderProcessingParent.setNavTitleParent(getString(R.string.OrderProcessing));
        navigationOrderProcessingParent.setNavigationItems(navigationOrderProcessing);

        NavigationItemParentModel navigationPurchaseParent = new NavigationItemParentModel();
        navigationPurchaseParent.setNavImageParent(R.drawable.ic_menu_purchase);
        navigationPurchaseParent.setNavTitleParent(getString(R.string.Purchanse));
        navigationPurchaseParent.setNavigationItems(navigationPurchase);

        NavigationItemParentModel navigationLogisticsParent = new NavigationItemParentModel();
        navigationLogisticsParent.setNavImageParent(R.drawable.ic_menu_logistic);
        navigationLogisticsParent.setNavTitleParent(getString(R.string.Logistics));
        navigationLogisticsParent.setNavigationItems(navigationLogistics);

        NavigationItemParentModel navigationInstallationParent = new NavigationItemParentModel();
        navigationInstallationParent.setNavImageParent(R.drawable.ic_menu_installation);
        navigationInstallationParent.setNavTitleParent(getString(R.string.Installation));
        navigationInstallationParent.setNavigationItems(navigationInstallation);

        NavigationItemParentModel navigationCollectionParent = new NavigationItemParentModel();
        navigationCollectionParent.setNavImageParent(R.drawable.ic_menu_collection);
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
        Intent intent = new Intent(DashboardActivity.this, SplashActivity.class);
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
                        if (!userProfile.getSBU().equals("WS")) {
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS1:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(0);
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS2:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(1);
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS3:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(2);
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS4:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(3);
                            fragment = new OrderProcessingFragment();
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS:
                        if (!userProfile.getSBU().equals("WS")) {
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS1:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(0);
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS2:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(1);
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS3:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(2);
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.PURCHASE_FRAGMENTS4:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(3);
                            //fragment = new PurchaseFragments();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS:
                        if (!userProfile.getSBU().equals("WS")) {
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS1:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(0);
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS2:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(1);
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS3:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(2);
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS4:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(3);
                            fragment = new LogisticsFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS:
                        if (!userProfile.getSBU().equals("WS")) {
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS1:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(0);
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS2:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(1);
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS3:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(2);
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS4:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(3);
                            fragment = new InstallationFragment();
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS:
                        if (!userProfile.getSBU().equals("WS")) {
                            //fragment = new CollectionFragment();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS1:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(0);
                            //fragment = new CollectionFragment();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS2:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(1);
                            //fragment = new CollectionFragment();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS3:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(2);
                            //fragment = new CollectionFragment();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.COLLECTION_FRAGMENTS4:
                        if (!userProfile.getSBU().equals("WS")) {
                            SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(3);
                            //fragment = new CollectionFragment();
                            showToast(ToastTexts.WORK_PROGRESS);
                        }
                        break;
                    case Fragments.OTHERS_FRAGMENTS:
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.SR_FRAGMENTS:
                        //fragment = new SalesReceivableFragment();
                        fragment = new NewSalesReceivableFragment();
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
                    case Fragments.WS_RSM_FRAGMENT:
                        fragment = new WSRSMFragment();
                        break;
                    case Fragments.WS_ACCOUNT_FRAGMENT:
                        fragment = new WSSalesPersonFragment();
                        break;
                    case Fragments.WS_CUSTOMER_FRAGMENT:
                        fragment = new WSCustomerFragment();
                        break;
                    case Fragments.WS_PRODUCT_FRAGMENT:
                        fragment = new WSProductFragment();
                        break;
                    default:
                        fragment = new HomeFragment();
                        break;
                }
                fragment.setArguments(bundle);
                try {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                    fragmentTransaction.replace(R.id.dash_board_content, fragment, fragment.getFragmentName());
                    int backStackEntryCount = fragmentManager.getBackStackEntryCount();
                    if (bundle.getBoolean(IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED)) {
                        if (backStackEntryCount < 6) {
                            fragmentTransaction.addToBackStack(fragment.getFragmentName());
                        }
                    } else {
                        if (backStackEntryCount < 2) {
                            fragmentTransaction.addToBackStack(fragment.getFragmentName());
                        }
                    }
                    fragmentTransaction.commitAllowingStateLoss();
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
        if (fragment.getFragmentName().equals("HomeFragment")) {
            finish();
        }
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.left_to_right_order, R.anim.right_to_left_order);
        //fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
        //        R.anim.slide_out_right, R.anim.slide_in_right);
        /*else if (fragment.getFragmentName().equals("OrderProcessingFragment")) {
            finish();
        } else if (fragment.getFragmentName().equals("LogisticsFragment")) {
            finish();
        } else if (fragment.getFragmentName().equals("HomeFragment")) {
            finish();
        } else if (fragment.getFragmentName().equals("HomeFragment")) {
            finish();
        }*/
    }

    private void getScreen() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss", Locale.getDefault());
        String refreshDate = sdf.format(now);
        View v = fragment.getView();
        //View v = dashboardActivityContext.getWindow().getCurrentFocus();
        //View v = findViewById(android.R.id.content).getRootView();
        if (null != v) {
            v.setDrawingCacheEnabled(true);
            Bitmap b = v.getDrawingCache();
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

    public void showTab(String level) {
        if (level.equals("R1") || level.equals("R2") || level.equals("R4"))
            llWSTabs.setVisibility(View.VISIBLE);
    }

    public void hideTab() {
        llWSTabs.setVisibility(View.GONE);
    }

    @OnClick(R.id.llRSM_Nav_tab)
    public void RSMNavClick() {
        //rSMClick();
        Bundle rsmDataBundle = new Bundle();
        rsmDataBundle.putString(WSRSMFragment.USER_ID, userId);
        rsmDataBundle.putString(WSRSMFragment.USER_LEVEL, level);
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
        productDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
        //dashboardActivityContext.replaceFragment(Fragments.RSM_ANALYSIS_FRAGMENT, rsmDataBundle);
        dashboardActivityContext.replaceFragment(Fragments.WS_PRODUCT_FRAGMENT, productDataBundle);
    }

    public void rSMClick(String level) {
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
        tviRSM_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviRSMNav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviSP_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviSP_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviCustomer_Nav_label.setTextColor(getResources().getColor(R.color.color_value_54));
        iviCustomer_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.color_value_54), android.graphics.PorterDuff.Mode.SRC_IN);

        tviProduct_Nav_label.setTextColor(getResources().getColor(R.color.end_header_color_bg));
        iviProduct_Nav_icon.setColorFilter(ContextCompat.getColor(dashboardActivityContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
    }

}
