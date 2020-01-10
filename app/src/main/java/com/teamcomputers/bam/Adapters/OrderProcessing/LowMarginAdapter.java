package com.teamcomputers.bam.Adapters.OrderProcessing;

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

public class LowMarginAdapter extends RecyclerView.Adapter<LowMarginAdapter.ViewHolder> {
    private ArrayList<LinkedTreeMap> dataList;
    private int card_layout;
    Activity mActivity;

    public LowMarginAdapter(DashboardActivity dashboardActivityContext, ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviCustomerName, tviBu, tviCount, tviProjectNo, tviHrsDays, tviAmount, tviName, tviFinanceDelay;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviBu = (TextView) itemView.findViewById(R.id.tviBu);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviProjectNo = (TextView) itemView.findViewById(R.id.tviProjectNo);
            this.tviHrsDays = (TextView) itemView.findViewById(R.id.tviHrsDays);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.tviFinanceDelay = (TextView) itemView.findViewById(R.id.tviFinanceDelay);
        }
    }

    @Override
    public LowMarginAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.low_margin_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LowMarginAdapter.ViewHolder holder, final int position) {
        holder.tviCustomerName.setText(String.valueOf(dataList.get(position).get("CustomerName")));
        holder.tviBu.setText(String.valueOf(dataList.get(position).get("BU")));
        holder.tviCount.setText(String.valueOf(dataList.get(position).get("Count")));
        holder.tviProjectNo.setText(String.valueOf(dataList.get(position).get("ProjectNo")));
        holder.tviHrsDays.setText(String.valueOf(dataList.get(position).get("HoursDays")));
        holder.tviAmount.setText(String.valueOf(dataList.get(position).get("Amount")));
        //holder.tviName.setText(String.valueOf(dataList.get(position).get("Name")));
        holder.tviFinanceDelay.setText(String.valueOf(dataList.get(position).get("Margin")));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

