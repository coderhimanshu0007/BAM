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

public class SOAuthorizationAdapter extends RecyclerView.Adapter<SOAuthorizationAdapter.ViewHolder> {
    private ArrayList<LinkedTreeMap> dataList;
    private int card_layout;
    Activity mActivity;

    public SOAuthorizationAdapter(DashboardActivity dashboardActivityContext, ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviNo, tviCount, tviValue, tviHrsDays, tviSalesCordinator;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviNo = (TextView) itemView.findViewById(R.id.tviNo);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviValue = (TextView) itemView.findViewById(R.id.tviValue);
            this.tviHrsDays = (TextView) itemView.findViewById(R.id.tviHrsDays);
            this.tviSalesCordinator = (TextView) itemView.findViewById(R.id.tviSalesCordinator);
        }
    }

    @Override
    public SOAuthorizationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.so_authorization_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SOAuthorizationAdapter.ViewHolder holder, final int position) {
        holder.tviNo.setText(String.valueOf(dataList.get(position).get("No")));
        holder.tviCount.setText(String.valueOf(dataList.get(position).get("Count")));
        holder.tviValue.setText(String.valueOf(dataList.get(position).get("Value")));
        holder.tviHrsDays.setText(String.valueOf(dataList.get(position).get("HoursDays")));
        holder.tviSalesCordinator.setText(String.valueOf(dataList.get(position).get("SalesCordinator")));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

