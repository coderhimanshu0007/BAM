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

public class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.ViewHolder> {
    private ArrayList<LinkedTreeMap> dataList;
    private int card_layout;
    Activity mActivity;

    public DispatchAdapter(DashboardActivity dashboardActivityContext, ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviCustomer, tviFromCity, tviToCity, tviHrsDays, tviDispatchPending, tviDispatchPendingValue, tviInvoice;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviCustomer = (TextView) itemView.findViewById(R.id.tviCustomer);
            this.tviFromCity = (TextView) itemView.findViewById(R.id.tviFromCity);
            this.tviToCity = (TextView) itemView.findViewById(R.id.tviToCity);
            this.tviHrsDays = (TextView) itemView.findViewById(R.id.tviHrsDays);
            this.tviDispatchPending = (TextView) itemView.findViewById(R.id.tviDispatchPending);
            this.tviDispatchPendingValue = (TextView) itemView.findViewById(R.id.tviDispatchPendingValue);
            this.tviInvoice = (TextView) itemView.findViewById(R.id.tviInvoice);
        }
    }

    @Override
    public DispatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dispatch_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DispatchAdapter.ViewHolder holder, final int position) {
        holder.tviCustomer.setText(String.valueOf(dataList.get(position).get("CustomerName")));
        holder.tviFromCity.setText(String.valueOf(dataList.get(position).get("FromCity")));
        holder.tviHrsDays.setText(String.valueOf(dataList.get(position).get("HoursDays")));
        holder.tviDispatchPending.setText(String.valueOf(dataList.get(position).get("DispatchPending")));
        holder.tviDispatchPendingValue.setText(String.valueOf(dataList.get(position).get("DispatchPendingValue")));
        holder.tviInvoice.setText(String.valueOf(dataList.get(position).get("InvoiceNo")));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

