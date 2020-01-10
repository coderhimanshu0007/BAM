package com.teamcomputers.bam.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.teamcomputers.bam.Fragments.EditProfileFragment;
import com.teamcomputers.bam.Fragments.FeedbackFragment;
import com.teamcomputers.bam.Fragments.Installation.InstallationFragment;
import com.teamcomputers.bam.Fragments.Logistics.LogisticsFragment;
import com.teamcomputers.bam.Fragments.OrderProcessing.OrderProcessingFragment;
import com.teamcomputers.bam.Fragments.WS.WsFragment;
import com.teamcomputers.bam.Fragments.home.HomeFragment;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.CircularImageView;
import com.teamcomputers.bam.Utils.WrapContentLinearLayoutManager;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.teamcomputers.bam.Activities.MainActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED;

public class DashboardActivity extends BaseActivity {

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
                        replaceFragment(Fragments.WS_FRAGMENTS, new Bundle());
                        break;
                    case Events.OTHERS:
                        replaceFragment(Fragments.OTHERS_FRAGMENTS, new Bundle());
                        break;
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
        //MenuItem action_refresh = menu.findItem(R.id.action_refresh);
        return true;
    }

    @OnClick(R.id.ll_home)
    public void home() {
        replaceFragment(Fragments.HOME_FRAGMENTS, new Bundle());
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
        if (title.equals("KOCKPIT")) {
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

        List<NavigationItem> navigationWS = new ArrayList<>();
        navigationWS.add(new NavigationItem(new SpannableStringBuilder("-" + getString(R.string.WS1)), Fragments.WS_FRAGMENTS));

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
        navigationWSParent.setNavTitleParent(getString(R.string.WS));
        navigationWSParent.setNavigationItems(navigationWS);

        /*if (SharedPreferencesController.getInstance(TeamWorksApplication.getInstance()).getUserProfile().getIsHead().equals("1")) {
            navigationItemParentModels.add(navigationItemParentMyTeam);
        }*/
        navigationItemParentModels.add(navigationOrderProcessingParent);
        navigationItemParentModels.add(navigationPurchaseParent);
        navigationItemParentModels.add(navigationLogisticsParent);
        navigationItemParentModels.add(navigationInstallationParent);
        navigationItemParentModels.add(navigationCollectionParent);
        navigationItemParentModels.add(navigationWSParent);
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

        drawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager = getSupportFragmentManager();
                toggleToolBarVisibility(true);
//                    dashBoardActivityContext.toggleToolBarVisibility(true);
                switch (fragmentToBePut) {
                    case Fragments.EDIT_PROFILE_FRAGMENTS:
                        //fragment = new EditProfileFragment();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.HOME_FRAGMENTS:
                        fragment = new HomeFragment();
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS:
                        fragment = new OrderProcessingFragment();
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS1:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(0);
                        fragment = new OrderProcessingFragment();
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS2:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(1);
                        fragment = new OrderProcessingFragment();
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS3:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(2);
                        fragment = new OrderProcessingFragment();
                        break;
                    case Fragments.ORDERPROCESSING_FRAGMENTS4:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setOPPageNo(3);
                        fragment = new OrderProcessingFragment();
                        break;
                    case Fragments.PURCHASE_FRAGMENTS:
                        //fragment = new PurchaseFragments();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.PURCHASE_FRAGMENTS1:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(0);
                        //fragment = new PurchaseFragments();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.PURCHASE_FRAGMENTS2:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(1);
                        //fragment = new PurchaseFragments();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.PURCHASE_FRAGMENTS3:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(2);
                        //fragment = new PurchaseFragments();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.PURCHASE_FRAGMENTS4:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setPurchasePageNo(3);
                        //fragment = new PurchaseFragments();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS:
                        fragment = new LogisticsFragment();
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS1:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(0);
                        fragment = new LogisticsFragment();
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS2:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(1);
                        fragment = new LogisticsFragment();
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS3:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(2);
                        fragment = new LogisticsFragment();
                        break;
                    case Fragments.LOGISTICS_FRAGMENTS4:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setLogisticPageNo(3);
                        fragment = new LogisticsFragment();
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS:
                        fragment = new InstallationFragment();
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS1:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(0);
                        fragment = new InstallationFragment();
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS2:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(1);
                        fragment = new InstallationFragment();
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS3:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(2);
                        fragment = new InstallationFragment();
                        break;
                    case Fragments.INSTALLATION_FRAGMENTS4:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setInstallationPageNo(3);
                        fragment = new InstallationFragment();
                        break;
                    case Fragments.COLLECTION_FRAGMENTS:
                        //fragment = new CollectionFragment();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.COLLECTION_FRAGMENTS1:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(0);
                        //fragment = new CollectionFragment();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.COLLECTION_FRAGMENTS2:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(1);
                        //fragment = new CollectionFragment();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.COLLECTION_FRAGMENTS3:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(2);
                        //fragment = new CollectionFragment();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.COLLECTION_FRAGMENTS4:
                        SharedPreferencesController.getInstance(dashboardActivityContext).setCollectionPageNo(3);
                        //fragment = new CollectionFragment();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.OTHERS_FRAGMENTS:
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.WS_FRAGMENTS:
                        fragment = new WsFragment();
                        showToast(ToastTexts.WORK_PROGRESS);
                        break;
                    case Fragments.FEEDBACK_FRAGMENTS:
                        fragment = new FeedbackFragment();
                        break;
                    default:
                        fragment = new HomeFragment();
                        break;
                }
                fragment.setArguments(bundle);
                try {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.dash_board_content, fragment, fragment.getFragmentName());
                    int backStackEntryCount = fragmentManager.getBackStackEntryCount();
                    if (bundle.getBoolean(IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED)) {
                        if (backStackEntryCount < 3) {
                            fragmentTransaction.addToBackStack(fragment.getFragmentName());
                        }
                    } else {
                        if (backStackEntryCount < 2) {
                            fragmentTransaction.addToBackStack(fragment.getFragmentName());
                        }
                    }
                    fragmentTransaction.commit();
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
        } /*else if (fragment.getFragmentName().equals("OrderProcessingFragment")) {
            finish();
        } else if (fragment.getFragmentName().equals("LogisticsFragment")) {
            finish();
        } else if (fragment.getFragmentName().equals("HomeFragment")) {
            finish();
        } else if (fragment.getFragmentName().equals("HomeFragment")) {
            finish();
        }*/
    }
}
