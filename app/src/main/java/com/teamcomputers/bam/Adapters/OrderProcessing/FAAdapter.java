package com.teamcomputers.bam.Adapters.OrderProcessing;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.FAModel;
import com.teamcomputers.bam.R;

import java.util.ArrayList;
import java.util.List;

public class FAAdapter extends RecyclerView.Adapter<FAAdapter.ViewHolder> {
    private List<FAModel.Table> dataList;
    private int card_layout;
    Activity mActivity;

    public FAAdapter(DashboardActivity dashboardActivityContext, List<FAModel.Table> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<FAModel.Table> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviCustomerName, tviBu, tviCount, tviValue, tviProjectNo, tviHrsDays, tviName, tviFinanceDelay;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviBu = (TextView) itemView.findViewById(R.id.tviBu);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviValue = (TextView) itemView.findViewById(R.id.tviValue);
            this.tviProjectNo = (TextView) itemView.findViewById(R.id.tviProjectNo);
            this.tviHrsDays = (TextView) itemView.findViewById(R.id.tviHrsDays);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.tviFinanceDelay = (TextView) itemView.findViewById(R.id.tviFinanceDelay);
        }
    }

    @Override
    public FAAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fa_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FAAdapter.ViewHolder holder, final int position) {
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviBu.setText(dataList.get(position).getBU());
        holder.tviCount.setText(String.valueOf(dataList.get(position).getCount()));
        holder.tviValue.setText(String.valueOf(dataList.get(position).getValue()));
        holder.tviProjectNo.setText(dataList.get(position).getProjectNo());
        holder.tviHrsDays.setText(dataList.get(position).getHoursDays());
        holder.tviName.setText(dataList.get(position).getName());
        holder.tviFinanceDelay.setText(dataList.get(position).getFinanceDelay());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

