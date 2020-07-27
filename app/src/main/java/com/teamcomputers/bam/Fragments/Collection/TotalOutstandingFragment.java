package com.teamcomputers.bam.Fragments.Collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.Collection.KCollectionTotalOutstandingAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.Collection.TotalOutstandingModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Collection.KCollectionOutstandingCurrentMonthRequester;
import com.teamcomputers.bam.Requesters.Collection.KCollectionOutstandingSubsequentMonthRequester;
import com.teamcomputers.bam.Requesters.Collection.KCollectionTotalOutstandingRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.KBAMUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TotalOutstandingFragment extends BaseFragment {
    private View rootView;
    public static final String FROM = "FROM";
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String from = "";

    @BindView(R.id.rviData)
    RecyclerView rviData;

    @BindView(R.id.tviTOInvoice)
    TextView tviTOInvoice;
    @BindView(R.id.tviTOAmount)
    TextView tviTOAmount;

    private KCollectionTotalOutstandingAdapter mAdapter;
    TotalOutstandingModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_total_outstanding, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        from = getArguments().getString(FROM);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        showProgress(ProgressDialogTexts.LOADING);
        if (from.equals("TOTALOUTSTANDING")) {
            BackgroundExecutor.getInstance().execute(new KCollectionTotalOutstandingRequester("0", "100"));
        } else if (from.equals("COLLECTIBLEOUTSTANDING")) {
            BackgroundExecutor.getInstance().execute(new KCollectionTotalOutstandingRequester("0", "100"));
        } else if (from.equals("COCM")) {
            BackgroundExecutor.getInstance().execute(new KCollectionOutstandingCurrentMonthRequester("0", "100"));
        } else if (from.equals("COSM")) {
            BackgroundExecutor.getInstance().execute(new KCollectionOutstandingSubsequentMonthRequester("0", "100"));
        }
        return rootView;
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
        return TotalOutstandingFragment.class.getSimpleName();
    }

    @Subscribe
    public void onEvent(final EventObject eventObject) {
        dashboardActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case Events.NO_INTERNET_CONNECTION:
                        dismissProgress();
                        break;
                    case Events.GET_COLLECTION_TOTAL_OUTSTANDING_SUCCESSFULL:
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceTotalOutstandingDataResponse(eventObject.getObject().toString()));
                            model = (TotalOutstandingModel) BAMUtil.fromJson(String.valueOf(jsonObject), TotalOutstandingModel.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        tviTOInvoice.setText(model.getInvoice().toString());
                        tviTOAmount.setText(KBAMUtils.getRoundOffValue(model.getAmount()));

                        mAdapter = new KCollectionTotalOutstandingAdapter(dashboardActivityContext, model.getTable());
                        rviData.setAdapter(mAdapter);
                        //init();
                        //chart.setData(generatePieData());
                        //chart.notifyDataSetChanged();
                        //chart.invalidate();
                        break;
                    case Events.GET_COLLECTION_TOTAL_OUTSTANDING_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.OOPS_MESSAGE:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.INTERNAL_SERVER_ERROR:
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

}
