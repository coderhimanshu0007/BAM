package com.teamcomputers.bam.Fragments.Collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.Collection.KCollectionOutstandingAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.Collection.OutstandingDataModel;
import com.teamcomputers.bam.Models.Collection.OutstandingModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Collection.KCollectionCollectibleOutstandingRequester;
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
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class TotalOutstandingFragment extends BaseFragment {
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

    @BindView(R.id.txtSearch)
    EditText txtSearch;

    @BindView(R.id.tviWIPDetailHeading)
    TextView tviWIPDetailHeading;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    @BindView(R.id.tviTOInvoice)
    TextView tviTOInvoice;
    @BindView(R.id.tviTOAmount)
    TextView tviTOAmount;

    private KCollectionOutstandingAdapter mAdapter;
    OutstandingDataModel model;
    OutstandingDataModel.Datum selectedCustomer;

    OutstandingModel.Table collectionData;

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

        fetchData();

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
                    if (nextLimit > 5 && linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == model.getTable().size() - 1) {
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

    private void fetchData() {
        showProgress(ProgressDialogTexts.LOADING);
        if (from.equals("TOTALOUTSTANDING")) {
            tviWIPDetailHeading.setText("Total Outstanding");
            tviTOInvoice.setText(collectionData.getTotalOutStandingInvoice().toString());
            tviTOAmount.setText(KBAMUtils.getRoundOffValue(collectionData.getTotalOutStandingAmount()));
            BackgroundExecutor.getInstance().execute(new KCollectionTotalOutstandingRequester());
        } else if (from.equals("COLLECTIBLEOUTSTANDING")) {
            tviWIPDetailHeading.setText("Collectible Outstanding");
            tviTOInvoice.setText(collectionData.getCollectibleOutStandingInvoice().toString());
            tviTOAmount.setText(KBAMUtils.getRoundOffValue(collectionData.getCollectibleOutStandingAmount()));
            BackgroundExecutor.getInstance().execute(new KCollectionCollectibleOutstandingRequester());
        } else if (from.equals("COCM")) {
            tviWIPDetailHeading.setText("Collectible Outstanding in Current Month");
            tviTOInvoice.setText(collectionData.getCollectibleOutStandingCurrentMonthInvoice().toString());
            tviTOAmount.setText(KBAMUtils.getRoundOffValue(collectionData.getCollectibleOutStandingCurrentMonthAmount()));
            BackgroundExecutor.getInstance().execute(new KCollectionOutstandingCurrentMonthRequester());
        } else if (from.equals("COSM")) {
            tviWIPDetailHeading.setText("Collectible Outstanding in Subsequent Month");
            tviTOInvoice.setText(collectionData.getCollectibleOutStandingCurrentMonthInvoice().toString());
            tviTOAmount.setText(KBAMUtils.getRoundOffValue(collectionData.getCollectibleOutStandingCurrentMonthAmount()));
            BackgroundExecutor.getInstance().execute(new KCollectionOutstandingSubsequentMonthRequester());
        }
    }

    /*private void loadMore() {
        model.getTable().add(null);
        mAdapter.notifyItemInserted(model.getTable().size() - 1);
        showProgress(ProgressDialogTexts.LOADING);
        String start = String.valueOf(nextLimit);
        String end = String.valueOf(nextLimit + 5);
        if (from.equals("TOTALOUTSTANDING")) {
            BackgroundExecutor.getInstance().execute(new KCollectionTotalOutstandingRequester(start, end, 1));
        } else if (from.equals("COLLECTIBLEOUTSTANDING")) {
            BackgroundExecutor.getInstance().execute(new KCollectionTotalOutstandingRequester(start, end, 1));
        } else if (from.equals("COCM")) {
            BackgroundExecutor.getInstance().execute(new KCollectionOutstandingCurrentMonthRequester(start, end, 1));
        } else if (from.equals("COSM")) {
            BackgroundExecutor.getInstance().execute(new KCollectionOutstandingSubsequentMonthRequester(start, end, 1));
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
                    case Events.GET_COLLECTION_OUTSTANDING_CUSTOMER_SUCCESSFULL:
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceTotalOutstandingDataResponse(eventObject.getObject().toString()));
                            model = (OutstandingDataModel) BAMUtil.fromJson(String.valueOf(jsonObject), OutstandingDataModel.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        if (model != null) {
                            //tviTOInvoice.setText(model.getInvoice().toString());
                            //tviTOAmount.setText(KBAMUtils.getRoundOffValue(model.getAmount()));

                            mAdapter = new KCollectionOutstandingAdapter(dashboardActivityContext, model.getData());
                            rviData.setAdapter(mAdapter);
                            //isLoading = true;
                        }
                        break;
                    case Events.GET_COLLECTION_OUTSTANDING_CUSTOMER_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    /*case Events.GET_CTOS_LOAD_MORE_SUCCESSFULL:
                        model.getTable().remove(model.getTable().size() - 1);
                        int scrollPosition = model.getTable().size();
                        mAdapter.notifyItemRemoved(scrollPosition);
                        int currentSize = scrollPosition;
                        nextLimit = currentSize + 6;
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceTotalOutstandingDataResponse(eventObject.getObject().toString()));
                            model = (TotalOutstandingModel) BAMUtil.fromJson(String.valueOf(jsonObject), TotalOutstandingModel.class);
                            if (model != null) {
                                tosDataList.addAll(model.getTable());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                        isLoading = false;
                        dismissProgress();
                        break;
                    case Events.GET_CTOS_LOAD_MORE_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;*/
                    case ClickEvents.COLLECTION_CUSTOMER_SELECT:
                        selectedCustomer = (OutstandingDataModel.Datum) eventObject.getObject();
                        Bundle customerBundle = new Bundle();
                        //customerBundle.putString(CollectionCustomerDetailsFragment.USER_ID, userId);
                        customerBundle.putString(OutstandingCustomerDetailsFragment.FROM, from);
                        customerBundle.putParcelable(OutstandingCustomerDetailsFragment.CUSTOMER_DATA, selectedCustomer);

                        customerBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.OUTSTANDING_CUSTOMER_DETAIL_FRAGMENT, customerBundle);
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

    @OnTextChanged(R.id.txtSearch)
    public void search() {
        if (txtSearch.getText().toString().length() > 2) {
            isLoading = true;
            fetchData();
        } else if (txtSearch.getText().toString().length() == 0) {
            isLoading = false;
            KBAMUtils.hideSoftKeyboard(dashboardActivityContext);
            showProgress(ProgressDialogTexts.LOADING);
            /*if (from.equals("TOTALOUTSTANDING")) {
                BackgroundExecutor.getInstance().execute(new KCollectionTotalOutstandingRequester("0", "10", 0));
            } else if (from.equals("COLLECTIBLEOUTSTANDING")) {
                BackgroundExecutor.getInstance().execute(new KCollectionTotalOutstandingSearchRequester(txtSearch.getText().toString()));
            } else if (from.equals("COCM")) {
                BackgroundExecutor.getInstance().execute(new KCollectionCollectibleOutStandingCurrentMonthCustomerInvoiceSearchRequester(txtSearch.getText().toString()));
            } else if (from.equals("COSM")) {
                BackgroundExecutor.getInstance().execute(new KCollectionOutstandingSubsequentMonthCustomerInvoiceSearchRequester(txtSearch.getText().toString()));
            }*/
        }
    }

    /*@OnClick(R.id.iviSearch)
    public void Search() {
        if (!search) {
            txtSearch.setVisibility(View.VISIBLE);
            search = true;
        } else if (search) {
            txtSearch.setVisibility(View.GONE);
            search = false;
        }
    }*/

}
