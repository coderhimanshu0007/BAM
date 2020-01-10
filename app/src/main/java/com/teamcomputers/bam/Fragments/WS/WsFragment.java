package com.teamcomputers.bam.Fragments.WS;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.ExpandableRecyclerview.WSModels.NavigationItemParent1Model;
import com.teamcomputers.bam.ExpandableRecyclerview.WSModels.NavigationItemParent2Model;
import com.teamcomputers.bam.ExpandableRecyclerview.WSModels.NavigationItemParent3Model;
import com.teamcomputers.bam.ExpandableRecyclerview.expandables.NavigationExpandable;
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItem;
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItemParentModel;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.home.HomeViewModel;
import com.teamcomputers.bam.Models.LoginModel;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.RatingBars.FacesRatingBar;
import com.teamcomputers.bam.Utils.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class WsFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    String toolbarTitle = "";
    @BindView(R.id.rviWS)
    RecyclerView rviWS;
    private NavigationExpandable navigationExpandable;
    List<NavigationItemParentModel> navigationItemParentModels = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ws, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        dashboardActivityContext = (DashboardActivity) context;
        toolbarTitle = "WS";
        dashboardActivityContext.setToolBarTitle(toolbarTitle);
        //reloadNavigationDrawer();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void reloadNavigationDrawer() {
        navigationItemParentModels.clear();
        setNavigationDrawerData();
        rviWS.setLayoutManager(new WrapContentLinearLayoutManager(dashboardActivityContext));
        navigationExpandable = new NavigationExpandable(navigationItemParentModels, dashboardActivityContext);
        rviWS.setItemAnimator(new DefaultItemAnimator());
        rviWS.setAdapter(navigationExpandable);
    }

    private void setNavigationDrawerData() {
        List<NavigationItem> navigationOrderProcessing = new ArrayList<>();
        navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP1)), Fragments.NONE));
        navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP2)), Fragments.NONE));
        navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP3)), Fragments.NONE));
        navigationOrderProcessing.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.OP4)), Fragments.NONE));

        List<NavigationItem> navigationPurchase = new ArrayList<>();
        navigationPurchase.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Purchanse1)), Fragments.NONE));
        navigationPurchase.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Purchanse2)), Fragments.NONE));
        navigationPurchase.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Purchanse3)), Fragments.NONE));
        navigationPurchase.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Purchanse4)), Fragments.NONE));

        List<NavigationItem> navigationLogistics = new ArrayList<>();
        navigationLogistics.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Logistics1)), Fragments.NONE));
        navigationLogistics.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Logistics2)), Fragments.NONE));
        navigationLogistics.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Logistics3)), Fragments.NONE));
        navigationLogistics.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Logistics4)), Fragments.NONE));

        List<NavigationItem> navigationInstallation = new ArrayList<>();
        navigationInstallation.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Installation1)), Fragments.NONE));
        navigationInstallation.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Installation2)), Fragments.NONE));
        navigationInstallation.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Installation3)), Fragments.NONE));
        navigationInstallation.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Installation4)), Fragments.NONE));

        List<NavigationItem> navigationCollection = new ArrayList<>();
        navigationCollection.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Collection1)), Fragments.NONE));
        navigationCollection.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Collection2)), Fragments.NONE));
        navigationCollection.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Collection3)), Fragments.NONE));
        navigationCollection.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Collection4)), Fragments.NONE));

        List<NavigationItem> navigationOthers = new ArrayList<>();
        navigationOthers.add(new NavigationItem(new SpannableStringBuilder("-" + getString(R.string.Others)), Fragments.NONE));
        //navigationOthers.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Others2)), Fragments.OTHERS_FRAGMENTS));
        //navigationOthers.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Others3)), Fragments.OTHERS_FRAGMENTS));
        //navigationOthers.add(new NavigationItem(new SpannableStringBuilder(getString(R.string.Others4)), Fragments.OTHERS_FRAGMENTS));

        List<NavigationItem> navigationWS = new ArrayList<>();
        navigationWS.add(new NavigationItem(new SpannableStringBuilder("-" + getString(R.string.WS1)), Fragments.NONE));

        NavigationItemParent3Model navigationParent3 = new NavigationItemParent3Model();
        navigationParent3.setNavImageParent(R.drawable.ic_menu_logistic);
        navigationParent3.setNavTitleParent(getString(R.string.Logistics));
        navigationParent3.setNavigationItems(navigationLogistics);

        NavigationItemParent2Model navigationParent2 = new NavigationItemParent2Model();
        navigationParent2.setNavImageParent(R.drawable.ic_menu_purchase);
        navigationParent2.setNavTitleParent(getString(R.string.Purchanse));
        navigationParent2.setNavigationItems((List<NavigationItemParent3Model>) navigationParent3);

        NavigationItemParent1Model navigationOrderProcessing1Parent = new NavigationItemParent1Model();
        navigationOrderProcessing1Parent.setNavImageParent(R.drawable.ic_menu_order_processing);
        navigationOrderProcessing1Parent.setNavTitleParent(getString(R.string.OrderProcessing));
        navigationOrderProcessing1Parent.setNavigationItems((List<NavigationItemParent2Model>) navigationParent2);





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
        /*navigationItemParentModels.add(navigationOrderProcessingParent);
        navigationItemParentModels.add(navigationPurchaseParent);
        navigationItemParentModels.add(navigationLogisticsParent);*/
        navigationItemParentModels.add(navigationInstallationParent);
        navigationItemParentModels.add(navigationCollectionParent);
        navigationItemParentModels.add(navigationWSParent);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_refresh);
        item.setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getFragmentName() {
        return WsFragment.class.getSimpleName();
    }

}