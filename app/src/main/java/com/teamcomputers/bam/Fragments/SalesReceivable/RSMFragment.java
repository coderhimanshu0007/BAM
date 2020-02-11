package com.teamcomputers.bam.Fragments.SalesReceivable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.SalesOutstanding.RSMAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.RSMDataModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.FullSalesListRequester;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RSMFragment extends BaseFragment {
    public static final String SALES_DATA = "SALES_DATA";
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;
    private LinearLayoutManager layoutManager;

    String toolbarTitle = "";

    @BindView(R.id.rviRSM)
    RecyclerView rviRSM;
    private RSMAdapter adapter;

    private ArrayList<RSMDataModel> rsmDataModelArrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rsm, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        toolbarTitle = getString(R.string.Heading_RSM);
        dashboardActivityContext.setToolBarTitle(toolbarTitle);

        layoutManager = new LinearLayoutManager(dashboardActivityContext);
        rviRSM.setLayoutManager(layoutManager);

        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new FullSalesListRequester("1464", "R1", "RSM"));

        /*rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        rsmDataModelArrayList.add(new RSMDataModel("Sagar Pushkarna", "625.2", "625.2", "625.2"));
        adapter = new RSMAdapter(dashboardActivityContext, rsmDataModelArrayList);
        rviRSM.setAdapter(adapter);*/

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*SalesDataModel[] model = SharedPreferencesController.getInstance(dashboardActivityContext).getSalesData();
        if (null != model) {
            initTreeData(model);
        }*/
        /*showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new SalesReceivableSalesRequester());*/
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return RSMFragment.class.getSimpleName();
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
                    case Events.GET_FULL_SALES_LIST_SUCCESSFULL:
                        dismissProgress();
                        FullSalesModel[] model = new FullSalesModel[0];
                        try {
                            JSONArray jsonArray = new JSONArray(BAMUtil.replaceDataResponse(eventObject.getObject().toString()));
                            model = (FullSalesModel[]) BAMUtil.fromJson(String.valueOf(jsonArray), FullSalesModel[].class);
                            Log.e("Hello", "Helo");
                            //SharedPreferencesController.getInstance(dashboardActivityContext).setSalesData(model);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //initTreeData(model);
                        initData(model);
                        dismissProgress();
                        break;
                    case Events.GET_FULL_SALES_LIST_UNSUCCESSFULL:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case ClickEvents.RSM_ITEM:
                        Bundle rsmDataBundle = new Bundle();
                        rsmDataBundle.putParcelable(AccountsFragment.USER_PROFILE, (RSMDataModel) eventObject.getObject());
                        rsmDataBundle.putBoolean(DashboardActivity.IS_EXTRA_FRAGMENT_NEEDS_TO_BE_LOADED, true);
                        dashboardActivityContext.replaceFragment(Fragments.ACCOUNT_FRAGMENT, rsmDataBundle);
                        break;
                }
            }
        });
    }

    private void initData(FullSalesModel[] model) {
        adapter = new RSMAdapter(dashboardActivityContext, model);
        rviRSM.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    /*private void initTreeData(SalesDataModel[] model) {
        Double mtd = 0.0;
        Double ytd = 0.0;
        Double oso = 0.0;
        List<TreeNode> nodes = new ArrayList<>();
        for (int i = 0; i < model.length; i++) {
            SalesDataModel salesDataModel = model[i];
            mtd = mtd + salesDataModel.getMTD();
            ytd = ytd + salesDataModel.getYTD();
            oso = oso + salesDataModel.getSOAmount();
            TreeNode<Dir> app = new TreeNode<>(new Dir(String.valueOf(salesDataModel.getName()), BAMUtil.getRoundOffValue(salesDataModel.getYTD()), BAMUtil.getRoundOffValue(salesDataModel.getMTD()), BAMUtil.getRoundOffValue(salesDataModel.getSOAmount()), "1"));
            if (null != salesDataModel.getRSM()) {
                List<RSMModel> rsmModelList = salesDataModel.getRSM();
                for (int rsm = 0; rsm < rsmModelList.size(); rsm++) {
                    RSMModel rsmModel = rsmModelList.get(rsm);
                    TreeNode<Dir> rsmData = new TreeNode<>(new Dir(String.valueOf(rsmModel.getName()), BAMUtil.getRoundOffValue(rsmModel.getYTD()), BAMUtil.getRoundOffValue(rsmModel.getMTD()), BAMUtil.getRoundOffValue(rsmModel.getSOAmount()), "2"));
                    if (null != rsmModel.getAccount()) {
                        List<AccountModel> accountModelList = rsmModel.getAccount();
                        for (int r = 0; r < accountModelList.size(); r++) {
                            for (int acct = 0; acct < accountModelList.size(); acct++) {
                                AccountModel accountModel = accountModelList.get(acct);
                                TreeNode<Dir> acctData = new TreeNode<>(new Dir(String.valueOf(accountModel.getName()), BAMUtil.getRoundOffValue(accountModel.getYTD()), BAMUtil.getRoundOffValue(accountModel.getMTD()), BAMUtil.getRoundOffValue(accountModel.getSOAmount()), "3"));
                                *//*if (null != accountModel.getSales()) {
                                    List<SalesModel> salesModelList = accountModel.getSales();
                                    for (int sls = 0; sls < salesModelList.size(); sls++) {
                                        SalesModel salesModel = salesModelList.get(sls);
                                        TreeNode<Dir> salesData = new TreeNode<>(new Dir(String.valueOf(salesModel.getName()), BAMUtil.getRoundOffValue(salesModel.getYTD()), BAMUtil.getRoundOffValue(salesModel.getMTD()), BAMUtil.getRoundOffValue(salesModel.getSOAmount()), "4"));
                                        if (null != salesModel.getCustomer()) {
                                            List<CustomerModel> customerModelList = salesModel.getCustomer();
                                            for (int cstmr = 0; cstmr < customerModelList.size(); cstmr++) {
                                                CustomerModel customerModel = customerModelList.get(cstmr);
                                                TreeNode<Dir> cstmrData = new TreeNode<>(new Dir(String.valueOf(customerModel.getName()), BAMUtil.getRoundOffValue(customerModel.getYTD()), BAMUtil.getRoundOffValue(customerModel.getMTD()), BAMUtil.getRoundOffValue(customerModel.getSOAmount()), "5"));
                                                if (null != customerModel.getProducts()) {
                                                    List<ProductModel> productModelList = customerModel.getProducts();
                                                    for (int prdct = 0; prdct < productModelList.size(); prdct++) {
                                                        ProductModel productModel = productModelList.get(prdct);
                                                        TreeNode<Dir> product = new TreeNode<>(new Dir(String.valueOf(productModel.getName()), BAMUtil.getRoundOffValue(productModel.getYTD()), BAMUtil.getRoundOffValue(productModel.getMTD()), BAMUtil.getRoundOffValue(productModel.getSOAmount()), "6"));
                                                        cstmrData.addChild(product);
                                                    }
                                                }
                                                salesData.addChild(cstmrData);
                                            }
                                        }
                                        acctData.addChild(salesData);
                                    }
                                }*//*
                                rsmData.addChild(acctData);
                            }
                        }
                    }
                    app.addChild(rsmData);
                }
            }
            nodes.add(app);
        }

        *//*tviMTD.setText(BAMUtil.getRoundOffValue(mtd));
        tviYTD.setText(BAMUtil.getRoundOffValue(ytd));
        tviOSO.setText(BAMUtil.getRoundOffValue(oso));*//*
        rviSales.setLayoutManager(new LinearLayoutManager(dashboardActivityContext));
        adapter = new TreeViewAdapter(nodes, Arrays.asList(new FileNodeBinder(), new DirectoryNodeBinder()));
        // whether collapse child nodes when their parent node was close.
//        adapter.ifCollapseChildWhileCollapseParent(true);
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                if (!node.isLeaf()) {
                    //Update and toggle the node.
                    onToggle(!node.isExpand(), holder);
//                    if (!node.isExpand())
//                        adapter.collapseBrotherNode(node);
                }
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                final LinearLayout llDir = dirViewHolder.getllDir();
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
                if (isExpand) {
                    llDir.setBackgroundColor(ContextCompat.getColor(dashboardActivityContext, R.color.colorTabNonSelected));
                } else {
                    llDir.setBackgroundColor(ContextCompat.getColor(dashboardActivityContext, R.color.login_bg));
                }
            }
        });
        rviSales.setAdapter(adapter);

    }

    private void initData(EventObject eventObject) {
        Double mtd = 0.0;
        Double ytd = 0.0;
        Double oso = 0.0;
        List<TreeNode> nodes = new ArrayList<>();
        for (int i = 0; i < ((ArrayList) eventObject.getObject()).size(); i++) {
            mtd = mtd + (Double) ((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i)).get("MTD");
            ytd = ytd + (Double) ((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i)).get("YTD");
            oso = oso + (Double) ((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i)).get("SOAmount");
            SRResponseModel srResponse = getSRData((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i), "RSM");
            TreeNode<Dir> app = new TreeNode<>(new Dir(srResponse.getName(), srResponse.getYTD(), srResponse.getMTD(), srResponse.getsO(), "1"));
            if (null != srResponse.getLinkedTreeMap()) {
                for (int r = 0; r < srResponse.getLinkedTreeMap().size(); r++) {
                    SRResponseModel srResponseRSM = getSRData(srResponse.getLinkedTreeMap().get(r), "Account");
                    TreeNode<Dir> rsm = new TreeNode<>(new Dir(srResponseRSM.getName(), srResponseRSM.getYTD(), srResponseRSM.getMTD(), srResponseRSM.getsO(), "2"));
                    if (null != srResponseRSM.getLinkedTreeMap()) {
                        for (int acct = 0; acct < srResponseRSM.getLinkedTreeMap().size(); acct++) {
                            SRResponseModel srResponseAccount = getSRData(srResponseRSM.getLinkedTreeMap().get(acct), "Sales");
                            TreeNode<Dir> account = new TreeNode<>(new Dir(srResponseAccount.getName(), srResponseAccount.getYTD(), srResponseAccount.getMTD(), srResponseAccount.getsO(), "3"));
                            if (null != srResponseAccount.getLinkedTreeMap()) {
                                for (int s = 0; s < srResponseAccount.getLinkedTreeMap().size(); s++) {
                                    SRResponseModel srResponseSales = getSRData(srResponseAccount.getLinkedTreeMap().get(s), "Customer");
                                    TreeNode<Dir> sales = new TreeNode<>(new Dir(srResponseSales.getName(), srResponseSales.getYTD(), srResponseSales.getMTD(), srResponseSales.getsO(), "4"));
                                    if (null != srResponseSales.getLinkedTreeMap()) {
                                        for (int cust = 0; cust < srResponseSales.getLinkedTreeMap().size(); cust++) {
                                            SRResponseModel srResponseCustomer = getSRData(srResponseSales.getLinkedTreeMap().get(cust), "Products");
                                            TreeNode<Dir> customer = new TreeNode<>(new Dir(srResponseCustomer.getName(), srResponseCustomer.getYTD(), srResponseCustomer.getMTD(), srResponseCustomer.getsO(), "5"));
                                            if (null != srResponseCustomer.getLinkedTreeMap()) {
                                                for (int prdct = 0; prdct < srResponseCustomer.getLinkedTreeMap().size(); prdct++) {
                                                    SRResponseModel srResponseProduct = getSRData(srResponseCustomer.getLinkedTreeMap().get(prdct), "");
                                                    TreeNode<Dir> product = new TreeNode<>(new Dir(srResponseProduct.getName(), srResponseProduct.getYTD(), srResponseProduct.getMTD(), srResponseProduct.getsO(), "6"));
                                                    customer.addChild(product);
                                                }
                                            }
                                            sales.addChild(customer);
                                        }
                                    }
                                    account.addChild(sales);
                                }
                            }
                            rsm.addChild(account);
                        }
                    }
                    app.addChild(rsm);
                }
            }
            nodes.add(app);
        }

        *//*tviMTD.setText(BAMUtil.getRoundOffValue(mtd));
        tviYTD.setText(BAMUtil.getRoundOffValue(ytd));
        tviOSO.setText(BAMUtil.getRoundOffValue(oso));*//*
        rviSales.setLayoutManager(new LinearLayoutManager(dashboardActivityContext));
        adapter = new TreeViewAdapter(nodes, Arrays.asList(new FileNodeBinder(), new DirectoryNodeBinder()));
        // whether collapse child nodes when their parent node was close.
//        adapter.ifCollapseChildWhileCollapseParent(true);
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                if (!node.isLeaf()) {
                    //Update and toggle the node.
                    onToggle(!node.isExpand(), holder);
//                    if (!node.isExpand())
//                        adapter.collapseBrotherNode(node);
                }
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                final LinearLayout llDir = dirViewHolder.getllDir();
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
                if (isExpand) {
                    llDir.setBackgroundColor(ContextCompat.getColor(dashboardActivityContext, R.color.colorTabNonSelected));
                } else {
                    llDir.setBackgroundColor(ContextCompat.getColor(dashboardActivityContext, R.color.login_bg));
                }
            }
        });
        rviSales.setAdapter(adapter);

    }

    private SRResponseModel getSRData(LinkedTreeMap linkedTreeMap, String treeName) {
        SRResponseModel srResponseModel = new SRResponseModel();
        String name = (String) linkedTreeMap.get("Name");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        String so = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("SOAmount"));
        if (treeName.equals("")) {
            srResponseModel = new SRResponseModel(name, mtd, ytd, so);
        } else {
            String tmc = (String) linkedTreeMap.get("TMC");
            ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get(treeName);
            srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, so, linkedTreeData);
        }
        return srResponseModel;
    }
*/
    /*private SRResponseModel getRSM(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        String so = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("SOAmount"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Account");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, so, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getAccount(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        String so = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("SOAmount"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Sales");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, so, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getSales(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        String so = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("SOAmount"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Customer");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, so, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getCustomer(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("Code");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        String so = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("SOAmount"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Products");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, so, linkedTreeData);
        //SRResponseModel srResponseModel = new SRResponseModel(name, tmc, mtd, ytd, so);
        return srResponseModel;
    }

    private SRResponseModel getProduct(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        //String tmc = (String) linkedTreeMap.get("Code");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        String so = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("SOAmount"));
        //ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Customer");
        SRResponseModel srResponseModel = new SRResponseModel(name, mtd, ytd, so);
        return srResponseModel;
    }
*/
}
