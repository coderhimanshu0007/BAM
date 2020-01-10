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

public class DOAIRAdapter extends RecyclerView.Adapter<DOAIRAdapter.ViewHolder> {
    private ArrayList<LinkedTreeMap> dataList;
    Activity mActivity;

    public DOAIRAdapter(DashboardActivity dashboardActivityContext, ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoice, tviCustomerName, tviPaymentStatus, tviCount, tviZoneName, tviReason, tviCity;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoice = (TextView) itemView.findViewById(R.id.tviInvoice);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviPaymentStatus = (TextView) itemView.findViewById(R.id.tviPaymentStatus);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviZoneName = (TextView) itemView.findViewById(R.id.tviZoneName);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
            this.tviCity = (TextView) itemView.findViewById(R.id.tviCity);
        }
    }

    @Override
    public DOAIRAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.installation_doair_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DOAIRAdapter.ViewHolder holder, final int position) {
        holder.tviInvoice.setText(String.valueOf(dataList.get(position).get("InvoiceNo")));
        holder.tviCustomerName.setText(String.valueOf(dataList.get(position).get("CustomerName")));
        holder.tviPaymentStatus.setText(String.valueOf(dataList.get(position).get("PaymentStatus")));
        holder.tviCount.setText(String.valueOf(dataList.get(position).get("Count")));
        holder.tviZoneName.setText(String.valueOf(dataList.get(position).get("ZoneName")));
        holder.tviReason.setText(String.valueOf(dataList.get(position).get("Reason")));
        holder.tviCity.setText(String.valueOf(dataList.get(position).get("City")));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

