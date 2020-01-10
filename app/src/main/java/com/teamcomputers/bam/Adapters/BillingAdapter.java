package com.teamcomputers.bam.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.BillingModel;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.ViewHolder> {
    private ArrayList<BillingModel> dataList;
    private int card_layout;
    Activity mActivity;

    public BillingAdapter(DashboardActivity dashboardActivityContext, ArrayList<BillingModel> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<BillingModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviPurchaserName, tviSoNo, tviCustomerName, tviCount, tviAmount, tviEDDDate, tviSalesCoardinatorName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviPurchaserName = (TextView) itemView.findViewById(R.id.tviPurchaserName);
            this.tviSoNo = (TextView) itemView.findViewById(R.id.tviSoNo);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviEDDDate = (TextView) itemView.findViewById(R.id.tviEDDDate);
            this.tviSalesCoardinatorName = (TextView) itemView.findViewById(R.id.tviSalesCoardinatorName);
        }
    }

    @Override
    public BillingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BillingAdapter.ViewHolder holder, final int position) {
        holder.tviPurchaserName.setText(dataList.get(position).getPurchaserName());
        holder.tviSoNo.setText(dataList.get(position).getSoNo());
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviCount.setText(dataList.get(position).getCount());
        holder.tviAmount.setText(dataList.get(position).getAmount());
        holder.tviEDDDate.setText(dataList.get(position).getEddDate());
        holder.tviSalesCoardinatorName.setText(dataList.get(position).getSalesCoardinatorName());



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

