package com.teamcomputers.bam.Fragments.SalesReceivableR2;

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
import com.teamcomputers.bam.Fragments.NewSalesReceivable.NewRSMTabFragment;
import com.teamcomputers.bam.Fragments.SalesReceivable.AccountsFragment;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.NewSalesReceivable.SalesPersonListRequester;
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

public class SalesPersonR2Fragment extends BaseFragment {
    public static final String USER_ID = "USER_ID";
    public static final String RSM_PROFILE = "RSM_PROFILE";
    public static final String RSM_POSITION = "RSM_POSITION";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    FullSalesModel fullSalesModel;

    String userId = "";
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

    List<FullSalesModel> rsmDataList = new ArrayList<>();
    List<FullSalesModel> spDataList = new ArrayList<>();

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
        //toolbarTitle = getString(R.string.Sales);
        //dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        //showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new FullSalesListRequester(fullSalesModel.getTMC(), "R2", "Sales", "", ""));
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new SalesPersonListRequester(userId, "R2", "Sales", "", ""));
        /*if (((NewRSMTabFragment) getParentFragment()).title.equals("RSM")) {
            BackgroundExecutor.getInstance().execute(new FullSalesListRequester(userId, "R1", "RSM", "", ""));
        } else if (((NewRSMTabFragment) getParentFragment()).title.equals("Sales Person")) {
            BackgroundExecutor.getInstance().execute(new SalesPersonListRequester(userId, "R1", "Sales", "", ""));
        }*/
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
                    /*case Events.GET_FULL_SALES_LIST_SUCCESSFULL:
                        dismissProgress();
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            rsmDataList = new ArrayList<FullSalesModel>(Arrays.asList(data));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //initTreeData(model);
                        initRSMData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_FULL_SALES_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;*/
                    /*case ClickEvents.RSM_CLICK:
                        dashboardActivityContext.setToolBarTitle(getString(R.string.Sales));
                        ((NewRSMTabFragment) getParentFragment()).title = getString(R.string.Sales);
                        cviRSMHeading.setVisibility(View.VISIBLE);
                        pos = (int) eventObject.getObject();
                        if (pos == 0) {
                            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
                        } else if (pos == 1) {
                            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
                        } else if (pos == 2) {
                            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
                        } else if (pos % 2 == 0) {
                            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
                        } else if (pos % 2 == 1) {
                            llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
                        }
                        tviName.setText(rsmDataList.get(pos).getName());
                        tviTarget.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getYTDTarget()));
                        tviActual.setText(BAMUtil.getRoundOffValue(rsmDataList.get(pos).getYTD()));
                        tviAch.setText(rsmDataList.get(pos).getYTDPercentage().intValue() + "%");
                        type = 1;
                        showProgress(ProgressDialogTexts.LOADING);
                        BackgroundExecutor.getInstance().execute(new SalesPersonListRequester(rsmDataList.get(pos).getTMC(), "R2", "Sales", "", ""));
                        break;*/
                    case Events.GET_SALES_PERSON_LIST_SUCCESSFULL:
                        dismissProgress();
                        dashboardActivityContext.setToolBarTitle(getString(R.string.Sales));
                        //((NewRSMTabFragment) getParentFragment()).title = getString(R.string.Sales);
                        /*fullSalesModel = ((NewRSMTabFragment) getParentFragment()).salesPersonDataList;
                        if (null != fullSalesModel) {
                            cviRSMHeading.setVisibility(View.VISIBLE);
                            pos = fullSalesModel.getPosition();
                            if (pos == 0) {
                                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_first_item_value));
                            } else if (pos == 1) {
                                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_second_item_value));
                            } else if (pos == 2) {
                                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_third_item_value));
                            } else if (pos % 2 == 0) {
                                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
                            } else if (pos % 2 == 1) {
                                llRSMLayout.setBackgroundColor(getResources().getColor(R.color.login_bg));
                            }
                            tviName.setText(fullSalesModel.getName());
                            tviTarget.setText(BAMUtil.getRoundOffValue(fullSalesModel.getYTDTarget()));
                            tviActual.setText(BAMUtil.getRoundOffValue(fullSalesModel.getYTD()));
                            tviAch.setText(fullSalesModel.getYTDPercentage().intValue() + "%");
                        }*/
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            FullSalesModel[] data = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            spDataList = new ArrayList<FullSalesModel>(Arrays.asList(data));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //initTreeData(model);
                        initData("YTD");
                        dismissProgress();
                        break;
                    case Events.GET_SALES_PERSON_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
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
        if (null != fullSalesModel) {
            tviTarget.setText(BAMUtil.getRoundOffValue(fullSalesModel.getYTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(fullSalesModel.getYTD()));
            tviAch.setText(fullSalesModel.getYTDPercentage().intValue() + "%");
        }
        initData("YTD");
        adapter.notifyDataSetChanged();
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
        if (null != fullSalesModel) {
            tviTarget.setText(BAMUtil.getRoundOffValue(fullSalesModel.getQTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(fullSalesModel.getQTD()));
            tviAch.setText(fullSalesModel.getQTDPercentage().intValue() + "%");
        }
        initData("QTD");
        adapter.notifyDataSetChanged();
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
        if (null != fullSalesModel) {
            tviTarget.setText(BAMUtil.getRoundOffValue(fullSalesModel.getMTDTarget()));
            tviActual.setText(BAMUtil.getRoundOffValue(fullSalesModel.getMTD()));
            tviAch.setText(fullSalesModel.getMTDPercentage().intValue() + "%");
        }
        initData("MTD");
        adapter.notifyDataSetChanged();
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

    @OnClick(R.id.iviR1Close)
    public void RSMClose() {
        ((NewRSMTabFragment) getParentFragment()).salesPersonDataList = null;
        cviRSMHeading.setVisibility(View.GONE);
        type = 1;
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new SalesPersonListRequester(userId, "R1", "Sales", "", ""));
        //BackgroundExecutor.getInstance().execute(new FullSalesListRequester(userId, "R1", "Sales", "", ""));
    }

    private void initRSMData(String type) {
        rsmAdapter = new NewRSMAdapter(dashboardActivityContext, type, "", rsmDataList, false, false, false);
        rviRSM.setAdapter(rsmAdapter);
    }

    private void initData(String type) {
        adapter = new SalesPersonAdapter(dashboardActivityContext, type, spDataList);
        rviRSM.setAdapter(adapter);
    }
}
