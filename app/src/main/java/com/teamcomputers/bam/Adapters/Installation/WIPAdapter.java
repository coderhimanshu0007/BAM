package com.teamcomputers.bam.Adapters.Installation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.WIPModel;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class WIPAdapter extends RecyclerView.Adapter<WIPAdapter.ViewHolder> {
    private ArrayList<LinkedTreeMap> dataList;
    private int card_layout;
    Activity mActivity;

    public WIPAdapter(DashboardActivity dashboardActivityContext, ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoiceNo, tviCustomerName, tviPaymentStage, tviCount, tviAmount, tviNOD, tviRemainingAmount,tviReason,tviZoneName,tviCity;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoiceNo = (TextView) itemView.findViewById(R.id.tviInvoiceNo);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviPaymentStage = (TextView) itemView.findViewById(R.id.tviPaymentStage);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviNOD = (TextView) itemView.findViewById(R.id.tviNOD);
            this.tviRemainingAmount = (TextView) itemView.findViewById(R.id.tviRemainingAmount);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
            this.tviZoneName = (TextView) itemView.findViewById(R.id.tviZoneName);
            this.tviCity = (TextView) itemView.findViewById(R.id.tviCity);
        }
    }

    @Override
    public WIPAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wip_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WIPAdapter.ViewHolder holder, final int position) {
        holder.tviInvoiceNo.setText(String.valueOf(dataList.get(position).get("InvoiceNo")));
        holder.tviCustomerName.setText(String.valueOf(dataList.get(position).get("CustomerName")));
        holder.tviPaymentStage.setText(String.valueOf(dataList.get(position).get("PaymentStatus")));
        holder.tviCount.setText(String.valueOf(dataList.get(position).get("Count")));
        holder.tviAmount.setText(String.valueOf(dataList.get(position).get("Amount")));
        holder.tviNOD.setText(String.valueOf(dataList.get(position).get("NOD")));
        holder.tviRemainingAmount.setText(String.valueOf(dataList.get(position).get("RemainingAmount")));
        holder.tviReason.setText(String.valueOf(dataList.get(position).get("Reason")));
        holder.tviZoneName.setText(String.valueOf(dataList.get(position).get("ZoneName")));
        holder.tviCity.setText(String.valueOf(dataList.get(position).get("City")));



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

