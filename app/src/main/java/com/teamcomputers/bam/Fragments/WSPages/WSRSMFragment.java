package com.teamcomputers.bam.Fragments.WSPages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.SalesOutstanding.NewRSMAdapter;
import com.teamcomputers.bam.Adapters.SalesOutstanding.SalesPersonAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.FilterSalesListRequester;
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
import butterknife.Unbinder;

public class WSRSMFragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String USER_LEVEL = "USER_LEVEL";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String RSM_POSITION = "RSM_POSITION";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String toolbarTitle = "";

    String userId = "", level="";
    @BindView(R.id.llRSMLayout)
    LinearLayout llRSMLayout;
    @BindView(R.id.cviRSMHeading)
    CardView cviRSMHeading;
    @BindView(R.id.tviName)
    TextView tviName;
    @BindView(R.id.tviTarget)
    TextView tviTarget;
    @BindView(R.id.tviActual)
    TextView tviActual;
    @BindView(R.id.tviAch)
    TextView tviAch;
    @BindView(R.id.viYTD)
    View viYTD;
    @BindView(R.id.viQTD)
    View viQTD;
    @BindView(R.id.viMTD)
    View viMTD;
    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private SalesPersonAdapter adapter;
    private NewRSMAdapter rsmAdapter;
    private int type = 0, pos = 0;

    FullSalesModel spData;
    List<FullSalesModel> rsmDataList = new ArrayList<>();
    ArrayList<FullSalesModel> spDataList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sales_person, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        userId = getArguments().getString(USER_ID);
        level = getArguments().getString(USER_LEVEL);
        //fullSalesModel = getArguments().getParcelable(RSM_PROFILE);
        /*position = getArguments().getInt(RSM_POSITION);

        if (position == 0) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
        } else if (position == 1) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
        } else if (position == 2) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
        } else if (position % 2 == 0) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
        } else if (position % 2 == 1) {
            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
        }
        tviName.setText(fullSalesModel.getName());
        tviYtd.setText(BAMUtil.getRoundOffValue(fullSalesModel.getYTD()));
        tviQtd.setText(BAMUtil.getRoundOffValue(fullSalesModel.getQTD()));
        tviMtd.setText(BAMUtil.getRoundOffValue(fullSalesModel.getMTD()));
*/
        toolbarTitle = getString(R.string.Heading_RSM);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FilterSalesListRequester(userId, level, "RSM", "", "", "", "", ""));
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
        dashboardActivityContext.showTab(level);
        dashboardActivityContext.rSMClick(level);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return AccountsFragment.class.getSimpleName();
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
                    case Events.GET_FILTER_SALES_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            rsmDataList = new ArrayList<FullSalesModel>(Arrays.asList(data));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initRSMData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_FILTER_SALES_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_CLICK:
                        spData = (FullSalesModel) eventObject.getObject();
                        Bundle rsmDataBundle = new Bundle();
                        rsmDataBundle.putString(WSSalesPersonFragment.USER_ID, userId);
                        rsmDataBundle.putString(WSSalesPersonFragment.USER_LEVEL, level);
                        rsmDataBundle.putParcelable(WSSalesPersonFragment.RSM_PROFILE, spData);
                        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.WS_ACCOUNT_FRAGMENT, rsmDataBundle);
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

    @OnClick(R.id.tviYTD)
    public void tviYTD() {
        viYTD.setVisibility(View.VISIBLE);
        viQTD.setVisibility(View.INVISIBLE);
        viMTD.setVisibility(View.INVISIBLE);
        initRSMData("YTD");
        rsmAdapter.notifyDataSetChanged();
        /*if (type == 0) {
            initRSMData("YTD");
            rsmAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getYTD()));
            tviAch.setText(rsmDataList.get(pos).getYTDPercentage().intValue() + "%");
            initData("YTD");
            adapter.notifyDataSetChanged();
        }*/
    }

    @OnClick(R.id.tviQTD)
    public void tviQTD() {
        viYTD.setVisibility(View.INVISIBLE);
        viQTD.setVisibility(View.VISIBLE);
        viMTD.setVisibility(View.INVISIBLE);
        initRSMData("QTD");
        rsmAdapter.notifyDataSetChanged();
        /*if (type == 0) {
            initRSMData("QTD");
            rsmAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getQTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getQTD()));
            tviAch.setText(rsmDataList.get(pos).getQTDPercentage().intValue() + "%");
            initData("QTD");
            adapter.notifyDataSetChanged();
        }*/
    }

    @OnClick(R.id.tviMTD)
    public void tviMTD() {
        viYTD.setVisibility(View.INVISIBLE);
        viQTD.setVisibility(View.INVISIBLE);
        viMTD.setVisibility(View.VISIBLE);
        initRSMData("MTD");
        rsmAdapter.notifyDataSetChanged();
        /*if (type == 0) {
            initRSMData("MTD");
            rsmAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            tviTarget.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getMTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getMTD()));
            tviAch.setText(rsmDataList.get(pos).getMTDPercentage().intValue() + "%");
            initData("MTD");
            adapter.notifyDataSetChanged();
        }*/
    }

    @OnClick(R.id.iviRSMClose)
    public void RSMClose() {
        cviRSMHeading.setVisibility(View.GONE);
        type = 1;
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FilterSalesListRequester(userId, level, "RSM", "", "", "", "", ""));
    }

    private void initRSMData(String type) {
        rsmAdapter = new NewRSMAdapter(dashboardActivityContext, type, rsmDataList);
        rviRSM.setAdapter(rsmAdapter);
    }

}
