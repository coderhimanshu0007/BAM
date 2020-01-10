package com.teamcomputers.bam.Adapters.Installation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class OpenCallsAdapter extends RecyclerView.Adapter<OpenCallsAdapter.ViewHolder> {
    private ArrayList<LinkedTreeMap> dataList;
    private int card_layout;
    Activity mActivity;

    public OpenCallsAdapter(DashboardActivity dashboardActivityContext, ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoice, tviCustomerName, tviAmount, tviZoneName, tviNOD, tviRemainingAmount, tviReason;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoice = (TextView) itemView.findViewById(R.id.tviInvoice);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviZoneName = (TextView) itemView.findViewById(R.id.tviZoneName);
            this.tviNOD = (TextView) itemView.findViewById(R.id.tviNOD);
            this.tviRemainingAmount = (TextView) itemView.findViewById(R.id.tviRemainingAmount);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
        }
    }

    @Override
    public OpenCallsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.installation_open_calls_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OpenCallsAdapter.ViewHolder holder, final int position) {
        holder.tviInvoice.setText(String.valueOf(dataList.get(position).get("InvoiceNo")));
        holder.tviCustomerName.setText(String.valueOf(dataList.get(position).get("CustomerName")));
        holder.tviAmount.setText(String.valueOf(dataList.get(position).get("Amount")));
        holder.tviZoneName.setText(String.valueOf(dataList.get(position).get("ZoneName")));
        holder.tviNOD.setText(String.valueOf(dataList.get(position).get("NOD")));
        holder.tviRemainingAmount.setText(String.valueOf(dataList.get(position).get("RemainingAmount")));
        holder.tviReason.setText(String.valueOf(dataList.get(position).get("Reason")));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

