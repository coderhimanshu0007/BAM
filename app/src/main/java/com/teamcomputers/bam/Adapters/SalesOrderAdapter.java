package com.teamcomputers.bam.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.FAModel;
import com.teamcomputers.bam.Models.SalesOrderModel;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class SalesOrderAdapter extends RecyclerView.Adapter<SalesOrderAdapter.ViewHolder> {
    private ArrayList<SalesOrderModel> dataList;
    private int card_layout;
    Activity mActivity;

    public SalesOrderAdapter(DashboardActivity dashboardActivityContext, ArrayList<SalesOrderModel> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<SalesOrderModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviPurchaserName, tviSoNo, tviCustomerName, tviCount, tviAmount, tviNoofDays, tviCoardinatorName, tviPurchaserRemark, tviPurchaseBucket;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviPurchaserName = (TextView) itemView.findViewById(R.id.tviPurchaserName);
            this.tviSoNo = (TextView) itemView.findViewById(R.id.tviSoNo);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviNoofDays = (TextView) itemView.findViewById(R.id.tviNoofDays);
            this.tviCoardinatorName = (TextView) itemView.findViewById(R.id.tviCoardinatorName);
            this.tviPurchaserRemark = (TextView) itemView.findViewById(R.id.tviPurchaserRemark);
            this.tviPurchaseBucket = (TextView) itemView.findViewById(R.id.tviPurchaseBucket);
        }
    }

    @Override
    public SalesOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_order_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SalesOrderAdapter.ViewHolder holder, final int position) {
        holder.tviPurchaserName.setText(dataList.get(position).getPurchaserName());
        holder.tviSoNo.setText(dataList.get(position).getSoNo());
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviCount.setText(dataList.get(position).getCount());
        holder.tviAmount.setText(dataList.get(position).getAmount());
        holder.tviNoofDays.setText(dataList.get(position).getNoofDays());
        holder.tviCoardinatorName.setText(dataList.get(position).getCoardinatorName());
        holder.tviPurchaserRemark.setText(dataList.get(position).getPurchaserRemark());
        holder.tviPurchaseBucket.setText(dataList.get(position).getPurchaseBucket());



        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesController.getInstance(mActivity).setFrom("2");
                SharedPreferencesController.getInstance(mActivity).setLocation(dataList.get(position).getCustName());
                EventBus.getDefault().post(new EventObject(ClickEvents.PENDING_TASK_CALLS, dataList.get(position)));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

