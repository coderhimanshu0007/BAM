package com.teamcomputers.bam.Fragments.SalesReceivable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Adapters.Installation.DOAIRAdapter;
import com.teamcomputers.bam.Fragments.BaseFragment;
import com.teamcomputers.bam.Models.SRResponseModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.SalesReceivable.SalesReceivableOutstandingRequester;
import com.teamcomputers.bam.TreeView.bean.Dir;
import com.teamcomputers.bam.TreeView.bean.File;
import com.teamcomputers.bam.TreeView.viewbinder.DirectoryNodeBinder;
import com.teamcomputers.bam.TreeView.viewbinder.FileNodeBinder;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.recyclertreeview_lib.TreeNode;
import com.teamcomputers.bam.recyclertreeview_lib.TreeViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OutstandingFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;

    @BindView(R.id.rviOutstanding)
    RecyclerView rviOutstanding;
    private TreeViewAdapter adapter;

    @BindView(R.id.tviARTD)
    TextView tviARTD;
    @BindView(R.id.tviARPM)
    TextView tviARPM;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sr_outstanding, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //showProgress(ProgressDialogTexts.LOADING);
        //BackgroundExecutor.getInstance().execute(new SalesReceivableOutstandingRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return OutstandingFragment.class.getSimpleName();
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
                    case Events.GET_SALES_RECEIVABLE_OUTSTANDING_SUCCESSFULL:
                        dismissProgress();
                        initData(eventObject);
                        break;
                    case Events.GET_SALES_RECEIVABLE_OUTSTANDING_UNSUCCESSFULL:
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

    private void initData(EventObject eventObject) {
        Double arpm = 0.0;
        Double artd = 0.0;
        List<TreeNode> nodes = new ArrayList<>();
        for (int i = 0; i < ((ArrayList) eventObject.getObject()).size(); i++) {
            //arpm = arpm + (Double) ((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i)).get("MTD");
            artd = artd + (Double) ((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i)).get("Amount");
            SRResponseModel srResponse = getSRData((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i),"RSM");
            TreeNode<Dir> app = new TreeNode<>(new Dir(srResponse.getName(), srResponse.getYTD(), srResponse.getMTD(), srResponse.getsO()));
            if (null != srResponse.getLinkedTreeMap()) {
                for (int r = 0; r < srResponse.getLinkedTreeMap().size(); r++) {
                    SRResponseModel srResponseRSM = getSRData(srResponse.getLinkedTreeMap().get(r),"Account");
                    TreeNode<Dir> rsm = new TreeNode<>(new Dir(srResponseRSM.getName(), srResponseRSM.getYTD(), srResponseRSM.getMTD(), srResponseRSM.getsO()));
                    if (null != srResponseRSM.getLinkedTreeMap()) {
                        for (int acct = 0; acct < srResponseRSM.getLinkedTreeMap().size(); acct++) {
                            SRResponseModel srResponseAccount = getSRData(srResponseRSM.getLinkedTreeMap().get(acct),"Sales");
                            TreeNode<Dir> account = new TreeNode<>(new Dir(srResponseAccount.getName(), srResponseAccount.getYTD(), srResponseAccount.getMTD(), srResponseAccount.getsO()));
                            if (null != srResponseAccount.getLinkedTreeMap()) {
                                for (int s = 0; s < srResponseAccount.getLinkedTreeMap().size(); s++) {
                                    SRResponseModel srResponseSales = getSRData(srResponseAccount.getLinkedTreeMap().get(s), "Customer");
                                    TreeNode<Dir> sales = new TreeNode<>(new Dir(srResponseSales.getName(), srResponseSales.getYTD(), srResponseSales.getMTD(), srResponseSales.getsO()));
                                    if (null != srResponseSales.getLinkedTreeMap()) {
                                        for (int cust = 0; cust < srResponseSales.getLinkedTreeMap().size(); cust++) {
                                            SRResponseModel srResponseCustomer = getSRData(srResponseSales.getLinkedTreeMap().get(cust),"");
                                            TreeNode<Dir> customer = new TreeNode<>(new Dir(srResponseCustomer.getName(), srResponseCustomer.getYTD(), srResponseCustomer.getMTD(), srResponseCustomer.getsO()));
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

        tviARPM.setText(BAMUtil.getRoundOffValue(arpm));
        tviARTD.setText(BAMUtil.getRoundOffValue(artd));
        rviOutstanding.setLayoutManager(new LinearLayoutManager(dashboardActivityContext));
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
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
            }
        });
        rviOutstanding.setAdapter(adapter);

    }

    private SRResponseModel getSRData(LinkedTreeMap linkedTreeMap, String treeName) {
        SRResponseModel srResponseModel = new SRResponseModel();
        String name = (String) linkedTreeMap.get("Name");
        String so = "0.0";
        String mtd = "0";//BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("Amount"));

        if(treeName.equals("")) {
            String tmc = (String) linkedTreeMap.get("Code");
            String productName = (String) linkedTreeMap.get("ProductName");
            srResponseModel = new SRResponseModel(name, productName, tmc, ytd, mtd, so);
        } else {
            String tmc = (String) linkedTreeMap.get("TMC");
            ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get(treeName);
            srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, so, linkedTreeData);
        }
        return srResponseModel;
    }

    private SRResponseModel getRSM(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String so = "0.0";
        String mtd = "0";//BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("Amount"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Account");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, so, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getAccount(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String so = "0.0";
        String mtd = "0";//BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("Amount"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Sales");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, so, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getSales(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String so = "0.0";
        String mtd = "0";//BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("Amount"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Customer");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, so, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getCustomer(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String productName = (String) linkedTreeMap.get("ProductName");
        String tmc = (String) linkedTreeMap.get("Code");
        String so = "0.0";
        String mtd = "0";//BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("Amount"));
        //ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Customer");
        SRResponseModel srResponseModel = new SRResponseModel(name, productName, tmc, ytd, mtd, so);
        return srResponseModel;
    }
}
