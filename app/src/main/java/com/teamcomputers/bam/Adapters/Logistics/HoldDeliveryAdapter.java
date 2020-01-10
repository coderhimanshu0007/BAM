package com.teamcomputers.bam.Adapters.Logistics;

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

public class HoldDeliveryAdapter extends RecyclerView.Adapter<HoldDeliveryAdapter.ViewHolder> {
    private ArrayList<LinkedTreeMap> dataList;
    private int card_layout;
    Activity mActivity;

    public HoldDeliveryAdapter(DashboardActivity dashboardActivityContext, ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoiceNo, tviCustomerName, tviFromCity, tviToCity, tviCourier, tviDays, tviReason, tviCount, tviValue;;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoiceNo = (TextView) itemView.findViewById(R.id.tviInvoiceNo);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviFromCity = (TextView) itemView.findViewById(R.id.tviFromCity);
            this.tviToCity = (TextView) itemView.findViewById(R.id.tviToCity);
            this.tviCourier = (TextView) itemView.findViewById(R.id.tviCourier);
            this.tviDays = (TextView) itemView.findViewById(R.id.tviDays);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviValue = (TextView) itemView.findViewById(R.id.tviValue);
        }
    }

    @Override
    public HoldDeliveryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intransit_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HoldDeliveryAdapter.ViewHolder holder, final int position) {
        holder.tviInvoiceNo.setText(String.valueOf(dataList.get(position).get("InvoiceNo")));
        holder.tviCustomerName.setText(String.valueOf(dataList.get(position).get("CustomerName")));
        holder.tviFromCity.setText(String.valueOf(dataList.get(position).get("FromCity")));
        holder.tviToCity.setText(String.valueOf(dataList.get(position).get("ToCity")));
        holder.tviCourier.setText(String.valueOf(dataList.get(position).get("AccountManager")));
        holder.tviDays.setText(String.valueOf(dataList.get(position).get("Days")));
        holder.tviReason.setText(String.valueOf(dataList.get(position).get("Reason")));
        holder.tviCount.setVisibility(View.GONE);
        holder.tviValue.setText(String.valueOf(dataList.get(position).get("Value")));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

