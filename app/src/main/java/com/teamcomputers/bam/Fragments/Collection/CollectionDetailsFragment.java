package com.teamcomputers.bam.Fragments.Collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.Collection.KCollectionCollectionDetailsAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.Collection.CollectionCollectionDetailModel;
import com.teamcomputers.bam.Models.Collection.CollectionCollectionModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Collection.KCollectionECMRequester;
import com.teamcomputers.bam.Requesters.Collection.KCollectionECWRequester;
import com.teamcomputers.bam.Requesters.Collection.KCollectionPCMRequester;
import com.teamcomputers.bam.Requesters.Collection.KCollectionPCWRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.KBAMUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CollectionDetailsFragment extends BaseFragment {
    private View rootView;
    public static final String FROM = "FROM";
    public static final String COLLECTIONDATA = "COLLECTIONDATA";
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String from = "";
    boolean isLoading = false;
    int nextLimit = 0;

    @BindView(R.id.rlSearch)
    RelativeLayout rlSearch;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    @BindView(R.id.tviWIPDetailHeading)
    TextView tviWIPDetailHeading;

    @BindView(R.id.tviTOInvoice)
    TextView tviTOInvoice;
    @BindView(R.id.tviTOAmount)
    TextView tviTOAmount;

    private KCollectionCollectionDetailsAdapter mAdapter;
    CollectionCollectionDetailModel model;
    List<CollectionCollectionDetailModel.Datum> collectionDataList = new ArrayList<>();
    CollectionCollectionDetailModel.Datum selectedCustomer;

    CollectionCollectionModel.Table collectionData;

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

        collectionData = getArguments().getParcelable(COLLECTIONDATA);

        rlSearch.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        showProgress(ProgressDialogTexts.LOADING);
        if (from.equals("ECW")) {
            tviWIPDetailHeading.setText("Follow-up this Week");
            tviTOInvoice.setText(collectionData.getExpectedCollectionThisWeekInvoice().toString());
            tviTOAmount.setText(KBAMUtils.getRoundOffValue(collectionData.getExpectedCollectionThisWeekAmount()));
            BackgroundExecutor.getInstance().execute(new KCollectionECWRequester());
        } else if (from.equals("ECM")) {
            tviWIPDetailHeading.setText("Follow-up this Month");
            tviTOInvoice.setText(collectionData.getExpectedCollectionThisMonthInvoice().toString());
            tviTOAmount.setText(KBAMUtils.getRoundOffValue(collectionData.getExpectedCollectionThisMonthAmount()));
            BackgroundExecutor.getInstance().execute(new KCollectionECMRequester());
        } else if (from.equals("PCW")) {
            tviWIPDetailHeading.setText("Payment Collection this Week");
            tviTOInvoice.setText(collectionData.getPaymentCollectedThisWeekhInvoice().toString());
            tviTOAmount.setText(KBAMUtils.getRoundOffValue(collectionData.getPaymentCollectedThisWeekAmount()));
            BackgroundExecutor.getInstance().execute(new KCollectionPCWRequester());
        } else if (from.equals("PCM")) {
            tviWIPDetailHeading.setText("Payment Collection this Month");
            tviTOInvoice.setText(collectionData.getPaymentCollectedThisMonthInvoice().toString());
            tviTOAmount.setText(KBAMUtils.getRoundOffValue(collectionData.getPaymentCollectedThisMonthAmount()));
            BackgroundExecutor.getInstance().execute(new KCollectionPCMRequester());
        }

        /*rviData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == model.getTable().size() - 1) {
                        //bottom of list!
                        loadMore();
                    }
                } else {
                    isLoading = false;
                }
            }
        });*/

        return rootView;
    }

    /*private void loadMore() {
        model.getTable().add(null);
        mAdapter.notifyItemInserted(model.getTable().size() - 1);
        showProgress(ProgressDialogTexts.LOADING);
        String start = String.valueOf(nextLimit);
        String end = String.valueOf(nextLimit + 10);
        if (from.equals("WIP0")) {
            BackgroundExecutor.getInstance().execute(new KWIP015DaysCustomerRequester("0", "10",0));
        } else if (from.equals("WIP16")) {
            BackgroundExecutor.getInstance().execute(new KWIP015DaysCustomerInvoiceRequester("0", "10",0));
        } else if (from.equals("WIP30")) {
            BackgroundExecutor.getInstance().execute(new KCollectionOutstandingCurrentMonthRequester("0", "10",0));
        } else if (from.equals("PDOSL")) {
            BackgroundExecutor.getInstance().execute(new KCollectionOutstandingSubsequentMonthRequester("0", "10",0));
        } else if (from.equals("PDOSG")) {
            BackgroundExecutor.getInstance().execute(new KCollectionOutstandingSubsequentMonthRequester("0", "10",0));
        }
        isLoading = true;
    }*/

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
        return CollectionDetailsFragment.class.getSimpleName();
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
                    case Events.GET_COLLECTION_COLLECTION_CUSTOMER_SUCCESSFULL:
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceCollectionWIPDataResponse(eventObject.getObject().toString()));
                            model = (CollectionCollectionDetailModel) BAMUtil.fromJson(String.valueOf(jsonObject), CollectionCollectionDetailModel.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        if (model != null) {
                            collectionDataList = model.getData();
                            //tviTOInvoice.setText(model.getInvoice().toString());
                            //tviTOAmount.setText(KBAMUtils.getRoundOffValue(model.getAmount()));

                            mAdapter = new KCollectionCollectionDetailsAdapter(dashboardActivityContext, collectionDataList);
                            rviData.setAdapter(mAdapter);
                        }
                        break;
                    case Events.GET_COLLECTION_COLLECTION_CUSTOMER_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.COLLECTION_CUSTOMER_SELECT:
                        selectedCustomer = (CollectionCollectionDetailModel.Datum) eventObject.getObject();
                        Bundle customerBundle = new Bundle();
                        //customerBundle.putString(CollectionCustomerDetailsFragment.USER_ID, userId);
                        customerBundle.putString(CollectionCustomerDetailsFragment.FROM, from);
                        customerBundle.putParcelable(CollectionCustomerDetailsFragment.CUSTOMER_DATA, selectedCustomer);

                        customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.COLLECTION_CUSTOMER_DETAIL_FRAGMENT, customerBundle);
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
