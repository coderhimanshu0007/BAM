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
import com.teamcomputers.bam.Requesters.SalesReceivable.SalesReceivableSalesRequester;
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

public class SalesFragment extends BaseFragment {
    private View rootView;
    private Unbinder unbinder;
    private DashboardActivity dashboardActivityContext;

    @BindView(R.id.rviSales)
    RecyclerView rviSales;
    private TreeViewAdapter adapter;

    @BindView(R.id.tviYTD)
    TextView tviYTD;
    @BindView(R.id.tviMTD)
    TextView tviMTD;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sales, container, false);
        dashboardActivityContext = (DashboardActivity) context;
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgress(ProgressDialogTexts.LOADING);
        BackgroundExecutor.getInstance().execute(new SalesReceivableSalesRequester());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return SalesFragment.class.getSimpleName();
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
                    case Events.GET_SALES_RECEIVABLE_SALES_SUCCESSFULL:
                        dismissProgress();
                        initData(eventObject);
                        break;
                    case Events.GET_SALES_RECEIVABLE_SALES_UNSUCCESSFULL:
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
        Double mtd = 0.0;
        Double ytd = 0.0;
        List<TreeNode> nodes = new ArrayList<>();
        for (int i = 0; i < ((ArrayList) eventObject.getObject()).size(); i++) {
            mtd = mtd + (Double) ((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i)).get("MTD");
            ytd = ytd + (Double) ((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i)).get("YTD");
            SRResponseModel srResponse = getMain((LinkedTreeMap) ((ArrayList) eventObject.getObject()).get(i));
            TreeNode<Dir> app = new TreeNode<>(new Dir(srResponse.getName(), srResponse.getYTD(), srResponse.getMTD()));
            if (null != srResponse.getLinkedTreeMap()) {
                for (int r = 0; r < srResponse.getLinkedTreeMap().size(); r++) {
                    SRResponseModel srResponseRSM = getRSM(srResponse.getLinkedTreeMap().get(r));
                    TreeNode<Dir> rsm = new TreeNode<>(new Dir(srResponseRSM.getName(), srResponseRSM.getYTD(), srResponseRSM.getMTD()));
                    if (null != srResponseRSM.getLinkedTreeMap()) {
                        for (int acct = 0; acct < srResponseRSM.getLinkedTreeMap().size(); acct++) {
                            SRResponseModel srResponseAccount = getAccount(srResponseRSM.getLinkedTreeMap().get(acct));
                            TreeNode<Dir> account = new TreeNode<>(new Dir(srResponseAccount.getName(), srResponseAccount.getYTD(), srResponseAccount.getMTD()));
                            if (null != srResponseAccount.getLinkedTreeMap()) {
                                for (int s = 0; s < srResponseAccount.getLinkedTreeMap().size(); s++) {
                                    SRResponseModel srResponseSales = getSales(srResponseAccount.getLinkedTreeMap().get(s));
                                    TreeNode<Dir> sales = new TreeNode<>(new Dir(srResponseSales.getName(), srResponseSales.getYTD(), srResponseSales.getMTD()));
                                    if (null != srResponseSales.getLinkedTreeMap()) {
                                        for (int cust = 0; cust < srResponseSales.getLinkedTreeMap().size(); cust++) {
                                            SRResponseModel srResponseCustomer = getCustomer(srResponseSales.getLinkedTreeMap().get(cust));
                                            TreeNode<Dir> customer = new TreeNode<>(new Dir(srResponseCustomer.getName(), srResponseCustomer.getYTD(), srResponseCustomer.getMTD()));
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

        tviMTD.setText(BAMUtil.getRoundOffValue(mtd));
        tviYTD.setText(BAMUtil.getRoundOffValue(ytd));
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
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
            }
        });
        rviSales.setAdapter(adapter);

    }

    private SRResponseModel getMain(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        ArrayList<LinkedTreeMap> linkedTreeData = null;//= (ArrayList<LinkedTreeMap>) linkedTreeMap.get("RSM");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getRSM(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Account");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getAccount(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Sales");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getSales(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("TMC");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Customer");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, ytd, mtd, linkedTreeData);
        return srResponseModel;
    }

    private SRResponseModel getCustomer(LinkedTreeMap linkedTreeMap) {
        String name = (String) linkedTreeMap.get("Name");
        String tmc = (String) linkedTreeMap.get("Code");
        String mtd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("MTD"));
        String ytd = BAMUtil.getRoundOffValue((Double) linkedTreeMap.get("YTD"));
        //ArrayList<LinkedTreeMap> linkedTreeData = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("Customer");
        SRResponseModel srResponseModel = new SRResponseModel(name, tmc, mtd, ytd);
        return srResponseModel;
    }

}