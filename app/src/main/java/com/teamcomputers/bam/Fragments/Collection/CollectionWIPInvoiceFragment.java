package com.teamcomputers.bam.Fragments.Collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.Collection.KCollectionWIPDetailListAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.Collection.CollectionWIPDetailModel;
import com.teamcomputers.bam.Models.Collection.WIPCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.Collection.KCollectionECMInvoiceSearchRequester;
import com.teamcomputers.bam.Requesters.Collection.KCollectionECWInvoiceSearchRequester;
import com.teamcomputers.bam.Requesters.Collection.KCollectionPCMInvoiceSearchRequester;
import com.teamcomputers.bam.Requesters.Collection.KCollectionPCWInvoiceSearchRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIP015DaysCustomerInvoiceRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIP015DaysCustomerInvoiceSearchRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIP1630DaysCustomerInvoiceRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIP1630DaysCustomerInvoiceSearchRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIP30DaysCustomerInvoiceRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIP30DaysCustomerInvoiceSearchRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIPPendingDocSubGreaterThan2DaysCustomerInvoiceRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIPPendingDocSubGreaterThan2DaysCustomerInvoiceSearchRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIPPendingDocSubLessThan2DaysCustomerInvoiceRequester;
import com.teamcomputers.bam.Requesters.Collection.KWIPPendingDocSubLessThan2DaysCustomerInvoiceSearchRequester;
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
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class CollectionWIPInvoiceFragment extends BaseFragment {
    private View rootView;
    public static final String FROM = "FROM";
    public static final String CUSTOMER_DATA = "CUSTOMER_DATA";
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String from = "";
    boolean isLoading = false;
    int nextLimit = 0;

    @BindView(R.id.txtSearch)
    TextView txtSearch;

    @BindView(R.id.rviData)
    RecyclerView rviData;

    @BindView(R.id.tviWIPDetailHeading)
    TextView tviWIPDetailHeading;

    @BindView(R.id.llInvoice)
    LinearLayout llInvoice;

    @BindView(R.id.tviTOInvoice)
    TextView tviTOInvoice;
    @BindView(R.id.tviTOAmount)
    TextView tviTOAmount;

    @BindView(R.id.cviHeading)
    CardView cviHeading;

    private KCollectionWIPDetailListAdapter mAdapter;
    CollectionWIPDetailModel model;
    List<CollectionWIPDetailModel.Datum> invoiceDataList = new ArrayList<>();
    WIPCustomerModel.Datum selectedCustomer;

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

        selectedCustomer = getArguments().getParcelable(CUSTOMER_DATA);

        llInvoice.setVisibility(View.INVISIBLE);
        cviHeading.setVisibility(View.GONE);

        tviWIPDetailHeading.setText(selectedCustomer.getCustomerName());
        tviTOAmount.setText(KBAMUtils.getRoundOffValue(selectedCustomer.getAmount()));

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviData.setLayoutManager(layoutManager);

        loadInitialData();

        rviData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == model.getData().size() - 1) {
                        //bottom of list!
                        loadMore();
                    }
                } else {
                    isLoading = false;
                }
            }
        });

        return rootView;
    }

    private void loadInitialData() {
        showProgress(ProgressDialogTexts.LOADING);
        if (from.equals("WIP0")) {
            BackgroundExecutor.getInstance().execute(new KWIP015DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), "0", "50", "0"));
        } else if (from.equals("WIP16")) {
            BackgroundExecutor.getInstance().execute(new KWIP1630DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), "0", "50", "0"));
        } else if (from.equals("WIP30")) {
            BackgroundExecutor.getInstance().execute(new KWIP30DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), "0", "50", "0"));
        } else if (from.equals("PDOSL")) {
            BackgroundExecutor.getInstance().execute(new KWIPPendingDocSubLessThan2DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), "0", "50", "0"));
        } else if (from.equals("PDOSG")) {
            BackgroundExecutor.getInstance().execute(new KWIPPendingDocSubGreaterThan2DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), "0", "50", "0"));
        }
    }

    private void loadMore() {
        model.getData().add(null);
        mAdapter.notifyItemInserted(model.getData().size() - 1);
        String start = String.valueOf(nextLimit);
        String end = String.valueOf(nextLimit + 50);
        showProgress(ProgressDialogTexts.LOADING);
        if (from.equals("WIP0")) {
            BackgroundExecutor.getInstance().execute(new KWIP015DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), start, end, "1"));
        } else if (from.equals("WIP16")) {
            BackgroundExecutor.getInstance().execute(new KWIP1630DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), start, end, "1"));
        } else if (from.equals("WIP30")) {
            BackgroundExecutor.getInstance().execute(new KWIP30DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), start, end, "1"));
        } else if (from.equals("PDOSL")) {
            BackgroundExecutor.getInstance().execute(new KWIPPendingDocSubLessThan2DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), start, end, "1"));
        } else if (from.equals("PDOSG")) {
            BackgroundExecutor.getInstance().execute(new KWIPPendingDocSubGreaterThan2DaysCustomerInvoiceRequester(selectedCustomer.getCustomerName(), start, end, "1"));
        }
        isLoading = true;
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
        return CollectionWIPInvoiceFragment.class.getSimpleName();
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
                    case Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_SUCCESSFULL:
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceCollectionWIPDataResponse(eventObject.getObject().toString()));
                            model = (CollectionWIPDetailModel) BAMUtil.fromJson(String.valueOf(jsonObject), CollectionWIPDetailModel.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        if (model != null) {
                            invoiceDataList = model.getData();
                            //tviTOInvoice.setText(model.getInvoice().toString());
                            //tviTOAmount.setText(KBAMUtils.getRoundOffValue(model.getAmount()));

                            mAdapter = new KCollectionWIPDetailListAdapter(dashboardActivityContext, invoiceDataList);
                            rviData.setAdapter(mAdapter);
                        }
                        break;
                    case Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_LOAD_MORE_SUCCESSFULL:
                        model.getData().remove(model.getData().size() - 1);
                        int scrollPosition = model.getData().size();
                        mAdapter.notifyItemRemoved(scrollPosition);
                        int currentSize = scrollPosition;
                        nextLimit = currentSize + 11;
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceCollectionWIPDataResponse(eventObject.getObject().toString()));
                            model = (CollectionWIPDetailModel) BAMUtil.fromJson(String.valueOf(jsonObject), CollectionWIPDetailModel.class);
                            if (model != null) {
                                invoiceDataList = model.getData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                        isLoading = false;
                        dismissProgress();
                        break;
                    case Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_LOAD_MORE_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_SEARCH_SUCCESSFULL:
                        try {
                            JSONObject jsonObject = new JSONObject(KBAMUtils.replaceCollectionWIPDataResponse(eventObject.getObject().toString()));
                            model = (CollectionWIPDetailModel) BAMUtil.fromJson(String.valueOf(jsonObject), CollectionWIPDetailModel.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                        if (model != null) {
                            invoiceDataList = model.getData();
                            mAdapter = new KCollectionWIPDetailListAdapter(dashboardActivityContext, invoiceDataList);
                            rviData.setAdapter(mAdapter);
                        }
                        break;
                    case Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_SEARCH_UNSUCCESSFULL:
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

    @OnTextChanged(R.id.txtSearch)
    public void search() {
        if (txtSearch.getText().toString().length() > 2) {
            isLoading = true;
            loadSearchData(txtSearch.getText().toString());
        } else if (txtSearch.getText().toString().length() == 0) {
            isLoading = false;
            KBAMUtils.hideSoftKeyboard(dashboardActivityContext);
            loadInitialData();
        }
    }

    private void loadSearchData(String searchText) {
        showProgress(ProgressDialogTexts.LOADING);
        if (from.equals("WIP0")) {
            BackgroundExecutor.getInstance().execute(new KWIP015DaysCustomerInvoiceSearchRequester(selectedCustomer.getCustomerName(), searchText));
        } else if (from.equals("WIP16")) {
            BackgroundExecutor.getInstance().execute(new KWIP1630DaysCustomerInvoiceSearchRequester(selectedCustomer.getCustomerName(), searchText));
        } else if (from.equals("WIP30")) {
            BackgroundExecutor.getInstance().execute(new KWIP30DaysCustomerInvoiceSearchRequester(selectedCustomer.getCustomerName(), searchText));
        } else if (from.equals("PDOSL")) {
            BackgroundExecutor.getInstance().execute(new KWIPPendingDocSubLessThan2DaysCustomerInvoiceSearchRequester(selectedCustomer.getCustomerName(), searchText));
        } else if (from.equals("PDOSG")) {
            BackgroundExecutor.getInstance().execute(new KWIPPendingDocSubGreaterThan2DaysCustomerInvoiceSearchRequester(selectedCustomer.getCustomerName(), searchText));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
